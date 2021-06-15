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
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.extent;

import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.datum.VerticalDatum;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Vertical domain of dataset.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 1.0
 */
@UML(identifier="EX_VerticalExtent", specification=ISO_19115)
public interface VerticalExtent {
    /**
     * Returns the lowest vertical extent contained in the dataset.
     *
     * @return Double mandatory for valid content, may be null for an invalid document.
     */
    @UML(identifier="minimumValue", obligation=MANDATORY, specification=ISO_19115)
    Double getMinimumValue();

    /**
     * Returns the highest vertical extent contained in the dataset.
     *
     * @return Double mandatory for valid content, may be null for an invalid document.
     */
    @UML(identifier="maximumValue", obligation=MANDATORY, specification=ISO_19115)
    Double getMaximumValue();

    /**
     * Provides information about the vertical coordinate reference system to
     * which the maximum and minimum elevation values are measured. The CRS
     * identification includes unit of measure.
     *
     * @departure
     *   ISO 19115 specifies a generic {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem
     *   Coordinate Reference System} instead than the more restrictive {@linkplain VerticalCRS Vertical
     *   CRS}. It may be because ISO 19111 does not allows vertical CRS to express height above the ellipsoid,
     *   so the full three-dimensional {@linkplain org.opengis.referencing.crs.GeographicCRS Geographic CRS}
     *   is needed in such case. However GeoAPI allows such vertical CRS since it imported the
     *   {@linkplain org.opengis.referencing.datum.VerticalDatumType#ELLIPSOIDAL ellipsoidal vertical datum type}
     *   from OGC 01-009. Giving this capability, this method returns a vertical CRS in accordance
     *   with the method name, documentation and historical version of ISO 19115 which used
     *   {@linkplain org.opengis.referencing.datum.VerticalDatum vertical datum}.
     *
     * @issue http://jira.codehaus.org/browse/GEO-134
     *
     * @return The vertical CRS.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="verticalCRS", obligation=MANDATORY, specification=ISO_19115)
    VerticalCRS getVerticalCRS();
}
