<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xq="http://ci.uchicago.edu/sidgrid/sidgrid-mobyle" 
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 exclude-result-prefixes="xq"
>
 
  <xsl:output method="html" indent="yes" />
  
  <xsl:template match="//xq:program">
    
    <html><title><xsl:value-of select="//xq:name/text()" /></title></html>    
  </xsl:template>
  
</xsl:stylesheet>