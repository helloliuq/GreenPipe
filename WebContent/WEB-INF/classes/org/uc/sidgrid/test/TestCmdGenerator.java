package org.uc.sidgrid.test;

import java.io.File;
import java.util.Hashtable;

import org.uc.sidgrid.mobyle.core.CmdGenerator;
import org.uc.sidgrid.mobyle.ProgramDocument;

public class TestCmdGenerator {
	public static void runCmdGenerator(String filename, Hashtable<String,String> valuePairs) throws Exception{
		File xmlFile = new File(filename);  
	    ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);   
        String cmd = CmdGenerator.buildLocalCmd(programDoc, valuePairs, null);
        System.out.println("cmd is "+cmd);
	}
	public static void testOops()throws Exception{
		Hashtable<String,String> valuePairs = new Hashtable<String,String>();
	    valuePairs.put("plist", "http://sidgrid.uc.teragrid.org/oops-test/protein.fasta");
	    valuePairs.put("nsims", "2");
	    valuePairs.put("st", "10");
	    valuePairs.put("tui", "5");
	    valuePairs.put("coeff", "0.2");
	    
	    String filename = "/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml/oops-hidden.xml";
	    valuePairs.put("outdir", "/sandbox/wwj/output");
	    
		File xmlFile = new File(filename);  
	    ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);   
        String cmd = CmdGenerator.buildLocalCmd(programDoc, valuePairs, null);
        System.out.println("cmd is "+cmd);
	}
	public static void testClustalw() throws Exception{
		Hashtable<String,String> valuePairs = new Hashtable<String,String>();
        valuePairs.put("infile", "input");
        valuePairs.put("quicktree", "slow");
        valuePairs.put("output", "output");
        
        String filename = "/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml/clustalw-multialign.xml";
		File xmlFile = new File(filename);  
	    ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);   
        String cmd = CmdGenerator.buildLocalCmd(programDoc, valuePairs, null);
        System.out.println("cmd is "+cmd);
	}
	public static void testPraat() throws Exception{
		 Hashtable<String,String> valuePairs = new Hashtable<String,String>();
		 valuePairs.put("inscript", "http://sidgrid.uc.teragrid.org/praat-test/pitch.praat");
		 valuePairs.put("infile", "http://sidgrid.uc.teragrid.org/praat-test/elan-example1.wav");
		 valuePairs.put("outfile", "output");
        
        String filename = "/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml/praat.xml";
        runCmdGenerator(filename, valuePairs);
        
	}
	public static void testGunzip() throws Exception{
		Hashtable<String,String> valuePairs = new Hashtable<String,String>();
		valuePairs.put("c", "1");
		valuePairs.put("infile", "http://sidgrid.uc.teragrid.org/praat-test/elan-example1.wav qq.tt");
		valuePairs.put("outfile", "xxx");
       
       String filename = "/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml/gunzip.xml";
       runCmdGenerator(filename, valuePairs);
	}
	public static void main(String[] args) throws Exception{
		//testClustalw();
		//testOops();
		//testPraat();
		testGunzip();
	}
}
