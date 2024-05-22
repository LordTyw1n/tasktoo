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

        try {
            File inputFile = new File("data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("record");
            JSONArray recordsArray = new JSONArray();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    JSONObject recordObject = new JSONObject();

                    if (selectedFields.contains("name")) {
                        recordObject.put("name", eElement.getElementsByTagName("name").item(0).getTextContent());
                    }
                    if (selectedFields.contains("postalzip")) {
                        recordObject.put("postalZip", eElement.getElementsByTagName("postalZip").item(0).getTextContent());
                    }
                    if (selectedFields.contains("region")) {
                        recordObject.put("region", eElement.getElementsByTagName("region").item(0).getTextContent());
                    }
                    if (selectedFields.contains("country")) {
                        recordObject.put("country", eElement.getElementsByTagName("country").item(0).getTextContent());
                    }
                    if (selectedFields.contains("address")) {
                        recordObject.put("address", eElement.getElementsByTagName("address").item(0).getTextContent());
                    }
                    if (selectedFields.contains("list")) {
                        recordObject.put("list", eElement.getElementsByTagName("list").item(0).getTextContent());
                    }

                    recordsArray.put(recordObject);
                }
            }

            // Output the JSON array
            System.out.println(recordsArray.toString(4)); // Pretty print with an indent of 4
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
