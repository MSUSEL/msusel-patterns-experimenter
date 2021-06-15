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
package com.gargoylesoftware.htmlunit.source;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with version 1.2.6 of the <a href="http://jquery.com/">jQuery JavaScript library</a>.
 *
 * @version $Revision: 5563 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class JQueryExtractorSample extends WebTestCase {

    private static final Log LOG = LogFactory.getLog(JQueryExtractorSample.class);

    private static Iterator<HtmlElement> ITERATOR_;
    private static BrowserVersion BROWSER_VERSION_;
    private HtmlListItem listItem_;
    private int itemIndex_;

    /**
     * Before.
     */
    @Before
    public void init() {
        if (getBrowserVersion() != BROWSER_VERSION_) {
            try {
                ITERATOR_ = loadPage();
                BROWSER_VERSION_ = getBrowserVersion();
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        listItem_ = (HtmlListItem) ITERATOR_.next();
        itemIndex_ = 0;
    }

    @SuppressWarnings("unchecked")
    private void assertResult(final String expectedTestResult) {
        final String actual = ((HtmlElement) ((List) listItem_.getByXPath("./strong")).get(0)).asText();
        assertEquals(expectedTestResult, actual);
    }

    @SuppressWarnings("unchecked")
    private void assertAssertion(final String expectedAssertion) {
        final String actual = ((HtmlListItem) ((List) listItem_.getByXPath("./ol/li")).get(itemIndex_++)).asText();
        assertEquals(expectedAssertion, actual);
    }

    /**
     * Loads the jQuery unit test index page using the specified browser version, allows its
     * JavaScript to run to completion, and returns a list item iterator containing the test
     * results.
     *
     * @return a list item iterator containing the test results
     * @throws Exception if an error occurs
     */
    protected Iterator<HtmlElement> loadPage() throws Exception {
        final String resource = "jquery/" + getVersion() + "/test/index.html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        final WebClient client = getWebClient();

        final HtmlPage page = client.getPage(url);
        client.waitForBackgroundJavaScript(2 * 60 * 1000);

        // dump the result page if not OK
        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null) {
            final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            final File f = new File(tmpDir,
                "jquery" + getVersion() + '_' + getBrowserVersion().getNickname() + "_result.html");
            FileUtils.writeStringToFile(f, page.asXml(), "UTF-8");
            LOG.info("Test result for "
                    + getVersion() + '_' + getBrowserVersion().getNickname()
                    + " written to: " + f.getAbsolutePath());
        }

        final HtmlElement doc = page.getDocumentElement();
        final HtmlOrderedList tests = (HtmlOrderedList) doc.getElementById("tests");
        final Iterable<HtmlElement> i = tests.getChildElements();
        return i.iterator();
    }

    /**
     * Returns the jQuery version being tested.
     * @return the jQuery version being tested
     */
    protected String getVersion() {
        return "1.2.6";
    }
}
