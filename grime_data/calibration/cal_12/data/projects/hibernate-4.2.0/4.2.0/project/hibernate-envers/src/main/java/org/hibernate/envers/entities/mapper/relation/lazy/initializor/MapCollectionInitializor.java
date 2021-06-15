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
import java.util.List;
import java.util.Map;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.mapper.relation.MiddleComponentData;
import org.hibernate.envers.entities.mapper.relation.query.RelationQueryGenerator;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.internal.util.ReflectHelper;

/**
 * Initializes a map.
 * @author Adam Warski (adam at warski dot org)
 */
public class MapCollectionInitializor<T extends Map> extends AbstractCollectionInitializor<T> {
    protected final Class<? extends T> collectionClass;
    private final MiddleComponentData elementComponentData;
    private final MiddleComponentData indexComponentData;

    public MapCollectionInitializor(AuditConfiguration verCfg,
                                    AuditReaderImplementor versionsReader,
                                    RelationQueryGenerator queryGenerator,
                                    Object primaryKey, Number revision,
                                    Class<? extends T> collectionClass,
                                    MiddleComponentData elementComponentData,
                                    MiddleComponentData indexComponentData) {
        super(verCfg, versionsReader, queryGenerator, primaryKey, revision);

        this.collectionClass = collectionClass;
        this.elementComponentData = elementComponentData;
        this.indexComponentData = indexComponentData;
    }

    protected T initializeCollection(int size) {
        try {
            return (T) ReflectHelper.getDefaultConstructor(collectionClass).newInstance();
        } catch (InstantiationException e) {
            throw new AuditException(e);
        } catch (IllegalAccessException e) {
            throw new AuditException(e);
        } catch (InvocationTargetException e) {
            throw new AuditException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    protected void addToCollection(T collection, Object collectionRow) {
        // collectionRow will be the actual object if retrieved from audit relation or middle table
        // otherwise it will be a List
        Object elementData = collectionRow;
	Object indexData = collectionRow;
	if (collectionRow instanceof java.util.List) {
            elementData = ((List) collectionRow).get(elementComponentData.getComponentIndex());
            indexData = ((List) collectionRow).get(indexComponentData.getComponentIndex());
	}
        Object element = elementComponentData.getComponentMapper().mapToObjectFromFullMap(entityInstantiator,
                (Map<String, Object>) elementData, null, revision);

        Object index = indexComponentData.getComponentMapper().mapToObjectFromFullMap(entityInstantiator,
                (Map<String, Object>) indexData, element, revision);

        collection.put(index, element);
    }
}
