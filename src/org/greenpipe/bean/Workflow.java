package org.greenpipe.bean;

import java.util.List;

public class Workflow {
	private int id;
	private String name;
	private String username;
	private int status;
	private String startTime;
	private String runningTime;
	private String finishTime;
	private List<Block> blockList;
	private List<Connector> connectorList;

	public Workflow() {
		this.id = 0;
		this.name = null;
		this.username = null;
		this.status = 0;
		this.startTime = null;
		this.runningTime = null;
		this.finishTime = null;
		this.blockList = null;
		this.connectorList = null;
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

	public List<Block> getBlockList() {
		return blockList;
	}

	public void setBlockList(List<Block> blockList) {
		this.blockList = blockList;
	}

	public List<Connector> getConnectorList() {
		return connectorList;
	}

	public void setConnectorList(List<Connector> connectorList) {
		this.connectorList = connectorList;
	}

}
