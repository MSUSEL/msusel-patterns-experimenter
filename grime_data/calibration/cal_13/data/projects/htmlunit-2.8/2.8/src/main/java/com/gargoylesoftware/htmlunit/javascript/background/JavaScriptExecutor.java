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
package com.gargoylesoftware.htmlunit.javascript.background;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * An event loop to execute all the JavaScript jobs.
 *
 * @version $Revision: 5920 $
 * @author Amit Manjhi
 *
 */
public class JavaScriptExecutor implements Runnable, Serializable {

    private static final long serialVersionUID = 8525230714555970165L;

    /**
     * A simple class to store a JavaScriptJobManager and its earliest job.
     */
    protected final class JobExecutor {
        private final JavaScriptJobManager jobManager_;
        private final JavaScriptJob earliestJob_;

        private JobExecutor(final JavaScriptJobManager jobManager, final JavaScriptJob earliestJob) {
            jobManager_ = jobManager;
            earliestJob_ = earliestJob;
        }

        /**
         * Returns the earliest job.
         * @return the earliest job.
         */
        protected JavaScriptJob getEarliestJob() {
            return earliestJob_;
        }

        /**
         * Returns the JavaScriptJobManager.
         * @return the JavaScriptJobManager.
         */
        protected JavaScriptJobManager getJobManager() {
            return jobManager_;
        }
    }

    // TODO: is there utility in not having these as transient?
    private transient WeakReference<WebClient> webClient_;

    private transient List<WeakReference<JavaScriptJobManager>> jobManagerList_;

    private volatile boolean shutdown_ = false;

    private transient Thread eventLoopThread_ = null;

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(JavaScriptExecutor.class);

    /** Creates an EventLoop for the webClient.
     *
     * @param webClient the provided webClient
     */
    public JavaScriptExecutor(final WebClient webClient) {
        jobManagerList_ = new ArrayList<WeakReference<JavaScriptJobManager>>();
        webClient_ = new WeakReference<WebClient>(webClient);
    }

    /**
     * Starts the eventLoopThread_.
     */
    protected void startThreadIfNeeded() {
        if (eventLoopThread_ == null) {
            eventLoopThread_ = new Thread(this, "JS executor for " + webClient_.get());
            eventLoopThread_.setDaemon(true);
            eventLoopThread_.start();
        }
    }

    private void killThread() {
        if (eventLoopThread_ == null) {
            return;
        }
        try {
            eventLoopThread_.interrupt();
            eventLoopThread_.join(10000);
        }
        catch (final InterruptedException e) {
            LOG.warn("InterruptedException while waiting for the eventLoop thread to join " + e);
            // ignore, this doesn't matter, we want to stop it
        }
        if (eventLoopThread_.isAlive()) {
            LOG.warn("Event loop thread "
                + eventLoopThread_.getName()
                + " still alive at "
                + System.currentTimeMillis());
        }
    }

    /**
     * Returns the JobExecutor corresponding to the earliest job.
     * @return the JobExectuor with the earliest job.
     */
    protected synchronized JobExecutor getEarliestJob() {
        JobExecutor jobExecutor = null;
        // iterate over the list and find the earliest job to run.
        for (WeakReference<JavaScriptJobManager> jobManagerRef : jobManagerList_) {
            final JavaScriptJobManager jobManager = jobManagerRef.get();
            if (jobManager != null) {
                final JavaScriptJob newJob = jobManager.getEarliestJob();
                if (newJob != null) {
                    if (jobExecutor == null
                        || jobExecutor.earliestJob_.getTargetExecutionTime() > newJob
                                .getTargetExecutionTime()) {
                        jobExecutor = new JobExecutor(jobManager, newJob);
                    }
                }
            }
        }
        return jobExecutor;
    }

    /**
     * Executes the jobs in the eventLoop till timeoutMillis expires or the eventLoop becomes empty.
     * No use in non-GAE mode.
     * @param timeoutMillis the timeout in milliseconds
     * @return the number of jobs executed
     */
    public int pumpEventLoop(final long timeoutMillis) {
        return 0;
    }

    /** Runs the eventLoop. */
    public void run() {
        while (!shutdown_ && webClient_.get() != null) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("started finding earliestJob at " + System.currentTimeMillis());
            }
            final JobExecutor jobExecutor = getEarliestJob();
            if (LOG.isTraceEnabled()) {
                LOG.trace("stopped finding earliestJob at " + System.currentTimeMillis());
            }

            final long sleepInterval = 10;
            if (jobExecutor == null
                || jobExecutor.earliestJob_.getTargetExecutionTime() - System.currentTimeMillis() > sleepInterval) {
                try {
                    Thread.sleep(sleepInterval);
                }
                catch (final InterruptedException e) {
                    // nothing, probably a shutdown notification
                }
            }
            if (shutdown_ || webClient_.get() == null) {
                break;
            }
            if (jobExecutor != null) {
                // execute the earliest job.
                if (LOG.isTraceEnabled()) {
                    LOG.trace("started executing job at " + System.currentTimeMillis());
                }
                jobExecutor.jobManager_.runSingleJob(jobExecutor.earliestJob_);
                if (LOG.isTraceEnabled()) {
                    LOG.trace("stopped executing job at " + System.currentTimeMillis());
                }
            }
        }
    }

    /**
     * Register a window with the eventLoop.
     * @param newWindow the new web window
     */
    public synchronized void addWindow(final WebWindow newWindow) {
        final JavaScriptJobManager jobManager = newWindow.getJobManager();
        if (jobManager != null && !contains(jobManager)) {
            jobManagerList_.add(new WeakReference<JavaScriptJobManager>(jobManager));
            startThreadIfNeeded();
        }
    }

    private boolean contains(final JavaScriptJobManager newJobManager) {
        for (WeakReference<JavaScriptJobManager> jobManagerRef : jobManagerList_) {
            if (jobManagerRef.get() == newJobManager) {
                return true;
            }
        }
        return false;
    }

    /** Notes that this thread has been shutdown. */
    public void shutdown() {
        shutdown_ = true;
        killThread();
    }

}
