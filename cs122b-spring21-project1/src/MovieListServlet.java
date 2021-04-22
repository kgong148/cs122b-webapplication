import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "MovieListServlet", urlPatterns = "/api/movie-list")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        String genre = request.getParameter("genre");
        String startsWith = request.getParameter("startsWith");
        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String stars = request.getParameter("stars");

        String conditions = "";

        if(genre != null || startsWith != null)
        {
            if(genre != null)
                conditions += " AND g.name LIKE '%" + genre + "%' ";
            else
                conditions += " AND m.title LIKE '" + startsWith + "%' ";
        }
        else {
            if (title != null) conditions += " AND m.title LIKE '%" + title + "%' ";
            if (year != null) conditions += " AND m.year = " + year + " ";
            if (director != null) conditions += " AND m.director LIKE '%" + director + "%' ";
            if (stars != null) conditions += " AND s.name LIKE '%" + stars + "%' ";
        }
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement
            Statement statement = dbcon.createStatement();

            String query = "";
            if(genre != null)
                query = "SELECT DISTINCT m.title, m.year, m.director, m.id FROM movies m JOIN (ratings r) ON (m.id =r.movieId), stars_in_movies sim, stars s, genres_in_movies gim, genres g WHERE m.id = sim.movieID AND sim.starId = s.id AND gim.movieId = m.id AND g.id = gim.genreId" +conditions+ " ORDER BY r.rating DESC, m.title ASC LIMIT 10 OFFSET 0";
            else
                query = "SELECT DISTINCT m.title, m.year, m.director, m.id FROM movies m JOIN (ratings r) ON (m.id =r.movieId), stars_in_movies sim, stars s WHERE m.id = sim.movieID AND sim.starId = s.id" +conditions+ " ORDER BY r.rating DESC, m.title ASC LIMIT 10 OFFSET 0";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {
                String movie_id = rs.getString("id");
                //System.out.println(rs.getString("title"));
                String movie_title = rs.getString("title");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");

                //Add 3 genres and 3 stars
                String q1 = "SELECT DISTINCT g.name " +
                        "FROM movies m, genres_in_movies gim, genres g " +
                        "WHERE m.id = gim.movieId AND gim.genreId = g.id " +
                        "AND m.id =? "+
                        "LIMIT 3";

                PreparedStatement s1 = dbcon.prepareStatement(q1);
                s1.setString(1, movie_id);

                // Perform the query
                ResultSet rs_t1 = s1.executeQuery();

                String q2 = "SELECT DISTINCT s.name, s.id " +
                        "FROM movies m, stars_in_movies sim, stars s " +
                        "WHERE m.id = sim.movieId AND sim.starId = s.id " +
                        "AND m.id =? "+
                        "LIMIT 3";

                PreparedStatement s2 = dbcon.prepareStatement(q2);
                s2.setString(1, movie_id);

                // Perform the query
                ResultSet rs_t2 = s2.executeQuery();

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);

                for(int i = 0; i < 3; i++)
                {
                    if(rs_t1.next())
                        jsonObject.addProperty("movie_genre_" + i, rs_t1.getString("name"));
                    else
                        jsonObject.addProperty("movie_genre_" + i, "");
                    if(rs_t2.next())
                    {
                        jsonObject.addProperty("movie_stars_" + i, rs_t2.getString("name"));
                        jsonObject.addProperty("movie_stars_id_" + i, rs_t2.getString("id"));
                    }
                    else {
                        jsonObject.addProperty("movie_stars_" + i, "");
                        jsonObject.addProperty("movie_stars_id_" + i, "");
                    }
                }


                jsonArray.add(jsonObject);
            }

            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}
