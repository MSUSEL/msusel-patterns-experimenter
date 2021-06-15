<?xml version="1.0"?>
<!--

    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!--
 This stylesheet was taken from the XSLT FAQ http://www.dpawson.co.uk/xsl/
 
 Comments and adaption to be used without normalize-space()
  by forrest-dev@xml.apache.org
-->
<!--
  Input:

<doc>

<para>
 123456 2345 343434 545454 43434 343 
 12345 343434 545454 43434 343 
 32345645 343434 545454 43434 343 
 3422222225 343434 545454 43434 343 
 llllllllllllllllllllllooooooooooooooonnnnnnnnnnnggggggggg
 345 343434 545454 43434 343 
</para>

</doc>

Output:

<HTML>
<BODY>
<PRE>123456 2345 343434 545454 
43434 343 12345 343434 545454 
43434 343 32345645 343434 
545454 43434 343 3422222225 
343434 545454 43434 343 
lllllllllllllllllllllloooooooo
ooooooonnnnnnnnnnnggggggggg 
345 343434 545454 43434 
343
</PRE>
</BODY>
</HTML>

Fragment ised: 

 <xsl:template match="/doc">
 <HTML><BODY><PRE>
    <xsl:call-template name="format">
    <xsl:with-param select="normalize-space(para)" name="txt" /> 
     <xsl:with-param name="width">30</xsl:with-param> 
     </xsl:call-template>
  </PRE></BODY></HTML>
  </xsl:template>


-->
  <xsl:template match="/body">
    <body>
      <xsl:call-template name="format">
        <xsl:with-param select="source" name="txt" />
        <xsl:with-param name="width">40</xsl:with-param>
      </xsl:call-template>
    </body>
  </xsl:template>
  <xsl:template name="format">
    <xsl:param name="txt" />
    <xsl:param name="width" />
<!-- if there is still text left -->
    <xsl:if test="$txt">
      <xsl:variable name = "pretxt" select = "substring($txt,0, $width)" />
      <xsl:choose>
        <xsl:when test="contains($pretxt, '&#xA;')">
          <xsl:value-of select="substring-before($pretxt, '&#xA;')"/>
<xsl:text>&#xA;</xsl:text>
          <xsl:call-template name="format">
            <xsl:with-param name="txt" select="substring-after($txt,'&#xA;')"/>
            <xsl:with-param select="$width" name="width" />
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
<!-- get the width at which to break-->
          <xsl:variable name="real-width">
            <xsl:call-template name="tune-width">
              <xsl:with-param select="$txt" name="txt" />
              <xsl:with-param select="$width" name="width" />
              <xsl:with-param select="$width" name="def" />
            </xsl:call-template>
          </xsl:variable>
<!-- output the first part of the broken string -->
          <xsl:value-of select="substring($txt, 1, $real-width)" />
<!-- output a newline -->
<xsl:text>&#xA;</xsl:text>
<!-- call itself with the remaining part of the text -->
          <xsl:call-template name="format">
            <xsl:with-param select="substring($txt,$real-width + 1)" name="txt" />
            <xsl:with-param select="$width" name="width" />
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>
<!-- used by template "format", it calculates the width at the given def 
       
       It starts at def length and comes back till it finds a space -->
  <xsl:template name="tune-width">
    <xsl:param name="txt" />
    <xsl:param name="width" />
    <xsl:param name="def" />
    <xsl:choose>
      <xsl:when test="$width = 0">
        <xsl:value-of select="$def" />
      </xsl:when>
      <xsl:when test="substring($txt, $width, 1 ) = ' '">
        <xsl:value-of select="$width" />
      </xsl:when>
      <xsl:otherwise>
<!-- otherwise need to tune again, trying with $width - 1 -->
        <xsl:call-template name="tune-width">
          <xsl:with-param select="$width - 1" name="width" />
          <xsl:with-param select="$txt" name="txt" />
          <xsl:with-param select="$def" name="def" />
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>
