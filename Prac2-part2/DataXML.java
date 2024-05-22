import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
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
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("record");
            if (nList.getLength() == 0) {
                System.out.println("No records found in the XML file.");
                return;
            }

            JSONArray recordsArray = new JSONArray();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    JSONObject recordObject = new JSONObject();

                    if (selectedFields.contains("name")) {
                        recordObject.put("name", getElementTextContent(eElement, "name"));
                    }
                    if (selectedFields.contains("postalzip")) {
                        recordObject.put("postalZip", getElementTextContent(eElement, "postalZip"));
                    }
                    if (selectedFields.contains("region")) {
                        recordObject.put("region", getElementTextContent(eElement, "region"));
                    }
                    if (selectedFields.contains("country")) {
                        recordObject.put("country", getElementTextContent(eElement, "country"));
                    }
                    if (selectedFields.contains("address")) {
                        recordObject.put("address", getElementTextContent(eElement, "address"));
                    }
                    if (selectedFields.contains("list")) {
                        recordObject.put("list", getElementTextContent(eElement, "list"));
                    }

                    recordsArray.put(recordObject);
                }
            }

            // Output the JSON array
            System.out.println(recordsArray.toString(4)); // Pretty print with an indent of 4
        } catch (Exception e) {
            System.out.println("An error occurred while processing the XML file.");
            e.printStackTrace();
        }
    }

    private static String getElementTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        } else {
            return "N/A"; // Or handle as appropriate for missing fields
        }
    }
}
