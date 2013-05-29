package org.greenpipe.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.greenpipe.bean.Block;
import org.greenpipe.bean.Connector;
import org.greenpipe.bean.Wiring;
import org.greenpipe.bean.Workflow;
import org.greenpipe.dao.BlockDAO;
import org.greenpipe.dao.ConnectorDAO;
import org.greenpipe.dao.WiringDAO;
import org.greenpipe.dao.WorkflowDAO;
import org.greenpipe.engine.JobQueue;
import org.greenpipe.util.XmlParser;

import test.DataSimulator;

/**
 * Servlet implementation class RunWiring
 */
@WebServlet("/RunWiring")
public class RunWiring extends HttpServlet {
	private static final long serialVersionUID = 1L;
	JobQueue jobQueue = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RunWiring() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("111");
		jobQueue = new JobQueue();
		jobQueue.start();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		jobQueue = null;
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
		 * Get the name parameter of the workflow
		 */
		String name = request.getParameter("name");

		/*
		 * Get the xml-form string and parse it into object
		 */
		String xml = readXmlFromDatabase(name);
		XmlParser xmlParser = new XmlParser();
		List list = xmlParser.parseXml(xml);

		/*
		 * Add a new workflow record into database
		 */
		int wfid = addNewWorkflowRecord(name);

		/*
		 * Add the new workflow details into database
		 */
		addNewWorkflowDetail(wfid, list);

		System.out.println("job start ha ha!!!!");
		
		//jobQueue = new JobQueue();
		//jobQueue.start();
		/*
		System.out.println("1");
		request.setAttribute("id", wfid);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/servlet/DataSimulator");
		System.out.println("2");
		dispatcher.forward(request, response);
		System.out.println("3");
		 */
	}

	/**
	 * Get a xml-form string of this workflow by name
	 * @param name
	 * @return String
	 */
	protected String readXmlFromDatabase(String name) {

		String xmlString = "";

		WiringDAO wiringDAO = new WiringDAO();
		Wiring wiring = wiringDAO.getWiringByName(name);
		if(wiring != null) {
			xmlString = wiring.getXml();
		}

		return xmlString;
	}

	/**
	 * Add a new workflow record into database
	 * @param name
	 * @return int (the new workflow id)
	 */
	protected synchronized int addNewWorkflowRecord(String name) {
		WorkflowDAO workflowDAO = new WorkflowDAO();

		workflowDAO.insertNewWorkflow(name);
		Workflow workflow = workflowDAO.getLatestWorkflow();

		return workflow.getId();
	}

	/**
	 * Add the new workflow's blocks and connectors into database
	 * @param wfid
	 * @param list
	 */
	protected void addNewWorkflowDetail(int wfid, List list) {
		BlockDAO blockDAO = new BlockDAO();
		ConnectorDAO connectorDAO = new ConnectorDAO();

		List<Connector> connectorList = (List<Connector>) list.get(0);
		List<Block> blockList = (List<Block>) list.get(1);

		for(int i = 0; i < blockList.size(); i++) {
			Block block = blockList.get(i);
			block.setWfid(wfid);
			blockDAO.insertNewBlock(block);
		}

		for(int i = 0; i < connectorList.size(); i++) {
			Connector connector = connectorList.get(i);
			connector.setWfid(wfid);
			connectorDAO.insertNewConnector(connector);
		}
	}
}
