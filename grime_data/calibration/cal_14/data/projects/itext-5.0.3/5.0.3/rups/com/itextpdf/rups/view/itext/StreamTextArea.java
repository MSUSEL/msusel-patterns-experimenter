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
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.itextpdf.rups.io.TextAreaOutputStream;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

public class StreamTextArea extends JScrollPane implements Observer {
	
	/** The text area with the content stream. */
	protected JTextArea text;
	
	/**
	 * Constructs a StreamTextArea.
	 */
	public StreamTextArea() {
		super();
		text = new JTextArea();
		setViewportView(text);
	}
	
	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object obj) {
		text.setText(null);
	}
	
	/**
	 * Renders the content stream of a PdfObject or empties the text area.
	 * @param object	the object of which the content stream needs to be rendered
	 */
	public void render(PdfObject object) {
		if (object instanceof PRStream) {
			PRStream stream = (PRStream)object;
			try {
				TextAreaOutputStream taos = new TextAreaOutputStream(text);
				taos.write(PdfReader.getStreamBytes(stream));
				//text.addMouseListener(new StreamEditorAction(stream));
			}
			catch(IOException e) {
				text.setText("The stream could not be read: " + e.getMessage());
			}
		}
		else {
			update(null, null);
			return;
		}
		text.repaint();
		repaint();
	}

	/** a serial version id. */
	private static final long serialVersionUID = 1302283071087762494L;

}
