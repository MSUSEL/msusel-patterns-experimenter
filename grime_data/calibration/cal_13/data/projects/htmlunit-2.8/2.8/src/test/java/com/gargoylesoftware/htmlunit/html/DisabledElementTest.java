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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests the <code>isDisabled()</code> method on all of the elements that must implement the <code>disabled</code>
 * attribute:  <code>button</code>, <code>input</code>, <code>optgroup</code>, <code>option</code>, <code>select</code>
 * and <code>textarea</code>.
 *
 * @version $Revision: 5347 $
 * @author David D. Kilzer
 * @author Ahmed Ashour
 */
@RunWith(Parameterized.class)
public class DisabledElementTest extends WebTestCase {

    /**
     * Tests data.
     * @return tests data
     */
    @Parameters
    public static Collection<String[]> data() {
        return Arrays.asList(new String[][] {
            {"<button id='element1' {0}>foo</button>"},
            {"<input type='button' id='element1' {0}>"},
            {"<input type='checkbox' id='element1' {0}>"},
            {"<input type='file' id='element1' {0}>"},
            {"<input type='hidden' id='element1' {0}>"},
            {"<input type='image' id='element1' {0}>"},
            {"<input type='password' id='element1' {0}>"},
            {"<input type='radio' id='element1' {0}>"},
            {"<input type='reset' id='element1' {0}>"},
            {"<input type='submit' id='element1' {0}>"},
            {"<input type='text' id='element1' {0}>"},
            {"<select><optgroup id='element1' {0}><option value='1'></option></optgroup></select>"},
            {"<select><option id='element1' value='1' {0}></option></select>"},
            {"<select id='element1' {0}><option value='1'></option></select>"},
            {"<textarea id='element1' {0}></textarea>"}
        });
    }

    private final String htmlContent_;

    /**
     * Creates an instance of the test class for testing <em>one</em> of the test methods.
     *
     * @param elementHtml the HTML representing the element to test with attribute <code>id='element1'</code>
     */
    public DisabledElementTest(final String elementHtml) {
        final String htmlContent = "<html><body><form id='form1'>{0}</form></body></html>";
        htmlContent_ = MessageFormat.format(htmlContent, new Object[]{elementHtml});
    }

    /**
     * Tests that the <code>isDisabled()</code> method returns <code>false</code> when the <code>disabled</code>
     * attribute does not exist.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void noDisabledAttribute() throws Exception {
        executeDisabledTest("", false);
    }

    /**
     * Tests that the <code>isDisabled()</code> method returns <code>true</code> when the <code>disabled</code>
     * attribute exists and is blank.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void blankDisabledAttribute() throws Exception {
        executeDisabledTest("disabled=''", true);
    }

    /**
     * Tests that the <code>isDisabled()</code> method returns <code>false</code> when the <code>disabled</code>
     * attribute exists and is <em>not</em> blank.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void populatedDisabledAttribute() throws Exception {
        executeDisabledTest("disabled='disabled'", true);
    }

    /**
     * Tests the <code>isDisabled()</code> method with the given parameters.
     *
     * @param disabledAttribute the definition of the <code>disabled</code> attribute
     * @param expectedIsDisabled the expected return value of the <code>isDisabled()</code> method
     * @throws Exception if test fails
     */
    private void executeDisabledTest(final String disabledAttribute, final boolean expectedIsDisabled)
        throws Exception {

        final String htmlContent = MessageFormat.format(htmlContent_, new Object[]{disabledAttribute});
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_3, htmlContent, collectedAlerts);

        final HtmlForm form = page.getHtmlElementById("form1");
        final DisabledElement element = (DisabledElement) form.getElementById("element1");
        assertEquals(expectedIsDisabled, element.isDisabled());
    }

}
