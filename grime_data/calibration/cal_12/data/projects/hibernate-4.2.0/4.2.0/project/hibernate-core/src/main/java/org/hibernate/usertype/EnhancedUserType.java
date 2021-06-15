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

/**
 * A custom type that may function as an identifier or discriminator type
 * 
 * @author Gavin King
 */
public interface EnhancedUserType extends UserType {
	/**
	 * Return an SQL literal representation of the value
	 */
	public String objectToSQLString(Object value);
	
	/**
	 * Return a string representation of this value, as it should appear in an XML document
	 *
	 * @deprecated To be removed in 5.  Implement {@link org.hibernate.type.StringRepresentableType#toString(Object)}
	 * instead.  See <a href="https://hibernate.onjira.com/browse/HHH-7776">HHH-7776</a> for details
	 */
	@Deprecated
	public String toXMLString(Object value);

	/**
	 * Parse a string representation of this value, as it appears in an XML document
	 *
	 * @deprecated To be removed in 5.  Implement
	 * {@link org.hibernate.type.StringRepresentableType#fromStringValue(String)} instead.
	 * See <a href="https://hibernate.onjira.com/browse/HHH-7776">HHH-7776</a> for details
	 */
	@Deprecated
	public Object fromXMLString(String xmlValue);
}
