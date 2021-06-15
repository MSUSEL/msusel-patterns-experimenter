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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0.bindings;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.gml3.v3_2.GML;
import org.geotools.xml.Node;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Utility class for FES bindings.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class FESParseEncodeUtil {

    /**
     * Parses the two operands for a binary temporal filter.
     */
    static Expression[] temporal(Node node, FilterFactory factory) {
        PropertyName name = (PropertyName) node.getChildValue(PropertyName.class);
        Object other = null;
        for (Object o : node.getChildValues(Object.class)) {
            if (o == name) {
                continue;
            }
            
            other = o;
            break;
        }
        
        if (other == null) {
           throw new IllegalArgumentException("Temporal filter did not specify two operands");
        }
        
        Expression expr = null;
        if (other instanceof Expression) {
            expr = (Expression) other;
        }
        else {
            expr = factory.literal(other);
        }
        
        return new Expression[]{name, expr};
    }
    
    static Expression getProperty(BinaryTemporalOperator op, QName name) {
        return getProperty(op.getExpression1(), op.getExpression2(), name);
    }
    static Expression getProperty(BinarySpatialOperator op, QName name) {
        return getProperty(op.getExpression1(), op.getExpression2(), name);
    }

    static List getProperties(DistanceBufferOperator op) {
        List l = new ArrayList();
        l.add(distanceBufferOpProperty(op.getExpression1()));
        l.add(distanceBufferOpProperty(op.getExpression2()));
        
        l.add(new Object[]{new QName(FES.NAMESPACE, "Distance"), op.getDistance()});
        return l;
    }

    static Object[] distanceBufferOpProperty(Expression e) {
        if (e instanceof PropertyName) {
            return new Object[]{FES.ValueReference, e};
        }
        else if (e instanceof Literal) {
            Literal l = (Literal) e;
            if (l.getValue() instanceof Geometry) {
                Geometry g = (Geometry) l.getValue();
                return new Object[]{new QName(GML.NAMESPACE, g.getGeometryType()), g}; 
            }
            return new Object[]{FES.Literal, e};
        }
        else if (e instanceof Function) {
            return new Object[]{FES.Function, e};
        }
        else {
            return new Object[]{FES.expression, e};
        }
    }
    static Expression getProperty(Expression e1, Expression e2, QName name) {
        if (FES.ValueReference.equals(name)) {
            if (e1 instanceof PropertyName) {
                return e1;
            }
            else if (e2 instanceof PropertyName) {
                return e2;
            }
        }
        if (FES.expression.equals(name)) {
            if (e1 instanceof PropertyName) {
                return e2;
            }
            else if (e2 instanceof PropertyName) {
                return e1;
            }
        }
        
        return null;
    }
}
