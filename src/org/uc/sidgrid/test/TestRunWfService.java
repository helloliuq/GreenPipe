package org.uc.sidgrid.test;

import java.io.File;
import java.util.Hashtable;

import org.uc.sidgrid.mobyle.ProgramDocument;
import org.uc.sidgrid.mobyle.core.CmdGenerator;
import org.uc.sidgrid.services.*;

public class TestRunWfService {
	public static void runClustalw(){
		WorkflowFactory wfFac = WorkflowFactory.getInstance();
		wfFac.setLocalPath("E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/workflows/");
		wfFac.setSidgridDataPath("E:/prj/sidgrid/sample-data1");
		wfFac.setUploadDataPath("E:/prj/sidgrid/sample-data1");
		WorkflowService wfsrv = new WorkflowService();
		wfsrv.setAppFilePath("E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml");
		
		Hashtable<String,String> valuePairs = new Hashtable<String,String>();
	    valuePairs.put("infile", "http://sidgrid.uc.teragrid.org/AFI-example/AFIT.mp4");
	    valuePairs.put("quicktree", "slow");
	    valuePairs.put("outfile", "output");
		wfsrv.runWorkflow("wwjag", "clustalw-multialign", valuePairs);
	}
	public static void runPraat(){
		WorkflowFactory wfFac = WorkflowFactory.getInstance();
		wfFac.setLocalPath("/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/workflows/");
		wfFac.setSidgridDataPath("/E:/prj/sidgrid/sample-data1");
		wfFac.setUploadDataPath("/E:/prj/sidgrid/sample-data1");
		WorkflowService wfsrv = new WorkflowService();
		wfsrv.setAppFilePath("/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml");
		
		Hashtable<String,String> valuePairs = new Hashtable<String,String>();
		valuePairs.put("inscript", "http://sidgrid.uc.teragrid.org/praat-test/pitch.praat");
	    valuePairs.put("infile", "http://sidgrid.uc.teragrid.org/praat-test/elan-example1.wav");
	    valuePairs.put("outfile", "output");
		wfsrv.runWorkflow("wwjag", "praat", valuePairs);
	}
	public static void runSIDGridPraat(){
		WorkflowFactory wfFac = WorkflowFactory.getInstance();
		wfFac.setLocalPath("/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/workflows/");
		wfFac.setSidgridDataPath("/E:/prj/sidgrid/sample-data1");
		wfFac.setUploadDataPath("/E:/prj/sidgrid/sample-data1");
		WorkflowService wfsrv = new WorkflowService();
		wfsrv.setAppFilePath("/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml");
		
		Hashtable<String,String> valuePairs = new Hashtable<String,String>();
		valuePairs.put("inscript", "http://sidgrid.uc.teragrid.org/praat-test/pitch.praat");
	    valuePairs.put("infile", "http://sidgrid.uc.teragrid.org/praat-test/elan-example1.wav");
	    valuePairs.put("outfile", "output");
		wfsrv.runSIDGridWorkflow("wwjag", 47, "test", "praat", valuePairs);
	}
	public static void runFFMPEG(){
		WorkflowFactory wfFac = WorkflowFactory.getInstance();
		wfFac.setLocalPath("/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/workflows/");
		wfFac.setSidgridDataPath("/E:/prj/sidgrid/sample-data1");
		wfFac.setUploadDataPath("/E:/prj/sidgrid/sample-data1");
		WorkflowService wfsrv = new WorkflowService();
		wfsrv.setAppFilePath("/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml");
		

		 Hashtable<String,String> valuePairs = new Hashtable<String,String>();
	     //valuePairs.put("infile", "http://sidgrid.uc.teragrid.org/praat-test/elan-example1.wav");
	     valuePairs.put("infile", "http://sidgrid.uc.teragrid.org/praat-test/elan-example1.wav");
	     valuePairs.put("outfile", "output.mp4");
	     valuePairs.put("acodec", "ac3");
	     //valuePairs.put("abitrate", "192000");
	     wfsrv.runWorkflow("wwjag", "ffmpeg", valuePairs);

	}
	public static void runOops(){
		WorkflowFactory wfFac = WorkflowFactory.getInstance();
		wfFac.setLocalPath("/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/workflows/");
		wfFac.setSidgridDataPath("/E:/prj/sidgrid/sample-data1");
		wfFac.setUploadDataPath("/E:/prj/sidgrid/sample-data1");
		WorkflowService wfsrv = new WorkflowService();
		wfsrv.setAppFilePath("/E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml");
		
		Hashtable<String,String> valuePairs = new Hashtable<String,String>();
	    valuePairs.put("plist", "http://sidgrid.uc.teragrid.org/oops-test/protein.fasta");
	    valuePairs.put("nsims", "2");
	    valuePairs.put("st", "10");
	    valuePairs.put("tui", "5");
	    valuePairs.put("coeff", "0.2");
	    wfsrv.runWorkflow("wwjag", "oops", valuePairs);
	}
	public static void main(String[] args) throws Exception{
		// 1) runClustalW();
		//runPraat();
		//runSIDGridPraat();
		//runFFMPEG();
		runOops();
	}
}
