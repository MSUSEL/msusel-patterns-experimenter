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
package org.geotools.styling;

import org.opengis.filter.expression.Expression;

/**
 * A symbolizer describes how a polygon feature should appear on a map.
 *
 * <p>
 * The symbolizer describes not just the shape that should appear but also
 * such graphical properties as color and opacity.
 * </p>
 *
 * <p>
 * A symbolizer is obtained by specifying one of a small number of different
 * types of symbolizer and then supplying parameters to overide its default
 * behaviour.
 * </p>
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="PolygonSymbolizer" substitutionGroup="sld:Symbolizer">
 *    &lt;xsd:annotation>
 *      &lt;xsd:documentation>
 *        A "PolygonSymbolizer" specifies the rendering of a polygon or
 *        area geometry, including its interior fill and border stroke.
 *      &lt;/xsd:documentation>
 *    &lt;/xsd:annotation>
 *    &lt;xsd:complexType>
 *      &lt;xsd:complexContent>
 *       &lt;xsd:extension base="sld:SymbolizerType">
 *         &lt;xsd:sequence>
 *           &lt;xsd:element ref="sld:Geometry" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:Fill" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:Stroke" minOccurs="0"/>
 *         &lt;/xsd:sequence>
 *       &lt;/xsd:extension>
 *     &lt;/xsd:complexContent>
 *   &lt;/xsd:complexType>
 * &lt;/xsd:element>
 * </code></pre>
 * </p>
 *
 * <p>
 * Renderers can use this information when displaying styled features, though
 * it must be remembered that not all renderers will be able to fully
 * represent strokes as set out by this interface.  For example, opacity may
 * not be supported.
 * </p>
 *
 * <p>
 * Notes:
 *
 * <ul>
 * <li>
 * The graphical parameters and their values are derived from SVG/CSS2
 * standards with names and semantics which are as close as possible.
 * </li>
 * </ul>
 * </p>
 *
 * @author James Macgill
 *
 *
 * @source $URL$
 * @version $Id$
 */
public interface PolygonSymbolizer extends org.opengis.style.PolygonSymbolizer,Symbolizer {
    /**
     * Provides the graphical-symbolization parameter to use to fill the area
     * of the geometry. Note that the area should be filled first before the
     * outline  is rendered.
     *
     * @return The Fill style to use when rendering the area.
     */
    Fill getFill();

    /**
     * Provides the graphical-symbolization parameter to use to fill the area
     * of the geometry. Note that the area should be filled first before the
     * outline  is rendered.
     *
     * @param fill The Fill style to use when rendering the area.
     */
    void setFill(org.opengis.style.Fill fill);

    /**
     * Provides the graphical-symbolization parameter to use for the outline of
     * the Polygon.
     *
     * @return The Stroke style to use when rendering lines.
     */
    Stroke getStroke();

    /**
     * Provides the graphical-symbolization parameter to use for the outline of
     * the Polygon.
     *
     * @param stroke The Stroke style to use when rendering lines.
     */
    void setStroke(org.opengis.style.Stroke stroke);
    
    /**
     * PerpendicularOffset works as defined for LineSymbolizer, allowing to draw polygons
     * smaller or larger than their actual geometry.
     * 
     * @param offset Offset from the edge polygon positive outside; negative to the inside with a default of 0.
     */
    public void setPerpendicularOffset(Expression offset);
    
    /**
     * Displacement from the original geometry in pixels.
     *
     * @return Displacement above and to the right of the indicated point; default x=0, y=0
     */
    public Displacement getDisplacement();

    /**
     * Provide x / y offset in pixels used to crate shadows.
     * @param displacement
     */
    public void setDisplacement(org.opengis.style.Displacement displacement);    
}
