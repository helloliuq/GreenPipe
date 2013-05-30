package org.uc.sidgrid.mobyle.core;

import java.util.List;
import java.util.Hashtable;

import org.uc.sidgrid.mobyle.*;
import org.uc.sidgrid.mobyle.ParametersDocument.Parameters;
import org.uc.sidgrid.mobyle.ParameterDocument.Parameter;
/**
 * @deprecated
 * @author wenjun wu
 *
 */
public class BuildMobylePage {
	public Hashtable processXML(ProgramDocument thisMobyle) {
		Hashtable attHash = new Hashtable();
		org.uc.sidgrid.mobyle.ProgramDocument.Program program = thisMobyle.getProgram();
		org.uc.sidgrid.mobyle.HeadDocument.Head head = program.getHead();
		String thisApp = head.getName();
        //Now get parameters
		return null;
	}
	 /**
     *  Used to process parameters in the unmarshalled XML.
     *  The parameters can come from 2 places, 1) off the original pise object
     *  or 2) recursively through parameter of type paragraph.
     *  @param theseparms Parameters type object that contains all the
     *         Parameters.
     *  @param attHash the Hashtable containing the command line objects. This
     *         will be populated by this method.
     *  @param inParagraph  boolean to indicate whether the parameters
     *         already belong to a paragraph (and thus may have a default group
     *         number). This is method 2) above.
     *  @param paraName String containing the name of the paragraph the
     *         parameters belong too. This is only applicable if
     *         inParagraph=true. 
     *  @return void
     */
    void processParameters(Parameters theseparms, Hashtable attHash, boolean inParagraph, String paraName) {
        // See how many parameters we have
    	List<org.uc.sidgrid.mobyle.ParameterDocument.Parameter> paraList = theseparms.getParameterList();
    	int numparms = theseparms.sizeOfParameterArray();
    	for (int i=0;i<numparms;i++) {
    		/** theseparms.getParametersItem(i);
    		//If Paragraph
    		if (thistype.toString().equals("Paragraph")) { 
    			
    		} else {
    			
    		} **/
    	}
    }
	static public void main(String args[]){
		
	}
}
