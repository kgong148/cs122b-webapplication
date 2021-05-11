import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class MovieParser extends DefaultHandler {

    List<Movie> myMovies;
    List<String> myGenres;

    private String tempVal;
    private String currentDir;

    //to maintain context
    private Movie tempMov;
    private Pattern isNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");

    public MovieParser() {
        myMovies = new ArrayList<Movie>();
        myGenres = new ArrayList<String>();
    }

    public void runExample() throws IOException {
        parseDocument();
        // test to make sure right data is pulled
        //printData();
        //Add to genre csv file
        FileWriter csvWriter = new FileWriter("genre.csv", true);
        for(int i = 0; i < myGenres.size(); i++){
            int tempID = 0;
            for(int j = 0; j < myGenres.get(i).length(); j++){
                tempID += myGenres.get(i).charAt(j);
            }
            csvWriter.append("" + tempID);
            csvWriter.append(",");
            csvWriter.append(myGenres.get(i));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() {

        System.out.println("No of Movies '" + myMovies.size() + "'.");
        Iterator<Movie> it = myMovies.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("director")) {
            //reset current director
            currentDir = "";
        }
        if (qName.equalsIgnoreCase("film")) {
            //create a new instance of movie
            tempMov = new Movie();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        // To parse mains243
        // Add movie to myMovies array
        if (qName.equalsIgnoreCase("film")) {
            //add it to the list
            myMovies.add(tempMov);

            // Open csv file and append movie info
            try {
                FileWriter csvWriter = new FileWriter("MovieParser.csv", true);
                csvWriter.append(tempMov.getId());
                csvWriter.append(",");
                csvWriter.append(tempMov.getTitle());
                csvWriter.append(",");
                csvWriter.append("" + tempMov.getYear());
                csvWriter.append(",");
                csvWriter.append("" + tempMov.getDirector());
                csvWriter.append(",");
                csvWriter.append("" + 10);
                csvWriter.append("\n");

                //Close csv file
                csvWriter.flush();
                csvWriter.close();

                // append to ratings file
                csvWriter = new FileWriter("ratings.csv", true);
                csvWriter.append(tempMov.getId());
                csvWriter.append("\n");

                //Close csv file
                csvWriter.flush();
                csvWriter.close();

                // append to genre csv file
                /*
                if(tempMov.getGenreId() != 0 && myGenres.contains(tempVal) != t) {
                    csvWriter = new FileWriter("genre.csv", true);
                    csvWriter.append("" + tempMov.getGenreId());
                    csvWriter.append(",");
                    csvWriter.append(tempMov.getGenre());
                    csvWriter.append("\n");

                    //Close csv file
                    csvWriter.flush();
                    csvWriter.close();
                }*/
                    // append to genres in movies csv file
                if(tempMov.getGenreId() != 0) {
                    csvWriter = new FileWriter("genres_in_movies.csv", true);
                    csvWriter.append("" + tempMov.getGenreId());
                    csvWriter.append(",");
                    csvWriter.append("" + tempMov.getId());
                    csvWriter.append("\n");

                    //Close csv file
                    csvWriter.flush();
                    csvWriter.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (qName.equalsIgnoreCase("dirname")) {
            // Set current director once since it covers many films
            currentDir = tempVal;
        } else if (qName.equalsIgnoreCase("year")) {
            if(isNumeric.matcher(tempVal).matches())
                tempMov.setYear(Integer.parseInt(tempVal));
        } else if (qName.equalsIgnoreCase("fid")) {
            tempMov.setId(tempVal);
            //Also set the director
            tempMov.setDirector(currentDir);
        } else if (qName.equalsIgnoreCase("cat")) {
            // Set genre id. id = sum of ascii chars
            int tempID = 0;
            for(int i = 0; i < tempVal.length(); i++){
                tempID += tempVal.charAt(i);
            }
            // Remove dups and null genres
            if(myGenres.contains(tempVal) != true && tempID !=0){
                myGenres.add(tempVal);
            }

            if(tempID == 0) {
                tempMov.setGenre("null");
                tempMov.setGenreId(tempID);
            }
            else{
                tempMov.setGenre(tempVal);
                tempMov.setGenreId(tempID);
            }
        }else if (qName.equalsIgnoreCase("t")) {
            tempMov.setTitle(tempVal);
        }

    }

    public static void main(String[] args) throws IOException {
        MovieParser spe = new MovieParser();
        spe.runExample();
    }

}

