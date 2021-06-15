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
package org.hibernate.bytecode.internal.javassist;

import java.io.Serializable;

/**
 * A JavaBean accessor.
 * <p/>
 * <p>This object provides methods that set/get multiple properties
 * of a JavaBean at once.  This class and its support classes have been
 * developed for the comaptibility with cglib
 * (<tt>http://cglib.sourceforge.net/</tt>).
 *
 * @author Muga Nishizawa
 * @author modified by Shigeru Chiba
 */
public abstract class BulkAccessor implements Serializable {
	protected Class target;
	protected String[] getters, setters;
	protected Class[] types;

	protected BulkAccessor() {
	}

	/**
	 * Obtains the values of properties of a given bean.
	 *
	 * @param bean   JavaBean.
	 * @param values the obtained values are stored in this array.
	 */
	public abstract void getPropertyValues(Object bean, Object[] values);

	/**
	 * Sets properties of a given bean to specified values.
	 *
	 * @param bean   JavaBean.
	 * @param values the values assinged to properties.
	 */
	public abstract void setPropertyValues(Object bean, Object[] values);

	/**
	 * Returns the values of properties of a given bean.
	 *
	 * @param bean JavaBean.
	 */
	public Object[] getPropertyValues(Object bean) {
		Object[] values = new Object[getters.length];
		getPropertyValues( bean, values );
		return values;
	}

	/**
	 * Returns the types of properties.
	 */
	public Class[] getPropertyTypes() {
		return ( Class[] ) types.clone();
	}

	/**
	 * Returns the setter names of properties.
	 */
	public String[] getGetters() {
		return ( String[] ) getters.clone();
	}

	/**
	 * Returns the getter names of the properties.
	 */
	public String[] getSetters() {
		return ( String[] ) setters.clone();
	}

	/**
	 * Creates a new instance of <code>BulkAccessor</code>.
	 * The created instance provides methods for setting/getting
	 * specified properties at once.
	 *
	 * @param beanClass the class of the JavaBeans accessed
	 *                  through the created object.
	 * @param getters   the names of setter methods for specified properties.
	 * @param setters   the names of getter methods for specified properties.
	 * @param types     the types of specified properties.
	 */
	public static BulkAccessor create(
			Class beanClass,
	        String[] getters,
	        String[] setters,
	        Class[] types) {
		BulkAccessorFactory factory = new BulkAccessorFactory( beanClass, getters, setters, types );
		return factory.create();
	}
}
