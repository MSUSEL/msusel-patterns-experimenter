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
package org.hibernate.mapping;

import org.hibernate.MappingException;
import org.hibernate.cfg.Mappings;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.type.CollectionType;
import org.hibernate.type.PrimitiveType;

/**
 * An array mapping has a primary key consisting of the key columns + index column.
 *
 * @author Gavin King
 */
public class Array extends List {

	private String elementClassName;

	public Array(Mappings mappings, PersistentClass owner) {
		super( mappings, owner );
	}

	public Class getElementClass() throws MappingException {
		if (elementClassName==null) {
			org.hibernate.type.Type elementType = getElement().getType();
			return isPrimitiveArray() ?
				( (PrimitiveType) elementType ).getPrimitiveClass() :
				elementType.getReturnedClass();
		}
		else {
			try {
				return ReflectHelper.classForName(elementClassName);
			}
			catch (ClassNotFoundException cnfe) {
				throw new MappingException(cnfe);
			}
		}
	}

	@Override
    public CollectionType getDefaultCollectionType() throws MappingException {
		return getMappings().getTypeResolver()
				.getTypeFactory()
				.array( getRole(), getReferencedPropertyName(), getElementClass() );
	}

	@Override
    public boolean isArray() {
		return true;
	}

	/**
	 * @return Returns the elementClassName.
	 */
	public String getElementClassName() {
		return elementClassName;
	}
	/**
	 * @param elementClassName The elementClassName to set.
	 */
	public void setElementClassName(String elementClassName) {
		this.elementClassName = elementClassName;
	}

	@Override
    public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}
}
