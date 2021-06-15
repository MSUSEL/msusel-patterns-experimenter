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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link XHtmlPage}.
 *
 * @version $Revision: 5724 $
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class XHtmlPageTest extends WebTestCase {

    /**
     * Regression test for bug 2515873. Originally located in {@link com.gargoylesoftware.htmlunit.xml.XmlPageTest}.
     * @throws Exception if an error occurs
     */
    @Test
    public void xpath1() throws Exception {
        final String html
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE html PUBLIC \n"
            + "    \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:xhtml='http://www.w3.org/1999/xhtml'>\n"
            + "<body><DIV>foo</DIV></body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(getDefaultUrl(), html, "text/xml");
        client.setWebConnection(conn);

        final XHtmlPage page = client.getPage(getDefaultUrl());
        final DomNode body = page.getDocumentElement().getFirstChild().getNextSibling();
        final DomNode div = body.getFirstChild();

        assertEquals(HtmlBody.class, body.getClass());
        assertEquals("body", body.getLocalName());
        assertEquals("DIV", div.getLocalName());
        assertNotNull(page.getFirstByXPath(".//xhtml:body"));
        assertNotNull(page.getFirstByXPath(".//xhtml:DIV"));
        assertNull(page.getFirstByXPath(".//xhtml:div"));
    }

    /**
     * Tests a simplified real-life response from Ajax4jsf. Originally located in
     * {@link com.gargoylesoftware.htmlunit.xml.XmlPageTest}.
     * @throws Exception if an error occurs
     */
    @Test
    public void a4jResponse() throws Exception {
        final String content = "<html xmlns='http://www.w3.org/1999/xhtml'><head>"
            + "<script src='//:'></script>"
            + "</head><body><span id='j_id216:outtext'>Echo Hello World</span></body></html>";
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content, 200, "OK", "text/xml");
        client.setWebConnection(webConnection);
        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getWebRequest().getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals("text/xml", page.getWebResponse().getContentType());
        assertTrue(XHtmlPage.class.isInstance(page));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xpath2() throws Exception {
        final String html =
              "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:xhtml='http://www.w3.org/1999/xhtml'>\n"
            + "<body><xhtml:div>foo</xhtml:div></body></html>";

        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html, 200, "OK", "application/xhtml+xml");

        final WebClient client = getWebClient();
        client.setWebConnection(conn);
        final XHtmlPage page = client.getPage(URL_FIRST);

        assertEquals(1, page.getByXPath("//:body").size());
        assertEquals(0, page.getByXPath("//:BODY").size());
        assertEquals(0, page.getByXPath("//:bOdY").size());

        assertEquals(1, page.getByXPath("//xhtml:body").size());
        assertEquals(0, page.getByXPath("//xhtml:BODY").size());
        assertEquals(0, page.getByXPath("//xhtml:bOdY").size());

        assertEquals(1, page.getByXPath("//xhtml:div").size());
        assertEquals(0, page.getByXPath("//xhtml:DIV").size());
        assertEquals(0, page.getByXPath("//xhtml:dIv").size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void namespace() throws Exception {
        final String html =
              "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:xhtml='http://www.w3.org/1999/xhtml'>\n"
            + "<body><div>foo</div></body></html>";

        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html, 200, "OK", "application/xhtml+xml");

        final WebClient client = getWebClient();
        client.setWebConnection(conn);
        final XHtmlPage page = client.getPage(URL_FIRST);

        final HtmlDivision div = page.getFirstByXPath("//xhtml:div");
        assertEquals("http://www.w3.org/1999/xhtml", div.getNamespaceURI());
    }

}
