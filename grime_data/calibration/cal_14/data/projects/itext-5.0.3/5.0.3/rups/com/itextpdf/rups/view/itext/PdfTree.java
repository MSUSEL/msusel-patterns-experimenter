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
package com.itextpdf.rups.view.itext;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.itextpdf.rups.view.icons.IconTreeCellRenderer;
import com.itextpdf.rups.view.itext.treenodes.PdfObjectTreeNode;
import com.itextpdf.rups.view.itext.treenodes.PdfTrailerTreeNode;

/**
 * A JTree that shows the object hierarchy of a PDF document.
 */
public class PdfTree extends JTree implements Observer {

	/** The root of the PDF tree. */
	protected PdfTrailerTreeNode root;
	
	/**
	 * Constructs a PDF tree.
	 */
	public PdfTree() {
		super();
		root = new PdfTrailerTreeNode();
		setCellRenderer(new IconTreeCellRenderer());
		update(null, null);
	}
	
	/**
	 * Getter for the root node
	 * @return	the PDF Trailer node
	 */
	public PdfTrailerTreeNode getRoot() {
		return root;
	}

	/**
	 * Updates the PdfTree when a file is closed or when a ObjectLoader
	 * has finished loading objects.
	 * @param observable	the Observable class that started the update
	 * @param obj			the object that has all the updates
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object obj) {
		if (obj == null) {
			root = new PdfTrailerTreeNode();
		}
		setModel(new DefaultTreeModel(root));
		repaint();
	}

	/**
	 * Select a specific node in the tree.
	 * Typically this method will be called from a different tree,
	 * such as the pages, outlines or form tree.
	 * @param	node	the node that has to be selected
	 */
	public void selectNode(PdfObjectTreeNode node) {
		TreePath path = new TreePath(node.getPath());
		setSelectionPath(path);
		scrollPathToVisible(path);
	}

	/** a serial version UID */
	private static final long serialVersionUID = 7545804447512085734L;
	
}