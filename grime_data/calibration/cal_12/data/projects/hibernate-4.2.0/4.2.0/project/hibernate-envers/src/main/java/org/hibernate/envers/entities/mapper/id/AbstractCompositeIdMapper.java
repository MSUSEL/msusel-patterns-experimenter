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
import java.util.Map;

import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.tools.Tools;
import org.hibernate.internal.util.ReflectHelper;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public abstract class AbstractCompositeIdMapper extends AbstractIdMapper implements SimpleIdMapperBuilder {
    protected Map<PropertyData, SingleIdMapper> ids;
    protected String compositeIdClass;

    protected AbstractCompositeIdMapper(String compositeIdClass) {
        ids = Tools.newLinkedHashMap();
        
        this.compositeIdClass = compositeIdClass;
    }

    public void add(PropertyData propertyData) {
        ids.put(propertyData, new SingleIdMapper(propertyData));
    }

    public Object mapToIdFromMap(Map data) {
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
            if (!mapper.mapToEntityFromMap(ret, data)) {
                return null;
            }
        }

        return ret;
    }
}
