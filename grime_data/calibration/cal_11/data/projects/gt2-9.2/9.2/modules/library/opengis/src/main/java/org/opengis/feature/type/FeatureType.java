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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature.type;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The type of a Feature.
 * <p>
 * Beyond a complex type, a feature defines some additional information:
 * <ul>
 *   <li>The default geometric attribute
 *   <li>The coordinate referencing system (derived from the default geometry)
 * </ul>
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public interface FeatureType extends ComplexType {

    /**
     * Features are always identified.
     *
     * @return <code>true</code>
     */
    boolean isIdentified();

    /**
     * Describe the default geometric attribute for this feature.
     * <p>
     * This method returns <code>null</code> in the case where no such attribute
     * exists.
     * </p>
     * @return The descriptor of the default geometry attribute, or <code>null</code>.
     */
    GeometryDescriptor getGeometryDescriptor();

    /**
     * The coordinate reference system of the feature.
     * <p>
     * This value is derived from the default geometry attribute:
     * <pre>
     *   ((GeometryType)getDefaultGeometry().getType()).getCRS();
     * </pre>
     * </p>
     * <p>
     * This method will return <code>null</code> in the case where no default
     * geometric attribute is defined.
     * </p>
     * @return The coordinate referencing system, or <code>null</code>.
     */
    CoordinateReferenceSystem getCoordinateReferenceSystem();
}
