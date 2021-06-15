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

import java.util.Observable;

import com.itextpdf.text.pdf.PdfReader;

/**
 * Loads the necessary iText PDF objects in Background.
 */
public class ObjectLoader extends BackgroundTask {
	/** This is the object that will forward the updates to the observers. */
	protected Observable observable;
	/** iText's PdfReader object. */
	protected PdfReader reader;
	/** The factory that can provide PDF objects. */
	protected IndirectObjectFactory objects;
	/** The factory that can provide tree nodes. */
	protected TreeNodeFactory nodes;
	/** a human readable name for this loaded */
	private String loaderName;
	
	/**
	 * Creates a new ObjectLoader.
	 * @param	observable	the object that will forward the changes.
	 * @param	reader		the PdfReader from which the objects will be read.
	 */
	public ObjectLoader(Observable observable, PdfReader reader, String loaderName) {
		this.observable = observable;
		this.reader = reader;
		this.loaderName = loaderName;
		start();
	}
	
	/**
	 * Getter for the PdfReader object.
	 * @return	a reader object
	 */
	public PdfReader getReader() {
		return reader;
	}

	/**
	 * Getter for the object factory.
	 * @return	an indirect object factory
	 */
	public IndirectObjectFactory getObjects() {
		return objects;
	}

	/**
	 * Getter for the tree node factory.
	 * @return	a tree node factory
	 */
	public TreeNodeFactory getNodes() {
		return nodes;
	}
	
	/**
	 * getter for a human readable name representing this loader
	 * @return the human readable name
	 * @since 5.0.3
	 */
	public String getLoaderName(){
	    return loaderName;
	}
	
	/**
	 * @see com.itextpdf.rups.model.BackgroundTask#doTask()
	 */
	@Override
	public void doTask() {
		ProgressDialog progress = new ProgressDialog(null, "Reading PDF file");
		objects = new IndirectObjectFactory(reader);
		int n = objects.getXRefMaximum();
		progress.setMessage("Reading the Cross-Reference table");
		progress.setTotal(n);
		while (objects.storeNextObject()) {
			progress.setValue(objects.getCurrent());
		}
		progress.setTotal(0);
		nodes = new TreeNodeFactory(objects);
		progress.setMessage("Updating GUI");
		observable.notifyObservers(this);
		progress.dispose();
	}
}