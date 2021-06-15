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
package org.hibernate.envers.entities.mapper.relation.lazy.initializor;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.SortedSet;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.mapper.relation.MiddleComponentData;
import org.hibernate.envers.entities.mapper.relation.query.RelationQueryGenerator;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * Initializes SortedSet collection with proper Comparator
 *
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class SortedSetCollectionInitializor extends BasicCollectionInitializor<SortedSet> {
	private final Comparator comparator;

	public SortedSetCollectionInitializor(AuditConfiguration verCfg, AuditReaderImplementor versionsReader, RelationQueryGenerator queryGenerator, Object primaryKey, Number revision, Class<? extends SortedSet> collectionClass, MiddleComponentData elementComponentData, Comparator comparator) {
		super(verCfg, versionsReader, queryGenerator, primaryKey, revision, collectionClass, elementComponentData);
		this.comparator = comparator;
	}

	@Override
	protected SortedSet initializeCollection(int size) {
		if (comparator == null) {
			return super.initializeCollection(size);
		}
		try {
			return collectionClass.getConstructor(Comparator.class).newInstance(comparator);
		} catch (InstantiationException e) {
			throw new AuditException(e);
		} catch (IllegalAccessException e) {
			throw new AuditException(e);
		} catch (NoSuchMethodException e) {
			throw new AuditException(e);
		} catch (InvocationTargetException e) {
			throw new AuditException(e);
		}
	}
}
