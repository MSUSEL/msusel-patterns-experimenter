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

<xsl:template name="header">
    <br /><br />

    <center><table width="100%" border="0"><tr><td bgcolor="#dddddd">
    <font size="+2" color="#0000000"><b><xsl:value-of select="/ganttproject/project/name"/></b></font>
    </td></tr></table></center>

    <center><table width="100%" border="0"><tr>
    <td bgcolor="#6d9ec6"><center>

    <a>
	<xsl:attribute name="href"><xsl:value-of select="/ganttproject/links/@prefix" />.html</xsl:attribute>
	<xsl:value-of select="/ganttproject/links/home" />
    </a>  |
    <a>
	<xsl:attribute name="href"><xsl:value-of select="/ganttproject/links/@prefix" />-chart.html</xsl:attribute>
	<xsl:value-of select="/ganttproject/links/chart" />
    </a> |
    <a>
	<xsl:attribute name="href"><xsl:value-of select="/ganttproject/links/@prefix" />-tasks.html</xsl:attribute>
	<xsl:value-of select="/ganttproject/links/tasks" />
    </a> |
    <a>
	<xsl:attribute name="href"><xsl:value-of select="/ganttproject/links/@prefix" />-resources.html</xsl:attribute>
	<xsl:value-of select="/ganttproject/links/resources" />
    </a>

    </center></td></tr></table></center>

    <br />

</xsl:template>

<xsl:template name="footer">
    <br />

    <table width="100%" border="0" cellpadding="0" cellspacing="0"><tr>
    <td bgcolor="#6d9ec6" width="50%">
    <font color="#FFFFFF">
    <b><xsl:value-of select="/ganttproject/footer/@version"/></b></font></td >
    <td bgcolor="#6d9ec6" width="50%" align="right">
    <b><a href="http://ganttproject.biz">ganttproject.biz</a></b><br />
	<b><xsl:value-of select="/ganttproject/footer/@date"/></b>
    </td></tr></table>
</xsl:template>

<xsl:template match="/">
    <html>
	<head>
		<meta name="author" content="Alexandre THOMAS" />
	    <title><xsl:value-of select="/ganttproject/title"/></title>

	    <style>
	    A:link {
		FONT-WEIGHT: bold;  TEXT-DECORATION: none;
		FONT-SIZE: 14px ; COLOR: black;
	    }
	    A:visited {
		FONT-WEIGHT: bold;  TEXT-DECORATION: none;
		FONT-SIZE: 14px ; COLOR: black; BACKGROUND: none
	    }
	    A:hover {
		FONT-WEIGHT: bold;  TEXT-DECORATION: none;
		FONT-SIZE: 14px ; COLOR: white ; BACKGROUND: #6d9ec6;
	    }
	    .attachment a:link {
	      font-weight: normal;
	      font-size: 12px;
	      color: #6d9ec6;
	      background: white;
	    }
	    .attachment a:hover {
	      font-weight: normal;
	      text-decoration: underline;
	      font-size: 12px;
	      color: #6d9ec6;
	      background: white;
	    }
	    .attachment a:visited {
	      font-weight: normal;
	      font-size: 12px;
	      background: white;
	      color: gray;
	    }
	    TD {
		FONT-SIZE: 12px; COLOR: black; FONT-STYLE: normal; FONT-FAMILY: Arial, Helvetica, Geneva, sans-serif
	    }
	    UL {
		FONT-SIZE: 12px; FONT-FAMILY: Arial, Helvetica, Geneva, sans-serif
	    }
	    LI {
		FONT-SIZE: 12px; FONT-FAMILY: Arial, Helvetica, Geneva, sans-serif
	    }
	    H1 {
		FONT-WEIGHT: bold; FONT-SIZE: 16pt; COLOR: #6d9ec6;
	    }
	    H2 {
		FONT-WEIGHT: bold; FONT-SIZE: 16pt; COLOR: #000000;
	    }
	    H3 {
		FONT-WEIGHT: bold; FONT-SIZE: 13pt; COLOR: #6d9ec6;
	    }
	    H4 {
		FONT-WEIGHT: bold; FONT-SIZE: 13px; COLOR: #000000;
	    }
	    H5 {
		FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: #6d9ec6;
	    }
	    H6 {
		FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: #000000
	    }

	    </style>
    </head>
    <body bgcolor="white">
    <center>
	    <xsl:call-template name="header"/>
	    <xsl:apply-templates/>
    </center>
    <xsl:call-template name="footer"/>
    </body>
    </html>
</xsl:template>

<xsl:template match="links" />
<xsl:template match="title" />
<xsl:template match="project" />
<xsl:template match="chart" />
<xsl:template match="ganttproject:resources" />
<xsl:template match="ganttproject:tasks" />

</xsl:stylesheet>
