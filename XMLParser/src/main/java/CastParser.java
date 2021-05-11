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

public class CastParser extends DefaultHandler {

    List<Cast> myCast;

    private String tempVal;

    //to maintain context
    private Cast tempCast;

    public CastParser() {
        myCast = new ArrayList<Cast>();
    }

    public void runExample() throws SQLException {
        parseDocument();
        // test to make sure right data is pulled
        //printData();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("casts124.xml", this);

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

        System.out.println("No of Casts '" + myCast.size() + "'.");
        Iterator<Cast> it = myCast.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("filmc")) {
            //create a new instance of actor
            tempCast = new Cast();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        // To parse cast124
        // Add cast to myCasts array
        if (qName.equalsIgnoreCase("filmc")) {
            //add it to the list
            myCast.add(tempCast);
        } else if (qName.equalsIgnoreCase("a")) {
            // Open csv file and write to it as its being parsed
            try {
                FileWriter csvWriter = new FileWriter("CastParser.csv", true);
                // get  actor id
                int tempID = 0;
                for(int j = 0; j < tempVal.length(); j++){
                    tempID += tempVal.charAt(j);
                }
                csvWriter.append("" + tempID);
                //csvWriter.append(tempVal);
                csvWriter.append(",");
                csvWriter.append(tempCast.getMovieId());
                csvWriter.append("\n");

                //Close csv file
                csvWriter.flush();
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Add actor to Cast member list of movie
            tempCast.setCastMember(tempVal);
        } else if (qName.equalsIgnoreCase("f")) {
            // Set films id
            tempCast.setMovieId(tempVal);
        }
    }

    public static void main(String[] args) throws SQLException {
        CastParser spe = new CastParser();
        spe.runExample();
    }
}