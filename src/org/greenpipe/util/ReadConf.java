package org.greenpipe.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Read file by a field name
 * @author 
 *
 */
public class ReadConf {
	private String fileName;
	
	public ReadConf(String fileName) {
		this.fileName = fileName;
	}
	
	public String readFileByField(String fieldName) {
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(fileName)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0 || line.charAt(0) == '#') {
					continue;
				}

				if (line.indexOf(fieldName) != -1) {
					int index = line.indexOf("=");
					if (!line.substring(0, index).trim().equals(fieldName)) {
						continue;
					}
					
					line = line.substring(index+1).trim();
					index = line.indexOf("#");
					if (index != -1)
						line = line.substring(0, index);
					
					br.close();
					return line.trim();
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
