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
import org.opengis.metadata.spatial.Dimension;
import org.opengis.metadata.spatial.CellGeometry;
import org.opengis.metadata.spatial.GridSpatialRepresentation;


/**
 * Basic information required to uniquely identify a resource or resources.
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
public class GridSpatialRepresentationImpl extends SpatialRepresentationImpl
        implements GridSpatialRepresentation
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8400572307442433979L;

    /**
     * Number of independent spatial-temporal axes.
     */
    private Integer numberOfDimensions;

    /**
     * Information about spatial-temporal axis properties.
     */
    private List<Dimension> axisDimensionsProperties;

    /**
     * Identification of grid data as point or cell.
     */
    private CellGeometry cellGeometry;

    /**
     * Indication of whether or not parameters for transformation exists.
     */
    private boolean transformationParameterAvailable;

    /**
     * Constructs an initially empty grid spatial representation.
     */
    public GridSpatialRepresentationImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public GridSpatialRepresentationImpl(final GridSpatialRepresentation source) {
        super(source);
    }

    /**
     * Creates a grid spatial representation initialized to the given values.
     * <p>
     * <b>Note:</b> this is a convenience constructor. The argument types don't need to
     * match exactly the types expected by getters and setters.
     */
    public GridSpatialRepresentationImpl(final int numberOfDimensions,
                                         final List<? extends Dimension> axisDimensionsProperties,
                                         final CellGeometry cellGeometry,
                                         final boolean transformationParameterAvailable)
    {
        setNumberOfDimensions               (numberOfDimensions);
        setAxisDimensionsProperties         (axisDimensionsProperties);
        setCellGeometry                     (cellGeometry);
        setTransformationParameterAvailable (transformationParameterAvailable);
    }

    /**
     * Number of independent spatial-temporal axes.
     */
    public Integer getNumberOfDimensions() {
        return numberOfDimensions;
    }

    /**
     * Set the number of independent spatial-temporal axes.
     */
    public synchronized void setNumberOfDimensions(final Integer newValue) {
        checkWritePermission();
        numberOfDimensions = newValue;
    }

    /**
     * Information about spatial-temporal axis properties.
     */
    public synchronized List<Dimension> getAxisDimensionsProperties() {
        return axisDimensionsProperties = nonNullList(axisDimensionsProperties, Dimension.class);
    }

    /**
     * Set information about spatial-temporal axis properties.
     */
    public synchronized void setAxisDimensionsProperties(final List<? extends Dimension> newValues) {
        checkWritePermission();
        axisDimensionsProperties = (List<Dimension>)
                copyCollection(newValues, axisDimensionsProperties, Dimension.class);
    }

    /**
     * Identification of grid data as point or cell.
     */
    public CellGeometry getCellGeometry() {
        return cellGeometry;
    }

    /**
     * Set identification of grid data as point or cell.
     */
    public synchronized void setCellGeometry(final CellGeometry newValue) {
        checkWritePermission();
        cellGeometry = newValue;
    }

    /**
     * Indication of whether or not parameters for transformation exists.
     */
    public boolean isTransformationParameterAvailable() {
        return transformationParameterAvailable;
    }

    /**
     * Set indication of whether or not parameters for transformation exists.
     */
    public synchronized void setTransformationParameterAvailable(final boolean newValue) {
        checkWritePermission();
        transformationParameterAvailable = newValue;
    }
}
