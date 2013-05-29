package org.uc.sidgrid.test;
import java.io.File;

import org.uc.sidgrid.util.Chmod;

public class TestDirectoryCopy {
	
	public static void main(String[] args) throws Exception{
		File srcPath = new File("E:\\prj\\life-science-gw\\workflow");
		File dstPath = new File("E:\\prj\\life-science-gw\\tmp");
		Chmod.copyDirectory(srcPath, dstPath);
	}
}
