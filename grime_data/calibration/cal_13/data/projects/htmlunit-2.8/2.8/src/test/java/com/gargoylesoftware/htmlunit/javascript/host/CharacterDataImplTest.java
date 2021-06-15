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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link CharacterDataImpl}.
 *
 * @version $Revision: 5301 $
 * @author David K. Taylor
 */
@RunWith(BrowserRunner.class)
public class CharacterDataImplTest extends WebDriverTestCase {

    /**
     * Regression test for inline text nodes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some Text", "9", "3", "Some Text", "#text" })
    public void characterDataImpl_textNode() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    alert(text1.data);\n"
            + "    alert(text1.length);\n"
            + "    alert(text1.nodeType);\n"
            + "    alert(text1.nodeValue);\n"
            + "    alert(text1.nodeName);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for setting the data property of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some New Text", "Some New Text" })
    public void characterDataImpl_setData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.data='Some New Text';\n"
            + "    alert(text1.data);\n"
            + "    alert(text1.nodeValue);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for setting the nodeValue property of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some New Text", "Some New Text" })
    public void characterDataImpl_setNodeValue() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.nodeValue='Some New Text';\n"
            + "    alert(text1.data);\n"
            + "    alert(text1.nodeValue);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for appendData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some Text Appended")
    public void characterDataImpl_appendData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.appendData(' Appended');\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for deleteData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some Text")
    public void characterDataImpl_deleteData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.deleteData(5, 11);\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Not So New Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for insertData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some New Text")
    public void characterDataImpl_insertData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.insertData(5, 'New ');\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for replaceData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some New Text")
    public void characterDataImpl_replaceData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.replaceData(5, 3, 'New');\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Old Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for substringData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"New", "Some New Text" })
    public void characterDataImpl_substringData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    alert(text1.substringData(5, 3));\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some New Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for substringData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some ", "Text", "true" })
    public void textImpl_splitText() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    var text2=text1.splitText(5);\n"
            + "    alert(text1.data);\n"
            + "    alert(text2.data);\n"
            + "    alert(text1.nextSibling==text2);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }
}
