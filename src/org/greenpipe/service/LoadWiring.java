package org.greenpipe.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.greenpipe.bean.Wiring;
import org.greenpipe.dao.WiringDAO;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

/**
 * Servlet implementation class LoadWiring
 */
@WebServlet("/LoadWiring")
public class LoadWiring extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoadWiring() {
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
		 * Send the json-form string back to the requested ajax
		 */
		String jsonString = queryFromDatabase();
		out.println(jsonString);	
	}

	/**
	 * Get all wirings and transform them to a json-form string
	 * @return String
	 */
	protected String queryFromDatabase() {

		//声明一个JSON字符串
		String jsonString = "";
		jsonString = jsonString + "[" + "\n";

		//声明一个WiringDAO对象，，用于处理与数据库的交互
		WiringDAO wiringDAO = new WiringDAO();
		List<Wiring> wiringList = wiringDAO.getAllWirings();

		for(int i = 0; i < wiringList.size(); i++) {
			if(i != 0) {
				jsonString = jsonString + ",\n";
			}

			Wiring wiring = wiringList.get(i);

			jsonString = jsonString + JSONObject.fromObject(wiring).toString();
		}

		jsonString = jsonString + "\n]";

		return jsonString;
	}
}
