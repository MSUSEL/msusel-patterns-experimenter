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
package org.hibernate.cache.spi.entry;

import java.io.Serializable;

import org.hibernate.Interceptor;
import org.hibernate.event.spi.EventSource;
import org.hibernate.persister.entity.EntityPersister;

/**
 * A cached instance of a persistent class
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public interface CacheEntry extends Serializable {
	public boolean isReferenceEntry();

	/**
	 * Hibernate stores all entries pertaining to a given entity hierarchy in a single region.  This attribute
	 * tells us the specific entity type represented by the cached data.
	 *
	 * @return The entry's exact entity type.
	 */
	public String getSubclass();

	/**
	 * Retrieves the version (optimistic locking) associated with this cache entry.
	 *
	 * @return The version of the entity represented by this entry
	 */
	public Object getVersion();

	public boolean areLazyPropertiesUnfetched();


	// todo: this was added to support initializing an entity's EntityEntry snapshot during reattach;
	// this should be refactored to instead expose a method to assemble a EntityEntry based on this
	// state for return.
	public Serializable[] getDisassembledState();

}






