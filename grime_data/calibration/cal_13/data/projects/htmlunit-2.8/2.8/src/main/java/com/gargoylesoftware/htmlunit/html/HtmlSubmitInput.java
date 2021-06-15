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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "input".
 *
 * @version $Revision: 5864 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HtmlSubmitInput extends HtmlInput {

    private static final long serialVersionUID = -615974535731910492L;

    /**
     * Value to use if no specified <tt>value</tt> attribute.
     */
    private static final String DEFAULT_VALUE = "Submit Query";

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlSubmitInput(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
        if (getPage().getWebClient().getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_12)
                && !hasAttribute("value")) {
            setAttribute("value", DEFAULT_VALUE);
        }
    }

    /**
     * This method will be called if there either wasn't an onclick handler or there was
     * but the result of that handler was true. This is the default behavior of clicking
     * the element. The default implementation returns the current page - subclasses
     * requiring different behavior (like {@link HtmlSubmitInput}) will override this
     * method.
     *
     * @throws IOException if an IO error occurred
     */
    @Override
    protected void doClickAction() throws IOException {
        final HtmlForm form = getEnclosingForm();
        if (form != null) {
            form.submit(this);
        }
    }

    /**
     * {@inheritDoc} This method <b>does nothing</b> for submit input elements.
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        // Empty.
    }

    /**
     * {@inheritDoc} Returns "Submit Query" if <tt>value</tt> attribute is not defined.
     */
    // we need to preserve this method as it is there since many versions with the above documentation.
    @Override
    public String asText() {
        String text = getValueAttribute();
        if (text == ATTRIBUTE_NOT_DEFINED) {
            text = DEFAULT_VALUE;
        }
        return text;
    }

    /**
     * {@inheritDoc} Doesn't print the attribute if it is <tt>value="Submit Query"</tt>.
     */
    @Override
    protected void printOpeningTagContentAsXml(final PrintWriter printWriter) {
        printWriter.print(getTagName());

        for (final DomAttr attribute : getAttributesMap().values()) {
            if (!attribute.getNodeName().equals("value") || !attribute.getValue().equals(DEFAULT_VALUE)) {
                printWriter.print(" ");
                final String name = attribute.getNodeName();
                printWriter.print(name);
                printWriter.print("=\"");
                printWriter.print(StringEscapeUtils.escapeXml(attribute.getNodeValue()));
                printWriter.print("\"");
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * Returns "Submit Query" if <tt>name</tt> attribute is defined and <tt>value</tt> attribute is not defined.
     */
    @Override
    public NameValuePair[] getSubmitKeyValuePairs() {
        if (getNameAttribute().length() != 0 && !hasAttribute("value")) {
            return new NameValuePair[]{new NameValuePair(getNameAttribute(), DEFAULT_VALUE)};
        }
        return super.getSubmitKeyValuePairs();
    }
}
