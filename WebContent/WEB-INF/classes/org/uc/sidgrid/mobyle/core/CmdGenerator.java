package org.uc.sidgrid.mobyle.core;
import java.io.File;
import java.lang.reflect.Method;

import org.uc.sidgrid.mobyle.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

import java.util.Comparator;
import java.util.List;
import java.util.Hashtable;
import org.python.core.PyObject;
import org.uc.sidgrid.mobyle.ParameterDocument.Parameter;
/**
 * this class contains methods to create commands based on application's xml and user's input
 * @author wenjun wu
 *
 */
public class CmdGenerator {
	private static Log log = LogFactory.getLog(CmdGenerator.class);
	public final static String m_namespaceDeclaration = 
	        "declare namespace xq='http://ci.uchicago.edu/sidgrid/sidgrid-mobyle';";
	 
    public static String getCommand(ProgramDocument programDoc){
    	org.uc.sidgrid.mobyle.ProgramDocument.Program program = programDoc.getProgram();
		org.uc.sidgrid.mobyle.HeadDocument.Head head = program.getHead();
		//TODO: cmd can be null!!
		org.uc.sidgrid.mobyle.CommandDocument.Command cmd = head.getCommand();
		XmlCursor cursor = cmd.newCursor();
		cursor.toLastAttribute();
        cursor.toNextToken();
        String cursorChars = cursor.getChars();
        return cursorChars;
    }
    public static String getPrompt(org.uc.sidgrid.mobyle.PromptDocument.Prompt prompt){
    	XmlCursor cursor = prompt.newCursor();
		cursor.toLastAttribute();
        cursor.toNextToken();
        String cursorChars = cursor.getChars();
        return cursorChars;
    }
    // parameter->parameters->paragraph->parameters->paragraph->... until the <program>
    public static String getArgPos(org.uc.sidgrid.mobyle.ParameterDocument.Parameter parameter){
    	if (parameter.isSetArgpos())
    		return parameter.getArgpos();
    	
    	XmlCursor cursor = parameter.newCursor();
    	while (cursor.toParent()){
    		XmlObject parent_tmp = cursor.getObject();
    		String type = parent_tmp.getDomNode().getLocalName();
    	    if ( type!= null && type.equalsIgnoreCase("paragraph")){
  			  //System.out.println("### "+type+" ####");
  			org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph parah_tmp = 
  				(org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph)parent_tmp;
  			  if (parah_tmp.isSetArgpos())
  				  return parah_tmp.getArgpos();
    	    }
    	}
        return "1";
    }
    public static String getArgPos(org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph parah){
    	if (parah.isSetArgpos())
    		return parah.getArgpos();
    	
    	XmlCursor cursor = parah.newCursor();
    	while (cursor.toParent()){
    		XmlObject parent_tmp = cursor.getObject();
    		String type = parent_tmp.getDomNode().getLocalName();
    	    if ( type!= null && type.equalsIgnoreCase("paragraph")){
  			  //System.out.println("### "+type+" ####");
  			org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph parah_tmp = 
  				(org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph)parent_tmp;
  			  if (parah_tmp.isSetArgpos())
  				  return parah_tmp.getArgpos();
    	    }
    	}
        return "1";
    }
    public static List<org.uc.sidgrid.mobyle.PrecondDocument.Precond> getPrecond(org.uc.sidgrid.mobyle.ParameterDocument.Parameter parameter){
    	List<org.uc.sidgrid.mobyle.PrecondDocument.Precond> list= parameter.getPrecondList();
    	XmlCursor cursor = parameter.newCursor();
    	while (cursor.toParent()){
    		XmlObject parent_tmp = cursor.getObject();
    		String type = parent_tmp.getDomNode().getLocalName();
    	    if ( type!= null && type.equalsIgnoreCase("paragraph")){
  			  org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph parah_tmp = 
  				(org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph)parent_tmp;
  			  org.uc.sidgrid.mobyle.PrecondDocument.Precond cond = parah_tmp.getPrecond();
  			  list.add(cond);
    	    }
    	}
    	return list;
    }
    public static String getPrecondCode(org.uc.sidgrid.mobyle.PrecondDocument.Precond precond, String prolang)
    {
    	List<org.uc.sidgrid.mobyle.CodeDocument.Code> codes = precond.getCodeList();
    	for(org.uc.sidgrid.mobyle.CodeDocument.Code tmp: codes){
    		if (tmp.getProglang().equalsIgnoreCase(prolang)) {
    			XmlCursor cursor = tmp.newCursor();
    			cursor.toLastAttribute();
                cursor.toNextToken();
                String cursorChars = cursor.getChars();
    			return cursorChars;
    		}
    	}
    	return null;
    }
    public static List<String> getVdefs(org.uc.sidgrid.mobyle.ParameterDocument.Parameter para){
    	if (para.isSetVdef() ){
    		org.uc.sidgrid.mobyle.VdefDocument.Vdef vdef = para.getVdef();
    		return vdef.getValueList();
    	} else 
    		return null;
    }
    public static String getVdef(org.uc.sidgrid.mobyle.ParameterDocument.Parameter para){
    	if (para.isSetVdef() ){
    		org.uc.sidgrid.mobyle.VdefDocument.Vdef vdef = para.getVdef();
    		return vdef.getValueList().get(0);
    	} else 
    		return null;
    }
    public static Object convertVdef(org.uc.sidgrid.mobyle.ParameterDocument.Parameter para, String value){
    	org.uc.sidgrid.mobyle.TypeDocument.Type type = para.getType();
    	String dataType = type.getDatatype().getClass1();
    	// find the right datatype java class
    	String className = "org.uc.sidgrid.mobyle.datatype."+dataType+"DataType";
    	try {
    	  log.info("doing the convert value "+className);
    	  Class dataTypeClass = Class.forName(className);
    	  Method convert = dataTypeClass.getMethod("convert", 
    			         new Class[] {String.class , org.uc.sidgrid.mobyle.ParameterDocument.Parameter.class});
    	  Object dataTypeObj = dataTypeClass.newInstance();
    	  Object newValue = convert.invoke(dataTypeObj, value, para);
    	  return newValue;
    	} catch (Exception e){
    		e.printStackTrace();
    		return value;
    	}
    }
    public static String getFormatCode(org.uc.sidgrid.mobyle.ParameterDocument.Parameter para, String prolang)
    {
    	org.uc.sidgrid.mobyle.FormatDocument.Format format = para.getFormat();
    	if (format== null)
    		return null;
    	List<org.uc.sidgrid.mobyle.CodeDocument.Code> codes = format.getCodeList();
    	for(org.uc.sidgrid.mobyle.CodeDocument.Code tmp: codes){
    		if (tmp.getProglang().equalsIgnoreCase(prolang)) {
    			XmlCursor cursor = tmp.newCursor();
    			cursor.toLastAttribute();
                cursor.toNextToken();
                String cursorChars = cursor.getChars();
    			
    			return cursorChars;
    		}
    	}
    	return null;
    }
    public static org.uc.sidgrid.mobyle.ParameterDocument.Parameter[] getAllParameterNameByArgpos(ProgramDocument programDoc){
         //find all parameters using xpath
    	 String xpath = "$this//xq:parameter";
         org.uc.sidgrid.mobyle.ParameterDocument.Parameter[] myparas = 
         	(org.uc.sidgrid.mobyle.ParameterDocument.Parameter[])programDoc.selectPath(m_namespaceDeclaration+xpath);
         //TODO: sort the array based on the Argpos!!
         java.util.Arrays.sort(myparas, new ParaArgComp());
         return myparas;
    }
    public static String buildLocalCmd(ProgramDocument programDoc, Hashtable<String,String> valuePairs, List<Argument> includedParas){
    	org.uc.sidgrid.mobyle.ParameterDocument.Parameter[] paras = getAllParameterNameByArgpos(programDoc);
    	boolean commandIsInserted=false;
    	int commandPos = 0;
    	String _cmdLine = "";
    	Evaluator evaluator = new Evaluator();
    	// set the context value-pair for the evaluator
    	java.util.Enumeration keys = valuePairs.keys();
    	while ( keys.hasMoreElements()){
    		String key = (String)keys.nextElement();
    		String value = valuePairs.get(key);
    		evaluator.setVar(key,value);
    	}
    	// find the command-parameter-name
    	org.uc.sidgrid.mobyle.ParameterDocument.Parameter cmdPara = null;
    	for (int i=0; i<paras.length; i++){
    		cmdPara = paras[i];
    		String paramName = cmdPara.getName();
    		if (cmdPara.isSetIscommand()){
    			break;
    		}
    	}
    	
    	if (cmdPara != null ){
    		commandPos = Integer.parseInt(getArgPos(cmdPara));
    	}
    	_cmdLine+= " "+getCommand(programDoc);
    	commandIsInserted = true;
    	log.info("current cmd "+_cmdLine);	
    	for (int i=0; i<paras.length; i++){
    		org.uc.sidgrid.mobyle.ParameterDocument.Parameter tmp = paras[i];
    		String paramName = tmp.getName();
    		log.info("para name "+paramName);
    		// add command to the command line
    		if ( !commandIsInserted && Integer.parseInt(getArgPos(paras[i])) >= commandPos){
    			commandIsInserted = true;
    		} else {
    			//_cmdLine+= ""+getCommand(programDoc);
    			commandIsInserted = true;
    		}
    		// prepare vdef
    		String rawVdef = getVdef(tmp);
    		Object convertedVdef = null;
    		if (rawVdef == null){
    			evaluator.setVar("vdef", null);
    			//myEvaluator.setVar( 'vdef' , None )
                //convertedVdef = None
    		} else {
    			convertedVdef = convertVdef(tmp, rawVdef);
    			evaluator.setVar("vdef", convertedVdef);
    			//myEvaluator.setVar( 'vdef' , convertedVdef )
    		}
    		// pass the value
    		if (valuePairs.containsKey(paramName)){
    			String inputV = valuePairs.get(paramName);
    			Object convertedInputV = convertVdef(tmp,inputV);
    			evaluator.setVar("value", convertedInputV);
    		} else {
    			evaluator.setVar("value", convertedVdef);
    		}
    		// check the precond
    		List<org.uc.sidgrid.mobyle.PrecondDocument.Precond> preconds = getPrecond(tmp);
    		boolean allPrecondTrue = true;
    		for(org.uc.sidgrid.mobyle.PrecondDocument.Precond tmpcond : preconds){
    			String precond = getPrecondCode(tmpcond, "python");
    			try { 
    			  if ( precond !=null && !evaluator.evalcond(precond) ){
    				allPrecondTrue = false;
    			    break;
    			  } 
    			} catch(Exception e){
    				e.printStackTrace();
    				allPrecondTrue = false;
    			    break;
    			 }
    		}
    		//eval the format code
    		if (allPrecondTrue){
    			String fmtCode = getFormatCode(tmp, "python");
    			if (fmtCode != null){
    				PyObject arg = evaluator.eval(fmtCode);
    				_cmdLine += " "+arg.toString();
    				String arg_cmd = arg.toString();
    				if (includedParas!=null && arg_cmd.length() > 0){
    					org.uc.sidgrid.mobyle.TypeDocument.Type type = tmp.getType();
    			    	String dataType = type.getDatatype().getClass1();
    					Argument tmpArg = new Argument(dataType, tmp.getName(), "");
    					String value =  valuePairs.get(tmp.getName());
    					tmpArg.setCmdLine(arg_cmd);
    					tmpArg.setValue(value);
    					
    					if ( tmp.isSetIsmaininput() && getBoolFromString(tmp.getIsmaininput())){
    					   tmpArg.setInput(true);
    					   arg_cmd = arg_cmd.replace(value, "@"+tmp.getName());
    					   //tmpArg.setCmdLine(arg_cmd);
    					}
    					if ( tmp.isSetIsout() && getBoolFromString(tmp.getIsout())){
    					   tmpArg.setOut(true);
    					   if (getBoolFromString(tmp.getIsstdout())){
    						 tmpArg.setIsStdout(true);
    						 arg_cmd = "stdout=@filename("+tmp.getName()+")";
    						 //arg_cmd = arg_cmd.replace(">"+value, "stdout=@filename("+tmp.getName()+")");
    					   } else 
    					    arg_cmd = arg_cmd.replace(value, "@"+tmp.getName());
    					   //tmpArg.setCmdLine(arg_cmd);
    					}
    					if ( tmp.isSetIsloop()&& getBoolFromString(tmp.getIsloop())){
    						tmpArg.setIsloop(true);
    						
    					}
    					// hacked: insert the quote to swift script if it doesn't have a starting @
    					//need to check swift people whether it is necessary
    					String[] tmps = arg_cmd.split(" ");
    					arg_cmd = "";
    					for(String tmpstr : tmps){
    						if (tmpstr.startsWith("@") || tmpstr.startsWith("stdout="))
    							arg_cmd += tmpstr+" ";
    						else if (tmpstr.length()>0)
    							arg_cmd += "\""+tmpstr+"\" ";
    					}
    					tmpArg.setCmdLine(arg_cmd);
    					includedParas.add(tmpArg);
    				}else if (includedParas!=null && arg_cmd.length() == 0 && tmp.isSetIshidden()){
    				  //TODO: still add the parameter value into the includedParas
    					org.uc.sidgrid.mobyle.TypeDocument.Type type = tmp.getType();
    			    	String dataType = type.getDatatype().getClass1();
    					Argument tmpArg = new Argument(dataType, tmp.getName(), "");
    					String value =  valuePairs.get(tmp.getName());
    					tmpArg.setCmdLine("");
    					tmpArg.setName(tmp.getName());
    					tmpArg.setValue(value);
    					if ( tmp.isSetIsout() && getBoolFromString(tmp.getIsout()))
     					   tmpArg.setOut(true);
     					if ( tmp.isSetIsloop()&& getBoolFromString(tmp.getIsloop()))
      						tmpArg.setIsloop(true);
    					includedParas.add(tmpArg);
    				}
    			}
    		}
    		log.info("current cmd "+_cmdLine);	
    	}
    	return _cmdLine;
    }
    public static boolean getBoolFromString(String v){
    	if ( v!=null && v.equalsIgnoreCase("1"))
    		return true;
    	else
    		return false;
    }
	public static void main(String[] args) throws Exception{
		File xmlFile = new File("./mobylexml/clustalw-multialign.xml"); 

//		 Bind the instance to the generated XMLBeans types.
		ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);
		org.uc.sidgrid.mobyle.ProgramDocument.Program program = programDoc.getProgram();
		org.uc.sidgrid.mobyle.HeadDocument.Head head = program.getHead();
		System.out.println(head.getName());
		
		org.uc.sidgrid.mobyle.ParametersDocument.Parameters parameters = program.getParameters();
		java.util.List<org.uc.sidgrid.mobyle.ParameterDocument.Parameter> list = parameters.getParameterList();
		for(org.uc.sidgrid.mobyle.ParameterDocument.Parameter tmp:list){
			System.out.println(tmp.getName()+"=>*"+tmp.getIsmaininput());
		}
        java.util.List<org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph> list2 = parameters.getParagraphList();
        for(org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph tmp:list2){
			System.out.println(tmp.getName()+"=>"+tmp.getArgpos());
		}
        // find all parameters using xpath
        //String xpath = "$this/program/parameters/*/parameter";
        String xpath = "$this//xq:parameter";
        XmlObject[] results = programDoc.selectPath(m_namespaceDeclaration+xpath);
        org.uc.sidgrid.mobyle.ParameterDocument.Parameter[] myparas = 
        	(org.uc.sidgrid.mobyle.ParameterDocument.Parameter[])results;
                
        for(org.uc.sidgrid.mobyle.ParameterDocument.Parameter tmp:myparas){
        	String tmpArgpos= getArgPos(tmp);
        	System.out.println(tmp.getName()+":"+tmpArgpos+":"+tmp.getType());
        	List<org.uc.sidgrid.mobyle.PrecondDocument.Precond> preconds = getPrecond(tmp);
        	for(org.uc.sidgrid.mobyle.PrecondDocument.Precond tmp_precond: preconds){
        		String precond_code = getPrecondCode(tmp_precond,"perl");
        		System.out.println("perl precond "+precond_code);
        	}
        	System.out.println("precond "+getPrecond(tmp));
        	System.out.println("code "+tmp.isSetFormat()+":"+tmp.getFormat());
        	System.out.println("perl code "+getFormatCode(tmp, "perl"));
        	System.out.println("vdef "+tmp.isSetVdef()+":"+getVdef(tmp));
        	System.out.println("paramfile "+tmp.isSetParamfile()+":"+tmp.getParamfile());
        }
        System.out.println("******");
        //find all paragraph using xpath
        /** String xpath1 = "$this//paragraph";
        org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph[] myParahs = 
        	(org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph[])programDoc.selectPath(xpath1);
        for(org.uc.sidgrid.mobyle.ParagraphDocument.Paragraph tmp:myParahs){
        	System.out.println(tmp.getDomNode()+":"+tmp.getName()+":"+getArgPos(tmp));
        	
        } **/
        Hashtable<String,String> valuePairs = new Hashtable<String,String>();
        valuePairs.put("infile", "input");
        valuePairs.put("quicktree", "slow");
        valuePairs.put("output", "output");
        buildLocalCmd(programDoc, valuePairs, null);
	}
}
