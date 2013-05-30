package org.uc.sidgrid.mobyle.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.globus.swift.language.Assign;
import org.globus.swift.language.ProgramDocument;
import org.griphyn.vdl.karajan.CompilationException;
import org.griphyn.vdl.toolkit.VDLt2VDLx;
import org.uc.sidgrid.app.AppParameter;
import org.uc.sidgrid.mobyle.core.MobyleBuilder;


/**
 * this class parse the swift script and generate a mobyle description
 * for the command-line parameters for the script if possible
 * @author wenjun wu
 *
 */

public class SwiftToMobyle {
	private Logger logger = Logger.getLogger(SwiftToMobyle.class);
	public static final String Karajan_TEMPLATE_FILE = "Karajan.stg"; 
	private String TemplateDir = "swift";
	
	public void setTemplateDir(String templatedir){
		this.TemplateDir = templatedir;
	}
	/**
	 * 
	 * @param in: swift script file
	 * @return ProgramDocument of the parsed XML doc
	 * @throws CompilationException
	 */
	public  ProgramDocument compile(String swiftscript, String workdir) throws CompilationException {
		File swiftfile = new File(swiftscript);
		String swiftscript_basename = swiftfile.getName();
		String compiled_xml = workdir+"/"+swiftscript_basename.replace("swift", "xml");
		//File swiftxml = new File(swiftfile.getParent()+"/"+compiled_xml);
		File swiftxml = new File(compiled_xml);
		if (!swiftxml.exists()) {
			try {
			  VDLt2VDLx.compile(new FileInputStream(swiftscript), new PrintStream(new FileOutputStream(swiftxml)));
			} catch (Exception e){
				e.printStackTrace();
				throw new CompilationException(e.getMessage(),e);
			}
		}
		//Karajan me = new Karajan();
		StringTemplateGroup templates;
		try {
			// find the Karajan template
			InputStream input = new FileInputStream(TemplateDir+"/"+Karajan_TEMPLATE_FILE);
		    /**
		     * if input not found, we can do classloader
		     * class.getClassLoader().getResource(Karajan_TEMPLATE_FILE).openStream()
		     */
			templates = new StringTemplateGroup(new InputStreamReader(input));
		} catch(IOException ioe) {
			throw new CompilationException("Unable to load karajan source templates",ioe);
		}

		ProgramDocument programDoc;
		try {
			programDoc = parseProgramXML(compiled_xml);
		} catch(Exception e) {
			throw new CompilationException("Unable to parse intermediate XML",e);
		}
		//Program prog = programDoc.getProgram();
		return programDoc;
	}
	/**
	 * parse and validate the input swift XML file
	 * @param defs
	 * @return
	 * @throws XmlException
	 * @throws IOException
	 */
	private ProgramDocument parseProgramXML(String defs) throws XmlException, IOException {
      XmlOptions options = new XmlOptions();
	  Collection errors = new ArrayList();
	  options.setErrorListener(errors);
	  options.setValidateOnSet();
	  options.setLoadLineNumbers();

	  ProgramDocument programDoc;
	  programDoc  = ProgramDocument.Factory.parse(new File(defs), options);

	  if(programDoc.validate(options)) {
		System.out.println("Validation of XML intermediate file was successful");
	  } else {
		  System.out.println("Validation of XML intermediate file failed.");
			// these errors look rather scary, so output them at
			// debug level
		  System.out.println("Validation errors:");
		Iterator i = errors.iterator();
		while(i.hasNext()) {
			XmlError error = (XmlError) i.next();
			System.out.println(error.toString());
		}
		//System.exit(3);
	   }
	   return programDoc;
    }
	/**
	 * do the xquery of the programDoc to find all the argument definitions 
	 * @param programDoc
	 */
	//TODO: can't parse the statement like int nSim = strtoi(@arg("nSim","4"));
	public List<AppParameter>findArgParameters(ProgramDocument programDoc){
		String xpath = "declare namespace any='http://ci.uchicago.edu/swift/2009/02/swiftscript';$this//any:function";
		org.apache.xmlbeans.XmlObject[] tmps = 
			 (org.apache.xmlbeans.XmlObject[])programDoc.selectPath(xpath);
		List<AppParameter> paralist = new ArrayList<AppParameter> ();
		 //(1) find all the argument definitions
		for( org.apache.xmlbeans.XmlObject tmp: tmps){
			 //System.out.println(tmp.toString());
			 org.globus.swift.language.Function func = (org.globus.swift.language.Function)tmp;
			 if (func.getName().equalsIgnoreCase("arg")){
				 ///System.out.println(tmp.toString());
				 /** arg token has two children: one for arg name, the other for the default value **/
				 XmlCursor cursor1 = tmp.newCursor();
				 cursor1.toFirstChild();
				 String argName = cursor1.getTextValue();
				 cursor1.toNextSibling();
				 String argValue = cursor1.getTextValue();
				 //System.out.println("get argname "+argName+" => "+argValue);
			     AppParameter tmpParam = new AppParameter();
			     tmpParam.setName(argName); 
			     tmpParam.setValue(argValue);
			     
			     // These are swift arguments, which are always of the dashEquals type
			     tmpParam.setArgType(MobyleBuilder.DashEqualsArgument);
			     
				 // 101019 turam - don't use the line number as the prompt, use the name as above
			     tmpParam.setPrompt(argName); 
				 paralist.add(tmpParam);
				 
			 }
		 }
		 return paralist;
	}
	
}
