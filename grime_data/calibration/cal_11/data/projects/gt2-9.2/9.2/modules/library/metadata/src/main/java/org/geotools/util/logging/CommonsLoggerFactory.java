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
package org.geotools.util.logging;

import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Jdk14Logger;


/**
 * A factory for loggers that redirect all Java logging events to the Apache's
 * <A HREF="http://jakarta.apache.org/commons/logging/">Commons-logging</A> framework.
 *
 * @since 2.4
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class CommonsLoggerFactory extends LoggerFactory<Log> {
    /**
     * The unique instance of this factory.
     */
    private static CommonsLoggerFactory factory;

    /**
     * Constructs a default factory.
     *
     * @throws NoClassDefFoundError if Apache's {@code Log} class was not found on the classpath.
     */
    protected CommonsLoggerFactory() throws NoClassDefFoundError {
        super(Log.class);
    }

    /**
     * Returns the unique instance of this factory.
     *
     * @throws NoClassDefFoundError if Apache's {@code Log} class was not found on the classpath.
     */
    public static synchronized CommonsLoggerFactory getInstance() throws NoClassDefFoundError {
        if (factory == null) {
            factory = new CommonsLoggerFactory();
        }
        return factory;
    }

    /**
     * Returns the implementation to use for the logger of the specified name,
     * or {@code null} if the logger would delegates to Java logging anyway.
     */
    protected Log getImplementation(final String name) {
        final Log log = LogFactory.getLog(name);
        if (log instanceof Jdk14Logger) {
            return null;
        }
        return log;
    }

    /**
     * Wraps the specified {@linkplain #getImplementation implementation} in a Java logger.
     */
    protected Logger wrap(String name, Log implementation) {
        return new CommonsLogger(name, implementation);
    }

    /**
     * Returns the {@linkplain #getImplementation implementation} wrapped by the specified logger,
     * or {@code null} if none.
     */
    protected Log unwrap(final Logger logger) {
        if (logger instanceof CommonsLogger) {
            return ((CommonsLogger) logger).logger;
        }
        return null;
    }
}
