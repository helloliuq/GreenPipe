<%@ page
	import="java.io.*, java.util.*, javax.xml.*,javax.xml.xpath.*,org.w3c.dom.*, java.lang.StringBuffer, 
                org.xml.sax.*, javax.xml.namespace.*, javax.xml.parsers.*, java.security.MessageDigest"
%>
<%!
    public void copyFile(String srcPath, String dstPath) throws Exception{
      InputStream in = new FileInputStream(srcPath);
      OutputStream fileout = new FileOutputStream(dstPath);
      // Transfer bytes from in to out
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
               fileout.write(buf, 0, len);
      }
      in.close();
      fileout.close();
    }


    public String md5(String plainText) {
      try {
        MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
        mdAlgorithm.update(plainText.getBytes());
        
        byte[] digest = mdAlgorithm.digest();
        StringBuffer hexString = new StringBuffer();
        
        for (int i = 0; i < digest.length; i++) {
            plainText = Integer.toHexString(0xFF & digest[i]);
        
            if (plainText.length() < 2) {
                plainText = "0" + plainText;
            }
            hexString.append(plainText);
        }
        return(hexString.toString());
      } catch (Exception e ) {

      } finally {

      }
      return "";
 
    }

%>
<%
    java.util.Date date;
    String dstRoot = "../ext-runs";
    String in_dir = request.getParameter("dir");
    HttpSession sess = request.getSession();
    String sessId = ((String)sess.getAttribute("username"))+ "/" + md5(in_dir);
    dstRoot += "/"+sessId;
    System.out.println("* Prepare delivery of output");
    if (in_dir == null) {
        System.out.println("- input file folder name is null; returning");
        out.print("input file folder name is null");
    	return;
    }
    System.out.println("- input file folder name = " + in_dir );

   // TODO: clean up the existing .meta folder for this session
    String meta_dstRoot =  dstRoot + "/.meta";
    String meta_inDir = in_dir + "/.meta";
    if (new File(dstRoot).exists() && new File(dstRoot+"/index.html").exists() ) {
       System.out.println("- returning existing generated output from "+dstRoot);
       BufferedReader reader = new BufferedReader(new FileReader(dstRoot+"/index.html"));
       String line = "", oldtext = "";
       while((line = reader.readLine()) != null)
       {
              oldtext += line + "\r\n";
       }
       reader.close();
       oldtext = oldtext.replaceAll("WFURL", request.getContextPath()+"/ext-runs/"+sessId);
       out.print(oldtext);
    } else {
        System.out.println("- copying files to ext-runs directory: " + dstRoot);

        String outputxml = in_dir+"/output.xml";
        File outputxmlFile = new File(outputxml);
        if ( ! outputxmlFile.exists() ){
            System.out.println("- output xml file does not exist; returning");
            out.print("can't find the output.xml file");
            return;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(outputxmlFile);
    
        XPathFactory xpf = XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI);
        XPath xpe = xpf.newXPath();
        XPathExpression findLine = xpe.compile("//file/text()");
    
        Object result = findLine.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            String value = (nodes.item(i).getNodeValue()).trim();
            // copy the file to the temp folder
            String srcPath = in_dir+"/"+value;
            String dstPath = dstRoot+"/"+value;
            String dstPath_dir = new File(dstPath).getParent();
            new File(dstPath_dir).mkdirs();
            try {
                copyFile(srcPath,dstPath);
            } catch(Exception e) {
                System.out.println("Exception copying file: " + srcPath + " to " + dstPath + ": " + e.getMessage());
            }
        }

        if (new File(meta_inDir).exists()) {
            System.out.println("- meta dir exists in input dir; copying files");
                new File(meta_dstRoot).mkdirs();
		String[] files = new File(meta_inDir).list(new FilenameFilter() {
		    public boolean accept(File meta_inDir, String name) {
				return name.charAt(0) != '.';
		    }
		});

		Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
                
                String index_html_contxt = "";
                boolean get_index_html_ok = false;
		for (String file : files) {
		    if (!new File(meta_inDir, file).isDirectory()) {
                      String srcPath = meta_inDir+"/"+file;
                      String dstPath = dstRoot+"/"+file;

                      if ( ! file.endsWith(".html") ){
                          copyFile(srcPath, dstPath);
                       } else {
                          BufferedReader reader = new BufferedReader(new FileReader(srcPath));
                          String line = "", oldtext = "";
                          while((line = reader.readLine()) != null)
                          {
                                 oldtext += line + "\r\n";
                          }
                          reader.close();
                          String newtext = oldtext.replaceAll("WFURL", request.getContextPath()+"/ext-runs/"+sessId);
                          FileWriter writer = new FileWriter(dstPath);
                          writer.write(newtext);
                          writer.close();
                          if (file.equals("index.html") ){
                             index_html_contxt = newtext;
                             get_index_html_ok = true;
                          }
                        }  
                   } 
		} // end of for loop
		// return the index.html file only
                if (get_index_html_ok)
                   out.print(index_html_contxt);
                else
                   out.print("can't find the index.html at the .meta folder");
        } else {
          System.out.println("- meta dir does not exist in input dir; generating files");
          // run viz process
          new File(meta_dstRoot).mkdirs();
          // need to merge into the same jvm later
          String wfurl = request.getContextPath()+"/ext-runs/"+sessId+"/";
          String xsltproc_cmd = "java -jar /home/turam/smartgrid/gadgetworkspace/common/jars/saxon9he.jar";
          xsltproc_cmd +=" -xsl:/home/turam/smartgrid/gadgetworkspace/common/xsl/indextabs9.xsl";
          xsltproc_cmd +=" -s:"+in_dir+"/output.xml"+"  basepath="+wfurl;
          xsltproc_cmd += " rootname="+meta_dstRoot+"/index";
    
          Process proc = Runtime.getRuntime().exec(xsltproc_cmd);
          proc.waitFor();
          int exitVal = proc.exitValue();
          System.out.println("call xsltproc cmd "+xsltproc_cmd);
          System.out.println("finishing creating the HTMLs "+exitVal);
          // read the index.html
           BufferedReader reader = new BufferedReader(new FileReader(meta_dstRoot+"/index.html"));
           String line = "", oldtext = "";
           while((line = reader.readLine()) != null)
           {
                  oldtext += line + "\r\n";
           }
           reader.close();
           oldtext = oldtext.replaceAll("WFURL", request.getContextPath()+"/ext-runs/"+sessId);
           out.print(oldtext);
        }
    }
%>
