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

import java.util.logging.Level;


/**
 * A dummy implementation of {@link LoggingAdapter} class for testing purpose.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class DummyLogger extends LoggerAdapter {
    /**
     * The level of the last logging event.
     */
    Level level;

    /**
     * The last logged message.
     */
    String last;

    /**
     * Creates a dummy logger.
     */
    DummyLogger() {
        super("org.geotools.util.logging");
        clear();
    }

    /**
     * Clears the logger state, for testing purpose only.
     */
    public void clear() {
        level = Level.OFF;
        last  = null;
    }

    @Override
    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public boolean isLoggable(Level level) {
        return level.intValue() > this.level.intValue();
    }

    @Override
    public void severe(String message) {
        level = Level.SEVERE;
        last  = message;
    }

    @Override
    public void warning(String message) {
        level = Level.WARNING;
        last  = message;
    }

    @Override
    public void info(String message) {
        level = Level.INFO;
        last  = message;
    }

    @Override
    public void config(String message) {
        level = Level.CONFIG;
        last  = message;
    }

    @Override
    public void fine(String message) {
        level = Level.FINE;
        last  = message;
    }

    @Override
    public void finer(String message) {
        level = Level.FINER;
        last  = message;
    }

    @Override
    public void finest(String message) {
        level = Level.FINEST;
        last  = message;
    }
}
