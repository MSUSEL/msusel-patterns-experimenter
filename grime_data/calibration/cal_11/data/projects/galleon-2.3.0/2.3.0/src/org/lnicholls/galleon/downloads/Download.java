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
package org.lnicholls.galleon.downloads;

/*
 * Luis Javier Beltran
 * luisjavier2@users.sourceforge.net

 * http://sourceforge.net/projects/gdownloader
 * 
 * DownloadThreadManager.java
 *
 * Created on 15 de abril de 2003, 09:42 PM
 */

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class Download extends Thread implements DownloadWork, Serializable {

	private static Logger log = Logger.getLogger(Download.class.getName());

	int id = -1, p = 0;

	int threads = 1;

	URL u = null;

	File f = null;

	Download d = this;

	long size = 0, completed = 0;

	long startTime = 0, endTime = 0;

	int status = StatusEvent.STOPPED;

	/** Creates a new instance of DownloadThreadManager */
	public Download(URL u, File f) throws Exception {
		this.u = u;
		this.f = f;

		URLConnection uc = u.openConnection();
		uc.connect();
		size = uc.getContentLength();
		if (size<=0)
			throw new IllegalArgumentException();
	}

	public int getID() {
		return id;
	}

	public int getDownloadPriority() {
		return p;
	}

	public int getElapsedTime() {
		if (status < ThreadStatus.COMPLETED.getID())
			endTime = System.currentTimeMillis();

		return (int) (endTime - startTime) / 1000;
	}

	public URL getURL() {
		return u;
	}

	public File getLocalFile() {
		return f;
	}

	public long getSize() {
		return size;
	}

	public long getBytesCompleted() {
		return completed;
	}

	public void setBytesCompleted(long value) {
		completed = value;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setDownloadPriority(int p) {
		this.p = p;
	}

	public void setThreadNumber(int threads) {

		if (threads > 0) {
			this.threads = threads;
		}
	}

	public int getThreadNumber() {
		return threads;
	}

	public int getStatus() {
		return status;
	}

	public void setStartTime(long value) {
		startTime = value;
	}

	public void setStatus(int value) {
		status = value;
	}
}