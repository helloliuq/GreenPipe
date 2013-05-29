package org.uc.sidgrid.test;

import java.io.File;
import java.util.Hashtable;

import org.uc.sidgrid.mobyle.ProgramDocument;
import org.uc.sidgrid.mobyle.core.*;

public class TestScriptGenerator {
  public static void genClustalwScript() throws Exception{
	 File xmlFile = new File("./mobylexml/clustalw-multialign.xml"); 
	 Hashtable<String,String> valuePairs = new Hashtable<String,String>();
     valuePairs.put("infile", "input");
     valuePairs.put("quicktree", "slow");
     valuePairs.put("outfile", "output");
     
     ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
     java.util.ArrayList<Argument> margs = new java.util.ArrayList<Argument>();
     CmdGenerator.buildLocalCmd(programDoc, valuePairs, margs);
     String script = ScriptGenerator.buildSwiftScript(programDoc, margs, "./mobylexml");
     System.out.println(script);
  }
  
  public static void genClustalwScript4Files() throws Exception{
		 File xmlFile = new File("./mobylexml/clustalw-multialign.xml"); 
		 Hashtable<String,String> valuePairs = new Hashtable<String,String>();
	     valuePairs.put("infile", "input1, input2, input 3");
	     valuePairs.put("quicktree", "slow");
	     valuePairs.put("outfile", "output1, output2, output3");
	     
	     ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
	     java.util.ArrayList<Argument> margs = new java.util.ArrayList<Argument>();
	     CmdGenerator.buildLocalCmd(programDoc, valuePairs, margs);
	     String script = ScriptGenerator.buildSwiftFilesScript(programDoc, margs, "./mobylexml");
	     System.out.println(script);
  }
  
  public static void genSIDGridClustalwScript() throws Exception{
	     File xmlFile = new File("./mobylexml/clustalw-multialign.xml"); 
		 Hashtable<String,String> valuePairs = new Hashtable<String,String>();
	     valuePairs.put("infile", "input");
	     valuePairs.put("quicktree", "slow");
	     valuePairs.put("outfile", "output");
	     
	     ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
	     java.util.ArrayList<Argument> margs = new java.util.ArrayList<Argument>();
	     CmdGenerator.buildLocalCmd(programDoc, valuePairs, margs);
	     String script = ScriptGenerator.buildSIDGridSwiftScript(47, "test",programDoc, margs, "./mobylexml");
	     System.out.println(script);
  }
  
  public static void genPraatFilesScript() throws Exception{
	     File xmlFile = new File("./mobylexml/praat.xml"); 
		 Hashtable<String,String> valuePairs = new Hashtable<String,String>();
		 valuePairs.put("inscript", "http://sidgrid.uc.teragrid.org/praat-test/pitch.praat");
		 String input1 = "http://sidgrid.uc.teragrid.org/praat-test/elan-example1.wav";
		 String input2 = "http://sidgrid.uc.teragrid.org/praat-test/elan-example2.wav";
		 valuePairs.put("infile", input1);
		 valuePairs.put("outfile", "output1");
	     
	     ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
	     java.util.ArrayList<Argument> margs = new java.util.ArrayList<Argument>();
	     CmdGenerator.buildLocalCmd(programDoc, valuePairs, margs);
	     String script = ScriptGenerator.buildSwiftFilesScript(programDoc, margs, "./mobylexml");
	     System.out.println(script);
}

  public static void genSgflowFilesScript() throws Exception{
	     File xmlFile = new File("./mobylexml/sgflow.xml"); 
		 Hashtable<String,String> valuePairs = new Hashtable<String,String>();
		 String input1 = "2";
		 String input2 = "out";
		 valuePairs.put("pow", input1);
		 valuePairs.put("bus", input1);
		 valuePairs.put("out", input2);
	     
	     ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
	     java.util.ArrayList<Argument> margs = new java.util.ArrayList<Argument>();
	     CmdGenerator.buildLocalCmd(programDoc, valuePairs, margs);
	     String script = ScriptGenerator.buildSwiftFilesScript(programDoc, margs, "./webapp/mobylexml/gadget/");
	     System.out.println(script);
}

  public static void genGunzipScript() throws Exception{
	    Hashtable<String,String> valuePairs = new Hashtable<String,String>();
		valuePairs.put("c", "1");
		valuePairs.put("infile", "http://sidgrid.uc.teragrid.org/praat-test/elan-example1.wav");
		valuePairs.put("outfile", "test.tar");
     
         String filename = "/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml/gunzip.xml";
         File xmlFile = new File(filename);
	     ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
	     java.util.ArrayList<Argument> margs = new java.util.ArrayList<Argument>();
	     CmdGenerator.buildLocalCmd(programDoc, valuePairs, margs);
	     String script = ScriptGenerator.buildSwiftScript(programDoc, margs, "./webapp/mobylexml/gadget/");
	     System.out.println(script);
  }
  public static void main(String[] args) throws Exception{
	  TestScriptGenerator.genSgflowFilesScript();
	  //TestScriptGenerator.genGunzipScript();
  }
}
