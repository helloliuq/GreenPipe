package org.greenpipe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.greenpipe.bean.Connector;

public class ConnectorDAO {
	private static InitialContext context = null;
	private DataSource dataSource = null;

	private static final String SELECT_ALL_SQL = "select * from connector where wfid=?";
	private static final String INSERT_NEW_SQL = "insert into connector(wfid,origin,destination) values(?,?,?)";

	/**
	 * Constructor
	 */
	public ConnectorDAO() {
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
	 * Get connectors by wfid
	 * @param wfid
	 * @return List<Connector>
	 */
	public List<Connector> getConnectorsByWfid(int wfid) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		List<Connector> connectorList = new ArrayList<Connector>();
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(SELECT_ALL_SQL);
			ps.setInt(1, wfid);
			rst = ps.executeQuery();

			while(rst.next()) {
				Connector connector = new Connector();

				connector.setId(rst.getInt("id"));
				connector.setWfid(rst.getInt("wfid"));
				connector.setOrigin(rst.getString("origin"));
				connector.setDestination(rst.getString("destination"));
				connector.setUsername(rst.getString("username"));

				connectorList.add(connector);
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

		return connectorList;
	}

	public boolean insertNewConnector(Connector connector) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(INSERT_NEW_SQL);
			ps.setInt(1, connector.getWfid());
			ps.setString(2, connector.getOrigin());
			ps.setString(3, connector.getDestination());
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
