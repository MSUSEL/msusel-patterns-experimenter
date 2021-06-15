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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlLabel}.
 *
 * @version $Revision: 5905 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlLabelTest extends WebTestCase {

    /**
     * Verifies that a checkbox is toggled when the related label is clicked.
     * @throws Exception if the test fails
     */
    @Test
    public void test_click() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + " <input type='checkbox' name='checkbox' id='testCheckbox' onclick='alert(\"checkbox\")'/>\n"
            + " <label for='testCheckbox' id='testLabel' onclick='alert(\"label\")'>Check me</label>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlCheckBoxInput checkBox = page.getHtmlElementById("testCheckbox");

        assertFalse(checkBox.isChecked());
        final HtmlLabel label = page.getHtmlElementById("testLabel");
        label.click();
        assertTrue(checkBox.isChecked());
        final String[] expectedAlerts = {"label", "checkbox"};
        assertEquals(expectedAlerts, collectedAlerts);
        label.click();
        assertFalse(checkBox.isChecked());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test_getReferencedElement() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + " <input type='checkbox' name='checkbox' id='testCheckbox'/>\n"
            + " <label for='testCheckbox' id='testLabel1'>Check me</label>\n"
            + " <label for='notExisting' id='testLabel2'>Check me too</label>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlCheckBoxInput checkBox = page.getHtmlElementById("testCheckbox");

        final HtmlLabel label = page.getHtmlElementById("testLabel1");
        assertTrue(checkBox == label.getReferencedElement());
        final HtmlLabel label2 = page.getHtmlElementById("testLabel2");
        assertNull(label2.getReferencedElement());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLLabelElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<label id='myId'>Item</label>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(HtmlLabel.class.isInstance(page.getHtmlElementById("myId")));
    }
}
