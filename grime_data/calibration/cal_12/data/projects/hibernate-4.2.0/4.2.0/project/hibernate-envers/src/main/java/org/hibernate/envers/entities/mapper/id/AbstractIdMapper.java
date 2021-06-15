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
import java.util.Iterator;
import java.util.List;

import org.hibernate.envers.tools.query.Parameters;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public abstract class AbstractIdMapper implements IdMapper {
    private Parameters getParametersToUse(Parameters parameters, List<QueryParameterData> paramDatas) {
         if (paramDatas.size() > 1) {
            return parameters.addSubParameters("and");
        } else {
            return parameters;
        }
    }

    public void addIdsEqualToQuery(Parameters parameters, String prefix1, String prefix2) {
        List<QueryParameterData> paramDatas = mapToQueryParametersFromId(null);

        Parameters parametersToUse = getParametersToUse(parameters, paramDatas);

        for (QueryParameterData paramData : paramDatas) {
            parametersToUse.addWhere(paramData.getProperty(prefix1), false, "=", paramData.getProperty(prefix2), false);
        }
    }

    public void addIdsEqualToQuery(Parameters parameters, String prefix1, IdMapper mapper2, String prefix2) {
        List<QueryParameterData> paramDatas1 = mapToQueryParametersFromId(null);
        List<QueryParameterData> paramDatas2 = mapper2.mapToQueryParametersFromId(null);

        Parameters parametersToUse = getParametersToUse(parameters, paramDatas1);

        Iterator<QueryParameterData> paramDataIter1 = paramDatas1.iterator();
        Iterator<QueryParameterData> paramDataIter2 = paramDatas2.iterator();
        while (paramDataIter1.hasNext()) {
            QueryParameterData paramData1 = paramDataIter1.next();
            QueryParameterData paramData2 = paramDataIter2.next();

            parametersToUse.addWhere(paramData1.getProperty(prefix1), false, "=", paramData2.getProperty(prefix2), false); 
        }
    }

    public void addIdEqualsToQuery(Parameters parameters, Object id, String prefix, boolean equals) {
        List<QueryParameterData> paramDatas = mapToQueryParametersFromId(id);

        Parameters parametersToUse = getParametersToUse(parameters, paramDatas);

        for (QueryParameterData paramData : paramDatas) {
            if (paramData.getValue() == null) {
                handleNullValue(parametersToUse, paramData.getProperty(prefix), equals);
            } else {
                parametersToUse.addWhereWithParam(paramData.getProperty(prefix), equals ? "=" : "<>", paramData.getValue());
            }
        }
    }

    public void addNamedIdEqualsToQuery(Parameters parameters, String prefix, boolean equals) {
        List<QueryParameterData> paramDatas = mapToQueryParametersFromId(null);

        Parameters parametersToUse = getParametersToUse(parameters, paramDatas);

        for (QueryParameterData paramData : paramDatas) {
            parametersToUse.addWhereWithNamedParam(paramData.getProperty(prefix), equals ? "=" : "<>", paramData.getQueryParameterName());
        }
    }

    private void handleNullValue(Parameters parameters, String propertyName, boolean equals) {
        if (equals) {
            parameters.addNullRestriction(propertyName, equals);
        } else {
            parameters.addNotNullRestriction(propertyName, equals);
        }
    }
}
