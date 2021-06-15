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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

import com.itextpdf.rups.controller.PdfReaderController;
import com.itextpdf.rups.model.ObjectLoader;
import com.itextpdf.rups.model.TreeNodeFactory;
import com.itextpdf.rups.view.PageSelectionListener;
import com.itextpdf.rups.view.itext.treenodes.PdfObjectTreeNode;
import com.itextpdf.rups.view.itext.treenodes.PdfPageTreeNode;
import com.itextpdf.rups.view.itext.treenodes.PdfPagesTreeNode;
import com.itextpdf.rups.view.itext.treenodes.PdfTrailerTreeNode;
import com.itextpdf.rups.view.models.JTableAutoModel;
import com.itextpdf.rups.view.models.JTableAutoModelInterface;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPageLabels;

/**
 * A JTable listing all the pages in a PDF file: the object number of each
 * page dictionary and the page numbers (with label information if present).
 */
public class PagesTable extends JTable implements JTableAutoModelInterface, Observer {

	/** A list with page nodes. */
	protected ArrayList<PdfPageTreeNode> list = new ArrayList<PdfPageTreeNode>();
	/** Nodes in the FormTree correspond with nodes in the main PdfTree. */
	protected PdfReaderController controller;
	/***/
	protected PageSelectionListener listener;

	/**
	 * Constructs a PagesTable.
	 * @param	listener	the page navigation listener.
	 */
	public PagesTable(PdfReaderController controller, PageSelectionListener listener) {
		this.controller = controller;
		this.listener = listener;
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
    @SuppressWarnings("unchecked")
    public void update(Observable observable, Object obj) {
		if (obj == null) {
			list = new ArrayList<PdfPageTreeNode>();
			repaint();
			return;
		}
		if (obj instanceof ObjectLoader) {
			ObjectLoader loader = (ObjectLoader)obj;
			String[] pagelabels = PdfPageLabels.getPageLabels(loader.getReader());
			int i = 0;
			TreeNodeFactory factory = loader.getNodes();
			PdfTrailerTreeNode trailer = controller.getPdfTree().getRoot();
			PdfObjectTreeNode catalog = factory.getChildNode(trailer, PdfName.ROOT);
			PdfPagesTreeNode pages = (PdfPagesTreeNode)factory.getChildNode(catalog, PdfName.PAGES);
			if (pages == null) {
				return;
			}
			Enumeration<PdfObjectTreeNode> p = pages.depthFirstEnumeration();
			PdfObjectTreeNode  child;
			StringBuffer buf;
			while (p.hasMoreElements()) {
				child = p.nextElement();
				if (child instanceof PdfPageTreeNode) {
					buf = new StringBuffer("Page ");
					buf.append(++i);
					if (pagelabels != null) {
						buf.append(" ( ");
						buf.append(pagelabels[i - 1]);
						buf.append(" )");
					}
					child.setUserObject(buf.toString());
					list.add((PdfPageTreeNode)child);
				}
			}
		}
		setModel(new JTableAutoModel(this));
	}

	/**
	 * @see javax.swing.JTable#getColumnCount()
	 */
	@Override
    public int getColumnCount() {
		return 2;
	}

	/**
	 * @see javax.swing.JTable#getRowCount()
	 */
	@Override
    public int getRowCount() {
		return list.size();
	}

    /**
     * @see javax.swing.JTable#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	if (getRowCount() == 0) return null;
		switch (columnIndex) {
		case 0:
			return "Object " + list.get(rowIndex).getNumber();
		case 1:
			return list.get(rowIndex);
		}
		return null;
	}
	/**
	 * @see javax.swing.JTable#getColumnName(int)
	 */
	@Override
    public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Object";
		case 1:
			return "Page";
		default:
			return null;
		}
	}

	/**
	 * @see javax.swing.JTable#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent evt) {
		if (evt != null)
			super.valueChanged(evt);
		if (controller == null)
			return;
		if (getRowCount() > 0) {
			controller.selectNode(list.get(getSelectedRow()));
			if (listener != null)
				listener.gotoPage(getSelectedRow() + 1);
		}
	}

	/** A serial version UID. */
	private static final long serialVersionUID = -6523261089453886508L;

}