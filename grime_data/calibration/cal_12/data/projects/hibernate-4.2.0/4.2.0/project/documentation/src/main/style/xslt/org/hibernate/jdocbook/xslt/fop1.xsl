<?xml version='1.0'?>
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
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <!-- this one taken verbatim from the Seam xslt -->

    <xsl:template match="*" mode="fop1.outline">
        <xsl:apply-templates select="*" mode="fop1.outline"/>
    </xsl:template>

    <xsl:template match="set|book|part|reference|
                     preface|chapter|appendix|article
                     |glossary|bibliography|index|setindex
                     |refentry
                     |sect1|sect2|sect3|sect4|sect5|section"
                  mode="fop1.outline">

        <xsl:variable name="id">
            <xsl:call-template name="object.id"/>
        </xsl:variable>
        <xsl:variable name="bookmark-label">
            <xsl:apply-templates select="." mode="object.title.markup"/>
        </xsl:variable>

        <!-- Put the root element bookmark at the same level as its children -->
        <!-- If the object is a set or book, generate a bookmark for the toc -->

        <xsl:choose>
            <xsl:when test="parent::*">
                <fo:bookmark internal-destination="{$id}" starting-state="hide">
                    <fo:bookmark-title>
                        <xsl:value-of select="normalize-space(translate($bookmark-label, $a-dia, $a-asc))"/>
                    </fo:bookmark-title>
                    <xsl:apply-templates select="*" mode="fop1.outline"/>
                </fo:bookmark>
            </xsl:when>
            <xsl:otherwise>
                <fo:bookmark internal-destination="{$id}" starting-state="hide">
                    <fo:bookmark-title>
                        <xsl:value-of select="normalize-space(translate($bookmark-label, $a-dia, $a-asc))"/>
                    </fo:bookmark-title>
                </fo:bookmark>

                <xsl:variable name="toc.params">
                    <xsl:call-template name="find.path.params">
                        <xsl:with-param name="table" select="normalize-space($generate.toc)"/>
                    </xsl:call-template>
                </xsl:variable>

                <xsl:if test="contains($toc.params, 'toc')
                    and (book|part|reference|preface|chapter|appendix|article
                         |glossary|bibliography|index|setindex
                         |refentry
                         |sect1|sect2|sect3|sect4|sect5|section)">
                    <fo:bookmark internal-destination="toc...{$id}" starting-state="hide">
                        <fo:bookmark-title>
                            <xsl:call-template name="gentext">
                                <xsl:with-param name="key" select="'TableofContents'"/>
                            </xsl:call-template>
                        </fo:bookmark-title>
                    </fo:bookmark>
                </xsl:if>
                <xsl:apply-templates select="*" mode="fop1.outline"/>
            </xsl:otherwise>
        </xsl:choose>
        <!--
  <fo:bookmark internal-destination="{$id}"/>
-->
    </xsl:template>


</xsl:stylesheet>

