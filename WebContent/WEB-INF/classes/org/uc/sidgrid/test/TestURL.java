package org.uc.sidgrid.test;

import java.net.URL;

public class TestURL {
	public static void main(String[] args) throws Exception{
		URL url = new URL("https://sidgrid.ci.uchicago.edu:8080/sidgrid/experiment_files/1/hz2_AFITPitch.txt");
		String fileName = url.getFile();
		String path = url.getPath();
		System.out.println(fileName+":"+path+":"+url.getPort());
		String [] res = path.split("/");
		for(int i=0; i<res.length;i++)
			System.out.println(res[i]);
	}
}
