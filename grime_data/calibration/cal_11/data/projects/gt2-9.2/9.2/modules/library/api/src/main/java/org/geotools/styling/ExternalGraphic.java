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
 *
 */
package org.geotools.styling;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;



/**
 * Holds a reference to an external graphics file with a URL to its location
 * and its expected MIME type. Knowing the MIME type in advance allows stylers
 * to select best-supported formats from a list of external graphics.
 *
 * <p></p>
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="ExternalGraphic"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       An "ExternalGraphic" gives a reference to an external raster or
 *       vector graphical object.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element ref="sld:OnlineResource"/&gt;
 *       &lt;xsd:element ref="sld:Format"/&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
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
 * @author James Macgill, CCG
 *
 *
 * @source $URL$
 * @version $Id$
 */
public interface ExternalGraphic extends org.opengis.style.ExternalGraphic,Symbol {
    public static final ExternalGraphic[] EXTERNAL_GRAPHICS_EMPTY = new ExternalGraphic[0];

    /**
     * Converts a URI in a string to the location URL
     *
     * @param uri the uri of the external graphic
     */
    public void setURI(String uri);

    /**
     * Provides the URL for where the external graphic resource can be located.
     * <p>
     * This method will be replaced by getOnlineResource().getLinkage() in 2.6.x
     * 
     * @return The URL of the ExternalGraphic
     *
     * @throws MalformedURLException If the url held in the ExternalGraphic is
     *         malformed.
     */
    URL getLocation() throws MalformedURLException;

    /**
     * Provides the URL for where the external graphic resource can be located.
     *
     * @param url The URL of the ExternalGraphic
     */
    void setLocation(URL url);

    /**
     * Provides the format of the external graphic.
     *
     * @param format The format of the external graphic.  Reported as its MIME
     *        type in a String object.
     */
    void setFormat(String format);

    /**
     * Custom properties; renderer may consult these values when drawing graphic.
     * <p>
     * The default GeoTools renderer uses the following:
     * <ul>
     * <li>radius: 50
     * <li>circle color: #000066
     * <li>bar height:150
     * <li>bar color:"#000000
     * <li>bar uncertainty:50
     * <li>bar uncertainty width:5
     * <li>bar uncertainty color:"#999999
     * <li>pointer length:100
     * <li>pointer color: #FF0000
     * <li>pointer direction: 21
     * <li>wedge width: 25
     * <li>wedge color: #9999FF"
     * </ul>
     * 
     * @param properties
     */
    public void setCustomProperties(Map<String,Object> properties);
    
    /**
     * Custom user supplied properties available when working with an external graphic.
     * 
     * @return properties
     */
    public Map<String,Object> getCustomProperties();
}
