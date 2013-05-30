package org.uc.sidgrid.services;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.sql.Timestamp;
import org.uc.sidgrid.data.FileInfo;

public class FileBrowsingService {
   private String rootURL;
   private String rootPath;
   
   public void setURL(String rootURL){
	   this.rootURL = rootURL;
   }
   public void setRootPath(String rootPath){
	   this.rootPath = rootPath;
   }
   // check the info for a file
   public FileInfo readFileInfo(String filename){
	   File file = new File(filename);
	   FileInfo info = new FileInfo();
	   info.setFileSize(file.length());
	   info.setName(filename);
	   long time = file.lastModified();
	   Timestamp t = new Timestamp(time);
	   info.setCreateTime(t.toString());
	   String download = this.rootURL+"/filedownload?file="+filename;
	   info.setDownload(download);
	   return info;
   }
   //TODO: later add plugins to read files with different mime types
   // write the input string to the temp folder
   public String writeStringToFile(String name, String input){
	   //create a random file name for the input
	   String uuid = WorkflowFactory.getUUID();
	   File tmpdir = new File(this.rootPath+"temp/"+name+"-"+uuid);
	   if(!tmpdir.exists()){
		   tmpdir.mkdirs();
	   }
	   //String filename = this.rootPath+"/temp/"+name+"-"+uuid;
	   String filename = tmpdir.getAbsolutePath()+"/"+name;
	   try {
	    FileWriter writer = new FileWriter(filename);
	    writer.write(input);
	    writer.close();
	    String url = this.rootURL+"/temp/"+name+"-"+uuid+"/"+name;
	    return url;
	   } catch (IOException e){
		   e.printStackTrace();
		   return null;
	   }
   }
}
