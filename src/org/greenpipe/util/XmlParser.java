package org.greenpipe.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.greenpipe.bean.Block;
import org.greenpipe.bean.Connector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XmlParser {

	public XmlParser(){}
	
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
	 * Parse the normalized xml file
	 * @param xml
	 * @return List contains block list and connector list
	 */
	public List parseXml(String xml) {

		/*
		 * Create block list and connector list
		 */
		List list = new ArrayList();
		List<Block> blockList = new ArrayList<Block>();
		List<Connector> connectorList = new ArrayList<Connector>();

		/*
		 * Write the xml to a file
		 */
		String fileName = "parseXml.xml";
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
		 * Get the root element of this xml tree
		 */
		Element root = document.getDocumentElement();

		/*
		 * Create a hashmap to store the blocks' name and their dependency
		 */
		HashMap<String,Integer> blockMap = new HashMap<String,Integer>();
		NodeList blockNodes = root.getElementsByTagName("block");
		for(int i = 0; i < blockNodes.getLength(); i++) {
			Element blockNode = (Element)blockNodes.item(i);
			Element nameNode = (Element) (blockNode.getElementsByTagName("name")).item(0);
			String blockName = "";
			Text nameText = (Text) nameNode.getFirstChild();
			if(nameText != null) {
				blockName = nameText.getNodeValue();
			}
			blockMap.put(blockName, 0);
		}

		/*
		 * Parse the connector nodes
		 */
		NodeList connectorNodes = root.getElementsByTagName("connector");
		for(int i = 0; i < connectorNodes.getLength(); i++) {

			Connector connector = new Connector();
			String origin = "";
			String destination = "";

			Element connectorNode = (Element) connectorNodes.item(i);

			//origin
			Element originNode = (Element) (connectorNode.getElementsByTagName("origin")).item(0);
			Text originText = (Text) originNode.getFirstChild();
			if(originText != null) {
				origin = originText.getNodeValue();
			}
			connector.setOrigin(origin);

			//destination
			Element destinationNode = (Element) (connectorNode.getElementsByTagName("destination")).item(0);
			Text destinationText = (Text) destinationNode.getFirstChild();
			if(destinationText != null) {
				destination = destinationText.getNodeValue();
			}
			connector.setDestination(destination);

			blockMap.put(destination, blockMap.get(destination) + 1);

			connectorList.add(connector);
		}
		list.add(connectorList);

		/*
		 * Parse the block nodes
		 */
		for(int i = 0; i < blockNodes.getLength(); i++) {

			Block block = new Block();
			String blockName = "";
			int dependency = 0;
			String command = "";
			String input = "";
			String output = "";

			Element blockNode = (Element) blockNodes.item(i);

			//name
			Element nameNode = (Element) (blockNode.getElementsByTagName("name")).item(0);
			Text nameText = (Text) nameNode.getFirstChild();
			if(nameText != null) {
				blockName = nameText.getNodeValue();
			}
			block.setName(blockName);

			//dependency
			dependency = blockMap.get(blockName);
			block.setDependency(dependency);

			//command
			Element commandNode = (Element) (blockNode.getElementsByTagName("command")).item(0);
			Text commandText = (Text) commandNode.getFirstChild();
			if(commandText != null) {
				command = commandText.getNodeValue();
			}
			block.setCommand(command);

			//input
			NodeList inputNodes = blockNode.getElementsByTagName("input");
			if(block.getDependency() == 0 && inputNodes.getLength() > 0) {
				Element inputNode = (Element) inputNodes.item(0);
				Element inputDataTypeNode = (Element) (inputNode.getElementsByTagName("dataType")).item(0);
				Text inputDataTypeText = (Text) inputDataTypeNode.getFirstChild();
				if(inputDataTypeText != null) {
					input = inputDataTypeText.getNodeValue();
				}
			} 
			block.setInput(input);

			//output
			Element outputNode = (Element) (blockNode.getElementsByTagName("output")).item(0);
			Element outputDataTypeNode = (Element) (outputNode.getElementsByTagName("dataType")).item(0);
			Text outputDataTypeText = (Text) outputDataTypeNode.getFirstChild();
			if(outputDataTypeText != null) {
				output = outputDataTypeText.getNodeValue();
			}
			block.setOutput(output);

			blockList.add(block);
		}
		list.add(blockList);

		return list;
	}
}
