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

import java.lang.ref.WeakReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A JavaScript-triggered background job representing the execution of some JavaScript code.
 *
 * @version $Revision: 5559 $
 * @author Daniel Gredler
 * @see MemoryLeakTest
 */
public abstract class JavaScriptExecutionJob extends JavaScriptJob {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(JavaScriptExecutionJob.class);

    /** The label for this job. */
    private final String label_;

    /** The window to which this job belongs (weakly referenced, so as not to leak memory). */
    private final WeakReference<WebWindow> window_;

    /**
     * Creates a new JavaScript execution job, where the JavaScript code to execute is a string.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (may be <tt>null</tt>)
     * @param label the label for the job
     * @param window the window to which the job belongs
     */
    public JavaScriptExecutionJob(final int initialDelay, final Integer period, final String label,
        final WebWindow window) {
        super(initialDelay, period);
        label_ = label;
        window_ = new WeakReference<WebWindow>(window);
    }

    /** {@inheritDoc} */
    public void run() {
        final WebWindow w = window_.get();
        if (w == null) {
            // The window has been garbage collected! No need to execute, obviously.
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Executing " + this + ".");
        }

        try {
            // Verify that the window is still open and the current page is the same.
            final HtmlPage page = (HtmlPage) w.getEnclosedPage();
            if (w.getEnclosedPage() != page || !w.getWebClient().getWebWindows().contains(w)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("The page that originated this job doesn't exist anymore. Execution cancelled.");
                }
                return;
            }
            else if (w.isClosed()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Enclosing window is now closed. Execution cancelled.");
                }
                return;
            }
            runJavaScript(page);
        }
        finally {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Finished executing " + this + ".");
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "JavaScript Execution Job " + getId() + ": " + label_;
    }

    /**
     * Run the JavaScript from the concrete class.
     * @param page the {@link HtmlPage} that owns the script
     */
    protected abstract void runJavaScript(final HtmlPage page);

}
