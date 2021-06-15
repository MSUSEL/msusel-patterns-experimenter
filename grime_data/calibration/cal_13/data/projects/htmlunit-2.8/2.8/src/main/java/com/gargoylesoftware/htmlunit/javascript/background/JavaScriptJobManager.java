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

import com.gargoylesoftware.htmlunit.Page;

/**
 * A manager for {@link JavaScriptJob}s.
 *
 * @version $Revision: 5842 $
 * @author Daniel Gredler
 */
public interface JavaScriptJobManager extends Serializable {

    /**
     * Returns the number of active jobs, including jobs that are currently executing and jobs that are
     * waiting to execute.
     * @return the number of active jobs
     */
    int getJobCount();

    /**
     * Adds the specified job to this job manager, assigning it an ID. If the specified page is not currently
     * loaded in the window which owns this job manager, the operation fails and this method returns <tt>0</tt>.
     * @param job the job to add to the job manager
     * @param page the page which is trying to add the job
     * @return the ID assigned to the job
     */
    int addJob(final JavaScriptJob job, final Page page);

    /**
     * Removes the specified job from the execution queue. This doesn't interrupt the job if it is currently running.
     * @param id the ID of the job to be removed from the execution queue
     */
    void removeJob(final int id);

    /**
     * Removes all jobs from the execution queue. This doesn't interrupt any jobs that may be currently running.
     */
    void removeAllJobs();

    /**
     * Stops the specified job and removes it from the execution queue, not even allowing the job to finish if it is
     * currently executing.
     * @param id the ID of the job to be stopped
     */
    void stopJob(final int id);

    /**
     * Blocks until all active jobs have finished executing. If a job is scheduled to begin executing after
     * <tt>(now + timeoutMillis)</tt>, this method will wait for <tt>timeoutMillis</tt> milliseconds and then
     * return <tt>false</tt>.
     * @param timeoutMillis the maximum amount of time to wait (in milliseconds); may be negative, in which
     *        case this method returns immediately
     * @return the number of background JavaScript jobs still executing or waiting to be executed when this
     *         method returns; will be <tt>0</tt> if there are no jobs left to execute
     */
    int waitForJobs(final long timeoutMillis);

    /**
     * Blocks until all jobs scheduled to start executing before <tt>(now + delayMillis)</tt> have finished executing.
     * If there is no background JavaScript task currently executing, and there is no background JavaScript task
     * scheduled to start executing within the specified time, this method returns immediately -- even if there are
     * tasks scheduled to be executed after <tt>(now + delayMillis)</tt>.
     * @param delayMillis the delay which determines the background tasks to wait for (in milliseconds);
     *        may be negative, as it is relative to the current time
     * @return the number of background JavaScript jobs still executing or waiting to be executed when this
     *         method returns; will be <tt>0</tt> if there are no jobs left to execute
     */
    int waitForJobsStartingBefore(final long delayMillis);

    /**
     * Shuts down this job manager and stops all of its jobs.
     */
    void shutdown();

    /**
     * Gets the earliest job for this manager.
     * @return <code>null</code> if none
     */
    JavaScriptJob getEarliestJob();

    /**
     * Runs the provided job if it is the right time for it.
     * @param job the job to run
     * @return returns true if the job was run.
     */
    boolean runSingleJob(final JavaScriptJob job);
}
