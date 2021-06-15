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

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Google App Engine specific subclass to facilitate execution of JS jobs.
 *
 * @version $Revision: 5842 $
 * @author Amit Manjhi
 *
 */
public class GAEJavaScriptExecutor extends JavaScriptExecutor {

    private static final long serialVersionUID = 6720347050164623356L;

    /** Creates an EventLoop for the webClient.
     *
     * @param webClient the provided webClient
     */
    public GAEJavaScriptExecutor(final WebClient webClient) {
        super(webClient);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void startThreadIfNeeded() {
        // no-op
    }

    /**
     * Executes the jobs in the eventLoop till timeoutMillis expires or the eventLoop becomes empty.
     * @param timeoutMillis the timeout in milliseconds
     * @return the number of jobs executed
     */
    @Override
    public int pumpEventLoop(final long timeoutMillis) {
        int count = 0;
        final long expirationTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < expirationTime) {
            final JobExecutor jobExecutor = getEarliestJob();
            if (jobExecutor == null) {
                break;
            }
            if (expirationTime < jobExecutor.getEarliestJob().getTargetExecutionTime()) {
                break;
            }
            // sleep if there is time remaining in the earliestJob.
            final long sleepTime = jobExecutor.getEarliestJob().getTargetExecutionTime()
                - System.currentTimeMillis();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            final boolean ran = jobExecutor.getJobManager().runSingleJob(jobExecutor.getEarliestJob());
            if (ran) {
                count++;
            }
        }
        return count;
    }

}
