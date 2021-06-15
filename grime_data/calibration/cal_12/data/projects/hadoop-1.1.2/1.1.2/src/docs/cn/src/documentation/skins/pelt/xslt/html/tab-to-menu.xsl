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
This stylesheet generates 'tabs' at the top left of the screen.  Tabs are
visual indicators that a certain subsection of the URI space is being browsed.
For example, if we had tabs with paths:

Tab1:  ''
Tab2:  'community'
Tab3:  'community/howto'
Tab4:  'community/howto/form/index.html'

Then if the current path was 'community/howto/foo', Tab3 would be highlighted.
The rule is: the tab with the longest path that forms a prefix of the current
path is enabled.

The output of this stylesheet is HTML of the form:
    <div class="tab">
      ...
    </div>

which is then merged by site-to-xhtml.xsl

-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:import href="../../../common/xslt/html/tab-to-menu.xsl"/>
  <xsl:template match="tabs">
    <ul id="tabs">
      <xsl:call-template name="base-tabs"/>
    </ul>
    <span id="level2tabs">
      <xsl:call-template name="level2tabs"/>
    </span>
  </xsl:template>
  <xsl:template name="pre-separator"></xsl:template>
  <xsl:template name="post-separator"></xsl:template>
  <xsl:template name="separator"></xsl:template>
  <xsl:template name="level2-separator"></xsl:template>
  <xsl:template name="selected">
    <li class="current"><xsl:call-template name="base-selected"/></li>
  </xsl:template>
  <xsl:template name="not-selected">
    <li><xsl:call-template name="base-not-selected"/></li>
  </xsl:template>
  <xsl:template name="level2-not-selected">
    <xsl:call-template name="base-not-selected"/>
  </xsl:template>
  <xsl:template name="level2-selected">
    <xsl:call-template name="base-selected"/>
  </xsl:template>
</xsl:stylesheet>
