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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;

import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;

/**
 * Runtime exception log formatter.
 *
 * Used to format unexpected runtime exceptions such as 
 * OOMEs.
 * 
 * @author gojomo
 */
public class RuntimeErrorFormatter extends UriProcessingFormatter
implements CoreAttributeConstants {
    public String format(LogRecord lr) {
        Object [] parameters = lr.getParameters();
        String stackTrace = "None retrieved";
        if (parameters != null) {
            // CrawlURI is always first parameter.
            CrawlURI curi = (CrawlURI)parameters[0];
            if (curi != null) {
                Throwable t = (Throwable)curi.getObject(A_RUNTIME_EXCEPTION);
                assert t != null : "Null throwable";
                StringWriter sw = new StringWriter();
                if (t == null) {
                    sw.write("No exception to report.");
                } else {
                    t.printStackTrace(new PrintWriter(sw));
                }
                stackTrace = sw.toString();
            }
        }
        return super.format(lr) + " " + stackTrace;
    }
}
