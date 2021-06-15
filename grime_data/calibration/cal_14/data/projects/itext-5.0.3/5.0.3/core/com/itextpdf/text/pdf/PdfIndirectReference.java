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

/**
 * <CODE>PdfIndirectReference</CODE> contains a reference to a <CODE>PdfIndirectObject</CODE>.
 * <P>
 * Any object used as an element of an array or as a value in a dictionary may be specified
 * by either a direct object of an indirect reference. An <I>indirect reference</I> is a reference
 * to an indirect object, and consists of the indirect object's object number, generation number
 * and the <B>R</B> keyword.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.11 (page 54).
 *
 * @see		PdfObject
 * @see		PdfIndirectObject
 */

public class PdfIndirectReference extends PdfObject {
    
    // membervariables
    
/** the object number */
    protected int number;
    
/** the generation number */
    protected int generation = 0;
    
    // constructors
    
    protected PdfIndirectReference() {
        super(0);
    }
    
/**
 * Constructs a <CODE>PdfIndirectReference</CODE>.
 *
 * @param		type			the type of the <CODE>PdfObject</CODE> that is referenced to
 * @param		number			the object number.
 * @param		generation		the generation number.
 */
    
    PdfIndirectReference(int type, int number, int generation) {
        super(0, new StringBuffer().append(number).append(" ").append(generation).append(" R").toString());
        this.number = number;
        this.generation = generation;
    }
    
/**
 * Constructs a <CODE>PdfIndirectReference</CODE>.
 *
 * @param		type			the type of the <CODE>PdfObject</CODE> that is referenced to
 * @param		number			the object number.
 */
    
    PdfIndirectReference(int type, int number) {
        this(type, number, 0);
    }
    
    // methods
    
/**
 * Returns the number of the object.
 *
 * @return		a number.
 */
    
    public int getNumber() {
        return number;
    }
    
/**
 * Returns the generation of the object.
 *
 * @return		a number.
 */
    
    public int getGeneration() {
        return generation;
    }
    
    public String toString() {
    	return new StringBuffer().append(number).append(" ").append(generation).append(" R").toString();
    }
}