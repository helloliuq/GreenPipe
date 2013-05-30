package org.uc.sidgrid.mobyle.core;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Result;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.uc.sidgrid.mobyle.ParameterDocument;
import org.uc.sidgrid.mobyle.ProgramDocument;
import org.uc.sidgrid.mobyle.TypeDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;

import org.uc.sidgrid.mobyle.*;

import antlr.CharScanner;

/**
 * this class contains methods to create sidgrid gadgets based on application's xml
 * dependency: 1) the template (xslt & stringtemplate) for generating gadgets 2) mobyle schema
 * 
 * sidgrid.xsl --> gadget html part
 * gadget-xml.st --> the overall gadget
 * gadget-st --> the JavaScript part of the gadget
 * 
 * @author wenjun wu
 *
 */
public class GadgetGenerator {
  private String GadgetTemplateDir = "gadget";
  public static final String XSLTTemplate = "sidgrid.xsl";
  public static final String GadgetJSTemplate = "gadget.st";
  
  //public static final String XSLTTemplate = "swift-gadget.xsl";
  
  public GadgetGenerator(){
      
  }
  public void setTemplateDir(String templatedir){
	this.GadgetTemplateDir = templatedir;
  }
  public static void createGadgetHTML(String xmlFile, String htmlFile, String xsltFile) throws Exception{
	
	  TransformerFactory tFactory = TransformerFactory.newInstance();
	  Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFile));
	  transformer.transform(new StreamSource(xmlFile), new StreamResult(new FileOutputStream(htmlFile)));
	
  }
  public static String createGadgetHTML(String xmlFile, String xsltFile) throws Exception{
		
	  TransformerFactory tFactory = TransformerFactory.newInstance();
	  Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFile));
	  ByteArrayOutputStream result = new ByteArrayOutputStream();
	  Result r = new StreamResult(result);
	  transformer.transform(new StreamSource(xmlFile), r);
	  return result.toString();
  }
  public String createSIDGridGadgetHTML(String xmlFile) throws Exception{
	  //InputStream in = GadgetGenerator.class.getClassLoader().getResourceAsStream(XSLTTemplate);
	  InputStream in = new FileInputStream(GadgetTemplateDir+"/"+XSLTTemplate);
	  TransformerFactory tFactory = TransformerFactory.newInstance();
	  Transformer transformer = tFactory.newTransformer(new StreamSource(in));
	  if (transformer ==null){ // this is a hack
		  System.err.println("can't find the XSLT file. fall back to axis2 loader");
		  tFactory.setURIResolver(new MyURIResolver());
		  InputStream in2 = org.apache.axis2.util.Loader.getResourceAsStream("/resources/"+XSLTTemplate);
		  transformer = tFactory.newTransformer(new StreamSource(in2));
		  //printLoaderChain(CharScanner.class.getClassLoader(), "check the CharScanner chain **");
	  } else {
		  //printLoaderChain(CharScanner.class.getClassLoader(), "check the CharScanner chain");
	  }
	  ByteArrayOutputStream result = new ByteArrayOutputStream();
	  Result r = new StreamResult(result);
	  transformer.transform(new StreamSource(xmlFile), r);
	  return result.toString();
  }
  /**
   * create the JavaScript code for the gadget
   * @param programDoc
   * @param webUrl: the web root url
   * @param uploadUrl: the uploader url for the swf uploader widget in the gadget
   * @return
   */
  public  String createJavaScript(ProgramDocument programDoc, String webUrl, String uploadUrl){
	  //load the template
	//InputStream in = GadgetGenerator.class.getClassLoader().getResourceAsStream(GadgetTemplate);
	  StringTemplateGroup group = new StringTemplateGroup("myGroup", GadgetTemplateDir);
	  
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
      gadget_template.setAttribute("WebServerUrl", webUrl);
      
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
      		  swfupload.setAttribute("UploadServerUrl", uploadUrl);
      		  swfupload.setAttribute("WebServerUrl", webUrl);
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
      //TODO: add the gadget publish-subscribe interface if any
      return gadget_template.toString();
  }
  public static String readFile(String filename) throws Exception{
	  File file = new File(filename);
      FileInputStream input = new FileInputStream(file);
      int length = input.available();
      byte b[]= new byte[length];
      input.read(b);
      String content = new String(b);
      return content;
  }
  /**
   * create an OpenSocial gadget 
   * @param gadgetName
   * @param weburl: the root URL for the shindig container
   * @param appXML: the application description xml
   * @return
   * @throws Exception
   */
  public String createGadget(String gadgetName, String weburl, String appXML) throws Exception{
	  return createGadget(gadgetName, weburl, weburl, appXML);
  }
  public String createGadget(String gadgetName, String weburl, String uploadurl, String appXML) throws Exception{
	  String html = this.createSIDGridGadgetHTML(appXML);
	  File xmlFile = new File(appXML); 
	  ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
	  String js = this.createJavaScript(programDoc,weburl, uploadurl);
	  StringTemplateGroup group = new StringTemplateGroup("myGroup", this.GadgetTemplateDir);
	  StringTemplate gadget_template = group.getInstanceOf("gadget-xml");
	  gadget_template.setAttribute("JavaScript", js);
	  gadget_template.setAttribute("HTMLSnippet", html);
	  gadget_template.setAttribute("GadgetName", gadgetName);
	  gadget_template.setAttribute("WebServerUrl", weburl);
	  return gadget_template.toString();
  }
  
  private static void printLoaderChain(ClassLoader cl, String msg)
  {
      if (msg != null) System.err.println(msg);
      System.err.println("Classloader chain starts with:");
      while (cl != null)
      {
          System.err.println(cl);
          cl = cl.getParent();
      }
      System.err.println("--- chain ends");
  }
  public static void main(String[] args) throws Exception{
	  //GadgetGenerator.createGadget("./mobylexml/praat.xml","./mobylexml/praat.html","./mobylexml/form.xsl");
	  //GadgetGenerator.createGadget("./mobylexml/ffmpeg.xml","./mobylexml/ffmpeg.html","./mobylexml/form.xsl");
	  //GadgetGenerator.createGadgetHTML("./mobylexml/ffmpeg.xml","./mobylexml/ffmpeg-gadget.xml","./mobylexml/sidgrid.xsl");
	  GadgetGenerator generator = new GadgetGenerator();
	  generator.setTemplateDir("mobylexml/gadget");
	  String gadgetXML = generator.createSIDGridGadgetHTML("./mobylexml/ffmpeg.xml");
	  System.out.println(gadgetXML);
	  
	  //String gadgetXML = createGadget("FFmpeg Gadget", "http://localhost:8080/jobsubportlet", "./mobylexml/ffmpeg.xml");
	  //System.out.println(gadgetXML);
	  
	  // read the generated html snippet. don't know how to get a string outcome from xslt transformer
	  // maybe we should try other APIs?

  }
}
