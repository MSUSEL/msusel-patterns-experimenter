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
package org.hibernate.envers.entities.mapper;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public interface PropertyMapper {
    /**
     * Maps properties to the given map, basing on differences between properties of new and old objects.
     * @param session The current session.
	 * @param data Data to map to.
	 * @param newObj New state of the entity.
	 * @param oldObj Old state of the entity.
	 * @return True if there are any differences between the states represented by newObj and oldObj.
     */
    boolean mapToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj);

    /**
     * Maps properties from the given map to the given object.
     * @param verCfg Versions configuration.
     * @param obj Object to map to.
     * @param data Data to map from.
     * @param primaryKey Primary key of the object to which we map (for relations)
     * @param versionsReader VersionsReader for reading relations
     * @param revision Revision at which the object is read, for reading relations
     */
    void mapToEntityFromMap(AuditConfiguration verCfg, Object obj, Map data, Object primaryKey,
                            AuditReaderImplementor versionsReader, Number revision);

    /**
     * Maps collection changes.
	 * @param session The current session.
     * @param referencingPropertyName Name of the field, which holds the collection in the entity.
     * @param newColl New collection, after updates.
     * @param oldColl Old collection, before updates.
     * @param id Id of the object owning the collection.
     * @return List of changes that need to be performed on the persistent store.
     */
    List<PersistentCollectionChangeData> mapCollectionChanges(SessionImplementor session, String referencingPropertyName,
                                                              PersistentCollection newColl,
                                                              Serializable oldColl, Serializable id);

	void mapModifiedFlagsToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj);
	void mapModifiedFlagsToMapForCollectionChange(String collectionPropertyName, Map<String, Object> data);
}
