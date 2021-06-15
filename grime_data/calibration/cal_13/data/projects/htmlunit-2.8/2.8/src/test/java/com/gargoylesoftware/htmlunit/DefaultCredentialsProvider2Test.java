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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @version $Revision: 5801 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DefaultCredentialsProvider2Test extends WebDriverTestCase {

    private static String XHRInstantiation_ = "(window.XMLHttpRequest ? "
        + "new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'))";

    /**
     * {@inheritDoc}
     */
    protected boolean isBasicAuthentication() {
        return getWebDriver() instanceof HtmlUnitDriver;
    }

    /**
     * {@inheritDoc}
     */
    protected WebClient createNewWebClient() {
        final WebClient webClient = super.createNewWebClient();
        ((DefaultCredentialsProvider) webClient.getCredentialsProvider()).addCredentials("jetty", "jetty");
        return webClient;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthenticationTwice() throws Exception {
        getMockWebConnection().setResponse(URL_SECOND, "Hello World");
        final WebDriver driver = loadPage2("Hi There");
        assertTrue(driver.getPageSource().contains("Hi There"));
        driver.get(URL_SECOND.toExternalForm());
        assertTrue(driver.getPageSource().contains("Hello World"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello World")
    public void basicAuthenticationXHR() throws Exception {
        final String html = "<html><head><script>\n"
            + "var xhr = " + XHRInstantiation_ + ";\n"
            + "var handler = function() {\n"
            + "  if (xhr.readyState == 4)\n"
            + "    alert(xhr.responseText);\n"
            + "}\n"
            + "xhr.onreadystatechange = handler;\n"
            + "xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "xhr.send('');\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setDefaultResponse("Hello World");
        loadPageWithAlerts2(html);
    }
}
