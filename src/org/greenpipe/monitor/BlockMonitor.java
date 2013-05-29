package org.greenpipe.monitor;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TIPStatus;
import org.apache.hadoop.mapred.TaskReport;

import org.greenpipe.bean.Block;
import org.greenpipe.dao.BlockDAO;
import org.greenpipe.util.ReadConf;
import org.greenpipe.util.Status;

public class BlockMonitor extends Thread{
	private static final SimpleDateFormat shortFormatter = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat longFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Block block;
	private String jobId;
	private boolean finished;

	/**
	 * Constructor
	 * @param block
	 * @param jobId
	 */
	public BlockMonitor(Block block, String jobId) {
		this.block = block;
		this.jobId = jobId;
		this.finished = false;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean getFinished() {
		return this.finished;
	}

	/**
	 * Update the block's status by id
	 * @param id
	 * @param status
	 * @return boolean
	 */
	public static boolean updateBlockStatusById(int id, int status) {
		BlockDAO blockDAO = new BlockDAO();
		boolean result = blockDAO.updateBlockStatusById(id, status);
		blockDAO = null;
		return result;
	}

	/**
	 * Update the block's wait time by id
	 * @param id
	 * @param date
	 * @return boolean
	 */
	public static boolean updateBlockWaitTimeById(int id, Date date) {
		BlockDAO blockDAO = new BlockDAO();
		boolean result = blockDAO.updateBlockWaitTimeById(id, date);
		blockDAO = null;
		return result;
	}

	/**
	 * Update the block's start time by id
	 * @param id
	 * @param date
	 * @return boolean
	 */
	public static boolean updateBlockStartTimeById(int id, Date date) {
		BlockDAO blockDAO = new BlockDAO();
		boolean result = blockDAO.updateBlockStartTimeById(id, date);
		blockDAO = null;
		return result;
	}

	/**
	 * Update the block's running time by id
	 * @param id
	 * @param date
	 * @return boolean
	 */
	public static boolean updateBlockRunningTimeById(int id, Date date) {
		BlockDAO blockDAO = new BlockDAO();
		boolean result = blockDAO.updateBlockRunningTimeById(id, date);
		blockDAO = null;
		return result;
	}

	/**
	 * Update the block's finish time by id
	 * @param id
	 * @param date
	 * @return boolean
	 */
	public static boolean updateBlockFinishTimeById(int id, Date date) {
		BlockDAO blockDAO = new BlockDAO();
		boolean result = blockDAO.updateBlockFinishTimeById(id, date);
		blockDAO = null;
		return result;
	}

	/**
	 * Update the block's progress by id
	 * @param id
	 * @param progress
	 * @return boolean
	 */
	public static boolean updateBlockProgressById(int id, Double progress) {
		BlockDAO blockDAO = new BlockDAO();
		boolean result = blockDAO.updateBlockProgressById(id, progress);
		blockDAO = null;
		return result;
	}

	/**
	 * Update the block monitor list to release the memory
	 * @param blockMonitorList
	 * @return
	 */
	public static boolean updateBlockMonitorList(List<BlockMonitor> blockMonitorList) {
		for(BlockMonitor blockMonitor : blockMonitorList) {
			Block block = blockMonitor.getBlock();
			if(block.getStatus() == Status.COMPLETED || block.getStatus() == Status.FAILED || block.getStatus() == Status.KILLED) {
				//stop the thread 
				blockMonitor.setFinished(true);
				//remove this block monitor from the monitor list
				//blockMonitorList.remove(blockMonitor);
				//release the memory;
				//blockMonitor = null;
			}
		}
		return true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ReadConf readConf = new ReadConf("conf/workflow.conf");

		System.out.println("job_tracker:" + readConf.readFileByField("job_tracker"));

		Configuration configuration = new Configuration();
		configuration.set("mapred.job.tracker", readConf.readFileByField("job_tracker"));

		JobConf jobConf = new JobConf(configuration);
		JobClient jobClient = null;
		try {
			jobClient = new JobClient(jobConf);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(!finished) {
			Date currentTime = new Date();

			try {
				/*
				 * Get map task reports
				 */
				if(block.getStatus() == Status.RUNNING) {
					TaskReport[] mapTaskReports = jobClient.getMapTaskReports(jobId);
					int total = mapTaskReports.length;
					int completed = 0, failed = 0, killed = 0, pending = 0, running = 0;
					for (TaskReport taskReport : mapTaskReports) {
						if(taskReport.getCurrentStatus() == TIPStatus.COMPLETE) {
							completed++;
						} else if(taskReport.getCurrentStatus() == TIPStatus.FAILED) {
							failed++;
						} else if(taskReport.getCurrentStatus() == TIPStatus.KILLED) {
							killed++;
						} else if(taskReport.getCurrentStatus() == TIPStatus.PENDING) {
							pending++;
						} else if(taskReport.getCurrentStatus() == TIPStatus.RUNNING) {
							running++;
						}
					}

					/*
					 * Update block's progress
					 */
					double progress = (completed * 1.0) / total * 100;
					DecimalFormat df = new DecimalFormat("0.00");
					progress = Double.parseDouble(df.format(progress));
					BlockMonitor.updateBlockProgressById(block.getId(), progress);
					block.setProgress(progress);

					/*
					 * Update block's running time
					 */
					Date runningTime = null;
					try {
						runningTime = new Date(16*60*60*1000 + currentTime.getTime() - (longFormatter.parse(block.getStartTime())).getTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					BlockMonitor.updateBlockRunningTimeById(block.getId(), runningTime);
					block.setRunningTime(shortFormatter.format(runningTime));

					/*
					 * Update the block's status
					 */
					if(completed == total) {
						BlockMonitor.updateBlockStatusById(block.getId(), Status.COMPLETED);
						block.setStatus(Status.COMPLETED);
					} else if(failed != 0) {
						BlockMonitor.updateBlockStatusById(block.getId(), Status.FAILED);
						block.setStatus(Status.FAILED);
					} else if(killed != 0) {
						BlockMonitor.updateBlockStatusById(block.getId(), Status.KILLED);
						block.setStatus(Status.KILLED);
					}

					/*
					 * Update the block's finish time
					 */
					if(block.getStatus() == Status.COMPLETED || block.getStatus() == Status.FAILED || block.getStatus() == Status.KILLED) {
						Date finishTime = new Date(currentTime.getTime());
						BlockMonitor.updateBlockFinishTimeById(block.getId(), finishTime);
						block.setFinishTime(longFormatter.format(finishTime));
					}

					/*
					 * Sleep for 1 second
					 */
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
