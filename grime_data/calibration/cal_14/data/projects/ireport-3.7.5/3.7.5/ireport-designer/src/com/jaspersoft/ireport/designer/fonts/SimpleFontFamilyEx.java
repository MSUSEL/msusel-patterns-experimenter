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
package com.jaspersoft.ireport.designer.fonts;

import net.sf.jasperreports.engine.fonts.SimpleFontFamily;

/**
 * This is an extended version of the SimpleFontFamily calss of JasperReports
 * used to edit the font family properties for fonts defined in font/fonts.xml
 *
 *
 * @version $Id: SimpleFontFamilyEx.java 0 2009-10-26 11:12:37 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class SimpleFontFamilyEx extends SimpleFontFamily {
    private String normalFont = null;
    private String boldFont = null;
    private String italicFont = null;
    private String boldItalicFont = null;

    /**
     * @return the normalFont
     */
    public String getNormalFont() {
        return normalFont;
    }

    /**
     * @param normalFont the normalFont to set
     */
    public void setNormalFont(String normalFont) {
        this.normalFont = normalFont;
    }

    /**
     * @return the boldFont
     */
    public String getBoldFont() {
        return boldFont;
    }

    /**
     * @param boldFont the boldFont to set
     */
    public void setBoldFont(String boldFont) {
        this.boldFont = boldFont;
    }

    /**
     * @return the italicFont
     */
    public String getItalicFont() {
        return italicFont;
    }

    /**
     * @param italicFont the italicFont to set
     */
    public void setItalicFont(String italicFont) {
        this.italicFont = italicFont;
    }

    /**
     * @return the italicBoldFont
     */
    public String getBoldItalicFont() {
        return boldItalicFont;
    }

    /**
     * @param boldItalicFont the boldItalicFont to set
     */
    public void setBoldItalicFont(String boldItalicFont) {
        this.boldItalicFont = boldItalicFont;
    }



}
