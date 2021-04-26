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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        session.setAttribute("paymentURL", request.getContextPath()+"/payment.html?"+request.getQueryString());

        // Return list url
        String return_url = (String) request.getSession(true).getAttribute("movieListURL");

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("return_url", return_url);

        // write all the data into the jsonObject
        response.getWriter().write(responseJsonObject.toString());
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //HttpSession session = request.getSession();
        //String sessionId = session.getId();
        //session.setAttribute("paymentURL", request.getContextPath()+"/payment.html?"+request.getQueryString());

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        boolean credentialsCorrect = false;
        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            String q_genres = "SELECT * " +
                    "FROM customers c " +
                    "WHERE c.email = ?" + " AND c.password = ?";

            PreparedStatement s1 = dbcon.prepareStatement(q_genres);
            s1.setString(1, username);
            s1.setString(2, password);

            ResultSet rs1 = s1.executeQuery();
            if(rs1.next()) credentialsCorrect = true;

            JsonObject responseJsonObject = new JsonObject();
            if (credentialsCorrect) {
                // Login success:

                // set this user into the session
                request.getSession().setAttribute("user", new User(username));

                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

            } else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");

                responseJsonObject.addProperty("message", "email or password is incorrect");
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
