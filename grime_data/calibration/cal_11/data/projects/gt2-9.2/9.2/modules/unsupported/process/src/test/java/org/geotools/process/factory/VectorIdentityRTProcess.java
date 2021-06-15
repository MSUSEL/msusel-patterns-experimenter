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
package org.geotools.process.factory;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.process.ProcessException;
import org.geotools.process.gs.GSProcess;
import org.opengis.coverage.grid.GridGeometry;

/**
 * A simple Rendering Transformation process for testing aspects of how transformations are called.
 * 
 * 
 * @author Martin Davis - OpenGeo
 * 
 */
@DescribeProcess(title = "SimpleVectorRTProcess", description = "Simple test RT process taking a vector dataset as input.")
public class VectorIdentityRTProcess implements GSProcess {
    /**
     * Note: for testing purposes only. A real Rendering Transformation must never store state.
     */
    int invertQueryValue;

    @DescribeResult(name = "result", description = "The result")
    public SimpleFeatureCollection execute(
            // process data
            @DescribeParameter(name = "data", description = "Features to process") SimpleFeatureCollection data,
            @DescribeParameter(name = "value", description = "Value for testing") Integer value)
            throws ProcessException {
        if (value != invertQueryValue) {
            throw new IllegalStateException("Values do not match");
        }
        return data;
    }

    public Query invertQuery(
            @DescribeParameter(name = "value", description = "Value for testing") Integer value,
            Query targetQuery, GridGeometry targetGridGeometry) throws ProcessException {

        invertQueryValue = value;

        return targetQuery;
    }
}