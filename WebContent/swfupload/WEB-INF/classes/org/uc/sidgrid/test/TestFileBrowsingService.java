package org.uc.sidgrid.test;

import org.uc.sidgrid.services.FileBrowsingService;
import org.uc.sidgrid.data.FileInfo;

public class TestFileBrowsingService {
	public static void main(String[] args) throws Exception{
		FileBrowsingService filesrv = new FileBrowsingService();
		FileInfo info = filesrv.readFileInfo("/E:/study/web-2.0/jqueryFileTree/jqueryFileTree.js");
		System.out.println(info.getName());
		System.out.println(info.getFileSize());
		System.out.println(info.getCreateTime());
	}
}
