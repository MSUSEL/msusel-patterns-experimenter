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
package org.archive.crawler.processor.recrawl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.event.CrawlStatusListener;
import org.archive.crawler.io.CrawlerJournal;
import org.archive.crawler.settings.SimpleType;
import org.archive.util.FileUtils;
import org.archive.util.IoUtils;



/**
 * Log CrawlURI attributes from latest fetch for consultation by a later 
 * recrawl. Log must be imported into alternate data structure in order
 * to be consulted. 
 * 
 * @author gojomo
 * @version $Date: 2006-09-25 20:19:54 +0000 (Mon, 25 Sep 2006) $, $Revision: 4654 $
 */
public class PersistLogProcessor extends PersistProcessor implements CrawlStatusListener {
    private static final long serialVersionUID = 1678691994065439346L;
    
    protected CrawlerJournal log;

    /** setting for log filename */
    public static final String ATTR_LOG_FILENAME = "log-filename";
    /** default log filename */ 
    public static final String DEFAULT_LOG_FILENAME = "persistlog.txtser.gz";
    
    /**
     * Usual constructor
     * 
     * @param name
     */
    public PersistLogProcessor(String name) {
        super(name, "PersistLogProcessor. Logs CrawlURI attributes " +
                "from latest fetch for consultation by a later recrawl.");
        
        addElementToDefinition(new SimpleType(ATTR_LOG_FILENAME,
                "Filename to which to log URI persistence information. " +
                "Interpreted relative to job logs directory. " +
                "Default is 'persistlog.txtser.gz'. ", 
                DEFAULT_LOG_FILENAME));
    }

    @Override
    protected void initialTasks() {
        // do not create persist log if processor is disabled
        if (!isEnabled()) {
            return;
        }

        // Add this class to crawl state listeners to note checkpoints
        getController().addCrawlStatusListener(this);
        try {
            File logFile = FileUtils.maybeRelative(getController().getLogsDir(),
                    (String) getUncheckedAttribute(null, ATTR_LOG_FILENAME));
            log = new CrawlerJournal(logFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void finalTasks() {
        if (log != null) {
            log.close();
        }
    }

    @Override
    protected void innerProcess(CrawlURI curi) {
        if(shouldStore(curi)) {
            log.writeLine(persistKeyFor(curi), " ", new String(Base64.encodeBase64(IoUtils
                    .serializeToByteArray(curi.getPersistentAList()))));      
        }
    }

    public void crawlCheckpoint(File checkpointDir) throws Exception {
        // rotate log
        log.checkpoint(checkpointDir);
    }

    public void crawlEnded(String sExitMessage) {
        // ignored
        
    }

    public void crawlEnding(String sExitMessage) {
        // ignored
        
    }

    public void crawlPaused(String statusMessage) {
        // ignored
        
    }

    public void crawlPausing(String statusMessage) {
        // ignored
        
    }

    public void crawlResuming(String statusMessage) {
        // ignored
        
    }

    public void crawlStarted(String message) {
        // ignored
    }
}