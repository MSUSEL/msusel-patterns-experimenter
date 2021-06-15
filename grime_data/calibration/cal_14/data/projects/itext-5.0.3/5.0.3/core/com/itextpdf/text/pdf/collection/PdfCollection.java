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

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;

public class PdfCollection extends PdfDictionary {

	/** A type of PDF Collection */
	public static final int DETAILS = 0;
	/** A type of PDF Collection */
	public static final int TILE = 1;
	/** A type of PDF Collection */
	public static final int HIDDEN = 2;
	/**
	 * A type of PDF Collection
	 * @since 5.0.2
	 */
	public static final int CUSTOM = 3;
	
	/**
	 * Constructs a PDF Collection.
	 * @param	type	the type of PDF collection.
	 */
	public PdfCollection(int type) {
		super(PdfName.COLLECTION);
		switch(type) {
		case TILE:
			put(PdfName.VIEW, PdfName.T);
			break;
		case HIDDEN:
			put(PdfName.VIEW, PdfName.H);
			break;
		case CUSTOM:
			put(PdfName.VIEW, PdfName.C);
			break;
		default:
			put(PdfName.VIEW, PdfName.D);
		}
	}
	
	/**
	 * Identifies the document that will be initially presented
	 * in the user interface.
	 * @param description	the description that was used when attaching the file to the document
	 */
	public void setInitialDocument(String description) {
		put(PdfName.D, new PdfString(description, null));
	}
	
	/**
	 * Sets the Collection schema dictionary.
	 * @param schema	an overview of the collection fields
	 */
	public void setSchema(PdfCollectionSchema schema) {
		put(PdfName.SCHEMA, schema);
	}
	
	/**
	 * Gets the Collection schema dictionary.
	 * @return schema	an overview of the collection fields
	 */
	public PdfCollectionSchema getSchema() {
		return (PdfCollectionSchema)get(PdfName.SCHEMA);
	}
	
	/**
	 * Sets the Collection sort dictionary.
	 * @param sort	a collection sort dictionary
	 */
	public void setSort(PdfCollectionSort sort) {
		put(PdfName.SORT, sort);
	}
}