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
package com.itextpdf.rups.model;

import java.util.ArrayList;

import com.itextpdf.text.pdf.IntHashtable;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNull;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

/**
 * A factory that can produce all the indirect objects in a PDF file.
 */
public class IndirectObjectFactory {

	/** The reader object. */
	protected PdfReader reader;
	/** The current xref number. */
	protected int current;
	/** The highest xref number. */
	protected int n;
	/** A list of all the indirect objects in a PDF file. */
	protected ArrayList<PdfObject> objects = new ArrayList<PdfObject>();
	/** Mapping between the index in the objects list and the reference number in the xref table.  */
	protected IntHashtable idxToRef = new IntHashtable();
	/** Mapping between the reference number in the xref table and the index in the objects list .  */
	protected IntHashtable refToIdx = new IntHashtable();
	
	/**
	 * Creates a list that will contain all the indirect objects
	 * in a PDF document. 
	 * @param reader	the reader that will read the PDF document
	 */
	public IndirectObjectFactory(PdfReader reader) {
		this.reader = reader;
		current = -1;
		n = reader.getXrefSize();
	}

	/**
	 * Gets the last object that has been registered.
	 * This method only makes sense while loading the factory.
	 * with loadNextObject().
	 * @return	the number of the last object that was stored
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * Gets the highest possible object number in the XRef table.
	 * @return	an object number
	 */
	public int getXRefMaximum() {
		return n;
	}

	/**
	 * Stores the next object of the XRef table.
	 * As soon as this method returns false, it makes no longer
	 * sense calling it as all the objects have been stored.
	 * @return	false if there are no objects left to check.
	 */
	public boolean storeNextObject() {
		while (current < n) {
			current++;
			PdfObject object = reader.getPdfObjectRelease(current);
			if (object != null) {
				int idx = size();
				idxToRef.put(idx, current);
				refToIdx.put(current, idx);
				store(object);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If we store all the objects, we might run out of memory;
	 * that's why we'll only store the objects that are necessary
	 * to construct other objects (for instance the page table).
	 * @param	object	an object we might want to store 
	 */
	private void store(PdfObject object) {
		if (object.isDictionary()){
			PdfDictionary dict = (PdfDictionary)object;
			if (PdfName.PAGE.equals(dict.get(PdfName.TYPE))) {
				objects.add(dict);
				return;
			}
		}
		objects.add(PdfNull.PDFNULL);
	}
	
	/**
	 * Gets the total number of indirect objects in the PDF file.
	 * This isn't necessarily the same number as returned by getXRefMaximum().
	 * The PDF specification allows gaps between object numbers.
	 * @return the total number of indirect objects in the PDF.
	 */
	public int size() {
		return objects.size();
	}
	
	/**
	 * Gets the index of an object based on its number in the xref table.
	 * @param ref	a number in the xref table
	 * @return	the index in the list of indirect objects
	 */
	public int getIndexByRef(int ref) {
		return refToIdx.get(ref);
	}
	
	/**
	 * Gets the reference number in the xref table based on the index in the
	 * indirect object list.
	 * @param i		the index of an object in the indirect object list
	 * @return	the corresponding reference number in the xref table
	 */
	public int getRefByIndex(int i) {
		return idxToRef.get(i);
	}
	
	/**
	 * Gets an object based on its index in the indirect object list.
	 * @param i		an index in the indirect object list	
	 * @return	a PDF object
	 */
	public PdfObject getObjectByIndex(int i) {
		return getObjectByReference(getRefByIndex(i));
	}

	/**
	 * Gets an object based on its reference number in the xref table.
	 * @param ref	a number in the xref table
	 * @return	a PDF object
	 */
	public PdfObject getObjectByReference(int ref) {
		return objects.get(getIndexByRef(ref));
	}
	
	/**
	 * Loads an object based on its reference number in the xref table.
	 * @param ref	a reference number in the xref table.
	 * @return	a PDF object
	 */
	public PdfObject loadObjectByReference(int ref) {
		PdfObject object = getObjectByReference(ref);
		if (object instanceof PdfNull) {
			int idx = getIndexByRef(ref);
			object = reader.getPdfObject(ref);
			objects.set(idx, object);
		}
		return object;
	}
}
