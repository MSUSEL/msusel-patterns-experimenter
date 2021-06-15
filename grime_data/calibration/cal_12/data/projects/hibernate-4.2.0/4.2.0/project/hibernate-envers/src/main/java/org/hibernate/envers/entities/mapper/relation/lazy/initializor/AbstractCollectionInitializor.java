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
import java.util.List;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.EntityInstantiator;
import org.hibernate.envers.entities.mapper.relation.query.RelationQueryGenerator;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * Initializes a persistent collection.
 * @author Adam Warski (adam at warski dot org)
 */
public abstract class AbstractCollectionInitializor<T> implements Initializor<T> {
    private final AuditReaderImplementor versionsReader;
    private final RelationQueryGenerator queryGenerator;
    private final Object primaryKey;
    
    protected final Number revision;
    protected final EntityInstantiator entityInstantiator;

    public AbstractCollectionInitializor(AuditConfiguration verCfg,
                                         AuditReaderImplementor versionsReader,
                                         RelationQueryGenerator queryGenerator,
                                         Object primaryKey, Number revision) {
        this.versionsReader = versionsReader;
        this.queryGenerator = queryGenerator;
        this.primaryKey = primaryKey;
        this.revision = revision;

        entityInstantiator = new EntityInstantiator(verCfg, versionsReader);
    }

    protected abstract T initializeCollection(int size);

    protected abstract void addToCollection(T collection, Object collectionRow);

    public T initialize() {
        List<?> collectionContent = queryGenerator.getQuery(versionsReader, primaryKey, revision).list();

        T collection = initializeCollection(collectionContent.size());

        for (Object collectionRow : collectionContent) {
            addToCollection(collection, collectionRow);
        }

        return collection;
    }
}
