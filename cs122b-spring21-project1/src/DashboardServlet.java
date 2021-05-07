import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

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

@WebServlet(name = "DashboardServlet", urlPatterns = "/api/_dashboard")
public class DashboardServlet extends HttpServlet {
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
        String name = request.getParameter("name");
        String year = request.getParameter("year");

        PrintWriter out = response.getWriter();

        try {
            Connection dbcon = dataSource.getConnection();
            String id_query = "SELECT max(id) AS max FROM stars";

            PreparedStatement s1 = dbcon.prepareStatement(id_query);
            ResultSet rs1 = s1.executeQuery();
            rs1.next();
            String max_id = rs1.getString("max");
            s1.close();

            //generating new id
            int i;
            for(i = 0; i < max_id.length(); i++)
            {
                if(!Character.isAlphabetic(max_id.charAt(i)))
                    break;
            }
            String new_id = max_id.substring(0,i)+(Integer.parseInt(max_id.substring(i))+1);
            String insert_query = "INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?)";

            PreparedStatement s2 = dbcon.prepareStatement(insert_query);
            s2.setString(1, new_id);
            s2.setString(2, name);
            s2.setString(3, (year.equals("") ? null : year));

            s2.executeUpdate();

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", ""+name+" successfully added");
            response.getWriter().write(responseJsonObject.toString());

            dbcon.close();
            s2.close();
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
