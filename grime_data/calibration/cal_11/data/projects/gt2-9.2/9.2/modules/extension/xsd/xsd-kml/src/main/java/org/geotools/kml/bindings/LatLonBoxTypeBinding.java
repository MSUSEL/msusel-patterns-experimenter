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
package org.geotools.kml.bindings;

import javax.xml.namespace.QName;
import com.vividsolutions.jts.geom.Envelope;
import org.geotools.kml.KML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:LatLonBoxType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="LatLonBoxType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:ObjectType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;&lt;![CDATA[Yes, north/south range to 180/-180]]&gt;&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *                  &lt;element default="180.0" minOccurs="0" name="north" type="kml:angle180"/&gt;
 *                  &lt;element default="-180.0" minOccurs="0" name="south" type="kml:angle180"/&gt;
 *                  &lt;element default="180.0" minOccurs="0" name="east" type="kml:angle180"/&gt;
 *                  &lt;element default="-180.0" minOccurs="0" name="west" type="kml:angle180"/&gt;
 *                  &lt;element default="0" minOccurs="0" name="rotation" type="kml:angle180"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
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
public class LatLonBoxTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return KML.LatLonBoxType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Envelope.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //&lt;element default="180.0" minOccurs="0" name="north" type="kml:angle180"/&gt;
        Double n = (Double) node.getChildValue("north", Double.valueOf(180d));

        //&lt;element default="-180.0" minOccurs="0" name="south" type="kml:angle180"/&gt;
        Double s = (Double) node.getChildValue("south", Double.valueOf(-180d));

        //&lt;element default="180.0" minOccurs="0" name="east" type="kml:angle180"/&gt;
        Double e = (Double) node.getChildValue("east", Double.valueOf(180d));

        //&lt;element default="-180.0" minOccurs="0" name="west" type="kml:angle180"/&gt;
        Double w = (Double) node.getChildValue("west", Double.valueOf(-180d));

        return new Envelope(w.doubleValue(), e.doubleValue(), s.doubleValue(), n.doubleValue());
    }
}
