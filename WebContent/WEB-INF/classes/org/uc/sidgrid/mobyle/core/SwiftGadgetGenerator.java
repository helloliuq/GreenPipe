package org.uc.sidgrid.mobyle.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.*;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uc.sidgrid.dao.DAOFactory;
import org.uc.sidgrid.mobyle.ParameterDocument;
import org.uc.sidgrid.mobyle.ProgramDocument;
import org.uc.sidgrid.mobyle.TypeDocument;

/**
 * This gadget generator is designed for creating a gadget interface to invoking
 * Swift workflows
 * @author wenjun wu
 *
 */
public class SwiftGadgetGenerator extends GadgetGenerator{
	private String templateDir;
	private String webURL;
	private String uploadURL;
	private String SwiftXSLT = "swift-gadget.xsl";
	private static SwiftGadgetGenerator instance = null;
	private static Log log = LogFactory.getLog(DAOFactory.class);
	
	
	public static SwiftGadgetGenerator getInstance() {
		   if(instance == null) {
		         instance = new SwiftGadgetGenerator();
		   }
		   return instance;
	}
	private SwiftGadgetGenerator(){
	  super();
	}
	public void setTemplateDir(String templatedir){
		this.templateDir = templatedir;
		super.setTemplateDir(templatedir);
	}
	public void setWebURL(String weburl){
		this.webURL = weburl;
		System.out.println("setWebURL: " + weburl );
	}
	public void setUploadURL(String uploadurl){
		this.uploadURL = uploadurl;
	}
	public String createSwiftGadget(String gadgetName, String scriptName, String version, String appXML) throws Exception{
		  String html = createSwiftGadgetHTML(appXML);
		  File xmlFile = new File(appXML); 
		  ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
		  String js = createJavaScript(programDoc,scriptName, version);
		  StringTemplateGroup group = new StringTemplateGroup("myGroup", templateDir);
		  StringTemplate gadget_template = group.getInstanceOf("gadget-xml");
		  gadget_template.setAttribute("JavaScript", js);
		  gadget_template.setAttribute("HTMLSnippet", html);
		  gadget_template.setAttribute("GadgetName", gadgetName);
		  System.out.println("web server url = " + webURL );
		  gadget_template.setAttribute("WebServerUrl", webURL);
		  return gadget_template.toString();
	}
	/** public static String createGadget(String gadgetName, String weburl, String uploadurl, String appXML, String templateDir) throws Exception{
		  String html = GadgetGenerator.createSIDGridGadgetHTML(appXML);
		  File xmlFile = new File(appXML); 
		  ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
		  String js = createJavaScript(programDoc,templateDir,weburl, uploadurl);
		  StringTemplateGroup group = new StringTemplateGroup("myGroup", templateDir);
		  StringTemplate gadget_template = group.getInstanceOf("gadget-xml");
		  gadget_template.setAttribute("JavaScript", js);
		  gadget_template.setAttribute("HTMLSnippet", html);
		  gadget_template.setAttribute("GadgetName", gadgetName);
		  gadget_template.setAttribute("WebServerUrl", weburl);
		  return gadget_template.toString();
	  } **/
	public String createSwiftGadgetHTML(String xmlFile) throws Exception{
		  //InputStream in = GadgetGenerator.class.getClassLoader().getResourceAsStream(SwiftXSLT);
		  InputStream in = new FileInputStream(this.templateDir+"/"+SwiftXSLT);
		  System.out.println("gadget template file = "+ this.templateDir+"/"+SwiftXSLT);
		  TransformerFactory tFactory = TransformerFactory.newInstance();
		  Transformer transformer = tFactory.newTransformer(new StreamSource(in));
		  if (transformer ==null){ // this is a hack
			  System.err.println("can't find the XSLT file. fall back to axis2 loader");
			  tFactory.setURIResolver(new MyURIResolver());
			  InputStream in2 = org.apache.axis2.util.Loader.getResourceAsStream("/resources/"+SwiftXSLT);
			  transformer = tFactory.newTransformer(new StreamSource(in2));
			  //printLoaderChain(CharScanner.class.getClassLoader(), "check the CharScanner chain **");
		  } else {
			  //printLoaderChain(CharScanner.class.getClassLoader(), "check the CharScanner chain");
		  }
		  ByteArrayOutputStream result = new ByteArrayOutputStream();
		  Result r = new StreamResult(result);
		  transformer.transform(new StreamSource(xmlFile), r);
		  System.out.println("swift gadget html = " + result.toString() );
		  return result.toString();
	  }
	public String createJavaScript(ProgramDocument programDoc,  String scriptName, String version){
		 //load the template
		  //InputStream in = GadgetGenerator.class.getClassLoader().getResourceAsStream(GadgetTemplate);
		  StringTemplateGroup group = new StringTemplateGroup("myGroup", templateDir);
		  
		  List<Argument> args = new ArrayList<Argument>();
		  String appName = programDoc.getProgram().getHead().getName();
	 	  String xpath = "$this//xq:parameter";
	      ParameterDocument.Parameter[] myparas = (ParameterDocument.Parameter[])programDoc.selectPath(CmdGenerator.m_namespaceDeclaration+xpath);
	      for( ParameterDocument.Parameter para : myparas){
	    	  TypeDocument.Type type = para.getType();
	      	  String dataType = type.getDatatype().getClass1();
	      	  String paraname = para.getName();
	    	  Argument arg = new Argument(dataType, paraname, "");
	    	  args.add(arg);
	      }
	      StringTemplate gadget_template = group.getInstanceOf("gadget-swift");
	      gadget_template.setAttribute("inputArgs", args);
	      gadget_template.setAttribute("Application", appName);
	      gadget_template.setAttribute("WebServerUrl", webURL);
	      
         // find all input file parameters and create the SWFUpload DataBox for them
	      List<String> inFileSWFJs = new ArrayList<String>();
	      List<String> inFileVar = new ArrayList<String>();
	      StringTemplate swfupload = group.getInstanceOf("swfupload");
	      StringTemplate infileVarTemplate = group.getInstanceOf("swfupvar");
	      // handle the FileUpload in the gadget
	      for( ParameterDocument.Parameter para : myparas){
	    	  TypeDocument.Type type = para.getType();
	      	  String dataType = type.getDatatype().getClass1();
	      	  if ( dataType.equalsIgnoreCase("File") && para.isSetIsmaininput() && para.getIsmaininput().equals("1")){
	      		  swfupload.setAttribute("UploadServerUrl", uploadURL);
	      		  swfupload.setAttribute("WebServerUrl", webURL);
	      		  swfupload.setAttribute("Application", appName);
	      		  swfupload.setAttribute("arg_name", para.getName());
	      		  inFileSWFJs.add(swfupload.toString());
	      		  infileVarTemplate.setAttribute("arg_name", para.getName());
	      		  infileVarTemplate.setAttribute("Application", appName);
	      		  inFileVar.add(infileVarTemplate.toString());
	      		  swfupload.reset();
	      		  infileVarTemplate.reset();
	      	  }
	      }
	      gadget_template.setAttribute("inFilesArgs", inFileVar);
	      gadget_template.setAttribute("uploadArgs", inFileSWFJs);
	      gadget_template.setAttribute("Script", scriptName);
	      gadget_template.setAttribute("ScriptVersion", version);
	      //TODO: add the gadget publish-subscribe interface if any
	      return gadget_template.toString();
	}
}
