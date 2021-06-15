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
<!--
Some callable templates useful when dealing with tab paths.  Mostly used in
tab-to-menu.xsl
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:param name="site-file" select="'cocoon://abs-menulinks'"/>
  <xsl:variable name="site" select="document($site-file)"/>
<!-- Given the current path and a tabs.xml entry, returns a relative path to
  the specified tab's URL.  When rendering a set of tabs, this template will be
  called once per tab.
  -->
  <xsl:template name="calculate-tab-href">
    <xsl:param name="dir_index" select="'index.html'"/>
    <xsl:param name="tab"/>
<!-- current 'tab' node -->
    <xsl:param name="path" select="$path"/>
    <xsl:if test="starts-with($tab/@href, 'http')">
<!-- Absolute URL -->
      <xsl:value-of select="$tab/@href"/>
    </xsl:if>
    <xsl:if test="not(starts-with($tab/@href, 'http'))">
<!-- Root-relative path -->
      <xsl:variable name="backpath">
        <xsl:call-template name="dotdots">
          <xsl:with-param name="path" select="$path"/>
        </xsl:call-template>
<xsl:text>/</xsl:text>
        <xsl:value-of select="$tab/@dir | $tab/@href"/>
<!-- If we obviously have a directory, add /index.html -->
        <xsl:if test="$tab/@dir or substring($tab/@href, string-length($tab/@href),
          string-length($tab/@href)) = '/'">
<xsl:text>/</xsl:text>
          <xsl:if test="$tab/@indexfile">
            <xsl:value-of select="$tab/@indexfile"/>
          </xsl:if>
          <xsl:if test="not(@indexfile)">
            <xsl:value-of select="$dir_index"/>
          </xsl:if>
        </xsl:if>
      </xsl:variable>
      <xsl:value-of
        select="translate(normalize-space(translate($backpath, ' /', '/ ')), ' /', '/ ')"/>
<!-- Link to backpath, normalizing slashes -->
    </xsl:if>
  </xsl:template>
<!--
    The id of any tab, whose path is a subset of the current URL.  Ie,
    the path of the 'current' tab.
  -->
  <xsl:template name="matching-id" xmlns:l="http://apache.org/forrest/linkmap/1.0">
    <xsl:value-of select="$site//*[starts-with(@href, $path)]/@tab"/>
  </xsl:template>
<!--
    The longest path of any level 1 tab, whose path is a subset of the current URL.  Ie,
    the path of the 'current' tab.
  -->
  <xsl:template name="longest-dir">
    <xsl:param name="tabfile"/>
    <xsl:for-each select="$tabfile/tabs/tab[starts-with($path, @dir|@href)]">
      <xsl:sort select="string-length(@dir|@href)"
        data-type="number" order="descending"/>
      <xsl:if test="position()=1">
        <xsl:value-of select="@dir|@href"/>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
<!--
    The longest path of any level 2 tab, whose path is a subset of the current URL.  Ie,
    the path of the 'current' tab.
  -->
  <xsl:template name="level2-longest-dir">
    <xsl:param name="tabfile"/>
    <xsl:for-each select="$tabfile/tabs/tab/tab[starts-with($path, @dir|@href)]">
      <xsl:sort select="string-length(@dir|@href)"
        data-type="number" order="descending"/>
      <xsl:if test="position()=1">
        <xsl:value-of select="@dir|@href"/>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
