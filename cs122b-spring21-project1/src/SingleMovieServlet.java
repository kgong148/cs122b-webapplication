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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            String q_genres = "SELECT DISTINCT g.name " +
                    "FROM movies m, genres_in_movies gim, genres g " +
                    "WHERE m.id = gim.movieId AND gim.genreId = g.id " +
                    "AND m.id =? ORDER BY g.name";

            PreparedStatement s1 = dbcon.prepareStatement(q_genres);
            s1.setString(1, id);

            ResultSet rs1 = s1.executeQuery();

            // Construct a query with parameter represented by "?"
            //String query = "SELECT * from stars as s, stars_in_movies as sim, movies as m, ratings as r where s.id = sim.starId and sim.movieId = m.id and r.movieId = m.id and m.id = ?";
            String query = "SELECT DISTINCT s.name, sim.movieId, m.title, m.year, m.director, m.price, " +
                    "r.rating, sim.starId, MA.ma " +
                    "from stars as s, stars_in_movies as sim, stars_in_movies as sim2, " +
                    "movies as m, ratings as r, " +
                    "(SELECT id, COUNT(movieId) AS ma " +
                    "FROM stars, stars_in_movies " +
                    "WHERE starId = id " +
                    "GROUP BY name) as MA " +
                    "where s.id = sim.starId " +
                    "and sim.movieId = m.id " +
                    "and r.movieId = m.id " +
                    "and sim2.starId = s.id " +
                    "and MA.id = s.id " +
                    "and m.id = ? " +
                    "ORDER BY MA.ma DESC, s.name ASC";


            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            //adding genres
            JsonObject jsonObject_g = new JsonObject();
            int count = 0;
            rs.next();
            String movieId = rs.getString("movieId");
            String movieTitle = rs.getString("title");
            String movieYear = rs.getString("year");
            String movieDirector = rs.getString("director");
            String movieRating = rs.getString("rating");
            String moviePrice = rs.getString("price");

            String return_url = (String) request.getSession(true).getAttribute("movieListURL");
            String shopping_cart_url = (String) request.getSession(true).getAttribute("indexURL");
            System.out.println(shopping_cart_url);
            jsonObject_g.addProperty("movie_id", movieId);
            jsonObject_g.addProperty("movie_title", movieTitle);
            jsonObject_g.addProperty("movie_year", movieYear);
            jsonObject_g.addProperty("movie_director", movieDirector);
            jsonObject_g.addProperty("movie_rating", movieRating);
            jsonObject_g.addProperty("movie_price", moviePrice);
            jsonObject_g.addProperty("star_id", rs.getString("starId"));
            jsonObject_g.addProperty("star_name", rs.getString("name"));
            jsonObject_g.addProperty("return_url", return_url);
            jsonObject_g.addProperty("shopping_cart_url", shopping_cart_url);


            rs1.next();
            int i = 0;
            while(rs1.next())
            {
                jsonObject_g.addProperty("movie_genres_"+ i++,rs1.getString("name"));
            }
            jsonObject_g.addProperty("genre_size",i);
//            String movie_genres = rs1.getString("name");
//            while(rs1.next())
//            {
//                movie_genres += ", " + rs1.getString("name");
//            }
//            jsonObject_g.addProperty("movie_genres",movie_genres);
            jsonArray.add(jsonObject_g);

            // Iterate through each row of rs
            while (rs.next()) {

                String starId = rs.getString("starId");
                String starName = rs.getString("name");

                // Create a JsonObject based on the data we retrieve from rs

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("star_id", starId);
                jsonObject.addProperty("star_name", starName);

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
