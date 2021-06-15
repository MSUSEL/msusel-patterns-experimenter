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

import java.lang.reflect.Method;
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link ActiveXObject}.
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ActiveXObjectTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE)
    public void xmlHttpRequestFlavours() throws Exception {
        assertFalse(ActiveXObject.isXMLHttpRequest(null));
        assertFalse(ActiveXObject.isXMLHttpRequest("foo"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Microsoft.XMLHTTP"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.3.0"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.4.0"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.5.0"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.6.0"));
        assertTrue(ActiveXObject.isXMLDocument("Microsoft.XmlDom"));
        assertTrue(ActiveXObject.isXMLDocument("MSXML4.DOMDocument"));
        assertTrue(ActiveXObject.isXMLDocument("Msxml2.DOMDocument.6.0"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void xmlDocument() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    alert(doc);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(getBrowserVersion(), content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("exception: Automation server can't create object")
    @NotYetImplemented
    public void activex() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var ie = new ActiveXObject('InternetExplorer.Application');\n"
            + "      alert(ie);\n"
            + "    } catch(e) {alert('exception: ' + e.message);}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void activex2() throws Exception {
        if (!getBrowserVersion().isIE()) {
            throw new Exception();
        }
        if (!isJacobInstalled()) {
            return;
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var ie = new ActiveXObject('InternetExplorer.Application');\n"
            + "      alert(ie.FullName);\n"
            + "    } catch(e) {alert('exception: ' + e.message);}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {getProperty("InternetExplorer.Application", "FullName").toString()};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final WebClient client = getWebClient();
        client.setActiveXNative(true);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(getDefaultUrl(), html);
        client.setWebConnection(webConnection);

        client.getPage(getDefaultUrl());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Returns true if Jacob is installed, so we can use {@link WebClient#setActiveXNative(boolean)}.
     * @return whether Jacob is installed or not
     */
    @SuppressWarnings("unchecked")
    public static boolean isJacobInstalled() {
        try {
            final Class clazz = Class.forName("com.jacob.activeX.ActiveXComponent");
            final Method method = clazz.getMethod("getProperty", String.class);
            final Object activXComponenet =
                clazz.getConstructor(String.class).newInstance("InternetExplorer.Application");
            method.invoke(activXComponenet, "Busy");
            return true;
        }
        catch (final Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private Object getProperty(final String activeXName, final String property) throws Exception {
        final Class clazz = Class.forName("com.jacob.activeX.ActiveXComponent");
        final Method method = clazz.getMethod("getProperty", String.class);
        final Object activXComponenet = clazz.getConstructor(String.class).newInstance(activeXName);
        return method.invoke(activXComponenet, property);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void method() throws Exception {
        if (!getBrowserVersion().isIE()) {
            throw new Exception();
        }
        if (!isJacobInstalled()) {
            return;
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var ie = new ActiveXObject('InternetExplorer.Application');\n"
            + "      ie.PutProperty('Hello', 'There');\n"
            + "      alert(ie.GetProperty('Hello'));\n"
            + "    } catch(e) {alert('exception: ' + e.message);}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"There"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final WebClient client = getWebClient();
        client.setActiveXNative(true);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(getDefaultUrl(), html);
        client.setWebConnection(webConnection);

        client.getPage(getDefaultUrl());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void setProperty() throws Exception {
        if (!getBrowserVersion().isIE()) {
            throw new Exception();
        }
        if (!isJacobInstalled()) {
            return;
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var ie = new ActiveXObject('InternetExplorer.Application');\n"
            + "      var full = ie.FullScreen;\n"
            + "      ie.FullScreen = true;\n"
            + "      alert(ie.FullScreen);\n"
            + "      ie.FullScreen = full;\n"
            + "    } catch(e) {alert('exception: ' + e.message);}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"true"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final WebClient client = getWebClient();
        client.setActiveXNative(true);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(getDefaultUrl(), html);
        client.setWebConnection(webConnection);

        client.getPage(getDefaultUrl());
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
