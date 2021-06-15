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
                xmlns:ganttproject="http://ganttproject.sf.net/" version="1.0">

<xsl:output method="html" indent="yes" encoding="UTF-8"/>

<xsl:include href="gantt-utils.xsl"/>

<xsl:template match="ganttproject:tasks">

<xsl:variable name="odd">#dbfff8</xsl:variable>
<xsl:variable name="even">#9bc9ef</xsl:variable>

<table width="802" border="0" bgcolor="#002000" cellspacing="0" cellpadding="0">
<tr><td colspan="3" height="1"/></tr>
<tr><td width="1"/><td>
<table width="800" border="0" bgcolor="#e3f49c" cellspacing="0" cellpadding="0">
 <tr>
  <th><xsl:value-of select="@name"/></th><td width="1" bgcolor="#002000"/>
  <th><xsl:value-of select="@begin"/></th><td width="1" bgcolor="#002000"/>
  <th><xsl:value-of select="@end"/></th><td width="1" bgcolor="#002000"/>
  <th><xsl:value-of select="@milestone"/></th><td width="1" bgcolor="#002000"/>
  <th><xsl:value-of select="@progress"/></th><td width="1" bgcolor="#002000"/>
  <th><xsl:value-of select="@assigned-to"/></th><td width="1" bgcolor="#002000"/>
  <th><xsl:value-of select="@notes"/></th>
 </tr>

 <xsl:for-each select="ganttproject:task">
	<tr><td colspan="13" height="1" bgcolor="#002000"/></tr>
	<tr>
		<xsl:choose>
		    <xsl:when test="position() mod 2 = 0">
			<xsl:attribute name="bgcolor"><xsl:value-of select="$even"/></xsl:attribute>
		    </xsl:when>
		    <xsl:when test="position() mod 2 = 1">
			<xsl:attribute name="bgcolor"><xsl:value-of select="$odd"/></xsl:attribute>
		    </xsl:when>
		</xsl:choose>
		<td valign="top"><b><xsl:value-of select="name"/></b></td><td width="1" bgcolor="#002000"/>
		<td valign="top"><xsl:value-of select="begin"/></td><td width="1" bgcolor="#002000"/>
		<td valign="top"><xsl:value-of select="end"/></td><td width="1" bgcolor="#002000"/>
		<td valign="top" align="center">
			<xsl:choose>
				<xsl:when test="milestone='true'">*</xsl:when>
				<xsl:otherwise>&#160;</xsl:otherwise>
			</xsl:choose>
		</td><td width="1" bgcolor="#002000"/>
		<td valign="top"><xsl:value-of select="progress"/></td><td width="1" bgcolor="#002000"/>
		<td valign="top"><xsl:value-of select="assigned-to"/></td><td width="1" bgcolor="#002000"/>
		<td valign="top"><div align="left"><pre width="40"><xsl:value-of select="notes"/></pre></div>
            <xsl:if test="attachment/text()">
	          <div class="attachment">
               <a href="{attachment/text()}" class="attachment">
	               <xsl:choose>
	                <xsl:when test="attachment/@display-name">
	                 <xsl:value-of select='attachment/@display-name'/>
	                </xsl:when>
	                <xsl:otherwise>
	                 <xsl:value-of select='attachment/text()'/>
	                </xsl:otherwise>
	               </xsl:choose>
               </a>
             </div>
            </xsl:if>
		</td>
	</tr>
  </xsl:for-each>
</table>
</td><td width="1"/></tr>
<tr><td colspan="3" height="1"/></tr>
</table>

<br/><br/><br/>
<b><xsl:value-of select="/ganttproject/footer/@version"/></b><br />
<b><a href="http://ganttproject.sourceforge.net">ganttproject.sf.net</a></b><br />
<b><xsl:value-of select="/ganttproject/footer/@date"/></b>
<br/>

</xsl:template>

</xsl:stylesheet>
