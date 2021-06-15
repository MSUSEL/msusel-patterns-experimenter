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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import com.itextpdf.rups.controller.PdfReaderController;
import com.itextpdf.rups.model.ObjectLoader;
import com.itextpdf.rups.model.TreeNodeFactory;
import com.itextpdf.rups.view.icons.IconTreeCellRenderer;
import com.itextpdf.rups.view.itext.treenodes.OutlineTreeNode;
import com.itextpdf.rups.view.itext.treenodes.PdfObjectTreeNode;
import com.itextpdf.rups.view.itext.treenodes.PdfTrailerTreeNode;
import com.itextpdf.text.pdf.PdfName;

/**
 * A JTree visualizing information about the outlines (aka bookmarks) of
 * the PDF file (if any).
 */
public class OutlineTree extends JTree implements TreeSelectionListener, Observer {

	/** Nodes in the FormTree correspond with nodes in the main PdfTree. */
	protected PdfReaderController controller;
	
	/** Creates a new outline tree. */
	public OutlineTree(PdfReaderController controller) {
		super();
		this.controller = controller;
		setCellRenderer(new IconTreeCellRenderer());
		setModel(new DefaultTreeModel(new OutlineTreeNode()));
		addTreeSelectionListener(this);
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object obj) {
		if (obj == null) {
			setModel(new DefaultTreeModel(new OutlineTreeNode()));
			repaint();
			return;
		}
		if (obj instanceof ObjectLoader) {
			ObjectLoader loader = (ObjectLoader)obj;
			TreeNodeFactory factory = loader.getNodes();
			PdfTrailerTreeNode trailer = controller.getPdfTree().getRoot();
			PdfObjectTreeNode catalog = factory.getChildNode(trailer, PdfName.ROOT);
			PdfObjectTreeNode outline = factory.getChildNode(catalog, PdfName.OUTLINES);
			if (outline == null) {
				return;
			}
			OutlineTreeNode root = new OutlineTreeNode();
			PdfObjectTreeNode first = factory.getChildNode(outline, PdfName.FIRST);
			if (first != null)
			    loadOutline(factory, root, first);
			setModel(new DefaultTreeModel(root));
		}
	}
	
	/**
	 * Method that can be used recursively to load the outline hierarchy into the tree.
	 */
	private void loadOutline(TreeNodeFactory factory, OutlineTreeNode parent, PdfObjectTreeNode child) {
		OutlineTreeNode childnode = new OutlineTreeNode(child);
		parent.add(childnode);
		PdfObjectTreeNode first = factory.getChildNode(child, PdfName.FIRST);
		if (first != null) {
			loadOutline(factory, childnode, first);
		}
		PdfObjectTreeNode next = factory.getChildNode(child, PdfName.NEXT);
		if (next != null) {
			loadOutline(factory, parent, next);
		}
	}

	/**
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent evt) {
		if (controller == null)
			return;
		OutlineTreeNode selectednode = (OutlineTreeNode)this.getLastSelectedPathComponent();
		PdfObjectTreeNode node = selectednode.getCorrespondingPdfObjectNode();
		if (node != null)
			controller.selectNode(node);
	}

	/** A serial version uid. */
	private static final long serialVersionUID = 5646572654823301007L;

}
