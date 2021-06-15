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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link HtmlTextInput}.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 */
@RunWith(BrowserRunner.class)
public class HtmlTextInputTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type() throws Exception {
        final String html = "<html><head></head><body><input id='t'/></body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type("abc");
        assertEquals("abc", t.getValueAttribute());
        t.type('\b');
        assertEquals("ab", t.getValueAttribute());
        t.type('\b');
        assertEquals("a", t.getValueAttribute());
        t.type('\b');
        assertEquals("", t.getValueAttribute());
        t.type('\b');
        assertEquals("", t.getValueAttribute());
    }

    /**
     * This test caused a StringIndexOutOfBoundsException as of HtmlUnit-2.7-SNAPSHOT on 27.10.2009.
     * This came from the fact that cloneNode() uses clone() and the two HtmlTextInput instances
     * were referencing the same DoTypeProcessor: type in second field were reflected in the first one.
     * @throws Exception if the test fails
     */
    @Test
    public void type_StringIndexOutOfBoundsException() throws Exception {
        type_StringIndexOutOfBoundsException("<input type='text' id='t'>");
        type_StringIndexOutOfBoundsException("<input type='password' id='t'>");
        type_StringIndexOutOfBoundsException("<textarea id='t'></textarea>");
    }

    void type_StringIndexOutOfBoundsException(final String tag) throws Exception {
        final String html = "<html><head></head><body>\n"
            + tag + "\n"
            + "<script>\n"
            + "function copy(node) {\n"
            + "  e.value = '231';"
            + "}"
            + "var e = document.getElementById('t');\n"
            + "e.onkeyup = copy;\n"
            + "var c = e.cloneNode();\n"
            + "c.id = 't2';\n"
            + "document.body.appendChild(c);\n"
            + "</script>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlElement t = page.getElementById("t2");
        t.type("abc");
        assertEquals("abc", t.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeWhileDisabled() throws Exception {
        final String html = "<html><body><input id='t' disabled='disabled'/></body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput t = page.getHtmlElementById("t");
        t.type("abc");
        assertEquals("", t.getValueAttribute());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function handler(e) {\n"
            + "    if (e && e.target.value.length > 2)\n"
            + "      e.preventDefault();\n"
            + "    else if (!e && window.event.srcElement.value.length > 2)\n"
            + "      return false;\n"
            + "  }\n"
            + "  function init() {\n"
            + "    document.getElementById('text1').onkeydown = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "<input id='text1'/>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput text1 = page.getHtmlElementById("text1");
        text1.type("abcd");
        assertEquals("abc", text1.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeNewLine() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<form action='" + URL_SECOND + "'>\n"
            + "<input name='myText' id='myText'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);

        final HtmlTextInput textInput = firstPage.getHtmlElementById("myText");

        final HtmlPage secondPage = (HtmlPage) textInput.type('\n');
        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void selection() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(getSelection(document.getElementById('text1')).length);\n"
            + "  }\n"
            + "  function getSelection(element) {\n"
            + "    if (typeof element.selectionStart == 'number') {\n"
            + "      return element.value.substring(element.selectionStart, element.selectionEnd);\n"
            + "    } else if (document.selection && document.selection.createRange) {\n"
            + "      return document.selection.createRange().text;\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<input id='text1'/>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = { "undefined,undefined", "undefined,undefined", "3,undefined", "3,10" },
            FF = { "7,7", "11,11", "3,11", "3,10" })
    public void selection2_1() throws Exception {
        selection2("text", 3, 10);
        selection2("password", 3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = { "undefined,undefined", "undefined,undefined", "-3,undefined", "-3,15" },
            FF = { "7,7", "11,11", "0,11", "0,11" })
    public void selection2_2() throws Exception {
        selection2("text", -3, 15);
        selection2("password", -3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = { "undefined,undefined", "undefined,undefined", "10,undefined", "10,5" },
            FF = { "7,7", "11,11", "10,11", "5,5" })
    public void selection2_3() throws Exception {
        selection2("text", 10, 5);
        selection2("password", 10, 5);
    }

    private void selection2(final String type, final int selectionStart, final int selectionEnd) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='Bonjour' type='" + type + "'>\n"
            + "<script>\n"
            + "    var input = document.getElementById('myTextInput');\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.value = 'Hello there';\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.selectionStart = " + selectionStart + ";\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.selectionEnd = " + selectionEnd + ";\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    public void setSelectionText() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    document.selection.createRange().text = 'new';\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<input id='myInput' value='My old value'><br>\n"
            + "<input id='myButton' type='button' value='Test' onclick='test()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        final HtmlButtonInput button = page.getHtmlElementById("myButton");
        page.setFocusedElement(input);
        input.setSelectionStart(3);
        input.setSelectionEnd(6);
        button.click();
        assertEquals("My new value", input.getValueAttribute());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void typeWhenSelected() throws Exception {
        final String html =
              "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' value='Hello world'><br>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World");
        assertEquals("Bye World", input.getValueAttribute());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void typeWhen_selectPositionChanged() throws Exception {
        final String html =
              "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' value='Hello world'><br>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World!");
        assertEquals("Bye World!", input.getValueAttribute());

        input.type("\b");
        assertEquals("Bye World", input.getValueAttribute());

        input.setSelectionStart(4);
        input.setSelectionEnd(4);
        input.type("Bye ");
        assertEquals("Bye Bye World", input.getValueAttribute());

        input.type("\b\b\b\b");
        assertEquals("Bye World", input.getValueAttribute());

        input.setSelectionStart(0);
        input.setSelectionEnd(3);
        input.type("Hello");
        assertEquals("Hello World", input.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type_specialCharacters() throws Exception {
        final String html = "<html><head></head><body>"
            + "<form>"
            + "<input id='t' onkeyup='document.forms[0].lastKey.value = event.keyCode'>"
            + "<input id='lastKey'>"
            + "</form>"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlTextInput t = page.getHtmlElementById("t");
        final HtmlTextInput lastKey = page.getHtmlElementById("lastKey");
        t.type("abc");
        assertEquals("abc", t.getValueAttribute());
        assertEquals("67", lastKey.getValueAttribute());

        // character in private use area E000–F8FF
        t.type("\uE014");
        assertEquals("abc", t.getValueAttribute());

        // TODO: find a way to handle left & right keys, ...
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = "<html><head></head><body onload=''><input type='text' onkeydown='' /></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlPage page2 = clone(page);
        assertNotNull(page2);
    }

}
