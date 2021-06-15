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

import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * <CODE>PdfNumber</CODE> provides two types of numbers, integer and real.
 * <P>
 * Integers may be specified by signed or unsigned constants. Reals may only be
 * in decimal format.<BR>
 * This object is described in the 'Portable Document Format Reference Manual
 * version 1.7' section 3.3.2 (page 52-53).
 *
 * @see		PdfObject
 * @see		BadPdfFormatException
 */
public class PdfNumber extends PdfObject {

    // CLASS VARIABLES
    
    /**
     * actual value of this <CODE>PdfNumber</CODE>, represented as a
     * <CODE>double</CODE>
     */
    private double value;
    
    // CONSTRUCTORS
    
    /**
     * Constructs a <CODE>PdfNumber</CODE>-object.
     *
     * @param content    value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(String content) {
        super(NUMBER);
        try {
            value = Double.parseDouble(content.trim());
            setContent(content);
        }
        catch (NumberFormatException nfe){
            throw new RuntimeException(MessageLocalization.getComposedMessage("1.is.not.a.valid.number.2", content, nfe.toString()));
        }
    }
    
    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type integer.
     *
     * @param value    value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(int value) {
        super(NUMBER);
        this.value = value;
        setContent(String.valueOf(value));
    }
    
    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type real.
     *
     * @param value    value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(double value) {
        super(NUMBER);
        this.value = value;
        setContent(ByteBuffer.formatDouble(value));
    }
    
    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type real.
     *
     * @param value    value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(float value) {
        this((double)value);
    }
    
    // methods returning the value of this object
    
    /**
     * Returns the primitive <CODE>int</CODE> value of this object.
     *
     * @return The value as <CODE>int</CODE>
     */
    public int intValue() {
        return (int) value;
    }
    
    /**
     * Returns the primitive <CODE>double</CODE> value of this object.
     *
     * @return The value as <CODE>double</CODE>
     */
    public double doubleValue() {
        return value;
    }
    
    /**
     * Returns the primitive <CODE>float</CODE> value of this object.
     *
     * @return The value as <CODE>float</CODE>
     */
    public float floatValue() {
        return (float)value;
    }
    
    // other methods
    
    /**
     * Increments the value of the <CODE>PdfNumber</CODE>-object by 1.
     */
    public void increment() {
        value += 1.0;
        setContent(ByteBuffer.formatDouble(value));
    }
}
