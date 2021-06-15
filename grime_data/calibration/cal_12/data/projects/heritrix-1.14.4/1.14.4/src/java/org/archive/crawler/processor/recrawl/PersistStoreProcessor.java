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

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.event.CrawlStatusListener;

import com.sleepycat.je.DatabaseException;

/**
 * Store CrawlURI attributes from latest fetch to persistent storage for
 * consultation by a later recrawl. 
 * 
 * @author gojomo
 * @version $Date: 2006-09-25 20:19:54 +0000 (Mon, 25 Sep 2006) $, $Revision: 4654 $
 */
public class PersistStoreProcessor extends PersistOnlineProcessor 
implements CrawlStatusListener {
    private static final long serialVersionUID = -8308356194337303758L;

    /**
     * Usual constructor
     * 
     * @param name
     */
    public PersistStoreProcessor(String name) {
        super(name, "PersistStoreProcessor. Stores CrawlURI attributes " +
                "from latest fetch for consultation by a later recrawl.");
    }

    protected void initialTasks() {
        super.initialTasks();
        // Add this class to crawl state listeners to note checkpoints
        getController().addCrawlStatusListener(this);
    }
    
    @Override
    protected void innerProcess(CrawlURI curi) throws InterruptedException {
        if(shouldStore(curi)) {
            store.put(persistKeyFor(curi),curi.getPersistentAList());
        }
    }
    
    public void crawlCheckpoint(File checkpointDir) throws Exception {
        // sync db
        try {
            historyDb.sync();
        } catch (DatabaseException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
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