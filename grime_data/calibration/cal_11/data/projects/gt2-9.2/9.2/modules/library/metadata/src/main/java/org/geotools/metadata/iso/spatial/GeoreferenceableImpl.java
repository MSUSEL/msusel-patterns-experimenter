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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.spatial;

import java.util.List;
import java.util.Collection;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.spatial.CellGeometry;
import org.opengis.metadata.spatial.Dimension;
import org.opengis.metadata.spatial.Georeferenceable;
import org.opengis.util.InternationalString;
import org.opengis.util.Record;


/**
 * Grid with cells irregularly spaced in any given geographic/map projection coordinate
 * system, whose individual cells can be geolocated using geolocation information
 * supplied with the data but cannot be geolocated from the grid properties alone.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class GeoreferenceableImpl extends GridSpatialRepresentationImpl implements Georeferenceable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 5203270142818028946L;

    /**
     * Indication of whether or not control point(s) exists.
     */
    private boolean controlPointAvailable;

    /**
     * Indication of whether or not orientation parameters are available.
     */
    private boolean orientationParameterAvailable;

    /**
     * Description of parameters used to describe sensor orientation.
     */
    private InternationalString orientationParameterDescription;

    /**
     * Terms which support grid data georeferencing.
     */
    private Record georeferencedParameters;

    /**
     * Reference providing description of the parameters.
     */
    private Collection<Citation> parameterCitation;

    /**
     * Constructs an initially empty georeferenceable.
     */
    public GeoreferenceableImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public GeoreferenceableImpl(final Georeferenceable source) {
        super(source);
    }

    /**
     * Creates a georeferencable initialized to the given parameters.
     */
    public GeoreferenceableImpl(final int numberOfDimensions,
                                final List<? extends Dimension> axisDimensionsProperties,
                                final CellGeometry cellGeometry,
                                final boolean transformationParameterAvailable,
                                final boolean controlPointAvailable,
                                final boolean orientationParameterAvailable)
    {
        super(numberOfDimensions, axisDimensionsProperties, cellGeometry, transformationParameterAvailable);
        setControlPointAvailable        (controlPointAvailable        );
        setOrientationParameterAvailable(orientationParameterAvailable);
    }

    /**
     * Indication of whether or not control point(s) exists.
     */
    public boolean isControlPointAvailable() {
        return controlPointAvailable;
    }

    /**
     * Set an indication of whether or not control point(s) exists.
     */
    public synchronized void setControlPointAvailable(final boolean newValue) {
       checkWritePermission();
       controlPointAvailable = newValue;
    }

    /**
     * Indication of whether or not orientation parameters are available.
     */
    public boolean isOrientationParameterAvailable() {
        return orientationParameterAvailable;
    }

    /**
     * Set an indication of whether or not orientation parameters are available.
     */
    public synchronized void setOrientationParameterAvailable(final boolean newValue) {
        checkWritePermission();
        orientationParameterAvailable = newValue;
    }

    /**
     * Description of parameters used to describe sensor orientation.
     */
    public InternationalString getOrientationParameterDescription() {
        return orientationParameterDescription;
    }

    /**
     * Set a description of parameters used to describe sensor orientation.
     */
    public synchronized void setOrientationParameterDescription(final InternationalString newValue) {
        checkWritePermission();
        orientationParameterDescription = newValue;
    }

    /**
     * Terms which support grid data georeferencing.
     *
     * @since 2.4
     */
    public Record getGeoreferencedParameters() {
        return georeferencedParameters;
    }

    /**
     * Set terms which support grid data georeferencing.
     *
     * @since 2.4
     */
    public synchronized void setGeoreferencedParameters(final Record newValue) {
        checkWritePermission();
        georeferencedParameters = newValue;
    }

    /**
     * Reference providing description of the parameters.
     */
    public synchronized Collection<Citation> getParameterCitation() {
        return (parameterCitation = nonNullCollection(parameterCitation, Citation.class));
    }

    /**
     * Set reference providing description of the parameters.
     */
    public synchronized void setParameterCitation(final Collection<? extends Citation> newValues) {
        parameterCitation = copyCollection(newValues, parameterCitation, Citation.class);
    }
}
