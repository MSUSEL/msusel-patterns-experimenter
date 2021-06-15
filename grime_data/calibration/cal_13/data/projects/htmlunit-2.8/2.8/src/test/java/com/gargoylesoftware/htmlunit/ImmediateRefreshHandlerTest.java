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

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link ImmediateRefreshHandler}.
 *
 * @version $Revision: 5660 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public final class ImmediateRefreshHandlerTest extends WebTestCase {

    /**
     * Regression test for bug 1211980: redirect on the same page after a post.
     * @throws Exception if the test fails
     */
    @Test
    public void testRefreshSamePageAfterPost() throws Exception {
        final WebClient client = getWebClient();
        client.setRefreshHandler(new ImmediateRefreshHandler());

        // connection will return a page with <meta ... refresh> for the first call
        // and the same page without it for the other calls
        final MockWebConnection webConnection = new MockWebConnection() {
            private int nbCalls_ = 0;
            @Override
            public WebResponse getResponse(final WebRequest request) throws IOException {
                String content = "<html><head>\n";
                if (nbCalls_ == 0) {
                    content += "<meta http-equiv='refresh' content='0;url="
                        + getDefaultUrl().toExternalForm()
                        + "'>\n";
                }
                content += "</head><body></body></html>";
                nbCalls_++;
                final StringWebResponse response = new StringWebResponse(content, request.getUrl());
                response.getWebRequest().setHttpMethod(request.getHttpMethod());
                return response;
            }
        };
        client.setWebConnection(webConnection);

        final WebRequest request = new WebRequest(getDefaultUrl());
        request.setHttpMethod(HttpMethod.POST);
        client.getPage(request);
    }
}
