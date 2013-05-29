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

import org.greenpipe.bean.Block;

public class BlockDAO {
	private static InitialContext context = null;
	private DataSource dataSource = null;
	private static final SimpleDateFormat shortFormatter = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat longFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String SELECT_ALL_SQL = "select * from block where wfid=?";
	private static final String INSERT_NEW_SQL = "insert into block(name,wfid,dependency,command,input,output) values(?,?,?,?,?,?)";
	private static final String UPDATE_ONE_STATUS_SQL = "update block set status=? where id=?";
	private static final String UPDATE_ALL_STATUS_SQL = "update block set status=? where wfid=?";
	private static final String UPDATE_WAIT_TIME_SQL = "update block set wait_time=str_to_date(?, '%k:%i:%s') where id=?";
	private static final String UPDATE_START_TIME_SQL = "update block set start_time=str_to_date(?, '%Y-%m-%d %k:%i:%s') where id=?";
	private static final String UPDATE_RUNNING_TIME_SQL = "update block set running_time=str_to_date(?, '%k:%i:%s') where id=?";
	private static final String UPDATE_FINISH_TIME_SQL = "update block set finish_time=str_to_date(?, '%Y-%m-%d %k:%i:%s') where id=?";
	private static final String UPDATE_PROGRESS_SQL = "update block set progress=? where id=?";

	/**
	 * Constructor
	 */
	public BlockDAO() {
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
	 * Get blocks by wfid
	 * @param wfid
	 * @return List<Block>
	 */
	public List<Block> getBlocksByWfid(int wfid) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		List<Block> blockList = new ArrayList<Block>();
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(SELECT_ALL_SQL);
			ps.setInt(1, wfid);
			rst = ps.executeQuery();

			while(rst.next()) {
				Block block = new Block();

				block.setId(rst.getInt("id"));
				block.setName(rst.getString("name"));
				block.setWfid(rst.getInt("wfid"));
				block.setDependency(rst.getInt("dependency"));
				block.setCommand(rst.getString("command"));
				block.setInput(rst.getString("input"));
				block.setOutput(rst.getString("output"));
				block.setCpuNumber(rst.getInt("cpuno"));
				block.setUsername(rst.getString("username"));
				block.setStatus(rst.getInt("status"));
				block.setProgress(rst.getDouble("progress"));
				if(rst.getTimestamp("wait_time") != null) {
					Date date = new Date(rst.getTimestamp("wait_time").getTime());
					block.setWaitTime(shortFormatter.format(date));
				}
				if(rst.getTimestamp("start_time") != null) {
					Date date = new Date(rst.getTimestamp("start_time").getTime());
					block.setStartTime(longFormatter.format(date));
				}
				if(rst.getTimestamp("running_time") != null) {
					Date date = new Date(rst.getTimestamp("running_time").getTime());
					block.setRunningTime(shortFormatter.format(date));
				}
				if(rst.getTimestamp("finish_time") != null) {
					Date date = new Date(rst.getTimestamp("finish_time").getTime());
					block.setFinishTime(longFormatter.format(date));
				}
				block.setEntry(block.getDependency() == 0 ? true : false);

				blockList.add(block);
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

		return blockList;
	}

	/**
	 * Insert a new block
	 * @param block
	 * @return boolean
	 */
	public boolean insertNewBlock(Block block) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(INSERT_NEW_SQL);
			ps.setString(1, block.getName());
			ps.setInt(2, block.getWfid());
			ps.setInt(3, block.getDependency());
			ps.setString(4, block.getCommand());
			ps.setString(5, block.getInput());
			ps.setString(6, block.getOutput());
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
	 * Update a block's status by id
	 * @param id
	 * @param status
	 * @return boolean
	 */
	public boolean updateBlockStatusById(int id, int status) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(UPDATE_ONE_STATUS_SQL);
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
	 * Update blocks' status by wfid
	 * @param wfid
	 * @param status
	 * @return boolean
	 */
	public boolean updateBlockStatusByWfid(int wfid, int status) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(UPDATE_ALL_STATUS_SQL);
			ps.setInt(1, status);
			ps.setInt(2, wfid);
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
	 * Update a block's wait time by id
	 * @param id
	 * @param status
	 * @return boolean
	 */
	public boolean updateBlockWaitTimeById(int id, Date date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(UPDATE_WAIT_TIME_SQL);
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
	 * Update a block's start time by id
	 * @param id
	 * @param status
	 * @return boolean
	 */
	public boolean updateBlockStartTimeById(int id, Date date) {
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
	 * Update a block's running time by id
	 * @param id
	 * @param status
	 * @return boolean
	 */
	public boolean updateBlockRunningTimeById(int id, Date date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(UPDATE_RUNNING_TIME_SQL);
			ps.setString(1, shortFormatter.format(date));
			ps.setInt(2, id);
			ps.executeUpdate();

			ps.close();
			conn.close();
			
			ps = null;
			conn = null;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
		}

		return true;
	}

	/**
	 * Update a block's finish time by id
	 * @param id
	 * @param status
	 * @return boolean
	 */
	public boolean updateBlockFinishTimeById(int id, Date date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(UPDATE_FINISH_TIME_SQL);
			ps.setString(1, longFormatter.format(date));
			ps.setInt(2, id);
			ps.executeUpdate();

			ps.close();
			conn.close();
			
			ps = null;
			conn = null;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
		}

		return true;
	}

	/**
	 * Update a block's progress by id
	 * @param id
	 * @param status
	 * @return boolean
	 */
	public boolean updateBlockProgressById(int id, Double progress) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rst = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(UPDATE_PROGRESS_SQL);
			ps.setDouble(1, progress);
			ps.setInt(2, id);
			ps.executeUpdate();
				
			ps.close();
			conn.close();
			
			ps = null;
			conn = null;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
		}

		return true;
	}

}
