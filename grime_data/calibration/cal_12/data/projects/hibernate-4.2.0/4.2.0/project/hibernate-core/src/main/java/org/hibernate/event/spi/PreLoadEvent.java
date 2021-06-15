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

import org.hibernate.persister.entity.EntityPersister;

/**
 * Called before injecting property values into a newly 
 * loaded entity instance.
 *
 * @author Gavin King
 */
public class PreLoadEvent extends AbstractEvent {
	private Object entity;
	private Object[] state;
	private Serializable id;
	private EntityPersister persister;

	public PreLoadEvent(EventSource session) {
		super(session);
	}

	public Object getEntity() {
		return entity;
	}
	
	public Serializable getId() {
		return id;
	}
	
	public EntityPersister getPersister() {
		return persister;
	}
	
	public Object[] getState() {
		return state;
	}

	public PreLoadEvent setEntity(Object entity) {
		this.entity = entity;
		return this;
	}
	
	public PreLoadEvent setId(Serializable id) {
		this.id = id;
		return this;
	}
	
	public PreLoadEvent setPersister(EntityPersister persister) {
		this.persister = persister;
		return this;
	}
	
	public PreLoadEvent setState(Object[] state) {
		this.state = state;
		return this;
	}
	
}
