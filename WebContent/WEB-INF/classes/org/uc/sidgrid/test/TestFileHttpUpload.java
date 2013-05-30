package org.uc.sidgrid.test;
import java.io.*;
import java.net.*;

public class TestFileHttpUpload {
	public static void main(String[] args)
    throws Exception
 {

HttpURLConnection conn = null;
BufferedReader br = null;
DataOutputStream dos = null;
DataInputStream inStream = null;

InputStream is = null;
OutputStream os = null;
boolean ret = false;
String StrMessage = "";
String exsistingFileName = "E:\\sidgrid-experiment.sql";

String lineEnd = "\r\n";
String twoHyphens = "--";
String boundary =  "*****";


int bytesRead, bytesAvailable, bufferSize;

byte[] buffer;

int maxBufferSize = 1*1024*1024;


String responseFromServer = "";

String urlString = "http://sidgrid.ci.uchicago.edu:8888/SIDGridPortal/fileupload";


try
{
 //------------------ CLIENT REQUEST

 FileInputStream fileInputStream = new FileInputStream( new
File(exsistingFileName) );

 // open a URL connection to the Servlet 

 URL url = new URL(urlString);


 // Open a HTTP connection to the URL

 conn = (HttpURLConnection) url.openConnection();

 // Allow Inputs
 conn.setDoInput(true);

 // Allow Outputs
 conn.setDoOutput(true);

 // Don't use a cached copy.
 conn.setUseCaches(false);

 // Use a post method.
 conn.setRequestMethod("POST");

 conn.setRequestProperty("Connection", "Keep-Alive");

 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

 dos = new DataOutputStream( conn.getOutputStream() );

 dos.writeBytes(twoHyphens + boundary + lineEnd);
 dos.writeBytes("Content-Disposition: form-data; name=\"upload\";"
    + " filename=\"" + exsistingFileName +"\"" + lineEnd);
 dos.writeBytes(lineEnd);

 

 // create a buffer of maximum size

 bytesAvailable = fileInputStream.available();
 bufferSize = Math.min(bytesAvailable, maxBufferSize);
 buffer = new byte[bufferSize];

 // read file and write it into form...

 bytesRead = fileInputStream.read(buffer, 0, bufferSize);

 while (bytesRead > 0)
 {
  dos.write(buffer, 0, bufferSize);
  bytesAvailable = fileInputStream.available();
  bufferSize = Math.min(bytesAvailable, maxBufferSize);
  bytesRead = fileInputStream.read(buffer, 0, bufferSize);
 }

 // send multipart form data necesssary after file data...

 dos.writeBytes(lineEnd);
 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

 // close streams

 fileInputStream.close();
 dos.flush();
 dos.close();


}
catch (MalformedURLException ex)
{
 System.out.println("From ServletCom CLIENT REQUEST:"+ex);
}

catch (IOException ioe)
{
 System.out.println("From ServletCom CLIENT REQUEST:"+ioe);
}


//------------------ read the SERVER RESPONSE


try
{
 inStream = new DataInputStream ( conn.getInputStream() );
 String str;
 while (( str = inStream.readLine()) != null)
 {
  System.out.println("Server response is: "+str);
  System.out.println("");
 }
 inStream.close();

}
catch (IOException ioex)
{
 System.out.println("From (ServerResponse): "+ioex);

}

}

}
