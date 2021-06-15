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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlOption}.
 *
 * @version $Revision: 5905 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HtmlOptionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSelect() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' id='select1'>\n"
            + "<option value='option1' id='option1'>Option1</option>\n"
            + "<option value='option2' id='option2' selected='selected'>Option2</option>\n"
            + "<option value='option3' id='option3'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        final HtmlOption option2 = page.getHtmlElementById("option2");
        final HtmlOption option3 = page.getHtmlElementById("option3");

        assertFalse(option1.isSelected());
        assertTrue(option2.isSelected());
        assertFalse(option3.isSelected());

        option3.setSelected(true);

        assertFalse(option1.isSelected());
        assertFalse(option2.isSelected());
        assertTrue(option3.isSelected());

        option3.setSelected(false);

        assertFalse(option1.isSelected());
        assertFalse(option2.isSelected());
        assertFalse(option3.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetValue() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' id='select1'>\n"
            + "<option value='option1' id='option1'>Option1</option>\n"
            + "<option id='option2' selected>Number Two</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        final HtmlOption option2 = page.getHtmlElementById("option2");

        assertEquals("option1", option1.getValueAttribute());
        assertEquals("Number Two", option2.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetValue_ContentsIsValue() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<select name='select1' id='select1'>\n"
            + "     <option id='option1'>Option1</option>\n"
            + "     <option id='option2' selected>Number Two</option>\n"
            + "     <option id='option3'>\n  Number 3 with blanks </option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        assertEquals("Option1", option1.getValueAttribute());

        final HtmlOption option2 = page.getHtmlElementById("option2");
        assertEquals("Number Two", option2.getValueAttribute());

        final HtmlOption option3 = page.getHtmlElementById("option3");
        assertEquals("Number 3 with blanks", option3.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick() throws Exception {
        final String htmlContent
            = "<html><body>\n"
            + "<form id='form1'>\n"
            + "<select name='select1' id='select1'>\n"
            + "     <option id='option1'>Option1</option>\n"
            + "     <option id='option2' selected>Number Two</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        assertFalse(option1.isSelected());
        option1.click();
        assertTrue(option1.isSelected());
        option1.click();
        assertTrue(option1.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form><select>\n"
            + "<option id='option1'>option1</option>\n"
            + "<option id='option2' label='Number Two'/>\n"
            + "<option id='option3' label='overridden'>Number Three</option>\n"
            + "<option id='option4'>Number&nbsp;4</option>\n"
            + "</select>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        final HtmlOption option2 = page.getHtmlElementById("option2");
        final HtmlOption option3 = page.getHtmlElementById("option3");
        final HtmlOption option4 = page.getHtmlElementById("option4");

        assertEquals("option1", option1.asText());
        assertEquals("", option2.asText());
        assertEquals("Number Three", option3.asText());
        assertEquals("Number 4", option4.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLOptionElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<select>\n"
            + "  <option id='myId'>test1</option>\n"
            + "  <option id='myId2'>test2</option>\n"
            + "</select>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(HtmlOption.class.isInstance(page.getHtmlElementById("myId")));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "false", "false", "true", "true", "false" })
    public void testDisabled() throws Exception {
        final String html = "<html><body onload='test()'><form name='f'>\n"
            + "  <select name='s' id='s'>\n"
            + "    <option value='o1' id='o1'>One</option>\n"
            + "    <option value='o2' id='o2' disabled='disabled'>Two</option>\n"
            + "  </select>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var s = document.getElementById('s');\n"
            + "      var o1 = document.getElementById('o1');\n"
            + "      var o2 = document.getElementById('o2');\n"
            + "      alert(s.disabled);\n"
            + "      alert(o1.disabled);\n"
            + "      alert(o2.disabled);\n"
            + "      o1.disabled = true;\n"
            + "      o2.disabled = false;\n"
            + "      alert(o1.disabled);\n"
            + "      alert(o2.disabled);\n"
            + "    }\n"
            + "  </script>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final boolean disabled = getBrowserVersion().isFirefox();
        assertEquals(disabled, ((HtmlOption) page.getElementById("o1")).isDisabled());
        assertFalse(((HtmlOption) page.getElementById("o2")).isDisabled());
    }

}
