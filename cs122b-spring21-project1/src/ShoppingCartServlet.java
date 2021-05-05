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
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.http.HttpSession;

// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "ShoppingCartServlet", urlPatterns = "/api/shopping-cart")
public class ShoppingCartServlet extends HttpServlet {
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

        HttpSession session = request.getSession();
        Hashtable<String, Integer> cartItems = (Hashtable<String, Integer>) session.getAttribute("cartItems");

        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            JsonArray jsonArray = new JsonArray();
            for(Map.Entry<String, Integer> entry : cartItems.entrySet())
            {
                String id = entry.getKey();
                Integer qty = entry.getValue();
                String query = "SELECT m.id, m.title, m.price FROM movies m WHERE m.id = /'?/'";

                // Declare our statement
                PreparedStatement statement = dbcon.prepareStatement(query);
                statement.setString(1,id);

                // Perform the query
                ResultSet rs = statement.executeQuery();

                String title = "";
                String price = "";
                while(rs.next())
                {
                    title = rs.getString("title");
                    price = rs.getString("price");
                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", id);
                jsonObject.addProperty("movie_title", title);
                jsonObject.addProperty("movie_price", price);
                jsonObject.addProperty("movie_qty", qty);

                jsonArray.add(jsonObject);
                rs.close();
                statement.close();
            }


            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
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
        String action = movieId.substring(0,1);
        String id = movieId.substring(1);
        System.out.println(action +" " +id);
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
            if (id != null) {
                if(!cartItems.containsKey(id)) return;
                else if(action.equals("+")) cartItems.put(id, cartItems.get(id)+1);
                else if(action.equals("-") && cartItems.get(id) > 1) cartItems.put(id, cartItems.get(id)-1);
                else if(action.equals("x")) cartItems.remove(id);
                System.out.println(cartItems.get(id));
            }
        }
    }
}
