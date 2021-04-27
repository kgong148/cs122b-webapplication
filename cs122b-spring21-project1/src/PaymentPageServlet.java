import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Hashtable;
import java.util.Map;

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedb,
 * generates output as a html <table>
 */

// Declaring a WebServlet called FormServlet, which maps to url "/form"
@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment-page")
public class PaymentPageServlet extends HttpServlet {

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        Hashtable<String, Integer> cartItems = (Hashtable<String, Integer>) session.getAttribute("cartItems");

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            JsonArray jsonArray = new JsonArray();
            double total_price = 0;
            for(Map.Entry<String, Integer> entry : cartItems.entrySet()) {
                String id = entry.getKey();
                Integer qty = entry.getValue();
                String q_genres = "SELECT m.price FROM movies m WHERE m.id = ?";

                PreparedStatement s1 = dbcon.prepareStatement(q_genres);
                s1.setString(1, id);

                ResultSet rs = s1.executeQuery();
                while(rs.next())
                {
                    total_price+=Double.parseDouble(rs.getString("price"))*qty;
                }
                rs.close();
                s1.close();
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("total_price", total_price);
            jsonArray.add(jsonObject);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fname = request.getParameter("firstName");
        String lname = request.getParameter("lastName");
        String cnum = request.getParameter("CCN");
        String expdate = request.getParameter("expirationDate");

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        boolean credentialsCorrect = false;
        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            String query = "SELECT * " +
                    "FROM creditcards c " +
                    "WHERE c.id = ? AND c.firstName = ? AND c.lastName = ? AND c.expiration = ?";

            PreparedStatement s = dbcon.prepareStatement(query);
            s.setString(1, cnum);
            s.setString(2, fname);
            s.setString(3, lname);
            s.setString(4, expdate);

            ResultSet rs = s.executeQuery();
            if(rs.next()) credentialsCorrect = true;

            JsonObject responseJsonObject = new JsonObject();
            if (credentialsCorrect) {
                // Login success:
                HttpSession session = request.getSession(true);
                Hashtable<String, Integer> cartItems = (Hashtable<String, Integer>) session.getAttribute("cartItems");
                if (cartItems == null) {

                    // Add the newly created ArrayList to session, so that it could be retrieved next time
                    cartItems = new Hashtable<>();
                    session.setAttribute("cartItems", cartItems);
                }
                for(Map.Entry<String, Integer> entry : cartItems.entrySet())
                {
                    String id = entry.getKey();
                    Integer qty = entry.getValue();

                    for(int i = 0; i < qty; i++)
                    {
                        String insert_query = "INSERT INTO sales (customerId, movieId, saleDate) VALUES (?, ?, ?)";
                        PreparedStatement s1 = dbcon.prepareStatement(insert_query);
                        s1.setString(1, ((User)session.getAttribute("user")).userId);
                        s1.setString(2, id);
                        s1.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));

                        s1.executeUpdate();
                    }
                }

                cartItems = new Hashtable<>();
                session.setAttribute("cartItems", cartItems);
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

            } else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");

                responseJsonObject.addProperty("message", "Information is incorrect");
            }
            response.getWriter().write(responseJsonObject.toString());
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
