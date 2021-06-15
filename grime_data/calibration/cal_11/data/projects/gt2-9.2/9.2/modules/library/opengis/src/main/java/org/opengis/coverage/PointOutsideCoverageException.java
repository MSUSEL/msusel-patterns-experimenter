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
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage;

import org.opengis.geometry.DirectPosition;
import org.opengis.annotation.Extension;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Specification.*;


/**
 * Thrown when a {@link Coverage#evaluate(DirectPosition, java.util.Set) evaluate} method
 * is invoked for a location outside the domain of the coverage.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Alexander Petkov
 * @since   GeoAPI 1.0
 *
 * @see Coverage#evaluate(DirectPosition, byte[])
 * @see Coverage#evaluate(DirectPosition, double[])
 */
@UML(identifier="CV_PointOutsideCoverage", specification=OGC_01004)
public class PointOutsideCoverageException extends CannotEvaluateException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8718412090539227101L;

    /**
     * Represents a direct position which is outside the domain of the coverage.
     */
    private DirectPosition offendingLocation;

    /**
     * Creates an exception with no message.
     */
    public PointOutsideCoverageException() {
        super();
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public PointOutsideCoverageException(String message) {
        super(message);
    }

    /**
     * Returns the {@linkplain DirectPosition direct position}
     * which is outside the domain of the {@linkplain #getCoverage coverage}.
     *
     * @return The position outside the coverage, or {@code null} if unknown.
     *
     * @since GeoAPI 2.2
     */
    @Extension
    public DirectPosition getOffendingLocation() {
        return offendingLocation;
    }

    /**
     * Sets the {@linkplain DirectPosition direct position}
     * which is outside the domain of the {@linkplain #getCoverage coverage}.
     *
     * @param location The position outside the coverage, or {@code null} if unknown.
     *
     * @since GeoAPI 2.2
     */
    @Extension
    public void setOffendingLocation(final DirectPosition location) {
        this.offendingLocation = location;
    }
}
