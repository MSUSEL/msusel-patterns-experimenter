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
package org.hibernate.persister.spi;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.PluralAttributeBinding;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.service.Service;

/**
 * Given an entity or collection mapping, resolve the appropriate persister class to use.
 * <p/>
 * The persister class is chosen according to the following rules:<ol>
 *     <li>the persister class defined explicitly via annotation or XML</li>
 *     <li>the persister class returned by the installed {@link PersisterClassResolver}</li>
 *     <li>the default provider as chosen by Hibernate Core (best choice most of the time)</li>
 * </ol>
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 * @author Steve Ebersole
 */
public interface PersisterClassResolver extends Service {
	/**
	 * Returns the entity persister class for a given entityName or null
	 * if the entity persister class should be the default.
	 *
	 * @param metadata The entity metadata
	 *
	 * @return The entity persister class to use
	 */
	Class<? extends EntityPersister> getEntityPersisterClass(PersistentClass metadata);

	/**
	 * Returns the entity persister class for a given entityName or null
	 * if the entity persister class should be the default.
	 *
	 * @param metadata The entity metadata
	 *
	 * @return The entity persister class to use
	 */
	Class<? extends EntityPersister> getEntityPersisterClass(EntityBinding metadata);

	/**
	 * Returns the collection persister class for a given collection role or null
	 * if the collection persister class should be the default.
	 *
	 * @param metadata The collection metadata
	 *
	 * @return The collection persister class to use
	 */
	Class<? extends CollectionPersister> getCollectionPersisterClass(Collection metadata);

	/**
	 * Returns the collection persister class for a given collection role or null
	 * if the collection persister class should be the default.
	 *
	 * @param metadata The collection metadata
	 *
	 * @return The collection persister class to use
	 */
	Class<? extends CollectionPersister> getCollectionPersisterClass(PluralAttributeBinding metadata);
}
