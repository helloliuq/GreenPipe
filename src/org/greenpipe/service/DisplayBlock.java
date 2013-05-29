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

import org.greenpipe.bean.Block;
import org.greenpipe.dao.BlockDAO;

/**
 * Servlet implementation class DisplayBlock
 */
@WebServlet("/DisplayBlock")
public class DisplayBlock extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DisplayBlock() {
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

		String wfid = request.getParameter("id");
		String json = getBlocksByWfid( Integer.parseInt(wfid) );

		/*
		 * Send the json-form string back to the requested ajax
		 */
		out.println(json);
	}

	/**
	 * Get blocks by wfid and transform them into a json-form string
	 * @param wfid
	 * @return String
	 */
	protected String getBlocksByWfid(int wfid) {
		String json = "";
		json = json + "[" + "\n";

		BlockDAO blockDAO = new BlockDAO();
		List<Block> blockList = blockDAO.getBlocksByWfid(wfid);

		for(int i = 0; i < blockList.size(); i++) {
			if(i != 0 ) {
				json = json + ",\n";
			}

			Block block = blockList.get(i);
			json = json + JSONObject.fromObject(block).toString();
		}

		json = json + "\n]";

		return json;
	}

}
