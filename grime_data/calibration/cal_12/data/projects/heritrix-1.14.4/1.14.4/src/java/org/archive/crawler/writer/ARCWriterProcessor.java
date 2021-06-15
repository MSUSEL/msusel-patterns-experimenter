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

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.event.CrawlStatusListener;
import org.archive.crawler.framework.WriterPoolProcessor;
import org.archive.io.ReplayInputStream;
import org.archive.io.WriterPoolMember;
import org.archive.io.WriterPoolSettings;
import org.archive.io.arc.ARCConstants;
import org.archive.io.arc.ARCWriter;
import org.archive.io.arc.ARCWriterPool;


/**
 * Processor module for writing the results of successful fetches (and
 * perhaps someday, certain kinds of network failures) to the Internet Archive
 * ARC file format.
 *
 * Assumption is that there is only one of these ARCWriterProcessors per
 * Heritrix instance.
 *
 * @author Parker Thompson
 */
public class ARCWriterProcessor extends WriterPoolProcessor
implements CoreAttributeConstants, ARCConstants, CrawlStatusListener,
WriterPoolSettings, FetchStatusCodes {
	private static final long serialVersionUID = 1957518408532644531L;

	private final Logger logger = Logger.getLogger(this.getClass().getName());
    
    public long getDefaultMaxFileSize() {
        return 100000000L; // 100 SI mega-bytes (10^8 bytes), by tradition
    }
    
    /**
     * Default path list.
     */
    private static final String [] DEFAULT_PATH = {"arcs"};

    /**
     * @param name Name of this writer.
     */
    public ARCWriterProcessor(String name) {
        super(name, "ARCWriter processor");
    }
    
    protected String [] getDefaultPath() {
    	return DEFAULT_PATH;
	}

    protected void setupPool(final AtomicInteger serialNo) {
		setPool(new ARCWriterPool(serialNo, this, getPoolMaximumActive(),
            getPoolMaximumWait()));
    }
    
    /**
     * Writes a CrawlURI and its associated data to store file.
     *
     * Currently this method understands the following uri types: dns, http, 
     * and https.
     *
     * @param curi CrawlURI to process.
     */
    protected void innerProcess(CrawlURI curi) {
        // If failure, or we haven't fetched the resource yet, return
        if (curi.getFetchStatus() <= 0) {
            return;
        }
        
        // XXX This happens normally with ftp (empty directory or equivalent of
        // 40x). We should write a record here, but we're not worrying about
        // full ftp support for arcs, just warcs.
        if (curi.getHttpRecorder() == null) {
            return;
        }
        
        // If no recorded content at all, don't write record.
        long recordLength = curi.getHttpRecorder().getRecordedInput().getSize();
        if (recordLength <= 0) {
        	// getContentSize() should be > 0 if any material (even just
            // HTTP headers with zero-length body) is available. 
        	return;
        }
        
        ReplayInputStream ris = null; 
        try {
            if(shouldWrite(curi)) {
                ris = curi.getHttpRecorder().getRecordedInput()
                        .getReplayInputStream();
                write(curi, recordLength, ris, getHostAddress(curi));
            } else {
                logger.info("does not write " + curi.toString());
            }
        } catch (IOException e) {
            curi.addLocalizedError(this.getName(), e, "WriteRecord: " +
                curi.toString());
            logger.log(Level.SEVERE, "Failed write of Record: " +
                curi.toString(), e);
        } finally {
            IOUtils.closeQuietly(ris); 
        }
    }
    
    protected void write(CrawlURI curi, long recordLength, InputStream in,
        String ip)
    throws IOException {
        WriterPoolMember writer = getPool().borrowFile();
        long position = writer.getPosition();
        // See if we need to open a new file because we've exceeed maxBytes.
        // Call to checkFileSize will open new file if we're at maximum for
        // current file.
        writer.checkSize();
        if (writer.getPosition() != position) {
            // We just closed the file because it was larger than maxBytes.
            // Add to the totalBytesWritten the size of the first record
            // in the file, if any.
            setTotalBytesWritten(getTotalBytesWritten() +
            	(writer.getPosition() - position));
            position = writer.getPosition();
        }
        
        ARCWriter w = (ARCWriter)writer;
        try {
            if (in instanceof ReplayInputStream) {
                w.write(curi.toString(), curi.getContentType(),
                    ip, curi.getLong(A_FETCH_BEGAN_TIME),
                    recordLength, (ReplayInputStream)in);
            } else {
                w.write(curi.toString(), curi.getContentType(),
                    ip, curi.getLong(A_FETCH_BEGAN_TIME),
                    recordLength, in);
            }
        } catch (IOException e) {
            // Invalidate this file (It gets a '.invalid' suffix).
            getPool().invalidateFile(writer);
            // Set the writer to null otherwise the pool accounting
            // of how many active writers gets skewed if we subsequently
            // do a returnWriter call on this object in the finally block.
            writer = null;
            throw e;
        } finally {
            if (writer != null) {
            	setTotalBytesWritten(getTotalBytesWritten() +
            	     (writer.getPosition() - position));
                getPool().returnFile(writer);
            }
        }
        checkBytesWritten();
    }
    
    @Override
    protected String getFirstrecordStylesheet() {
        return "/arcMetaheaderBody.xsl";
    }
}