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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link IncorrectnessListener}.
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public final class IncorrectnessListenerTest extends WebTestCase {
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testNotification() throws Exception {
        final String html = "<html><head>\n"
                + "<meta http-equiv='set-cookie' content='webm=none; path=/; a=b;'>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        final WebClient webClient = getWebClient();
        final List<String> collectedIncorrectness = new ArrayList<String>();
        final IncorrectnessListener listener = new IncorrectnessListener() {
            public void notify(final String message, final Object origin) {
                collectedIncorrectness.add(message);
            }
        };
        webClient.setIncorrectnessListener(listener);

        final MockWebConnection webConnection = new MockWebConnection();
        webClient.setWebConnection(webConnection);
        webConnection.setDefaultResponse(html);
        webClient.getPage(URL_FIRST);

        final String[] expectedIncorrectness = {
            "set-cookie http-equiv meta tag: unknown attribute 'a'."
        };
        assertEquals(expectedIncorrectness, collectedIncorrectness);
    }
}
