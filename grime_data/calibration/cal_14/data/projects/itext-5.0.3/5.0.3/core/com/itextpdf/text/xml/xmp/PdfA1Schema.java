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
package com.itextpdf.text.xml.xmp;

/**
 * An implementation of an XmpSchema.
 */
public class PdfA1Schema extends XmpSchema {
    
    private static final long serialVersionUID = 5300646133692948168L;
    /** default namespace identifier*/
    public static final String DEFAULT_XPATH_ID = "pdfaid";
    /** default namespace uri*/
    public static final String DEFAULT_XPATH_URI = "http://www.aiim.org/pdfa/ns/id/";
    
    /** Part, always 1. */
    public static final String PART = "pdfaid:part";
    /** Conformance, A or B. */
    public static final String CONFORMANCE = "pdfaid:conformance";
    
    public PdfA1Schema() {
        super("xmlns:" + DEFAULT_XPATH_ID + "=\"" + DEFAULT_XPATH_URI + "\"");
        addPart("1");
    }
    
    /**
     * Adds part.
     * @param part
     */
    public void addPart(String part) {
        setProperty(PART, part);
    }
    
    /**
     * Adds the conformance.
     * @param conformance
     */
    public void addConformance(String conformance) {
        setProperty(CONFORMANCE, conformance);
    }
}