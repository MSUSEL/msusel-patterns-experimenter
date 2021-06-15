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

<xsl:template match="/">
<html>
    <head>
    <title><xsl:value-of select="/ganttproject/title"/></title>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-2" />
    <meta name="author" content="Pawel Lipinski" />
    <link href="css/styles.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
	    a {
		    text-decoration: none;
		    font-family: Arial;
		    color: rgb(255, 255, 255);
		    border: none;
	    }
	    a:hover {
		    text-decoration: none;
		    font-family: Arial;
		    color: #dc2dff;
		    border: none;
	    }
	    body {
		    margin: 0;
	    }
    </style>
  </head>

  <body>
    <div align="center">
      <table cellpadding="0" cellspacing="0" border="0" width="990" height="620">
        <tr>
          <td valign="top" colspan="2" background="images/top.jpg" width="990" height="94">
            <br />
          </td>
        </tr>

        <tr>
	    <td valign="top" width="136">
	    <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0" background="images/menu.jpg">
                <tr>
                  <td height="50">
                    <br />
                  </td>
		  <td width="5">
                    <br />
                  </td>
                </tr>

                <tr>
                  <td align="right" valign="top">
		    <a target="inner" id="chart">
			<xsl:attribute name="href"><xsl:value-of select="/ganttproject/links/@prefix" />-chart.html</xsl:attribute>  
			<xsl:value-of select="/ganttproject/links/chart" />
		    </a>
                  </td>
                  <td>
                    <br />
                  </td>
                </tr>

                <tr>
                  <td align="right" valign="top">
		    <a target="inner" id="tasks">
			<xsl:attribute name="href"><xsl:value-of select="/ganttproject/links/@prefix" />-tasks.html</xsl:attribute>  
			<xsl:value-of select="/ganttproject/links/tasks" />
		    </a>
                  </td>
                  <td>
                    <br />
                  </td>
                </tr>

                <tr>
                  <td align="right" valign="top">
		    <a target="inner" id="resources">
			<xsl:attribute name="href"><xsl:value-of select="/ganttproject/links/@prefix" />-resources.html</xsl:attribute>  
			<xsl:value-of select="/ganttproject/links/resources" />
		    </a>
                  </td>
                  <td>
                    <br />
                  </td>
                </tr>

                <tr>
                  <td height="300" valign="bottom">
                    <br />
                  </td>
		  <td>
                    <br />
                  </td>
                </tr>
            </table>
          </td>

          <td valign="top" width="854" height="100%">
            <iframe frameborder="0" height="100%" id="inner" name="inner" width="100%">
		<xsl:attribute name="src"><xsl:value-of select="/ganttproject/links/@prefix" />-chart.html</xsl:attribute>
	    </iframe>
          </td>
        </tr>
      </table>
    </div>
  </body>
</html>
</xsl:template>

</xsl:stylesheet>  
