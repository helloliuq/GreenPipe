<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <xsl:output method="html" indent="yes" />
  
  <xsl:template match="//*[local-name()='program']">
    <title><xsl:value-of select="//*[local-name()='name']/text()" /></title>    
  </xsl:template>
  
</xsl:stylesheet>