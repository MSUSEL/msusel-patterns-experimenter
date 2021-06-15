/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.itextpdf.text.pdf;
import java.util.HashMap;

import com.itextpdf.text.Rectangle;

/**
 * Implements the appearance stream to be used with form fields..
 */

public class PdfAppearance extends PdfTemplate {

    public static final HashMap<String, PdfName> stdFieldFontNames = new HashMap<String, PdfName>();
    static {
        stdFieldFontNames.put("Courier-BoldOblique", new PdfName("CoBO"));
        stdFieldFontNames.put("Courier-Bold", new PdfName("CoBo"));
        stdFieldFontNames.put("Courier-Oblique", new PdfName("CoOb"));
        stdFieldFontNames.put("Courier", new PdfName("Cour"));
        stdFieldFontNames.put("Helvetica-BoldOblique", new PdfName("HeBO"));
        stdFieldFontNames.put("Helvetica-Bold", new PdfName("HeBo"));
        stdFieldFontNames.put("Helvetica-Oblique", new PdfName("HeOb"));
        stdFieldFontNames.put("Helvetica", PdfName.HELV);
        stdFieldFontNames.put("Symbol", new PdfName("Symb"));
        stdFieldFontNames.put("Times-BoldItalic", new PdfName("TiBI"));
        stdFieldFontNames.put("Times-Bold", new PdfName("TiBo"));
        stdFieldFontNames.put("Times-Italic", new PdfName("TiIt"));
        stdFieldFontNames.put("Times-Roman", new PdfName("TiRo"));
        stdFieldFontNames.put("ZapfDingbats", PdfName.ZADB);
        stdFieldFontNames.put("HYSMyeongJo-Medium", new PdfName("HySm"));
        stdFieldFontNames.put("HYGoThic-Medium", new PdfName("HyGo"));
        stdFieldFontNames.put("HeiseiKakuGo-W5", new PdfName("KaGo"));
        stdFieldFontNames.put("HeiseiMin-W3", new PdfName("KaMi"));
        stdFieldFontNames.put("MHei-Medium", new PdfName("MHei"));
        stdFieldFontNames.put("MSung-Light", new PdfName("MSun"));
        stdFieldFontNames.put("STSong-Light", new PdfName("STSo"));
        stdFieldFontNames.put("MSungStd-Light", new PdfName("MSun"));
        stdFieldFontNames.put("STSongStd-Light", new PdfName("STSo"));
        stdFieldFontNames.put("HYSMyeongJoStd-Medium", new PdfName("HySm"));
        stdFieldFontNames.put("KozMinPro-Regular", new PdfName("KaMi"));
    }

    /**
     *Creates a <CODE>PdfAppearance</CODE>.
     */

    PdfAppearance() {
        super();
        separator = ' ';
    }

    PdfAppearance(PdfIndirectReference iref) {
        thisReference = iref;
    }

    /**
     * Creates new PdfTemplate
     *
     * @param wr the <CODE>PdfWriter</CODE>
     */

    PdfAppearance(PdfWriter wr) {
        super(wr);
        separator = ' ';
    }

    /**
     * Creates a new appearance to be used with form fields.
     *
     * @param writer the PdfWriter to use
     * @param width the bounding box width
     * @param height the bounding box height
     * @return the appearance created
     */
    public static PdfAppearance createAppearance(PdfWriter writer, float width, float height) {
        return createAppearance(writer, width, height, null);
    }

    static PdfAppearance createAppearance(PdfWriter writer, float width, float height, PdfName forcedName) {
        PdfAppearance template = new PdfAppearance(writer);
        template.setWidth(width);
        template.setHeight(height);
        writer.addDirectTemplateSimple(template, forcedName);
        return template;
    }

    /**
     * Set the font and the size for the subsequent text writing.
     *
     * @param bf the font
     * @param size the font size in points
     */
    @Override
    public void setFontAndSize(BaseFont bf, float size) {
        checkWriter();
        state.size = size;
        if (bf.getFontType() == BaseFont.FONT_TYPE_DOCUMENT) {
            state.fontDetails = new FontDetails(null, ((DocumentFont)bf).getIndirectReference(), bf);
        }
        else
            state.fontDetails = writer.addSimple(bf);
        PdfName psn = stdFieldFontNames.get(bf.getPostscriptFontName());
        if (psn == null) {
            if (bf.isSubset() && bf.getFontType() == BaseFont.FONT_TYPE_TTUNI)
                psn = state.fontDetails.getFontName();
            else {
                psn = new PdfName(bf.getPostscriptFontName());
                state.fontDetails.setSubset(false);
            }
        }
        PageResources prs = getPageResources();
//        PdfName name = state.fontDetails.getFontName();
        prs.addFont(psn, state.fontDetails.getIndirectReference());
        content.append(psn.getBytes()).append(' ').append(size).append(" Tf").append_i(separator);
    }

    @Override
    public PdfContentByte getDuplicate() {
        PdfAppearance tpl = new PdfAppearance();
        tpl.writer = writer;
        tpl.pdf = pdf;
        tpl.thisReference = thisReference;
        tpl.pageResources = pageResources;
        tpl.bBox = new Rectangle(bBox);
        tpl.group = group;
        tpl.layer = layer;
        if (matrix != null) {
            tpl.matrix = new PdfArray(matrix);
        }
        tpl.separator = separator;
        return tpl;
    }
}