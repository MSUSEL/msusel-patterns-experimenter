<?xml version="1.0" encoding="iso-8859-2" ?>
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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:ganttproject="http://ganttproject.sf.net/"
                version="1.0">

<xsl:output method="html" indent="yes" encoding="UTF-8"/>

<xsl:include href="gantt-utils.xsl"/>

<xsl:template match="ganttproject:resources">
<table width="600" border="0">
 <tr>
  <td bgcolor="#dddddd"><h5><xsl:value-of select="@name"/></h5></td>
  <td bgcolor="#dddddd"><h5><xsl:value-of select="@role"/></h5></td>
  <td bgcolor="#dddddd"><h5><xsl:value-of select="@mail"/></h5></td>
  <td bgcolor="#dddddd"><h5><xsl:value-of select="@phone"/></h5></td>
 </tr>
  <xsl:for-each select="ganttproject:resource">
	<tr>
		<td valign="top"><b><xsl:value-of select="name"/></b></td>
		<td valign="top"><xsl:value-of select="role"/></td>
		<td valign="top"><a><xsl:attribute name="href">mailto:<xsl:value-of select="mail"/></xsl:attribute><xsl:value-of select="mail"/></a></td>
		<td valign="top"><xsl:value-of select="phone"/></td>
	</tr>
  </xsl:for-each>
</table>

<br/><br/>
<img>
	<xsl:attribute name="src"><xsl:value-of select="/ganttproject/resources/chart/@path"/></xsl:attribute>
</img>
<br/>

</xsl:template>

</xsl:stylesheet>

