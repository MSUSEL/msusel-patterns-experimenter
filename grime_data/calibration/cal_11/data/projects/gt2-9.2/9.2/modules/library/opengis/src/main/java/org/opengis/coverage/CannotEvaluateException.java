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
 *    (C) 2003-2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage;

import org.opengis.geometry.DirectPosition;  // For Javadoc
import org.opengis.annotation.Extension;


/**
 * The base class for exceptions thrown when a quantity can't be evaluated.
 * This exception is usually invoked by a
 * <code>Coverage.{@linkplain Coverage#evaluate(DirectPosition, double[]) evaluate}(&hellip;)</code>
 * method, for example when a point is outside the coverage.
 *
 * @author  Martin Desruisseaux (IRD)
 * @author  Alexander Petkov
 * @since   GeoAPI 1.0
 *
 * @see Coverage#evaluate(DirectPosition, byte[])
 * @see Coverage#evaluate(DirectPosition, double[])
 *
 *
 * @source $URL$
 */
public class CannotEvaluateException extends RuntimeException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 506793649975583062L;

    /**
     * Represents the coverage for which this exception is thrown. Useful when {@link Coverage}
     * is used on a multilevel, so {@code PointOutsideCoverageException} can provide informative
     * details.
     */
    private Coverage coverage;

    /**
     * Creates an exception with no message.
     */
    public CannotEvaluateException() {
        super();
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public CannotEvaluateException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     * @param  cause The cause for this exception. The cause is saved
     *         for later retrieval by the {@link #getCause()} method.
     */
    public CannotEvaluateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns the coverage for which this exception is thrown. Useful when {@link Coverage}
     * is used on a multilevel, so {@code CannotEvaluateException} can provide informative
     * details.
     *
     * @return The coverage, or {@code null} if unknown.
     *
     * @since GeoAPÏ 2.2
     */
    @Extension
    public Coverage getCoverage() {
        return coverage;
    }

    /**
     * Sets the coverage.
     *
     * @param coverage The coverage, or {@code null} if unknown.
     *
     * @since GeoAPÏ 2.2
     */
    @Extension
    public void setCoverage(final Coverage coverage) {
        this.coverage = coverage;
    }
}
