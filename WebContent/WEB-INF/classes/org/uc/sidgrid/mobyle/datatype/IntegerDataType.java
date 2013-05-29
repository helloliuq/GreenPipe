package org.uc.sidgrid.mobyle.datatype;

public class IntegerDataType {
	public Object convert(String value, org.uc.sidgrid.mobyle.ParameterDocument.Parameter param){
		  if ( value == null )
	          return 0;
		  return Integer.parseInt(value);
	      
	  }
}
