package org.greenpipe.bean;

public class Wiring {
	private int id;
	private String name;
	private String json;
	private String xml;
	private String username;

	public Wiring() {
		this.id = 0;
		this.name = null;
		this.json = null;
		this.xml = null;
		this.username = null;
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

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
