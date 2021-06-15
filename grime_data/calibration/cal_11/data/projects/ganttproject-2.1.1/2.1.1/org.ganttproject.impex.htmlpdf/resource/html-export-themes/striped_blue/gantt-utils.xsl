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

<xsl:template match="/">
    <html>
    <head>
	<meta http-equiv="content-type" content="text/html; charset=ISO-8859-2" />
	<link href="css/styles.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
	    th {
			font-family: Tahoma,Arial;
			font-size: 12pt;
			font-weight: Bold;
			color: #002000;
			background-color: #FFFFFF;
	    }
	    td {
			font-family: Arial;
			font-size: 10pt;
			color: #00194c;
			text-align: center;
	    }
		a {
		    text-decoration: bold;
		    font-family: Arial;
		    color: rgb(0, 0, 0);
		    border: none;
	    }
	    a:hover {
		    text-decoration: bold;
		    font-family: Arial;
		    color: #dc2dff;
		    border: none;
	    }
	</style>
    </head>
    <body bgcolor="#e0ffb0" background="images/backg.png">
	<div align="center">
	    <xsl:apply-templates/>
	</div>		
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
