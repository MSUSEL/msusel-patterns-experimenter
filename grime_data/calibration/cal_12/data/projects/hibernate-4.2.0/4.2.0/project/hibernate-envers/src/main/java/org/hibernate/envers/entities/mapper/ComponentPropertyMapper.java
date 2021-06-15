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
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.envers.tools.reflection.ReflectionTools;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.property.Setter;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class ComponentPropertyMapper implements PropertyMapper, CompositeMapperBuilder {
    private final PropertyData propertyData;
    private final MultiPropertyMapper delegate;
	private final String componentClassName;

    public ComponentPropertyMapper(PropertyData propertyData, String componentClassName) {
        this.propertyData = propertyData;
        this.delegate = new MultiPropertyMapper();
		this.componentClassName = componentClassName;
    }

	public void add(PropertyData propertyData) {
        delegate.add(propertyData);
    }

    public CompositeMapperBuilder addComponent(PropertyData propertyData, String componentClassName) {
        return delegate.addComponent(propertyData, componentClassName);
    }

    public void addComposite(PropertyData propertyData, PropertyMapper propertyMapper) {
        delegate.addComposite(propertyData, propertyMapper);
    }

    public boolean mapToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
        return delegate.mapToMapFromEntity(session, data, newObj, oldObj);
    }

	@Override
	public void mapModifiedFlagsToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
		if (propertyData.isUsingModifiedFlag()) {
            data.put(propertyData.getModifiedFlagPropertyName(),
                    delegate.mapToMapFromEntity(session, new HashMap<String, Object>(), newObj, oldObj));
		}
	}

	@Override
	public void mapModifiedFlagsToMapForCollectionChange(String collectionPropertyName, Map<String, Object> data) {
		if (propertyData.isUsingModifiedFlag()) {
			boolean hasModifiedCollection = false;
			for (PropertyData propData : delegate.getProperties().keySet()) {
				if (collectionPropertyName.equals(propData.getName())) {
					hasModifiedCollection = true;
					break;
				}
			}
			data.put(propertyData.getModifiedFlagPropertyName(), hasModifiedCollection);
		}
	}

	public void mapToEntityFromMap(AuditConfiguration verCfg, Object obj, Map data, Object primaryKey, AuditReaderImplementor versionsReader, Number revision) {
        if (data == null || obj == null) {
            return;
        }

        if (propertyData.getBeanName() == null) {
            // If properties are not encapsulated in a component but placed directly in a class
            // (e.g. by applying <properties> tag).
            delegate.mapToEntityFromMap(verCfg, obj, data, primaryKey, versionsReader, revision);
            return;
        }

        Setter setter = ReflectionTools.getSetter(obj.getClass(), propertyData);

		// If all properties are null and single, then the component has to be null also.
		boolean allNullAndSingle = true;
		for (Map.Entry<PropertyData, PropertyMapper> property : delegate.getProperties().entrySet()) {
			if (data.get(property.getKey().getName()) != null || !(property.getValue() instanceof SinglePropertyMapper)) {
				allNullAndSingle = false;
				break;
			}
		}

		if (allNullAndSingle) {
			// single property, but default value need not be null, so we'll set it to null anyway 
			setter.set(obj, null, null);			
		} else {
			// set the component
			try {
				Object subObj = ReflectHelper.getDefaultConstructor(
						ReflectHelper.classForName(componentClassName)).newInstance();
				setter.set(obj, subObj, null);
				delegate.mapToEntityFromMap(verCfg, subObj, data, primaryKey, versionsReader, revision);
			} catch (Exception e) {
				throw new AuditException(e);
			}
		}
    }

	public List<PersistentCollectionChangeData> mapCollectionChanges(SessionImplementor session, String referencingPropertyName,
                                                                     PersistentCollection newColl,
                                                                     Serializable oldColl, Serializable id) {
        return delegate.mapCollectionChanges(session, referencingPropertyName, newColl, oldColl, id);
    }

	public Map<PropertyData, PropertyMapper> getProperties() {
		return delegate.getProperties();
	}
}
