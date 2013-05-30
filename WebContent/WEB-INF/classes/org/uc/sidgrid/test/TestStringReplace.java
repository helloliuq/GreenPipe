package org.uc.sidgrid.test;

public class TestStringReplace {
  public static void main(String[] argv){
	  String cmdline = "\"-nsims=1\"";
	  System.out.println(cmdline.replaceAll("\"",""));
	  System.out.println(cmdline);
  }
}
