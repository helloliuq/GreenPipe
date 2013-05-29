package org.uc.sidgrid.mobyle.datatype;

import java.util.List;

public class BooleanDataType {
	public Object convert(String value, org.uc.sidgrid.mobyle.ParameterDocument.Parameter param){
		  if ( value == null )
	          return 0;
		  return Integer.parseInt(value);
	      
	  }
}
