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
package org.archive.crawler.writer;

public interface Kw3Constants {

    /* 
     * A bunch of keys for the header fields in the ArchiveInfo part 
     * of the MIME-file. 
     */
    public static String COLLECTION_KEY = "HTTP-Collection";
    public static String HARVESTER_KEY = "HTTP-Harvester";
    public static String URL_KEY = "HTTP-URL";
    public static String IP_ADDRESS_KEY = "HTTP-IP-Address";
    public static String HEADER_LENGTH_KEY = "HTTP-Header-Length";
    public static String HEADER_MD5_KEY = "HTTP-Header-MD5";
    public static String CONTENT_LENGTH_KEY = "HTTP-Content-Length";
    public static String CONTENT_MD5_KEY = "HTTP-Content-MD5";
    public static String ARCHIVE_TIME_KEY = "HTTP-Archive-Time";
    public static String STATUS_CODE_KEY = "HTTP-Status-Code";
    public static String CONTENT_TYPE_KEY = "Content-Type";
    
}
