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

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.BasicUserPrincipal;
import org.apache.http.auth.Credentials;
import org.junit.Test;

/**
 * Tests for {@link WebRequest}.
 *
 * @version $Revision: 5724 $
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Rodney Gitzel
 */
public class WebRequestTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headers() throws Exception {
        final WebRequest request = new WebRequest(WebTestCase.getDefaultUrl());
        final int initialSize = request.getAdditionalHeaders().size();
        request.setAdditionalHeader("Accept", "nothing");
        assertEquals(initialSize, request.getAdditionalHeaders().size());
        request.setAdditionalHeader("ACCEPT", "compares");
        assertEquals(initialSize, request.getAdditionalHeaders().size());
        request.removeAdditionalHeader("ACcEpT");
        assertEquals(initialSize - 1, request.getAdditionalHeaders().size());
    }

    /**
     * A number of these refer to '285434' which is this defect:
     *  https://sourceforge.net/tracker/?func=detail&aid=2854634&group_id=47038&atid=448266.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void setUrl_eliminateDirUp() throws Exception {
        final URL url1 = new URL("http://htmlunit.sf.net/foo.html");
        final URL url2 = new URL("http://htmlunit.sf.net/dir/foo.html");
        final URL url3 = new URL("http://htmlunit.sf.net/dir/foo.html?a=1&b=2");

        // with directory/..
        WebRequest request = new WebRequest(new URL("http://htmlunit.sf.net/bla/../foo.html"));
        assertEquals(url1, request.getUrl());

        // with /..
        request = new WebRequest(new URL("http://htmlunit.sf.net/../foo.html"));
        assertEquals(url1, request.getUrl());

        // with /(\w\w)/.. (c.f. 2854634)
        request = new WebRequest(new URL("http://htmlunit.sf.net/dir/fu/../foo.html"));
        assertEquals(url2, request.getUrl());

        // with /../..
        request = new WebRequest(new URL("http://htmlunit.sf.net/../../foo.html"));
        assertEquals(url1, request.getUrl());

        // with ../.. (c.f. 2854634)
        request = new WebRequest(new URL("http://htmlunit.sf.net/dir/foo/bar/../../foo.html"));
        assertEquals(url2, request.getUrl());

        request = new WebRequest(
                          new URL("http://htmlunit.sf.net/dir/foo/bar/boo/hoo/silly/../../../../../foo.html"));
        assertEquals(url2, request.getUrl());

        // with /.
        request = new WebRequest(new URL("http://htmlunit.sf.net/./foo.html"));
        assertEquals(url1, request.getUrl());

        // with /\w//. (c.f. 2854634)
        request = new WebRequest(new URL("http://htmlunit.sf.net/a/./foo.html"));
        assertEquals(new URL("http://htmlunit.sf.net/a/foo.html"), request.getUrl());

        // with /.
        request = new WebRequest(new URL("http://htmlunit.sf.net/dir/./foo.html"));
        assertEquals(url2, request.getUrl());

        // with /. and query
        request = new WebRequest(new URL("http://htmlunit.sf.net/dir/./foo.html?a=1&b=2"));
        assertEquals(url3, request.getUrl());

        // pathological
        request = new WebRequest(
                new URL("http://htmlunit.sf.net/dir/foo/bar/./boo/hoo/silly/.././../../../.././foo.html?a=1&b=2"));
        assertEquals(url3, request.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void credentials() throws Exception {
        final URL url = new URL("http://john.smith:secret@localhost/");
        final WebRequest request = new WebRequest(url);
        final Credentials credentials =
            request.getCredentialsProvider().getCredentials(AuthScope.ANY);
        assertEquals(new BasicUserPrincipal("john.smith"), credentials.getUserPrincipal());
        assertEquals("secret", credentials.getPassword());
    }
}
