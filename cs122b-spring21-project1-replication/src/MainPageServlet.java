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

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedb,
 * generates output as a html <table>
 */

// Declaring a WebServlet called FormServlet, which maps to url "/form"
@WebServlet(name = "FormServlet", urlPatterns = "/api/main-page")
public class MainPageServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
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
        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String stars = request.getParameter("stars");
        String shopping_cart_url = (String) request.getSession(true).getAttribute("indexURL");
        //response.setContentType("text/html");    // Response mime type

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("title", title);
        responseJsonObject.addProperty("year", year);
        responseJsonObject.addProperty("director", director);
        responseJsonObject.addProperty("stars", stars);
        responseJsonObject.addProperty("shopping_cart_url", shopping_cart_url);

        PrintWriter out = response.getWriter();


        // Output stream to STDOUT


        try {
            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();

            // Generate a SQL query
            String query = "SELECT g.name from genres g";

            // Declare a new statement
            PreparedStatement statement = dbCon.prepareStatement(query);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            int count = 0;
            while(rs.next())
            {
                responseJsonObject.addProperty("genre_"+count++, rs.getString("name"));
            }
            responseJsonObject.addProperty("size", count);

            out.write(responseJsonObject.toString());

            // Close all structures
            rs.close();
            statement.close();
            dbCon.close();

        } catch (Exception ex) {
            ex.printStackTrace();

            // Output Error Massage to html
            out.println(String.format("<html><head><title>MovieDB: Error</title></head>\n<body><p>SQL error in doGet: %s</p></body></html>", ex.getMessage()));
            return;
        }
        out.close();
    }
}
