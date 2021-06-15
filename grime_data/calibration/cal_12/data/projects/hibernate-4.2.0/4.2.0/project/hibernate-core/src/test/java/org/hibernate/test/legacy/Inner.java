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
package org.hibernate.test.legacy;
import java.io.Serializable;
import java.util.List;

/**
 * @author Stefano Travelli
 */
public class Inner implements Serializable {
	private InnerKey id;
	private String dudu;
	private List middles;
	private Outer backOut;

	public InnerKey getId() {
		return id;
	}

	public void setId(InnerKey id) {
		this.id = id;
	}

	public String getDudu() {
		return dudu;
	}

	public void setDudu(String dudu) {
		this.dudu = dudu;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Inner)) return false;

		final Inner cidSuper = (Inner) o;

		if (id != null ? !id.equals(cidSuper.id) : cidSuper.id != null) return false;

		return true;
	}

	public int hashCode() {
		return (id != null ? id.hashCode() : 0);
	}
	public List getMiddles() {
		return middles;
	}

	public void setMiddles(List list) {
		middles = list;
	}

	public Outer getBackOut() {
		return backOut;
	}

	public void setBackOut(Outer outer) {
		backOut = outer;
	}

}
