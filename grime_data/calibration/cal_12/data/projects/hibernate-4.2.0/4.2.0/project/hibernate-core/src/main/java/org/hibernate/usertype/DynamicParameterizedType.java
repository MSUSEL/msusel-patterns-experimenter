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
package org.hibernate.usertype;

import java.lang.annotation.Annotation;

/**
 * Types who implements this interface will have in the setParameterValues an
 * instance of the class DynamicParameterizedType$ParameterType with 
 * the key PARAMETER_TYPE = "org.hibernate.type.ParameterType"
 * 
 * The interface ParameterType provides some methods to read information
 * dynamically for build the type
 * 
 * @author Janario Oliveira
 */
public interface DynamicParameterizedType extends ParameterizedType {
	public static final String PARAMETER_TYPE = "org.hibernate.type.ParameterType";

	public static final String IS_DYNAMIC = "org.hibernate.type.ParameterType.dynamic";

	public static final String RETURNED_CLASS = "org.hibernate.type.ParameterType.returnedClass";
	public static final String IS_PRIMARY_KEY = "org.hibernate.type.ParameterType.primaryKey";
	public static final String ENTITY = "org.hibernate.type.ParameterType.entityClass";
	public static final String PROPERTY = "org.hibernate.type.ParameterType.propertyName";
	public static final String ACCESS_TYPE = "org.hibernate.type.ParameterType.accessType";
	public static final String XPROPERTY = "org.hibernate.type.ParameterType.xproperty";

	public static interface ParameterType {

		public Class getReturnedClass();

		public Annotation[] getAnnotationsMethod();

		public String getCatalog();

		public String getSchema();

		public String getTable();

		public boolean isPrimaryKey();

		public String[] getColumns();

	}
}
