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
package org.geotools.gml3.bindings.ext;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.gml3.bindings.SurfaceTypeBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 *
 * @source $URL$
 */
public class SurfacePropertyTypeBinding extends org.geotools.gml3.bindings.SurfacePropertyTypeBinding 
    implements Comparable {

    GeometryFactory gf;
    
    public SurfacePropertyTypeBinding(GML3EncodingUtils encodingUtils, XSDIdRegistry idRegistry, GeometryFactory gf) {
        super(encodingUtils, idRegistry);
        this.gf = gf;
    }

    public Class<? extends Geometry> getGeometryType() {
        return MultiPolygon.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Polygon polygon = (Polygon)node.getChildValue(Polygon.class);
        MultiPolygon surface = (MultiPolygon)node.getChildValue(MultiPolygon.class);

        if (polygon != null) {
            return gf.createMultiPolygon(new Polygon[] {polygon});
        } else {
            return surface;
        }
    }

    @Override
    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("_Surface".equals(name.getLocalPart()) || "AbstractSurface".equals(name.getLocalPart())) {
            MultiPolygon multiPolygon = (MultiPolygon) object;
            // this MultiPolygon consists of a single Polygon wrapped in a MultiPolygon:
            return multiPolygon.getGeometryN(0);
        }

        return super.getProperty(object, name);
    }

    public int compareTo(Object o) {
        if (o instanceof SurfaceTypeBinding) {
            return 1;
        } else {
            return 0;
        }
    }
}
