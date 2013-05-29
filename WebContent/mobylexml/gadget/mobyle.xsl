<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- 
  mobyle.xsl stylesheet
  Contains shared rules between the different portal xsl stylesheets

-->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <!-- Depending on isInPortal, we either print a whole html document, or just its body -->
    <xsl:choose>
      <xsl:when test="$isInPortal!='true'">
        <html xmlns="http://www.w3.org/1999/xhtml" lang="en"
          xml:lang="en">
          <!-- TODO update all CSS links -->
          <head>
            <style type="text/css" media="screen">
              @import "/MobylePortal/css/mobyle.css"; @import
              "/MobylePortal/css/form.css"; @import
              "/MobylePortal/css/databox.css"; @import
              "/MobylePortal/css/drawer.css"; @import
              "/MobylePortal/css/programs_list.css"; @import
              "/MobylePortal/css/results.css"; @import
              "/MobylePortal/css/tabs.css";
            </style>
            <link href="/MobylePortal/css/form.css" rel="stylesheet"
              type="text/css" media="screen" />
            <link href="/MobylePortal/css/mobyle.css" rel="stylesheet"
              type="text/css" media="screen" />
            <xsl:apply-templates select="/" mode="head" />
          </head>
          <body>
            <xsl:apply-templates />
          </body>
        </html>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="popups">
    <!-- name: either a program name or a job id -->
    <xsl:param name="popupName"/>  
    <!-- popups overlay -->
    <div style="display:none" class="popup_overlay">
      <xsl:attribute name="id">
        <xsl:value-of select="concat($popupName,'PopupOverlay')" />        
      </xsl:attribute>
      <span class="popup_background"></span>
      <!-- captcha popup -->
      <span style="display:none" class="popup">
        <xsl:attribute name="id">
          <xsl:value-of select="concat($popupName,'CaptchaPopup')" />        
        </xsl:attribute>
        <form>
          <xsl:attribute name="id">
            <xsl:value-of select="concat($popupName,'CaptchaPopupForm')" />        
          </xsl:attribute>
          <div>
            <img>
              <xsl:attribute name="id">
                <xsl:value-of select="concat($popupName,'CaptchaImage')" />        
              </xsl:attribute>
            </img>
          </div>
          <div>
            To validate your submission, please type the
            <br />
            above text in the field below.
          </div>
          <div>
            <input type="text">
              <xsl:attribute name="id">
                <xsl:value-of select="concat($popupName,'CaptchaSolution')" />        
              </xsl:attribute>
              <xsl:attribute name="name">
                <xsl:value-of select="concat($popupName,'CaptchaSolution')" />        
              </xsl:attribute>
            </input>
          </div>
          <div>
            <input type="submit" value="ok">
              <xsl:attribute name="id">
                <xsl:value-of select="concat($popupName,'CaptchaSubmit')" />        
              </xsl:attribute>
              <xsl:attribute name="name">
                <xsl:value-of select="concat($popupName,'CaptchaSubmit')" />        
              </xsl:attribute>
            </input>
          </div>
        </form>
      </span>
      <!-- job submission popup -->
      <span style="display:none" class="popup">
        <xsl:attribute name="id">
          <xsl:value-of select="concat($popupName,'JobPopup')" />        
        </xsl:attribute>
        <div>
          <xsl:value-of select="concat('Your ', $popupName, ' is being submitted.')" />        
        </div>
        <div>
          <img src="../../MobylePortal/images/loading.gif"
            alt="Loading" />
        </div>
      </span>
      <!-- reset popup -->
      <span style="display:none" class="popup">
        <xsl:attribute name="id">
          <xsl:value-of select="concat($popupName,'ResetPopup')" />        
        </xsl:attribute>
        <div>Resetting, please wait.</div>
        <div>
          <img src="../../MobylePortal/images/loading.gif"
            alt="Loading" />
        </div>
      </span>
      <!-- help mail popup -->
      <span style="display:none" class="popup">
        <xsl:attribute name="id">
          <xsl:value-of select="concat($popupName,'HelpPopup')" />        
        </xsl:attribute>
        <h4>
          Help on your job
        </h4>
        <h6>(<xsl:value-of select="$popupName" />)</h6>
        <div>Enter your message here:</div>
        <textarea cols="70" rows="15">
          <xsl:attribute name="id">
            <xsl:value-of select="concat($popupName,'HelpMessage')" />        
          </xsl:attribute>
        </textarea>
        <div>
            <input type="button" value="Cancel">
              <xsl:attribute name="id">
                <xsl:value-of select="concat($popupName,'HelpCancel')" />        
              </xsl:attribute>
            </input>
            <input type="button" value="Send">
              <xsl:attribute name="id">
                <xsl:value-of select="concat($popupName,'HelpSubmit')" />        
              </xsl:attribute>
            </input>
        </div>
      </span>
      <!-- help message confirmation -->
      <span style="display:none" class="popup">
        <xsl:attribute name="id">
          <xsl:value-of select="concat($popupName,'HelpConfirmation')" />        
        </xsl:attribute>
        <h4>Your message has been sent.</h4>
        <div>Message text:</div>
        <textarea cols="70" rows="15">
          <xsl:attribute name="id">
            <xsl:value-of select="concat($popupName,'HelpConfirmationMessage')" />        
          </xsl:attribute>
        </textarea>
        <input type="button" value="Ok">
          <xsl:attribute name="id">
            <xsl:value-of select="concat($popupName,'HelpConfirmationOk')" />        
          </xsl:attribute>
        </input>
      </span>
      <!-- job removal popup -->
      <span style="display:none" class="popup">
        <xsl:attribute name="id">
          <xsl:value-of select="concat($popupName,'JobRemovalConfirmPopup')" />        
        </xsl:attribute>
        <h4>
          Job removal.
        </h4>
        <div>This will remove this job from your session. Continue?</div>
        <div>
            <input type="button" value="Cancel">
              <xsl:attribute name="id">
                <xsl:value-of select="concat($popupName,'JobRemovalConfirmCancel')" />        
              </xsl:attribute>
            </input>
            <input type="button" value="Remove">
              <xsl:attribute name="id">
                <xsl:value-of select="concat($popupName,'JobRemovalConfirmSubmit')" />        
              </xsl:attribute>
            </input>
        </div>
      </span>
      <!-- job removal waiting screen -->
      <span style="display:none" class="popup">
        <xsl:attribute name="id">
          <xsl:value-of select="concat($popupName,'JobRemovalWaitPopup')" />        
        </xsl:attribute>
        <div>Removing your job, please wait.</div>
        <div>
          <img src="../../MobylePortal/images/loading.gif"
            alt="Loading" />
        </div>
      </span>
    </div>
  </xsl:template>

  <xsl:template name="askHelpPopUpButton">
    <!-- name: either a program name or a job id -->
    <xsl:param name="popupName"/>  
    <input type="button" style="display:none" value="ask for help">
      <xsl:attribute name="id">helpdisplay_<xsl:value-of select="$popupName" /></xsl:attribute>
    </input>
  </xsl:template>

  <xsl:template name="removeJobButton">
    <!-- name: either a program name or a job id -->
    <xsl:param name="popupName"/>  
    <input type="button" style="display:none" value="remove this job">
      <xsl:attribute name="id">jobremovaldisplay_<xsl:value-of select="$popupName" /></xsl:attribute>
    </input>
  </xsl:template>
  
  <xsl:template name="commentText">
    <xsl:param name="commentNode"/>
    <xsl:param name="exampleNode"/>
    <xsl:if test="($commentNode/text) or $exampleNode">
      <ul class="commentText" style="display: none">
        <xsl:attribute name="id">
          <xsl:if test="$commentNode and not($exampleNode)">
	        <xsl:value-of select="generate-id($commentNode)" />
		  </xsl:if>
          <xsl:if test="$exampleNode">		  
	        <xsl:value-of select="generate-id($exampleNode)" />
		  </xsl:if>
        </xsl:attribute>        
        <xsl:for-each select="$commentNode/text">
          <li><xsl:value-of select="text()"/></li>
        </xsl:for-each>
        <xsl:if test="string-length($exampleNode)>0">
          <div class="example">Example data:
	        <pre><xsl:value-of select="$exampleNode"/></pre>
		  </div>
        </xsl:if>
      </ul>
    </xsl:if>
  </xsl:template>  

  <xsl:template name="commentToggle">
    <xsl:param name="commentNode"/>  
    <xsl:param name="exampleNode"/>
    <xsl:if test="($commentNode/text) or $exampleNode">
      <label class="commentToggle">
        <xsl:attribute name="for">
          <xsl:if test="$commentNode and not($exampleNode)">
	        <xsl:value-of select="generate-id($commentNode)" />
		  </xsl:if>
          <xsl:if test="$exampleNode">		  
	        <xsl:value-of select="generate-id($exampleNode)" />
		  </xsl:if>
        </xsl:attribute>        
        <xsl:attribute name="title">Click here to get help on this parameter</xsl:attribute>
        <xsl:value-of select="'?'"/>
      </label>
    </xsl:if>            
  </xsl:template>
  
    <xsl:template match="layout">
    <xsl:choose>
      <xsl:when test="name(..)='hbox'">
          <td>
            <table>
                <xsl:apply-templates select="*" />                    
            </table>
          </td>        
      </xsl:when>
      <xsl:when test="name(..)='vbox'">
        <tr>
          <td>
            <table>
                <xsl:apply-templates select="*" />                    
            </table>
          </td>        
        </tr>
      </xsl:when>
      <xsl:otherwise>
        <table>
            <xsl:apply-templates select="*" />                    
        </table>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="hbox">
    <tr>
      <xsl:apply-templates select="*" />                    
    </tr>
  </xsl:template>

  <xsl:template match="vbox">
    <xsl:apply-templates select="*" />                    
  </xsl:template>


  <xsl:template match="box">
    <xsl:choose>
      <xsl:when test="name(..)='hbox'">
          <td>
            <xsl:apply-templates select="//parameter[name/text()=current()/text()]" />                    
          </td>        
      </xsl:when>
      <xsl:when test="name(..)='vbox'">
        <tr>
          <td>
            <xsl:apply-templates select="//parameter[name/text()=current()/text()]" />                    
          </td>        
        </tr>
      </xsl:when>      
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>
