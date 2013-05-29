package org.greenpipe.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XmlGenerator {

	public XmlGenerator() {}

	/**
	 * Transform a json-form string to a xml-form string
	 * @param json
	 * @return String
	 */
	public String transformJsonToXml(String json) {
		String xml = "";
		try {
			//声明一个XML序列化对象
			XMLSerializer xmlSerializer = new XMLSerializer();
			//用JSON序列化对象将传入的Json字符串转化为一个JSON对象
			JSON jsonObject = JSONSerializer.toJSON(json);
			//用XML序列化对象将JSON对象转化为一个xml格式的字符串
			xml = xmlSerializer.write(jsonObject);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return xml;
	}

	/**
	 * Write a xml-form string to a file
	 * @param fileName
	 * @param xml
	 */
	public void writeToFile(String fileName, String xml) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			bw.write(xml);
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Normalize a xml-form string
	 * @param xmlString
	 * @return String
	 */
	public String generateXml(String json) {

		/*
		 * Transform a json-form string to a xml-form string
		 */
		String xml = transformJsonToXml(json);

		/*
		 * Write the xml to a file
		 */
		String fileName = "normalizeXml.xml";
		writeToFile(fileName, xml);

		/*
		 * Use DocumentBuilder to parse the xml file
		 */
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document document = null;
		try {
			db = dbf.newDocumentBuilder();
			document = db.parse(fileName);
		} catch(ParserConfigurationException pe) {
			System.out.println(pe.getMessage());
		} catch(SAXException se) {
			System.out.println(se.getMessage());
		} catch(IOException ie) {
			System.out.println(ie.getMessage());
		}

		/*
		 * Create a new xml string
		 */
		String newXmlString = "";
		newXmlString = newXmlString + "<WF>" + "\n\n";
		String firstLevel = "   ";
		String secondLevel = "      ";
		String thirdLevel = "         ";

		/*
		 * Get the root element of this xml tree
		 */
		Element root = document.getDocumentElement();

		/*
		 * Normalize the block nodes
		 */
		NodeList blockNodes = root.getElementsByTagName("block");
		for(int i = 0; i < blockNodes.getLength(); i++) {
			newXmlString = newXmlString + firstLevel + "<block>" + "\n";

			Element blockNode = (Element) blockNodes.item(i);

			//Name node
			Element nameNode =  (Element) (blockNode.getElementsByTagName("name")).item(0);
			String name = "";
			Text nameText = (Text) nameNode.getFirstChild();
			if(nameText != null) {
				name = nameText.getNodeValue();
			}
			newXmlString = newXmlString + secondLevel + "<name>" + name + "</name>" + "\n";

			//Command node
			Element commandNode = (Element) (blockNode.getElementsByTagName("command")).item(0);
			String command = "";
			Text commandText = (Text) commandNode.getFirstChild();
			if(commandText != null) {
				command = commandText.getNodeValue();
			}
			newXmlString = newXmlString + secondLevel + "<command>" + command + "</command>" + "\n";

			//Input node
			NodeList inputNodes = blockNode.getElementsByTagName("input");
			for(int j = 0; j < inputNodes.getLength(); j++) {
				newXmlString = newXmlString + secondLevel + "<input>" + "\n";

				Element inputNode = (Element) inputNodes.item(j);

				Element locationTypeNode = (Element) (inputNode.getElementsByTagName("locationType")).item(0);
				String locationType = "";
				Text locationTypeText = (Text) locationTypeNode.getFirstChild();
				if(locationTypeText != null) {
					locationType = locationTypeText.getNodeValue();
				}
				newXmlString = newXmlString + thirdLevel + "<locationType>" + locationType + "</locationType>" + "\n";

				Element locationNode = (Element) (inputNode.getElementsByTagName("location")).item(0);
				String location = "";
				Text locationText = (Text) locationNode.getFirstChild();
				if(locationText != null) {
					location = locationText.getNodeValue();
				}
				newXmlString = newXmlString + thirdLevel + "<location>" + location + "</location>" + "\n";

				Element dataTypeNode = (Element) (inputNode.getElementsByTagName("dataType")).item(0);
				String dataType = "";
				Text dataTypeText = (Text) dataTypeNode.getFirstChild();
				if(dataTypeText != null) {
					dataType = dataTypeText.getNodeValue();
				}
				newXmlString = newXmlString + thirdLevel + "<dataType>" + dataType + "</dataType>" + "\n";

				newXmlString = newXmlString + secondLevel + "</input>" + "\n";
			}

			//Output node
			Element outputNode = (Element) (blockNode.getElementsByTagName("output")).item(0);

			newXmlString = newXmlString + secondLevel + "<output>" + "\n";

			Element locationTypeNode = (Element) (outputNode.getElementsByTagName("locationType")).item(0);
			String locationType = "";
			Text locationTypeText = (Text) locationTypeNode.getFirstChild();
			if(locationTypeText != null) {
				locationType = locationTypeText.getNodeValue();
			}
			newXmlString = newXmlString + thirdLevel + "<locationType>" + locationType + "</locationType>" + "\n";

			Element locationNode = (Element) (outputNode.getElementsByTagName("location")).item(0);
			String location = "";
			Text locationText = (Text) locationNode.getFirstChild();
			if(locationText != null) {
				location = locationText.getNodeValue();
			}
			newXmlString = newXmlString + thirdLevel + "<location>" + location + "</location>" + "\n";

			Element dataTypeNode = (Element) (outputNode.getElementsByTagName("dataType")).item(0);
			String dataType = "";
			Text dataTypeText = (Text) dataTypeNode.getFirstChild();
			if(dataTypeText != null) {
				dataType = dataTypeText.getNodeValue();
			}
			newXmlString = newXmlString + thirdLevel + "<dataType>" + dataType + "</dataType>" + "\n";

			newXmlString = newXmlString + secondLevel + "</output>" + "\n";


			newXmlString = newXmlString + firstLevel + "</block>" + "\n\n";	
		}

		newXmlString = newXmlString + "\n";

		/*
		 * Normalize the connector nodes
		 */
		NodeList wireNodes = root.getElementsByTagName("wire");
		for(int i = 0; i < wireNodes.getLength(); i++) {
			newXmlString = newXmlString + firstLevel + "<connector>" +"\n";

			Element wireNode =  (Element) wireNodes.item(i);

			//Origin node
			Element originNode = (Element) (wireNode.getElementsByTagName("origin")).item(0);
			String origin = "";
			Text originText = (Text) originNode.getFirstChild();
			if(originText != null) {
				origin = originText.getNodeValue();
			}
			newXmlString = newXmlString + secondLevel + "<origin>" + origin + "</origin>" + "\n";

			//Destination node
			Element destinationNode = (Element) (wireNode.getElementsByTagName("destination")).item(0);
			String destination = "";
			Text destinationText = (Text) destinationNode.getFirstChild();
			if(destinationText != null) {
				destination = destinationText.getNodeValue();
			}
			newXmlString = newXmlString + secondLevel + "<destination>" + destination + "</destination>" + "\n";

			newXmlString =newXmlString + firstLevel + "</connector>" + "\n\n";
		}

		newXmlString = newXmlString + "</WF>";

		/*
		 * Return the new xml string
		 */
		return newXmlString;
	}
}
