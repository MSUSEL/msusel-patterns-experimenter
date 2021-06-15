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
package org.archive.crawler.io;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.net.UURI;
import org.archive.util.ArchiveUtils;

/**
 * Formatter for 'uri-errors.log', of URIs so malformed they could
 * not be instantiated.
 * 
 * @author gojomo
 *
 */
public class UriErrorFormatter extends Formatter implements CoreAttributeConstants {

    /* (non-Javadoc)
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    public String format(LogRecord lr) {
        UURI uuri = (UURI) lr.getParameters()[0];
        String problem = (String) lr.getParameters()[1];

        return ArchiveUtils.getLog17Date()
        + " "
        + ( (uuri ==null) ? "n/a" : uuri.toString() )
        + " \""
        + lr.getMessage()
        + "\" "
        + problem
        + "\n";
    }
}

