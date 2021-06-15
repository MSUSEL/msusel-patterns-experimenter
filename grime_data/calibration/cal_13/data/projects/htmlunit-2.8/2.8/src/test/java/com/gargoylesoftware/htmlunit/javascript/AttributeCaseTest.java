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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HtmlElement} attributes.
 *
 * @version $Revision: 5301 $
 * @author David D. Kilzer
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class AttributeCaseTest extends WebTestCase {

    private static final String ATTRIBUTE_NAME = "randomAttribute";
    private static final String ATTRIBUTE_VALUE = "someValue";
    private static final String ATTRIBUTE_VALUE_NEW = "newValue";

    private HtmlElement element_;
    private HtmlPage page_;

    /**
     * Tests {@link HtmlElement#getAttribute(String)} with a lower case name.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeLowerCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE, element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#getAttribute(String)} with a mixed case name.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeMixedCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE, element_.getAttribute(ATTRIBUTE_NAME));
    }

    /**
     * Tests {@link HtmlElement#getAttribute(String)} with an upper case name.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeUpperCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE, element_.getAttribute(ATTRIBUTE_NAME.toUpperCase()));
    }

    /**
     * Tests {@link HtmlElement#setAttribute(String,String)} with a lower case name.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttributeLowerCase() throws Exception {
        setupSetAttributeTest(ATTRIBUTE_NAME.toLowerCase(), ATTRIBUTE_VALUE, ATTRIBUTE_VALUE_NEW);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE_NEW,
            element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#setAttribute(String,String)} with a mixed case name.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttributeMixedCase() throws Exception {
        setupSetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE, ATTRIBUTE_VALUE_NEW);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE_NEW,
            element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#setAttribute(String,String)} with an upper case name.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttributeUpperCase() throws Exception {
        setupSetAttributeTest(ATTRIBUTE_NAME.toUpperCase(), ATTRIBUTE_VALUE, ATTRIBUTE_VALUE_NEW);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE_NEW,
            element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#removeAttribute(String)} with a lower case name.
     * @throws Exception if the test fails
     */
    @Test
    public void removeAttributeLowerCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        element_.removeAttribute(ATTRIBUTE_NAME.toLowerCase());
        Assert.assertEquals(page_.asXml(), "", element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#removeAttribute(String)} with a mixed case name.
     * @throws Exception if the test fails
     */
    @Test
    public void removeAttributeMixedCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        element_.removeAttribute(ATTRIBUTE_NAME);
        Assert.assertEquals(page_.asXml(), "", element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#removeAttribute(String)} with an upper case name.
     * @throws Exception if the test fails
     */
    @Test
    public void removeAttributeUpperCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        element_.removeAttribute(ATTRIBUTE_NAME.toUpperCase());
        Assert.assertEquals(page_.asXml(), "", element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#hasAttribute(String)} with a lower case name.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeLowerCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertTrue(page_.asXml(), element_.hasAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#hasAttribute(String)} with a mixed case name.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeMixedCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertTrue(page_.asXml(), element_.hasAttribute(ATTRIBUTE_NAME));
    }

    /**
     * Tests {@link HtmlElement#hasAttribute(String)} with an upper case name.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeUpperCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertTrue(page_.asXml(), element_.hasAttribute(ATTRIBUTE_NAME.toUpperCase()));
    }

    private void setupAttributeTest(final String content, final String elementId) throws Exception {
        page_ = loadPage(content);
        element_ = page_.getHtmlElementById(elementId);
    }

    private void setupGetAttributeTest(final String attributeName, final String attributeValue) throws Exception {
        final String elementId = "p-id";
        final String content = "<html><head><title>AttributeCaseTest</title></head><body>\n"
                          + "<p id=\"" + elementId + "\" " + attributeName + "=\"" + attributeValue + "\">\n"
                          + "</body></html>";

        setupAttributeTest(content, elementId);
    }

    private void setupSetAttributeTest(
            final String attributeName, final String attributeValue,
            final String newAttributeValue)
        throws Exception {

        final String elementId = "p-id";
        final String content
            = "<html><head><title>AttributeCaseTest</title></head><body>\n"
             + "<p id=\"" + elementId + "\" " + attributeName + "=\"" + attributeValue + "\">\n"
             + "<script language=\"javascript\" type=\"text/javascript\">\n<!--\n"
             + "  document.getElementById(\"" + elementId + "\").setAttribute(\"" + attributeName + "\", \""
             + newAttributeValue + "\");\n"
             + "\n// -->\n</script>\n"
             + "</body></html>";

        setupAttributeTest(content, elementId);
    }
}
