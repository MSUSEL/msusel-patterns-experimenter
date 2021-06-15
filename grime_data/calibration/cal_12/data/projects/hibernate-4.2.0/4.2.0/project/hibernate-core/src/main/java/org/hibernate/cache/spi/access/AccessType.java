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
package org.hibernate.cache.spi.access;

/**
 * The types of access strategies available.
 *
 * @author Steve Ebersole
 */
public enum AccessType {
	READ_ONLY( "read-only" ),
	READ_WRITE( "read-write" ),
	NONSTRICT_READ_WRITE( "nonstrict-read-write" ),
	TRANSACTIONAL( "transactional" );

	private final String externalName;

	private AccessType(String externalName) {
		this.externalName = externalName;
	}

	public String getExternalName() {
		return externalName;
	}

	public String toString() {
		return "AccessType[" + externalName + "]";
	}

	public static AccessType fromExternalName(String externalName) {
		if ( externalName == null ) {
			return null;
		}
		for ( AccessType accessType : AccessType.values() ) {
			if ( accessType.getExternalName().equals( externalName ) ) {
				return accessType;
			}
		}
		throw new UnknownAccessTypeException( externalName );
	}
}
