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
package org.hibernate.envers.entities.mapper.relation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.mapper.PropertyMapper;
import org.hibernate.envers.entities.mapper.relation.lazy.initializor.BasicCollectionInitializor;
import org.hibernate.envers.entities.mapper.relation.lazy.initializor.Initializor;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class BasicCollectionMapper<T extends Collection> extends AbstractCollectionMapper<T> implements PropertyMapper {
    protected final MiddleComponentData elementComponentData;

    public BasicCollectionMapper(CommonCollectionMapperData commonCollectionMapperData,
                                 Class<? extends T> collectionClass, Class<? extends T> proxyClass,
                                 MiddleComponentData elementComponentData, boolean revisionTypeInId) {
        super(commonCollectionMapperData, collectionClass, proxyClass, revisionTypeInId);
        this.elementComponentData = elementComponentData;
    }

    protected Initializor<T> getInitializor(AuditConfiguration verCfg, AuditReaderImplementor versionsReader,
                                            Object primaryKey, Number revision) {
        return new BasicCollectionInitializor<T>(verCfg, versionsReader, commonCollectionMapperData.getQueryGenerator(),
                primaryKey, revision, collectionClass, elementComponentData);
    }

    protected Collection getNewCollectionContent(PersistentCollection newCollection) {
        return (Collection) newCollection;
    }

    protected Collection getOldCollectionContent(Serializable oldCollection) {
        if (oldCollection == null) {
            return null;
        } else if (oldCollection instanceof Map) {
            return ((Map) oldCollection).keySet();
        } else {
            return (Collection) oldCollection;
        }
    }

    protected void mapToMapFromObject(SessionImplementor session, Map<String, Object> idData, Map<String, Object> data, Object changed) {
        elementComponentData.getComponentMapper().mapToMapFromObject(session, idData, data, changed);
    }
}
