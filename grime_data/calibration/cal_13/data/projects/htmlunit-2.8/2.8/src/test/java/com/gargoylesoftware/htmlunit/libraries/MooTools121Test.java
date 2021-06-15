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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Tries;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with version 1.2.1 of the <a href="http://mootools.net/">MooTools JavaScript library</a>.
 *
 * @version $Revision: 5684 $
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class MooTools121Test extends WebTestCase {

    private WebClient client_;

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Tries(3)
    @SuppressWarnings("unchecked")
    public void mooTools() throws Exception {
        final String resource = "libraries/mootools/1.2.1/Specs/index.html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        client_ = getWebClient();
        final HtmlPage page = client_.getPage(url);

        final HtmlElement progress = page.getElementById("progress");
        client_.waitForBackgroundJavaScriptStartingBefore(2000 * 100);

        final String prevProgress = progress.asText();

        FileUtils.writeStringToFile(new File("/tmp/mootols.html"), page.asXml());
        final String xpath = "//ul[@class='specs']/li[@class!='success']";
        final List<HtmlElement> failures = (List<HtmlElement>) page.getByXPath(xpath);
        if (!failures.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            for (HtmlElement failure : failures) {
                sb.append(failure.asXml()).append("\n\n");
            }
            throw new AssertionFailedError(sb.toString());
        }

        assertEquals("364", page.getElementById("total_examples").asText());
        assertEquals("0", page.getElementById("total_failures").asText());
        assertEquals("0", page.getElementById("total_errors").asText());
        assertEquals("100", prevProgress);
    }

    /**
     * Performs post-test deconstruction.
     */
    @After
    public void tearDown() {
        client_.closeAllWindows();
    }

}
