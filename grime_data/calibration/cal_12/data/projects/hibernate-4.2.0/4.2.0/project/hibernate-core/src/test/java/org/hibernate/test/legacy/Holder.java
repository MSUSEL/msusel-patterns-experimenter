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
//$Id: Holder.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;
import java.util.List;
import java.util.Set;

public class Holder implements Named {
	private String id;
	private List ones;
	private Foo[] fooArray;
	private Set foos;
	private String name;
	private Holder otherHolder;
	
	public Holder() {
	}
	public Holder(String name) {
		this.name=name;
	}
	
	/**
	 * Returns the fooArray.
	 * @return Foo[]
	 */
	public Foo[] getFooArray() {
		return fooArray;
	}
	
	/**
	 * Returns the foos.
	 * @return Set
	 */
	public Set getFoos() {
		return foos;
	}
	
	/**
	 * Sets the fooArray.
	 * @param fooArray The fooArray to set
	 */
	public void setFooArray(Foo[] fooArray) {
		this.fooArray = fooArray;
	}
	
	/**
	 * Sets the foos.
	 * @param foos The foos to set
	 */
	public void setFoos(Set foos) {
		this.foos = foos;
	}
	
	/**
	 * Returns the id.
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the ones.
	 * @return List
	 */
	public List getOnes() {
		return ones;
	}
	
	/**
	 * Sets the ones.
	 * @param ones The ones to set
	 */
	public void setOnes(List ones) {
		this.ones = ones;
	}
	
	public Holder getOtherHolder() {
		return otherHolder;
	}

	public void setOtherHolder(Holder holder) {
		otherHolder = holder;
	}

}






