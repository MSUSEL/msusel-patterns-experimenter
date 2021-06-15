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

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link FailingHttpStatusCodeException}.
 *
 * @version $Revision: 5724 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public final class FailingHttpStatusCodeExceptionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testConstructorWithWebResponse() throws Exception {
        final List<NameValuePair> emptyList = Collections.emptyList();
        final WebResponseData webResponseData = new WebResponseData(
                ArrayUtils.EMPTY_BYTE_ARRAY, HttpStatus.SC_NOT_FOUND, "not found",
                emptyList);
        final WebResponse webResponse = new WebResponse(webResponseData, URL_FIRST, HttpMethod.GET, 10);
        final FailingHttpStatusCodeException e = new FailingHttpStatusCodeException(webResponse);

        assertEquals(webResponse, e.getResponse());
        assertEquals(webResponse.getStatusMessage(), e.getStatusMessage());
        assertEquals(webResponse.getStatusCode(), e.getStatusCode());
        assertTrue("message doesn't contain failing url", e.getMessage().indexOf(URL_FIRST.toExternalForm()) > -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test(expected = FailingHttpStatusCodeException.class)
    public void failureByGetPage() throws Exception {
        getMockWebConnection().setDefaultResponse("", 404, "Not found", "text/html");
        getWebClientWithMockWebConnection().getPage(getDefaultUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test(expected = FailingHttpStatusCodeException.class)
    public void failureByClickLink() throws Exception {
        final String html = "<html><body><a href='doesntExist'>go</a></body></html>";
        getMockWebConnection().setDefaultResponse("", 404, "Not found", "text/html");
        final HtmlPage page = loadPageWithAlerts(html);
        page.getAnchors().get(0).click();
    }
}
