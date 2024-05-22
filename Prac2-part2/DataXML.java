import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
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

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("record");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    if (selectedFields.contains("name")) {
                        System.out.println("Name: " + eElement.getElementsByTagName("name").item(0).getTextContent());
                    }
                    if (selectedFields.contains("postalzip")) {
                        System.out.println("Postal Zip: " + eElement.getElementsByTagName("postalZip").item(0).getTextContent());
                    }
                    if (selectedFields.contains("region")) {
                        System.out.println("Region: " + eElement.getElementsByTagName("region").item(0).getTextContent());
                    }
                    if (selectedFields.contains("country")) {
                        System.out.println("Country: " + eElement.getElementsByTagName("country").item(0).getTextContent());
                    }
                    if (selectedFields.contains("address")) {
                        System.out.println("Address: " + eElement.getElementsByTagName("address").item(0).getTextContent());
                    }
                    if (selectedFields.contains("list")) {
                        System.out.println("List: " + eElement.getElementsByTagName("list").item(0).getTextContent());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
