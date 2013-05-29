package org.greenpipe.monitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.greenpipe.bean.Block;
import org.greenpipe.bean.Workflow;
import org.greenpipe.dao.WorkflowDAO;
import org.greenpipe.util.Status;

public class WorkflowMonitor {
	private static final SimpleDateFormat shortFormatter = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat longFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Constructor
	 */
	public WorkflowMonitor() {}
	
	/**
	 * Get new workflow list
	 * @return List<Workflow>
	 */
	public static List<Workflow> getNewWorkflow() {
		WorkflowDAO workflowDAO = new WorkflowDAO();
		List<Workflow> workflowList = workflowDAO.getNewWorkflow();
		workflowDAO = null;
		return workflowList;
	}

	/**
	 * Update the workflow's status by id
	 * @param id
	 * @param status
	 * @return boolean
	 */
	public static boolean updateWorkflowStatusById(int id, int status) {
		WorkflowDAO workflowDAO = new WorkflowDAO();
		boolean result = workflowDAO.updateWorkflowStatusById(id, status);
		workflowDAO = null;
		return result;
	}

	/**
	 * Update the workflow's start time by id
	 * @param id
	 * @param date
	 * @return boolean
	 */
	public static boolean updateWorkflowStartTimeById(int id, Date date) {
		WorkflowDAO workflowDAO = new WorkflowDAO();
		boolean result = workflowDAO.updateWorkflowStartTimeById(id, date);
		workflowDAO = null;
		return result;
	}

	/**
	 * Update the workflow's running time by id
	 * @param id
	 * @param date
	 * @return boolean
	 */
	public static boolean updateWorkflowRunningTimeById(int id, Date date) {
		WorkflowDAO workflowDAO = new WorkflowDAO();
		boolean result = workflowDAO.updateWorkflowRunningTimeById(id, date);
		workflowDAO = null;
		return result;
	}

	/**
	 * Update workflow's finish time by id
	 * @param id
	 * @param date
	 * @return boolean
	 */
	public static boolean updateWorkflowFinishTimeById(int id, Date date) {
		WorkflowDAO workflowDAO = new WorkflowDAO();
		boolean result = workflowDAO.updateWorkflowFinishTimeById(id, date);
		workflowDAO = null;
		return result;
	}
	
	/**
	 * Update the current workflow list
	 * @param workflowList
	 * @return
	 */
	public static boolean updateCurrentWorkflowList(List<Workflow> workflowList) {
		Date currentTime = new Date();
		
		for(Workflow wf : workflowList) {
			if(wf.getStatus() == Status.RUNNING) {
				Date runningTime = null;
				try {
					runningTime = new Date(16*60*60*1000 + currentTime.getTime() - (longFormatter.parse(wf.getStartTime())).getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * Update the workflow's running time by id
				 */
				WorkflowMonitor.updateWorkflowRunningTimeById(wf.getId(), runningTime);
				wf.setRunningTime(shortFormatter.format(runningTime));
	
				/*
				 * Check the blocks' status
				 */
				int total = wf.getBlockList().size();
				int queue = 0, ready = 0, running = 0, completed = 0, failed = 0, killed = 0;
				for(Block block : wf.getBlockList()) {
					switch (block.getStatus()) {
					case Status.QUEUING: queue++; break;
					case Status.READY: ready++; break;
					case Status.RUNNING: running++; break;
					case Status.COMPLETED: completed++; break;
					case Status.FAILED: failed++; break;
					case Status.KILLED: killed++; break;
					default: break;
					}
				}
				
				/*
				 * Update the workflow's status
				 */
				if(completed == total) {
					WorkflowMonitor.updateWorkflowStatusById(wf.getId(), Status.COMPLETED);
					wf.setStatus(Status.COMPLETED);
				} else if(failed != 0) {
					WorkflowMonitor.updateWorkflowStatusById(wf.getId(), Status.FAILED);
					wf.setStatus(Status.FAILED);
				} else if(killed != 0) {
					WorkflowMonitor.updateWorkflowStatusById(wf.getId(), Status.KILLED);
					wf.setStatus(Status.KILLED);
				}
				
				if(wf.getStatus() == Status.COMPLETED || wf.getStatus() == Status.FAILED || wf.getStatus() == Status.KILLED) {
					/*
					 * Update the workflow's finish time
					 */
					Date finishTime = new Date(currentTime.getTime());
					WorkflowMonitor.updateWorkflowFinishTimeById(wf.getId(), finishTime);
					wf.setFinishTime(longFormatter.format(finishTime));
					
					/*
					 * Update the blocks' status to follow this workflow
					 */
					for(Block block : wf.getBlockList()) {
						if(block.getStatus() == Status.QUEUING || block.getStatus() == Status.READY || block.getStatus() == Status.RUNNING) {
							block.setStatus(wf.getStatus());
						}
					}
					/*
					 * Remove this workflow from the current workflow list
					 */
					workflowList.remove(wf);
 				}
			}
		}
		return true;
	}
}
