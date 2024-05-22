import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXParser;
import org.xml.sax.SAXParserFactory;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ReadXMLFile {
    public static void main(String[] args) {
        // Check if user provided fields as command-line arguments
        if (args.length == 0) {
            System.out.println("Please specify the fields to be printed (e.g., name postalZip region).");
            return;
        }

        // Create a set of selected fields for quick lookup
        Set<String> selectedFields = new HashSet<>();
        for (String field : args) {
            selectedFields.add(field.toLowerCase());
        }

        // Validate the input fields
        Set<String> validFields = Set.of("name", "postalzip", "region", "country", "address", "list");
        for (String field : selectedFields) {
            if (!validFields.contains(field)) {
                System.out.println("Invalid field: " + field);
                System.out.println("Valid fields are: name, postalZip, region, country, address, list");
                return;
            }
        }

        try {
            File inputFile = new File("data.xml");
            if (!inputFile.exists()) {
                System.out.println("The file data.xml does not exist.");
                return;
            }

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            UserSelectedFieldsHandler handler = new UserSelectedFieldsHandler(selectedFields);
            saxParser.parse(inputFile, handler);

            // Output the JSON array
            System.out.println(handler.getRecordsArray().toString(4)); // Pretty print with an indent of 4
        } catch (Exception e) {
            System.out.println("An error occurred while processing the XML file.");
            e.printStackTrace();
        }
    }
}

class UserSelectedFieldsHandler extends DefaultHandler {
    private Set<String> selectedFields;
    private JSONArray recordsArray = new JSONArray();
    private JSONObject currentRecord;
    private StringBuilder currentValue;
    private boolean inRecord;

    public UserSelectedFieldsHandler(Set<String> selectedFields) {
        this.selectedFields = selectedFields;
        this.currentValue = new StringBuilder();
        this.inRecord = false;
    }

    public JSONArray getRecordsArray() {
        return recordsArray;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentValue.setLength(0); // Clear the current value
        if (qName.equalsIgnoreCase("record")) {
            currentRecord = new JSONObject();
            inRecord = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentValue.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (inRecord) {
            String tagValue = currentValue.toString().trim();
            String lowerQName = qName.toLowerCase();
            if (selectedFields.contains(lowerQName)) {
                currentRecord.put(lowerQName.equals("postalzip") ? "postalZip" : lowerQName, tagValue);
            }
            if (qName.equalsIgnoreCase("record")) {
                recordsArray.put(currentRecord);
                inRecord = false;
            }
        }
    }
}
