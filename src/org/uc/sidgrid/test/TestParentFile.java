package org.uc.sidgrid.test;

import java.io.File;
public class TestParentFile {
	public static void main(String[] args) throws Exception{
		File tmp = new File("/sandbox/wwj/apache-tomcat-5.5.27/webapps/axis2/WEB-INF/");
		System.out.println(tmp.getAbsolutePath());
		System.out.println(tmp.getParent());
		File parent = tmp.getParentFile();
		System.out.println(parent.getParent());
	}
}
