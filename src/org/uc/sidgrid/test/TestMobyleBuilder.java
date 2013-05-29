package org.uc.sidgrid.test;

import java.util.ArrayList;

import org.apache.xmlbeans.XmlOptions;
import org.uc.sidgrid.mobyle.HeadDocument;
import org.uc.sidgrid.mobyle.ParameterDocument;
import org.uc.sidgrid.mobyle.ParametersDocument;
import org.uc.sidgrid.mobyle.ProgramDocument;
import org.uc.sidgrid.mobyle.core.MobyleBuilder;

public class TestMobyleBuilder {
 public void createMobyleXML() throws Exception{
	    ProgramDocument programDoc = ProgramDocument.Factory.newInstance();
		ProgramDocument.Program program = programDoc.addNewProgram();
		HeadDocument headdoc = MobyleBuilder.createHead("test", "1.1", "test it", "this is a test", "runit", "", "");
		program.setHead(headdoc.getHead());
		
		/*
		ParameterDocument paraDoc = MobyleBuilder.createParameter("infile", "sequences file", "Sequence", 1, null,null, null, "\" -infile=\" + str( value )",
				 true,true,false,true);
		ParametersDocument.Parameters paras = program.addNewParameters();
		paras.addNewParameter();
		paras.setParameterArray(0,paraDoc.getParameter());
		*/
		
		/*
		ParameterDocument paraDoc2 = MobyleBuilder.createParameter("outfile", "sequences file", "Sequence", 1, null,null, null, "\" -outfile=\" + str( value )",
				 true,true,false,true);
		paras.addNewParameter();
		paras.setParameterArray(1,paraDoc2.getParameter());
		*/
		
		XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		System.out.println(programDoc.xmlText(opts));
		//log.info(programDoc.toString());
		/** validate the xml **/
		 ArrayList validationErrors = new ArrayList();
		 XmlOptions m_validationOptions = new XmlOptions();
		 m_validationOptions.setErrorListener(validationErrors);
		
	     boolean isValid = programDoc.validate(m_validationOptions);
      if (!isValid)
	        {
	            MobyleBuilder.printErrors(validationErrors);
	        }
  }
  public void getParameterList() {
	  org.uc.sidgrid.services.AppMgtService svr = new org.uc.sidgrid.services.AppMgtService();
	  svr.setAppFilePath("E:\\prj\\life-science-gw\\LSGWGrid\\SIDGridPortal\\mobylexml");
	  java.util.List<org.uc.sidgrid.app.AppParameter> paralist = svr.getParameters("ffmpeg");
	  
	  System.out.println(paralist.toString());
  }
  public void testFormtReg(){
	  String name="an";
	  String switchArgTemp = ".*-"+name+"\\s*\"\\)\\[value\\].*";
	  String longArgTemp = ".*-\\S*\\s*=\\s*\"\\s*\\+\\s*str\\(\\s*value\\s*\\)\\).*";
	  //String switchArgTemp = ".*"+name+".*";
	  // String input = "<xml-fragment proglang=\"python\">(\"\",\" -sameq \")[value]</xml-fragment>";
	  //String input = "<xml-fragment proglang=\"python\">(\"\",\" -an \")[value]</xml-fragment>";
	  String input = "<xml-fragment proglang=\"python\">( \"\" , \" -vframes=\"+str(value))[ value is not None]</xml-fragment>";
	  if (input.matches(longArgTemp)){
		  System.out.println("match!");
	  } else
		  System.out.println("no match");
  }
  public static void main(String[] args)throws Exception {
	  TestMobyleBuilder test = new TestMobyleBuilder();
	  //test.createMobyleXML();
	  test.getParameterList();
	  //test.testFormtReg();
  }
}
