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
package org.archive.crawler.selftest;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestResult;

import org.archive.crawler.Heritrix;
import org.archive.crawler.admin.CrawlJob;
import org.archive.crawler.admin.CrawlJobHandler;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.event.CrawlURIDispositionListener;


/**
 * An override to gain access to end-of-crawljob message.
 *
 *
 * @author stack
 * @version $Id: SelfTestCrawlJobHandler.java 4667 2006-09-26 20:38:48Z paul_jack $
 */

public class SelfTestCrawlJobHandler extends CrawlJobHandler
implements CrawlURIDispositionListener {
    /**
     * Name of the selftest webapp.
     */
    private static final String SELFTEST_WEBAPP = "selftest";

    private static Logger logger =
        Logger.getLogger("org.archive.crawler.admin.SelftestCrawlJobHandler");

    /**
     * Name of selftest to run.
     *
     * If set, run this test only.  Otherwise run them all.
     */
    private String selfTestName = null;
    
    private String selfTestUrl = null;


    private SelfTestCrawlJobHandler() {
        this(null, null, null);
    }

    public SelfTestCrawlJobHandler(final File jobsDir,
            final String selfTestName, final String url) {
        // No need to load jobs or profiles
        super(jobsDir, false, false);
        this.selfTestName = selfTestName;
        this.selfTestUrl = url;
    }
    
    @Override
    public void crawlStarted(String message) {
    	super.crawlStarted(message);
    	this.getCurrentJob().getController().
    		addCrawlURIDispositionListener(this);
    }

    public void crawlEnded(String sExitMessage)  {
        TestResult result = null;
        try {
            super.crawlEnded(sExitMessage);

            // At crawlEnded time, there is no current job.  Get the selftest
            // job by pulling from the completedCrawlJobs queue.
            List completedCrawlJobs = getCompletedJobs();
            if (completedCrawlJobs == null || completedCrawlJobs.size() <= 0) {
                logger.severe("Selftest job did not complete.");
            } else {
                CrawlJob job = (CrawlJob)completedCrawlJobs.
                    get(completedCrawlJobs.size()-1);
                Test test = null;
                if (this.selfTestName != null &&
                        this.selfTestName.length() > 0) {
                    // Run single selftest only.
                    // Get class for the passed single selftest.
                    // Assume test to run is in this package.
                    String thisClassName = this.getClass().getName();
                    String pkg = thisClassName.
                        substring(0, thisClassName.lastIndexOf('.'));
                    // All selftests end in 'SelfTest'.
                    String selftestClass = pkg + '.' + this.selfTestName +
                        "SelfTest";
                    // Need to make a list.  Make an array first.
                    List<Class<?>> classList = new ArrayList<Class<?>>();
                    classList.add(Class.forName(selftestClass));
                    test = AllSelfTestCases.suite(this.selfTestUrl,
                        job, job.getDirectory(), Heritrix.getHttpServer().
                        getWebappPath(SELFTEST_WEBAPP), classList);
                } else {
                    // Run all tests.
                    test = AllSelfTestCases.suite(this.selfTestUrl,
                        job, job.getDirectory(), Heritrix.getHttpServer().
                        getWebappPath(SELFTEST_WEBAPP));
                }
                result = junit.textui.TestRunner.run(test);
            }
        } catch (Exception e) {
            logger.info("Failed running selftest analysis: " + e.getMessage());
        } finally  {
            // TODO: This technique where I'm calling shutdown directly means
            // we bypass the running of other crawlended handlers.  Means
            // that such as the statistics tracker have no chance to run so
            // reports are never generated.  Fix -- but preserve 0 or 1 exit
            // code.
            logger.info((new Date()).toString() + " Selftest " +
                (result != null && result.wasSuccessful()? "PASSED": "FAILED"));
            stop();
            Heritrix.shutdown(((result !=  null) && result.wasSuccessful())?
                0: 1);
        }
    }

	public void crawledURIDisregard(CrawlURI curi) {
		// TODO Auto-generated method stub
	}

	public void crawledURIFailure(CrawlURI curi) {
		// TODO Auto-generated method stub
	}

	public void crawledURINeedRetry(CrawlURI curi) {
		// TODO Auto-generated method stub
	}

	public void crawledURISuccessful(CrawlURI curi) {
		// If curi ends in 'Checkpoint/index.html', then run a Checkpoint.
		if (curi.toString().endsWith("/Checkpoint/")) {
			this.getCurrentJob().getController().requestCrawlCheckpoint();
		}
	}
}