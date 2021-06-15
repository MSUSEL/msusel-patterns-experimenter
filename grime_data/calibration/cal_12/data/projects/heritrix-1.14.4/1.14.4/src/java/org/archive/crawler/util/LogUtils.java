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
package org.archive.crawler.util;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import org.archive.util.PropertyUtils;

/**
 * Logging utils.
 * @author stack
 */
public class LogUtils {
    /**
     * Creates a file logger that use heritrix.properties file logger
     * configuration.
     * Change the java.util.logging.FileHandler.* properties in
     * heritrix.properties to change file handler properties.
     * Use this method if you want a class to log to its own file
     * rather than use default (console) logger.
     * @param logsDir Directory in which to write logs.
     * @param baseName Base name to use for log file (Will have
     * java.util.logging.FileHandler.pattern or '.log' for suffix).
     * @param logger Logger whose handler we'll replace with the
     * file handler created herein.
     */
    public static void createFileLogger(File logsDir, String baseName,
            Logger logger) {
        int limit =
            PropertyUtils.getIntProperty("java.util.logging.FileHandler.limit",
            1024 * 1024 * 1024 * 1024);
        int count =
            PropertyUtils.getIntProperty("java.util.logging.FileHandler.count", 1);
        try {
            String tmp =
                System.getProperty("java.util.logging.FileHandler.pattern");
                File logFile = new File(logsDir, baseName +
                    ((tmp != null && tmp.length() > 0)? tmp: ".log"));
            FileHandler fh = new FileHandler(logFile.getAbsolutePath(), limit,
                count, true);
            // Manage the formatter to use.
            tmp = System.getProperty("java.util.logging.FileHandler.formatter");
            if (tmp != null && tmp.length() > 0) {
                Constructor co = Class.forName(tmp).
                    getConstructor(new Class[] {});
                Formatter f = (Formatter) co.newInstance(new Object[] {});
                fh.setFormatter(f);
            }
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            logger.severe("Failed customization of logger: " + e.getMessage());
        }
    }
}
