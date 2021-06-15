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
package com.itextpdf.rups.view.itext.treenodes;

import com.itextpdf.rups.view.icons.IconTreeNode;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

/**
 * A FormTreeNode is a standard node in a FormTree.
 */
public class FormTreeNode extends IconTreeNode {
	
	/** The corresponding tree node in the PdfTree. */
	protected PdfObjectTreeNode object_node;
	
	/**
	 * Creates the root node of the FormTree.
	 */
	public FormTreeNode() {
		super("form.png", "Form");
	}
	
	/**
	 * Creates a node corresponding with a node in the PdfTree.
	 * @param	node	a corresponding node
	 */
	public FormTreeNode(PdfObjectTreeNode node) {
		super("form.png");
		this.object_node = node;
		if (node.isDictionary()) {
			PdfDictionary dict = (PdfDictionary)node.getPdfObject();
			PdfObject fieldname = dict.get(PdfName.T);
			if (fieldname != null) {
				this.setUserObject(fieldname);
			}
			else {
				this.setUserObject("unnamed field");
			}
		}
	}

    /**
     * Gets the node in the PdfTree that corresponds with this
     * FormTreeNode.
     * @return	a PdfObjectTreeNode in the PdfTree
     */
	public PdfObjectTreeNode getCorrespondingPdfObjectNode() {
		return object_node;
	}

	/** A serial version UID. */
	private static final long serialVersionUID = 7800080437550790989L;
}