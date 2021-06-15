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

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.error_messages.MessageLocalization;

public class PdfCollectionSort extends PdfDictionary {
	
	/**
	 * Constructs a PDF Collection Sort Dictionary.
	 * @param key	the key of the field that will be used to sort entries
	 */
	public PdfCollectionSort(String key) {
		super(PdfName.COLLECTIONSORT);
		put(PdfName.S, new PdfName(key));
	}
	
	/**
	 * Constructs a PDF Collection Sort Dictionary.
	 * @param keys	the keys of the fields that will be used to sort entries
	 */
	public PdfCollectionSort(String[] keys) {
		super(PdfName.COLLECTIONSORT);
		PdfArray array = new PdfArray();
		for (int i = 0; i < keys.length; i++) {
			array.add(new PdfName(keys[i]));
		}
		put(PdfName.S, array);
	}
	
	/**
	 * Defines the sort order of the field (ascending or descending).
	 * @param ascending	true is the default, use false for descending order
	 */
	public void setSortOrder(boolean ascending) {
		PdfObject o = get(PdfName.S);
		if (o instanceof PdfName) {
			put(PdfName.A, new PdfBoolean(ascending));
		}
		else {
			throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.have.to.define.a.boolean.array.for.this.collection.sort.dictionary"));
		}
	}
	
	/**
	 * Defines the sort order of the field (ascending or descending).
	 * @param ascending	an array with every element corresponding with a name of a field.
	 */
	public void setSortOrder(boolean[] ascending) {
		PdfObject o = get(PdfName.S);
		if (o instanceof PdfArray) {
			if (((PdfArray)o).size() != ascending.length) {
				throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.number.of.booleans.in.this.array.doesn.t.correspond.with.the.number.of.fields"));
			}
			PdfArray array = new PdfArray();
			for (int i = 0; i < ascending.length; i++) {
				array.add(new PdfBoolean(ascending[i]));
			}
			put(PdfName.A, array);
		}
		else {
			throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.need.a.single.boolean.for.this.collection.sort.dictionary"));
		}
	}
	
	
}
