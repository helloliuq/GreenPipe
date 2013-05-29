package org.uc.sidgrid.mobyle.datatype;

import java.util.List;

//TODO: can we use annotation for this later?
public class ChoiceDataType {
  public String convert(String value, org.uc.sidgrid.mobyle.ParameterDocument.Parameter param){
	  if ( value == null )
          return null;
	  org.uc.sidgrid.mobyle.VlistDocument.Vlist vlist = param.getVlist();
	  List<org.uc.sidgrid.mobyle.VelemDocument.Velem> velems = vlist.getVelemList();
	  for(org.uc.sidgrid.mobyle.VelemDocument.Velem velem:velems){
		  if ( velem.isSetUndef()){
			  if (value.equalsIgnoreCase(velem.getValue())){
				  return null;
			  }
		  }
	  }
	  return value;
      
  }
  public boolean validate(org.uc.sidgrid.mobyle.ParameterDocument.Parameter param){
	  return true;
  }
}
