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

import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;
import it.unimi.dsi.mg4j.util.MutableString;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.archive.util.ArchiveUtils;

/**
 * Utility class for a crawler journal/log that is compressed and 
 * rotates by serial number at checkpoints. 
 * 
 * @author gojomo
 */
public class CrawlerJournal {

    /** prefix for error lines*/
    public static final String LOG_ERROR = "E ";
    /** prefix for timestamp lines */
    public static final String LOG_TIMESTAMP = "T ";
    
    /**
     * Get a BufferedReader on the crawler journal given
     * 
     * @param source File journal
     * @return journal buffered reader.
     * @throws IOException
     */
    public static BufferedReader getBufferedReader(File source) throws IOException {
        boolean isGzipped = source.getName().toLowerCase().
            endsWith(GZIP_SUFFIX);
        FileInputStream fis = new FileInputStream(source);
        return new BufferedReader(isGzipped?
            new InputStreamReader(new GZIPInputStream(fis)):
            new InputStreamReader(fis));   
    }

    /**
     * Get a BufferedReader on the crawler journal given.
     * 
     * @param source URL journal
     * @return journal buffered reader.
     * @throws IOException
     */
    public static BufferedReader getBufferedReader(URL source) throws IOException {
        URLConnection conn = source.openConnection();
        boolean isGzipped = conn.getContentType() != null && conn.getContentType().equalsIgnoreCase("application/x-gzip")
                || conn.getContentEncoding() != null && conn.getContentEncoding().equalsIgnoreCase("gzip");
        InputStream uis = conn.getInputStream();
        return new BufferedReader(isGzipped?
            new InputStreamReader(new GZIPInputStream(uis)):
            new InputStreamReader(uis));   
        
    }
    
    /**
     * Get a BufferedInputStream on the recovery file given.
     *
     * @param source file to open
     * @return journal buffered input stream.
     * @throws IOException
     */
    public static BufferedInputStream getBufferedInput(File source) throws IOException {
        boolean isGzipped = source.getName().toLowerCase().
            endsWith(GZIP_SUFFIX);
        FileInputStream fis = new FileInputStream(source);
        return isGzipped ? new BufferedInputStream(new GZIPInputStream(fis))
                : new BufferedInputStream(fis);
    }

    /**
     * Stream on which we record frontier events.
     */
    protected Writer out = null;
    
    /** line count */ 
    protected long lines = 0;
    /** number of lines between timestamps */ 
    protected int timestamp_interval = 0; // 0 means no timestamps

    
    /** suffix to recognize gzipped files */
    public static final String GZIP_SUFFIX = ".gz";
    
    /**
     * File we're writing journal to.
     * Keep a reference in case we want to rotate it off.
     */
    protected File gzipFile = null;
    
    /**
     * Create a new crawler journal at the given location
     * 
     * @param path Directory to make thejournal in.
     * @param filename Name to use for journal file.
     * @throws IOException
     */
    public CrawlerJournal(String path, String filename)
    throws IOException {
        this.gzipFile = new File(path, filename);
        this.out = initialize(gzipFile);
    }
    
    /**
     * Create a new crawler journal at the given location
     * 
     * @param file path at which to make journal
     * @throws IOException
     */
    public CrawlerJournal(File file) throws IOException {
        this.gzipFile = file;
        this.out = initialize(gzipFile);
    }
    
    /**
     * Allocate a buffer for accumulating lines to write and reuse it.
     */
    protected MutableString accumulatingBuffer = new MutableString(1024);

    protected Writer initialize(final File f) throws FileNotFoundException, IOException {
        return new OutputStreamWriter(new GZIPOutputStream(
            new FastBufferedOutputStream(new FileOutputStream(f))));
    }

    /**
     * Write a line
     * 
     * @param string String
     */
    public synchronized void writeLine(String string) {
        try {
            this.out.write("\n");
            this.out.write(string);
            noteLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write a line of two strings
     * 
     * @param s1 String
     * @param s2 String
     */
    public synchronized void writeLine(String s1, String s2) {
        try {
            this.out.write("\n");
            this.out.write(s1);
            this.out.write(s2);
            noteLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Write a line of three strings
     * 
     * @param s1 String
     * @param s2 String
     * @param s3 String
     */
    public synchronized void writeLine(String s1, String s2, String s3) {
        try {
            this.out.write("\n");
            this.out.write(s1);
            this.out.write(s2);
            this.out.write(s3);
            noteLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write a line. 
     * 
     * @param mstring MutableString to write
     */
    public synchronized void writeLine(MutableString mstring) {
        if (this.out == null) {
            return;
        }
        try {
            this.out.write("\n");
            mstring.write(out);
            noteLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Count and note a line
     * 
     * @throws IOException
     */
    protected void noteLine() throws IOException {
        lines++;
        considerTimestamp();
    }

    /**
     * Write a timestamp line if appropriate
     * 
     * @throws IOException
     */
    protected void considerTimestamp() throws IOException {
        if(timestamp_interval > 0 && lines % timestamp_interval == 0) {
            out.write("\n");
            out.write(LOG_TIMESTAMP);
            out.write(ArchiveUtils.getLog14Date());
        }
    }

    /**
     * Flush and close the underlying IO objects.
     */
    public void close() {
        if (this.out == null) {
            return;
        }
        try {
            this.out.flush();
            this.out.close();
            this.out = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Note a serious error vioa a special log line
     * 
     * @param err
     */
    public void seriousError(String err) {
        writeLine("\n"+LOG_ERROR+ArchiveUtils.getLog14Date()+" "+err);
    }

    /**
     * Handle a checkpoint by rotating the current log to a checkpoint-named
     * file and starting a new log. 
     * 
     * @param checkpointDir
     * @throws IOException
     */
    public synchronized void checkpoint(final File checkpointDir) throws IOException {
        if (this.out == null || !this.gzipFile.exists()) {
            return;
        }
        close();
        // Rename gzipFile with the checkpoint name as suffix.
        this.gzipFile.renameTo(new File(this.gzipFile.getParentFile(),
                this.gzipFile.getName() + "." + checkpointDir.getName()));
        // Open new gzip file.
        this.out = initialize(this.gzipFile);
    }

}
