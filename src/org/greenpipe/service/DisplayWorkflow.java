package org.greenpipe.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.greenpipe.bean.Workflow;
import org.greenpipe.dao.WorkflowDAO;

/**
 * Servlet implementation class DisplayWorkflow
 */
@WebServlet("/DisplayWorkflow")
public class DisplayWorkflow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayWorkflow() {
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
		PrintWriter out = response.getWriter();
		
		String json = null;
		String id = null;
		id = request.getParameter("id");
		
		if(id == null) {
			json = getAllWorkflow();
		} else {
			json = getSingleWorkflow( Integer.parseInt(id) );
		}
		
		/*
		 * Send the json-form string back to the requested ajax
		 */
		out.println(json);
	}
	
	/**
	 * Get all workflows and transform them into a json-form string
	 * @return String
	 */
	protected String getAllWorkflow() {
		String json = "";
		json = json + "[" + "\n";
		
		WorkflowDAO workflowDAO = new WorkflowDAO();
		List<Workflow> workflowList = workflowDAO.getAllWorkflow();
		
		for(int i = 0; i < workflowList.size(); i++) {
			if(i != 0 ) {
				json = json + ",\n";
			}
			Workflow workflow = workflowList.get(i);
			json = json + JSONObject.fromObject(workflow).toString();
		}
		
		json = json + "\n]";
		
		return json;
	}

	/**
	 * Get a workflow by id and transform it into a json-form string
	 * @param id
	 * @return String
	 */
	protected String getSingleWorkflow(int id) {
		String json = "";
		
		WorkflowDAO workflowDAO = new WorkflowDAO();
		Workflow workflow = workflowDAO.getWorkflowById(id);
		
		if(workflow != null) {
			json = json + JSONObject.fromObject(workflow).toString();
		}
		
		System.out.println(json);
		
		return json;
	}
}
