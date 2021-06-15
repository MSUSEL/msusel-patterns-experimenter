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
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.envers.tools.MappingTools;
import org.hibernate.envers.tools.Pair;
import org.hibernate.envers.tools.Tools;
import org.hibernate.envers.tools.reflection.ReflectionTools;
import org.hibernate.property.Getter;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class MultiPropertyMapper implements ExtendedPropertyMapper {
    protected final Map<PropertyData, PropertyMapper> properties;
    private final Map<String, PropertyData> propertyDatas;

    public MultiPropertyMapper() {
        properties = Tools.newHashMap();
        propertyDatas = Tools.newHashMap();
    }

    public void add(PropertyData propertyData) {
        SinglePropertyMapper single = new SinglePropertyMapper();
        single.add(propertyData);
        properties.put(propertyData, single);
        propertyDatas.put(propertyData.getName(), propertyData);
    }

    public CompositeMapperBuilder addComponent(PropertyData propertyData, String componentClassName) {
        if (properties.get(propertyData) != null) {
			// This is needed for second pass to work properly in the components mapper
            return (CompositeMapperBuilder) properties.get(propertyData);
        }

        ComponentPropertyMapper componentMapperBuilder = new ComponentPropertyMapper(propertyData, componentClassName);
		addComposite(propertyData, componentMapperBuilder);

        return componentMapperBuilder;
    }

    public void addComposite(PropertyData propertyData, PropertyMapper propertyMapper) {
        properties.put(propertyData, propertyMapper);
        propertyDatas.put(propertyData.getName(), propertyData);
    }

    private Object getAtIndexOrNull(Object[] array, int index) { return array == null ? null : array[index]; }

    public boolean map(SessionImplementor session, Map<String, Object> data, String[] propertyNames, Object[] newState, Object[] oldState) {
        boolean ret = false;
        for (int i=0; i<propertyNames.length; i++) {
            String propertyName = propertyNames[i];

            if (propertyDatas.containsKey(propertyName)) {
				PropertyMapper propertyMapper = properties.get(propertyDatas.get(propertyName));
				Object newObj = getAtIndexOrNull(newState, i);
				Object oldObj = getAtIndexOrNull(oldState, i);
				ret |= propertyMapper.mapToMapFromEntity(session, data, newObj, oldObj);
				propertyMapper.mapModifiedFlagsToMapFromEntity(session, data, newObj, oldObj);
            }
        }

        return ret;
    }

    public boolean mapToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
        boolean ret = false;
        for (PropertyData propertyData : properties.keySet()) {
            Getter getter;
            if (newObj != null) {
                getter = ReflectionTools.getGetter(newObj.getClass(), propertyData);
            } else if (oldObj != null) {
                getter = ReflectionTools.getGetter(oldObj.getClass(), propertyData);
            } else {
                return false;
            }

            ret |= properties.get(propertyData).mapToMapFromEntity(session, data,
                    newObj == null ? null : getter.get(newObj),
                    oldObj == null ? null : getter.get(oldObj));
        }

        return ret;
    }

	@Override
	public void mapModifiedFlagsToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
        for (PropertyData propertyData : properties.keySet()) {
            Getter getter;
            if (newObj != null) {
                getter = ReflectionTools.getGetter(newObj.getClass(), propertyData);
            } else if (oldObj != null) {
                getter = ReflectionTools.getGetter(oldObj.getClass(), propertyData);
            } else {
                return;
            }

            properties.get(propertyData).mapModifiedFlagsToMapFromEntity(session, data,
					newObj == null ? null : getter.get(newObj),
					oldObj == null ? null : getter.get(oldObj));
        }
	}

	public void mapToEntityFromMap(AuditConfiguration verCfg, Object obj, Map data, Object primaryKey,
                                   AuditReaderImplementor versionsReader, Number revision) {
        for (PropertyMapper mapper : properties.values()) {
            mapper.mapToEntityFromMap(verCfg, obj, data, primaryKey, versionsReader, revision);
        }
    }

	private Pair<PropertyMapper, String> getMapperAndDelegatePropName(String referencingPropertyName){
		// Name of the property, to which we will delegate the mapping.
		String delegatePropertyName;

		// Checking if the property name doesn't reference a collection in a component - then the name will containa a .
		int dotIndex = referencingPropertyName.indexOf('.');
		if (dotIndex != -1) {
			// Computing the name of the component
			String componentName = referencingPropertyName.substring(0, dotIndex);
			// And the name of the property in the component
			String propertyInComponentName = MappingTools.createComponentPrefix(componentName)
					+ referencingPropertyName.substring(dotIndex+1);

			// We need to get the mapper for the component.
			referencingPropertyName = componentName;
			// As this is a component, we delegate to the property in the component.
			delegatePropertyName = propertyInComponentName;
		} else {
			// If this is not a component, we delegate to the same property.
			delegatePropertyName = referencingPropertyName;
		}
		return Pair.make(properties.get(propertyDatas.get(referencingPropertyName)), delegatePropertyName);
	}

	@Override
	public void mapModifiedFlagsToMapForCollectionChange(String collectionPropertyName, Map<String, Object> data) {
		Pair<PropertyMapper, String> pair = getMapperAndDelegatePropName(collectionPropertyName);
		PropertyMapper mapper = pair.getFirst();
		if (mapper != null) {
			mapper.mapModifiedFlagsToMapForCollectionChange(pair.getSecond(), data);
		}
	}

	public List<PersistentCollectionChangeData> mapCollectionChanges(SessionImplementor session,
																	 String referencingPropertyName,
                                                                     PersistentCollection newColl,
                                                                     Serializable oldColl, Serializable id) {
		Pair<PropertyMapper, String> pair = getMapperAndDelegatePropName(referencingPropertyName);
		PropertyMapper mapper = pair.getFirst();
		if (mapper != null) {
			return mapper.mapCollectionChanges(session, pair.getSecond(), newColl, oldColl, id);
		} else {
			return null;
		}
	}

	public Map<PropertyData, PropertyMapper> getProperties() {
		return properties;
	}
}
