package se.playtech.updater.impl;


import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import se.playtech.updater.util.XMLUpdater;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLUpdaterImpl implements XMLUpdater {
    private String xmlFilePath;
    private Log log;
    private Document doc;

    public XMLUpdaterImpl(String path, Log log) {
        if(!new File(path).exists()){
            throw new IllegalArgumentException("File does not exist at: " + path);
        }
        try {
            this.xmlFilePath = path;
            this.log = log;
            File inputFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(inputFile);
            if(doc != null){
                doc.getDocumentElement().normalize();
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
   }


    public void UpdateXML() {
        boolean isUpdated = UpdateVersion();
        boolean isCoreRemoved = removeCore();
        if(isUpdated || isCoreRemoved){
           writeToFile();
        }
    }

    public boolean UpdateVersion() {
        NodeList nList = doc.getElementsByTagName("plugin");
        for (int temp = 0; temp < nList.getLength(); temp++) {
           Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if(eElement.getElementsByTagName("version").item(0) == null){
                    log.info("Please check the xml file. There is no element as version");
                    return false;
                }
                if (eElement.getElementsByTagName("artifactId").item(0).getTextContent() != "packer" && Double.parseDouble(eElement
                        .getElementsByTagName("version")
                        .item(0)
                        .getTextContent()) < 2) {
                    eElement.getElementsByTagName("version").item(0).setTextContent("2.1");
                    log.info("Packer version updated");
                    return true;
                }
                else{
                    log.info("Packer version already updated");
                    return false;
                }

            }
        }
    return false;
    }

    public boolean removeCore() {
        Element element = (Element)doc.getElementsByTagName("core").item(0);
        if(element != null){
            Node parent = element.getParentNode();
            parent.removeChild(element);
            log.info("Core tag successfully removed");
            return true;
        }
        log.info("Core tag does not exist");
        return false;
    }


    public void writeToFile() {
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(xmlFilePath));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

}
