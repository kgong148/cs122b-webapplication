import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MainParser {

    public static void addData() throws SQLException {
         DataSource dataSource;

         // Connect to database
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");

            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            // add to movie table
            String query = "LOAD DATA INFILE 'ActorParser.csv' \n" +
                    "INTO TABLE stars \n" +
                    "FIELDS TERMINATED BY ',' \n" +
                    "LINES TERMINATED BY '\\n'\n" +
                    "IGNORE 1 ROWS;";

            String query2 = "LOAD DATA INFILE 'CastParser.csv' \n" +
                    "INTO TABLE stars_in_movies \n" +
                    "FIELDS TERMINATED BY ',' \n" +
                    "LINES TERMINATED BY '\\n'\n" +
                    "IGNORE 1 ROWS;";

            String query3 = "LOAD DATA INFILE 'genre.csv' \n" +
                    "INTO TABLE genres \n" +
                    "FIELDS TERMINATED BY ',' \n" +
                    "LINES TERMINATED BY '\\n'\n" +
                    "IGNORE 1 ROWS;";

            String query4 = "LOAD DATA INFILE 'genres_in_movies.csv' \n" +
                    "INTO TABLE genres_in_movies \n" +
                    "FIELDS TERMINATED BY ',' \n" +
                    "LINES TERMINATED BY '\\n'\n" +
                    "IGNORE 1 ROWS;";

            String query5 = "LOAD DATA INFILE 'MovieParser.csv' \n" +
                    "INTO movies \n" +
                    "FIELDS TERMINATED BY ',' \n" +
                    "LINES TERMINATED BY '\\n'\n" +
                    "IGNORE 1 ROWS;";
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws SQLException {
        ActorParser spe = new ActorParser();
        spe.runExample();
        MovieParser spe2 = new MovieParser();
        spe2.runExample();
        CastParser spe3 = new CastParser();
        spe3.runExample();

        addData();
    }
}
