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
//$Id: Contained.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;
import java.util.ArrayList;
import java.util.Collection;

public class Contained {
	private Container container;
	private long id;
	private Collection bag = new ArrayList();
	private Collection lazyBag = new ArrayList();
	
	public boolean equals(Object other) {
		return id==( (Contained) other ).getId();
	}
	public int hashCode() {
		return new Long(id).hashCode();
	}
	
	/**
	 * Returns the container.
	 * @return Container
	 */
	public Container getContainer() {
		return container;
	}
	
	/**
	 * Returns the id.
	 * @return long
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the container.
	 * @param container The container to set
	 */
	public void setContainer(Container container) {
		this.container = container;
	}
	
	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Returns the bag.
	 * @return Collection
	 */
	public Collection getBag() {
		return bag;
	}
	
	/**
	 * Sets the bag.
	 * @param bag The bag to set
	 */
	public void setBag(Collection bag) {
		this.bag = bag;
	}
	
	/**
	 * Returns the lazyBag.
	 * @return Collection
	 */
	public Collection getLazyBag() {
		return lazyBag;
	}
	
	/**
	 * Sets the lazyBag.
	 * @param lazyBag The lazyBag to set
	 */
	public void setLazyBag(Collection lazyBag) {
		this.lazyBag = lazyBag;
	}
	
}






