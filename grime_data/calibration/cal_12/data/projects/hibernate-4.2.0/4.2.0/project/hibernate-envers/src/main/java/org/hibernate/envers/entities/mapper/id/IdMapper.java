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
import java.util.List;
import java.util.Map;

import org.hibernate.envers.tools.query.Parameters;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public interface IdMapper {
    void mapToMapFromId(Map<String, Object> data, Object obj);

    void mapToMapFromEntity(Map<String, Object> data, Object obj);

    /**
     * @param obj Object to map to.
     * @param data Data to map.
     * @return True if data was mapped; false otherwise (when the id is {@code null}).
     */
    boolean mapToEntityFromMap(Object obj, Map data);

    Object mapToIdFromEntity(Object data);

    Object mapToIdFromMap(Map data);

    /**
     * Creates a mapper with all mapped properties prefixed. A mapped property is a property which
     * is directly mapped to values (not composite).
     * @param prefix Prefix to add to mapped properties
     * @return A copy of the current property mapper, with mapped properties prefixed.
     */
    IdMapper prefixMappedProperties(String prefix);

    /**
     * @param obj Id from which to map.
     * @return A set parameter data, needed to build a query basing on the given id.
     */
    List<QueryParameterData> mapToQueryParametersFromId(Object obj);

    /**
     * Adds query statements, which contains restrictions, which express the property that the id of the entity
     * with alias prefix1, is equal to the id of the entity with alias prefix2 (the entity is the same).
     * @param parameters Parameters, to which to add the statements.
     * @param prefix1 First alias of the entity + prefix to add to the properties.
     * @param prefix2 Second alias of the entity + prefix to add to the properties.
     */
    void addIdsEqualToQuery(Parameters parameters, String prefix1, String prefix2);

    /**
     * Adds query statements, which contains restrictions, which express the property that the id of the entity
     * with alias prefix1, is equal to the id of the entity with alias prefix2 mapped by the second mapper
     * (the second mapper must be for the same entity, but it can have, for example, prefixed properties).
     * @param parameters Parameters, to which to add the statements.
     * @param prefix1 First alias of the entity + prefix to add to the properties.
     * @param mapper2 Second mapper for the same entity, which will be used to get properties for the right side
     * of the equation.
     * @param prefix2 Second alias of the entity + prefix to add to the properties.
     */
    void addIdsEqualToQuery(Parameters parameters, String prefix1, IdMapper mapper2, String prefix2);

    /**
     * Adds query statements, which contains restrictions, which express the property that the id of the entity
     * with alias prefix, is equal to the given object.
     * @param parameters Parameters, to which to add the statements.
     * @param id Value of id.
     * @param prefix Prefix to add to the properties (may be null).
     * @param equals Should this query express the "=" relation or the "<>" relation.
     */
    void addIdEqualsToQuery(Parameters parameters, Object id, String prefix, boolean equals);

    /**
     * Adds query statements, which contains named parameters, which express the property that the id of the entity
     * with alias prefix, is equal to the given object. It is the responsibility of the using method to read
     * parameter values from the id and specify them on the final query object.
     * @param parameters Parameters, to which to add the statements.
     * @param prefix Prefix to add to the properties (may be null).
     * @param equals Should this query express the "=" relation or the "<>" relation.
     */
    void addNamedIdEqualsToQuery(Parameters parameters, String prefix, boolean equals);
}
