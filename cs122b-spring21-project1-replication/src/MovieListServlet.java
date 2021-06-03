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
import java.util.Hashtable;
import javax.servlet.http.HttpSession;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        HttpSession session = request.getSession();
        session.setAttribute("movieListURL", request.getContextPath()+"/movie-list.html?"+request.getQueryString());
        //System.out.println(request.getContextPath()+"/movie-list.html?"+request.getQueryString());
        response.setContentType("application/json"); // Response mime type
        // Add shopping cart URL
        String shopping_cart_url = (String) request.getSession(true).getAttribute("indexURL");


        String genre = request.getParameter("genre");
        String startsWith = request.getParameter("startsWith");
        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String stars = request.getParameter("stars");
        String amount_s = request.getParameter("amount");
        String order = request.getParameter("order");
        String page = request.getParameter("page");

        if(page == null) page = "1";
        if(amount_s == null) amount_s = "10";

        if(order == null || order.equals("rating")) order = "r.rating DESC, m.title ASC";
        else order = "m.title ASC, r.rating DESC";

        String conditions = "";

        if(genre != null || startsWith != null)
        {
            if(genre != null)
                conditions += " AND g.name LIKE '%" + genre + "%' ";
            else if(startsWith.equals("*"))
                conditions += " AND m.title REGEXP '^[^0-9A-Za-z]' ";
            else
                conditions += " AND m.title LIKE '" + startsWith + "%' ";
        }
        else {
            if (title != null)
            {
                //MATCH(title) AGAINST (? IN BOOLEAN MODE)
                String title_str = "";
                for(String str : title.split(" "))
                {
                    title_str += "+" + str +"* ";
                }
                conditions += " AND MATCH(m.title) AGAINST ('" + title_str + "' IN BOOLEAN MODE) ";
            }
            if (year != null) conditions += " AND m.year = " + year + " ";
            if (director != null) conditions += " AND m.director LIKE '%" + director + "%' ";
            if (stars != null) conditions += " AND s.name LIKE '%" + stars + "%' ";
        }

        int offset = (Integer.parseInt(page)-1)*Integer.parseInt(amount_s);
        int amount = Integer.parseInt(amount_s)+1;

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            String query = "";
            if(genre != null) {
                query = "SELECT DISTINCT m.title, m.year, m.director, m.id, r.rating FROM movies m JOIN (ratings r) ON (m.id =r.movieId), stars_in_movies sim, stars s, genres_in_movies gim, genres g WHERE m.id = sim.movieID AND sim.starId = s.id AND gim.movieId = m.id AND g.id = gim.genreId"+conditions+" ORDER BY "+order+" LIMIT ? OFFSET ?";
            }
            else {
                query = "SELECT DISTINCT m.title, m.year, m.director, m.id, r.rating FROM movies m JOIN (ratings r) ON (m.id =r.movieId), stars_in_movies sim, stars s WHERE m.id = sim.movieID AND sim.starId = s.id"+conditions+" ORDER BY "+order+" LIMIT ? OFFSET ?";
            }
            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            statement.setInt(1, amount);
            statement.setInt(2, offset);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            int count = 0;
            while (rs.next()) {
                count++;

                String movie_id = rs.getString("id");
                String movie_title = rs.getString("title");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String movie_rating = rs.getString("rating");

                //Add 3 genres and 3 stars
                String q1 = "SELECT DISTINCT g.name " +
                        "FROM movies m, genres_in_movies gim, genres g " +
                        "WHERE m.id = gim.movieId AND gim.genreId = g.id " +
                        "AND m.id =? "+
                        "ORDER BY g.name LIMIT 3";

                PreparedStatement s1 = dbcon.prepareStatement(q1);
                s1.setString(1, movie_id);

                // Perform the query
                ResultSet rs_t1 = s1.executeQuery();

                String q2 = "SELECT DISTINCT sim1.starId, s.name " +
                        "FROM stars_in_movies sim1, stars_in_movies sim2, movies m, stars s " +
                        "WHERE m.id = sim1.movieId AND sim1.starId = sim2.starId AND sim1.starId = s.id " +
                        "AND sim1.movieId = ? " +
                        "GROUP BY sim1.starId " +
                        "ORDER BY COUNT(sim2.movieId) DESC, s.name ASC " +
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
                jsonObject.addProperty("movie_rating", movie_rating);
                // Add shopping cart URL to jsonObject
                jsonObject.addProperty("shopping_cart_url", shopping_cart_url);
                for(int i = 0; i < 3; i++)
                {
                    if(rs_t1.next())
                        jsonObject.addProperty("movie_genre_" + i, rs_t1.getString("name"));
                    else
                        jsonObject.addProperty("movie_genre_" + i, "");
                    if(rs_t2.next())
                    {
                        jsonObject.addProperty("movie_stars_" + i, rs_t2.getString("name"));
                        jsonObject.addProperty("movie_stars_id_" + i, rs_t2.getString("starId"));
                    }
                    else {
                        jsonObject.addProperty("movie_stars_" + i, "");
                        jsonObject.addProperty("movie_stars_id_" + i, "");
                    }
                }
                jsonArray.add(jsonObject);

                if(count >= amount-1) break;
            }
            JsonObject jsonObject = new JsonObject();
            if(rs.next())
                jsonObject.addProperty("EndOfQuery", false);
            else
                jsonObject.addProperty("EndOfQuery", true);
            jsonArray.add(jsonObject);

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

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String movieId = request.getParameter("movieId");
        System.out.println(movieId);
        HttpSession session = request.getSession();

        // Retrieve data named "previousItems" from session
        Hashtable<String, Integer> cartItems = (Hashtable<String, Integer>) session.getAttribute("cartItems");

        // If "previousItems" is not found on session, means this is a new user, thus we create a new previousItems
        // ArrayList for the user
        if (cartItems == null) {

            // Add the newly created ArrayList to session, so that it could be retrieved next time
            cartItems = new Hashtable<>();
            session.setAttribute("cartItems", cartItems);
        }

        synchronized (cartItems) {
            if (movieId != null) {
                if(cartItems.containsKey(movieId)) cartItems.put(movieId, cartItems.get(movieId)+1);
                else cartItems.put(movieId, 1);
                System.out.println(cartItems.get(movieId));
            }
        }
    }
}
