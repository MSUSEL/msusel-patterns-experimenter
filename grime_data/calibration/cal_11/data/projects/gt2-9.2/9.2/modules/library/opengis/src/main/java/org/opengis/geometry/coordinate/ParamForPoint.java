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

import java.util.Set;
import org.opengis.geometry.DirectPosition;
import org.opengis.annotation.Extension;
import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * The curve parameter for a point. This is the result of call to
 * {@link GenericCurve#getParamForPoint}.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier="paramForPoint", specification=ISO_19107)
public interface ParamForPoint {
    /**
     * Returns the parameter distance computed by
     * <code>{@linkplain GenericCurve#getParamForPoint getParamForPoint}(p)</code>.
     * The output will contain only one distance, unless the curve is not simple. If there is more
     * than one {@linkplain DirectPosition direct position} on the {@linkplain GenericCurve generic
     * curve} at the same minimal distance from the passed "{@code p}", the return value may
     * be an arbitrary choice of one of the possible answers.
     *
     * @return The parameter distance.
     * @unitof Distance
     *
     * @since GeoAPI 2.1
     */
    Set<Number> getDistances();

    /**
     * Returns the first value in the {@linkplain #getDistances distances} set.
     *
     * @return The parameter distance.
     * @unitof Distance
     */
    @Extension
    double getDistance();

    /**
     * Returns the actual value for the direct position used by
     * <code>{@linkplain GenericCurve#getParamForPoint getParamForPoint}(p)</code>.
     * That is, it shall be the point on the {@linkplain GenericCurve generic curve}
     * closest to the coordinate passed in as the "{@code p}" argument.
     *
     * @return The actual position used.
     */
    DirectPosition getPosition();
}
