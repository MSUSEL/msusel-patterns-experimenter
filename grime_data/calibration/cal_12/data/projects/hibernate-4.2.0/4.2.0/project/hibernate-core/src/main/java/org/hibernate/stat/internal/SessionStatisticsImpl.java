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
package org.hibernate.stat.internal;

import java.util.Collections;
import java.util.Set;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.stat.SessionStatistics;

/**
 * @author Gavin King
 */
public class SessionStatisticsImpl implements SessionStatistics {

	private final SessionImplementor session;
	
	public SessionStatisticsImpl(SessionImplementor session) {
		this.session = session;
	}

	public int getEntityCount() {
		return session.getPersistenceContext().getNumberOfManagedEntities();
	}
	
	public int getCollectionCount() {
		return session.getPersistenceContext().getCollectionEntries().size();
	}
	
	public Set getEntityKeys() {
		return Collections.unmodifiableSet( session.getPersistenceContext().getEntitiesByKey().keySet() );
	}
	
	public Set getCollectionKeys() {
		return Collections.unmodifiableSet( session.getPersistenceContext().getCollectionsByKey().keySet() );
	}
	
	public String toString() {
		return new StringBuilder()
			.append("SessionStatistics[")
			.append("entity count=").append( getEntityCount() )
			.append(",collection count=").append( getCollectionCount() )
			.append(']')
			.toString();
	}

}
