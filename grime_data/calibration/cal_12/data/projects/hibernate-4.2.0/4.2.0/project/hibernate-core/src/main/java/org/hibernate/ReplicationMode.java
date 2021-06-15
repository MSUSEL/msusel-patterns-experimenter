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

import org.hibernate.type.VersionType;

/**
 * Represents a replication strategy.
 *
 * @author Gavin King
 * @see Session#replicate(Object, ReplicationMode)
 */
public enum ReplicationMode {
	/**
	 * Throw an exception when a row already exists.
	 */
	EXCEPTION {
		public boolean shouldOverwriteCurrentVersion(Object entity, Object currentVersion, Object newVersion, VersionType versionType) {
			throw new AssertionFailure( "should not be called" );
		}
	},
	/**
	 * Ignore replicated entities when a row already exists.
	 */
	IGNORE {
		public boolean shouldOverwriteCurrentVersion(Object entity, Object currentVersion, Object newVersion, VersionType versionType) {
			return false;
		}
	},
	/**
	 * Overwrite existing rows when a row already exists.
	 */
	OVERWRITE {
		public boolean shouldOverwriteCurrentVersion(Object entity, Object currentVersion, Object newVersion, VersionType versionType) {
			return true;
		}
	},
	/**
	 * When a row already exists, choose the latest version.
	 */
	LATEST_VERSION {
		public boolean shouldOverwriteCurrentVersion(Object entity, Object currentVersion, Object newVersion, VersionType versionType) {
			if ( versionType == null ) {
				return true; //always overwrite nonversioned data
			}
			return versionType.getComparator().compare( currentVersion, newVersion ) <= 0;
		}
	};

	public abstract boolean shouldOverwriteCurrentVersion(Object entity, Object currentVersion, Object newVersion, VersionType versionType);


}






