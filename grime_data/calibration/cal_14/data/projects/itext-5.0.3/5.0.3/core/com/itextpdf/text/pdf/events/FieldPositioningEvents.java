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
package com.itextpdf.text.pdf.events;

import java.io.IOException;
import java.util.HashMap;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfRectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

/**
 * Class that can be used to position AcroForm fields.
 */
public class FieldPositioningEvents extends PdfPageEventHelper implements PdfPCellEvent {

    /**
     * Keeps a map with fields that are to be positioned in inGenericTag.
     */
    protected HashMap<String, PdfFormField> genericChunkFields = new HashMap<String, PdfFormField>();

    /**
     * Keeps the form field that is to be positioned in a cellLayout event.
     */
    protected PdfFormField cellField = null;

    /**
     * The PdfWriter to use when a field has to added in a cell event.
     */
    protected PdfWriter fieldWriter = null;
    /**
     * The PdfFormField that is the parent of the field added in a cell event.
     */
    protected PdfFormField parent = null;

    /** Creates a new event. This constructor will be used if you need to position fields with Chunk objects. */
    public FieldPositioningEvents() {}

    /** Some extra padding that will be taken into account when defining the widget. */
    public float padding;

    /**
     * Add a PdfFormField that has to be tied to a generic Chunk.
     */
    public void addField(String text, PdfFormField field) {
    	genericChunkFields.put(text, field);
    }

    /** Creates a new event. This constructor will be used if you need to position fields with a Cell Event. */
    public FieldPositioningEvents(PdfWriter writer, PdfFormField field) {
    	this.cellField = field;
    	this.fieldWriter = writer;
    }

    /** Creates a new event. This constructor will be used if you need to position fields with a Cell Event. */
    public FieldPositioningEvents(PdfFormField parent, PdfFormField field) {
    	this.cellField = field;
    	this.parent = parent;
    }

    /** Creates a new event. This constructor will be used if you need to position fields with a Cell Event.
     * @throws DocumentException
     * @throws IOException*/
    public FieldPositioningEvents(PdfWriter writer, String text) throws IOException, DocumentException {
    	this.fieldWriter = writer;
    	TextField tf = new TextField(writer, new Rectangle(0, 0), text);
		tf.setFontSize(14);
		cellField = tf.getTextField();
	}

    /** Creates a new event. This constructor will be used if you need to position fields with a Cell Event.
     * @throws DocumentException
     * @throws IOException*/
    public FieldPositioningEvents(PdfWriter writer, PdfFormField parent, String text) throws IOException, DocumentException {
    	this.parent = parent;
    	TextField tf = new TextField(writer, new Rectangle(0, 0), text);
		tf.setFontSize(14);
		cellField = tf.getTextField();
	}

	/**
	 * @param padding The padding to set.
	 */
	public void setPadding(float padding) {
		this.padding = padding;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(PdfFormField parent) {
		this.parent = parent;
	}
	/**
	 * @see com.itextpdf.text.pdf.PdfPageEvent#onGenericTag(com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, com.itextpdf.text.Rectangle, java.lang.String)
	 */
	@Override
    public void onGenericTag(PdfWriter writer, Document document,
			Rectangle rect, String text) {
		rect.setBottom(rect.getBottom() - 3);
		PdfFormField field = genericChunkFields.get(text);
		if (field == null) {
			TextField tf = new TextField(writer, new Rectangle(rect.getLeft(padding), rect.getBottom(padding), rect.getRight(padding), rect.getTop(padding)), text);
			tf.setFontSize(14);
			try {
				field = tf.getTextField();
			} catch (Exception e) {
				throw new ExceptionConverter(e);
			}
		}
		else {
			field.put(PdfName.RECT,  new PdfRectangle(rect.getLeft(padding), rect.getBottom(padding), rect.getRight(padding), rect.getTop(padding)));
		}
		if (parent == null)
			writer.addAnnotation(field);
		else
			parent.addKid(field);
	}

	/**
	 * @see com.itextpdf.text.pdf.PdfPCellEvent#cellLayout(com.itextpdf.text.pdf.PdfPCell, com.itextpdf.text.Rectangle, com.itextpdf.text.pdf.PdfContentByte[])
	 */
	public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvases) {
		if (cellField == null || fieldWriter == null && parent == null) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.have.used.the.wrong.constructor.for.this.fieldpositioningevents.class"));
		cellField.put(PdfName.RECT, new PdfRectangle(rect.getLeft(padding), rect.getBottom(padding), rect.getRight(padding), rect.getTop(padding)));
		if (parent == null)
			fieldWriter.addAnnotation(cellField);
		else
			parent.addKid(cellField);
	}
}
