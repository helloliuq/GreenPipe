package org.greenpipe.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import org.greenpipe.bean.Block;
import org.greenpipe.bean.Connector;
import org.greenpipe.bean.Workflow;
import org.greenpipe.monitor.BlockMonitor;
import org.greenpipe.monitor.WorkflowMonitor;
import org.greenpipe.util.ReadConf;
import org.greenpipe.util.Status;

public class JobQueue {

	Queue<Block> jobWaitingQ;
	Queue<Block> jobReadyQ;
	Queue<Block> jobRunningQ;

	List<Workflow> wfCurList;
	List<BlockMonitor> blockMonitorList;

	private static int SLOTNUMBER;
	final int GETJOBINTERNAL = 1;	// internal of getting job from database (10s by default)

	ReadConf rc;

	private static Lock lock = new ReentrantLock();
	private static Condition blockComplete = lock.newCondition();
	private static Condition blockReady = lock.newCondition();

	private static final SimpleDateFormat shortFormatter = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat longFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public JobQueue() {
		jobWaitingQ = new ConcurrentLinkedQueue<Block>();
		jobReadyQ = new ConcurrentLinkedQueue<Block>();
		jobRunningQ = new ConcurrentLinkedQueue<Block>();
		wfCurList = new ArrayList<Workflow>();
		blockMonitorList = new ArrayList<BlockMonitor>(); 
		rc = new ReadConf("conf/workflow.conf");
		SLOTNUMBER = Integer.parseInt(rc.readFileByField("slot_no"));
	}

	/**
	 * Get new workflow jobs from database
	 * @return boolean
	 */
	private boolean getJob() {
		lock.lock();
		/*
		 * Update the current workflow list
		 */
		WorkflowMonitor.updateCurrentWorkflowList(wfCurList);

		/*
		 * Update the block monitor list to release the memory
		 */
		BlockMonitor.updateBlockMonitorList(blockMonitorList);

		/*
		 * Get new workflow list
		 */
		List<Workflow> wfList = null;
		wfList = WorkflowMonitor.getNewWorkflow();

		if (wfList == null || wfList.size() == 0) {
			// no new workflow
			lock.unlock();
			return false;
		}
			

		for (Workflow wf : wfList) {
			/*
			 * Update the workflow's status to queuing by id
			 */
			WorkflowMonitor.updateWorkflowStatusById(wf.getId(), Status.QUEUING);
			wf.setStatus(Status.QUEUING);

			for (Block block : wf.getBlockList()) {
				jobWaitingQ.offer(block); // add to queue
				/*
				 * Update the block's status to queuing by id
				 */
				BlockMonitor.updateBlockStatusById(block.getId(), Status.QUEUING);
				block.setStatus(Status.QUEUING);
			}

			/*
			 * Update the workflow's status to ready by id
			 */
			WorkflowMonitor.updateWorkflowStatusById(wf.getId(), Status.READY);
			wf.setStatus(Status.READY);
		}

		wfCurList.addAll(wfList);
		wfList = null;

		// put ready Block to jobReadyQ
		int waitingQSize = jobWaitingQ.size();
		for (int i = 0; i < waitingQSize; ++i) {
			Block block = jobWaitingQ.poll();
			if (block.getDependency() == 0) {
				/*
				 * Update the block's status to ready by id
				 */
				BlockMonitor.updateBlockStatusById(block.getId(), Status.READY);
				block.setStatus(Status.READY);   //*** wfCurList is also changed.  ****//
				jobReadyQ.offer(block);
			} else {
				jobWaitingQ.offer(block);
			}
		}

		if (jobReadyQ.size() > 0) {
			blockReady.signal();
		}

		lock.unlock();

		return true;
	}

	private boolean putBlockToReadyQ() {
		lock.lock();

		try {
			System.out.println("wait for other block to complete, so depended block may start to run.");
			blockComplete.await();
			System.out.println("some block completed, put depended block to read q if it is ready");

			// put ready Block to jobReadyQ
			int waitingQSize = jobWaitingQ.size();
			for (int i = 0; i < waitingQSize; ++i) {
				Block block = jobWaitingQ.poll();
				if (block.getDependency() == 0) {
					/*
					 * Update the block's status to ready by id
					 */
					BlockMonitor.updateBlockStatusById(block.getId(), Status.READY);
					block.setStatus(Status.READY);   //*** wfCurList is also changed.  ****//
					jobReadyQ.offer(block);
				} else {
					jobWaitingQ.offer(block);
				}
			}

			if (jobReadyQ.size() > 0) {
				blockReady.signal();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}		

		return false;
	}

	private boolean executeJob() {
		lock.lock();

		try {
			while (jobReadyQ.isEmpty()) {
				System.out.println("ready queue is empty, waiting for ready block ...");
				blockReady.await();
				System.out.println("ready queue is not empty, starting to execute.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			// execute ready Block
			while (!jobReadyQ.isEmpty()) {
				Block block = jobReadyQ.poll();
				while (block.getCpuNumber() > SLOTNUMBER) {
					System.out.println("block " + block.getId() + ", not enough slots: " 
							+ " need " + block.getCpuNumber() + " cpus, SLOTNUMBER = " + SLOTNUMBER + ", waiting...");
					blockComplete.await();
					System.out.println("start to run block " + block.getId());
				}
				SLOTNUMBER -= block.getCpuNumber();  // subtract cpu number
				System.out.println("block " + block.getId() + ": take " + block.getCpuNumber() + " cpus, SLOTNUMBER = " + SLOTNUMBER);
				Thread th = new Thread(new ExecuteBlockTask(block));
				th.start();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

		return true;
	}

	private boolean executeBlock(Block block) {
		// execute block		
		/*	try {
			for (int i = 0; i < 10; ++i) {
				System.out.println(i + ": execute block " + block.getBlockid());
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		// LFS <workspace node>
		String workDir = rc.readFileByField("work_dir") + "/" + block.getUsername() + "_" + block.getWfid();
		String workInputDir = workDir + "/input/" + block.getName();
		String workOutputDir = workDir + "/output/" + block.getName();
		String workCommandDir = workDir + "/command";
		String workScriptDir = workDir + "/script";
		// LFS <namenode>
		String namenode = rc.readFileByField("namenode");
		String namenodeUser = rc.readFileByField("namenode_user");
		String namenodePasswd = rc.readFileByField("namenode_passwd");
		String namenodeDir = rc.readFileByField("namenode_dir") + "/" + block.getUsername() + "_" + block.getWfid();
		String namenodeInputDir = namenodeDir + "/input/" + block.getName();
		String namenodeOutputDir = namenodeDir + "/output/" + block.getName();
		String namenodeScriptDir = namenodeDir + "/script";
		// DFS
		String dfsDir = rc.readFileByField("dfs_dir") + "/" + block.getUsername() + "_" + block.getWfid();
		String dfsInputDir = dfsDir + "/input/" + block.getName();
		String dfsOutputDir = dfsDir + "/output/" + block.getName();	
		// LFS <datanode>
		String datanodeDir = rc.readFileByField("datanode_dir") + "/" + block.getUsername() + "_" + block.getWfid();
		String datanodeInputDir = datanodeDir + "/input/" + block.getName();
		String datanodeOutputDir = datanodeDir + "/output/" + block.getName();

		generateScripts(workScriptDir, dfsOutputDir, dfsInputDir, namenodeInputDir, namenodeScriptDir, datanodeInputDir, datanodeOutputDir, dfsDir, workCommandDir, block);
		transferAndExec(namenodeDir, namenode, namenodeUser, namenodePasswd, namenodeScriptDir, workScriptDir, block.getName(), workInputDir, namenodeInputDir, block);

		// block finished (1.add cpu number  2.subtract dependency)
		lock.lock();
		SLOTNUMBER += block.getCpuNumber();
		System.out.println("block " + block.getId() + " finished, release " + block.getCpuNumber() + " cpus, SLOTNUMBER = " + SLOTNUMBER);
		for (Workflow wf : wfCurList) {
			if (wf.getId() == block.getWfid()) {
				for (Connector con : wf.getConnectorList()) {
					if (con.getOrigin().equals(block.getName())) {
						System.out.println("block name: " + block.getName());
						for (Block b : wf.getBlockList()) {
							if (b.getName().equals(con.getDestination())) {
								b.setDependency(b.getDependency() - 1);
								break;
							}
						}
						break;
					}
				}
				break;
			}
		}
		blockComplete.signalAll();
		lock.unlock();
		return true;
	}

	private class CommandLine {
		String command;
		String inputTag;
		String outputTag;
	}

	private void generateScripts(String workScriptDir, String dfsOutputDir, String dfsInputDir, String namenodeInputDir, String namenodeScriptDir,
			String datanodeInputDir, String datanodeOutputDir, String dfsDir, String workCommandDir, Block block) {
		String driver = workScriptDir + "/" + block.getName() + "_driver.sh";
		String mapper = workScriptDir + "/" + block.getName() + "_mapper.pl";

		try {

			String hadoopHomeDir = rc.readFileByField("hadoop_home_dir");
			String streamingDir = hadoopHomeDir + rc.readFileByField("hadoop_streaming_dir");

			List<String> inputDirList = new ArrayList<String>();
			if (!block.isEntry()) {
				for (Workflow wf : wfCurList) {
					if (wf.getId() == block.getWfid()) {
						for (Connector con : wf.getConnectorList()) {
							if (con.getDestination().equals(block.getName())) {
								String input = dfsDir + "/output/" + con.getOrigin();
								inputDirList.add(input);
							}
						}
						break;
					}
				}
			}
			String [] inputDir = (String[]) inputDirList.toArray(new String[inputDirList.size()]);

			STGroup g = new STGroupFile("templates/hadoop_script.stg");
			ST st = g.getInstanceOf("driver");
			st.add("hadoopHomeDir", hadoopHomeDir);
			st.add("dfsOutputDir", dfsOutputDir);
			st.add("dfsInputDir", dfsInputDir);
			st.add("isEntry", block.isEntry());
			st.add("inputDir", inputDir);
			st.add("namenodeInputDir", namenodeInputDir);
			st.add("streamingDir", streamingDir);
			st.add("namenodeScriptDir", namenodeScriptDir);
			st.add("blockName", block.getName());
			st.add("cpuno", block.getCpuNumber());
			File file = new File(driver);
			if (file.exists())
				file.delete();
			FileWriter writer = new FileWriter(driver, true);
			writer.write(st.render());
			writer.close();

			st = g.getInstanceOf("mapper");
			st.add("dfsInputDir", dfsInputDir);
			st.add("datanodeInputDir", datanodeInputDir);
			st.add("datanodeOutputDir", datanodeOutputDir);
			st.add("type", block.getOutput());
			st.add("hadoopHomeDir", hadoopHomeDir);
			CommandLine cl = getCommandLine(workCommandDir + "/" + block.getCommand());
			st.add("command", cl.command);   //***command needs to be modified******
			st.add("inputTag", cl.inputTag);
			st.add("outputTag", cl.outputTag);
			st.add("dfsOutputDir", dfsOutputDir);
			file = new File(mapper);
			if (file.exists())
				file.delete();
			writer = new FileWriter(mapper, true);
			writer.write(st.render());
			writer.close();

			// change file attribute to "777" owner to "hadoop"
			String changeAttr[] = { "/bin/bash", "-c", "chmod 777 " + driver + " " + mapper};
			Runtime.getRuntime().exec(changeAttr);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private CommandLine getCommandLine(String comPath) {
		CommandLine cl = null;
		try {
			Document doc = (new SAXBuilder()).build(new FileInputStream(comPath));
			cl = new CommandLine();
			Element root = doc.getRootElement();
			System.out.println("root: " + root.getName());
			List<Element> command = root.getChildren();
			for (Element parameter : command) {
				System.out.println("parameter: " + parameter.getName());
				if (parameter.getName() == "app") {
					cl.command = parameter.getText();
				} else {
					List<Element> params = parameter.getChildren();
					for (Element param : params) {
						if (param.getAttribute("input") != null && param.getAttribute("input").getValue().equals("true")) {
							cl.inputTag = param.getChildText("tag");
						} else if (param.getAttribute("output") != null &&  param.getAttribute("output").getValue().equals("true")) {
							cl.outputTag = param.getChildText("tag");
						} else {
							cl.command += " " + param.getChildText("tag") + " " + param.getChildText("value");
						}
					}
				}
			}
			System.out.println("cl.command: " + cl.command + "\ncl.inputTag: " + cl.inputTag + "\ncl.outputTag: " + cl.outputTag);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cl;
	}

	// transfer input data and scripts to namenode, then execute
	private void transferAndExec(String namenodeDir, String namenode, String namenodeUser, String namenodePasswd,
			String namenodeScriptDir, String workScriptDir, String blockName, String workInputDir, String namenodeInputDir, Block block) {
		String transferScript = workScriptDir + "/tranfer_" + blockName + ".exp";
		String execScript = workScriptDir + "/exec_" + blockName + ".exp";
		STGroup g = new STGroupFile("templates/transfer.stg");
		ST st = g.getInstanceOf("transfer");
		st.add("namenode", namenode);
		st.add("namenode_user", namenodeUser);
		st.add("namenode_passwd", namenodePasswd);
		st.add("namenodeDir", namenodeDir);
		st.add("namenodeInputDir", namenodeInputDir);
		st.add("workInputDir", workInputDir);
		st.add("workScriptDir", workScriptDir);
		st.add("driver", blockName + "_driver.sh");
		st.add("mapper", blockName + "_mapper.pl");
		File dir = new File(workInputDir);
		List<String> inputFileList = new ArrayList<String>();
		for (File f : dir.listFiles()) {
			inputFileList.add(f.getName());
		}
		String [] inputFile = (String[]) inputFileList.toArray(new String[inputFileList.size()]);
		st.add("inputFile", inputFile);
		File file = new File(transferScript);
		if (file.exists())
			file.delete();
		FileWriter writer;
		try {
			writer = new FileWriter(transferScript, true);
			writer.write(st.render());
			writer.close();

			// tansfer
			String changeAttr[] = { "/bin/bash", "-c", "chmod 777 " + transferScript};
			Runtime.getRuntime().exec(changeAttr).waitFor();
			String execTransfer[] = {"/bin/bash", "-c", transferScript};
			Runtime.getRuntime().exec(execTransfer).waitFor();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		g = new STGroupFile("templates/execScript.stg");
		st = g.getInstanceOf("execScript");
		st.add("namenode", namenode);
		st.add("namenode_user", namenodeUser);
		st.add("namenode_passwd", namenodePasswd);
		st.add("namenodeScriptDir", namenodeScriptDir);
		st.add("driver", blockName + "_driver.sh");
		file = new File(execScript);
		if (file.exists())
			file.delete();
		try {
			writer = new FileWriter(execScript, true);
			writer.write(st.render());
			writer.close();

			// execute
			String changeAttr[] = { "/bin/bash", "-c", "chmod 777 " + execScript};
			Runtime.getRuntime().exec(changeAttr).waitFor();
			String exec[] = {"/bin/bash", "-c", execScript};
			Process p = Runtime.getRuntime().exec(exec);

			Date startTime = new Date();
			Date waitTime = null;

			/*
			 * Update the block's status to running by id
			 */
			BlockMonitor.updateBlockStatusById(block.getId(), Status.RUNNING);
			block.setStatus(Status.RUNNING);

			for(Workflow wf : wfCurList) {
				if(block.getWfid() == wf.getId()) {
					if(wf.getStatus() == Status.READY) {
						/*
						 * Update the workflow's status to running by id
						 */
						WorkflowMonitor.updateWorkflowStatusById(wf.getId(), Status.RUNNING);
						wf.setStatus(Status.RUNNING);
						/*
						 * Update the workflow's start time by id
						 */
						WorkflowMonitor.updateWorkflowStartTimeById(wf.getId(), startTime);
						wf.setStartTime(longFormatter.format(startTime));

						waitTime = new Date(16*60*60*1000);
						/*
						 * Update the block's wait time by id
						 */
						BlockMonitor.updateBlockWaitTimeById(block.getId(), waitTime);
						block.setWaitTime(shortFormatter.format(waitTime));
					} else {
						try {
							waitTime = new Date(16*60*60*1000 + startTime.getTime() - (longFormatter.parse(wf.getStartTime())).getTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*
						 * Update the block's wait time by id
						 */
						BlockMonitor.updateBlockWaitTimeById(block.getId(), waitTime);
						block.setWaitTime(shortFormatter.format(waitTime));
					}
				}
			}
			/*
			 * Update the block's start time by id
			 */
			BlockMonitor.updateBlockStartTimeById(block.getId(), startTime);
			block.setStartTime(longFormatter.format(startTime));

			/*
			 * Get job id
			 */
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if (-1 != line.indexOf("Running job: job_")) {
					break;
				}
			}
			int index = line.indexOf("job_");
			String jobId = line.substring(index);

			/*
			 * Create a thread to monitor this job
			 */
			BlockMonitor blockMonitor = new BlockMonitor(block, jobId);
			blockMonitor.start();
			/*
			 * Put this monitor into the monitor list
			 */
			blockMonitorList.add(blockMonitor);
			blockMonitor = null;
			
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
	}

	//*************************************************************************************
	//***********************Threads***********************
	//*************************************************************************************

	// Get job from database task every 10s by default
	private class GetJobFromDBTask implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while (true) {
					getJob();	
					Thread.sleep(GETJOBINTERNAL * 1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

	private class PutBlockToReadyQTask implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				putBlockToReadyQ();
			}
		}	
	}

	private class ExecuteJobTask implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				executeJob();
			}
		}
	}

	private class ExecuteBlockTask implements Runnable {
		private Block block;

		public ExecuteBlockTask(Block block) {
			super();
			this.block = block;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			executeBlock(block);
		}
	}

	// driver
	public void start() {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.execute(new GetJobFromDBTask());
		executor.execute(new PutBlockToReadyQTask());
		executor.execute(new ExecuteJobTask());
		executor.shutdown();
	}
}
