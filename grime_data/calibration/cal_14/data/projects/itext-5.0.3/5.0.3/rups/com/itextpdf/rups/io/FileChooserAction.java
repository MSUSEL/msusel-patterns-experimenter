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
package com.itextpdf.rups.io;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Observable;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Allows you to browse the file system and forwards the file
 * to the object that is waiting for you to choose a file.
 */
public class FileChooserAction extends AbstractAction {
	
	/** An object that is expecting the result of the file chooser action. */
	protected Observable observable;
	/** A file filter to apply when browsing for a file. */
	protected FileFilter filter;
	/** Indicates if you're browsing to create a new or an existing file. */
	protected boolean newFile;
	/** The file that was chosen. */
	protected File file;
	
	private File lastSelectedFolder;
	
	/**
	 * Creates a new file chooser action.
	 * @param observable	the object waiting for you to select file
	 * @param caption	a description for the action
	 * @param filter	a filter to apply when browsing
	 * @param newFile	indicates if you should browse for a new or existing file
	 */
	public FileChooserAction(Observable observable, String caption, FileFilter filter, boolean newFile) {
		super(caption);
		this.observable = observable;
		this.filter = filter;
		this.newFile = newFile;
	}
	
	/**
	 * Getter for the file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(lastSelectedFolder);
		fc.setSelectedFile(file);
		
		if (filter != null) {
			fc.setFileFilter(filter);
		}
		int okCancel;
		if (newFile) {
			okCancel = fc.showSaveDialog(null);
		}
		else {
			okCancel = fc.showOpenDialog(null);
		}
		if (okCancel == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			lastSelectedFolder = fc.getCurrentDirectory();
			observable.notifyObservers(this);
		}
	}

	/** A serial version UID. */
	private static final long serialVersionUID = 2225830878098387118L;

}
