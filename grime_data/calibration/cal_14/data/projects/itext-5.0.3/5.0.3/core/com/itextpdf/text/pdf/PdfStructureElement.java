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

import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * This is a node in a document logical structure. It may contain a mark point or it may contain
 * other nodes.
 * @author Paulo Soares
 */
public class PdfStructureElement extends PdfDictionary {
    
    /**
     * Holds value of property kids.
     */
    private PdfStructureElement parent;
    private PdfStructureTreeRoot top;
    
    /**
     * Holds value of property reference.
     */
    private PdfIndirectReference reference;
    
    /**
     * Creates a new instance of PdfStructureElement.
     * @param parent the parent of this node
     * @param structureType the type of structure. It may be a standard type or a user type mapped by the role map
     */
    public PdfStructureElement(PdfStructureElement parent, PdfName structureType) {
        top = parent.top;
        init(parent, structureType);
        this.parent = parent;
        put(PdfName.P, parent.reference);
    }
    
    /**
     * Creates a new instance of PdfStructureElement.
     * @param parent the parent of this node
     * @param structureType the type of structure. It may be a standard type or a user type mapped by the role map
     */    
    public PdfStructureElement(PdfStructureTreeRoot parent, PdfName structureType) {
        top = parent;
        init(parent, structureType);
        put(PdfName.P, parent.getReference());
    }
    
    private void init(PdfDictionary parent, PdfName structureType) {
        PdfObject kido = parent.get(PdfName.K);
        PdfArray kids = null;
        if (kido != null && !kido.isArray())
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.parent.has.already.another.function"));
        if (kido == null) {
            kids = new PdfArray();
            parent.put(PdfName.K, kids);
        }
        else
            kids = (PdfArray)kido;
        kids.add(this);
        put(PdfName.S, structureType);
        reference = top.getWriter().getPdfIndirectReference();
    }
    
    /**
     * Gets the parent of this node.
     * @return the parent of this node
     */    
    public PdfDictionary getParent() {
        return parent;
    }
    
    void setPageMark(int page, int mark) {
        if (mark >= 0)
            put(PdfName.K, new PdfNumber(mark));
        top.setPageMark(page, reference);
    }
    
    /**
     * Gets the reference this object will be written to.
     * @return the reference this object will be written to
     * @since	2.1.6 method removed in 2.1.5, but restored in 2.1.6
     */    
    public PdfIndirectReference getReference() {
        return this.reference;
    }
}
