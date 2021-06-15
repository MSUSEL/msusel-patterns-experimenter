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
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class captures an AcroForm on input. Basically, it extends Dictionary
 * by indexing the fields of an AcroForm
 * @author Mark Thompson
 */

public class PRAcroForm extends PdfDictionary {

    /**
     * This class holds the information for a single field
     */
    public static class FieldInformation {
        String name;
        PdfDictionary info;
        PRIndirectReference ref;

        FieldInformation(String name, PdfDictionary info, PRIndirectReference ref) {
            this.name = name; this.info = info; this.ref = ref;
        }
        public String getName() { return name; }
        public PdfDictionary getInfo() { return info; }
        public PRIndirectReference getRef() { return ref; }
    };
    ArrayList<FieldInformation> fields;
    ArrayList<PdfDictionary> stack;
    HashMap<String, FieldInformation> fieldByName;
    PdfReader reader;

    /**
     * Constructor
     * @param reader reader of the input file
     */
    public PRAcroForm(PdfReader reader) {
        this.reader = reader;
        fields = new ArrayList<FieldInformation>();
        fieldByName = new HashMap<String, FieldInformation>();
        stack = new ArrayList<PdfDictionary>();
    }
    /**
     * Number of fields found
     * @return size
     */
    @Override
    public int size() {
        return fields.size();
    }

    public ArrayList<FieldInformation> getFields() {
        return fields;
    }

    public FieldInformation getField(String name) {
        return fieldByName.get(name);
    }

    /**
     * Given the title (/T) of a reference, return the associated reference
     * @param name a string containing the path
     * @return a reference to the field, or null
     */
    public PRIndirectReference getRefByName(String name) {
        FieldInformation fi = fieldByName.get(name);
        if (fi == null) return null;
        return fi.getRef();
    }
    /**
     * Read, and comprehend the acroform
     * @param root the document root
     */
    public void readAcroForm(PdfDictionary root) {
        if (root == null)
            return;
        hashMap = root.hashMap;
        pushAttrib(root);
        PdfArray fieldlist = (PdfArray)PdfReader.getPdfObjectRelease(root.get(PdfName.FIELDS));
        iterateFields(fieldlist, null, null);
    }

    /**
     * After reading, we index all of the fields. Recursive.
     * @param fieldlist An array of fields
     * @param fieldDict the last field dictionary we encountered (recursively)
     * @param title the pathname of the field, up to this point or null
     */
    protected void iterateFields(PdfArray fieldlist, PRIndirectReference fieldDict, String title) {
        for (Iterator<PdfObject> it = fieldlist.listIterator(); it.hasNext();) {
            PRIndirectReference ref = (PRIndirectReference)it.next();
            PdfDictionary dict = (PdfDictionary) PdfReader.getPdfObjectRelease(ref);

            // if we are not a field dictionary, pass our parent's values
            PRIndirectReference myFieldDict = fieldDict;
            String myTitle = title;
            PdfString tField = (PdfString)dict.get(PdfName.T);
            boolean isFieldDict = tField != null;

            if (isFieldDict) {
                myFieldDict = ref;
                if (title == null) myTitle = tField.toString();
                else myTitle = title + '.' + tField.toString();
            }

            PdfArray kids = (PdfArray)dict.get(PdfName.KIDS);
            if (kids != null) {
                pushAttrib(dict);
                iterateFields(kids, myFieldDict, myTitle);
                stack.remove(stack.size() - 1);   // pop
            }
            else {          // leaf node
                if (myFieldDict != null) {
                    PdfDictionary mergedDict = stack.get(stack.size() - 1);
                    if (isFieldDict)
                        mergedDict = mergeAttrib(mergedDict, dict);

                    mergedDict.put(PdfName.T, new PdfString(myTitle));
                    FieldInformation fi = new FieldInformation(myTitle, mergedDict, myFieldDict);
                    fields.add(fi);
                    fieldByName.put(myTitle, fi);
                }
            }
        }
    }
    /**
     * merge field attributes from two dictionaries
     * @param parent one dictionary
     * @param child the other dictionary
     * @return a merged dictionary
     */
    protected PdfDictionary mergeAttrib(PdfDictionary parent, PdfDictionary child) {
        PdfDictionary targ = new PdfDictionary();
        if (parent != null) targ.putAll(parent);

        for (Object element : child.getKeys()) {
            PdfName key = (PdfName) element;
            if (key.equals(PdfName.DR) || key.equals(PdfName.DA) ||
            key.equals(PdfName.Q)  || key.equals(PdfName.FF) ||
            key.equals(PdfName.DV) || key.equals(PdfName.V)
            || key.equals(PdfName.FT)
            || key.equals(PdfName.F)) {
                targ.put(key,child.get(key));
            }
        }
        return targ;
    }
    /**
     * stack a level of dictionary. Merge in a dictionary from this level
     */
    protected void pushAttrib(PdfDictionary dict) {
        PdfDictionary dic = null;
        if (!stack.isEmpty()) {
            dic = stack.get(stack.size() - 1);
        }
        dic = mergeAttrib(dic, dict);
        stack.add(dic);
    }
}
