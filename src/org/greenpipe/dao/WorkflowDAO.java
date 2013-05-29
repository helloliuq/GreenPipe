package org.greenpipe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.greenpipe.bean.Workflow;
import org.greenpipe.util.Status;

public class WorkflowDAO {
	private static InitialContext context = null;
	private DataSource dataSource = null;
	private static final SimpleDateFormat shortFormatter = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat longFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String SELECT_ALL_SQL = "select * from workflow";
	private static final String SELECT_ONE_BY_ID_SQL = "select * from workflow where id=?";
	private static final String SELECT_NEW_SQL = "select * from workflow where status=?";
	private static final String SELECT_LATEST_SQL = "select * from workflow order by id desc limit 0,1";
	private static final String INSERT_ONE_SQL = "insert into workflow(name) values(?)";
	private static final String UPDATE_STATUS_SQL = "update workflow set status=? where id=?";
	private static final String UPDATE_START_TIME_SQL = "update workflow set start_time=str_to_date(?, '%Y-%m-%d %k:%i:%s') where id=?";
	private static final String UPDATE_RUNNING_TIME_SQL = "update workflow set running_time=str_to_date(?, '%k:%i:%s') where id=?";
	private static final String UPDATE_FINISH_TIME_SQL = "update workflow set finish_time=str_to_date(?, '%Y-%m-%d %k:%i:%s') where id=?";
	/**
	 * Constructor
	 */
	public WorkflowDAO() {
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
	 * Get all workflow
	 * @return List<Workflow>
	 */
	public List<Workflow> getAllWorkflow() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		List<Workflow> workflowList = new ArrayList<Workflow>();
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(SELECT_ALL_SQL);
			rst = ps.executeQuery();

			while(rst.next()) {
				Workflow workflow = new Workflow();

				workflow.setId(rst.getInt("id"));
				workflow.setName(rst.getString("name"));
				workflow.setUsername(rst.getString("username"));
				workflow.setStatus(rst.getInt("status"));
				if(rst.getTimestamp("start_time") != null) {
					Date date = new Date(rst.getTimestamp("start_time").getTime());
					workflow.setStartTime(longFormatter.format(date));
				}
				if(rst.getTimestamp("running_time") != null) {
					Date date = new Date(rst.getTimestamp("running_time").getTime());
					workflow.setRunningTime(shortFormatter.format(date));
				}
				if(rst.getTimestamp("finish_time") != null) {
					Date date = new Date(rst.getTimestamp("finish_time").getTime());
					workflow.setFinishTime(longFormatter.format(date));
				}

				workflowList.add(workflow);
			}

			rst = null;
			ps = null;
			conn = null;
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
		}

		return workflowList;
	}

	/**
	 * Get workflow by id
	 * @param id
	 * @return Workflow
	 */
	public Workflow getWorkflowById(int id) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		Workflow workflow = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(SELECT_ONE_BY_ID_SQL);
			ps.setInt(1, id);
			rst = ps.executeQuery();

			if(rst.next()) {
				workflow = new Workflow();

				workflow.setId(rst.getInt("id"));
				workflow.setName(rst.getString("name"));
				workflow.setUsername(rst.getString("username"));
				workflow.setStatus(rst.getInt("status"));
				if(rst.getTimestamp("start_time") != null) {
					Date date = new Date(rst.getTimestamp("start_time").getTime());
					workflow.setStartTime(longFormatter.format(date));
				}
				if(rst.getTimestamp("running_time") != null) {
					Date date = new Date(rst.getTimestamp("running_time").getTime());
					workflow.setRunningTime(shortFormatter.format(date));
				}
				if(rst.getTimestamp("finish_time") != null) {
					Date date = new Date(rst.getTimestamp("finish_time").getTime());
					workflow.setFinishTime(longFormatter.format(date));
				}

			}
			
			rst.close();
			ps.close();
			conn.close();
			
			rst = null;
			ps = null;
			conn = null;
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
		}

		return workflow;
	}

	/**
	 * Get the new Workflows
	 * @return List<Workflow>
	 */
	public List<Workflow> getNewWorkflow() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		List<Workflow> workflowList = new ArrayList<Workflow>();
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(SELECT_NEW_SQL);
			ps.setInt(1, Status.WAITING);
			rst = ps.executeQuery();

			while(rst.next()) {
				Workflow workflow = new Workflow();

				workflow.setId(rst.getInt("id"));
				workflow.setName(rst.getString("name"));
				workflow.setUsername(rst.getString("username"));
				workflow.setStatus(rst.getInt("status"));
				if(rst.getTimestamp("start_time") != null) {
					Date date = new Date(rst.getTimestamp("start_time").getTime());
					workflow.setStartTime(longFormatter.format(date));
				}
				if(rst.getTimestamp("running_time") != null) {
					Date date = new Date(rst.getTimestamp("running_time").getTime());
					workflow.setRunningTime(shortFormatter.format(date));
				}
				if(rst.getTimestamp("finish_time") != null) {
					Date date = new Date(rst.getTimestamp("finish_time").getTime());
					workflow.setFinishTime(longFormatter.format(date));
				}
				BlockDAO blockDAO = new BlockDAO();
				workflow.setBlockList(blockDAO.getBlocksByWfid(workflow.getId()));
				ConnectorDAO connectorDAO = new ConnectorDAO();
				workflow.setConnectorList(connectorDAO.getConnectorsByWfid(workflow.getId()));

				workflowList.add(workflow);
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

		return workflowList;
	}

	/**
	 * Get the latest new workflow
	 * @return Workflow
	 */
	public Workflow getLatestWorkflow() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		Workflow workflow = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(SELECT_LATEST_SQL);
			rst = ps.executeQuery();

			if(rst.next()) {
				workflow = new Workflow();

				workflow.setId(rst.getInt("id"));
				workflow.setName(rst.getString("name"));
				workflow.setUsername(rst.getString("username"));
				workflow.setStatus(rst.getInt("status"));
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

		return workflow;
	}

	/**
	 * Insert a new workflow
	 * @param name
	 * @return boolean
	 */
	public boolean insertNewWorkflow(String name) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(INSERT_ONE_SQL);
			ps.setString(1, name);
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

	/**
	 * Update the workflow's status by id
	 * @param id
	 * @param status
	 * @return boolean
	 */
	public boolean updateWorkflowStatusById(int id, int status) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(UPDATE_STATUS_SQL);
			ps.setInt(1, status);
			ps.setInt(2, id);
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

	/**
	 * Update the workflow's start time by id
	 * @param id
	 * @param date
	 * @return boolean
	 */
	public boolean updateWorkflowStartTimeById(int id, Date date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(UPDATE_START_TIME_SQL);
			ps.setString(1, longFormatter.format(date));
			ps.setInt(2, id);
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

	/**
	 * Update the workflow's running time by id
	 * @param id
	 * @param date
	 * @return boolean
	 */
	public boolean updateWorkflowRunningTimeById(int id, Date date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(UPDATE_RUNNING_TIME_SQL);
			ps.setString(1, shortFormatter.format(date));
			ps.setInt(2, id);
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

	/**
	 * Update the workflow's finish time by id
	 * @param id
	 * @param date
	 * @return boolean
	 */
	public boolean updateWorkflowFinishTimeById(int id, Date date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(UPDATE_FINISH_TIME_SQL);
			ps.setString(1, longFormatter.format(date));
			ps.setInt(2, id);
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
