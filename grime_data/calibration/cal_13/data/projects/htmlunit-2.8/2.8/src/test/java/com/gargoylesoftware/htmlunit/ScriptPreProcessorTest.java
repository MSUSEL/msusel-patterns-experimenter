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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link ScriptPreProcessor}.
 *
 * @version $Revision: 5569 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author David D. Kilzer
 * @author Chris Erskine
 * @author Hans Donner
 * @author Paul King
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Sudhan Moghe
 */
@RunWith(BrowserRunner.class)
public class ScriptPreProcessorTest extends WebServerTestCase {

    /**
     * Test the script preprocessor.
     * @throws IOException if the test fails
     */
    @Test
    public void testScriptPreProcessor() throws IOException {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final String alertText = "content";
        final String newAlertText = "newcontent";
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "<!--\n   alert('" + alertText + "');\n// -->\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        // Test null return from pre processor
        client.setScriptPreProcessor(new ScriptPreProcessor() {
            public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                    final int lineNumber, final HtmlElement htmlElement) {
                return null;
            }
        });
        client.setAlertHandler(new AlertHandler() {
            public void handleAlert(final Page page, final String message) {
                fail("The pre processor did not remove the JavaScript");
            }

        });
        client.getPage(URL_FIRST);

        // Test modify script in pre processor
        client.setScriptPreProcessor(new ScriptPreProcessor() {
            public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                    final int lineNumber, final HtmlElement htmlElement) {
                final int start = sourceCode.indexOf(alertText);
                final int end = start + alertText.length();

                return sourceCode.substring(0, start) + newAlertText + sourceCode.substring(end);
            }
        });
        client.setAlertHandler(new AlertHandler() {
            public void handleAlert(final Page page, final String message) {
                if (!message.equals(newAlertText)) {
                    fail("The pre processor did not modify the JavaScript");
                }
            }

        });
        client.getPage(URL_FIRST);
    }

    /**
     * Test the ScriptPreProcessor's ability to filter out a JavaScript method
     * that is not implemented without affecting the rest of the page.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testScriptPreProcessor_UnimplementedJavascript() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final String content = "<html><head><title>foo</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<script>document.unimplementedFunction();</script>\n"
            + "<script>alert('implemented function');</script>\n"
            + "</body></html>";

        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        client.setScriptPreProcessor(new ScriptPreProcessor() {
            public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
                    final int lineNumber, final HtmlElement htmlElement) {
                if (sourceCode.indexOf("unimplementedFunction") > -1) {
                    return "";
                }
                return sourceCode;
            }
        });
        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        client.getPage("http://page");

        assertEquals(1, alerts.size());
        assertEquals("implemented function", alerts.get(0).toString());
    }

    /**
     * Verifies that script preprocessing is applied to eval()'ed scripts (bug 2630555).
     * @throws Exception if the test fails
     */
    @Test
    public void testScriptPreProcessor_Eval() throws Exception {
        final String html = "<html><body><script>eval('aX'+'ert(\"abc\")');</script></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html);
        client.setWebConnection(conn);

        client.setScriptPreProcessor(new ScriptPreProcessor() {
            public String preProcess(final HtmlPage p, final String src, final String srcName,
                    final int lineNumber, final HtmlElement htmlElement) {
                return src.replaceAll("aXert", "alert");
            }
        });

        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        client.getPage(URL_FIRST);

        assertEquals(1, alerts.size());
        assertEquals("abc", alerts.get(0));
    }
}
