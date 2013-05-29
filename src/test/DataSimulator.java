package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.greenpipe.bean.Block;
import org.greenpipe.dao.BlockDAO;
import org.greenpipe.dao.WorkflowDAO;
import org.greenpipe.util.Status;

/**
 * Servlet implementation class test
 */
@WebServlet("/test")
public class DataSimulator extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Date initTime = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DataSimulator() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		initTime = new Date();
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

		System.out.println("ds");

		request.setCharacterEncoding("gb2312");
		response.setContentType("text/xml;charset=gb2312");
		PrintWriter out=response.getWriter();

		int wfid = (Integer) request.getAttribute("id");

		WorkflowDAO workflowDAO = new WorkflowDAO();
		BlockDAO blockDAO = new BlockDAO();

		List<Block> blockList = blockDAO.getBlocksByWfid(wfid);

		//workflow status
		workflowDAO.updateWorkflowStatusById(wfid, Status.RUNNING);

		//workflow start time
		Date startTime = new Date();
		workflowDAO.updateWorkflowStartTimeById(wfid, startTime);

		for(int i = 0; i < blockList.size() - 1; i++) {
			Simulator simulator = new Simulator(blockList.get(i));
			simulator.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//workflow running time
		boolean flag = true;
		while(flag) {
			int i = 0;
			for(i = 0; i < blockList.size(); i++) {
				Block block = blockList.get(i);
				if(block.getStatus() != Status.COMPLETED) {
					break;
				}
			}

			if(i == blockList.size()) {
				flag = false;
			} else {
				Date currentTime = new Date();
				long timeInterval = currentTime.getTime() - startTime.getTime();
				Date runningTime = new Date(16*60*60*1000 + timeInterval);
				workflowDAO.updateWorkflowRunningTimeById(wfid, runningTime);
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//workflow status
		workflowDAO.updateWorkflowStatusById(wfid, Status.COMPLETED);

		//workflow finish_time
		Date finishTime = new Date();
		workflowDAO.updateWorkflowFinishTimeById(wfid, finishTime);
	}

	private class Simulator extends Thread {
		private Block block;

		public Simulator(Block block) {
			this.block = block;
		}

		public void run() {
			BlockDAO blockDAO = new BlockDAO();

			int id = block.getId();

			SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			//status
			blockDAO.updateBlockStatusById(id, Status.RUNNING);

			//wait_time
			Date startTime = new Date();
			long timeInterval = startTime.getTime() - initTime.getTime();
			System.out.println(timeInterval);
			Date waitTime = new Date(16*60*60*1000 + timeInterval);
			System.out.println(waitTime.toString());
			blockDAO.updateBlockWaitTimeById(id, waitTime);

			//start_time
			blockDAO.updateBlockStartTimeById(id, startTime);

			//progress and running_time
			for(int i = 0; i < 101; i = i + 10) {
				//status 
				Date currentTime = new Date();
				timeInterval = currentTime.getTime() - startTime.getTime();
				Date runningTime = new Date(16*60*60*1000 + timeInterval);

				blockDAO.updateBlockProgressById(id, i*1.0);

				blockDAO.updateBlockRunningTimeById(id, runningTime);

				currentTime = null;
				runningTime = null;

				if(i == 100) {
					//status
					blockDAO.updateBlockStatusById(id, Status.COMPLETED);
					block.setStatus(Status.COMPLETED);

					//finish_time
					Date finishTime = new Date();
					blockDAO.updateBlockFinishTimeById(id, finishTime);
				}

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
