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

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedb,
 * generates output as a html <table>
 */

// Declaring a WebServlet called FormServlet, which maps to url "/form"
@WebServlet(name = "TitleSuggestionServlet", urlPatterns = "/title-suggestion")
public class TitleSuggestionServlet extends HttpServlet {

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
        String title_str = "";
        for(String str : title.split(" "))
        {
            title_str += "+" + str +"* ";
        }
        JsonArray jsonArray = new JsonArray();

        PrintWriter out = response.getWriter();

        // Output stream to STDOUT

        try {
            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();

            // Generate a SQL query
            String query = "SELECT m.title, m.id FROM movies m WHERE MATCH(title) AGAINST (? IN BOOLEAN MODE) LIMIT 10";

            // Declare a new statement
            PreparedStatement statement = dbCon.prepareStatement(query);
            statement.setString(1, title_str);
            // Perform the query
            ResultSet rs = statement.executeQuery();

            while(rs.next())
            {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("value", rs.getString("title"));
                jsonObject.addProperty("data", rs.getString("id"));
                jsonArray.add(jsonObject);
            }

            out.write(jsonArray.toString());

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