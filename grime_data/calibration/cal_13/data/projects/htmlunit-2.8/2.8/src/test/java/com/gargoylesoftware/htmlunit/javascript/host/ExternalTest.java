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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Test for {@link External}.
 *
 * @version $Revision: 5466 $
 * @author Peter Faller
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ExternalTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void AutoCompleteSaveForm() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function fnSaveForm() {\n"
            + "  window.external.AutoCompleteSaveForm(oForm);\n"
            + "  oForm.AutoCompleteTest.value='';\n"
            + "  oForm.AutoCompleteIgnore.value='';\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='fnSaveForm()'>\n"
            + "<form name='oForm'>\n"
            + "\n"
            + "This text is saved:\n"
            + "<input type='text' name='AutoCompleteTest' value='abcdef'>\n"
            + "\n"
            + "This text is not saved:"
            + "<input type='text' name='AutoCompleteIgnore' autocomplete='off' value='ghijklm'>\n"
            + "\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }
}
