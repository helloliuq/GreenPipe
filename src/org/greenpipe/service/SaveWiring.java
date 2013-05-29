package org.greenpipe.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.greenpipe.bean.Wiring;
import org.greenpipe.dao.WiringDAO;
import org.greenpipe.util.XmlGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Servlet implementation class SaveWiring
 */
@WebServlet("/SaveWiring")
public class SaveWiring extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SaveWiring() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("gb2312");
		response.setContentType("text/xml;charset=gb2312");
		PrintWriter out=response.getWriter();

		/*
		 * Get the parameters of this wiring
		 */
		String name = request.getParameter("nameValue");
		String editorJson = request.getParameter("editorValue");
		String workflowJson = request.getParameter("workflowValue");

		/*
		 * Generate the workflow xml file
		 */
		XmlGenerator xmlGenerator = new XmlGenerator();
		String workflowXml = xmlGenerator.generateXml(workflowJson);

		/*
		 * Store this wiring to database
		 */
		String message = storeToDatabase(name, editorJson, workflowXml);
		out.println(message);
	}

	/**
	 * Insert this wiring into database
	 * @param name
	 * @param json
	 * @param xml
	 * @return String
	 */
	protected String storeToDatabase(String name, String json, String xml) {
		String message = "";

		WiringDAO wiringDAO = new WiringDAO();
		if(wiringDAO.getWiringByName(name) == null) {
			Wiring wiring = new Wiring();

			wiring.setName(name);
			wiring.setJson(json);
			wiring.setXml(xml);

			wiringDAO.insertOneWiring(wiring);
			message = "Save Successful";

		} else {
			message = "Duplicated Name, Please Rename Your Workflow";
		}

		return message;
	}

}
