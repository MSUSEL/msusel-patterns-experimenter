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
package org.geotools.gml2.bindings;

import java.net.URI;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.geotools.gml2.GML;
import org.geotools.referencing.CRS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Binding object for the type http://www.opengis.net/gml:AbstractGeometryType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="AbstractGeometryType" abstract="true"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         All geometry elements are derived from
 *              this abstract supertype;          a geometry element may
 *              have an identifying attribute (gid).          It may be
 *              associated with a spatial reference system.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;restriction base="anyType"&gt;
 *              &lt;attribute name="gid" type="ID" use="optional"/&gt;
 *              &lt;attribute name="srsName" type="anyURI" use="optional"/&gt;
 *          &lt;/restriction&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class GMLAbstractGeometryTypeBinding extends AbstractComplexBinding {
    Logger logger;

    public GMLAbstractGeometryTypeBinding(Logger logger) {
        this.logger = logger;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.ABSTRACTGEOMETRYTYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Geometry.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        if (value instanceof Geometry) {
            Geometry geometry = (Geometry) value;

            //&lt;attribute name="srsName" type="anyURI" use="optional"/&gt;
            if (node.hasAttribute("srsName")) {
                URI srs = (URI) node.getAttributeValue("srsName");
                CoordinateReferenceSystem crs = CRS.decode(srs.toString());

                if (crs != null) {
                    geometry.setUserData(crs);
                } else {
                    logger.warning("Could not create Coordinate Reference System for " + srs);
                }
            }

            //TODO: process the ID attribute
        }

        return value;
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("srsName".equals(name.getLocalPart())) {
            Geometry geometry = (Geometry) object;

            if (geometry.getUserData() instanceof CoordinateReferenceSystem) {
                return GML2EncodingUtils.crs((CoordinateReferenceSystem) geometry.getUserData());
            }
        }

        return null;
    }
}
