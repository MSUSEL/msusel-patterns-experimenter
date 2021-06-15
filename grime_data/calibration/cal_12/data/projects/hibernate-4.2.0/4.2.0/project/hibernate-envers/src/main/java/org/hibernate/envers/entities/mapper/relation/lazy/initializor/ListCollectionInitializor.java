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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.mapper.relation.MiddleComponentData;
import org.hibernate.envers.entities.mapper.relation.query.RelationQueryGenerator;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * Initializes a map.
 * @author Adam Warski (adam at warski dot org)
 */
public class ListCollectionInitializor extends AbstractCollectionInitializor<List> {
    private final MiddleComponentData elementComponentData;
    private final MiddleComponentData indexComponentData;

    public ListCollectionInitializor(AuditConfiguration verCfg,
                                    AuditReaderImplementor versionsReader,
                                    RelationQueryGenerator queryGenerator,
                                    Object primaryKey, Number revision,
                                    MiddleComponentData elementComponentData,
                                    MiddleComponentData indexComponentData) {
        super(verCfg, versionsReader, queryGenerator, primaryKey, revision);

        this.elementComponentData = elementComponentData;
        this.indexComponentData = indexComponentData;
    }

    @SuppressWarnings({"unchecked"})
    protected List initializeCollection(int size) {
        // Creating a list of the given capacity with all elements null initially. This ensures that we can then
        // fill the elements safely using the <code>List.set</code> method.
        List list = new ArrayList(size);
        for (int i=0; i<size; i++) { list.add(null); }
        return list;
    }

    @SuppressWarnings({"unchecked"})
    protected void addToCollection(List collection, Object collectionRow) {
        // collectionRow will be the actual object if retrieved from audit relation or middle table
        // otherwise it will be a List
        Object elementData = collectionRow;
	Object indexData = collectionRow;
	if (collectionRow instanceof java.util.List) {
            elementData = ((List) collectionRow).get(elementComponentData.getComponentIndex());
            indexData = ((List) collectionRow).get(indexComponentData.getComponentIndex());
	}
        Object element = elementData instanceof Map ?
                elementComponentData.getComponentMapper().mapToObjectFromFullMap(entityInstantiator,
                (Map<String, Object>) elementData, null, revision)
                : elementData ;

        Object indexObj = indexComponentData.getComponentMapper().mapToObjectFromFullMap(entityInstantiator,
                (Map<String, Object>) indexData, element, revision);
        int index = ((Number) indexObj).intValue();

        collection.set(index, element);
    }
}
