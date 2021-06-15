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

/**
 * A JavaScript-triggered background job managed by a {@link JavaScriptJobManager}.
 *
 * @version $Revision: 5592 $
 * @author Daniel Gredler
 * @author Amit Manjhi
 */
public abstract class JavaScriptJob implements Runnable, Comparable<JavaScriptJob> {

    /** The job ID. */
    private Integer id_;

    /** The initial amount of time to wait before executing this job. */
    private final int initialDelay_;

    /** The amount of time to wait between executions of this job (may be <tt>null</tt>). */
    private final Integer period_;

    /**
     * The time at which this job should be executed.
     * Note: the browser will make its best effort to execute the job at the target
     * time, as specified by the timeout/interval.  However, depending on other
     * scheduled jobs, this target time may not be the actual time at which the job
     * is executed.
     */
    private long targetExecutionTime_;

    /** Creates a new job instance that executes once, immediately. */
    public JavaScriptJob() {
        this(0, null);
    }

    /**
     * Creates a new job instance.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (may be <tt>null</tt>)
     */
    public JavaScriptJob(final int initialDelay, final Integer period) {
        initialDelay_ = initialDelay;
        period_ = period;
        setTargetExecutionTime(initialDelay + System.currentTimeMillis());
    }

    /**
     * Sets the job ID.
     * @param id the job ID
     */
    public void setId(final Integer id) {
        id_ = id;
    }

    /**
     * Returns the job ID.
     * @return the job ID
     */
    public Integer getId() {
        return id_;
    }

    /**
     * Returns the initial amount of time to wait before executing this job.
     * @return the initial amount of time to wait before executing this job
     */
    public int getInitialDelay() {
        return initialDelay_;
    }

    /**
     * Returns the amount of time to wait between executions of this job (may be <tt>null</tt>).
     * @return the amount of time to wait between executions of this job (may be <tt>null</tt>)
     */
    public Integer getPeriod() {
        return period_;
    }

    /**
     * Returns <tt>true</tt> if this job executes periodically.
     * @return <tt>true</tt> if this job executes periodically
     */
    public boolean isPeriodic() {
        return period_  != null;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "JavaScript Job " + id_;
    }

    /** {@inheritDoc} */
    public int compareTo(final JavaScriptJob other) {
        return (int) (targetExecutionTime_ - other.getTargetExecutionTime());
    }

    /**
     * Returns the target execution time of the job.
     * @return the target execution time in ms
     */
    public long getTargetExecutionTime() {
        return targetExecutionTime_;
    }

    /**
     * Sets the target execution time of the job.
     * @param targetExecutionTime the new target execution time.
     */
    public void setTargetExecutionTime(final long targetExecutionTime) {
        targetExecutionTime_ = targetExecutionTime;
    }

}
