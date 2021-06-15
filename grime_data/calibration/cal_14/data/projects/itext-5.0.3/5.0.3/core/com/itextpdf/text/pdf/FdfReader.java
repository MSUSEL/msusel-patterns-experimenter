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
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
/** Reads an FDF form and makes the fields available
 * @author Paulo Soares
 */
public class FdfReader extends PdfReader {

    HashMap<String, PdfDictionary> fields;
    String fileSpec;
    PdfName encoding;

    /** Reads an FDF form.
     * @param filename the file name of the form
     * @throws IOException on error
     */
    public FdfReader(String filename) throws IOException {
        super(filename);
    }

    /** Reads an FDF form.
     * @param pdfIn the byte array with the form
     * @throws IOException on error
     */
    public FdfReader(byte pdfIn[]) throws IOException {
        super(pdfIn);
    }

    /** Reads an FDF form.
     * @param url the URL of the document
     * @throws IOException on error
     */
    public FdfReader(URL url) throws IOException {
        super(url);
    }

    /** Reads an FDF form.
     * @param is the <CODE>InputStream</CODE> containing the document. The stream is read to the
     * end but is not closed
     * @throws IOException on error
     */
    public FdfReader(InputStream is) throws IOException {
        super(is);
    }

    @Override
    protected void readPdf() throws IOException {
        fields = new HashMap<String, PdfDictionary>();
        try {
            tokens.checkFdfHeader();
            rebuildXref();
            readDocObj();
        }
        finally {
            try {
                tokens.close();
            }
            catch (Exception e) {
                // empty on purpose
            }
        }
        readFields();
    }

    protected void kidNode(PdfDictionary merged, String name) {
        PdfArray kids = merged.getAsArray(PdfName.KIDS);
        if (kids == null || kids.isEmpty()) {
            if (name.length() > 0)
                name = name.substring(1);
            fields.put(name, merged);
        }
        else {
            merged.remove(PdfName.KIDS);
            for (int k = 0; k < kids.size(); ++k) {
                PdfDictionary dic = new PdfDictionary();
                dic.merge(merged);
                PdfDictionary newDic = kids.getAsDict(k);
                PdfString t = newDic.getAsString(PdfName.T);
                String newName = name;
                if (t != null)
                    newName += "." + t.toUnicodeString();
                dic.merge(newDic);
                dic.remove(PdfName.T);
                kidNode(dic, newName);
            }
        }
    }

    protected void readFields() {
        catalog = trailer.getAsDict(PdfName.ROOT);
        PdfDictionary fdf = catalog.getAsDict(PdfName.FDF);
        if (fdf == null)
            return;
        PdfString fs = fdf.getAsString(PdfName.F);
        if (fs != null)
            fileSpec = fs.toUnicodeString();
        PdfArray fld = fdf.getAsArray(PdfName.FIELDS);
        if (fld == null)
            return;
        encoding = fdf.getAsName(PdfName.ENCODING);
        PdfDictionary merged = new PdfDictionary();
        merged.put(PdfName.KIDS, fld);
        kidNode(merged, "");
    }

    /** Gets all the fields. The map is keyed by the fully qualified
     * field name and the value is a merged <CODE>PdfDictionary</CODE>
     * with the field content.
     * @return all the fields
     */
    public HashMap<String, PdfDictionary> getFields() {
        return fields;
    }

    /** Gets the field dictionary.
     * @param name the fully qualified field name
     * @return the field dictionary
     */
    public PdfDictionary getField(String name) {
        return fields.get(name);
    }

    /**
     * Gets a byte[] containing a file that is embedded in the FDF.
     * @param name the fully qualified field name
     * @return the bytes of the file
     * @throws IOException
     * @since 5.0.1
     */
    public byte[] getAttachedFile(String name) throws IOException {
    	PdfDictionary field = fields.get(name);
    	if (field != null) {
    		PdfIndirectReference ir = (PRIndirectReference)field.get(PdfName.V);
    		PdfDictionary filespec = (PdfDictionary)getPdfObject(ir.getNumber());
    		PdfDictionary ef = filespec.getAsDict(PdfName.EF);
    		ir = (PRIndirectReference)ef.get(PdfName.F);
    		PRStream stream = (PRStream)getPdfObject(ir.getNumber());
    		return getStreamBytes(stream);
    	}
		return new byte[0];
    }

    /**
     * Gets the field value or <CODE>null</CODE> if the field does not
     * exist or has no value defined.
     * @param name the fully qualified field name
     * @return the field value or <CODE>null</CODE>
     */
    public String getFieldValue(String name) {
        PdfDictionary field = fields.get(name);
        if (field == null)
            return null;
        PdfObject v = getPdfObject(field.get(PdfName.V));
        if (v == null)
            return null;
        if (v.isName())
            return PdfName.decodeName(((PdfName)v).toString());
        else if (v.isString()) {
            PdfString vs = (PdfString)v;
            if (encoding == null || vs.getEncoding() != null)
                return vs.toUnicodeString();
            byte b[] = vs.getBytes();
            if (b.length >= 2 && b[0] == (byte)254 && b[1] == (byte)255)
                return vs.toUnicodeString();
            try {
                if (encoding.equals(PdfName.SHIFT_JIS))
                    return new String(b, "SJIS");
                else if (encoding.equals(PdfName.UHC))
                    return new String(b, "MS949");
                else if (encoding.equals(PdfName.GBK))
                    return new String(b, "GBK");
                else if (encoding.equals(PdfName.BIGFIVE))
                    return new String(b, "Big5");
            }
            catch (Exception e) {
            }
            return vs.toUnicodeString();
        }
        return null;
    }

    /** Gets the PDF file specification contained in the FDF.
     * @return the PDF file specification contained in the FDF
     */
    public String getFileSpec() {
        return fileSpec;
    }
}