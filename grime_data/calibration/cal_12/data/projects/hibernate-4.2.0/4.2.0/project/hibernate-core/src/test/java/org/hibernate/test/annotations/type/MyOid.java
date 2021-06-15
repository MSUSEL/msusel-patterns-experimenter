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
//$Id$
package org.hibernate.test.annotations.type;
import java.io.Serializable;

/**
 * @author Emmanuel Bernard
 */
public class MyOid implements Serializable {
	private int high;
	private int middle;
	private int low;
	private int other;

	protected MyOid() {
	}

	public MyOid(int aHigh, int aMiddle, int aLow, int aOther) {
		high = aHigh;
		middle = aMiddle;
		low = aLow;
		other = aOther;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int aHigh) {
		high = aHigh;
	}

	public int getMiddle() {
		return middle;
	}

	public void setMiddle(int aMiddle) {
		middle = aMiddle;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int aLow) {
		low = aLow;
	}

	public int getOther() {
		return other;
	}

	public void setOther(int aOther) {
		other = aOther;
	}

	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;

		final MyOid myOid = (MyOid) o;

		if ( high != myOid.high ) return false;
		if ( low != myOid.low ) return false;
		if ( middle != myOid.middle ) return false;
		if ( other != myOid.other ) return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = low;
		result = 29 * result + middle;
		result = 29 * result + high;
		result = 29 * result + other;
		return result;
	}
}
