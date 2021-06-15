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
package org.hibernate.envers.entities.mapper.id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.tools.reflection.ReflectionTools;
import org.hibernate.property.Getter;
import org.hibernate.property.Setter;
import org.hibernate.proxy.HibernateProxy;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class SingleIdMapper extends AbstractIdMapper implements SimpleIdMapperBuilder {
    private PropertyData propertyData;

    public SingleIdMapper() {
    }

    public SingleIdMapper(PropertyData propertyData) {
        this.propertyData = propertyData;
    }

    public void add(PropertyData propertyData) {
        if (this.propertyData != null) {
            throw new AuditException("Only one property can be added!");
        }

        this.propertyData = propertyData;
    }

    public boolean mapToEntityFromMap(Object obj, Map data) {
        if (data == null || obj == null) {
            return false;
        }

        Object value = data.get(propertyData.getName());
        if (value == null) {
            return false;
        }

        Setter setter = ReflectionTools.getSetter(obj.getClass(), propertyData);
        setter.set(obj, value, null);

        return true;
    }

    public Object mapToIdFromMap(Map data) {
        if (data == null) {
            return null;
        }

        return data.get(propertyData.getName());
    }

    public Object mapToIdFromEntity(Object data) {
        if (data == null) {
            return null;
        }

        if(data instanceof HibernateProxy) {
        	HibernateProxy hibernateProxy = (HibernateProxy) data;
        	return hibernateProxy.getHibernateLazyInitializer().getIdentifier();
        } else {
        	Getter getter = ReflectionTools.getGetter(data.getClass(), propertyData);
            return getter.get(data);
        }
    }

    public void mapToMapFromId(Map<String, Object> data, Object obj) {
        if (data != null) {
            data.put(propertyData.getName(), obj);
        }
    }

    public void mapToMapFromEntity(Map<String, Object> data, Object obj) {
        if (obj == null) {
            data.put(propertyData.getName(), null);
        } else {
            if(obj instanceof HibernateProxy) {
            	HibernateProxy hibernateProxy = (HibernateProxy)obj;
            	data.put(propertyData.getName(), hibernateProxy.getHibernateLazyInitializer().getIdentifier());
            } else {
            	Getter getter = ReflectionTools.getGetter(obj.getClass(), propertyData);
            	data.put(propertyData.getName(), getter.get(obj));
            }
        }
    }

    public void mapToEntityFromEntity(Object objTo, Object objFrom) {
        if (objTo == null || objFrom == null) {
            return;
        }

        Getter getter = ReflectionTools.getGetter(objFrom.getClass(), propertyData);
        Setter setter = ReflectionTools.getSetter(objTo.getClass(), propertyData);
        setter.set(objTo, getter.get(objFrom), null);
    }

    public IdMapper prefixMappedProperties(String prefix) {
        return new SingleIdMapper(new PropertyData(prefix + propertyData.getName(), propertyData));
    }

    public List<QueryParameterData> mapToQueryParametersFromId(Object obj) {
        List<QueryParameterData> ret = new ArrayList<QueryParameterData>();

        ret.add(new QueryParameterData(propertyData.getName(), obj));

        return ret;
    }
}
