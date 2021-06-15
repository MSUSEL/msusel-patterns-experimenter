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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.PluginConfiguration;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Unit tests for {@link MimeType}.
 *
 * @version $Revision: 5347 $
 * @author Marc Guillemot
 */
public class MimeTypeTest extends WebTestCase {

    /**
     * Tests default configuration of Flash plugin for Firefox.
     * @throws Exception if the test fails
     */
    @Test
    public void testFlashMimeType() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var mimeTypeFlash = navigator.mimeTypes['application/x-shockwave-flash'];\n"
            + "  alert(mimeTypeFlash);\n"
            + "  alert(mimeTypeFlash.suffixes);\n"
            + "  var pluginFlash = mimeTypeFlash.enabledPlugin;\n"
            + "  alert(pluginFlash.name);\n"
            + "  alert(pluginFlash == navigator.plugins[pluginFlash.name]);\n"
            + "  alert(pluginFlash == navigator.plugins.namedItem(pluginFlash.name));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final String[] expectedAlerts = {"[object MimeType]", "swf", "Shockwave Flash", "true", "true"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_3, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests default configuration of Flash plugin for Firefox.
     * @throws Exception if the test fails
     */
    @Test
    public void testRemoveFlashMimeType() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var mimeTypeFlash = navigator.mimeTypes['application/x-shockwave-flash'];\n"
            + "  alert(mimeTypeFlash);\n"
            + "  alert(navigator.plugins['Shockwave Flash']);\n"
            + "  alert(navigator.plugins.namedItem('Shockwave Flash'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";
        final String[] expectedAlerts = {"undefined", "undefined", "null"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        final Set<PluginConfiguration> plugins =
            new HashSet<PluginConfiguration>(BrowserVersion.FIREFOX_3.getPlugins());
        BrowserVersion.FIREFOX_3.getPlugins().clear();
        try {
            loadPage(BrowserVersion.FIREFOX_3, html, collectedAlerts);
            assertEquals(expectedAlerts, collectedAlerts);
        }
        finally {
            BrowserVersion.FIREFOX_3.getPlugins().addAll(plugins);
        }
    }
}
