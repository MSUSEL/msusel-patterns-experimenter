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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.metadata;

/**
 * A {@code CoordinateSystem} node in the metadata tree.
 * <p>
 * A coordinate system (CS) is the non-repeating sequence of coordinate system
 * axes that spans a given coordinate space. A CS is derived from a set of
 * mathematical rules for specifying how coordinates in a given space are to be
 * assigned to points. The coordinate values in a coordinate tuple shall be
 * recorded in the order in which the coordinate system axes associations are
 * recorded.
 * </p>
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 *
 *
 *
 * @source $URL$
 */
public class CoordinateSystem extends IdentifiableMetadataAccessor {

    /** Axes * */
    private final ChildList<Axis> axes;

    /**
     * Implicit constructor.
     * 
     * @param metadata
     * @param absoluteParentCRSPath
     */
    protected CoordinateSystem(SpatioTemporalMetadata metadata,
            String absoluteParentCRSPath) {
        super(metadata, new StringBuilder(absoluteParentCRSPath).append(SEPARATOR)
        		.append(SpatioTemporalMetadataFormat.MD_COORDINATESYSTEM).toString(),
                SpatioTemporalMetadataFormat.MD_CS_AXES);
        axes = new ChildList.Axes(metadata, absoluteParentCRSPath);
    }

    protected int childCount() {
        return axes.childCount();
    }

    public Axis getAxis(int index) {
        return axes.getChild(index);
    }

    public Axis addAxis() {
        return axes.addChild();
    }
}
