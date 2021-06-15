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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.gml3.bindings.SurfaceTypeBinding;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;


/**
 * 
 *
 * @source $URL$
 */
public class MultiSurfaceTypeBinding extends org.geotools.gml3.bindings.MultiSurfaceTypeBinding
    implements Comparable {
    
    public MultiSurfaceTypeBinding(GeometryFactory gf) {
        super(gf);
    }

    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        
// we keep the same sequence order as in the xsd:
        
        //&lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:surfaceMember"/&gt;
        List<MultiPolygon> surfaceMemberList = node.getChildValues("surfaceMember");
        //&lt;element minOccurs="0" ref="gml:surfaceMembers"/&gt;
        MultiPolygon surfaceMembers = (MultiPolygon)node.getChildValue("surfaceMembers");


        List<Polygon> polygons = new ArrayList<Polygon>();

        if (surfaceMemberList != null) {
            for (MultiPolygon surface : surfaceMemberList) {
                for (int i = 0; i < surface.getNumGeometries(); i++) {
                    Polygon polygon = (Polygon)surface.getGeometryN(i);
                    polygons.add(polygon);
                }
            }
        }

        if (surfaceMembers != null) {
            for (int i = 0; i < surfaceMembers.getNumGeometries(); i++) {
                Polygon polygon = (Polygon)surfaceMembers.getGeometryN(i);
                polygons.add(polygon);
            }
        }

        return gf.createMultiPolygon((Polygon[])polygons.toArray(new Polygon[polygons.size()]));
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("surfaceMembers".equals(name.getLocalPart())) {
            return super.getProperty(object, GML.surfaceMember);
        }
        return super.getProperty(object, name);
    }
    
    /**
     * Implement comparable because MultiPolygonBinding, MultiSurfaceBinding and Surface
     * are bound to the same class, MultiPolygon. Since MultiPolygon is deprecated
     * by gml3 and MultiSurface only has children that are also mapped to MultiPolygons,
     * Surface always wins.
     */
    public int compareTo(Object o) {
        if (o instanceof SurfaceTypeBinding) {
            return 1;
        } else {
            return 0;
        }
    }
}
