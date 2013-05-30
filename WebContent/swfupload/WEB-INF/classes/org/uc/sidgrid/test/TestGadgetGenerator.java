package org.uc.sidgrid.test;

import org.uc.sidgrid.mobyle.core.GadgetGenerator;

public class TestGadgetGenerator {
	public static String gadgetDir = "E:/prj/life-science-gw/LSGWGrid/apache-tomcat-5.5.25/webapps/gadgets";
/*
	public static String generatePraat() throws Exception {
        String gadgetHtml =  GadgetGenerator.createSIDGridGadgetHTML("./mobylexml/praat.xml");
		System.out.println(gadgetHtml);
		return gadgetHtml;
	}
	public static String generateFFmpeg() throws Exception {
		String gadgetXML = GadgetGenerator.createSIDGridGadgetHTML("mobylexml/ffmpeg.xml");
		System.out.println(gadgetXML);
		return gadgetXML;
	}
	public static String generateOops() throws Exception{
		String xml = GadgetGenerator.createSIDGridGadgetHTML("./mobylexml/oops-hidden.xml");
		//System.out.println(xml);
		String gadgetUrl = "";
		String filename = "./mobylexml/oops-hidden.xml";
		xml = GadgetGenerator.createGadget("oops", gadgetUrl, filename,
				"E:/prj/life-science-gw/LSGWGrid/apache-tomcat-5.5.25/webapps/SIDGridPortal/mobylexml/gadget");
		System.out.println(xml);
		return xml;
	}
	public static String generateBlast() throws Exception{
		String xml = GadgetGenerator.createSIDGridGadgetHTML("./mobylexml/oops.xml");
		//System.out.println(xml);
		String gadgetUrl = "";
		String filename = "./mobylexml/blast.xml";
		xml = GadgetGenerator.createGadget("blast", gadgetUrl, filename,
				"E:/prj/life-science-gw/LSGWGrid/apache-tomcat-5.5.25/webapps/SIDGridPortal/mobylexml/gadget");
		System.out.println(xml);
		return xml;
	}
	public static String generateModPipe() throws Exception{
		String xml = GadgetGenerator.createSIDGridGadgetHTML("./mobylexml/oops.xml");
		//System.out.println(xml);
		String gadgetUrl = "";
		String filename = "E:/prj/life-science-gw/LSGWGrid/LSGWService/swiftscripts/modpipe/modpipe.xml";
		xml = GadgetGenerator.createGadget("modpipe", gadgetUrl, filename,
				"E:/prj/life-science-gw/LSGWGrid/apache-tomcat-5.5.25/webapps/SIDGridPortal/mobylexml/gadget");
		System.out.println(xml);
		return xml;
	}
	*/
	public static void main(String[] args) throws Exception{
		  //GadgetGenerator.createGadget("./mobylexml/praat.xml","./mobylexml/praat.html","./mobylexml/form.xsl");
		  //GadgetGenerator.createGadget("./mobylexml/ffmpeg.xml","./mobylexml/ffmpeg.html","./mobylexml/form.xsl");
		  //GadgetGenerator.createGadgetHTML("./mobylexml/ffmpeg.xml","./mobylexml/ffmpeg-gadget.xml","./mobylexml/sidgrid.xsl");
		   
		  /** String gadgetXML = GadgetGenerator.createGadget("Praat Gadget", 
				             "http://sidgrid.ci.uchicago.edu:8888/SIDGridGadgets", 
				             "http://sidgrid.ci.uchicago.edu:8888/SIDGridPortal", 
				             "./mobylexml/praat.xml",
		  		             "./mobylexml/gadget");
		  System.out.println(gadgetXML); **/
		  
		  //String gadgetHtml =  GadgetGenerator.createSIDGridGadgetHTML("./mobylexml/praat.xml");
		  //System.out.println(gadgetHtml);
		  
		  //String gadgetXML2 = GadgetGenerator.createGadget("Praat Gadget", "http://localhost:8080/jobsubportlet", "./mobylexml/praat.xml");
		  //System.out.println(gadgetXML2);
		  // read the generated html snippet. don't know how to get a string outcome from xslt transformer
		  // maybe we should try other APIs?

         //String gadgetHtml =  GadgetGenerator.createSIDGridGadgetHTML("E:\\prj\\sidgrid\\mobyle-xml\\test01.xml");
		 //System.out.println(gadgetHtml);
		 
		//generateOops();
		//generateBlast();
		
		
		//generateModPipe();
	  }
}
