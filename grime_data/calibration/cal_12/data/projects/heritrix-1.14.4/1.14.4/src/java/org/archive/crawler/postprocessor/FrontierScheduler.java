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
package org.archive.crawler.postprocessor;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.framework.Processor;

/**
 * 'Schedule' with the Frontier CandidateURIs being carried by the passed
 * CrawlURI.
 * Adds either prerequisites or whatever is in CrawlURI outlinks to the
 * Frontier.  Run a Scoper ahead of this processor so only links that
 * are in-scope get scheduled.
 * @author stack
 */
public class FrontierScheduler extends Processor
implements FetchStatusCodes {

    private static final long serialVersionUID = -5178775477602250542L;

    private static Logger LOGGER =
        Logger.getLogger(FrontierScheduler.class.getName());
    
    /**
     * @param name Name of this filter.
     */
    public FrontierScheduler(String name) {
        super(name, "FrontierScheduler. 'Schedule' with the Frontier " +
            "any CandidateURIs carried by the passed CrawlURI. " +
            "Run a Scoper before this " +
            "processor so links that are not in-scope get bumped from the " +
            "list of links (And so those in scope get promoted from Link " +
            "to CandidateURI).");
    }

    protected void innerProcess(final CrawlURI curi) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest(getName() + " processing " + curi);
        }
        
        // Handle any prerequisites when S_DEFERRED for prereqs
        if (curi.hasPrerequisiteUri() && curi.getFetchStatus() == S_DEFERRED) {
            handlePrerequisites(curi);
            return;
        }

        synchronized(this) {
            for (CandidateURI cauri: curi.getOutCandidates()) {
                schedule(cauri);
            }
        }
    }

    protected void handlePrerequisites(CrawlURI curi) {
        schedule((CandidateURI)curi.getPrerequisiteUri());
    }

    /**
     * Schedule the given {@link CandidateURI CandidateURI} with the Frontier.
     * @param caUri The CandidateURI to be scheduled.
     */
    protected void schedule(CandidateURI caUri) {
        getController().getFrontier().schedule(caUri);
    }
}
