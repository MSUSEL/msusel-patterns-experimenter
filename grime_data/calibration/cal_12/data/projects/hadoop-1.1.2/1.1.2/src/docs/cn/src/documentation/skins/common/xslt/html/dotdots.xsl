<?xml version="1.0" encoding="utf-8"?>
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
Contains the 'dotdots' template, which, given a path, will output a set of
directory traversals to get back to the source directory. Handles both '/' and
'\' directory separators.

Examples:
  Input                           Output 
    index.html                    ""
    dir/index.html                "../"
    dir/subdir/index.html         "../../"
    dir//index.html              "../"
    dir/                          "../"
    dir//                         "../"
    \some\windows\path            "../../"
    \some\windows\path\           "../../../"
    \Program Files\mydir          "../"

Cannot handle ..'s in the path, so don't expect 'dir/subdir/../index.html' to
work.

-->
<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template name="dotdots">
    <xsl:param name="path"/>
    <xsl:variable name="dirs" select="normalize-space(translate(concat($path, 'x'), ' /\', '_  '))"/>
<!-- The above does the following:
       o Adds a trailing character to the path. This prevents us having to deal
         with the special case of ending with '/'
       o Translates all directory separators to ' ', and normalize spaces,
		 cunningly eliminating duplicate '//'s. We also translate any real
		 spaces into _ to preserve them.
    -->
    <xsl:variable name="remainder" select="substring-after($dirs, ' ')"/>
    <xsl:if test="$remainder">
<xsl:text>../</xsl:text>
      <xsl:call-template name="dotdots">
        <xsl:with-param name="path" select="translate($remainder, ' ', '/')"/>
<!-- Translate back to /'s because that's what the template expects. -->
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
<!--
  Uncomment to test.
  Usage: saxon dotdots.xsl dotdots.xsl path='/my/test/path'

  <xsl:param name="path"/>
  <xsl:template match="/">
    <xsl:message>Path: <xsl:value-of select="$path"/></xsl:message>
    <xsl:call-template name="dotdots">
      <xsl:with-param name="path" select="$path"/>
    </xsl:call-template>
  </xsl:template>
 -->
</xsl:stylesheet>
