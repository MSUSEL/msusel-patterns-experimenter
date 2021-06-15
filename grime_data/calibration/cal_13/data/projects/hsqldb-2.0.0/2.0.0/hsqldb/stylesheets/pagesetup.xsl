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
<xsl:stylesheet exclude-result-prefixes="d"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:d="http://docbook.org/ns/docbook"
xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version="1.0">

<!-- ********************************************************************
     $Id: pagesetup.xsl 3538 2010-03-17 13:55:43Z unsaved $
     ********************************************************************

     This file is part of the DocBook XSL Stylesheet distribution.
     See ../README or http://docbook.sf.net/ for copyright
     copyright and other information.

     ******************************************************************** -->

<!-- ==================================================================== -->

<xsl:param name="body.fontset">
  <xsl:value-of select="$body.font.family"/>
  <xsl:if test="$body.font.family != ''
                and $symbol.font.family  != ''">,</xsl:if>
    <xsl:value-of select="$symbol.font.family"/>
</xsl:param>

<xsl:param name="title.fontset">
  <xsl:value-of select="$title.font.family"/>
  <xsl:if test="$title.font.family != ''
                and $symbol.font.family  != ''">,</xsl:if>
    <xsl:value-of select="$symbol.font.family"/>
</xsl:param>

<!-- PassiveTeX can't handle the math expression for
     title.margin.left being negative, so ignore it.
     margin-left="{$page.margin.outer} - {$title.margin.left}"
-->
<xsl:param name="margin.left.outer">
  <xsl:choose>
    <xsl:when test="$passivetex.extensions != 0">
      <xsl:value-of select="$page.margin.outer"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$page.margin.outer"/>
      <xsl:text> - </xsl:text>
      <xsl:value-of select="$title.margin.left"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:param>

<xsl:param name="margin.left.inner">
  <xsl:choose>
    <xsl:when test="$passivetex.extensions != 0">
      <xsl:value-of select="$page.margin.inner"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$page.margin.inner"/>
      <xsl:text> - </xsl:text>
      <xsl:value-of select="$title.margin.left"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:param>

<xsl:template name="header.content">
  <xsl:param name="pageclass" select="''"/>
  <xsl:param name="sequence" select="''"/>
  <xsl:param name="position" select="''"/>
  <xsl:param name="gentext-key" select="''"/>

<!--
  <fo:block>
    <xsl:value-of select="$pageclass"/>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="$sequence"/>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="$position"/>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="$gentext-key"/>
  </fo:block>
-->

  <fo:block>

    <!-- sequence can be odd, even, first, blank -->
    <!-- position can be left, center, right -->
    <xsl:choose>
      <xsl:when test="$sequence = 'blank'">
        <!-- nothing -->
      </xsl:when>

      <xsl:when test="$position='left'">
        <!-- Same for odd, even, empty, and blank sequences -->
        <!--
        <xsl:call-template name="draft.text"/>
        -->
        <fo:external-graphic content-height="0.43cm">
          <xsl:attribute name="src">
            <xsl:call-template name="fo-external-image">
              <xsl:with-param name="filename">../images/hypersql_logo2.png</xsl:with-param>
            </xsl:call-template>
          </xsl:attribute>
        </fo:external-graphic>
      </xsl:when>

      <xsl:when test="($sequence='odd' or $sequence='even') and $position='right'">
        <xsl:if test="$pageclass != 'titlepage'">
          <xsl:choose>
            <xsl:when test="ancestor::d:book and ($double.sided != 0)">
              <fo:retrieve-marker retrieve-class-name="section.head.marker"
                                  retrieve-position="first-including-carryover"
                                  retrieve-boundary="page-sequence"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:apply-templates select="." mode="titleabbrev.markup"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:if>
      </xsl:when>

      <xsl:when test="$position='center'">
        <!-- nothing for empty and blank sequences -->
      </xsl:when>

      <xsl:when test="$position='right'">
        <!-- Same for odd, even, empty, and blank sequences -->
        <xsl:call-template name="draft.text"/>
      </xsl:when>

      <xsl:when test="$sequence = 'first'">
        <!-- nothing for first pages -->
      </xsl:when>

      <xsl:when test="$sequence = 'blank'">
        <!-- nothing for blank pages -->
      </xsl:when>
    </xsl:choose>
  </fo:block>
</xsl:template>
</xsl:stylesheet>
