<%@ page
	import="java.io.InputStreamReader,java.io.File,java.io.FileReader,java.io.BufferedReader,java.io.FilenameFilter,java.util.Arrays"%>
<%
    String dir = request.getParameter("dir");
    if (dir == null) {
    	return;
    }
	
	if (dir.charAt(dir.length()-1) == '\\') {
    	dir = dir.substring(0, dir.length()-1) + "/";
	}

    String dir2 = request.getContextPath() + "/filedownload?file=" + dir;
    boolean is_text = false;
    boolean is_image = false;
    if (new File(dir).exists()) {
         String previewHtml = "<div style='clear:both'>No preview available</div>";

         is_image = dir.endsWith(".png") || dir.endsWith(".jpg") || dir.endsWith(".gif");
         if( is_image == true ) {
             previewHtml = "<img src=" + dir2 + ">";
         }
         else if( dir.endsWith(".pdb") || dir.endsWith(".pdt") ) {
             previewHtml = "<script type='text/javascript' src='"+ request.getContextPath()+"/jmol/Jmol.js'></script><div id='3d_applet_1_1'></div><form><script type='text/javascript'>jmolInitialize('"+request.getContextPath()+"/jmol'); jmolSetAppletCssClass('jmol'); jmolSetDocument(null); applet_html = jmolApplet(400, 'load " + dir2 + ";cpk off;cartoon on;wireframe off;color structure');  document.getElementById('3d_applet_1_1').innerHTML = applet_html;</script> </form>";
         }
        else {
            Runtime r=Runtime.getRuntime();
            Process p=null;
            String cmd="file --mime " + dir;
            try{
                p=r.exec(cmd);
                InputStreamReader isr=new InputStreamReader(p.getInputStream());
                BufferedReader br=new BufferedReader(isr);
                String line=null;
                while((line = br.readLine()) != null){
                   //previewHtml += line;
                   if( line.contains("text") ) { is_text = true; }
                   if( line.contains("empty") ) { previewHtml = "<div style='clear:both'>No preview available; empty file</div>"; }
                   if( line.contains("image/") ) { previewHtml = "<img src=" + dir2 + ">"; }
                }
                p.waitFor();
            }
            catch(Exception e){
                //out.println(e);
            }
    
             if( is_text == true || dir.endsWith(".sh") == true ) {
                 previewHtml = "<textarea readonly='readonly' rows=25 style='width:99%'>";
                 BufferedReader input = new BufferedReader(new FileReader(dir));
                 String line = "";
                 Integer MAX = 50000;
                 Integer i = 0;
                 while ((line = input.readLine()) != null && i < MAX) {
                    previewHtml += line + "\n";
                    i+=line.length();
                 }
                 previewHtml += "</textarea>";
                 input.close();
             }
    

        }
        

         out.print(previewHtml);
    }
%>
