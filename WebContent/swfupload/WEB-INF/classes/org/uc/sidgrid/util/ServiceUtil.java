package org.uc.sidgrid.util;

import java.util.*;
import java.util.zip.*;
import java.io.*;
import java.net.URL;

public class ServiceUtil {
  public static List<String> listZipfiles(String zipfile) throws Exception{
	  //try {
	        // Open the ZIP file
	        ZipFile zf = new ZipFile(zipfile);
	        List filelist = new ArrayList();
	        // Enumerate each entry
	        for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
	            // Get the entry name
	            String zipEntryName = ((ZipEntry)entries.nextElement()).getName();
	            filelist.add(zipEntryName);
	        }
	        return filelist;
	    //} catch (IOException e) {
	    //	e.printStackTrace();
	    //	return null;
	    //}
  }
  public static void unzipFile(String inFilename, String outdir) throws Exception{
		    File dir = new File(outdir);
		    if (!dir.exists())
		    	dir.mkdirs();
	        // Open the ZIP file
		    ZipFile zf = new ZipFile(inFilename);
	        for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
	        // Get the first entry
	        ZipEntry entry = (ZipEntry)entries.nextElement();
	        // Open the output file
	        String outFilename = outdir+"/"+entry.getName();
	        InputStream in = zf.getInputStream(entry);
	        OutputStream out = new FileOutputStream(outFilename);
	        // Transfer bytes from the ZIP file to the output file
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	         // Close the streams
	         out.flush();
	         out.close();
	         in.close();
	        }
	        for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
	        	ZipEntry entry = (ZipEntry)entries.nextElement();
	        	String outFilename = outdir+"/"+entry.getName();
	        	if (outFilename.endsWith(".pl") || outFilename.endsWith("*.sh") || outFilename.endsWith("*.py")){
	        		Chmod.setFileMod(outFilename, "755");
	        		System.out.println("set the file "+outFilename+" to executable");
	        	}
	        }
	         zf.close();
  }
  public static void zipFile(List<String> filenames, String outFilename){
	  byte[] buf = new byte[1024]; 
	  try {  
		  ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename)); 
		  // Compress the files 
		  for (String filename : filenames) { 
			  FileInputStream in = new FileInputStream(filename); 
			  // Add ZIP entry to output stream. 
			  out.putNextEntry(new ZipEntry(filename)); 
			  // Transfer bytes from the file to the ZIP file 
			  int len; 
			  while ((len = in.read(buf)) > 0) { 
				  out.write(buf, 0, len); 
			  } 
			  // Complete the entry 
			  out.closeEntry(); 
			  in.close(); 
			} // Complete the ZIP file 
		    out.close(); 
		} catch (IOException e) { 
			e.printStackTrace();
		} 
  }
  public static void nativeUnzip(String inFilename, String outdir) throws Exception{
	    String cmdLine = "mkdir "+outdir+"; unzip -o "+inFilename+" -d "+outdir;
		Process proc = Runtime.getRuntime().exec(cmdLine);
		proc.waitFor();
		int exitVal = proc.exitValue();
		System.out.println("unzip exitValue: " + exitVal + " "+cmdLine); 
  }
  public static void nativeZip(List<String> filenames, String zipfile) throws Exception{
	    for(String filename : filenames){
	      String cmdLine = "zip "+zipfile+" "+filename;
	      Process proc = Runtime.getRuntime().exec(cmdLine);
	      proc.waitFor();
	      int exitVal = proc.exitValue();
	      System.out.println("zip exitValue: " + exitVal + " "+cmdLine); 
	    }
  }
  public static String locatePhysicalFile(String urlstr){
		try {
			URL url = new URL(urlstr);
			String path = url.getPath();
			String [] tmp = path.split("/");
			String fileName = "";
			for(int i=1; i<tmp.length; i++){
				fileName += tmp[i];
				if (i != tmp.length-1)
					fileName += "/";
			}
			return fileName;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
  public static void copyFile(String srcFile, String dstFile) throws Exception{
	    File src = new File(srcFile);
	    File dst = new File(dstFile);
	    //create the directories for dst if necessary
	    if (!dst.getParentFile().exists()){
	    	File parent = dst.getParentFile();
	    	parent.mkdirs();
	    }
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);
	    
	    byte[] buffer = new byte[1024];
	    int len;

	    while((len = in.read(buffer)) >= 0)
	      out.write(buffer, 0, len);

	    in.close();
	    out.close();
  }
  public static void writeNewFile(String fullFilePath, String data)throws Exception{
	  // get the file path
	  File file = new File(fullFilePath);
	  String path = file.getParent();
	  File parentPath = new File(path);
	  if (!parentPath.exists())
		  parentPath.mkdirs();
	  FileWriter writer = new FileWriter(fullFilePath);
	  writer.write(data);
	  writer.close();
  }
  public static void removeDir(String path){
	  File dir = new File(path);
	  deleteDirectory(dir);
  }
  public static boolean deleteDirectory(File path){
	  if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );

  }
  public static String scriptVersionPath(String scriptName, String version){
	  return scriptName+"("+version+")";
  }
  public static String baseName(String filepath){
	  String name = (new File(filepath)).getName();
	  return name;
  }
  public static void main(String[] args) throws Exception{
	  List<String> filelist = ServiceUtil.listZipfiles("E:/prj/life-science-gw/tmp/result.zip");
	  for(String filename : filelist){
		  System.out.println(filename);
	  }
	  //ServiceUtil.unzipFile("E:/prj/life-science-gw/tmp/result.zip", ".");
	  
  }
}
