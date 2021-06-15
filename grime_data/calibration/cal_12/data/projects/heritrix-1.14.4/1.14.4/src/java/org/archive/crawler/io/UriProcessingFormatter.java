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

import it.unimi.dsi.mg4j.util.MutableString;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.util.ArchiveUtils;
import org.archive.util.Base32;
import org.archive.util.MimetypeUtils;

/**
 * Formatter for 'crawl.log'. Expects completed CrawlURI as parameter.
 *
 * @author gojomo
 */
public class UriProcessingFormatter
extends Formatter implements CoreAttributeConstants {
    private final static String NA = "-";
    /**
     * Guess at line length (URIs are assumed avg. of 128 bytes).
     * Used to preallocated the buffer we accumulate the log line
     * in.  Hopefully we get it right most of the time and no need
     * to enlarge except in the rare case.
     */
    private final static int GUESS_AT_LOG_LENGTH =
        17 + 1 + 3 + 1 + 10 + 128 + + 1 + 10 + 1 + 128 + 1 + 10 + 1 + 3 +
        14 + 1 + 32 + 4 + 128 + 1;
    
    /**
     * Reuseable assembly buffer.
     */
    private final MutableString buffer =
        new MutableString(GUESS_AT_LOG_LENGTH);
    
    public String format(LogRecord lr) {
        CrawlURI curi = (CrawlURI)lr.getParameters()[0];
        String length = NA;
        String mime = null;
        if (curi.isHttpTransaction()) {
            if(curi.getContentLength() >= 0) {
                length = Long.toString(curi.getContentLength());
            } else if (curi.getContentSize() > 0) {
                length = Long.toString(curi.getContentSize());
            }
            mime = curi.getContentType();
        } else {
            if (curi.getContentSize() > 0) {
                length = Long.toString(curi.getContentSize());
            } 
            mime = curi.getContentType();
        }
        mime = MimetypeUtils.truncate(mime);

        long time = System.currentTimeMillis();
        String arcTimeAndDuration;
        if(curi.containsKey(A_FETCH_COMPLETED_TIME)) {
            long completedTime = curi.getLong(A_FETCH_COMPLETED_TIME);
            long beganTime = curi.getLong(A_FETCH_BEGAN_TIME);
            arcTimeAndDuration = ArchiveUtils.get17DigitDate(beganTime) + "+"
                    + Long.toString(completedTime - beganTime);
        } else {
            arcTimeAndDuration = NA;
        }

        String via = curi.flattenVia();
        
        String digest = curi.getContentDigestSchemeString();

        String sourceTag = curi.containsKey(A_SOURCE_TAG) 
                ? curi.getString(A_SOURCE_TAG)
                : null;
                
        this.buffer.length(0);
        return this.buffer.append(ArchiveUtils.getLog17Date(time))
            .append(" ")
            .append(ArchiveUtils.padTo(curi.getFetchStatus(), 5))
            .append(" ")
            .append(ArchiveUtils.padTo(length, 10))
            .append(" ")
            .append(curi.getUURI().toString())
            .append(" ")
            .append(checkForNull(curi.getPathFromSeed()))
            .append(" ")
            .append(checkForNull(via))
            .append(" ")
            .append(mime)
            .append(" ")
            .append("#")
            // Pad threads to be 3 digits.  For Igor.
            .append(ArchiveUtils.padTo(
                Integer.toString(curi.getThreadNumber()), 3, '0'))
            .append(" ")
            .append(arcTimeAndDuration)
            .append(" ")
            .append(checkForNull(digest))
            .append(" ")
            .append(checkForNull(sourceTag))
            .append(" ")
            .append(checkForNull(curi.getAnnotations()))
            .append("\n").toString();
    }
    
    /**
     * @param str String to check.
     * @return Return passed string or <code>NA</code> if null.
     */
    protected String checkForNull(String str) {
        return (str == null || str.length() <= 0)? NA: str;
    }
}


