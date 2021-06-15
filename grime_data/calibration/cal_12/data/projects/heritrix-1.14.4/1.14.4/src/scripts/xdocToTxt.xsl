<?xml version="1.0" encoding="UTF-8"?>
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
<!--Transform xdoc files to text.

    $Id: xdocToTxt.xsl 2074 2004-05-19 22:14:37Z stack-sf $
 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" version="1.0" encoding="UTF-8"/>
    <xsl:param name="newline" select="'&#xa;'"/>
    <xsl:param name="gt" select="'&gt;'"/>
    <xsl:param name="lt" select="'&lt;'"/>
    <xsl:param name="space" select="' '"/>
    <xsl:param name="quot" select="'&quot;'"/>
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="section">
        <xsl:value-of select="$newline" /><xsl:text />
        <xsl:number count="section" level="single" format="1.0. "/>
        <xsl:value-of select="@name"/><xsl:text />
        <xsl:value-of select="$newline" /><xsl:text />
        <xsl:apply-templates/>
        <xsl:value-of select="$newline" /><xsl:text />
    </xsl:template>
    <xsl:template match="subsection">
        <xsl:value-of select="$newline" /><xsl:text />
        <xsl:number count="section|subsection" level="multiple" format="1.1. "/>
        <xsl:value-of select="@name"/><xsl:text />
        <xsl:value-of select="$newline" /><xsl:text />
        <xsl:apply-templates/>
        <xsl:value-of select="$newline" /><xsl:text />
    </xsl:template>
    <xsl:template match="release">
        <xsl:value-of select="$newline" /><xsl:text />
        <xsl:value-of select="@version"/><xsl:text />
        <xsl:value-of select="$space" /><xsl:text />
        <xsl:value-of select="@date"/><xsl:text />
        <xsl:value-of select="$newline" /><xsl:text />
        <xsl:apply-templates/>
        <xsl:value-of select="$newline" /><xsl:text />
    </xsl:template>
    <xsl:template match="action">
        <xsl:value-of select="$newline" /><xsl:text />
        <xsl:value-of select="$quot" /><xsl:text />
        <xsl:apply-templates/>
        <xsl:value-of select="$quot" /><xsl:text />
        <xsl:value-of select="$space" /><xsl:text />
        <xsl:value-of select="@type"/><xsl:text />
        <xsl:value-of select="$space" /><xsl:text />
        <xsl:value-of select="@dev"/><xsl:text />
        <xsl:value-of select="$space" /><xsl:text />
        <xsl:value-of select="$newline" /><xsl:text />
    </xsl:template>
    <xsl:template match="a">
        <xsl:value-of select="normalize-space(.)"/><xsl:text />
        <xsl:value-of select="$space" /><xsl:text />
        <xsl:value-of select="$lt" /><xsl:text />
        <xsl:value-of select="@href"/><xsl:text />
        <xsl:value-of select="$gt" /><xsl:text />
    </xsl:template>
    <xsl:template match="p">
        <xsl:apply-templates />
        <xsl:value-of select="$space" /><xsl:text />
    </xsl:template>
    <xsl:template match="img"> &lt;<xsl:value-of select="@src"/>&gt;
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="text()" >
        <xsl:value-of select="normalize-space(.)" /><xsl:text />
    </xsl:template>
</xsl:stylesheet>
