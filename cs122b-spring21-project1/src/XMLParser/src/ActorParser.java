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

public class ActorParser extends DefaultHandler {

    List<Actor> myActors;

    private String tempVal;

    //to maintain context
    private Actor tempAct;

    public ActorParser() {
        myActors = new ArrayList<Actor>();
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
            sp.parse("actors63.xml", this);

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

        System.out.println("No of Actors '" + myActors.size() + "'.");
        Iterator<Actor> it = myActors.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("actor")) {
            //create a new instance of actor
            tempAct = new Actor();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        // To parse actor63
        // Add actor to myActors array
        if (qName.equalsIgnoreCase("actor")) {
            //add it to the list
            myActors.add(tempAct);

            // Open csv file and append actors info
            try {
                FileWriter csvWriter = new FileWriter("ActorParser.csv", true);
                csvWriter.append("" + tempAct.getActorID());
                csvWriter.append(",");
                csvWriter.append(tempAct.getName());
                csvWriter.append(",");
                csvWriter.append("" + tempAct.getDob());
                csvWriter.append("\n");

                //Close csv file
                csvWriter.flush();
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (qName.equalsIgnoreCase("stagename")) {
            // Set actors name
            tempAct.setName(tempVal);
            // Set actor id. id = sum of ascii chars
            int tempID = 0;
            for(int i = 0; i < tempVal.length(); i++){
                tempID += tempVal.charAt(i);
            }
            tempAct.setActorID("" + tempID);
        } else if (qName.equalsIgnoreCase("dob")) {
            // Set actors birth date
            tempAct.setDob(Integer.parseInt(tempVal));
        }
    }

    public static void main(String[] args) throws SQLException {
        ActorParser spe = new ActorParser();
        spe.runExample();
    }

}