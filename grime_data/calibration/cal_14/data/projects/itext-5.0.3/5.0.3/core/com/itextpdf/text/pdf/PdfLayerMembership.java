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

import java.util.Collection;
import java.util.HashSet;

/**
 * Content typically belongs to a single optional content group,
 * and is visible when the group is <B>ON</B> and invisible when it is <B>OFF</B>. To express more
 * complex visibility policies, content should not declare itself to belong to an optional
 * content group directly, but rather to an optional content membership dictionary
 * represented by this class.
 *
 * @author Paulo Soares
 */
public class PdfLayerMembership extends PdfDictionary implements PdfOCG {

    /**
     * Visible only if all of the entries are <B>ON</B>.
     */
    public static final PdfName ALLON = new PdfName("AllOn");
    /**
     * Visible if any of the entries are <B>ON</B>.
     */
    public static final PdfName ANYON = new PdfName("AnyOn");
    /**
     * Visible if any of the entries are <B>OFF</B>.
     */
    public static final PdfName ANYOFF = new PdfName("AnyOff");
    /**
     * Visible only if all of the entries are <B>OFF</B>.
     */
    public static final PdfName ALLOFF = new PdfName("AllOff");

    PdfIndirectReference ref;
    PdfArray members = new PdfArray();
    HashSet<PdfLayer> layers = new HashSet<PdfLayer>();

    /**
     * Creates a new, empty, membership layer.
     * @param writer the writer
     */
    public PdfLayerMembership(PdfWriter writer) {
        super(PdfName.OCMD);
        put(PdfName.OCGS, members);
        ref = writer.getPdfIndirectReference();
    }

    /**
     * Gets the <CODE>PdfIndirectReference</CODE> that represents this membership layer.
     * @return the <CODE>PdfIndirectReference</CODE> that represents this layer
     */
    public PdfIndirectReference getRef() {
        return ref;
    }

    /**
     * Adds a new member to the layer.
     * @param layer the new member to the layer
     */
    public void addMember(PdfLayer layer) {
        if (!layers.contains(layer)) {
            members.add(layer.getRef());
            layers.add(layer);
        }
    }

    /**
     * Gets the member layers.
     * @return the member layers
     */
    public Collection<PdfLayer> getLayers() {
        return layers;
    }

    /**
     * Sets the visibility policy for content belonging to this
     * membership dictionary. Possible values are ALLON, ANYON, ANYOFF and ALLOFF.
     * The default value is ANYON.
     * @param type the visibility policy
     */
    public void setVisibilityPolicy(PdfName type) {
        put(PdfName.P, type);
    }

    /**
     * Sets the visibility expression for content belonging to this
     * membership dictionary.
     * @param ve A (nested) array of which the first value is /And, /Or, or /Not
     * followed by a series of indirect references to OCGs or other visibility
     * expressions.
     * @since 5.0.2
     */
    public void setVisibilityExpression(PdfVisibilityExpression ve) {
        put(PdfName.VE, ve);
    }

    /**
     * Gets the dictionary representing the membership layer. It just returns <CODE>this</CODE>.
     * @return the dictionary representing the layer
     */
    public PdfObject getPdfObject() {
        return this;
    }
}
