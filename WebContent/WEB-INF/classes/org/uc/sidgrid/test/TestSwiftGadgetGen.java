package org.uc.sidgrid.test;

import java.io.File;

import org.uc.sidgrid.mobyle.ProgramDocument;
import org.uc.sidgrid.mobyle.core.SwiftGadgetGenerator;

public class TestSwiftGadgetGen {
	public static void main(String[] args) throws Exception{
		String xml;
		//String xml = SwiftGadgetGenerator.createSIDGridGadgetHTML("./mobylexml/oops.xml");
		//System.out.println(xml);
		String gadgetUrl = "";
		//String filename = "E:/prj/life-science-gw/LSGWGrid/LSGWService/swiftscripts/modpipe/modpipe.xml";
		//String filename = "E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/mobylexml/oops.xml";
		String filename = "E:/prj/life-science-gw/LSGWGrid/SIDGridPortal/swiftscript/oops-analysis/analysis.xml";
		
		String weburl = "http://sidgrid.ci.uchicago.edu:8888/SIDGridPortal";
		String uploadurl = "http://sidgrid.ci.uchicago.edu:8888/SIDGridPortal/fileupload";
		
		SwiftGadgetGenerator generator = SwiftGadgetGenerator.getInstance();
		generator.setTemplateDir("mobylexml/gadget");
		generator.setUploadURL(uploadurl);
		generator.setWebURL(weburl);
		
		//xml = generator.createSwiftGadget("modpipe", "modpipe", "v1.0", filename);
		//xml = generator.createSwiftGadget("oops", "myoops", "v1.2", filename);
		xml = generator.createSwiftGadget("oops", "analysis", "v1.2", filename);
		
		//File xmlFile = new File(filename); 
		//ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
		//xml = SwiftGadgetGenerator.createJavaScript(programDoc, "mobylexml/gadget", gadgetUrl, gadgetUrl);
		
		
		System.out.println(xml);
	}
}
