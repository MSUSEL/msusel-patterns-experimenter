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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.resources;

import java.util.Set;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.sql.Driver;

import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;


/**
 * A set of utilities methods related to JDBC (<cite>Java Database Connectivity</cite>).
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @todo This class may be removed when we will be allowed to compile for J2SE 1.6.
 */
public final class JDBC {
    /**
     * Lists of JDBC drivers already loaded.
     */
    private static final Set<String> DRIVERS = new HashSet<String>();

    /**
     * Do not allow instantiation of this class.
     */
    private JDBC() {
    }

    /**
     * Attempts to load the specified JDBC driver, if not already done. If this method has already
     * been invoked for the specified driver, then it does nothing and returns {@code null}.
     * Otherwise, it attempts to load the specified driver and returns a log record initialized
     * with a message at the {@link Level#CONFIG CONFIG} level on success, or at the
     * {@link Level#WARNING WARNING} level on failure.
     *
     * @param  driver The JDBC driver to load, as a fully qualified Java class name.
     * @return A log message with driver information, or {@code null} if the driver was already
     *         loaded.
     *
     * @todo Remember to invoke {@link LogRecord#setLoggerName}.
     */
    public static LogRecord loadDriver(final String driver) {
        LogRecord log = null;
        if (driver != null) {
            synchronized (DRIVERS) {
                if (!DRIVERS.contains(driver)) {
                    try {
                        final Driver d = (Driver) Class.forName(driver).newInstance();
                        log = Loggings.format(Level.CONFIG, LoggingKeys.LOADED_JDBC_DRIVER_$3,
                                              driver, d.getMajorVersion(), d.getMinorVersion());
                        DRIVERS.add(driver);
                    } catch (Exception exception) {
                        log = new LogRecord(Level.WARNING, exception.toString());
                    }
                }
            }
        }
        return log;
    }
}
