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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 * Created on May 11, 2005, 6:21 PM
 */
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.visitor.BoundsVisitor;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Calculates the bounds of an attribute for a given FeatureCollection
 * and Expression.
 * 
 * @author Cory Horner
 * @since 2.2M2
 *
 *
 * @source $URL$
 */
public class Collection_BoundsFunction extends FunctionExpressionImpl{
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.filter.function");
    FeatureCollection<FeatureType, Feature> previousFeatureCollection = null;
    Object bounds = null;
    
    public static FunctionName NAME = new FunctionNameImpl("Collection_Bounds",
            parameter("bounds",Object.class),
            parameter("geometry",Geometry.class));
    /**
     * Creates a new instance of Collection_BoundsFunction
     */
    public Collection_BoundsFunction() {
        super(NAME);
    }

    /**
     * Calculate unique (using FeatureCalc) - only one parameter is used.
     *
     * @param collection collection to calculate the unique
     *
     * @return An object containing the unique value of the attributes
     *
     * @throws IllegalFilterException
     * @throws IOException 
     */
    static CalcResult calculateBounds(
            FeatureCollection<? extends FeatureType, ? extends Feature> collection)
            throws IllegalFilterException, IOException {
        BoundsVisitor boundsVisitor = new BoundsVisitor();
        collection.accepts(boundsVisitor, null);

        return boundsVisitor.getResult();
    }

    public void setExpression(Expression e) {
        setParameters(Collections.singletonList(e));
    }

    /**
     * The provided arguments are evaulated with respect to the
     * FeatureCollection.
     * 
     * <p>
     * For an aggregate function (like unique) please use the WFS mandated XPath
     * syntax to refer to featureMember content.
     * </p>
     * 
     * <p>
     * To refer to all 'X': <code>featureMember/asterisk/X</code>
     * </p>
     *
     * @param args Function parameters
     *
     * @throws IllegalArgumentException If parameters do not match FunctionName
     */
    public void setParameters(List args) {
        if (args.size() != 1) {
            throw new IllegalArgumentException(
                "Require a single argument for "+getName());
        }
        
        // if we see "featureMembers/*/ATTRIBUTE" change to "ATTRIBUTE"
        org.opengis.filter.expression.Expression expr = (org.opengis.filter.expression.Expression) args.get(0);
        expr = (org.opengis.filter.expression.Expression) expr.accept(new CollectionFeatureMemberFilterVisitor(),null);
        args.set(0, expr );
        super.setParameters(args);
    }

    @SuppressWarnings("unchecked")
    public Object evaluate(Object feature) {
		if (feature == null) {
			return new Integer(0); // no features were visited in the making of this answer
		}
		FeatureCollection<FeatureType, Feature> featureCollection = (FeatureCollection<FeatureType, Feature>) feature;
		synchronized (featureCollection) {
			if (featureCollection != previousFeatureCollection) {
				previousFeatureCollection = featureCollection;
				bounds = null;
				try {
					CalcResult result = calculateBounds(featureCollection);
					if (result != null) {
						bounds = result.getValue();
					}
				} catch (IllegalFilterException e) {
					LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
				} catch (IOException e) {
					LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
				}
			}
		}
		return bounds;
    }

}
