package org.uc.sidgrid.mobyle.core;

import java.util.Comparator;
import org.uc.sidgrid.mobyle.ParameterDocument.Parameter;

/**
 * compare two mobyle xml parameters
 * @author wenjun wu
 *
 */
public class ParaArgComp implements Comparator<Parameter>{
	
  public int compare(Parameter para1, Parameter para2){
	  int pos1= Integer.parseInt(CmdGenerator.getArgPos(para1));
	  int pos2= Integer.parseInt(CmdGenerator.getArgPos(para2));
	  if (pos1 > pos2)
		  return 1;
	  else if (pos1 == pos2)
		  return 0;
	  else 
		  return -1;
  }
  
  public boolean equal(Parameter para1, Parameter para2){
	  int pos1= Integer.parseInt(CmdGenerator.getArgPos(para1));
	  int pos2= Integer.parseInt(CmdGenerator.getArgPos(para2));
	  if (pos1 == pos2)
		  return true;
	  else
		  return false;
  }
}
