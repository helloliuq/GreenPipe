package org.uc.sidgrid.mobyle.core;

import java.io.FileWriter;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uc.sidgrid.mobyle.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlTokenSource;
import org.uc.sidgrid.app.AppParameter;

/**
 * a builder for creating mobyle xmls. 
 * This class can create a mobyle dom from the parameter list and the application attributes
 * specified by user inputs. 
 * 
 * @author wenjun wu
 *
 */
public class MobyleBuilder {
	private static Log log = LogFactory.getLog(CmdGenerator.class);
	final static String namespace = "http://ci.uchicago.edu/sidgrid/sidgrid-mobyle";
	
	public final static int PlainArgument=0;
	public final static int LongArgument=2;
	public final static int ShortArgument=3;

	public final static int ValueOnlyArgument=0;
	public final static int SwitchArgument=1;
	public final static int DashEqualsArgument=2;
	public final static int DashSpaceArgument=3;
	public final static int DashDashEqualsArgument=4;
	public final static int DashDashSpaceArgument=5;
	public final static int DashDashSwitchArgument=6;

	// create the mobyle header
	public static HeadDocument createHead(String name, String version, String title, String description, 
			                      String command, String authors, String reference) throws Exception{
		// prepare the namespace
		XmlOptions opts = new XmlOptions();
		opts.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://ci.uchicago.edu/sidgrid/sidgrid-mobyle", "command"));
		
		HeadDocument headdoc = HeadDocument.Factory.newInstance();
		HeadDocument.Head head = headdoc.addNewHead();
		head.setName(name);
		head.setVersion(version);
		CommandDocument cmddoc = CommandDocument.Factory.parse("<command>"+command+"</command>", opts);
		head.setCommand(cmddoc.getCommand());
		// create <doc> ... </doc>
		DocDocument docDoc = DocDocument.Factory.newInstance();
		DocDocument.Doc doc = docDoc.addNewDoc();
		if ( description!= null && description.length()>0 ){
			opts.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://ci.uchicago.edu/sidgrid/sidgrid-mobyle", "description"));
			DescriptionDocument descDoc = DescriptionDocument.Factory.parse("<description><text lang=\"en\">"
					+description+"</text></description>", opts);
			doc.setDescription(descDoc.getDescription());
		}
		if ( authors!= null && authors.length()>0 ){
			doc.setAuthors(authors);
		}
		if (reference!= null && reference.length()>0){
			doc.setReferenceArray(0,reference);
		}
		//doc.setDescription(descDoc);
		head.setDoc(doc);
		log.info(headdoc.toString());
		return headdoc;
	}
	// create the parameter 
	public static ParameterDocument createParameter(String name, String prompt, String type, int argpos, 
			          List<String> default_values, Hashtable<String,String> vlist,  
			          String precond, String format,
			          boolean ismaininput, boolean ismandatory, boolean isout, boolean isstdout,
			          boolean issimple, boolean isloop) throws Exception{
		HashMap namespaceMap = new HashMap(); 
		namespaceMap.put("", namespace); 
		XmlOptions opts = new XmlOptions(); 
		opts.setLoadSubstituteNamespaces(namespaceMap); 
		
		ParameterDocument paraDoc = ParameterDocument.Factory.newInstance();
		ParameterDocument.Parameter para = paraDoc.addNewParameter();
		para.setName(name);
		if (argpos >0)
		  para.setArgpos(Integer.toString(argpos));
		// set prompt
		if (prompt!=null && prompt.length()>0){
			PromptDocument promptDoc = PromptDocument.Factory.parse("<prompt>"+prompt+"</prompt>", opts);
			para.addNewPrompt();
			para.setPromptArray(0,promptDoc.getPrompt());
		}
		// set data type
		TypeDocument typedoc;
		if (type!=null && prompt.length()>0){
			typedoc = TypeDocument.Factory.parse("<type><datatype><class>"+type+"</class></datatype></type>", opts);
		} else {
			typedoc = TypeDocument.Factory.parse("<type><datatype><class>Boolean</class></datatype></type>", opts);
		}
		para.setType(typedoc.getType());
		// set the default value
		if (default_values !=null){
			VdefDocument vdefDoc = VdefDocument.Factory.newInstance();
			VdefDocument.Vdef vdef = vdefDoc.addNewVdef();
			for(String tmp : default_values){
				vdef.addValue(tmp);
			}
			if ( (vdef.getValueList().size()) >0)
				para.setVdef(vdef);
		}
		if (vlist !=null){
			VlistDocument.Vlist myvlist = VlistDocument.Factory.newInstance().addNewVlist();
			Enumeration<String> enumer = vlist.keys();
			int i=0;
			while(enumer.hasMoreElements()){
				String label = enumer.nextElement();
				String value = vlist.get(label);
				VelemDocument velemdoc = VelemDocument.Factory.parse("<velem><value>"+value+"</value>"+"<label>"+label+"</label></velem>", opts);
				myvlist.setVelemArray(i, velemdoc.getVelem());
				i++;
			}
			if (vlist.size()>0)
				para.setVlist(myvlist);
		}
		if (precond !=null && precond.length()>0){
		   PrecondDocument preconddoc = PrecondDocument.Factory.parse("<precond><code proglang=\"python\">"+precond+"</code></precond>", opts);
		   para.setPrecondArray(0, preconddoc.getPrecond());
		}
		if (format !=null && format.length()>0){
		   FormatDocument fmtDoc = FormatDocument.Factory.parse("<format><code proglang=\"python\">"+format+"</code></format>", opts);
		   para.setFormat(fmtDoc.getFormat());
		}
		if (ismaininput){
			para.setIsmaininput("1");
		}
		if (ismandatory){
			para.setIsmandatory("1");
		}
		if (isout){
			para.setIsout("1");
		}
		if (isstdout){
			para.setIsstdout("1");
		}
		if (issimple){
			para.setIssimple("1");
		}
		if (isloop){
			para.setIsloop("1");
		}
		return paraDoc;
	}
	/**
	 * transform a ParameterDocument.Parameter to AppParameter for displaying
	 * @param param
	 * @return
	 */
    public static AppParameter readMobyleParam(ParameterDocument.Parameter param){
      AppParameter appParam = new AppParameter();
      appParam.setName(param.getName());
      // retrieve the prompt information
      PromptDocument.Prompt prompt = param.getPromptArray(0);
      //System.out.println(prompt.toString());
      // this is an ugly hack
      /** String tmp = prompt.toString();
      int indx_start = tmp.indexOf("\"en\">");
      indx_start += 5;
      int indx_end = tmp.indexOf("</xml-fragment>");
      String prompt_str = tmp.substring(indx_start, indx_end); **/
      XmlCursor cursor = prompt.newCursor();
	  cursor.toLastAttribute();
      cursor.toNextToken();
      String prompt_str = cursor.getChars();
      //System.out.println(prompt_str);
      appParam.setPrompt(prompt_str);
      appParam.setDataType(param.getType().getDatatype().getClass1());
      //make sure to get the 'python' language
      String fmt="";
      List<org.uc.sidgrid.mobyle.CodeDocument.Code> codelist = param.getFormat().getCodeList();
      for(org.uc.sidgrid.mobyle.CodeDocument.Code code : codelist){
    	  if (code.getProglang().equalsIgnoreCase("python")){
    		  fmt = code.toString();
    		  break;
    	  }
      }
  	  
  	  int indx_start = fmt.indexOf("<xml-fragment");
  	  fmt = fmt.substring(indx_start);
  	  System.out.println(fmt);
  	  
  	  int argType=ValueOnlyArgument;
  	  String paramName = param.getName().trim();
  	  String switchArgTemp = ".*-\\S*\\s*\"\\)\\[value\\].*";
  	  String longArgTemp = ".*-\\S*\\s*=\\s*\"\\s*\\+\\s*str\\(\\s*value\\s*\\)\\).*";
  	  String shortArgTemp = ".*-\\S*\\s*\"\\s*\\+\\s*str\\(\\s*value\\s*\\)\\)\\[value is not None.*";
  	  if (fmt.matches(switchArgTemp))
  		  argType = SwitchArgument;
  	  else if  (fmt.matches(longArgTemp)  )
  		  argType = LongArgument;
  	  else if  (fmt.matches(shortArgTemp))
  		  argType = ShortArgument;
  	  System.out.println("argType for "+paramName+" is "+argType);
  	  appParam.setArgType(argType);
  	  appParam.setInput(CmdGenerator.getBoolFromString(param.getIsmaininput()));
  	  appParam.setOut(CmdGenerator.getBoolFromString(param.getIsout()));
  	  appParam.setIsStdout(CmdGenerator.getBoolFromString(param.getIsstdout()));
  	  return appParam;
    }
    // create a mobyle programdoc from the parameter list and application description
    public static ProgramDocument createMobyleDom(String name,String command, String title, List<AppParameter> parameters) throws Exception{
    	ProgramDocument programDoc = ProgramDocument.Factory.newInstance();
		ProgramDocument.Program program = programDoc.addNewProgram();
		HeadDocument headdoc = MobyleBuilder.createHead(name, "", title, "",command, "", "");
		program.setHead(headdoc.getHead());
		ParametersDocument.Parameters paras = program.addNewParameters();
		for(int i=0; i<parameters.size(); i++){
			AppParameter tmp = parameters.get(i);
			String paraName = tmp.getName();
			String prompt = tmp.getPrompt();
			String type = tmp.getDataType();
			// the position of the argument in the command line
			int argpos = i+1;
			// <default_values, vlist, precond, format 
			String defaultValue = "";
			//int argumentType = PlainArgument;
			int argumentType = tmp.getArgType();
			//(1)  command line switch ( web UI is a check box ) (format like -a, -b)
			// ?? Long options
			//(2)  argument with default value (format like -a=value )
			//(3)  argument without default value (format like -a=value )
			//(4)  plain argument (format like $value )
			String fmt = "-"+name;
			String vlist = "";
			if ( argumentType == ValueOnlyArgument){
				fmt = "(\"\",str(value))[value is not None] ";
			}
			if ( argumentType == SwitchArgument){
				defaultValue = "0";
				fmt = "(\"\", \"-"+paraName+"\")[value]";
				type = "Boolean";
			}
			if ( argumentType == DashDashSwitchArgument){
				defaultValue = "0";
				fmt = "(\"\", \"--"+paraName+"\")[value]";
				type = "Boolean";
			}
			if ( argumentType == DashEqualsArgument){
				if ( defaultValue.length() <=0 )
				  fmt = "(\"\", \"-"+paraName+"=\"+str(value))[value is not None]";
				else{
				  fmt = "(\"\", \"-"+paraName+"=\"+str(value))[value is not None and value != vdef]";
				}
			}
			if ( argumentType == DashSpaceArgument){
				if ( defaultValue.length() <=0 )
					  fmt = "(\"\", \"-"+paraName+" \"+str(value))[value is not None]";
				else{
					  fmt = "(\"\", \"-"+paraName+" \"+str(value))[value is not None and value != vdef]";
				}
			}
			if ( argumentType == DashDashEqualsArgument){
				if ( defaultValue.length() <=0 )
				  fmt = "(\"\", \"--"+paraName+"=\"+str(value))[value is not None]";
				else{
				  fmt = "(\"\", \"--"+paraName+"=\"+str(value))[value is not None and value != vdef]";
				}
			}
			if ( argumentType == DashDashSpaceArgument){
				if ( defaultValue.length() <=0 )
				  fmt = "(\"\", \"--"+paraName+" \"+str(value))[value is not None]";
				else{
				  fmt = "(\"\", \"--"+paraName+" \"+str(value))[value is not None and value != vdef]";
				}
			}

			
			//TODO: set the attributes of the parameter
			//boolean ismaininput, ismandatory, boolean isout, boolean issimple
			log.info("ismaininput=>"+tmp.getInput()+" isout=>"+tmp.getOut()+
					" isstdout=>"+ tmp.getIsStdout()+" isloop=>"+tmp.getIsloop());
			boolean ismaininput = tmp.getInput();
			boolean ismandatory = true;
			boolean isout = tmp.getOut();
			boolean isstdout = tmp.getIsStdout();
			boolean issimple = true;
			boolean isloop = tmp.getIsloop() ;
			List<String> default_values = new ArrayList<String>();
			default_values.add(tmp.getValue());
			ParameterDocument paraDoc = MobyleBuilder.createParameter(paraName, prompt, type, i+1, default_values,null, null, 
					 fmt, ismaininput,ismandatory, isout,isstdout, issimple, isloop);
			paras.addNewParameter();
			paras.setParameterArray(i,paraDoc.getParameter());
		}
		return programDoc;
    }
	// output the mobyle xml file in a pretty way
	public static void outputXML(ParameterDocument programDoc, String fileName)throws Exception{
		FileWriter out = new FileWriter(fileName);
		XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		programDoc.save(out,opts);
		out.close();
	}
	public static void printErrors(ArrayList validationErrors)
    {
        System.out.println("Errors discovered during validation: \n");
        Iterator iter = validationErrors.iterator();
        while (iter.hasNext())
        {
            System.out.println(">> " + iter.next() + "\n");
        }
    }
	public static String readTextValue(XmlTokenSource token){
		XmlCursor cursor = token.newCursor();
		cursor.toLastAttribute();
	    cursor.toNextToken();
	    String value = cursor.getChars();
	    return value;
	}
}
