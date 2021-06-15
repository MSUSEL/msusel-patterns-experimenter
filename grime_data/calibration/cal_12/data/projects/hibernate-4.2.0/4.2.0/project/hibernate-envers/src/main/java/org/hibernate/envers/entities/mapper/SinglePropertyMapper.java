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

import org.hibernate.HibernateException;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.envers.tools.StringTools;
import org.hibernate.envers.tools.Tools;
import org.hibernate.envers.tools.reflection.ReflectionTools;
import org.hibernate.property.DirectPropertyAccessor;
import org.hibernate.property.Setter;

/**
 * TODO: diff
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class SinglePropertyMapper implements PropertyMapper, SimpleMapperBuilder {
    private PropertyData propertyData;

    public SinglePropertyMapper(PropertyData propertyData) {
        this.propertyData = propertyData;
    }

    public SinglePropertyMapper() { }

    public void add(PropertyData propertyData) {
        if (this.propertyData != null) {
            throw new AuditException("Only one property can be added!");
        }

        this.propertyData = propertyData;
    }

    public boolean mapToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
        data.put(propertyData.getName(), newObj);
        boolean dbLogicallyDifferent = true;
        if ((session.getFactory().getDialect() instanceof Oracle8iDialect) && (newObj instanceof String || oldObj instanceof String)) {
            // Don't generate new revision when database replaces empty string with NULL during INSERT or UPDATE statements.
            dbLogicallyDifferent = !(StringTools.isEmpty(newObj) && StringTools.isEmpty(oldObj));
        }
        return dbLogicallyDifferent && !Tools.objectsEqual(newObj, oldObj);
    }

	@Override
	public void mapModifiedFlagsToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
		if (propertyData.isUsingModifiedFlag()) {
			data.put(propertyData.getModifiedFlagPropertyName(), !Tools.objectsEqual(newObj, oldObj));
		}
	}

	@Override
	public void mapModifiedFlagsToMapForCollectionChange(String collectionPropertyName, Map<String, Object> data) {
	}

	public void mapToEntityFromMap(AuditConfiguration verCfg, Object obj, Map data, Object primaryKey,
                                   AuditReaderImplementor versionsReader, Number revision) {
        if (data == null || obj == null) {
            return;
        }

        Setter setter = ReflectionTools.getSetter(obj.getClass(), propertyData);
		Object value = data.get(propertyData.getName());
		// We only set a null value if the field is not primite. Otherwise, we leave it intact.
		if (value != null || !isPrimitive(setter, propertyData, obj.getClass())) {
        	setter.set(obj, value, null);
		}
    }

	private boolean isPrimitive(Setter setter, PropertyData propertyData, Class<?> cls) {
		if (cls == null) {
			throw new HibernateException("No field found for property: " + propertyData.getName());
		}

		if (setter instanceof DirectPropertyAccessor.DirectSetter) {
			// In a direct setter, getMethod() returns null
			// Trying to look up the field
			try {
				return cls.getDeclaredField(propertyData.getBeanName()).getType().isPrimitive();
			} catch (NoSuchFieldException e) {
				return isPrimitive(setter, propertyData, cls.getSuperclass());
			}
		} else {
			return setter.getMethod().getParameterTypes()[0].isPrimitive();
		}
	}

    public List<PersistentCollectionChangeData> mapCollectionChanges(SessionImplementor sessionImplementor,
																	 String referencingPropertyName,
                                                                     PersistentCollection newColl,
                                                                     Serializable oldColl, Serializable id) {
        return null;
    }

}
