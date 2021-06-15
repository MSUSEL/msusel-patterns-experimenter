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
package org.hibernate;

/**
 * Defines the various policies by which Hibernate might release its underlying
 * JDBC connection.
 *
 * @author Steve Ebersole
 */
public enum ConnectionReleaseMode{

	/**
	 * Indicates that JDBC connection should be aggressively released after each 
	 * SQL statement is executed. In this mode, the application <em>must</em>
	 * explicitly close all iterators and scrollable results. This mode may
	 * only be used with a JTA datasource.
	 */
	AFTER_STATEMENT("after_statement"),

	/**
	 * Indicates that JDBC connections should be released after each transaction 
	 * ends (works with both JTA-registered synch and HibernateTransaction API).
	 * This mode may not be used with an application server JTA datasource.
	 * <p/>
	 * This is the default mode starting in 3.1; was previously {@link #ON_CLOSE}.
	 */
	AFTER_TRANSACTION("after_transaction"),

	/**
	 * Indicates that connections should only be released when the Session is explicitly closed 
	 * or disconnected; this is the legacy (Hibernate2 and pre-3.1) behavior.
	 */
	ON_CLOSE("on_close");

	private final String name;
	ConnectionReleaseMode(String name){
		this.name = name;
	}
	public static ConnectionReleaseMode parse(String name){
		return ConnectionReleaseMode.valueOf( name.toUpperCase() );
	}
}
