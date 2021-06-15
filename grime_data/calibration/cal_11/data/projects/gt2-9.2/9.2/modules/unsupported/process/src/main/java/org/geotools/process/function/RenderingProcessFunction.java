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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.process.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.data.Query;
import org.geotools.filter.function.RenderingTransformation;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.process.RenderingProcess;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * A function wrapping a {@link Process} with a single output. All inputs to the function are
 * supposed to evaluate to Map<String, Object> where the key is the name of an argument and the
 * value is the argument value
 * 
 * @author Andrea Aime - GeoSolutions
 * @author Daniele Romagnoli - GeoSolutions
 */
class RenderingProcessFunction extends ProcessFunction implements RenderingTransformation {

    public RenderingProcessFunction(String name, Name processName, List<Expression> inputExpressions,
            Map<String, Parameter<?>> parameters, RenderingProcess process, Literal fallbackValue) {
        super(name, processName, inputExpressions, parameters, process, fallbackValue);
    }

    public Query invertQuery(Query targetQuery, GridGeometry gridGeometry) {
        RenderingProcess process = (RenderingProcess) this.process;
        // evaluate input expressions
        // at this point do not have an object to evaluate them against
        Map<String, Object> inputs = evaluateInputs(null);
        try {
            return process.invertQuery(inputs, targetQuery, gridGeometry);
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to invert the query, error is: "
                    + e.getMessage(), e);
        }
    }

    public GridGeometry invertGridGeometry(Query targetQuery, GridGeometry targetGridGeometry) {
        RenderingProcess process = (RenderingProcess) this.process;
        // evaluate input expressions
        // at this point do not have an object to evaluate them against
        Map<String, Object> inputs = evaluateInputs(null);
        try {
            return process.invertGridGeometry(inputs, targetQuery, targetGridGeometry);
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to invert the grid geometry, error is: "
                    + e.getMessage(), e);
        }
    }

}
