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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.NamedNodeMap}.
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class NamedNodeMapTest extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(FF = { "name=f", "id=f", "foo=bar", "baz=blah" }, IE = { "CORRECT THE EXPECTATION PLEASE!!!!" })
    public void testAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var f = document.getElementById('f');\n"
            + "    for(var i = 0; i < f.attributes.length; i++) {\n"
            + "      alert(f.attributes[i].name + '=' + f.attributes[i].value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='f' id='f' foo='bar' baz='blah'></form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "name", "f", "name", "f", "name", "f", "name", "f", "null" })
    public void testGetNamedItem_HTML() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var f = document.getElementById('f');\n"
            + "    alert(f.attributes.getNamedItem('name').nodeName);\n"
            + "    alert(f.attributes.getNamedItem('name').nodeValue);\n"
            + "    alert(f.attributes.getNamedItem('NaMe').nodeName);\n"
            + "    alert(f.attributes.getNamedItem('nAmE').nodeValue);\n"
            + "    alert(f.attributes.name.nodeName);\n"
            + "    alert(f.attributes.name.nodeValue);\n"
            + "    alert(f.attributes.NaMe.nodeName);\n"
            + "    alert(f.attributes.nAmE.nodeValue);\n"
            + "    alert(f.attributes.getNamedItem('notExisting'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='f' id='f' foo='bar' baz='blah'></form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetNamedItem_XML() throws Exception {
        final URL firstURL = new URL("http://htmlunit/first.html");
        final URL secondURL = new URL("http://htmlunit/second.xml");

        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    doc.load('" + "second.xml" + "');\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('name').nodeName);\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('name').nodeValue);\n"
            + "    alert(doc.documentElement.attributes.name.nodeName);\n"
            + "    alert(doc.documentElement.attributes.name.nodeValue);\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('NaMe'));\n"
            + "    alert(doc.documentElement.attributes.NaMe);\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('nonExistent'));\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<blah name='y'></blah>";

        final String[] expectedAlerts = new String[] {"name", "y", "name", "y", "null", "undefined", "null"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(firstURL, html);
        conn.setResponse(secondURL, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(firstURL);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "[object]", "[object]", "[object]" }, FF = { "undefined", "undefined", "undefined" })
    public void unspecifiedAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.body.attributes.language);\n"
            + "    alert(document.body.attributes.id);\n"
            + "    alert(document.body.attributes.dir);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }
}
