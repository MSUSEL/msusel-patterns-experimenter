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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlResetInput}.
 *
 * @version $Revision: 5905 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 */
public class HtmlResetInputTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void reset() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='text' name='textfield1' id='textfield1' value='foo'/>\n"
            + "<input type='password' name='password1' id='password1' value='foo'/>\n"
            + "<input type='hidden' name='hidden1' id='hidden1' value='foo'/>\n"
            + "<input type='radio' name='radioButton' value='foo' checked/>\n"
            + "<input type='radio' name='radioButton' value='bar'/>\n"
            + "<input type='checkbox' name='checkBox' value='check'/>\n"
            + "<select id='select1'>\n"
            + "    <option id='option1' selected value='1'>Option1</option>\n"
            + "    <option id='option2' value='2'>Option2</option>\n"
            + "</select>\n"
            + "<textarea id='textarea1'>Foobar</textarea>\n"
            + "<isindex prompt='Enter some text' id='isindex1'>\n"
            + "<input type='reset' name='resetButton' value='pushme'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlResetInput resetInput = form.getInputByName("resetButton");

        // change all the values to something else
        form.<HtmlRadioButtonInput>getFirstByXPath(
                "//input[@type='radio' and @name='radioButton' and @value='bar']").setChecked(true);
        form.<HtmlCheckBoxInput>getInputByName("checkBox").setChecked(true);
        page.<HtmlOption>getHtmlElementById("option1").setSelected(false);
        page.<HtmlOption>getHtmlElementById("option2").setSelected(true);
        page.<HtmlTextArea>getHtmlElementById("textarea1").setText("Flintstone");
        page.<HtmlTextInput>getHtmlElementById("textfield1").setValueAttribute("Flintstone");
        page.<HtmlHiddenInput>getHtmlElementById("hidden1").setValueAttribute("Flintstone");
        page.<HtmlPasswordInput>getHtmlElementById("password1").setValueAttribute("Flintstone");
        page.<HtmlIsIndex>getHtmlElementById("isindex1").setValue("Flintstone");

        // Check to make sure they did get changed
        assertEquals("bar", form.getCheckedRadioButton("radioButton").getValueAttribute());
        assertTrue(form.<HtmlCheckBoxInput>getInputByName("checkBox").isChecked());
        assertFalse(page.<HtmlOption>getHtmlElementById("option1").isSelected());
        assertTrue(page.<HtmlOption>getHtmlElementById("option2").isSelected());
        assertEquals("Flintstone", page.<HtmlTextArea>getHtmlElementById("textarea1").getText());
        assertEquals("Flintstone", page.<HtmlTextInput>getHtmlElementById("textfield1").getValueAttribute());
        assertEquals("Flintstone", page.<HtmlHiddenInput>getHtmlElementById("hidden1").getValueAttribute());
        assertEquals("Flintstone", page.<HtmlIsIndex>getHtmlElementById("isindex1").getValue());

        final HtmlPage secondPage = resetInput.click();
        assertSame(page, secondPage);

        // Check to make sure all the values have been set back to their original values.
        assertEquals("foo", form.getCheckedRadioButton("radioButton").getValueAttribute());
        assertFalse(form.<HtmlCheckBoxInput>getInputByName("checkBox").isChecked());
        assertTrue(page.<HtmlOption>getHtmlElementById("option1").isSelected());
        assertFalse(page.<HtmlOption>getHtmlElementById("option2").isSelected());
        assertEquals("Foobar", page.<HtmlTextArea>getHtmlElementById("textarea1").getText());
        assertEquals("foo", page.<HtmlTextInput>getHtmlElementById("textfield1").getValueAttribute());
        assertEquals("foo", page.<HtmlHiddenInput>getHtmlElementById("hidden1").getValueAttribute());
        assertEquals("foo", page.<HtmlPasswordInput>getHtmlElementById("password1").getValueAttribute());
        assertEquals("", page.<HtmlIsIndex>getHtmlElementById("isindex1").getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void outsideForm() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' type='reset' onclick='alert(1)'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        final HtmlResetInput input = page.getHtmlElementById("myInput");
        input.click();

        assertEquals(expectedAlerts, collectedAlerts);
    }
}
