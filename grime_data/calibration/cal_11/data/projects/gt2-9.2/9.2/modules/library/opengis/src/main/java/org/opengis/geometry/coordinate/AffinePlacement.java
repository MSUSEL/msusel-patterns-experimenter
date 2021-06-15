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
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.coordinate;

import org.opengis.geometry.coordinate.Position;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

/**
 * A placement defined by linear transformation from the parameter space to the target
 * coordinate space. In 2-dimensional Cartesian parameter space, (<var>u</var>, <var>v</var>),
 * transforms into a 3-dimensional coordinate reference system, (<var>x</var>, <var>y</var>, <var>z</var>),
 * by using an affine transformation, (<var>u</var>, <var>v</var>) &rarr; (<var>x</var>, <var>y</var>, <var>z</var>),
 * which is defined:
 *
 * <P><center>(TODO: paste the matrix here)</center></P>
 *
 * <P>Then, given this equation, the {@link #getLocation} method returns the direct position
 * (<var>x</var><sub>0</sub>, <var>y</var><sub>0</sub>, <var>z</var><sub>0</sub>), which
 * is the target position of the origin in (<var>u</var>, <var>v</var>). The two
 * {@linkplain #getReferenceDirection reference directions}
 * (<var>u</var><sub>x</sub>, <var>u</var><sub>y</sub>, <var>u</var><sub>z</sub>)
 * and (<var>v</var><sub>x</sub>, <var>v</var><sub>y</sub>, <var>v</var><sub>z</sub>) are the
 * target directions of the unit basis vectors at the origin in (<var>u</var>, <var>v</var>).</P>
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_AffinePlacement", specification=ISO_19107)
public interface AffinePlacement extends Placement {
    /**
     * Gives the target of the parameter space origin. This is the vector
     * (<var>x</var><sub>0</sub>, <var>y</var><sub>0</sub>, <var>z</var><sub>0</sub>)
     * in the formulae in the class description.
     */
    @UML(identifier="location", obligation=MANDATORY, specification=ISO_19107)
    Position getLocation();

    /**
     * Gives the target directions for the coordinate basis vectors of the parameter space.
     * These are the columns of the matrix in the formulae given in the class description.
     * The number of directions given shall be {@link #getInDimension inDimension}.
     * The dimension of the directions shall be {@link #getOutDimension outDimension}.
     *
     * @param  dimension The in dimension, as an index from 0 inclusive to
     *         {@link #getInDimension inDimension} exclusive.
     * @return The direction as an array of length {@link #getOutDimension outDimension}.
     */
    @UML(identifier="refDirection", obligation=MANDATORY, specification=ISO_19107)
    double[] getReferenceDirection(int dimension);
}
