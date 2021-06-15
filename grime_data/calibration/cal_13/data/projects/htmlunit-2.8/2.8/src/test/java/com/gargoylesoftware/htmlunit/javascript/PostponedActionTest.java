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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for the {@link PostponedAction}.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class PostponedActionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void loadingJavaScript() throws Exception {
        final String firstContent = "<html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    document.getElementById('debugDiv').innerHTML += 'before, ';\n"
            + "    var iframe2 = document.createElement('iframe');\n"
            + "    iframe2.src = '" + URL_SECOND + "';\n"
            + "    document.body.appendChild(iframe2);\n"
            + "    var iframe3 = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe3);\n"
            + "    iframe3.src = '" + URL_THIRD + "';\n"
            + "    document.getElementById('debugDiv').innerHTML += 'after, ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='debugDiv'></div>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent
            = "<script>parent.document.getElementById('debugDiv').innerHTML += 'second.html, ';</script>";
        final String thirdContent
            = "<script>parent.document.getElementById('debugDiv').innerHTML += 'third.html, ';</script>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        conn.setResponse(URL_THIRD, thirdContent);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlDivision div = page.getHtmlElementById("debugDiv");
        assertEquals("before, after, second.html, third.html, ", div.getFirstChild().getNodeValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void loadingJavaScript2() throws Exception {
        final String firstContent = "<html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    document.getElementById('debugDiv').innerHTML += 'before, ';\n"
            + "    var iframe = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe);\n"
            + "    iframe.contentWindow.location.replace('" + URL_SECOND + "');\n"
            + "    document.getElementById('debugDiv').innerHTML += 'after, ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='debugDiv'></div>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent
            = "<script>parent.document.getElementById('debugDiv').innerHTML += 'second.html, ';</script>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlDivision div = page.getHtmlElementById("debugDiv");
        assertEquals("before, after, second.html, ", div.getFirstChild().getNodeValue());
    }
}
