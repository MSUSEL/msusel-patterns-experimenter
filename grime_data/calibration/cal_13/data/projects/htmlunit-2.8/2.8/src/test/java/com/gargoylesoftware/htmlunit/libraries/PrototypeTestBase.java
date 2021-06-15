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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Base class for tests for compatibility with <a href="http://prototype.conio.net/">Prototype</a>.
 *
 * @version $Revision: 5923 $
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public abstract class PrototypeTestBase extends WebServerTestCase {

    private static final Log LOG = LogFactory.getLog(PrototypeTestBase.class);

    private WebClient webClient_;

    /**
     * Gets the prototype tested version.
     * @return the version
     */
    protected abstract String getVersion();

    /**
     * Runs the specified test.
     * @param filename the test file to run
     * @throws Exception if the test fails
     */
    protected void test(final String filename) throws Exception {
        webClient_ = getWebClient();
        final HtmlPage page =
            webClient_.getPage("http://localhost:" + PORT + "/test/unit/" + filename);

        webClient_.waitForBackgroundJavaScript(25000);

        String expected = getExpectations(getBrowserVersion(), filename);
        final HtmlElement testlog = page.getHtmlElementById("testlog");
        String actual = testlog.asText();

        // ignore Info lines
        expected = expected.replaceAll("Info:.*", "Info: -- skipped for comparison --");
        actual = actual.replaceAll("Info:.*", "Info: -- skipped for comparison --");

        // dump the result page if not ok
        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null && !expected.equals(actual)) {
            final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            final File f = new File(tmpDir, "prototype" + getVersion() + "_result_" + filename);
            FileUtils.writeStringToFile(f, page.asXml(), "UTF-8");
            LOG.info("Test result for " + filename + " written to: " + f.getAbsolutePath());
        }

        assertEquals(expected, actual);
    }

    private String getExpectations(final BrowserVersion browserVersion, final String filename)
        throws IOException {
        final String fileNameBase = StringUtils.substringBeforeLast(filename, ".");
        final String baseName = "src/test/resources/libraries/prototype/" + getVersion() + "/expected." + fileNameBase;

        File expectationsFile = null;
        // version specific to this browser (or browser group)?
        String browserSuffix = "." + browserVersion.getNickname();
        while (browserSuffix.length() > 0) {
            final File file = new File(baseName + browserSuffix + ".txt");
            if (file.exists()) {
                expectationsFile = file;
                break;
            }
            browserSuffix = browserSuffix.substring(0, browserSuffix.length() - 1);
        }

        // generic version
        if (expectationsFile == null) {
            expectationsFile = new File(baseName + ".txt");
            if (!expectationsFile.exists()) {
                throw new FileNotFoundException("Can't find expectations file for test " + filename
                        + "(" + browserVersion.getNickname() + ")");
            }
        }

        return FileUtils.readFileToString(expectationsFile, "UTF-8");
    }

    /**
     * Performs pre-test initialization.
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        startWebServer("src/test/resources/libraries/prototype/" + getVersion());
    }

    /**
     * Performs post-test deconstruction.
     * Ensures everything stops in the WebClient.
     * @throws Exception if an error occurs
     */
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        webClient_.closeAllWindows();
    }
}
