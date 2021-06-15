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
package org.archive.util.anvl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.archive.io.UTF8Bytes;

/**
 * List of {@link ANVLRecord}s.
 * @author stack
 * @version $Date: 2006-08-05 01:15:47 +0000 (Sat, 05 Aug 2006) $ $Version$
 */
public class ANVLRecords extends ArrayList<ANVLRecord> implements UTF8Bytes {
	private static final long serialVersionUID = 5361551920550106113L;

	public ANVLRecords() {
	    super();
	}

	public ANVLRecords(int initialCapacity) {
		super(initialCapacity);
	}

	public ANVLRecords(Collection<ANVLRecord> c) {
		super(c);
	}

	public byte[] getUTF8Bytes() throws UnsupportedEncodingException {
		return toString().getBytes(UTF8);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (final Iterator i = iterator(); i.hasNext();) {
			sb.append(i.next().toString());
		}
		return super.toString();
	}
}