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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:d="http://docbook.org/ns/docbook">

    <xsl:import href="common-base.xsl"/>

    <xsl:param name="siteHref" select="'http://www.hibernate.org'"/>
    <xsl:param name="docHref" select="'http://hibernate.org/Documentation/DocumentationOverview'"/>
    <xsl:param name="siteLinkText" select="'Hibernate.org'"/>

    <xsl:param name="legalnotice.filename">legalnotice.html</xsl:param>

    <xsl:template match="d:legalnotice" mode="chunk-filename">
        <xsl:value-of select="$legalnotice.filename"/>
    </xsl:template>

    <xsl:template name="user.footer.content">
        <hr/>
        <a>
            <xsl:attribute name="href">
                <xsl:value-of select="$legalnotice.filename"/>
            </xsl:attribute>
            <xsl:choose>
                <xsl:when test="//d:book/d:bookinfo/d:copyright[1]">
                    <xsl:apply-templates select="//d:book/d:bookinfo/d:copyright[1]" mode="titlepage.mode"/>
                </xsl:when>
                <xsl:when test="//d:legalnotice[1]">
                    <xsl:apply-templates select="//d:legalnotice[1]" mode="titlepage.mode"/>
                </xsl:when>
            </xsl:choose>
        </a>
    </xsl:template>

</xsl:stylesheet>
