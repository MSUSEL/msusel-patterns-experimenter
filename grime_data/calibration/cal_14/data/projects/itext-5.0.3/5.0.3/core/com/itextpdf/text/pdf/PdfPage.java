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
/**
 * <CODE>PdfPage</CODE> is the PDF Page-object.
 * <P>
 * A Page object is a dictionary whose keys describe a single page containing text,
 * graphics, and images. A Page object is a leaf of the Pages tree.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 6.4 (page 73-81)
 *
 * @see		PdfPages
 */

public class PdfPage extends PdfDictionary {

    private static final String boxStrings[] = {"crop", "trim", "art", "bleed"};
    private static final PdfName boxNames[] = {PdfName.CROPBOX, PdfName.TRIMBOX, PdfName.ARTBOX, PdfName.BLEEDBOX};
    // membervariables

/** value of the <B>Rotate</B> key for a page in PORTRAIT */
    public static final PdfNumber PORTRAIT = new PdfNumber(0);

/** value of the <B>Rotate</B> key for a page in LANDSCAPE */
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);

/** value of the <B>Rotate</B> key for a page in INVERTEDPORTRAIT */
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);

/**	value of the <B>Rotate</B> key for a page in SEASCAPE */
    public static final PdfNumber SEASCAPE = new PdfNumber(270);

/** value of the <B>MediaBox</B> key */
    PdfRectangle mediaBox;

    // constructors

/**
 * Constructs a <CODE>PdfPage</CODE>.
 *
 * @param		mediaBox		a value for the <B>MediaBox</B> key
 * @param		resources		an indirect reference to a <CODE>PdfResources</CODE>-object
 * @param		rotate			a value for the <B>Rotate</B> key
 */

//    PdfPage(PdfRectangle mediaBox, Rectangle cropBox, PdfIndirectReference resources, PdfNumber rotate) {
//        super(PAGE);
//        this.mediaBox = mediaBox;
//        put(PdfName.MEDIABOX, mediaBox);
//        put(PdfName.RESOURCES, resources);
//        if (rotate != null) {
//            put(PdfName.ROTATE, rotate);
//        }
//        if (cropBox != null)
//            put(PdfName.CROPBOX, new PdfRectangle(cropBox));
//    }

/**
 * Constructs a <CODE>PdfPage</CODE>.
 *
 * @param		mediaBox		a value for the <B>MediaBox</B> key
 * @param		resources		an indirect reference to a <CODE>PdfResources</CODE>-object
 * @param		rotate			a value for the <B>Rotate</B> key
 */

    PdfPage(PdfRectangle mediaBox, HashMap<String, PdfRectangle> boxSize, PdfDictionary resources, int rotate) {
        super(PAGE);
        this.mediaBox = mediaBox;
        put(PdfName.MEDIABOX, mediaBox);
        put(PdfName.RESOURCES, resources);
        if (rotate != 0) {
            put(PdfName.ROTATE, new PdfNumber(rotate));
        }
        for (int k = 0; k < boxStrings.length; ++k) {
            PdfObject rect = boxSize.get(boxStrings[k]);
            if (rect != null)
                put(boxNames[k], rect);
        }
    }

/**
 * Constructs a <CODE>PdfPage</CODE>.
 *
 * @param		mediaBox		a value for the <B>MediaBox</B> key
 * @param		resources		an indirect reference to a <CODE>PdfResources</CODE>-object
 */

//    PdfPage(PdfRectangle mediaBox, Rectangle cropBox, PdfIndirectReference resources) {
//        this(mediaBox, cropBox, resources, null);
//    }

/**
 * Constructs a <CODE>PdfPage</CODE>.
 *
 * @param		mediaBox		a value for the <B>MediaBox</B> key
 * @param		resources		an indirect reference to a <CODE>PdfResources</CODE>-object
 */

    PdfPage(PdfRectangle mediaBox, HashMap<String, PdfRectangle> boxSize, PdfDictionary resources) {
        this(mediaBox, boxSize, resources, 0);
    }

/**
 * Checks if this page element is a tree of pages.
 * <P>
 * This method always returns <CODE>false</CODE>.
 *
 * @return	<CODE>false</CODE> because this is a single page
 */

    public boolean isParent() {
        return false;
    }

    // methods

/**
 * Adds an indirect reference pointing to a <CODE>PdfContents</CODE>-object.
 *
 * @param		contents		an indirect reference to a <CODE>PdfContents</CODE>-object
 */

    void add(PdfIndirectReference contents) {
        put(PdfName.CONTENTS, contents);
    }

/**
 * Rotates the mediabox, but not the text in it.
 *
 * @return		a <CODE>PdfRectangle</CODE>
 */

    PdfRectangle rotateMediaBox() {
        this.mediaBox =  mediaBox.rotate();
        put(PdfName.MEDIABOX, this.mediaBox);
        return this.mediaBox;
    }

/**
 * Returns the MediaBox of this Page.
 *
 * @return		a <CODE>PdfRectangle</CODE>
 */

    PdfRectangle getMediaBox() {
        return mediaBox;
    }
}