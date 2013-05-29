package org.uc.sidgrid.mobyle.datatype;

public class MultipleChoiceDataType {
	public String convert(String value, org.uc.sidgrid.mobyle.ParameterDocument.Parameter param){
		String sep = "";
		if (param.isSetSeparator()) {
			sep = param.getSeparator();
		}
		return value;
	}
}
