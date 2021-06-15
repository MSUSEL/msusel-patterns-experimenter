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
import java.io.PrintStream;
import java.util.Iterator;
/**
 * List a PDF file in human-readable form (for debugging reasons mostly)
 * @author Mark Thompson
 */

public class PdfLister {

	/** the printStream you want to write the output to. */
    PrintStream out;

    /**
     * Create a new lister object.
     * @param out
     */
    public PdfLister(PrintStream out) {
        this.out = out;
    }

    /**
     * Visualizes a PDF object.
     * @param object	a com.itextpdf.text.pdf object
     */
    public void listAnyObject(PdfObject object)
    {
        switch (object.type()) {
        case PdfObject.ARRAY:
            listArray((PdfArray)object);
            break;
        case PdfObject.DICTIONARY:
            listDict((PdfDictionary) object);
            break;
        case PdfObject.STRING:
            out.println("(" + object.toString() + ")");
            break;
        default:
            out.println(object.toString());
            break;
        }
    }
    /**
     * Visualizes a PdfDictionary object.
     * @param dictionary	a com.itextpdf.text.pdf.PdfDictionary object
     */
    public void listDict(PdfDictionary dictionary)
    {
        out.println("<<");
        PdfObject value;
        for (PdfName key: dictionary.getKeys()) {
            value = dictionary.get(key);
            out.print(key.toString());
            out.print(' ');
            listAnyObject(value);
        }
        out.println(">>");
    }

    /**
     * Visualizes a PdfArray object.
     * @param array	a com.itextpdf.text.pdf.PdfArray object
     */
    public void listArray(PdfArray array)
    {
        out.println('[');
        for (Iterator<PdfObject> i = array.listIterator(); i.hasNext(); ) {
            PdfObject item = i.next();
            listAnyObject(item);
        }
        out.println(']');
    }
    /**
     * Visualizes a Stream.
     * @param stream
     * @param reader
     */
    public void listStream(PRStream stream, PdfReaderInstance reader)
    {
        try {
            listDict(stream);
            out.println("startstream");
            byte[] b = PdfReader.getStreamBytes(stream);
//                  byte buf[] = new byte[Math.min(stream.getLength(), 4096)];
//                  int r = 0;
//                  stream.openStream(reader);
//                  for (;;) {
//                      r = stream.readStream(buf, 0, buf.length);
//                      if (r == 0) break;
//                      out.write(buf, 0, r);
//                  }
//                  stream.closeStream();
            int len = b.length - 1;
            for (int k = 0; k < len; ++k) {
                if (b[k] == '\r' && b[k + 1] != '\n')
                    b[k] = (byte)'\n';
            }
            out.println(new String(b));
            out.println("endstream");
        } catch (IOException e) {
            System.err.println("I/O exception: " + e);
//          } catch (java.util.zip.DataFormatException e) {
//              System.err.println("Data Format Exception: " + e);
        }
    }
    /**
     * Visualizes an imported page
     * @param iPage
     */
    public void listPage(PdfImportedPage iPage)
    {
        int pageNum = iPage.getPageNumber();
        PdfReaderInstance readerInst = iPage.getPdfReaderInstance();
        PdfReader reader = readerInst.getReader();

        PdfDictionary page = reader.getPageN(pageNum);
        listDict(page);
        PdfObject obj = PdfReader.getPdfObject(page.get(PdfName.CONTENTS));
        if (obj == null)
            return;
        switch (obj.type) {
        case PdfObject.STREAM:
            listStream((PRStream)obj, readerInst);
            break;
        case PdfObject.ARRAY:
            for (Iterator<PdfObject> i = ((PdfArray)obj).listIterator(); i.hasNext();) {
                PdfObject o = PdfReader.getPdfObject(i.next());
                listStream((PRStream)o, readerInst);
                out.println("-----------");
            }
            break;
        }
    }
}
