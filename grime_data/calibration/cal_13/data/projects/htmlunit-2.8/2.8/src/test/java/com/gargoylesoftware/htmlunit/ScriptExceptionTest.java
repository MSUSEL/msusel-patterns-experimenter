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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link ScriptException}.
 *
 * @version $Revision: 5658 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public final class ScriptExceptionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void constructor() throws Exception {
        final String message = "bla bla";
        final Throwable t = new RuntimeException(message);
        final HtmlPage page = loadPage(getBrowserVersion(), "<html></html>", null);
        final ScriptException exception = new ScriptException(page, t);

        assertEquals(t, exception.getCause());
        assertEquals(message, exception.getMessage());
    }

    /**
     * Tests access to the page where the exception occurred from the exception.
     * @throws Exception if the test fails
     */
    @Test
    public void getPage() throws Exception {
        final String html = "<html><script>notExisting()</script></html>";
        try {
            loadPage(getBrowserVersion(), html, null);
        }
        catch (final ScriptException e) {
            assertEquals(getDefaultUrl(), e.getPage().getWebResponse().getWebRequest().getUrl());
        }
    }

    /**
     * Test the JavaScript stacktrace.
     * Note: this test will fail when the information (line, col, source name) provided to execute
     * scripts is improved. In this case this unit test should be adapted IF the provided information
     * in the script stack trace gets better and provides more useful information to understand the
     * cause of a problem.
     * @throws Exception if the test fails
     */
    @Test
    public void scriptStackTrace() throws Exception {
        testScriptStackTrace("ScriptExceptionTest1");
        testScriptStackTrace("ScriptExceptionTest2");
    }

    private void testScriptStackTrace(final String baseFileName) throws Exception {
        try {
            final Locale locale = Locale.getDefault();
            // Set the default locale to US because Rhino messages are localized
            Locale.setDefault(Locale.US);

            loadPage(getBrowserVersion(), getFileContent(baseFileName + ".html"), null,
                new URL("http://www.gargoylesoftware.com/"));

            Locale.setDefault(locale);
        }
        catch (final ScriptException e) {
            final StringWriter stringWriter = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printScriptStackTrace(printWriter);

            final String expectedTrace = getFileContent(baseFileName + ".txt");
            assertEquals(expectedTrace, stringWriter.toString());
            return;
        }
        fail("Exception not thrown");
    }

    private String getFileContent(final String fileName) throws IOException {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
        assertNotNull(fileName, stream);
        return IOUtils.toString(stream);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void errorLineNumber() throws Exception {
        testErrorLineNumber("testJsError1.html", 6);
        testErrorLineNumber("testJsError2.html", 7);
        testErrorLineNumber("testJsError3.html", 6);
        testErrorLineNumber("testJsError4.html", 8);
    }

    private void testErrorLineNumber(final String fileName, final int errorLine) throws Exception {
        final WebClient webClient = getWebClient();
        final URL url = getClass().getClassLoader().getResource(fileName);
        assertNotNull(url);
        try {
            final HtmlPage page = webClient.getPage(url);
            page.<HtmlElement>getHtmlElementById("clickMe").click();
            fail();
        }
        catch (final ScriptException e) {
            assertEquals(errorLine, e.getFailingLineNumber());
        }
    }
}
