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
package org.archive.crawler.event;

import java.io.File;

import org.archive.crawler.framework.CrawlController;


/**
 * Listen for CrawlStatus events.
 * 
 * Classes that implement this interface can register themselves with
 * a CrawlController to receive notifications about the events that
 * affect a crawl job's current status.
 *
 * @author Kristinn Sigurdsson
 *
 * @see org.archive.crawler.framework.CrawlController#addCrawlStatusListener(CrawlStatusListener)
 */

public interface CrawlStatusListener {
    /**
     * Called on crawl start.
     * @param message Start message.
     */
    public void crawlStarted(String message);
    
    /**
     * Called when a CrawlController is ending a crawl (for any reason)
     *
     * @param sExitMessage Type of exit. Should be one of the STATUS constants
     * in defined in CrawlJob.
     *
     * @see org.archive.crawler.admin.CrawlJob
     */
    public void crawlEnding(String sExitMessage);

    /**
     * Called when a CrawlController has ended a crawl and is about to exit.
     *
     * @param sExitMessage Type of exit. Should be one of the STATUS constants
     * in defined in CrawlJob.
     *
     * @see org.archive.crawler.admin.CrawlJob
     */
    public void crawlEnded(String sExitMessage);

    /**
     * Called when a CrawlController is going to be paused.
     *
     * @param statusMessage Should be
     * {@link org.archive.crawler.admin.CrawlJob#STATUS_WAITING_FOR_PAUSE
     * STATUS_WAITING_FOR_PAUSE}. Passed for convenience
     */
    public void crawlPausing(String statusMessage);

    /**
     * Called when a CrawlController is actually paused (all threads are idle).
     *
     * @param statusMessage Should be
     * {@link org.archive.crawler.admin.CrawlJob#STATUS_PAUSED}. Passed for
     * convenience
     */
    public void crawlPaused(String statusMessage);

    /**
     * Called when a CrawlController is resuming a crawl that had been paused.
     *
     * @param statusMessage Should be
     * {@link org.archive.crawler.admin.CrawlJob#STATUS_RUNNING}. Passed for
     * convenience
     */
    public void crawlResuming(String statusMessage);
    
    /**
     * Called by {@link CrawlController} when checkpointing.
     * @param checkpointDir Checkpoint dir.  Write checkpoint state here.
     * @throws Exception A fatal exception.  Any exceptions
     * that are let out of this checkpoint are assumed fatal
     * and terminate further checkpoint processing.
     */
    public void crawlCheckpoint(File checkpointDir) throws Exception;
}
