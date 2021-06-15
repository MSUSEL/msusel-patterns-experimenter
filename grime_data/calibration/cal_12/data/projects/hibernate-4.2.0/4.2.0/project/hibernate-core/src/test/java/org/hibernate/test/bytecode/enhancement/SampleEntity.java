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
package org.hibernate.test.bytecode.enhancement;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.ManagedEntity;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;

/**
 * @author Steve Ebersole
 */
public class SampleEntity implements ManagedEntity, PersistentAttributeInterceptable {
	@Transient
	private transient EntityEntry entityEntry;
	@Transient
	private transient ManagedEntity previous;
	@Transient
	private transient ManagedEntity next;
	@Transient
	private transient PersistentAttributeInterceptor interceptor;

	private Long id;
	private String name;

	@Id
	public Long getId() {
		return hibernate_read_id();
	}

	public void setId(Long id) {
		hibernate_write_id( id );
	}

	public String getName() {
		return hibernate_read_name();
	}

	public void setName(String name) {
		hibernate_write_name( name );
	}

	private Long hibernate_read_id() {
		if ( $$_hibernate_getInterceptor() != null ) {
			this.id = (Long) $$_hibernate_getInterceptor().readObject( this, "id", this.id );
		}
		return id;
	}

	private void hibernate_write_id(Long id) {
		Long localVar = id;
		if ( $$_hibernate_getInterceptor() != null ) {
			localVar = (Long) $$_hibernate_getInterceptor().writeObject( this, "id", this.id, id );
		}
		this.id = localVar;
	}

	private String hibernate_read_name() {
		if ( $$_hibernate_getInterceptor() != null ) {
			this.name = (String) $$_hibernate_getInterceptor().readObject( this, "name", this.name );
		}
		return name;
	}

	private void hibernate_write_name(String name) {
		String localName = name;
		if ( $$_hibernate_getInterceptor() != null ) {
			localName = (String) $$_hibernate_getInterceptor().writeObject( this, "name", this.name, name );
		}
		this.name = localName;
	}

	@Override
	public Object $$_hibernate_getEntityInstance() {
		return this;
	}

	@Override
	public EntityEntry $$_hibernate_getEntityEntry() {
		return entityEntry;
	}

	@Override
	public void $$_hibernate_setEntityEntry(EntityEntry entityEntry) {
		this.entityEntry = entityEntry;
	}

	@Override
	public ManagedEntity $$_hibernate_getNextManagedEntity() {
		return next;
	}

	@Override
	public void $$_hibernate_setNextManagedEntity(ManagedEntity next) {
		this.next = next;
	}

	@Override
	public ManagedEntity $$_hibernate_getPreviousManagedEntity() {
		return previous;
	}

	@Override
	public void $$_hibernate_setPreviousManagedEntity(ManagedEntity previous) {
		this.previous = previous;
	}

	@Override
	public PersistentAttributeInterceptor $$_hibernate_getInterceptor() {
		return interceptor;
	}

	@Override
	public void $$_hibernate_setInterceptor(PersistentAttributeInterceptor interceptor) {
		this.interceptor = interceptor;
	}
}
