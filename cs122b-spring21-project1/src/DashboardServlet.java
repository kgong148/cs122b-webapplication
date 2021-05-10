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
import java.sql.CallableStatement;
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
        PrintWriter out = response.getWriter();

        try {
            Connection dbcon = dataSource.getConnection();
            JsonArray jsonArray = new JsonArray();

            String tables_query = "SHOW tables";

            PreparedStatement s1 = dbcon.prepareStatement(tables_query);
            ResultSet rs1 = s1.executeQuery();
            while(rs1.next())
            {
                String tablename = rs1.getString("Tables_in_moviedb");
                String attribute_query = "DESCRIBE "+tablename;
                PreparedStatement s2 = dbcon.prepareStatement(attribute_query);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("tablename", tablename);

                ResultSet rs2 = s2.executeQuery();
                int count = 0;
                while(rs2.next())
                {
                    jsonObject.addProperty("table_attribute_name_"+count, rs2.getString("Field"));
                    jsonObject.addProperty("table_attribute_type_"+count++, rs2.getString("Type"));
                }
                jsonObject.addProperty("length", count);
                jsonArray.add(jsonObject);
                s2.close();
            }
            out.write(jsonArray.toString());
            s1.close();
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
        PrintWriter out = response.getWriter();
        if(request.getParameter("director") != null)
        {
            String title = request.getParameter("title");
            String year = request.getParameter("year");
            String director = request.getParameter("director");
            String price = request.getParameter("price");
            String star_name = request.getParameter("star_name");
            String genre_name = request.getParameter("genre_name");

            try {
                Connection dbcon = dataSource.getConnection();
                String query = "{CALL add_movie(?,?,?,?,?,?)}";

                CallableStatement s1 = dbcon.prepareCall(query);
                s1.setString(1, title);
                s1.setString(2, year);
                s1.setString(3, director);
                s1.setString(4, price);
                s1.setString(5, star_name);
                s1.setString(6, genre_name);
                s1.executeQuery();
                s1.close();

                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", ""+title+" successfully added");
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

        String name = request.getParameter("name");
        String year = request.getParameter("year");

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
