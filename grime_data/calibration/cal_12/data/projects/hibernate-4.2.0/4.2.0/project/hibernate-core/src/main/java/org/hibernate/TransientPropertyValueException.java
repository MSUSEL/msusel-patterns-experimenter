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

import org.hibernate.internal.util.StringHelper;

/**
 * Thrown when a property cannot be persisted because it is an association
 * with a transient unsaved entity instance.
 *
 * @author Gail Badner
 */
public class TransientPropertyValueException extends TransientObjectException {
	private final String transientEntityName;
	private final String propertyOwnerEntityName;
	private final String propertyName;

	/**
	 * Constructs an {@link TransientPropertyValueException} instance.
	 *
	 * @param message - the exception message;
	 * @param transientEntityName - the entity name for the transient entity
	 * @param propertyOwnerEntityName - the entity name for entity that owns
	 * the association property.
	 * @param propertyName - the property name
	 */
	public TransientPropertyValueException(
			String message, 
			String transientEntityName, 
			String propertyOwnerEntityName, 
			String propertyName) {
		super(message);
		this.transientEntityName = transientEntityName;
		this.propertyOwnerEntityName = propertyOwnerEntityName;
		this.propertyName = propertyName;
	}

	/**
	 * Returns the entity name for the transient entity.
	 * @return the entity name for the transient entity.
	 */
	public String getTransientEntityName() {
		return transientEntityName;
	}

	/**
	 * Returns the entity name for entity that owns the association
	 * property.
	 * @return the entity name for entity that owns the association
	 * property
	 */
	public String getPropertyOwnerEntityName() {
		return propertyOwnerEntityName;
	}

	/**
	 * Returns the property name.
	 * @return the property name.
	 */
	public String getPropertyName() {
		return propertyName;
	}


	/**
	 * Return the exception message.
	 * @return the exception message.
	 */
	@Override
	public String getMessage() {
		return new StringBuilder( super.getMessage() )
				.append( ": " )
				.append( StringHelper.qualify( propertyOwnerEntityName, propertyName ) )
				.append( " -> " )
				.append( transientEntityName )
				.toString();
	}
}
