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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * A mapper which maps from a parent mapper and a "main" one, but adds only to the "main". The "main" mapper
 * should be the mapper of the subclass.
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class SubclassPropertyMapper implements ExtendedPropertyMapper {
    private ExtendedPropertyMapper main;
    private ExtendedPropertyMapper parentMapper;

    public SubclassPropertyMapper(ExtendedPropertyMapper main, ExtendedPropertyMapper parentMapper) {
        this.main = main;
        this.parentMapper = parentMapper;
    }

    public boolean map(SessionImplementor session, Map<String, Object> data, String[] propertyNames, Object[] newState, Object[] oldState) {
        boolean parentDiffs = parentMapper.map(session, data, propertyNames, newState, oldState);
        boolean mainDiffs = main.map(session, data, propertyNames, newState, oldState);

        return parentDiffs || mainDiffs;
    }

    public boolean mapToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
        boolean parentDiffs = parentMapper.mapToMapFromEntity(session, data, newObj, oldObj);
        boolean mainDiffs = main.mapToMapFromEntity(session, data, newObj, oldObj);

        return parentDiffs || mainDiffs;
    }

	@Override
	public void mapModifiedFlagsToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
		parentMapper.mapModifiedFlagsToMapFromEntity(session, data, newObj, oldObj);
        main.mapModifiedFlagsToMapFromEntity(session, data, newObj, oldObj);
	}

	@Override
	public void mapModifiedFlagsToMapForCollectionChange(String collectionPropertyName, Map<String, Object> data) {
		parentMapper.mapModifiedFlagsToMapForCollectionChange(collectionPropertyName, data);
		main.mapModifiedFlagsToMapForCollectionChange(collectionPropertyName, data);
	}

	public void mapToEntityFromMap(AuditConfiguration verCfg, Object obj, Map data, Object primaryKey, AuditReaderImplementor versionsReader, Number revision) {
        parentMapper.mapToEntityFromMap(verCfg, obj, data, primaryKey, versionsReader, revision);
        main.mapToEntityFromMap(verCfg, obj, data, primaryKey, versionsReader, revision);
    }

    public List<PersistentCollectionChangeData> mapCollectionChanges(SessionImplementor session, String referencingPropertyName,
                                                                     PersistentCollection newColl,
                                                                     Serializable oldColl, Serializable id) {
        List<PersistentCollectionChangeData> parentCollectionChanges = parentMapper.mapCollectionChanges(
                session, referencingPropertyName, newColl, oldColl, id);

		List<PersistentCollectionChangeData> mainCollectionChanges = main.mapCollectionChanges(
				session, referencingPropertyName, newColl, oldColl, id);

        if (parentCollectionChanges == null) {
            return mainCollectionChanges;
        } else {
        	if(mainCollectionChanges != null) {
                parentCollectionChanges.addAll(mainCollectionChanges);
        	}
			return parentCollectionChanges;
        }
    }

    public CompositeMapperBuilder addComponent(PropertyData propertyData, String componentClassName) {
        return main.addComponent(propertyData, componentClassName);
    }

    public void addComposite(PropertyData propertyData, PropertyMapper propertyMapper) {
        main.addComposite(propertyData, propertyMapper);
    }

    public void add(PropertyData propertyData) {
        main.add(propertyData);
    }

	public Map<PropertyData, PropertyMapper> getProperties() {
		final Map<PropertyData, PropertyMapper> joinedProperties = new HashMap<PropertyData, PropertyMapper>();
		joinedProperties.putAll(parentMapper.getProperties());
		joinedProperties.putAll(main.getProperties());
		return joinedProperties;
	}
}
