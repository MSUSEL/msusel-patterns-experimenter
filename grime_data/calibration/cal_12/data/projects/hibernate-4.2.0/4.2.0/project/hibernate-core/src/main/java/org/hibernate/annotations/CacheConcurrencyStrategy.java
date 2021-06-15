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
package org.hibernate.annotations;

import org.hibernate.cache.spi.access.AccessType;

/**
 * Cache concurrency strategy
 *
 * @author Emmanuel Bernard
 */
public enum CacheConcurrencyStrategy {
	NONE( null ),
	READ_ONLY( AccessType.READ_ONLY ),
	NONSTRICT_READ_WRITE( AccessType.NONSTRICT_READ_WRITE ),
	READ_WRITE( AccessType.READ_WRITE ),
	TRANSACTIONAL( AccessType.TRANSACTIONAL );

	private final AccessType accessType;

	private CacheConcurrencyStrategy(AccessType accessType) {
		this.accessType = accessType;
	}

	private boolean isMatch(String name) {
		return ( accessType != null && accessType.getExternalName().equalsIgnoreCase( name ) )
				|| name().equalsIgnoreCase( name );
	}

	public static CacheConcurrencyStrategy fromAccessType(AccessType accessType) {
		if (null == accessType) {
			return NONE;
		}
		
		switch ( accessType ) {
			case READ_ONLY: {
				return READ_ONLY;
			}
			case READ_WRITE: {
				return READ_WRITE;
			}
			case NONSTRICT_READ_WRITE: {
				return NONSTRICT_READ_WRITE;
			}
			case TRANSACTIONAL: {
				return TRANSACTIONAL;
			}
			default: {
				return NONE;
			}
		}
	}

	public static CacheConcurrencyStrategy parse(String name) {
		if ( READ_ONLY.isMatch( name ) ) {
			return READ_ONLY;
		}
		else if ( READ_WRITE.isMatch( name ) ) {
			return READ_WRITE;
		}
		else if ( NONSTRICT_READ_WRITE.isMatch( name ) ) {
			return NONSTRICT_READ_WRITE;
		}
		else if ( TRANSACTIONAL.isMatch( name ) ) {
			return TRANSACTIONAL;
		}
		else if ( NONE.isMatch( name ) ) {
			return NONE;
		}
		else {
			return null;
		}
	}

	public AccessType toAccessType() {
		return accessType;
	}
}
