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
<xsl:stylesheet version="1.1"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:param name="currentVersion"/>
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="classpath">
    <xsl:copy>
      <classpathentry excluding="**/CVS/" kind="src" path="src/test"/>
      <xsl:apply-templates select="*"/>
      <classpathentry kind="var" path="MAVEN_REPO/junit/jars/junit-3.8.1.jar"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="classpathentry[@kind='src' and (@path='src/java' or @path='src\java')]">
    <classpathentry excluding="**/CVS/" kind="src" path="src/java"/>
  </xsl:template>
  <xsl:variable name="ivatagroupware">MAVEN_REPO/ivatagroupware/jars/ivatagroupware-</xsl:variable>
  <xsl:variable name="ivatagroupwareDOS">MAVEN_REPO\ivatagroupware\jars\ivatagroupware-</xsl:variable>
  <xsl:template match="classpathentry[@kind='var' and starts-with(@path, $ivatagroupware)]">
    <xsl:variable name="afterOp"><xsl:value-of select="substring-after(@path, $ivatagroupware)"/></xsl:variable>
    <xsl:variable name="beforeDash"><xsl:value-of select="substring-before($afterOp, '-')"/></xsl:variable>
    <classpathentry kind="src" path="/{$beforeDash}"/>
  </xsl:template>
  <xsl:template match="classpathentry[@kind='var' and starts-with(@path, $ivatagroupwareDOS)]">
    <xsl:variable name="afterOpDOS"><xsl:value-of select="substring-after(@path, $ivatagroupwareDOS)"/></xsl:variable>
    <xsl:variable name="beforeDashDOS"><xsl:value-of select="substring-before($afterOpDOS, '-')"/></xsl:variable>
    <classpathentry kind="src" path="/{$beforeDashDOS}"/>
  </xsl:template>
  <xsl:template match="*|@*|comment()|text()">
    <xsl:copy>
      <xsl:apply-templates select="*|@*|comment()|text()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>

