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
 * ThreadStatus.java
 *
 * Created on 22 de abril de 2003, 11:24 AM
 */

public class ThreadStatus {

	private static final String[] descs = { "Error", "Stopped", "Connecting", "", "", "Downloading", "", "Paused",
			"Resuming", "", "Completed" };

	public static final ThreadStatus ERROR = getStatus(0);

	public static final ThreadStatus STOPPED = getStatus(1);

	public static final ThreadStatus CONNECTING = getStatus(2);

	public static final ThreadStatus IN_PROGRESS = getStatus(5);

	public static final ThreadStatus PAUSED = getStatus(7);

	public static final ThreadStatus RESUMING = getStatus(8);

	public static final ThreadStatus COMPLETED = getStatus(10);

	int id = -1;

	String desc = "";

	public ThreadStatus(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public static ThreadStatus getStatus(int id) {
		if (id > 10)
			id = 10;

		if (id < 0)
			id = 0;

		return new ThreadStatus(id, descs[id]);
	}

	public int getID() {
		return id;
	}

	public String getDescription() {
		return desc;
	}

	public String toString() {
		return desc;
	}
}