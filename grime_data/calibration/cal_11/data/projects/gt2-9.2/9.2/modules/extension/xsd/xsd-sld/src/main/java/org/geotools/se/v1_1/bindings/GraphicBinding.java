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
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import org.geotools.se.v1_1.SE;
import org.geotools.sld.bindings.SLDGraphicBinding;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.Graphic;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:Graphic.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Graphic" type="se:GraphicType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "Graphic" specifies or refers to a "graphic Symbolizer" with inherent
 *          shape, size, and coloring.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 *
 * @source $URL$
 */
public class GraphicBinding extends SLDGraphicBinding {

    public GraphicBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.Graphic;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Graphic g = (Graphic) super.parse(instance, node, value);
        if (node.hasChild(AnchorPoint.class)) {
            g.setAnchorPoint((AnchorPoint)node.getChildValue(AnchorPoint.class));
        }
        if (node.hasChild(Displacement.class)) {
            g.setDisplacement((Displacement)node.getChildValue(Displacement.class));
        }
        return g;
    }
}
