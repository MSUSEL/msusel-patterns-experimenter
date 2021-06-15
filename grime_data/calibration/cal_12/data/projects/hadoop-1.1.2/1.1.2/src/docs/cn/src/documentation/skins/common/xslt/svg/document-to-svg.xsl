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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
  <xsl:output method="xml" media-type="image/svg" omit-xml-declaration="yes" indent="yes"/>
<!-- the skinconf file -->
  <xsl:param name="config-file"/>
  <xsl:variable name="config" select="document($config-file)/skinconfig"/>
<!-- Get the section depth to use when generating the minitoc (default is 2) -->
  <xsl:variable name="toc-max-depth" select="number($config/toc/@max-depth)"/>
  <xsl:param name="numbersections" select="'true'"/>
<!-- Section depth at which we stop numbering and just indent -->
  <xsl:param name="numbering-max-depth" select="'3'"/>
  <xsl:param name="ctxbasedir" select="."/>
  <xsl:param name="xmlbasedir"/>
  <xsl:template match="/">
    <svg width="1305" height="1468" xmlns="http://www.w3.org/2000/svg">
      <g transform="translate(0 0)">
        <xsl:apply-templates/>
      </g>
    </svg>
  </xsl:template>
  <xsl:template match="document">
    <text x="00px" y="30px" style="font-size:20;">
      <xsl:value-of select="header/title"/>
    </text>
    <text x="0px" y="50px" style="font-size:12;">
      <xsl:apply-templates/>
    </text>
  </xsl:template>
</xsl:stylesheet>
