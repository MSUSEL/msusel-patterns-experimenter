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
import org.hibernate.internal.util.ReflectHelper;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class MultipleIdMapper extends AbstractCompositeIdMapper implements SimpleIdMapperBuilder {
    public MultipleIdMapper(String compositeIdClass) {
        super(compositeIdClass);
    }

    public void mapToMapFromId(Map<String, Object> data, Object obj) {
        for (IdMapper idMapper : ids.values()) {
            idMapper.mapToMapFromEntity(data, obj);
        }
    }

    public void mapToMapFromEntity(Map<String, Object> data, Object obj) {
        mapToMapFromId(data, obj);
    }

    public boolean mapToEntityFromMap(Object obj, Map data) {
        boolean ret = true;
        for (IdMapper idMapper : ids.values()) {
            ret &= idMapper.mapToEntityFromMap(obj, data);
        }

        return ret;
    }

    public IdMapper prefixMappedProperties(String prefix) {
        MultipleIdMapper ret = new MultipleIdMapper(compositeIdClass);

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

        Object ret;
        try {
            final Class clazz = ReflectHelper.classForName(compositeIdClass);
            ret = ReflectHelper.getDefaultConstructor(clazz).newInstance();
        } catch (Exception e) {
            throw new AuditException(e);
        }

        for (SingleIdMapper mapper : ids.values()) {
            mapper.mapToEntityFromEntity(ret, data);
        }

        return ret;
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
