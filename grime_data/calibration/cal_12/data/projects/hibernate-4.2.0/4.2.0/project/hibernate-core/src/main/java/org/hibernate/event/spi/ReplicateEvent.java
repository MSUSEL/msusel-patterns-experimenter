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
package org.hibernate.event.spi;

import org.hibernate.ReplicationMode;

/**
 *  Defines an event class for the replication of an entity.
 *
 * @author Steve Ebersole
 */
public class ReplicateEvent extends AbstractEvent {
	private Object object;
	private ReplicationMode replicationMode;
	private String entityName;

	public ReplicateEvent(Object object, ReplicationMode replicationMode, EventSource source) {
		this(null, object, replicationMode, source);
	}
	
	public ReplicateEvent(String entityName, Object object, ReplicationMode replicationMode, EventSource source) {
		super(source);
		this.entityName = entityName;

		if ( object == null ) {
			throw new IllegalArgumentException(
					"attempt to create replication strategy with null entity"
			);
		}
		if ( replicationMode == null ) {
			throw new IllegalArgumentException(
					"attempt to create replication strategy with null replication mode"
			);
		}

		this.object = object;
		this.replicationMode = replicationMode;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public ReplicationMode getReplicationMode() {
		return replicationMode;
	}

	public void setReplicationMode(ReplicationMode replicationMode) {
		this.replicationMode = replicationMode;
	}

	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
}
