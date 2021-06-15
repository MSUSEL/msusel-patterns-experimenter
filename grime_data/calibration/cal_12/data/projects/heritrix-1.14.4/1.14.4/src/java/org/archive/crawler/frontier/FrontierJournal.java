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
package org.archive.crawler.frontier;

import java.io.File;
import java.io.IOException;

import org.archive.crawler.datamodel.CandidateURI;

/**
 * Record of key Frontier happenings.
 * @author stack
 * @version $Date: 2008-07-11 22:14:18 +0000 (Fri, 11 Jul 2008) $, $Revision: 5870 $
 */
public interface FrontierJournal {
    public static final String LOGNAME_RECOVER = "recover.gz";

    /**
     * @param curi CrawlURI that has been scheduled to be added to the
     * Frontier.
     */
    public abstract void added(CandidateURI curi);

    /**
     * @param curi CrawlURI that finished successfully.
     */
    public abstract void finishedSuccess(CandidateURI curi);

    /**
     * Note that a CrawlURI was emitted for processing.
     * If not followed by a finished or rescheduled notation in
     * the journal, the CrawlURI was still in-process when the journal ended.
     * 
     * @param curi CrawlURI emitted.
     */
    public abstract void emitted(CandidateURI curi);

    
    /**
     * @param curi CrawlURI finished unsuccessfully.
     */
    public abstract void finishedFailure(CandidateURI curi);

    /**
     * @param curi CrawlURI finished disregarded (uncounted failure).
     */
    public abstract void finishedDisregard(CandidateURI curi);
    
    /**
     * @param curi CrawlURI that was returned to the Frontier for 
     * another try.
     */
    public abstract void rescheduled(CandidateURI curi);

    /**
     *  Flush and close any held objects.
     */
    public abstract void close();
    
    /**
     * Checkpoint.
     * @param checkpointDir Directory we're checkpointing into.
     * @throws IOException
     */
    public abstract void checkpoint(final File checkpointDir)
    throws IOException;

    /**
     * Add a line noting a serious crawl error. 
     * 
     * @param string
     */
    public abstract void seriousError(String string);
}