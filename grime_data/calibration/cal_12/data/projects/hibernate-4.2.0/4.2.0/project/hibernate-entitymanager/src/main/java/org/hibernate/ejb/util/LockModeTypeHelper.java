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
package org.hibernate.ejb.util;

import javax.persistence.LockModeType;

import org.hibernate.AssertionFailure;
import org.hibernate.LockMode;

/**
 * Helper to deal with {@link LockModeType} <-> {@link LockMode} conversions.
 *
 * @author Steve Ebersole
 */
public class LockModeTypeHelper {
	public static LockModeType getLockModeType(LockMode lockMode) {
		if ( lockMode == LockMode.NONE ) {
			return LockModeType.NONE;
		}
		else if ( lockMode == LockMode.OPTIMISTIC || lockMode == LockMode.READ ) {
			return LockModeType.OPTIMISTIC;
		}
		else if ( lockMode == LockMode.OPTIMISTIC_FORCE_INCREMENT || lockMode == LockMode.WRITE ) {
			return LockModeType.OPTIMISTIC_FORCE_INCREMENT;
		}
		else if ( lockMode == LockMode.PESSIMISTIC_READ ) {
			return LockModeType.PESSIMISTIC_READ;
		}
		else if ( lockMode == LockMode.PESSIMISTIC_WRITE
				|| lockMode == LockMode.UPGRADE
				|| lockMode == LockMode.UPGRADE_NOWAIT ) {
			return LockModeType.PESSIMISTIC_WRITE;
		}
		else if ( lockMode == LockMode.PESSIMISTIC_FORCE_INCREMENT
				|| lockMode == LockMode.FORCE ) {
			return LockModeType.PESSIMISTIC_FORCE_INCREMENT;
		}
		throw new AssertionFailure( "unhandled lock mode " + lockMode );
	}


	public static LockMode getLockMode(LockModeType lockMode) {
		switch ( lockMode ) {
			case READ:
			case OPTIMISTIC: {
				return LockMode.OPTIMISTIC;
			}
			case OPTIMISTIC_FORCE_INCREMENT:
			case WRITE: {
				return LockMode.OPTIMISTIC_FORCE_INCREMENT;
			}
			case PESSIMISTIC_READ: {
				return LockMode.PESSIMISTIC_READ;
			}
			case PESSIMISTIC_WRITE: {
				return LockMode.PESSIMISTIC_WRITE;
			}
			case PESSIMISTIC_FORCE_INCREMENT: {
				return LockMode.PESSIMISTIC_FORCE_INCREMENT;
			}
			case NONE: {
				return LockMode.NONE;
			}
			default: {
				throw new AssertionFailure( "Unknown LockModeType: " + lockMode );
			}
		}
	}

	public static LockMode interpretLockMode(Object value) {
		if ( value == null ) {
			return LockMode.NONE;
		}
		if ( LockMode.class.isInstance( value ) ) {
			return (LockMode) value;
		}
		else if ( LockModeType.class.isInstance( value ) ) {
			return getLockMode( (LockModeType) value );
		}
		else if ( String.class.isInstance( value ) ) {
			// first try LockMode name
			LockMode lockMode = LockMode.valueOf( (String) value );
			if ( lockMode == null ) {
				try {
					lockMode = getLockMode( LockModeType.valueOf( (String) value ) );
				}
				catch ( Exception ignore ) {
				}
			}
			if ( lockMode != null ) {
				return lockMode;
			}
		}

		throw new IllegalArgumentException( "Unknown lock mode source : " + value );
	}

}
