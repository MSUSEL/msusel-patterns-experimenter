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
//$Id: SubMulti.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;
import java.util.List;

public class SubMulti extends Multi {
	private float amount;
	private SubMulti parent;
	private List children;
	private List moreChildren;
	/**
	 * Returns the amount.
	 * @return float
	 */
	public float getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount.
	 * @param amount The amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	/**
	 * Returns the childen.
	 * @return List
	 */
	public List getChildren() {
		return children;
	}
	
	/**
	 * Returns the parent.
	 * @return SubMulti
	 */
	public SubMulti getParent() {
		return parent;
	}
	
	/**
	 * Sets the childen.
	 * @param childen The childen to set
	 */
	public void setChildren(List children) {
		this.children = children;
	}
	
	/**
	 * Sets the parent.
	 * @param parent The parent to set
	 */
	public void setParent(SubMulti parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns the moreChildren.
	 * @return List
	 */
	public List getMoreChildren() {
		return moreChildren;
	}

	/**
	 * Sets the moreChildren.
	 * @param moreChildren The moreChildren to set
	 */
	public void setMoreChildren(List moreChildren) {
		this.moreChildren = moreChildren;
	}

}






