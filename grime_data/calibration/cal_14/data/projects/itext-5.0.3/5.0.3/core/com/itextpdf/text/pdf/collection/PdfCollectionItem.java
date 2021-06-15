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
package com.itextpdf.text.pdf.collection;

import java.util.Calendar;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

public class PdfCollectionItem extends PdfDictionary {
	
	/** The PdfCollectionSchema with the names and types of the items. */
	PdfCollectionSchema schema;
	
	/**
	 * Constructs a Collection Item that can be added to a PdfFileSpecification.
	 */
	public PdfCollectionItem(PdfCollectionSchema schema) {
		super(PdfName.COLLECTIONITEM);
		this.schema = schema;
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(String key, String value) {
		PdfName fieldname = new PdfName(key);
		PdfCollectionField field = (PdfCollectionField)schema.get(fieldname);
		put(fieldname, field.getValue(value));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(String key, PdfString value) {
		PdfName fieldname = new PdfName(key);
		PdfCollectionField field = (PdfCollectionField)schema.get(fieldname);
		if (field.fieldType == PdfCollectionField.TEXT) {
			put(fieldname, value);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param d
	 */
	public void addItem(String key, PdfDate d) {
		PdfName fieldname = new PdfName(key);
		PdfCollectionField field = (PdfCollectionField)schema.get(fieldname);
		if (field.fieldType == PdfCollectionField.DATE) {
			put(fieldname, d);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param n
	 */
	public void addItem(String key, PdfNumber n) {
		PdfName fieldname = new PdfName(key);
		PdfCollectionField field = (PdfCollectionField)schema.get(fieldname);
		if (field.fieldType == PdfCollectionField.NUMBER) {
			put(fieldname, n);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param c
	 */
	public void addItem(String key, Calendar c) {
		addItem(key, new PdfDate(c));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param i
	 */
	public void addItem(String key, int i) {
		addItem(key, new PdfNumber(i));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param f
	 */
	public void addItem(String key, float f) {
		addItem(key, new PdfNumber(f));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param d
	 */
	public void addItem(String key, double d) {
		addItem(key, new PdfNumber(d));
	}
	
	/**
	 * Adds a prefix for the Collection item.
	 * You can only use this method after you have set the value of the item.
	 * @param prefix	a prefix
	 */
	public void setPrefix(String key, String prefix) {
		PdfName fieldname = new PdfName(key);
		PdfObject o = get(fieldname);
		if (o == null)
			throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.must.set.a.value.before.adding.a.prefix"));
		PdfDictionary dict = new PdfDictionary(PdfName.COLLECTIONSUBITEM);
		dict.put(PdfName.D, o);
		dict.put(PdfName.P, new PdfString(prefix, PdfObject.TEXT_UNICODE));
		put(fieldname, dict);
	}
}
