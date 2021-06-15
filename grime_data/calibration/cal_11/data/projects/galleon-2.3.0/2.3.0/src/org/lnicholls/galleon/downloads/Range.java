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
 *
 * Created on 21 de abril de 2003, 05:43 PM
 */

public class Range {

	long start = 0;

	long end = 0;

	/** Creates a new instance of Range */
	public Range(long start, long end) {

		if (end < start) {
			long b = end;
			end = start;
			start = b;
		}

		this.start = start;
		this.end = end;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public Range getSubRange(long start) {
		if (start < this.start)
			start = this.start;

		if (start > end)
			start = end;

		return new Range(start, getEnd());
	}

	public Range getSubRange(long start, long end) {
		if (start < this.start)
			start = this.start;

		if (start > this.end)
			start = this.end;

		if (end > this.end)
			end = this.end;

		if (end < this.start)
			end = this.start;

		return new Range(start, end);
	}

	public Range split(long point) {
		if (point < start)
			point = start;

		if (point > end)
			point = end;

		if (point > end)
			point = end;

		if (point < start)
			point = start;

		start = getSubRange(point + 1).getStart();

		return new Range(start, point);
	}

	public String toString() {
		return start + "-" + end;
	}
}