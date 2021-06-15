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

import java.io.Serializable;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;

/**
 *  Defines an event class for the loading of an entity.
 *
 * @author Steve Ebersole
 */
public class LoadEvent extends AbstractEvent {
	public static final LockMode DEFAULT_LOCK_MODE = LockMode.NONE;

	private Serializable entityId;
	private String entityClassName;
	private Object instanceToLoad;
	private LockOptions lockOptions;
	private boolean isAssociationFetch;
	private Object result;

	public LoadEvent(Serializable entityId, Object instanceToLoad, EventSource source) {
		this(entityId, null, instanceToLoad, new LockOptions(), false, source);
	}

	public LoadEvent(Serializable entityId, String entityClassName, LockMode lockMode, EventSource source) {
		this(entityId, entityClassName, null, lockMode, false, source);
	}

	public LoadEvent(Serializable entityId, String entityClassName, LockOptions lockOptions, EventSource source) {
		this(entityId, entityClassName, null, lockOptions, false, source);
	}

	public LoadEvent(Serializable entityId, String entityClassName, boolean isAssociationFetch, EventSource source) {
		this(entityId, entityClassName, null, new LockOptions(), isAssociationFetch, source);
	}
	
	public boolean isAssociationFetch() {
		return isAssociationFetch;
	}

	private LoadEvent(
			Serializable entityId,
			String entityClassName,
			Object instanceToLoad,
			LockMode lockMode,
			boolean isAssociationFetch,
			EventSource source) {
		this(entityId, entityClassName, instanceToLoad, new LockOptions().setLockMode(lockMode), isAssociationFetch, source );
	}

	private LoadEvent(
			Serializable entityId,
			String entityClassName,
			Object instanceToLoad,
			LockOptions lockOptions,
			boolean isAssociationFetch,
			EventSource source) {

		super(source);

		if ( entityId == null ) {
			throw new IllegalArgumentException("id to load is required for loading");
		}

		if ( lockOptions.getLockMode() == LockMode.WRITE ) {
			throw new IllegalArgumentException("Invalid lock mode for loading");
		}
		else if ( lockOptions.getLockMode() == null ) {
			lockOptions.setLockMode(DEFAULT_LOCK_MODE);
		}

		this.entityId = entityId;
		this.entityClassName = entityClassName;
		this.instanceToLoad = instanceToLoad;
		this.lockOptions = lockOptions;
		this.isAssociationFetch = isAssociationFetch;
	}

	public Serializable getEntityId() {
		return entityId;
	}

	public void setEntityId(Serializable entityId) {
		this.entityId = entityId;
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}

	public Object getInstanceToLoad() {
		return instanceToLoad;
	}

	public void setInstanceToLoad(Object instanceToLoad) {
		this.instanceToLoad = instanceToLoad;
	}

	public LockOptions getLockOptions() {
		return lockOptions;
	}

	public LockMode getLockMode() {
		return lockOptions.getLockMode();
	}

	public void setLockMode(LockMode lockMode) {
		this.lockOptions.setLockMode(lockMode);
	}

	public void setLockTimeout(int timeout) {
		this.lockOptions.setTimeOut(timeout);
	}

	public int getLockTimeout() {
		return this.lockOptions.getTimeOut();
	}

	public void setLockScope(boolean cascade) {
		this.lockOptions.setScope(cascade);
	}

	public boolean getLockScope() {
		return this.lockOptions.getScope();
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
