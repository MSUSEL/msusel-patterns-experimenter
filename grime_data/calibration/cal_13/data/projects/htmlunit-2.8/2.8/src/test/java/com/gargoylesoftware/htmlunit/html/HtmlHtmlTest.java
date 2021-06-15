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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlHtml}.
 *
 * @version $Revision: 5905 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlHtmlTest extends WebTestCase {
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void attributes() throws Exception {
        final String htmlContent = "<?xml version=\"1.0\"?>\n"
            + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml' lang='en' xml:lang='en'>\n"
            + "<head><title>test</title></head>\n"
            + "<body></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlHtml root = (HtmlHtml) page.getDocumentElement();
        assertEquals("en", root.getLangAttribute());
        assertEquals("en", root.getXmlLangAttribute());
    }

    /**
     * Regression test for
     * <a href="http://sf.net/support/tracker.php?aid=2865948">Bug 2865948</a>:
     * canonical XPath for html element was computed to "/html[2]" where a doctype
     * was present.
     * @throws Exception if the test fails
     */
    @Test
    public void canonicalXPath() throws Exception {
        final String htmlContent = "<?xml version=\"1.0\"?>\n"
            + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml' lang='en' xml:lang='en'>\n"
            + "<head><title>test</title></head>\n"
            + "<body></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlHtml root = (HtmlHtml) page.getDocumentElement();
        assertEquals("/html", root.getCanonicalXPath());
    }
}
