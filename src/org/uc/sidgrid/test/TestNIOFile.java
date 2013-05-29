package org.uc.sidgrid.test;

import java.io.*;

import org.uc.sidgrid.util.Chmod;
public class TestNIOFile {
	public static void testSetFileMod()throws Exception{
		File test = new File("./test1/");
		if (!test.exists()){
			test.mkdir();
			Chmod.setFileMod("./test1/", "0777");
		}
		try {
		      FileWriter out1 = new FileWriter("./test1/test.txt");
		      out1.write("this is a test");
		      out1.close();
		      Chmod.setFileMod("./test1/test.txt", "0766");
		      
		    } catch(Exception e){
		    	e.printStackTrace();
		    }
	}
	
	public static void main(String[] args) throws Exception{
	    Chmod.nativeCpDir("e:/life/resume", "e:/life/tmp");	
	}
}
