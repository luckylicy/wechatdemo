package com.bnlr.lrparking.util;


import com.blankj.utilcode.util.LogUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * description: TODO
 * XmlUtil class
 *
 * @author : Licy
 * @date : 2018/12/19
 */
public class XmlUtil {

    public static String mapToXml(HashMap<String, String> data) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()){
            String value = data.get(key);
            if (value == null){
                value = "";
            }
            value = value.trim();
            Element field = document.createElement(key);
            field.appendChild(document.createTextNode(value));
            root.appendChild(field);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();

        writer.close();

        return output;
    }


    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            LogUtils.i("Invalid XML, can not convert to map. Error message: {}. XML content: {}",ex.getMessage(), strXML);
            throw ex;
        }

    }

}
