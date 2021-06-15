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
//$Id: Lower.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;
import java.util.List;
import java.util.Set;


public class Lower extends Top {
	private int intprop;
	private String foo;
	private Set set;
	private List bag;
	private Top another;
	private Lower yetanother;
	private Po mypo;
	/**
	 * Returns the intprop.
	 * @return int
	 */
	public int getIntprop() {
		return intprop;
	}
	
	/**
	 * Sets the intprop.
	 * @param intprop The intprop to set
	 */
	public void setIntprop(int intprop) {
		this.intprop = intprop;
	}
	
	/**
	 * Returns the foo.
	 * @return String
	 */
	public String getFoo() {
		return foo;
	}
	
	/**
	 * Sets the foo.
	 * @param foo The foo to set
	 */
	public void setFoo(String foo) {
		this.foo = foo;
	}
	
	/**
	 * Returns the set.
	 * @return Set
	 */
	public Set getSet() {
		return set;
	}
	
	/**
	 * Sets the set.
	 * @param set The set to set
	 */
	public void setSet(Set set) {
		this.set = set;
	}
	
	/**
	 * Returns the another.
	 * @return Simple
	 */
	public Top getAnother() {
		return another;
	}
	
	/**
	 * Returns the yetanother.
	 * @return LessSimple
	 */
	public Lower getYetanother() {
		return yetanother;
	}
	
	/**
	 * Sets the another.
	 * @param another The another to set
	 */
	public void setAnother(Top another) {
		this.another = another;
	}
	
	/**
	 * Sets the yetanother.
	 * @param yetanother The yetanother to set
	 */
	public void setYetanother(Lower yetanother) {
		this.yetanother = yetanother;
	}
	
	/**
	 * Returns the bag.
	 * @return List
	 */
	public List getBag() {
		return bag;
	}
	
	/**
	 * Sets the bag.
	 * @param bag The bag to set
	 */
	public void setBag(List bag) {
		this.bag = bag;
	}
	
	/**
	 * Returns the mypo.
	 * @return Po
	 */
	public Po getMypo() {
		return mypo;
	}
	
	/**
	 * Sets the mypo.
	 * @param mypo The mypo to set
	 */
	public void setMypo(Po mypo) {
		this.mypo = mypo;
	}
	
}






