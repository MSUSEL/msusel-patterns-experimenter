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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.tools.reflection.ReflectionTools;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.property.Getter;
import org.hibernate.property.Setter;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class EmbeddedIdMapper extends AbstractCompositeIdMapper implements SimpleIdMapperBuilder {
    private PropertyData idPropertyData;

    public EmbeddedIdMapper(PropertyData idPropertyData, String compositeIdClass) {
        super(compositeIdClass);

        this.idPropertyData = idPropertyData;
    }

    public void mapToMapFromId(Map<String, Object> data, Object obj) {
        for (IdMapper idMapper : ids.values()) {
            idMapper.mapToMapFromEntity(data, obj);
        }
    }

    public void mapToMapFromEntity(Map<String, Object> data, Object obj) {
        if (obj == null) {
            return;
        }

        Getter getter = ReflectionTools.getGetter(obj.getClass(), idPropertyData);
        mapToMapFromId(data, getter.get(obj));
    }

    public boolean mapToEntityFromMap(Object obj, Map data) {
        if (data == null || obj == null) {
            return false;
        }

        Getter getter = ReflectionTools.getGetter(obj.getClass(), idPropertyData);
        Setter setter = ReflectionTools.getSetter(obj.getClass(), idPropertyData);

        try {
            Object subObj = ReflectHelper.getDefaultConstructor( getter.getReturnType() ).newInstance();

            boolean ret = true;
            for (IdMapper idMapper : ids.values()) {
                ret &= idMapper.mapToEntityFromMap(subObj, data);
            }

            if (ret) {
                setter.set(obj, subObj, null);
            }

            return ret;
        } catch (Exception e) {
            throw new AuditException(e);
        }
    }

    public IdMapper prefixMappedProperties(String prefix) {
        EmbeddedIdMapper ret = new EmbeddedIdMapper(idPropertyData, compositeIdClass);

        for (PropertyData propertyData : ids.keySet()) {
            String propertyName = propertyData.getName();
            ret.ids.put(propertyData, new SingleIdMapper(new PropertyData(prefix + propertyName, propertyData)));
        }

        return ret;
    }

    public Object mapToIdFromEntity(Object data) {
        if (data == null) {
            return null;
        }

        Getter getter = ReflectionTools.getGetter(data.getClass(), idPropertyData);
        return getter.get(data);
    }

    public List<QueryParameterData> mapToQueryParametersFromId(Object obj) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        mapToMapFromId(data, obj);

        List<QueryParameterData> ret = new ArrayList<QueryParameterData>();

        for (Map.Entry<String, Object> propertyData : data.entrySet()) {
            ret.add(new QueryParameterData(propertyData.getKey(), propertyData.getValue()));
        }

        return ret;
    }
}
