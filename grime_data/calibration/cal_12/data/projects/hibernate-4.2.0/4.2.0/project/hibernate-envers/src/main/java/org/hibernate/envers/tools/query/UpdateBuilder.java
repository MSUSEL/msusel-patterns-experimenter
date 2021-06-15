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
package org.hibernate.envers.tools.query;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.envers.tools.MutableInteger;
import org.hibernate.envers.tools.Pair;
import org.hibernate.envers.tools.StringTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class UpdateBuilder {
    private final String entityName;
    private final String alias;
    private final MutableInteger paramCounter;
    private final Parameters rootParameters;
    private final Map<String, Object> updates;

    public UpdateBuilder(String entityName, String alias) {
        this(entityName, alias, new MutableInteger());
    }

    private UpdateBuilder(String entityName, String alias, MutableInteger paramCounter) {
        this.entityName = entityName;
        this.alias = alias;
        this.paramCounter = paramCounter;
        rootParameters = new Parameters(alias, "and", paramCounter);
        updates = new HashMap<String, Object>();
    }

    public Parameters getRootParameters() {
        return rootParameters;
    }

    public void updateValue(String propertyName, Object value) {
        updates.put(propertyName, value);
    }

    public void build(StringBuilder sb, Map<String, Object> updateParamValues) {
        sb.append("update ").append(entityName).append(" ").append(alias);
        sb.append(" set ");
        int i = 1;
        for (String property : updates.keySet()) {
            final String paramName = generateParameterName();
            sb.append(alias).append(".").append(property).append(" = ").append(":").append(paramName);
            updateParamValues.put(paramName, updates.get(property));
            if (i < updates.size()) {
                sb.append(", ");
            }
            ++i;
        }
        if (!rootParameters.isEmpty()) {
            sb.append(" where ");
            rootParameters.build(sb, updateParamValues);
        }
    }

    private String generateParameterName() {
        return "_u" + paramCounter.getAndIncrease();
    }

    public Query toQuery(Session session) {
        StringBuilder querySb = new StringBuilder();
        Map<String, Object> queryParamValues = new HashMap<String, Object>();

        build(querySb, queryParamValues);

        Query query = session.createQuery(querySb.toString());
        for (Map.Entry<String, Object> paramValue : queryParamValues.entrySet()) {
            query.setParameter(paramValue.getKey(), paramValue.getValue());
        }

        return query;
    }
}
