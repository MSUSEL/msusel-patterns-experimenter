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
	<xsl:output
		method="xml"
		indent="yes"
		omit-xml-declaration="yes"
		doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"
		doctype-public="-//W3C//DTD XHTML 1.1//EN"
	/>

	<xsl:param name="target" />
	<xsl:param name="title" />
	<xsl:preserve-space elements="*" />

	<xsl:template name="namespace">
		<li class="closed">
			<span class="namespace" title="Namespace"><a href="#"><xsl:value-of select="@name" /></a></span>
			<ul>
				<xsl:for-each select="namespace">
					<xsl:call-template name="namespace" />
				</xsl:for-each>

				<xsl:for-each select="class">
					<xsl:call-template name="class" />
				</xsl:for-each>

				<xsl:for-each select="members/*">
					<xsl:call-template name="member" />
				</xsl:for-each>
			</ul>
		</li>
	</xsl:template>

	<xsl:template name="class">
		<li>
			<span class="class" title="Class">
				<xsl:if test="@static">
					<xsl:attribute name="class">singleton</xsl:attribute>
					<xsl:attribute name="title">Singleton class</xsl:attribute>
				</xsl:if>

				<a>
					<xsl:attribute name="href">class_<xsl:value-of select="@fullname" />.html</xsl:attribute>
					<xsl:text><xsl:value-of select="@name" /></xsl:text>
				</a>
			</span>
		</li>
	</xsl:template>

	<xsl:template name="member">
		<li>
			<span class="Member">
				<xsl:attribute name="class"><xsl:value-of select="name()" /></xsl:attribute>
				<a>
					<xsl:attribute name="href">member_<xsl:value-of select="@fullname" />.html</xsl:attribute>
					<xsl:text><xsl:value-of select="@name" /></xsl:text>
				</a>
			</span>
		</li>
	</xsl:template>

	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml">
		<head>
			<title><xsl:value-of select="$title" /></title>
			<xsl:text disable-output-escaping="yes"><![CDATA[
<meta name="generator" content="MoxieDoc" />

<link rel="stylesheet" type="text/css" href="css/reset.css" />
<link rel="stylesheet" type="text/css" href="css/grids.css" />
<link rel="stylesheet" type="text/css" href="css/general.css" />
<link rel="stylesheet" type="text/css" href="css/jquery.treeview.css" />
<link type="text/css" rel="stylesheet" href="css/shCore.css" />
<link type="text/css" rel="stylesheet" href="css/shThemeMoxieDoc.css" />

<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("jquery", "1.3");
</script>
<script type="text/javascript" src="js/jquery.treeview.min.js"></script>
<script type="text/javascript" src="js/general.js"></script>
<script type="text/javascript" src="js/shCore.js"></script>
<script type="text/javascript" src="js/shBrushJScript.js"></script>
]]></xsl:text>
		</head>
		<body>
			<div id="doc3" class="yui-t1" style="height:500px">
				<div id="hd"><h1><xsl:value-of select="$title" /></h1></div>

				<div id="bd">
					<div id="yui-main">
						<div id="detailsView" class="yui-b">
							<xsl:comment>Gets filled using Ajax</xsl:comment>
						</div>
					</div>

					<div id="classView" class="yui-b">
						<ul id="browser" class="classtree treeview-famfamfam">
							<li><span class="root">API Documentation</span>
								<ul>
									<xsl:for-each select="model/namespace">
										<xsl:call-template name="namespace" />
									</xsl:for-each>

									<xsl:for-each select="model/class">
										<xsl:call-template name="class" />
									</xsl:for-each>

									<xsl:for-each select="model/members/*">
										<xsl:call-template name="member" />
									</xsl:for-each>
								</ul>
							</li>
						</ul>
					</div>
				</div>
			</div> 
		</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
