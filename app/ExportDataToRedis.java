package app;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import redis.clients.jedis.Jedis;
import org.json.JSONArray;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ExportDataToRedis {

    public static void main(String[] args) {
        if (args.length != 2 || !args[0].equals("-v")) {
            System.out.println("Usage: export.sh -v /path/to/xml");
            return;
        }

        String xmlPath = args[1];
        boolean verbose = args[0].equals("-v");

        try {
            // Initialize Redis connection
            Jedis jedis = new Jedis("redis");

            // Load and parse XML file
            File xmlFile = new File(xmlPath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            // Export subdomains to Redis
            NodeList subdomainNodes = document.getElementsByTagName("subdomain");
            JSONArray subdomains = new JSONArray();
            for (int i = 0; i < subdomainNodes.getLength(); i++) {
                Element subdomainElement = (Element) subdomainNodes.item(i);
                subdomains.put(subdomainElement.getTextContent());
            }
            jedis.set("subdomains", subdomains.toString());

            // Export cookies to Redis
            NodeList cookieNodes = document.getElementsByTagName("cookie");
            for (int i = 0; i < cookieNodes.getLength(); i++) {
                Element cookieElement = (Element) cookieNodes.item(i);
                String name = cookieElement.getAttribute("name");
                String host = cookieElement.getAttribute("host");
                String value = cookieElement.getTextContent();
                jedis.set("cookie:" + name + ":" + host, value);
            }

            // Close Redis connection
            jedis.close();

            // Print keys saved to Redis if in verbose mode
            if (verbose) {
                System.out.println("Keys saved to Redis:");
                System.out.println(jedis.keys("*"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
