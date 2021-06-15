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

/**
 * Instances represent a lock mode for a row of a relational
 * database table. It is not intended that users spend much
 * time worrying about locking since Hibernate usually
 * obtains exactly the right lock level automatically.
 * Some "advanced" users may wish to explicitly specify lock
 * levels.
 *
 * @author Gavin King
 * @see Session#lock(Object, LockMode)
 */
public enum LockMode {
	/**
	 * No lock required. If an object is requested with this lock
	 * mode, a <tt>READ</tt> lock will be obtained if it is
	 * necessary to actually read the state from the database,
	 * rather than pull it from a cache.<br>
	 * <br>
	 * This is the "default" lock mode.
	 */
	NONE( 0 ),
	/**
	 * A shared lock. Objects in this lock mode were read from
	 * the database in the current transaction, rather than being
	 * pulled from a cache.
	 */
	READ( 5 ),
	/**
	 * An upgrade lock. Objects loaded in this lock mode are
	 * materialized using an SQL <tt>select ... for update</tt>.
	 *
	 * @deprecated instead use PESSIMISTIC_WRITE
	 */
    @Deprecated
	UPGRADE( 10 ),
	/**
	 * Attempt to obtain an upgrade lock, using an Oracle-style
	 * <tt>select for update nowait</tt>. The semantics of
	 * this lock mode, once obtained, are the same as
	 * <tt>UPGRADE</tt>.
	 */
	UPGRADE_NOWAIT( 10 ),
	/**
	 * A <tt>WRITE</tt> lock is obtained when an object is updated
	 * or inserted.   This lock mode is for internal use only and is
	 * not a valid mode for <tt>load()</tt> or <tt>lock()</tt> (both
	 * of which throw exceptions if WRITE is specified).
	 */
	WRITE( 10 ),

	/**
	 * Similiar to {@link #UPGRADE} except that, for versioned entities,
	 * it results in a forced version increment.
	 *
	 * @deprecated instead use PESSIMISTIC_FORCE_INCREMENT
	 */
    @Deprecated
	FORCE( 15 ),

	/**
	 *  start of javax.persistence.LockModeType equivalent modes
	 */

	/**
	 * Optimisticly assume that transaction will not experience contention for
	 * entities.  The entity version will be verified near the transaction end.
	 */
	OPTIMISTIC( 6 ),

	/**
	 * Optimisticly assume that transaction will not experience contention for
	 * entities.  The entity version will be verified and incremented near the transaction end.
	 */
	OPTIMISTIC_FORCE_INCREMENT( 7 ),

	/**
	 * Implemented as PESSIMISTIC_WRITE.
	 * TODO:  introduce separate support for PESSIMISTIC_READ
	 */
	PESSIMISTIC_READ( 12 ),

	/**
	 * Transaction will obtain a database lock immediately.
	 * TODO:  add PESSIMISTIC_WRITE_NOWAIT
	 */
	PESSIMISTIC_WRITE( 13 ),

	/**
	 * Transaction will immediately increment the entity version.
	 */
	PESSIMISTIC_FORCE_INCREMENT( 17 );
	private final int level;

	private LockMode(int level) {
		this.level = level;
	}

	/**
	 * Check if this lock mode is more restrictive than the given lock mode.
	 *
	 * @param mode LockMode to check
	 *
	 * @return true if this lock mode is more restrictive than given lock mode
	 */
	public boolean greaterThan(LockMode mode) {
		return level > mode.level;
	}

	/**
	 * Check if this lock mode is less restrictive than the given lock mode.
	 *
	 * @param mode LockMode to check
	 *
	 * @return true if this lock mode is less restrictive than given lock mode
	 */
	public boolean lessThan(LockMode mode) {
		return level < mode.level;
	}


}
