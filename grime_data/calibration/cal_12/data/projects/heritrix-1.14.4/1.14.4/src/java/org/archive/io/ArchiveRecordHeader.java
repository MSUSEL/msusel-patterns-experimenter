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
package org.archive.io;

import java.util.Map;
import java.util.Set;

/**
 * Archive Record Header.
 * @author stack
 * @version $Date: 2006-08-28 23:44:20 +0000 (Mon, 28 Aug 2006) $ $Version$
 */
public interface ArchiveRecordHeader {
    /**
     * Get the time when the record was created.
     * @return Date in 14 digit time format (UTC).
     * @see org.archive.util.ArchiveUtils#parse14DigitDate(String)
     */
    public abstract String getDate();

    /**
     * @return Return length of record.
     */
    public abstract long getLength();

    /**
     * @return Record subject-url.
     */
    public abstract String getUrl();

    /**
     * @return Record mimetype.
     */
    public abstract String getMimetype();

    /**
     * @return Record version.
     */
    public abstract String getVersion();

    /**
     * @return Offset into Archive file at which this record begins.
     */
    public abstract long getOffset();

    /**
     * @param key Key to use looking up field value.
     * @return value for passed key of null if no such entry.
     */
    public abstract Object getHeaderValue(final String key);

    /**
     * @return Header field name keys.
     */
    public abstract Set getHeaderFieldKeys();

    /**
     * @return Map of header fields.
     */
    public abstract Map getHeaderFields();

    /**
     * @return Returns identifier for current Archive file.  Be aware this
     * may not be a file name or file path.  It may just be an URL.  Depends
     * on how Archive file was made.
     */
    public abstract String getReaderIdentifier();
    
    /**
     * @return Identifier for the record.  If ARC, the URL + date.  If WARC, 
     * the GUID assigned.
     */
    public abstract String getRecordIdentifier();
    
    /**
     * @return Returns digest as String for this record. Only available after
     * the record has been read in totality.
     */
    public abstract String getDigest();

    /**
     * Offset at which the content begins.
     * For ARCs, its used to delimit where http headers end and content begins.
     * For WARCs, its end of Named Fields before payload starts.
     */
    public int getContentBegin();

    public abstract String toString();
}