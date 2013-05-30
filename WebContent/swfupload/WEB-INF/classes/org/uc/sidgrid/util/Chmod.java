package org.uc.sidgrid.util;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uc.sidgrid.services.WorkflowFactory;
import org.uc.sidgrid.util.StreamGobbler;

public class Chmod {
  private static Log log = LogFactory.getLog(Chmod.class);
	
  public native static void chmod(String filename, String mode);
  public static void createDirectory(String dirname)throws Exception{
	  String cmdLine = "mkdir -p "+dirname;
	  Process proc = Runtime.getRuntime().exec(cmdLine);
	  proc.waitFor();
	  int exitVal = proc.exitValue();
	  System.out.println("create dir exitValue: " + exitVal + " "+cmdLine); 
  }
  public static void setFileMod(String filename, String mode) throws Exception{
    String cmdLine = "chmod "+mode+" "+filename;
	Process proc = Runtime.getRuntime().exec(cmdLine);
	proc.waitFor();
	int exitVal = proc.exitValue();
	System.out.println("setFileMod exitValue: " + exitVal + " "+cmdLine); 
  }
  public static void writeFiles(String filename, String script, String inputLinks, String outputLinks)throws Exception{
	  String cmdLine = "python "+filename;
	  Process proc = Runtime.getRuntime().exec(cmdLine);
	  proc.waitFor();
	  int exitVal = proc.exitValue();
	  System.out.println("writeFiles exitValue: " + exitVal + " "+cmdLine);  
  }
  public static int createFileLink(String filename, String symbol) throws Exception{
	  String cmdLine = "ln -s "+filename+" "+symbol;
	  Process proc = Runtime.getRuntime().exec(cmdLine);
	  proc.waitFor();
	  int exitVal = proc.exitValue();
	  System.out.println("createFileLink exitValue: " + exitVal+" "+cmdLine); 
	  return exitVal;
  }
  public static int nativeCpDir(String srcdir, String desdir)throws Exception{
	  /** jvm doesn't know the pattern match character '*'
	   *  so we use a cp.sh for copying files. (cp.sh has to be visibile)
	   *  we should move on to jdk1.6 to fix this urgly code.
	   */
	  //String cmdLine = "cp "+srcdir+"/* "+desdir;
	  String cmdLine = "cp.sh "+srcdir+" "+desdir;
	  Process proc = Runtime.getRuntime().exec(cmdLine);
	  InputStream out = proc.getErrorStream();
	  StreamGobbler outputGobbler = new StreamGobbler(out, "Output");
      //errorGobbler.start();
      outputGobbler.start();
	  proc.waitFor();
	  int exitVal = proc.exitValue();
	  System.out.println("nativeCpDir: " + exitVal+" "+cmdLine); 
	  return exitVal;
  }
  // copy the directory
  public static void copyDirectory(File srcPath, File dstPath)
  throws IOException{
	  if (srcPath.isDirectory()){
		  if (!dstPath.exists()){
			  dstPath.mkdir();
		  }
		  String files[] = srcPath.list();
		  for(int i = 0; i < files.length; i++){
			  copyDirectory(new File(srcPath, files[i]), 
					  new File(dstPath, files[i]));
		  }
	  } else{
		  if(!srcPath.exists()){
			  log.error("File or directory does not exist. "+srcPath.getAbsolutePath());
			  return;
		  } else {
			  InputStream in = new FileInputStream(srcPath);
			  OutputStream out = new FileOutputStream(dstPath); 
			  // Transfer bytes from in to out
			  byte[] buf = new byte[1024];
			  int len;
			  while ((len = in.read(buf)) > 0) {
				  out.write(buf, 0, len);
			  }
			  in.close();
			  out.close();
			  // jdk1.5 doesn't support this yet.
			  //out.setExecutable(true);  
		  }
	  }
	  log.debug(srcPath.getAbsolutePath()+" Directory copied.");
  	}
   // create symbolic links 
   public static void createSymLinks(String workdir, List<String> workflowids)throws Exception{
	   for(String workflowid : workflowids){
		   String [] tmp = workflowid.split("-");
		   String date = tmp[1];
		   String path = "../../"+date+"/"+workflowid;
		   String cmdLine = "ln -s "+path+" "+workflowid;
		   log.info("create symbolic link =>"+cmdLine);
		   File dir = new File(workdir);
		   Process proc = Runtime.getRuntime().exec(cmdLine, null, dir);
		   proc.waitFor();
		   int exitVal = proc.exitValue();
		   log.info("create symbolic link =>"+exitVal);
	   }
   }
}
