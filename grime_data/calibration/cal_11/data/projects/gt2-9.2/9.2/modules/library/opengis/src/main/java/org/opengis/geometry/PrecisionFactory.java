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
package org.opengis.geometry;

/**
 * A factory for managing {@linkplain Precision direct position} creation. A
 * Precision is used to describe the accuracy at which you would like your
 * geometry maintained during operations and transformations.
 * <p>
 * Here are a couple of examples of creating a Precision using a
 * PrecisionFactory:
 *
 * <pre><code>
 * factory.createPrecision(PrecisionType.FIXED, 1000); // three significant digits
 * factory.createPrecision(PrecisionType.FLOAT, 0); // float precision - 6 digits
 * factory.createPrecision(PrecisionType.DOUBLE, 0); // double precision - 16 digits
 * <code></pre>
 * Although the
 * DirectPosition makes use of double when representing ordinates some
 * implementations, transfer mechanisms or storage facilities will not be able
 * to maintain this accuracy. In order to maintain a valid geometry representation
 * in these situations you will need to provide a {@link Percision} as a stratagy
 * object used to round coordinates during creation and transformation. We cannot
 * allow you to round to the correct precision afterwards as the result may be an
 * invalid geometry.
 * <p>
 * The easiest example is the construction of a very small
 * poloygon for a WFS configured to use 2 significant digits when generating
 * GML. When generating a polgon in meters of less than 1 cm in size the
 * rounding policy would "collapse" all the points of the outer ring into the
 * same location - a WFS faced with this situtation may choose to skip the polygon
 * or represent it as a Point.
 *
 * @author Jody Garnett
 * @since GeoAPI 2.1
 *
 *
 * @source $URL$
 */
public interface PrecisionFactory {

    /**
     * Creates a Precision of the provided type, scale is used for
     * PrecisionType.FIXED.
     * <p>
     * Here are a couple of examples:
     *
     * <pre><code>
     * factory.createPrecision(PrecisionType.FIXED, 1000); // three significant digits
     * factory.createPrecision(PrecisionType.FLOAT, 0); // float precision - 6 digits
     * factory.createPrecision(PrecisionType.DOUBLE, 0); // double precision - 16 digits
     * <code></pre>
     *
     * @param type PercisionType The rounding policy used
     * @param scale Multiplying factor used to obtain a precise coordinate
     * @return Precision capable of rounding as described by type and scale
     */
    Precision createFixedPrecision(PrecisionType code, double scale);

}
