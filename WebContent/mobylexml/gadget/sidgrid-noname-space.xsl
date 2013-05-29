<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- 
form.xsl stylesheet
Transforms a Mobyle xml program definition into an HTML form.
This form is either:
- a standalone AJAX-less form that can be called from outside the Mobyle Portal or
- a form (i.e., not a whole html document) that is loaded in the Mobyle Portal
Authors: Hervé Ménager, Bertrand Néron
 -->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xq="http://ci.uchicago.edu/sidgrid/sidgrid-mobyle">
  
  <xsl:output method="html" indent="yes" />
  <!-- parameter, if set to 'true', means that we should not generate a complete form, but only one to fill the program channel in the Portal -->
  <xsl:param name="isInPortal">false</xsl:param>
  <!-- parameter, if set to 'true', means that we should generate an e-mail input -->
  <xsl:param name="optEmail" />
  <!-- parameter, that specifies that an error prevents this form from being displayed and an error message should be displayed -->
  <xsl:param name="errorMsg" />
  <xsl:include href="resources/sidgrid-mobyle.xsl" />
  
  <!-- program title in html title -->
  <xsl:template match="//xq:program" mode="head">
    <title><xsl:value-of select="name/text()" /></title>    
  </xsl:template>

  <!-- program form body -->
  <xsl:template match="//xq:program">
    <!-- Printing the html body -->    
    <xsl:choose>
      <xsl:when test="$isInPortal='true'">
        <div class="formContent">
          <xsl:attribute name="id"><xsl:value-of select="head/name" />Form</xsl:attribute>
          <!-- popups overlay -->
          <xsl:call-template name="popups">
            <xsl:with-param name="popupName" select="head/name"/>
          </xsl:call-template>
          <!-- form contents -->
          <xsl:apply-templates select="head" />
          </div>
      </xsl:when>
      <xsl:otherwise>
          <xsl:apply-templates select="head" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
    
  <xsl:template match="xq:head">
    <!-- Printing the html body -->    
    <!-- Title -->
    <div class="formHead">
      <span class="formTitle">
        <h1>
        	<xsl:value-of select="doc/title" />
		</h1>
        <!-- Description -->
        <h2>
        	<xsl:value-of select="doc/description/text" />
			<xsl:call-template name="commentToggle">
	          <xsl:with-param name="commentNode" select="doc/comment"/>
	      	</xsl:call-template>
		</h2>
      </span>
      <span class="formCtrl">
        <!-- form controls -->
        <xsl:choose>
          <xsl:when test="$isInPortal!='true'">
            <label >
              <xsl:attribute name="for">form_email_value<xsl:value-of select="name" /></xsl:attribute>        
              E-mail:
            </label>
            <input name="email" type="text">
              <xsl:attribute name="id">form_email_value<xsl:value-of select="name" /></xsl:attribute>
              <xsl:if test="$optEmail!='True'">
                <xsl:attribute name="class">mandatory</xsl:attribute>                
              </xsl:if>
            </input>
          </xsl:when>
          <xsl:otherwise>
            <input name="email_mandatory" type="hidden">
              <xsl:attribute name="id">email_mandatory_<xsl:value-of select="name" /></xsl:attribute>
              <xsl:attribute name="value"><xsl:value-of select="$optEmail!='True'" /></xsl:attribute>
            </input>
          </xsl:otherwise>
        </xsl:choose>
        <!--  Reset button -->
        <input type="button" value="Reset">
          <xsl:attribute name="id">reset_<xsl:value-of select="name" /></xsl:attribute>      
        </input>
        <!--  Run button -->
        <input type="submit" value="Run">
          <xsl:attribute name="id">submit_<xsl:value-of select="name" /></xsl:attribute>
        </input>
      </span>
      <span class="formCtrl">
        <!-- Help pages links scroll button -->
        <xsl:if test="doc/doclink">
          <input type="button" value="Help Pages">
            <xsl:attribute name="id">help_pages_btn_<xsl:value-of select="name" /></xsl:attribute>
          </input>      
        </xsl:if>
        <!-- help popup display button -->
        <xsl:call-template name="askHelpPopUpButton">
          <xsl:with-param name="popupName" select="name"/>
        </xsl:call-template>
        <!-- program name, sent to jobcgi -->
        <input type="hidden" name="programName">
            <xsl:attribute name="value">
              <xsl:value-of select="name" />
            </xsl:attribute>
        </input>
      </span>
    </div>
    <xsl:call-template name="commentText">
      <xsl:with-param name="commentNode" select="doc/comment"/>
    </xsl:call-template>
    <!-- program parameters -->
    <div class="parameters">
      <xsl:apply-templates select="/program/parameters"/>
    </div>
    <!-- Program information: references, authors, help links -->
    <xsl:if test="(doc/reference) or (doc/authors) or (doc/doclink)">
      <fieldset class="program_information">
        <!-- References -->
        <xsl:for-each select="doc/reference">
          <div class="reference"><xsl:value-of select="." /></div>
        </xsl:for-each>    
        <!-- Authors -->
        <div class="authors"><xsl:value-of select="doc/authors" /></div>
        <!-- Help link -->
        <xsl:if test="doc/doclink">
          <div>Program help pages:
          <ul>
            <xsl:attribute name="id">help_pages_list_<xsl:value-of select="name" /></xsl:attribute>
            <xsl:for-each select="doc/doclink">
              <li>
                <a target="_blank">
                  <xsl:attribute name="href">
                    <xsl:value-of select="." />
                  </xsl:attribute>
                  <xsl:value-of select="." />
                </a>
              </li>
            </xsl:for-each>
          </ul>
          </div>
        </xsl:if>
      </fieldset>
    </xsl:if>
  </xsl:template>

  <!-- template for each parameter node -->
  <xsl:template match="parameter">
    <xsl:choose>
      <xsl:when test="boolean(@isout)" />
      <xsl:when test="boolean(@isstdout)" />
      <xsl:when test="boolean(@ishidden)" />
      <xsl:otherwise>
        <div class="parameter">
          <xsl:call-template name="param-type" />
        </div>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="paragraph">
    <xsl:if test=".//parameter[not(@isout) and not(@isstdout) and not(@ishidden)]">
      <fieldset class="fieldset">
        <legend>
          <xsl:value-of select="prompt" />
          <xsl:call-template name="commentToggle">
            <xsl:with-param name="commentNode" select="comment"/>
          </xsl:call-template>
        </legend>
        <xsl:call-template name="commentText">
          <xsl:with-param name="commentNode" select="comment"/>
        </xsl:call-template>
        <xsl:choose>
          <xsl:when test="not(layout)">
            <xsl:apply-templates select="parameters" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates select="layout" />            
          </xsl:otherwise>
        </xsl:choose>
      </fieldset>
    </xsl:if>
  </xsl:template>

  <!-- Templates for various parameter types -->
  <xsl:template name="param-type">
    <xsl:variable name ="datatype" select="type/datatype/class"/>
    <xsl:choose>
      <xsl:when test="($datatype = 'Choice') or ($datatype = 'MultipleChoice') or ($datatype = 'Boolean') or ($datatype = 'Integer') or ($datatype = 'Float') or ($datatype = 'String') or ($datatype = 'Filename')">
        <xsl:call-template name="field">
          <xsl:with-param name="paramNode" select="."/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
          <xsl:call-template name="Databox" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="Databox">
    <xsl:variable name ="databoxId" select="concat(/program/head/name, '_', name)"/>
    <xsl:variable name ="datatype" select="type/datatype/class"/>
    <xsl:variable name ="datatype_sc" select="type/datatype/superclass"/>
    <xsl:variable name="isText" select="(type/datatype/class/text()!='Binary') and (not(type/datatype/superclass) or (type/datatype/superclass/text()!='Binary'))" />
    <fieldset class="fieldset">
    <legend>
      <label>
        <xsl:call-template name="labelAttrs">
          <xsl:with-param name="paramNode" select="."/>
        </xsl:call-template>
        <xsl:value-of select="prompt" />
      </label>
      (<xsl:value-of select="normalize-space(concat(type/biotype, ' ', type/datatype/class))" />)
      <xsl:call-template name="commentToggle">
        <xsl:with-param name="commentNode" select="comment"/>
        <xsl:with-param name="exampleNode" select="example"/>
      </xsl:call-template>        
    </legend>
    <xsl:call-template name="commentText">
      <xsl:with-param name="commentNode" select="comment"/>
      <xsl:with-param name="exampleNode" select="example"/>
    </xsl:call-template>
    <xsl:choose>
      <xsl:when test="$isInPortal='true'">
        <div class="databox_form">
          <xsl:attribute name="id"><xsl:value-of select="concat($databoxId, '_Databox')" /></xsl:attribute>
            <div class="databox">
            <input type="hidden" class="parameterName">
              <xsl:attribute name="name"><xsl:value-of select="concat(name, '_parameterName')" /></xsl:attribute>
              <xsl:attribute name="value"><xsl:value-of select="name" /></xsl:attribute>
            </input>
            <input type="hidden" class="parameterDataType">
              <xsl:attribute name="name"><xsl:value-of select="concat(name, '_parameterDataType')" /></xsl:attribute>
              <xsl:attribute name="value"><xsl:value-of select="$datatype" /></xsl:attribute>
            </input>
            <input type="hidden" class="parameterBioType">
              <xsl:attribute name="name"><xsl:value-of select="concat(name, '_parameterBioType')" /></xsl:attribute>
              <xsl:attribute name="value"><xsl:value-of select="type/biotype" /></xsl:attribute>
            </input>
            <input type="hidden" class="parameterCard">
              <xsl:attribute name="name"><xsl:value-of select="concat(name, '_parameterCard')" /></xsl:attribute>
              <xsl:attribute name="value"><xsl:value-of select="type/card" /></xsl:attribute>
            </input>
            <div class="data_input_choices">
              <table><tbody><tr>
              <td>
              <ul>
                <xsl:if test="$isText">
                  <li title="Paste (or directly type) your data here">Paste 
                    <input type="radio" value="paste" checked="checked">
                      <xsl:attribute name="name"><xsl:value-of select="concat('dataInputType_',$databoxId)" /></xsl:attribute>
                      <xsl:attribute name="id"><xsl:value-of select="concat('dataInputType_',$databoxId,'_paste')" /></xsl:attribute>
                    </input>
                  </li>
                </xsl:if>
                <li title="Load data from a bank">DB
                  <input type="radio" value="db">
                    <xsl:attribute name="name"><xsl:value-of select="concat('dataInputType_',$databoxId)" /></xsl:attribute>
                    <xsl:attribute name="id"><xsl:value-of select="concat('dataInputType_',$databoxId,'_db')" /></xsl:attribute>
                  </input>
                </li>
                <li title="Upload a file">File
                  <input type="radio" value="upload">                
                    <xsl:attribute name="name"><xsl:value-of select="concat('dataInputType_',$databoxId)" /></xsl:attribute>
                    <xsl:attribute name="id"><xsl:value-of select="concat('dataInputType_',$databoxId,'_upload')" /></xsl:attribute>
                  </input>
                </li>
                <li title="Use results bookmarks">Result 
                  <input type="radio" value="result">
                    <xsl:attribute name="name"><xsl:value-of select="concat('dataInputType_',$databoxId)" /></xsl:attribute>
                    <xsl:attribute name="id"><xsl:value-of select="concat('dataInputType_',$databoxId,'_result')" /></xsl:attribute>
                  </input>
                </li>
              </ul>
              </td>
	      <xsl:if test="$isText">
                <td class="editControls">
                  <input type="button" value=" edit data ">
                    <xsl:attribute name="id"><xsl:value-of select="concat('data_input_edit_',$databoxId)" /></xsl:attribute>
                  </input>
                  <input type="button" value=" clear data ">
                    <xsl:attribute name="id"><xsl:value-of select="concat('data_input_reset_',$databoxId)" /></xsl:attribute>
                  </input>
                </td>
              </xsl:if>
	    </tr></tbody></table>
            </div>
            <div id="data_input_fields" class="data_input_fields">
              <!-- paste data input -->
              <xsl:if test="$isText">
                <div class="data_input">
                  <xsl:attribute name="id"><xsl:value-of select="concat('data_input_paste_',$databoxId)" /></xsl:attribute>
                  <table><tr><td class="history_data">
                  <label>
                    <xsl:attribute name="for"><xsl:value-of select="concat('history_paste_',$databoxId)" /></xsl:attribute>              
                    Paste Bookmarks:
                  </label>
                  <select class="history_data">
                    <xsl:attribute name="id"><xsl:value-of select="concat('history_paste_',$databoxId)" /></xsl:attribute>              
                    <xsl:attribute name="name"><xsl:value-of select="concat('history_paste_',$databoxId)" /></xsl:attribute>              
                    <option value="" selected="selected">&lt;&lt; select previous data here &gt;&gt;</option>
                  </select>
                  <input type="button" value=" select ">
                    <xsl:attribute name="id"><xsl:value-of select="concat('history_paste_submit_',$databoxId)" /></xsl:attribute>
                  </input>
                  </td></tr></table>                
                </div>
              </xsl:if>
              <!-- DB data input -->
              <div class="data_input">
                <xsl:attribute name="id"><xsl:value-of select="concat('data_input_db_',$databoxId)" /></xsl:attribute>
                <table><tr><td>
                <select name="data_input_db_db" title="choose a databank">
                  <xsl:attribute name="id"><xsl:value-of select="concat('data_input_db_db_',$databoxId)" /></xsl:attribute>
                </select> 
                <input name="data_input_db_id" type="text" size="25" title="enter an identifier">
                  <xsl:attribute name="id"><xsl:value-of select="concat('data_input_db_id_',$databoxId)" /></xsl:attribute>
                </input>
                <input type="button" value=" add " title="fetch the entry from the databank">
                  <xsl:attribute name="id"><xsl:value-of select="concat('data_input_db_submit_',$databoxId)" /></xsl:attribute>
                </input>
                </td><td class="history_data">
                <label>
                  <xsl:attribute name="for"><xsl:value-of select="concat('history_db_',$databoxId)" /></xsl:attribute>              
                  DB Bookmarks:
                </label>
                <select>
                  <xsl:attribute name="id"><xsl:value-of select="concat('history_db_',$databoxId)" /></xsl:attribute>              
                  <xsl:attribute name="name"><xsl:value-of select="concat('history_db_',$databoxId)" /></xsl:attribute>              
                  <option value="" selected="selected">&lt;&lt; select previous data here &gt;&gt;</option>
                </select>
                <input type="button" value=" select ">
                  <xsl:attribute name="id"><xsl:value-of select="concat('history_db_submit_',$databoxId)" /></xsl:attribute>
                </input>
                </td></tr></table>
              </div>
              <!-- File data input -->
              <div class="data_input">
                <xsl:attribute name="id"><xsl:value-of select="concat('data_input_upload_',$databoxId)" /></xsl:attribute>
                <table><tr><td>
                <form method="post" enctype="multipart/form-data" action="data_upload.py" class="uploadform">
                  <xsl:attribute name="id"><xsl:value-of select="concat('uploadform_',$databoxId)" /></xsl:attribute>
                  <xsl:attribute name="name"><xsl:value-of select="concat('uploadform_',$databoxId)" /></xsl:attribute>              
                  <xsl:attribute name="target"><xsl:value-of select="concat('uploadtarget_',$databoxId)" /></xsl:attribute>    
                  <input name="_charset_" type="hidden" />
                  <input name="data_input_upload" type="file">
                    <xsl:attribute name="id"><xsl:value-of select="concat('data_input_upload_value_',$databoxId)" /></xsl:attribute>                
                  </input>
                  <input name="datatype_class" type="hidden">
                    <xsl:attribute name="value"><xsl:value-of select="$datatype" /></xsl:attribute>
                  </input>
                  <input name="datatype_superclass" type="hidden">
                    <xsl:attribute name="value"><xsl:value-of select="$datatype_sc" /></xsl:attribute>
                  </input>                  
                </form>
                <iframe style="display: none;">
                  <xsl:attribute name="id"><xsl:value-of select="concat('uploadtarget_',$databoxId)" /></xsl:attribute>
                  <xsl:attribute name="name"><xsl:value-of select="concat('uploadtarget_',$databoxId)" /></xsl:attribute>
                </iframe>
                </td><td class="history_data">
                <label>
                  <xsl:attribute name="for"><xsl:value-of select="concat('history_upload_',$databoxId)" /></xsl:attribute>              
                  File Bookmarks:
                </label>
                <select class="history_data">
                  <xsl:attribute name="id"><xsl:value-of select="concat('history_upload_',$databoxId)" /></xsl:attribute>
                  <xsl:attribute name="name"><xsl:value-of select="concat('history_upload_',$databoxId)" /></xsl:attribute>
                  <option value="" selected="selected">&lt;&lt; select previous data here &gt;&gt;</option>
                </select>      
                <input type="button" value=" select ">
                  <xsl:attribute name="id"><xsl:value-of select="concat('history_upload_submit_',$databoxId)" /></xsl:attribute>
                </input>
                </td></tr></table>
              </div>
              <!-- Result data input -->
              <div class="data_input">
                <xsl:attribute name="id"><xsl:value-of select="concat('data_input_result_',$databoxId)" /></xsl:attribute>
                <table><tr><td class="history_data">
                <label>
                  <xsl:attribute name="for"><xsl:value-of select="concat('history_result_',$databoxId)" /></xsl:attribute>              
                  Results bookmarks:
                </label>
                <select>
                  <xsl:attribute name="id"><xsl:value-of select="concat('history_result_',$databoxId)" /></xsl:attribute>
                  <xsl:attribute name="name"><xsl:value-of select="concat('history_result_',$databoxId)" /></xsl:attribute>
                  <option value="" selected="selected">&lt;&lt; select previous data here &gt;&gt;</option>
                </select>
                <input type="button" value=" select ">
                  <xsl:attribute name="id"><xsl:value-of select="concat('history_result_submit_',$databoxId)" /></xsl:attribute>
                </input>
                </td></tr></table>
              </div>
            </div>
            <!-- data area -->
            <xsl:choose>
              <!-- if text parameter: textarea -->
              <xsl:when test="$isText">
                <textarea rows="7" cols="80">
                  <xsl:attribute name="id"><xsl:value-of select="concat($databoxId,'_data')" /></xsl:attribute>
                  <xsl:attribute name="name"><xsl:value-of select="concat($databoxId,'_data')" /></xsl:attribute>              
                </textarea>
                <em class="warningSize">
                  <xsl:attribute name="id"><xsl:value-of select="concat('data_input_editforbid_',$databoxId)" /></xsl:attribute>
                  Your data cannot be fully displayed here since its size is too important
                </em>
              </xsl:when>
              <!-- otherwise (binary): file name -->
              <xsl:otherwise>
                <label>
                  <xsl:attribute name="for"><xsl:value-of select="concat($databoxId,'_uname')" /></xsl:attribute>
                  File name
                </label>
                <input type="text" disabled="disabled">
                  <xsl:attribute name="id"><xsl:value-of select="concat($databoxId,'_uname')" /></xsl:attribute>
                  <xsl:attribute name="name"><xsl:value-of select="concat($databoxId,'_uname')" /></xsl:attribute>              
                </input>
              </xsl:otherwise>
            </xsl:choose>
            <input type="hidden" disabled="disabled">
              <xsl:attribute name="id"><xsl:value-of select="concat($databoxId,'_sname')" /></xsl:attribute>
              <xsl:attribute name="name"><xsl:value-of select="concat($databoxId,'_sname')" /></xsl:attribute>              
            </input>
            <!-- format detection area -->
            <div class="fmt_detect">
            <span><xsl:attribute name="id"><xsl:value-of select="concat($databoxId,'_fmt_count')" /></xsl:attribute></span>
            <span><xsl:attribute name="id"><xsl:value-of select="concat($databoxId,'_fmt_name')" /></xsl:attribute></span>
            <span><xsl:attribute name="id"><xsl:value-of select="concat($databoxId,'_fmt_errormsg')" /></xsl:attribute></span>
            </div>
          </div>
        </div>       
      </xsl:when>
      <xsl:otherwise>
        <span class="field_input">
          <div>Please enter either :</div>
          <ol>
    	<li>
    	  the name of a <strong>file</strong>:
    	  <input type="file">
            <xsl:call-template name="fieldAttrs">
               <xsl:with-param name="paramNode" select="."/>
            </xsl:call-template>
    	  </input>
          <xsl:call-template name="commentToggle">
            <xsl:with-param name="commentNode" select="comment"/>
            <xsl:with-param name="exampleNode" select="example"/>
          </xsl:call-template>        
          <xsl:call-template name="commentText">
            <xsl:with-param name="commentNode" select="comment"/>
            <xsl:with-param name="exampleNode" select="example"/>
          </xsl:call-template>
    	</li>
    	<li>
    	  or the actual data here:
    	  <textarea rows="7" cols="60">
            <xsl:call-template name="fieldAttrs">
               <xsl:with-param name="paramNode" select="."/>
               <xsl:with-param name="nameSuff" select="'data'"/>
            </xsl:call-template>
    	  </textarea>
          <xsl:call-template name="commentToggle">
            <xsl:with-param name="commentNode" select="comment"/>
   	        <xsl:with-param name="exampleNode" select="example"/>
          </xsl:call-template>        
          <xsl:call-template name="commentText">
            <xsl:with-param name="commentNode" select="comment"/>
            <xsl:with-param name="exampleNode" select="example"/>
          </xsl:call-template>
    	</li>
          </ol>
        </span>
      </xsl:otherwise>
    </xsl:choose>
    </fieldset>
  </xsl:template>

  <xsl:template name="field">
    <xsl:param name="paramNode"/>
    <div>
      <label>
        <xsl:call-template name="labelAttrs">
          <xsl:with-param name="paramNode" select="."/>
        </xsl:call-template>
        <xsl:value-of select="$paramNode/prompt" />
      </label>
      <xsl:call-template name="commentToggle">
        <xsl:with-param name="commentNode" select="comment"/>
        <xsl:with-param name="exampleNode" select="example"/>
      </xsl:call-template>        
      <span class="field_input">
        <xsl:choose>
          <!-- Textarea pour type Text -->
          <xsl:when test="$paramNode/type/datatype/class = 'Text'">
            <textarea>
              <xsl:call-template name="fieldAttrs">
                <xsl:with-param name="paramNode" select="."/>
              </xsl:call-template>
              <xsl:if test="$paramNode/vdef/value/text()">
                <xsl:value-of select="$paramNode/vdef/value/text()" />
              </xsl:if>
            </textarea>
          </xsl:when>
          <!-- Select pour Choice avec bcp de valeurs et MultipleChoice ou valeur undefined-->
          <xsl:when test="($paramNode/type/datatype/class='MultipleChoice') or (($paramNode/type/datatype/class='Choice') and (count($paramNode/*[substring(local-name(.),2,4)='list']/*) &gt; 4)) or $paramNode//@undef">
            <select>
              <xsl:if test="$paramNode/type/datatype/class='MultipleChoice'"><xsl:attribute name="multiple">multiple</xsl:attribute></xsl:if>            
              <xsl:call-template name="fieldAttrs">
                <xsl:with-param name="paramNode" select="."/>
              </xsl:call-template>
              <xsl:for-each select="$paramNode/*[substring(local-name(.),2,4)='list']/*">
                <xsl:if test="not(string(.)='default')">
                  <option>
                    <xsl:attribute name="value">
                      <xsl:value-of select="value" /><xsl:value-of select="$paramNode/separator/text()" />
                    </xsl:attribute>
                    <xsl:if test="../../vdef/value[text()=current()/value/text()] ">
                      <xsl:attribute name="selected" />
                    </xsl:if>
                    <xsl:value-of select="label" />
                  </option>
                </xsl:if>
              </xsl:for-each>
            </select>
          </xsl:when>
          <!-- Select pour Choice avec peu de valeurs -->
          <xsl:when test="($paramNode/type/datatype/class='Choice') and (count($paramNode/*[substring(local-name(.),2,4)='list']/*) &lt; 5) and not($paramNode//@undef)">
            <ul>
            <xsl:for-each select="$paramNode/*[substring(local-name(.),2,4)='list']/*">
              <li>
                <input type="radio">
                  <xsl:call-template name="fieldAttrs">
                    <xsl:with-param name="paramNode" select="$paramNode"/>
                    <xsl:with-param name="idSuff" select="current()/value/text()"/>
                  </xsl:call-template>
                  <xsl:attribute name="value"><xsl:value-of select="current()/value/text()" /></xsl:attribute>
                  <xsl:if test="$paramNode/vdef/value[text()=current()/value/text()]  ">
                    <xsl:attribute name="checked">checked</xsl:attribute>
                  </xsl:if>
                </input>
                <label>
                  <xsl:call-template name="labelAttrs">
                    <xsl:with-param name="paramNode" select="$paramNode"/>
                    <xsl:with-param name="idSuff" select="current()/value/text()"/>
                  </xsl:call-template>
                  <xsl:value-of select="label" />           
                </label>
              </li>
            </xsl:for-each>
            </ul>
          </xsl:when>
          <!-- Input par défaut -->
          <xsl:otherwise>
            <input>
              <xsl:choose>
                <xsl:when test="$paramNode/type/datatype/class = 'Boolean'"><xsl:attribute name="type">checkbox</xsl:attribute></xsl:when>
                <xsl:when test="$paramNode/type/datatype/class = 'File'"><xsl:attribute name="type">file</xsl:attribute></xsl:when>
                <xsl:otherwise><xsl:attribute name="type">text</xsl:attribute></xsl:otherwise>
              </xsl:choose>
              <xsl:call-template name="fieldAttrs">
                <xsl:with-param name="paramNode" select="."/>
              </xsl:call-template>
              <xsl:choose>
                <xsl:when test="$paramNode/type/datatype/class = 'Boolean'">
              	<xsl:if test="vdef/value/text() and vdef/value/text()='1'">
              	  <xsl:attribute name="checked" />
              	</xsl:if>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:attribute name="value">
                    <xsl:value-of select="vdef/value/text()" />
                  </xsl:attribute>
                </xsl:otherwise>
              </xsl:choose>
            </input>
          </xsl:otherwise>
        </xsl:choose>
      </span>
      <xsl:call-template name="commentText">
        <xsl:with-param name="commentNode" select="$paramNode/comment"/>
        <xsl:with-param name="exampleNode" select="example"/>
      </xsl:call-template>
      </div>
  </xsl:template>

  <xsl:template name="fieldAttrs">
    <xsl:param name="paramNode"/>
    <xsl:param name="idSuff"/>
    <xsl:param name="nameSuff"/>
    <xsl:attribute name="id">
      <xsl:value-of select="concat(/program/head/name,'_',$paramNode/name)" />
      <xsl:if test="$nameSuff"><xsl:value-of select="concat('_',$nameSuff)" /></xsl:if>
      <xsl:if test="$idSuff"><xsl:value-of select="concat('_',$idSuff)" /></xsl:if>
    </xsl:attribute>
    <xsl:attribute name="name">
      <xsl:value-of select="concat(/program/head/name,'_',$paramNode/name)" />
      <xsl:if test="$nameSuff"><xsl:value-of select="concat('_',$nameSuff)" /></xsl:if>
    </xsl:attribute>
    <xsl:attribute name="class">
      <xsl:value-of select="'form_parameter'"/>
      <xsl:if test="boolean($paramNode/@ismandatory) and not($paramNode/ancestor-or-self::*/precond)"> mandatory</xsl:if>
    </xsl:attribute>
  </xsl:template>

  <xsl:template name="labelAttrs">
    <xsl:param name="paramNode"/>
    <xsl:param name="idSuff"/>
    <xsl:param name="nameSuff"/>
    <xsl:attribute name="for">
      <xsl:value-of select="concat(/program/head/name,'_',$paramNode/name)" />
      <xsl:if test="$nameSuff"><xsl:value-of select="concat('_',$nameSuff)" /></xsl:if>
      <xsl:if test="$idSuff"><xsl:value-of select="concat('_',$idSuff)" /></xsl:if>
    </xsl:attribute>
    <xsl:attribute name="id">
      <xsl:value-of select="concat('for_',/program/head/name,'_',$paramNode/name)" />
      <xsl:if test="$nameSuff"><xsl:value-of select="concat('_',$nameSuff)" /></xsl:if>
      <xsl:if test="$idSuff"><xsl:value-of select="concat('_',$idSuff)" /></xsl:if>
    </xsl:attribute>
    <xsl:attribute name="class">
      <xsl:if test="boolean($paramNode/@ismandatory) and not($paramNode/ancestor-or-self::*/precond)"> mandatory</xsl:if>
    </xsl:attribute>
  </xsl:template>

</xsl:stylesheet>
