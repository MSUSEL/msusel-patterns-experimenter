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
package org.hibernate.envers.entities.mapper.relation.component;
import java.util.Map;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.entities.EntityInstantiator;
import org.hibernate.envers.entities.mapper.relation.MiddleIdData;
import org.hibernate.envers.tools.query.Parameters;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public final class MiddleRelatedComponentMapper implements MiddleComponentMapper {
    private final MiddleIdData relatedIdData;

    public MiddleRelatedComponentMapper(MiddleIdData relatedIdData) {
        this.relatedIdData = relatedIdData;
    }

    public Object mapToObjectFromFullMap(EntityInstantiator entityInstantiator, Map<String, Object> data,
                                         Object dataObject, Number revision) {
        return entityInstantiator.createInstanceFromVersionsEntity(relatedIdData.getEntityName(), data, revision);
    }

    public void mapToMapFromObject(SessionImplementor session, Map<String, Object> idData, Map<String, Object> data, Object obj) {
        relatedIdData.getPrefixedMapper().mapToMapFromEntity(idData, obj);
    }

    public void addMiddleEqualToQuery(Parameters parameters, String idPrefix1, String prefix1, String idPrefix2, String prefix2) {
        relatedIdData.getPrefixedMapper().addIdsEqualToQuery(parameters, idPrefix1, idPrefix2);
    }
}
