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
package org.hibernate.engine.spi;

import java.io.Serializable;

import org.hibernate.EntityMode;
import org.hibernate.type.Type;

/**
 * An ordered pair of a value and its Hibernate type.
 * 
 * @see org.hibernate.type.Type
 * @author Gavin King
 */
public final class TypedValue implements Serializable {
	private final Type type;
	private final Object value;
	private final EntityMode entityMode;

	public TypedValue(Type type, Object value) {
		this( type, value, EntityMode.POJO );
	}

	public TypedValue(Type type, Object value, EntityMode entityMode) {
		this.type = type;
		this.value=value;
		this.entityMode = entityMode;
	}

	public Object getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}

	public String toString() {
		return value==null ? "null" : value.toString();
	}

	public int hashCode() {
		//int result = 17;
		//result = 37 * result + type.hashCode();
		//result = 37 * result + ( value==null ? 0 : value.hashCode() );
		//return result;
		return value==null ? 0 : type.getHashCode(value );
	}

	public boolean equals(Object other) {
		if ( !(other instanceof TypedValue) ) return false;
		TypedValue that = (TypedValue) other;
		/*return that.type.equals(type) && 
			EqualsHelper.equals(that.value, value);*/
		return type.getReturnedClass() == that.type.getReturnedClass() &&
			type.isEqual(that.value, value );
	}

}





