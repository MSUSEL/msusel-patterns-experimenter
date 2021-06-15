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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Creates a number tree.
 * @author Paulo Soares
 */
public class PdfNumberTree {

    private static final int leafSize = 64;

    /**
     * Creates a number tree.
     * @param items the item of the number tree. The key is an <CODE>Integer</CODE>
     * and the value is a <CODE>PdfObject</CODE>.
     * @param writer the writer
     * @throws IOException on error
     * @return the dictionary with the number tree.
     */
    public static <O extends PdfObject> PdfDictionary writeTree(HashMap<Integer, O> items, PdfWriter writer) throws IOException {
        if (items.isEmpty())
            return null;
        Integer numbers[] = new Integer[items.size()];
        numbers = items.keySet().toArray(numbers);
        Arrays.sort(numbers);
        if (numbers.length <= leafSize) {
            PdfDictionary dic = new PdfDictionary();
            PdfArray ar = new PdfArray();
            for (int k = 0; k < numbers.length; ++k) {
                ar.add(new PdfNumber(numbers[k].intValue()));
                ar.add(items.get(numbers[k]));
            }
            dic.put(PdfName.NUMS, ar);
            return dic;
        }
        int skip = leafSize;
        PdfIndirectReference kids[] = new PdfIndirectReference[(numbers.length + leafSize - 1) / leafSize];
        for (int k = 0; k < kids.length; ++k) {
            int offset = k * leafSize;
            int end = Math.min(offset + leafSize, numbers.length);
            PdfDictionary dic = new PdfDictionary();
            PdfArray arr = new PdfArray();
            arr.add(new PdfNumber(numbers[offset].intValue()));
            arr.add(new PdfNumber(numbers[end - 1].intValue()));
            dic.put(PdfName.LIMITS, arr);
            arr = new PdfArray();
            for (; offset < end; ++offset) {
                arr.add(new PdfNumber(numbers[offset].intValue()));
                arr.add(items.get(numbers[offset]));
            }
            dic.put(PdfName.NUMS, arr);
            kids[k] = writer.addToBody(dic).getIndirectReference();
        }
        int top = kids.length;
        while (true) {
            if (top <= leafSize) {
                PdfArray arr = new PdfArray();
                for (int k = 0; k < top; ++k)
                    arr.add(kids[k]);
                PdfDictionary dic = new PdfDictionary();
                dic.put(PdfName.KIDS, arr);
                return dic;
            }
            skip *= leafSize;
            int tt = (numbers.length + skip - 1 )/ skip;
            for (int k = 0; k < tt; ++k) {
                int offset = k * leafSize;
                int end = Math.min(offset + leafSize, top);
                PdfDictionary dic = new PdfDictionary();
                PdfArray arr = new PdfArray();
                arr.add(new PdfNumber(numbers[k * skip].intValue()));
                arr.add(new PdfNumber(numbers[Math.min((k + 1) * skip, numbers.length) - 1].intValue()));
                dic.put(PdfName.LIMITS, arr);
                arr = new PdfArray();
                for (; offset < end; ++offset) {
                    arr.add(kids[offset]);
                }
                dic.put(PdfName.KIDS, arr);
                kids[k] = writer.addToBody(dic).getIndirectReference();
            }
            top = tt;
        }
    }

    private static void iterateItems(PdfDictionary dic, HashMap<Integer, PdfObject> items) {
        PdfArray nn = (PdfArray)PdfReader.getPdfObjectRelease(dic.get(PdfName.NUMS));
        if (nn != null) {
            for (int k = 0; k < nn.size(); ++k) {
                PdfNumber s = (PdfNumber)PdfReader.getPdfObjectRelease(nn.getPdfObject(k++));
                items.put(new Integer(s.intValue()), nn.getPdfObject(k));
            }
        }
        else if ((nn = (PdfArray)PdfReader.getPdfObjectRelease(dic.get(PdfName.KIDS))) != null) {
            for (int k = 0; k < nn.size(); ++k) {
                PdfDictionary kid = (PdfDictionary)PdfReader.getPdfObjectRelease(nn.getPdfObject(k));
                iterateItems(kid, items);
            }
        }
    }

    public static HashMap<Integer, PdfObject> readTree(PdfDictionary dic) {
        HashMap<Integer, PdfObject> items = new HashMap<Integer, PdfObject>();
        if (dic != null)
            iterateItems(dic, items);
        return items;
    }
}