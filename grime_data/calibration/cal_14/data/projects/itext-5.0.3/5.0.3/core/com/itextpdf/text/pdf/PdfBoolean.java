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
 * <CODE>PdfBoolean</CODE> is the boolean object represented by the keywords <VAR>true</VAR> or <VAR>false</VAR>.
 * <P>
 * This object is described in the 'Portable Document Format Reference Manual version 1.7'
 * section 3.2.1 (page 52).
 *
 * @see		PdfObject
 * @see		BadPdfFormatException
 */

public class PdfBoolean extends PdfObject {
    
    // static membervariables (possible values of a boolean object)
    public static final PdfBoolean PDFTRUE = new PdfBoolean(true);
    public static final PdfBoolean PDFFALSE = new PdfBoolean(false);
/** A possible value of <CODE>PdfBoolean</CODE> */
    public static final String TRUE = "true";
    
/** A possible value of <CODE>PdfBoolean</CODE> */
    public static final String FALSE = "false";
    
    // membervariables
    
/** the boolean value of this object */
    private boolean value;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfBoolean</CODE>-object.
 *
 * @param		value			the value of the new <CODE>PdfObject</CODE>
 */
    
    public PdfBoolean(boolean value) {
        super(BOOLEAN);
        if (value) {
            setContent(TRUE);
        }
        else {
            setContent(FALSE);
        }
        this.value = value;
    }
    
/**
 * Constructs a <CODE>PdfBoolean</CODE>-object.
 *
 * @param		value			the value of the new <CODE>PdfObject</CODE>, represented as a <CODE>String</CODE>
 *
 * @throws		BadPdfFormatException	thrown if the <VAR>value</VAR> isn't '<CODE>true</CODE>' or '<CODE>false</CODE>'
 */
    
    public PdfBoolean(String value) throws BadPdfFormatException {
        super(BOOLEAN, value);
        if (value.equals(TRUE)) {
            this.value = true;
        }
        else if (value.equals(FALSE)) {
            this.value = false;
        }
        else {
            throw new BadPdfFormatException(MessageLocalization.getComposedMessage("the.value.has.to.be.true.of.false.instead.of.1", value));
        }
    }
    
    // methods returning the value of this object
    
/**
 * Returns the primitive value of the <CODE>PdfBoolean</CODE>-object.
 *
 * @return		the actual value of the object.
 */
    
    public boolean booleanValue() {
        return value;
    }
    
    public String toString() {
    	return value ? TRUE : FALSE;
    }
}
