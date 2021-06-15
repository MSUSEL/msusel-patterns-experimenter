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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.pdf.AcroFields.Item;

/** Writes an FDF form.
 * @author Paulo Soares
 */
public class FdfWriter {
    private static final byte[] HEADER_FDF = DocWriter.getISOBytes("%FDF-1.4\n%\u00e2\u00e3\u00cf\u00d3\n");
    HashMap<String, Object> fields = new HashMap<String, Object>();

    /** The PDF file associated with the FDF. */
    private String file;

    /** Creates a new FdfWriter. */
    public FdfWriter() {
    }

    /** Writes the content to a stream.
     * @param os the stream
     * @throws IOException on error
     */
    public void writeTo(OutputStream os) throws IOException {
        Wrt wrt = new Wrt(os, this);
        wrt.writeTo();
    }

    @SuppressWarnings("unchecked")
    boolean setField(String field, PdfObject value) {
        HashMap<String, Object> map = fields;
        StringTokenizer tk = new StringTokenizer(field, ".");
        if (!tk.hasMoreTokens())
            return false;
        while (true) {
            String s = tk.nextToken();
            Object obj = map.get(s);
            if (tk.hasMoreTokens()) {
                if (obj == null) {
                    obj = new HashMap<String, Object>();
                    map.put(s, obj);
                    map = (HashMap<String, Object>)obj;
                    continue;
                }
                else if (obj instanceof HashMap)
                    map = (HashMap<String, Object>)obj;
                else
                    return false;
            }
            else {
                if (!(obj instanceof HashMap)) {
                    map.put(s, value);
                    return true;
                }
                else
                    return false;
            }
        }
    }

    @SuppressWarnings("unchecked")
    void iterateFields(HashMap<String, Object> values, HashMap<String, Object> map, String name) {
        for (Map.Entry<String, Object> entry: map.entrySet()) {
            String s = entry.getKey();
            Object obj = entry.getValue();
            if (obj instanceof HashMap)
                iterateFields(values, (HashMap<String, Object>)obj, name + "." + s);
            else
                values.put((name + "." + s).substring(1), obj);
        }
    }

    /** Removes the field value.
     * @param field the field name
     * @return <CODE>true</CODE> if the field was found and removed,
     * <CODE>false</CODE> otherwise
     */
    @SuppressWarnings("unchecked")
    public boolean removeField(String field) {
        HashMap<String, Object> map = fields;
        StringTokenizer tk = new StringTokenizer(field, ".");
        if (!tk.hasMoreTokens())
            return false;
        ArrayList<Object> hist = new ArrayList<Object>();
        while (true) {
            String s = tk.nextToken();
            Object obj = map.get(s);
            if (obj == null)
                return false;
            hist.add(map);
            hist.add(s);
            if (tk.hasMoreTokens()) {
                if (obj instanceof HashMap)
                    map = (HashMap<String, Object>)obj;
                else
                    return false;
            }
            else {
                if (obj instanceof HashMap)
                    return false;
                else
                    break;
            }
        }
        for (int k = hist.size() - 2; k >= 0; k -= 2) {
            map = (HashMap<String, Object>)hist.get(k);
            String s = (String)hist.get(k + 1);
            map.remove(s);
            if (!map.isEmpty())
                break;
        }
        return true;
    }

    /** Gets all the fields. The map is keyed by the fully qualified
     * field name and the values are <CODE>PdfObject</CODE>.
     * @return a map with all the fields
     */
    public HashMap<String, Object> getFields() {
        HashMap<String, Object> values = new HashMap<String, Object>();
        iterateFields(values, fields, "");
        return values;
    }

    /** Gets the field value.
     * @param field the field name
     * @return the field value or <CODE>null</CODE> if not found
     */
    @SuppressWarnings("unchecked")
    public String getField(String field) {
        HashMap<String, Object> map = fields;
        StringTokenizer tk = new StringTokenizer(field, ".");
        if (!tk.hasMoreTokens())
            return null;
        while (true) {
            String s = tk.nextToken();
            Object obj = map.get(s);
            if (obj == null)
                return null;
            if (tk.hasMoreTokens()) {
                if (obj instanceof HashMap)
                    map = (HashMap<String, Object>)obj;
                else
                    return null;
            }
            else {
                if (obj instanceof HashMap)
                    return null;
                else {
                    if (((PdfObject)obj).isString())
                        return ((PdfString)obj).toUnicodeString();
                    else
                        return PdfName.decodeName(obj.toString());
                }
            }
        }
    }

    /** Sets the field value as a name.
     * @param field the fully qualified field name
     * @param value the value
     * @return <CODE>true</CODE> if the value was inserted,
     * <CODE>false</CODE> if the name is incompatible with
     * an existing field
     */
    public boolean setFieldAsName(String field, String value) {
        return setField(field, new PdfName(value));
    }

    /** Sets the field value as a string.
     * @param field the fully qualified field name
     * @param value the value
     * @return <CODE>true</CODE> if the value was inserted,
     * <CODE>false</CODE> if the name is incompatible with
     * an existing field
     */
    public boolean setFieldAsString(String field, String value) {
        return setField(field, new PdfString(value, PdfObject.TEXT_UNICODE));
    }

    /**
     * Sets the field value as a <CODE>PDFAction</CODE>.
     * For example, this method allows setting a form submit button action using {@link PdfAction#createSubmitForm(String, Object[], int)}.
     * This method creates an <CODE>A</CODE> entry for the specified field in the underlying FDF file.
     * Method contributed by Philippe Laflamme (plaflamme)
     * @param field the fully qualified field name
     * @param action the field's action
     * @return <CODE>true</CODE> if the value was inserted,
     * <CODE>false</CODE> if the name is incompatible with
     * an existing field
     * @since	2.1.5
     */
    public boolean setFieldAsAction(String field, PdfAction action) {
    	return setField(field, action);
    }

    /** Sets all the fields from this <CODE>FdfReader</CODE>
     * @param fdf the <CODE>FdfReader</CODE>
     */
    public void setFields(FdfReader fdf) {
        HashMap<String, PdfDictionary> map = fdf.getFields();
        for (Map.Entry<String, PdfDictionary> entry: map.entrySet()) {
            String key = entry.getKey();
            PdfDictionary dic = entry.getValue();
            PdfObject v = dic.get(PdfName.V);
            if (v != null) {
                setField(key, v);
            }
            v = dic.get(PdfName.A); // (plaflamme)
            if (v != null) {
            	setField(key, v);
            }
        }
    }

    /** Sets all the fields from this <CODE>PdfReader</CODE>
     * @param pdf the <CODE>PdfReader</CODE>
     */
    public void setFields(PdfReader pdf) {
        setFields(pdf.getAcroFields());
    }

    /** Sets all the fields from this <CODE>AcroFields</CODE>
     * @param af the <CODE>AcroFields</CODE>
     */
    public void setFields(AcroFields af) {
        for (Map.Entry<String, Item> entry: af.getFields().entrySet()) {
            String fn = entry.getKey();
            AcroFields.Item item = entry.getValue();
            PdfDictionary dic = item.getMerged(0);
            PdfObject v = PdfReader.getPdfObjectRelease(dic.get(PdfName.V));
            if (v == null)
                continue;
            PdfObject ft = PdfReader.getPdfObjectRelease(dic.get(PdfName.FT));
            if (ft == null || PdfName.SIG.equals(ft))
                continue;
            setField(fn, v);
        }
    }

    /** Gets the PDF file name associated with the FDF.
     * @return the PDF file name associated with the FDF
     */
    public String getFile() {
        return this.file;
    }

    /** Sets the PDF file name associated with the FDF.
     * @param file the PDF file name associated with the FDF
     *
     */
    public void setFile(String file) {
        this.file = file;
    }

    static class Wrt extends PdfWriter {
        private FdfWriter fdf;

        Wrt(OutputStream os, FdfWriter fdf) throws IOException {
            super(new PdfDocument(), os);
            this.fdf = fdf;
            this.os.write(HEADER_FDF);
            body = new PdfBody(this);
        }

        void writeTo() throws IOException {
            PdfDictionary dic = new PdfDictionary();
            dic.put(PdfName.FIELDS, calculate(fdf.fields));
            if (fdf.file != null)
                dic.put(PdfName.F, new PdfString(fdf.file, PdfObject.TEXT_UNICODE));
            PdfDictionary fd = new PdfDictionary();
            fd.put(PdfName.FDF, dic);
            PdfIndirectReference ref = addToBody(fd).getIndirectReference();
            os.write(getISOBytes("trailer\n"));
            PdfDictionary trailer = new PdfDictionary();
            trailer.put(PdfName.ROOT, ref);
            trailer.toPdf(null, os);
            os.write(getISOBytes("\n%%EOF\n"));
            os.close();
        }


        @SuppressWarnings("unchecked")
        PdfArray calculate(HashMap<String, Object> map) throws IOException {
            PdfArray ar = new PdfArray();
            for (Map.Entry<String, Object> entry: map.entrySet()) {
                String key = entry.getKey();
                Object v = entry.getValue();
                PdfDictionary dic = new PdfDictionary();
                dic.put(PdfName.T, new PdfString(key, PdfObject.TEXT_UNICODE));
                if (v instanceof HashMap) {
                    dic.put(PdfName.KIDS, calculate((HashMap<String, Object>)v));
                }
                else if(v instanceof PdfAction) {	// (plaflamme)
                   	dic.put(PdfName.A, (PdfAction)v);
                }
                else {
                    dic.put(PdfName.V, (PdfObject)v);
                }
                ar.add(dic);
            }
            return ar;
        }
    }
}
