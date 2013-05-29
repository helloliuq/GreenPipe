package org.greenpipe.bean;

public class Connector {
	private int id;
	private int wfid;
	private String origin;
	private String destination;
	private String username;

	public Connector() {
		this.id = 0;
		this.wfid = 0;
		this.origin = null;
		this.destination = null;
		this.username = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWfid() {
		return wfid;
	}

	public void setWfid(int wfid) {
		this.wfid = wfid;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
