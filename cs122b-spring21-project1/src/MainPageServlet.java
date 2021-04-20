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
import java.sql.Statement;

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
        //response.setContentType("text/html");    // Response mime type

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("title", title);
        responseJsonObject.addProperty("year", year);
        responseJsonObject.addProperty("director", director);
        responseJsonObject.addProperty("stars", stars);

        response.getWriter().write(responseJsonObject.toString());

        // Output stream to STDOUT
        //PrintWriter out = response.getWriter();

        // Building page head with title
        //out.println("<html><head><title>MovieDB: Found Records</title></head>");

        // Building page body
        //out.println("<body><h1>MovieDB: Found Records</h1>");


//        try {
//            // Create a new connection to database
//            Connection dbCon = dataSource.getConnection();
//
//            // Declare a new statement
//            Statement statement = dbCon.createStatement();
//
//            // Generate a SQL query
//            String query = "SELECT * from movies m, stars_in_movies sim, stars s WHERE "
//                         + "m.id = sim.movieId AND sim.starId = s.id "
//                         + "AND m.title LIKE "+title;
//            if(!year.isEmpty()) query += (" AND m.year = " + year);
//            query += " AND m.director LIKE "+director + " AND s.name LIKE "+stars;
//
//            // Perform the query
//            ResultSet rs = statement.executeQuery(query);
//
//            // Create a html <table>
//            out.println("<table border>");
//
//            // Iterate through each row of rs and create a table row <tr>
//            out.println("<tr><td>ID</td><td>Name</td></tr>");
//            while (rs.next()) {
//                String m_ID = rs.getString("ID");
//                String m_Name = rs.getString("name");
//                out.println(String.format("<tr><td>%s</td><td>%s</td></tr>", m_ID, m_Name));
//            }
//            out.println("</table>");
//
//
//            // Close all structures
//            rs.close();
//            statement.close();
//            dbCon.close();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//
//            // Output Error Massage to html
//            out.println(String.format("<html><head><title>MovieDB: Error</title></head>\n<body><p>SQL error in doGet: %s</p></body></html>", ex.getMessage()));
//            return;
//        }
//        out.close();
    }
}
