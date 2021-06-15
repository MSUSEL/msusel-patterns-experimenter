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
//$Id: Circular.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;


public class Circular {
	
	private String id;
	private Class clazz;
	private Circular other;
	private Object anyEntity;
	
	/**
	 * Constructor for Circular.
	 */
	public Circular() {
		super();
	}
	
	/**
	 * Returns the clazz.
	 * @return Class
	 */
	public Class getClazz() {
		return clazz;
	}
	
	/**
	 * Returns the id.
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the clazz.
	 * @param clazz The clazz to set
	 */
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the other.
	 * @return Circular
	 */
	public Circular getOther() {
		return other;
	}
	
	/**
	 * Sets the other.
	 * @param other The other to set
	 */
	public void setOther(Circular other) {
		this.other = other;
	}
	
	/**
	 * Returns the anyEntity.
	 * @return Object
	 */
	public Object getAnyEntity() {
		return anyEntity;
	}
	
	/**
	 * Sets the anyEntity.
	 * @param anyEntity The anyEntity to set
	 */
	public void setAnyEntity(Object anyEntity) {
		this.anyEntity = anyEntity;
	}
	
}






