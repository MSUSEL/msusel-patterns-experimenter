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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This refresh handler spawns a new thread that waits the specified
 * number of seconds before refreshing the specified page, using the
 * specified URL.
 *
 * If you want a refresh handler that ignores the wait time, see
 * {@link ImmediateRefreshHandler}.
 *
 * @version $Revision: 5563 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 */
public class ThreadedRefreshHandler implements RefreshHandler {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(ThreadedRefreshHandler.class);

    /**
     * Refreshes the specified page using the specified URL after the specified number
     * of seconds.
     * @param page the page that is going to be refreshed
     * @param url the URL where the new page will be loaded
     * @param seconds the number of seconds to wait before reloading the page
     */
    public void handleRefresh(final Page page, final URL url, final int seconds) {
        final Thread thread = new Thread("ThreadedRefreshHandler Thread") {
            @Override
            public void run() {
                try {
                    new WaitingRefreshHandler().handleRefresh(page, url, seconds);
                }
                catch (final IOException e) {
                    LOG.error("Unable to refresh page!", e);
                    throw new RuntimeException("Unable to refresh page!", e);
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

}
