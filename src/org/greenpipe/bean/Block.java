package org.greenpipe.bean;

public class Block {
	private int id;
	private String name;
	private int wfid;
	private int dependency;
	private String command;
	private String input;
	private String output;
	private int cpuNumber;
	private String username;
	private int status;
	private double progress;
	private String waitTime;
	private String startTime;
	private String runningTime;
	private String finishTime;
	private boolean entry; // if dependency = 0 then entry = true, else entry = false;

	public Block() {
		this.id = 0;
		this.name = null;
		this.wfid = 0;
		this.dependency = 0;
		this.command = null;
		this.input= null;
		this.output= null;
		this.cpuNumber = 0;
		this.username= null;
		this.status= 0;
		this.progress = 0;
		this.waitTime = null;
		this.startTime = null;
		this.runningTime = null;
		this.finishTime = null;
		this.entry = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWfid() {
		return wfid;
	}

	public void setWfid(int wfid) {
		this.wfid = wfid;
	}

	public int getDependency() {
		return dependency;
	}

	public void setDependency(int dependency) {
		this.dependency = dependency;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public int getCpuNumber() {
		return cpuNumber;
	}

	public void setCpuNumber(int cpuNumber) {
		this.cpuNumber = cpuNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(String runningTime) {
		this.runningTime = runningTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public boolean isEntry() {
		return entry;
	}

	public void setEntry(boolean entry) {
		this.entry = entry;
	}

}
