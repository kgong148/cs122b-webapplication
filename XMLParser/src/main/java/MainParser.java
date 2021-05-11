import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MainParser {

    public static void addData() throws Exception {
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?allowLoadLocalInfile=true";

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        Statement statement = connection.createStatement();

        // Get a connection from dataSource
        // add to movie table
        String query = "LOAD DATA INFILE 'ActorParser.csv' \n" +
                "INTO TABLE stars \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";

        String query2 = "LOAD LOCAL DATA INFILE 'CastParser.csv' \n" +
                "INTO TABLE stars_in_movies \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";

        String query3 = "LOAD LOCAL DATA INFILE 'genre.csv' \n" +
                "INTO TABLE genres \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";

        String query4 = "LOAD LOCAL DATA INFILE 'genres_in_movies.csv' \n" +
                "INTO TABLE genres_in_movies \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";

        String query5 = "LOAD LOCAL DATA INFILE 'MovieParser.csv' \n" +
                "INTO movies \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";

//        statement.executeUpdate(query);
//        statement.executeUpdate(query2);
//        statement.executeUpdate(query3);
//        statement.executeUpdate(query4);
//        statement.executeUpdate(query5);


        statement.close();
        connection.close();
    }
    public static void main(String[] args) throws Exception {
        ActorParser spe = new ActorParser();
        spe.runExample();
        MovieParser spe2 = new MovieParser();
        spe2.runExample();
        CastParser spe3 = new CastParser();
        spe3.runExample();

        addData();
    }
}
