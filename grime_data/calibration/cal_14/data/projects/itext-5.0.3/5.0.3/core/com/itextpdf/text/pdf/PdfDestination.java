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

import java.util.StringTokenizer;

/**
 * A <CODE>PdfColor</CODE> defines a Color (it's a <CODE>PdfArray</CODE> containing 3 values).
 *
 * @see		PdfDictionary
 */

public class PdfDestination extends PdfArray {
    
    // public static final member-variables
    
/** This is a possible destination type */
    public static final int XYZ = 0;
    
/** This is a possible destination type */
    public static final int FIT = 1;
    
/** This is a possible destination type */
    public static final int FITH = 2;
    
/** This is a possible destination type */
    public static final int FITV = 3;
    
/** This is a possible destination type */
    public static final int FITR = 4;
    
/** This is a possible destination type */
    public static final int FITB = 5;
    
/** This is a possible destination type */
    public static final int FITBH = 6;
    
/** This is a possible destination type */
    public static final int FITBV = 7;
    
    // member variables
    
/** Is the indirect reference to a page already added? */
    private boolean status = false;
    
    // constructors
    
/**
 * Constructs a new <CODE>PdfDestination</CODE>.
 * <P>
 * If <VAR>type</VAR> equals <VAR>FITB</VAR>, the bounding box of a page
 * will fit the window of the Reader. Otherwise the type will be set to
 * <VAR>FIT</VAR> so that the entire page will fit to the window.
 *
 * @param		type		The destination type
 */
    
    public PdfDestination(int type) {
        super();
        if (type == FITB) {
            add(PdfName.FITB);
        }
        else {
            add(PdfName.FIT);
        }
    }
    
/**
 * Constructs a new <CODE>PdfDestination</CODE>.
 * <P>
 * If <VAR>type</VAR> equals <VAR>FITBH</VAR> / <VAR>FITBV</VAR>,
 * the width / height of the bounding box of a page will fit the window
 * of the Reader. The parameter will specify the y / x coordinate of the
 * top / left edge of the window. If the <VAR>type</VAR> equals <VAR>FITH</VAR>
 * or <VAR>FITV</VAR> the width / height of the entire page will fit
 * the window and the parameter will specify the y / x coordinate of the
 * top / left edge. In all other cases the type will be set to <VAR>FITH</VAR>.
 *
 * @param		type		the destination type
 * @param		parameter	a parameter to combined with the destination type
 */
    
    public PdfDestination(int type, float parameter) {
        super(new PdfNumber(parameter));
        switch(type) {
            default:
                addFirst(PdfName.FITH);
                break;
            case FITV:
                addFirst(PdfName.FITV);
                break;
            case FITBH:
                addFirst(PdfName.FITBH);
                break;
            case FITBV:
                addFirst(PdfName.FITBV);
        }
    }
    
/** Constructs a new <CODE>PdfDestination</CODE>.
 * <P>
 * Display the page, with the coordinates (left, top) positioned
 * at the top-left corner of the window and the contents of the page magnified
 * by the factor zoom. A negative value for any of the parameters left or top, or a
 * zoom value of 0 specifies that the current value of that parameter is to be retained unchanged.
 * @param type must be a <VAR>PdfDestination.XYZ</VAR>
 * @param left the left value. Negative to place a null
 * @param top the top value. Negative to place a null
 * @param zoom The zoom factor. A value of 0 keeps the current value
 */
    
    public PdfDestination(int type, float left, float top, float zoom) {
        super(PdfName.XYZ);
        if (left < 0)
            add(PdfNull.PDFNULL);
        else
            add(new PdfNumber(left));
        if (top < 0)
            add(PdfNull.PDFNULL);
        else
            add(new PdfNumber(top));
        add(new PdfNumber(zoom));
    }
    
/** Constructs a new <CODE>PdfDestination</CODE>.
 * <P>
 * Display the page, with its contents magnified just enough
 * to fit the rectangle specified by the coordinates left, bottom, right, and top
 * entirely within the window both horizontally and vertically. If the required
 * horizontal and vertical magnification factors are different, use the smaller of
 * the two, centering the rectangle within the window in the other dimension.
 *
 * @param type must be PdfDestination.FITR
 * @param left a parameter
 * @param bottom a parameter
 * @param right a parameter
 * @param top a parameter
 * @since iText0.38
 */
    
    public PdfDestination(int type, float left, float bottom, float right, float top) {
        super(PdfName.FITR);
        add(new PdfNumber(left));
        add(new PdfNumber(bottom));
        add(new PdfNumber(right));
        add(new PdfNumber(top));
    }
    
    /**
     * Creates a PdfDestination based on a String.
     * Valid Strings are for instance the values returned by SimpleNamedDestination:
     * "Fit", "XYZ 36 806 0",...
     * @param	dest	a String notation of a destination.
     * @since	iText 5.0
     */
    public PdfDestination(String dest) {
    	super();
    	StringTokenizer tokens = new StringTokenizer(dest);
    	if (tokens.hasMoreTokens()) {
    		add(new PdfName(tokens.nextToken()));
    	}
    	while (tokens.hasMoreTokens()) {
    		add(new PdfNumber(tokens.nextToken()));
    	}
    }
    
    // methods

/**
 * Checks if an indirect reference to a page has been added.
 *
 * @return	<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    public boolean hasPage() {
        return status;
    }
    
/** Adds the indirect reference of the destination page.
 *
 * @param page	an indirect reference
 * @return true if the page reference was added
 */
    
    public boolean addPage(PdfIndirectReference page) {
        if (!status) {
            addFirst(page);
            status = true;
            return true;
        }
        return false;
    }
}