package org.greenpipe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.greenpipe.bean.Wiring;

public class WiringDAO {
	private static InitialContext context = null;
	private DataSource dataSource = null;

	private static final String SELECT_ALL_SQL = "select * from wiring";
	private static final String SELECT_ONE_BY_NAME_SQL = "select * from wiring where name=?";
	private static final String INSERT_ONE_SQL = "insert into wiring(name,json,xml) values(?,?,?)";

	/**
	 * Constructor
	 */
	public WiringDAO() {
		try {
			if(context == null) {
				context = new InitialContext();
			}
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/workflow");
		} catch(NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get a database connection
	 * @Method getConnection
	 * @return Connection
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * Get all wirings
	 * @return List<Wiring>
	 */
	public List<Wiring> getAllWirings() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		List<Wiring> wiringList = new ArrayList<Wiring>();
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(SELECT_ALL_SQL);
			rst = ps.executeQuery();

			while(rst.next()) {
				Wiring wiring = new Wiring();

				wiring.setId(rst.getInt("id"));
				wiring.setName(rst.getString("name"));
				wiring.setJson(rst.getString("json"));
				wiring.setXml(rst.getString("xml"));
				wiring.setUsername(rst.getString("username"));

				wiringList.add(wiring);
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rst.close();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return wiringList;
	}

	/**
	 * Get a wiring by name
	 * @param name
	 * @return Wiring
	 */
	public Wiring getWiringByName(String name) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		Wiring wiring = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(SELECT_ONE_BY_NAME_SQL);
			ps.setString(1, name);
			rst = ps.executeQuery();

			if(rst.next()) {
				wiring = new Wiring();
				
				wiring.setId(rst.getInt("id"));
				wiring.setName(rst.getString("name"));
				wiring.setJson(rst.getString("json"));
				wiring.setXml(rst.getString("xml"));
				wiring.setUsername(rst.getString("username"));
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rst.close();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return wiring;
	}

	/**
	 * Insert into a new wiring
	 * @param wiring
	 * @return boolean
	 */
	public boolean insertOneWiring(Wiring wiring) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(INSERT_ONE_SQL);
			ps.setString(1, wiring.getName());
			ps.setString(2, wiring.getJson());
			ps.setString(3, wiring.getXml());
			ps.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return true;
	}
}
