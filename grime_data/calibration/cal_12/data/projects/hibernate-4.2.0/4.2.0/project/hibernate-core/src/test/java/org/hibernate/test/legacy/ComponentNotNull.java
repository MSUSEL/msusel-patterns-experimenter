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
//$Id: ComponentNotNull.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;


/**
 * Component used to check not-null sub properties management
 * 
 * @author Emmanuel Bernard
 */
public class ComponentNotNull {
	/* 
	 * I've flatten several components in one class, this is kind of ugly but
 	 * I don't have to write tons of classes
 	 */
	private String prop1Nullable;
	private String prop2Nullable;
	private ComponentNotNull supercomp;
	private ComponentNotNull subcomp;
	private String prop1Subcomp;

	/**
	 * @return
	 */
	public String getProp1Nullable() {
		return prop1Nullable;
	}

	/**
	 * @return
	 */
	public String getProp1Subcomp() {
		return prop1Subcomp;
	}

	/**
	 * @return
	 */
	public String getProp2Nullable() {
		return prop2Nullable;
	}

	/**
	 * @return
	 */
	public ComponentNotNull getSubcomp() {
		return subcomp;
	}

	/**
	 * @return
	 */
	public ComponentNotNull getSupercomp() {
		return supercomp;
	}

	/**
	 * @param string
	 */
	public void setProp1Nullable(String string) {
		prop1Nullable = string;
	}

	/**
	 * @param string
	 */
	public void setProp1Subcomp(String string) {
		prop1Subcomp = string;
	}

	/**
	 * @param string
	 */
	public void setProp2Nullable(String string) {
		prop2Nullable = string;
	}

	/**
	 * @param null1
	 */
	public void setSubcomp(ComponentNotNull null1) {
		subcomp = null1;
	}

	/**
	 * @param null1
	 */
	public void setSupercomp(ComponentNotNull null1) {
		supercomp = null1;
	}

}
