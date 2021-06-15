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

/**
 * Constants used by Archive files and in Archive file processing.
 * @author stack
 * @version $Date: 2006-09-12 19:41:23 +0000 (Tue, 12 Sep 2006) $ $Revision: 4620 $
 */
public interface ArchiveFileConstants {
    /**
     * Suffix given to files currently in use.
     */
    public static final String OCCUPIED_SUFFIX = ".open";
    
    /**
     * Suffix appended to 'broken' files.
     */
    public static final String INVALID_SUFFIX = ".invalid";
    
    /**
     * Compressed file extention.
     */
    public static final String COMPRESSED_FILE_EXTENSION = "gz";
    
    /**
     * Dot plus compressed file extention.
     */
    public static final String DOT_COMPRESSED_FILE_EXTENSION = "." +
        COMPRESSED_FILE_EXTENSION;
    
    /**
     * Key for the Archive File version field.
     */
    public static final String VERSION_FIELD_KEY = "version";
    
    /**
     * Key for the Archive File length field.
     */
    public static final String LENGTH_FIELD_KEY = "length";
    
    /**
     * Key for the Archive File type field.
     */
    public static final String TYPE_FIELD_KEY = "type";
    
    /**
     * Key for the Archive File URL field.
     */
    public static final String URL_FIELD_KEY = "subject-uri";
    
    /**
     * Key for the Archive File Creation Date field.
     */
    public static final String DATE_FIELD_KEY = "creation-date";

    /**
     * Key for the Archive File mimetype field.
     */
    public static final String MIMETYPE_FIELD_KEY = "content-type";
    
    /**
     * Key for the Archive File record field.
     */
    public static final String RECORD_IDENTIFIER_FIELD_KEY =
    	"record-identifier";
    
    /**
     * Key for the Archive Record absolute offset into Archive file.
     */
    public static final String ABSOLUTE_OFFSET_KEY = "absolute-offset";
    
    public static final String READER_IDENTIFIER_FIELD_KEY =
    	"reader-identifier";
    
    /**
     * Size used to preallocate stringbuffer used outputting a cdx line.
     * The numbers below are guesses at sizes of each of the cdx fields.
     * The ones in the below are spaces. Here is the legend used outputting
     * the cdx line: CDX b e a m s c V n g.  Consult cdx documentation on
     * meaning of each of these fields.
     */
    public static final int CDX_LINE_BUFFER_SIZE = 14 + 1 + 15 + 1 + 1024 +
        1 + 24 + 1 + + 3 + 1 + 32 + 1 + 20 + 1 + 20 + 1 + 64;
    
    public static final String DEFAULT_DIGEST_METHOD = "SHA-1";
    
    public static final char SINGLE_SPACE = ' ';
    
    public static final String CRLF = "\r\n";
    
    public static final String CDX = "cdx";
    public static final String DUMP = "dump";
    public static final String GZIP_DUMP = "gzipdump";
    public static final String HEADER = "header";
    public static final String NOHEAD = "nohead";
    public static final String CDX_FILE = "cdxfile";
}
