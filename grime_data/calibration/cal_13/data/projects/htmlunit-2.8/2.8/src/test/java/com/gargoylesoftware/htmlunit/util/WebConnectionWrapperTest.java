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
package com.gargoylesoftware.htmlunit.util;

import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link WebConnectionWrapper}.
 *
 * @version $Revision: 5724 $
 * @author Marc Guillemot
 */
public class WebConnectionWrapperTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void wrapper() throws Exception {
        final List<NameValuePair> emptyList = Collections.emptyList();
        final WebResponseData data = new WebResponseData(new byte[]{}, HttpStatus.SC_OK, "", emptyList);
        final WebResponse response = new WebResponse(data, URL_FIRST, HttpMethod.GET, 0);
        final WebRequest wrs = new WebRequest(URL_FIRST);

        final WebConnection realConnection = new WebConnection() {
            public WebResponse getResponse(final WebRequest request) {
                assertSame(wrs, request);
                return response;
            }
        };

        final WebConnectionWrapper wrapper = new WebConnectionWrapper(realConnection);
        assertSame(response, wrapper.getResponse(wrs));
    }

}
