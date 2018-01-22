import java.io.File;
import java.math.BigDecimal;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class FromXMLfile {

    static int[][] getAllUserNames(String fileName) {
        int [][]k;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            File file = new File(fileName);
            if (file.exists()) {
                Document doc = db.parse(file);
                Element docEle = doc.getDocumentElement();


                NodeList vertices = docEle.getElementsByTagName("vertex");
                k=new int[vertices.getLength()][vertices.getLength()];
                String c;
                if (vertices.getLength() > 0) {
                    for (int i = 0; i < vertices.getLength(); i++) {

                        Node node = vertices.item(i);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {

                            Element e = (Element) node;

                            NodeList nodeList = e.getElementsByTagName("edge");

                            for (int j=0;j<vertices.getLength()-1;j++) {
                                c = nodeList.item(j).getAttributes().getNamedItem("cost").getNodeValue();
                                float val = new BigDecimal(c).floatValue();
                                int qwe=(int)val;
                                if (j>=i)
                                    k[i][j+1]=qwe;

                                else
                                    k[i][j]=qwe;
                            }

                        }
                    }
                    return k;
                }

                else {
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }



}