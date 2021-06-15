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

import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

/**
 * <p>Tests for compatibility with <a href="http://tinymce.moxiecode.com/">TinyMCE</a>.</p>
 *
 * <p>TODO: fix "not yet implemented" tests</p>
 * <p>TODO: more tests to add</p>
 * <p>TODO: don't depend on external jQuery</p>
 *
 * @version $Revision: 5856 $
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class TinyMceTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void api() throws Exception {
        test("api", 348, 0);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void basic() throws Exception {
        test("basic", 89, 0);
    }

    @SuppressWarnings("unchecked")
    private void test(final String fileName, final int expectedTotal, final int expectedFailed) throws Exception {
        final URL url = getClass().getClassLoader().getResource("libraries/tinymce/3.2.7/tests/" + fileName + ".html");
        assertNotNull(url);

        final WebClient client = getWebClient();
        final HtmlPage page = (HtmlPage) client.getPage(url);
        client.waitForBackgroundJavaScript(5000L);

        final HtmlElement result = page.getElementById("testresult");
        final HtmlSpan totalSpan = result.getFirstByXPath("span[@class='all']");
        final int total = Integer.parseInt(totalSpan.asText());
        assertEquals(expectedTotal, total);

        final List<HtmlElement> failures = (List<HtmlElement>) page.getByXPath("//li[@class='fail']");
        String msg = "";
        for (HtmlElement failure : failures) {
            msg += failure.asXml() + "\n\n";
        }

        final HtmlSpan failedSpan = result.getFirstByXPath("span[@class='bad']");
        final int failed = Integer.parseInt(failedSpan.asText());
        Assert.assertEquals(msg, expectedFailed, failed);
    }

}
