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

import java.util.ArrayList;

/**
 * <CODE>PdfTextArray</CODE> defines an array with displacements and <CODE>PdfString</CODE>-objects.
 * <P>
 * A <CODE>TextArray</CODE> is used with the operator <VAR>TJ</VAR> in <CODE>PdfText</CODE>.
 * The first object in this array has to be a <CODE>PdfString</CODE>;
 * see reference manual version 1.3 section 8.7.5, pages 346-347.
 *       OR
 * see reference manual version 1.6 section 5.3.2, pages 378-379.
 */

public class PdfTextArray{
    ArrayList<Object> arrayList = new ArrayList<Object>();

    // To emit a more efficient array, we consolidate
    // repeated numbers or strings into single array entries.
    // "add( 50 ); add( -50 );" will REMOVE the combined zero from the array.
    // the alternative (leaving a zero in there) was Just Weird.
    // --Mark Storer, May 12, 2008
    private String lastStr;
    private Float lastNum;

    // constructors
    public PdfTextArray(String str) {
        add(str);
    }

    public PdfTextArray() {
    }

    /**
     * Adds a <CODE>PdfNumber</CODE> to the <CODE>PdfArray</CODE>.
     *
     * @param  number   displacement of the string
     */
    public void add(PdfNumber number) {
        add((float) number.doubleValue());
    }

    public void add(float number) {
        if (number != 0) {
            if (lastNum != null) {
                lastNum = new Float(number + lastNum.floatValue());
                if (lastNum.floatValue() != 0) {
                    replaceLast(lastNum);
                } else {
                    arrayList.remove(arrayList.size() - 1);
                }
            } else {
                lastNum = new Float(number);
                arrayList.add(lastNum);
            }

            lastStr = null;
        }
        // adding zero doesn't modify the TextArray at all
    }

    public void add(String str) {
        if (str.length() > 0) {
            if (lastStr != null) {
                lastStr = lastStr + str;
                replaceLast(lastStr);
            } else {
                lastStr = str;
                arrayList.add(lastStr);
            }
            lastNum = null;
        }
        // adding an empty string doesn't modify the TextArray at all
    }

    ArrayList<Object> getArrayList() {
        return arrayList;
    }

    private void replaceLast(Object obj) {
        // deliberately throw the IndexOutOfBoundsException if we screw up.
        arrayList.set(arrayList.size() - 1, obj);
    }
}
