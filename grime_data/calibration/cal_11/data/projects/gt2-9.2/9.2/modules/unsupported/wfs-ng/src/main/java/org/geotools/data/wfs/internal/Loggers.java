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
package org.geotools.data.wfs.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;

public final class Loggers {

    private Loggers() {
        //
    }

    public static final Logger MODULE = Logging.getLogger("org.geotools.data.wfs");

    public static final Logger REQUESTS = Logging.getLogger("org.geotools.data.wfs.requests");

    public static final Logger RESPONSES = Logging.getLogger("org.geotools.data.wfs.responses");

    public static final Level MODULE_TRACE_LEVEL = Level.FINER;

    public static final Level MODULE_DEBUG_LEVEL = Level.FINE;

    public static final Level MODULE_INFO_LEVEL = Level.INFO;

    public static final Level REQUEST_TRACE_LEVEL = Level.INFO;// TODO: lower this

    public static final Level REQUEST_DEBUG_LEVEL = Level.INFO;// TODO: lower this

    public static final Level REQUEST_INFO_LEVEL = Level.INFO;

    public static final Level RESPONSES_TRACE_LEVEL = Level.INFO;// TODO: lower this

    public static final Level RESPONSES_DEBUG_LEVEL = Level.INFO;// TODO: lower this

    public static void trace(Object... message) {
        log(MODULE, MODULE_TRACE_LEVEL, message);
    }

    public static void debug(Object... message) {
        log(MODULE, MODULE_DEBUG_LEVEL, message);
    }

    public static void info(Object... message) {
        log(MODULE, MODULE_INFO_LEVEL, message);
    }

    public static void requestTrace(Object... message) {
        log(REQUESTS, REQUEST_TRACE_LEVEL, message);
    }

    public static void requestDebug(Object... message) {
        log(REQUESTS, REQUEST_DEBUG_LEVEL, message);
    }

    public static void requestInfo(Object... message) {
        log(REQUESTS, REQUEST_INFO_LEVEL, message);
    }

    public static void responseTrace(Object... message) {
        log(RESPONSES, RESPONSES_TRACE_LEVEL, message);
    }

    public static void responseDebug(Object... message) {
        log(RESPONSES, RESPONSES_DEBUG_LEVEL, message);
    }

    private static void log(Logger logger, Level level, Object... message) {
        if (logger.isLoggable(level)) {
            // miss guava Joiner....
            StringBuilder sb = new StringBuilder();
            for (Object part : message) {
                sb.append(part);
            }
            logger.log(level, sb.toString());
        }
    }

}
