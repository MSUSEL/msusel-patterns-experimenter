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
//$Id: MoreStuff.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;
import java.io.Serializable;
import java.util.Collection;


public class MoreStuff implements Serializable {
	private String stringId;
	private int intId;
	private Collection stuffs;
	private String name;
	
	public boolean equals(Object other) {
		return ( (MoreStuff) other ).getIntId()==intId && ( (MoreStuff) other ).getStringId().equals(stringId);
	}
	
	public int hashCode() {
		return stringId.hashCode();
	}
	
	/**
	 * Returns the stuffs.
	 * @return Collection
	 */
	public Collection getStuffs() {
		return stuffs;
	}
	
	/**
	 * Sets the stuffs.
	 * @param stuffs The stuffs to set
	 */
	public void setStuffs(Collection stuffs) {
		this.stuffs = stuffs;
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
	 * Returns the intId.
	 * @return int
	 */
	public int getIntId() {
		return intId;
	}
	
	/**
	 * Returns the stringId.
	 * @return String
	 */
	public String getStringId() {
		return stringId;
	}
	
	/**
	 * Sets the intId.
	 * @param intId The intId to set
	 */
	public void setIntId(int intId) {
		this.intId = intId;
	}
	
	/**
	 * Sets the stringId.
	 * @param stringId The stringId to set
	 */
	public void setStringId(String stringId) {
		this.stringId = stringId;
	}
	
}






