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
    version="1.0">
    <xsl:import href="../doc/docbook-xsl-1.64.1/fo/docbook.xsl"/>

    <xsl:variable name="datetime">
          <xsl:value-of select="//articleinfo/date"/>
    </xsl:variable>

    <xsl:template match="processing-instruction('custom-pagebreak')">
        <fo:block break-before='page'/>
    </xsl:template>

    <xsl:param name="paper.type">A4</xsl:param>
    <xsl:param name="draft.mode">no</xsl:param>
    <xsl:param name="draft.watermark.image"></xsl:param>
    <xsl:param name="appendix.autolabel">1</xsl:param>
    <xsl:param name="fop.extensions">1</xsl:param>
    <xsl:param name="shade.verbatim">1</xsl:param>

    <!-- Space between paper border and content (chaotic stuff, don't touch) -->

    <xsl:param name="title.margin.left">2pt</xsl:param>

    <xsl:param name="page.margin.top">5mm</xsl:param>
    <xsl:param name="region.before.extent">10mm</xsl:param>
    <xsl:param name="body.margin.top">10mm</xsl:param>

    <xsl:param name="alignment">left</xsl:param>

    <xsl:param name="body.margin.bottom">15mm</xsl:param>
    <xsl:param name="region.after.extent">10mm</xsl:param>
    <xsl:param name="page.margin.bottom">0mm</xsl:param>

    <xsl:param name="page.margin.outer">18mm</xsl:param>
    <xsl:param name="page.margin.inner">18mm</xsl:param>

    <!-- Label Chapters and Sections (numbering) -->
    <xsl:param name="chapter.autolabel">1</xsl:param>
    <xsl:param name="section.autolabel">1</xsl:param>
    <xsl:param name="section.label.includes.component.label">1</xsl:param>

    <!-- Format Variable Lists as Blocks (prevents horizontal overflow) -->
    <xsl:param name="variablelist.as.blocks">1</xsl:param>

    <xsl:attribute-set name="root.properties">
        <xsl:attribute name="color">black</xsl:attribute>
        <xsl:attribute name="font-family">sans-serif</xsl:attribute>
        <xsl:attribute name="font-size">9pt</xsl:attribute>
    </xsl:attribute-set>


    <xsl:attribute-set name="verbatim.properties">
        <xsl:attribute name="space-before.minimum">1em</xsl:attribute>
        <xsl:attribute name="space-before.optimum">1em</xsl:attribute>
        <xsl:attribute name="space-before.maximum">1em</xsl:attribute>
        <!-- alef: commented out because footnotes were screwed because of it -->
        <!--<xsl:attribute name="space-after.minimum">0.1em</xsl:attribute>
        <xsl:attribute name="space-after.optimum">0.1em</xsl:attribute>
        <xsl:attribute name="space-after.maximum">0.1em</xsl:attribute>-->
        <xsl:attribute name="border-color">#444444</xsl:attribute>
        <xsl:attribute name="border-style">solid</xsl:attribute>
        <xsl:attribute name="border-width">0.1pt</xsl:attribute>
        <xsl:attribute name="padding-top">0.5em</xsl:attribute>
        <xsl:attribute name="padding-left">0.5em</xsl:attribute>
        <xsl:attribute name="padding-right">0.5em</xsl:attribute>
        <xsl:attribute name="padding-bottom">0.5em</xsl:attribute>
        <xsl:attribute name="margin-left">0.5em</xsl:attribute>
        <xsl:attribute name="margin-right">0.5em</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="monospace.verbatim.properties"
        use-attribute-sets="verbatim.properties">
        <xsl:attribute name="font-family">
            <xsl:value-of select="$monospace.font.family"/>
        </xsl:attribute>
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 0.8"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
    </xsl:attribute-set>


    <!--
    <xsl:attribute-set name="article.titlepage.recto.style">
      <xsl:attribute name="font-family">sans-serif</xsl:attribute>
      <xsl:attribute name="text-align">left</xsl:attribute>
      <xsl:attribute name="color">#000000</xsl:attribute>
    </xsl:attribute-set>

    -->

    <!--###################################################
                       Custom Title Page
        ################################################### -->


    <xsl:template name="article.titlepage.recto">
        <fo:block>
            <fo:table table-layout="fixed" width="175mm">
                <fo:table-column column-width="175mm"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell text-align="center">
                            <fo:block>
                                <fo:external-graphic src="file:docbook/images/logo.gif"/>
                            </fo:block>
                            <fo:block font-family="Helvetica" font-size="22pt" padding-before="10mm">
                                <xsl:value-of select="articleinfo/title"/>
                            </fo:block>
                            <fo:block font-family="Helvetica" font-size="12pt" padding="10mm">
                                <xsl:value-of select="articleinfo/subtitle"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell text-align="center">
                            <fo:block font-family="Helvetica" font-size="14pt" padding="10mm">
                                <xsl:value-of select="$datetime"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell text-align="center">
                            <fo:block font-family="Helvetica" font-size="12pt" padding="10mm">
                                <xsl:text>Author:  </xsl:text>
                                <xsl:for-each select="articleinfo/author">
                                    <xsl:if test="position() > 1">
                                        <xsl:text>, </xsl:text>
                                    </xsl:if>
                                    <xsl:value-of select="firstname"/>
                                    <xsl:text> </xsl:text>
                                    <xsl:value-of select="surname"/>
                                </xsl:for-each>
                            </fo:block>
                            <fo:block font-family="Helvetica" font-size="10pt" padding="1mm">
                                <xsl:value-of select="articleinfo/legalnotice"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
        <fo:block break-after='page'/>

        <fo:block>
            <fo:table table-layout="fixed" width="175mm">
                <fo:table-column column-width="175mm"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell text-align="left">
                        <fo:block font-family="Helvetica" font-size="22pt" padding-before="10mm">
                            <xsl:text>Revisions</xsl:text>
                        </fo:block>
                        </fo:table-cell>
                    </fo:table-row>

                    <fo:table-row>
                        <fo:table-cell text-align="left">
                        <fo:block padding-before="10mm">
                          <xsl:apply-templates
                                 mode="book.titlepage.recto.mode"
                                 select="articleinfo/revhistory"/>
                        </fo:block>
                      </fo:table-cell>
                    </fo:table-row >
                </fo:table-body>
            </fo:table>
        </fo:block>

        <fo:block break-after='page'/>
     </xsl:template>

    <!--###################################################
                          Custom Footer
        ################################################### -->

    <!-- This footer prints the version number on the left side -->
    <xsl:template name="footer.content">
        <xsl:param name="pageclass" select="''"/>
        <xsl:param name="sequence" select="''"/>
        <xsl:param name="position" select="''"/>
        <xsl:param name="gentext-key" select="''"/>

        <xsl:variable name="Version">
            <xsl:choose>
                <xsl:when test="//revnumber">
                    <xsl:text>Revision </xsl:text>
                    <xsl:value-of select="//revnumber"/>
                        <xsl:text>, </xsl:text>
                        <xsl:value-of select="$datetime"/>
                </xsl:when>
                <xsl:otherwise>
                    <!-- nop -->
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:choose>
            <xsl:when test="$sequence='blank'">
                <xsl:choose>
                    <xsl:when test="$double.sided != 0 and $position = 'left'">
                        <xsl:value-of select="$Version"/>
                    </xsl:when>

                    <xsl:when test="$double.sided = 0 and $position = 'center'">
                        <!-- nop -->
                    </xsl:when>

                    <xsl:otherwise>
                        <fo:page-number/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>

            <xsl:when test="$pageclass='titlepage'">
                <!-- nop: other titlepage sequences have no footer -->
            </xsl:when>

            <xsl:when test="$double.sided != 0 and $sequence = 'even' and $position='left'">
                <fo:page-number/>
            </xsl:when>

            <xsl:when test="$double.sided != 0 and $sequence = 'odd' and $position='right'">
                <fo:page-number/>
            </xsl:when>

            <xsl:when test="$double.sided = 0 and $position='right'">
                <fo:page-number/>
            </xsl:when>

            <xsl:when test="$double.sided != 0 and $sequence = 'odd' and $position='left'">
                <xsl:value-of select="$Version"/>
            </xsl:when>

            <xsl:when test="$double.sided != 0 and $sequence = 'even' and $position='right'">
                <xsl:value-of select="$Version"/>
            </xsl:when>

            <xsl:when test="$double.sided = 0 and $position='left'">
                <xsl:value-of select="$Version"/>
            </xsl:when>

            <xsl:otherwise>
                <!-- nop -->
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


</xsl:stylesheet>

