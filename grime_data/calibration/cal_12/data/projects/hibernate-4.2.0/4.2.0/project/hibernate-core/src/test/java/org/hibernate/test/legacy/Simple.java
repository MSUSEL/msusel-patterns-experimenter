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

/**
 * @author Gavin King
 */
public class Simple implements Serializable {
	private Long id;
	private String name;
	private String address;
	private int count;
	private java.util.Date date;
	private Float number;
	private Simple other;

	private Long parent;

	public Simple(Long id) {
		this.id = id;
	}

	public Simple(int c) {
		count=c;
	}
	public Simple() {}

	public void init() {
		name="Someone With Along Name";
		address="1234 Some Street, Some City, Victoria, 3000, Austraya";
		count=69;
		date=new java.sql.Date(666);
		number=new Float(55.8);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name
	 * @return Returns a String
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the address
	 * @return Returns a String
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * Sets the address
	 * @param address The address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the count
	 * @return Returns a int
	 */
	public int getCount() {
		return count;
	}
	/**
	 * Sets the count
	 * @param count The count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Gets the date
	 * @return Returns a java.util.Date
	 */
	public java.util.Date getDate() {
		return date;
	}
	/**
	 * Sets the date
	 * @param date The date to set
	 */
	public void setDate(java.util.Date date) {
		this.date = date;
	}

	/**
	 * Gets the pay number
	 * @return Returns a Float
	 */
	public Float getPay() {
		return number;
	}

	/**
	 * Sets the pay number
	 * @param number The Pay to set
	 */
	public void setPay(Float number) {
		this.number = number;
	}

	/**
	 * Returns the other.
	 * @return Simple
	 */
	public Simple getOther() {
		return other;
	}

	/**
	 * Sets the other.
	 * @param other The other to set
	 */
	public void setOther(Simple other) {
		this.other = other;
	}

}







