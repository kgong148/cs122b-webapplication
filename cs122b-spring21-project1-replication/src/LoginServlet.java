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
import org.jasypt.util.password.StrongPasswordEncryptor;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
//        Boolean isAndroid = (request.getParameter("type") != null);

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

//        if(!isAndroid) {
            //Check recaptcha response
//            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
//            System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
//
//            // Verify reCAPTCHA
//            try {
//                RecaptchaVerifyUtils.verify(gRecaptchaResponse);
//            } catch (Exception e) {
//                JsonObject responseJsonObject = new JsonObject();
//                responseJsonObject.addProperty("status", "fail");
//                responseJsonObject.addProperty("message", "recaptcha verification error");
//                response.getWriter().write(responseJsonObject.toString());
//                out.close();
//                return;
//            }
//        }
        boolean credentialsCorrect = false;
        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            String q_genres = "SELECT * " +
                    "FROM customers c " +
                    "WHERE c.email = ?";

            PreparedStatement s1 = dbcon.prepareStatement(q_genres);
            s1.setString(1, username);

            ResultSet rs1 = s1.executeQuery();
            String userId = "";
            if(rs1.next()) {
                //credentialsCorrect = true;
                userId = rs1.getString("id");
                String encryptedPassword = rs1.getString("password");
                credentialsCorrect = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);

            }

            JsonObject responseJsonObject = new JsonObject();
            if (credentialsCorrect) {
                // Login success:

                // set this user into the session
                request.getSession().setAttribute("user", new User(username, userId));

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