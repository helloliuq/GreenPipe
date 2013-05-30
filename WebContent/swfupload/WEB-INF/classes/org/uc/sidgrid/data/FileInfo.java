package org.uc.sidgrid.data;

public class FileInfo {
  private String filename;
  private long filesize;
  private String createTime;
  private String download;
  
  public String getName(){
	  return this.filename;
  }
  public void setName(String name){
	  this.filename = name;
  }
  public long getFileSize(){
	  return this.filesize;
  }
  public void setFileSize(long size){
	  this.filesize = size;
  }
  public String getCreateTime(){
	  return this.createTime;
  }
  public void setCreateTime(String createTime){
	  this.createTime = createTime;
  }
  public String getDownload(){
	  return this.download;
  }
  public void setDownload(String download){
	  this.download = download;
  }
}
