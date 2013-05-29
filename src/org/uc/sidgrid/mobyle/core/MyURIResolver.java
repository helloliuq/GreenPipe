package org.uc.sidgrid.mobyle.core;

import javax.xml.transform.URIResolver;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

public class MyURIResolver implements URIResolver{
	//@Override
	public Source resolve(String href, String base) throws TransformerException {
	  try {
		System.out.println("My XSLT resolver "+href+":"+base);
	    InputStream is = org.apache.axis2.util.Loader.getResourceAsStream(href);
	    return new StreamSource(is, href);
	 } catch(Exception e){
		 e.printStackTrace();
		 throw new TransformerException(e);

	 }
	}
}
