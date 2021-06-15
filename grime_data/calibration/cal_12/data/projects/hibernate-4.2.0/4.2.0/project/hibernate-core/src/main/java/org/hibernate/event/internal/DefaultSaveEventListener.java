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
package org.hibernate.event.internal;

import java.io.Serializable;

import org.hibernate.Hibernate;
import org.hibernate.PersistentObjectException;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.Status;
import org.hibernate.event.spi.SaveOrUpdateEvent;

/**
 * An event handler for save() events
 * @author Gavin King
 */
public class DefaultSaveEventListener extends DefaultSaveOrUpdateEventListener {

	protected Serializable performSaveOrUpdate(SaveOrUpdateEvent event) {
		// this implementation is supposed to tolerate incorrect unsaved-value
		// mappings, for the purpose of backward-compatibility
		EntityEntry entry = event.getSession().getPersistenceContext().getEntry( event.getEntity() );
		if ( entry!=null && entry.getStatus() != Status.DELETED ) {
			return entityIsPersistent(event);
		}
		else {
			return entityIsTransient(event);
		}
	}
	
	protected Serializable saveWithGeneratedOrRequestedId(SaveOrUpdateEvent event) {
		if ( event.getRequestedId() == null ) {
			return super.saveWithGeneratedOrRequestedId(event);
		}
		else {
			return saveWithRequestedId( 
					event.getEntity(), 
					event.getRequestedId(), 
					event.getEntityName(), 
					null, 
					event.getSession() 
				);
		}
		
	}

	protected boolean reassociateIfUninitializedProxy(Object object, SessionImplementor source) {
		if ( !Hibernate.isInitialized(object) ) {
			throw new PersistentObjectException("uninitialized proxy passed to save()");
		}
		else {
			return false;
		}
	}
	

}
