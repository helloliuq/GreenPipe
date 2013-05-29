package org.uc.sidgrid.test;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.uc.sidgrid.mobyle.core.Argument;

public class TestStringTemplate {
	public static void testStringTemplate(){
		StringTemplate hello = new StringTemplate("Hello $name$");
		hello.setAttribute("name", "World");
		System.out.println(hello.toString());
		
		StringTemplateGroup group = new StringTemplateGroup("myGroup", "./mobylexml");
		StringTemplate query = group.getInstanceOf("theQuery");
		query.setAttribute("column", "name");
		query.setAttribute("column", "email");
		query.setAttribute("table", "User");
		System.out.println(query.toString());
		
		StringTemplate loop = group.getInstanceOf("testloop");
		Argument[] argvs = new Argument[] {
			    new Argument("file", "Boris", "39"),
			    new Argument("integer", "Natasha", "31"),
			    new Argument("integer", "Jorge", "25"),
			    new Argument("integer", "Vladimir", "28")
			};
        loop.setAttribute("args", argvs);
        System.out.println(loop.toString());
        
        StringTemplate script = group.getInstanceOf("swift");
        String [] typedeflist = new String [] { "mediafile", "integer" };
        script.setAttribute("typedeflist", typedeflist);
        Argument[] outArgs = new Argument[] {
        		new Argument("mediafile", "outvid", "myout"),
        		new Argument("mediafile", "outeq", "myout2")
        };
        Argument[] inArgs = new Argument[] {
        		new Argument("scriptfile", "inscript", "/myscript"),
        		new Argument("mediafile", "input", "/myinput")
        };
        String [] Args = new String[] { "\"-nopgap\"", "@inscript", "-input=@input" };
        script.setAttribute("inArgs", inArgs);
        script.setAttribute("outArgs", outArgs);
        script.setAttribute("CmdArgs", Args);
        Argument[] argts = new Argument[] {
        		  new Argument("mediafile", "input", "/myinput"),
        		  new Argument("mediafile", "output", "/myoutput")
        };
        script.setAttribute("inoutArgs", argts);
        script.setAttribute("application", "praat");
        System.out.println(script.toString());
	}
}
