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
package org.geotools.renderer.lite;

import static org.geotools.filter.capability.FunctionNameImpl.*;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.referencing.CRS;
import org.opengis.filter.capability.FunctionName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A rendering transformation that reprojects a feature collection
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class ReprojectCollectionFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("ReprojectCollection", parameter("crs",
            String.class));

    public ReprojectCollectionFunction() {
        super(NAME);
    }

    @Override
    public int getArgCount() {
        return -1;
    }

    public Object evaluate(Object object) {
        String targetCRS = getAttribute(object, 0, String.class, true);
        try {
            CoordinateReferenceSystem crs = CRS.decode(targetCRS);
            
            return new ReprojectingFeatureCollection((SimpleFeatureCollection) object, crs);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reproject the collection");
        }
    }

    <T> T getAttribute(Object object, int expressionIdx, Class<T> targetClass, boolean mandatory) {
        try { // attempt to get value and perform conversion
            T result = getExpression(expressionIdx).evaluate(object, targetClass);
            if (result == null && mandatory) {
                throw new IllegalArgumentException("Could not find function argument #"
                        + expressionIdx + ", but it's mandatory");
            }
            return result;
        } catch (Exception e) {
            // probably a type error
            if (mandatory) {
                throw new IllegalArgumentException("Could not find function argument #"
                        + expressionIdx + ", but it's mandatory");
            } else {
                return null;
            }
        }

    }

}
