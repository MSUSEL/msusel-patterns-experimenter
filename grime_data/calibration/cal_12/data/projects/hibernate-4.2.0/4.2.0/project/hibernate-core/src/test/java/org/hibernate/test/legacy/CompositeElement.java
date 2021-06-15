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
//$Id: CompositeElement.java 6844 2005-05-21 14:22:16Z oneovthafew $
package org.hibernate.test.legacy;
import java.io.Serializable;

public class CompositeElement implements Comparable, Serializable {
	private String foo;
	private String bar;
	/**
	 * Returns the bar.
	 * @return String
	 */
	public String getBar() {
		return bar;
	}

	/**
	 * Returns the foo.
	 * @return String
	 */
	public String getFoo() {
		return foo;
	}

	/**
	 * Sets the bar.
	 * @param bar The bar to set
	 */
	public void setBar(String bar) {
		this.bar = bar;
	}

	/**
	 * Sets the foo.
	 * @param foo The foo to set
	 */
	public void setFoo(String foo) {
		this.foo = foo;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		return ( (CompositeElement) o ).foo.compareTo(foo);
	}
	
	public int hashCode() {
		return foo.hashCode() + bar.hashCode();
	}
	
	public boolean equals(Object that) {
		CompositeElement ce = (CompositeElement) that;
		return ce.bar.equals(bar) && ce.foo.equals(foo);
	}

}
