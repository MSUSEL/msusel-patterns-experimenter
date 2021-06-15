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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The structure tree root corresponds to the highest hierarchy level in a tagged PDF.
 * @author Paulo Soares
 */
public class PdfStructureTreeRoot extends PdfDictionary {

    private HashMap<Integer, PdfObject> parentTree = new HashMap<Integer, PdfObject>();
    private PdfIndirectReference reference;

    /**
     * Holds value of property writer.
     */
    private PdfWriter writer;

    /** Creates a new instance of PdfStructureTreeRoot */
    PdfStructureTreeRoot(PdfWriter writer) {
        super(PdfName.STRUCTTREEROOT);
        this.writer = writer;
        reference = writer.getPdfIndirectReference();
    }

    /**
     * Maps the user tags to the standard tags. The mapping will allow a standard application to make some sense of the tagged
     * document whatever the user tags may be.
     * @param used the user tag
     * @param standard the standard tag
     */
    public void mapRole(PdfName used, PdfName standard) {
        PdfDictionary rm = (PdfDictionary)get(PdfName.ROLEMAP);
        if (rm == null) {
            rm = new PdfDictionary();
            put(PdfName.ROLEMAP, rm);
        }
        rm.put(used, standard);
    }

    /**
     * Gets the writer.
     * @return the writer
     */
    public PdfWriter getWriter() {
        return this.writer;
    }

    /**
     * Gets the reference this object will be written to.
     * @return the reference this object will be written to
     * @since	2.1.6 method removed in 2.1.5, but restored in 2.1.6
     */
    public PdfIndirectReference getReference() {
        return this.reference;
    }

    void setPageMark(int page, PdfIndirectReference struc) {
        Integer i = new Integer(page);
        PdfArray ar = (PdfArray)parentTree.get(i);
        if (ar == null) {
            ar = new PdfArray();
            parentTree.put(i, ar);
        }
        ar.add(struc);
    }

    private void nodeProcess(PdfDictionary struc, PdfIndirectReference reference) throws IOException {
        PdfObject obj = struc.get(PdfName.K);
        if (obj != null && obj.isArray() && !(((PdfArray)obj).getArrayList().get(0)).isNumber()) {
            PdfArray ar = (PdfArray)obj;
            ArrayList<PdfObject> a = ar.getArrayList();
            for (int k = 0; k < a.size(); ++k) {
                PdfStructureElement e = (PdfStructureElement)a.get(k);
                a.set(k, e.getReference());
                nodeProcess(e, e.getReference());
            }
        }
        if (reference != null)
            writer.addToBody(struc, reference);
    }

    void buildTree() throws IOException {
        HashMap<Integer, PdfIndirectReference> numTree = new HashMap<Integer, PdfIndirectReference>();
        for (Integer i: parentTree.keySet()) {
            PdfArray ar = (PdfArray)parentTree.get(i);
            numTree.put(i, writer.addToBody(ar).getIndirectReference());
        }
        PdfDictionary dicTree = PdfNumberTree.writeTree(numTree, writer);
        if (dicTree != null)
            put(PdfName.PARENTTREE, writer.addToBody(dicTree).getIndirectReference());

        nodeProcess(this, reference);
    }
}
