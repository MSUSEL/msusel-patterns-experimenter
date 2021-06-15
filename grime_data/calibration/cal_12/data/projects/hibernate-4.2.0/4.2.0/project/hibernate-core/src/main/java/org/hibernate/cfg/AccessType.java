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
package org.hibernate.cfg;

/**
 * Enum defining different access strategies for accessing entity values.
 *
 * @author Hardy Ferentschik
 */
public enum AccessType {
	/**
	 * Default access strategy is property
	 */
	DEFAULT( "property" ),

	/**
	 * Access to value via property
	 */
	PROPERTY( "property" ),

	/**
	 * Access to value via field
	 */
	FIELD( "field" );

	private final String accessType;

	AccessType(String type) {
		this.accessType = type;
	}

	public String getType() {
		return accessType;
	}

	public static AccessType getAccessStrategy(String type) {
		if ( type == null ) {
			return DEFAULT;
		}
		else if ( FIELD.getType().equals( type ) ) {
			return FIELD;
		}
		else if ( PROPERTY.getType().equals( type ) ) {
			return PROPERTY;
		}
		else {
			// TODO historically if the type string could not be matched default access was used. Maybe this should be an exception though!?
			return DEFAULT;
		}
	}

	public static AccessType getAccessStrategy(javax.persistence.AccessType type) {
		if ( javax.persistence.AccessType.PROPERTY.equals( type ) ) {
			return PROPERTY;
		}
		else if ( javax.persistence.AccessType.FIELD.equals( type ) ) {
			return FIELD;
		}
		else {
			return DEFAULT;
		}
	}
}
