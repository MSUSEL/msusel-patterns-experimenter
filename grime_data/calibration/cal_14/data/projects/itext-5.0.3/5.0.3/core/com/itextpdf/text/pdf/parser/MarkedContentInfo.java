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
package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;

/**
 * Represents a Marked Content block in a PDF
 * @since 5.0.2
 */
public class MarkedContentInfo {
    private final PdfName tag;
    private final PdfDictionary dictionary;
    
    public MarkedContentInfo(PdfName tag, PdfDictionary dictionary) {
        this.tag = tag;
        this.dictionary = dictionary != null ? dictionary : new PdfDictionary(); // I'd really prefer to make a defensive copy here to make this immutable
    }

    /**
     * Get the tag of this marked content
     * @return the tag of this marked content
     */
    public PdfName getTag(){
        return tag;
    }
    
    /**
     * Determine if an MCID is available
     * @return true if the MCID is available, false otherwise
     */
    public boolean hasMcid(){
        return dictionary.contains(PdfName.MCID);
    }
    
    /**
     * Gets the MCID value  If the Marked Content contains
     * an MCID entry, returns that value.  Otherwise, a {@link NullPointerException} is thrown.
     * @return the MCID value
     * @throws NullPointerException if there is no MCID (see {@link MarkedContentInfo#hasMcid()})
     */
    public int getMcid(){
        PdfNumber id = dictionary.getAsNumber(PdfName.MCID);
        if (id == null)
            throw new IllegalStateException("MarkedContentInfo does not contain MCID");
        
        return id.intValue();
    }
    
}
