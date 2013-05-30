package org.uc.sidgrid.util;

import java.util.*;
import java.io.*;
public class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    private String lastLine;
    
    public StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    public String getLastLine(){
    	return this.lastLine;
    }
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null){
                System.out.println(type + ">" + line);
                this.lastLine = line;
                //TODO: parse the swift file for showing the workflow exec progress
            }
            } catch (IOException ioe)
              {
                ioe.printStackTrace();  
              }
    }
}

