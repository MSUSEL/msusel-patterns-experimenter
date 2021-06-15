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
import java.awt.CardLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import com.itextpdf.rups.view.models.DictionaryTableModel;
import com.itextpdf.rups.view.models.PdfArrayTableModel;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfObject;

public class PdfObjectPanel extends JPanel implements Observer {

	/** Name of a panel in the CardLayout. */
	private static final String TEXT = "text";
	/** Name of a panel in the CardLayout. */
	private static final String TABLE = "table";
	
	/** The layout that will show the info about the PDF object that is being analyzed. */
	protected CardLayout layout = new CardLayout();

	/** Table with dictionary entries. */
	JTable table = new JTable();
	/** The text pane with the info about a PDF object in the bottom panel. */
	JTextArea text = new JTextArea();
	
	/** Creates a PDF object panel. */
	public PdfObjectPanel() {
		// layout
		setLayout(layout);

		// dictionary / array / stream
		JScrollPane dict_scrollpane = new JScrollPane();
		dict_scrollpane.setViewportView(table);
		add(dict_scrollpane, TABLE);
		
		// number / string / ...
		JScrollPane text_scrollpane = new JScrollPane();
		text_scrollpane.setViewportView(text);
		add(text_scrollpane, TEXT);
	}
	
	/**
	 * Clear the object panel.
	 */
	public void clear() {
		text.setText(null);
		layout.show(this, TEXT);
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object obj) {
		clear();
	}
	
	/**
	 * Shows a PdfObject as text or in a table.
	 * @param object	the object that needs to be shown.
	 */
	public void render(PdfObject object) {
		if (object == null) {
			text.setText(null);
			layout.show(this, TEXT);
			this.repaint();
			text.repaint();
			return;
		}
		switch(object.type()) {
		case PdfObject.DICTIONARY:
		case PdfObject.STREAM:
			table.setModel(new DictionaryTableModel((PdfDictionary)object));
			layout.show(this, TABLE);
			this.repaint();
			break;
		case PdfObject.ARRAY:
			table.setModel(new PdfArrayTableModel((PdfArray)object));
			layout.show(this, TABLE);
			this.repaint();
			break;
		default:
			text.setText(object.toString());
			layout.show(this, TEXT);
			break;
		}
	}
	
	/** a serial version id. */
	private static final long serialVersionUID = 1302283071087762494L;

}
