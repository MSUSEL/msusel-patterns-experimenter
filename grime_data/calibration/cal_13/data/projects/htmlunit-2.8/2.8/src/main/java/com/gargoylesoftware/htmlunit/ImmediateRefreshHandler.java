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
import java.io.Serializable;
import java.net.URL;

/**
 * The default handler for page refreshes. This refresh handler immediately
 * refreshes the specified page, using the specified URL and ignoring the
 * wait time.
 *
 * If you want a refresh handler that does not ignore the wait time,
 * see {@link ThreadedRefreshHandler}.
 *
 * @version $Revision: 5658 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class ImmediateRefreshHandler implements RefreshHandler, Serializable  {

    private static final long serialVersionUID = 529942009235309224L;

    /**
     * Immediately refreshes the specified page using the specified URL.
     * @param page the page that is going to be refreshed
     * @param url the URL where the new page will be loaded
     * @param seconds the number of seconds to wait before reloading the page (ignored!)
     * @throws IOException if the refresh fails
     */
    public void handleRefresh(final Page page, final URL url, final int seconds) throws IOException {
        final WebWindow window = page.getEnclosingWindow();
        if (window == null) {
            return;
        }
        final WebClient client = window.getWebClient();
        if (page.getWebResponse().getWebRequest().getUrl().toExternalForm().equals(url.toExternalForm())
                && HttpMethod.GET == page.getWebResponse().getWebRequest().getHttpMethod()) {
            final String msg = "Refresh to " + url + " (" + seconds + "s) aborted by HtmlUnit: "
                + "Attempted to refresh a page using an ImmediateRefreshHandler "
                + "which could have caused an OutOfMemoryError "
                + "Please use WaitingRefreshHandler or ThreadedRefreshHandler instead.";
            throw new RuntimeException(msg);
        }
        client.getPage(window, new WebRequest(url));
    }

}
