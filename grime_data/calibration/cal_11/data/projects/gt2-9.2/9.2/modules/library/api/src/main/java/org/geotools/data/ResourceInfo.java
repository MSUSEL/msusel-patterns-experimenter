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
 */
package org.geotools.data;

import java.net.URI;
import java.util.Set;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * This interface defines methods to convey information about some resource such as
 * title, keywords, description and spatial parameters.
 * <p>
 * It is based on Dublin Core (a metadata specification initiative;
 * http://dublincore.org/) and the RDF application profile.
 * <p>
 * There are two ids that may be associated with a resource:
 * <ul>
 * <li>name - unqiue within the context of a Service
 * <li>schema - used to identify the type of resource
 * </ul>
 * 
 * @author Jody Garnett, Refractions Research
 * @author David Zwiers, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.5
 *
 *
 *
 * @source $URL$
 */
public interface ResourceInfo {
    /**
     * Returns the resource's title.
     * <p>
     * The title is human readable text representing the resource, in the 
     * current locale if available.
     * <p>
     * @return tile, in the current locale if available.
     */
    String getTitle();

    /**
     * Returns keywords associated with this resource for use with searches
     * etc.
     * <p>
     * Known Mappings:
     * <ul>
     * <li> Maps to Dublin Core's Subject element
     * </ul>
     * </p>
     * @return Keywords or {@code null} if unavailable
     */
    Set<String> getKeywords();

    /**
     * Returns a description or abstract for this resource.
     * <p>
     * Known Mappings:
     * <ul>
     * <li>WFS GetCapabilities abstract
     * <li>WMS GetCapabilities abstract
     * </ul>
     * </p>
     * The description may be in the current locale if known.
     * 
     * @return description of resource, or <code>null</code> if unavailable
     */
    String getDescription();

    /**
     * Returns the name of this resource within the context of its service.
     * <p>
     * Known mappings:
     * <ul>
     * <li>WFS typeName
     * <li>Database table name
     * <li>WMS layer name
     * <li>level of a grid coverage
     * </ul>
     * 
     * The name should be unique within the context of a single Service.
     * 
     * @return name of this resource
     */
    String getName();
    
    /**
     * A namespace, in the form of a {@code URI}, used to identify the resource type.
     * <p>
     * Known Mappings:
     * <ul>
     * <li>Dublin Code Format element
     * <li>WFS DescribeFeatureType URL
     * <li>file.toURI()
     * <li>XML namespace
     * <li>URL
     * </ul>
     * 
     * @return namespace, used with getName() to identify resource type
     */
    URI getSchema();

    /**
     * Returns the bounds of the resource, expressed in the native coordinate
     * reference system. IF the bounds are unknown or undefined calling {@code isNull()}
     * on the returned envelope will return {@code true}.
     * 
     * @return bounds of the resource if defined; otherwise an envelope where the
     *         {@code isNull()} returns {@code true}
     */
    ReferencedEnvelope getBounds();

    /**
     * Returns the coordinate reference system of this resource if known.
     * <p>
     * Known Mappings:
     * <ul>
     * <li>2nd part of the Dublin Core Coverage
     * <li>Shapefile prj file
     * <li>WFS SRS
     * </ul>
     * </p>
     *
     * @return CRS of the resource, or {@code null} if unavailable.
     */
    CoordinateReferenceSystem getCRS();
    
    /*
     * mbedward : removed from javadocs
     *
     * This method was considered because some services maintain this
     * information as metadata. If it was a bit more common (and axis order more consistent)
     * it would be worth considering.
     * <p>
     * Known Mappings:
     * <ul>
     * <li>1st part of the Dublin Core Coverage
     * <li>wfs LatLongBbox
     * </ul>
     * </p>
     */
    // ReferencedEnvelope getLatLongBbox();
}
