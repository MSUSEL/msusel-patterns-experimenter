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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;

/**
 * Renames the specified attribute to a new target name  
 * @author Andrea Aime - GeoSolutions
 */
class AttributeRenameVisitor extends DuplicatingFilterVisitor {

    String source;
    String target;
    
    public AttributeRenameVisitor(String source, String target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        final String propertyName = expression.getPropertyName();
        if(propertyName != null && propertyName.equals(source)) {
            return getFactory(extraData).property(target);
        } else {
            return super.visit(expression, extraData);
        }
    }
    
    @Override
    public Object visit(BBOX filter, Object extraData) {
        // rename if necessary
        Expression e1 = filter.getExpression1();
        if(e1 instanceof PropertyName) {
            PropertyName pname = (PropertyName) e1;
            String name = pname.getPropertyName();
            if(name != null && name.equals(source)) {
                e1 = ff.property(target);
            } 
        }
        
        // duplicate preserving fast bbox filters
        if(filter instanceof FastBBOX && e1 instanceof PropertyName) {
            FastBBOX fbox = (FastBBOX) filter;
            return new FastBBOX((PropertyName) e1, fbox.getEnvelope(), getFactory(extraData));
        } else {
            double minx=filter.getMinX();
            double miny=filter.getMinY();
            double maxx=filter.getMaxX();
            double maxy=filter.getMaxY();
            String srs=filter.getSRS();
            return getFactory(extraData).bbox(e1, minx, miny, maxx, maxy, srs);
        }
    }
    
}
