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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.FormChild;

/**
 * The JavaScript object that represents an option.
 *
 * @version $Revision: 5864 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLOptionElement extends FormChild {

    private static final long serialVersionUID = 947015932373556314L;

    /**
     * Creates an instance.
     */
    public HTMLOptionElement() {
        // Empty.
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     * @param newText the text
     * @param newValue the value
     * @param defaultSelected Whether the option is initially selected
     * @param selected the current selection state of the option
     */
    public void jsConstructor(final String newText, final String newValue,
            final boolean defaultSelected, final boolean selected) {
        final HtmlPage page = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
        AttributesImpl attributes = null;
        if (defaultSelected) {
            attributes = new AttributesImpl();
            attributes.addAttribute(null, "selected", "selected", null, "selected");
        }

        final HtmlOption htmlOption = (HtmlOption) HTMLParser.getFactory(HtmlOption.TAG_NAME).createElement(
                page, HtmlOption.TAG_NAME, attributes);
        htmlOption.setSelected(selected);
        setDomNode(htmlOption);

        if (newText != null && !newText.equals("undefined")) {
            htmlOption.appendChild(new DomText(page, newText));
        }
        if (newValue != null && !newValue.equals("undefined")) {
            htmlOption.setValueAttribute(newValue);
        }
    }

    /**
     * Returns the value of the "value" property.
     * @return the value property
     */
    public String jsxGet_value() {
        String value = getDomNodeOrNull().getAttribute("value");
        if (value == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_170)) {
            value = getDomNodeOrNull().getText();
        }
        return value;
    }

    /**
     * Sets the value of the "value" property.
     * @param newValue the value property
     */
    public void jsxSet_value(final String newValue) {
        getDomNodeOrNull().setValueAttribute(newValue);
    }

    /**
     * Returns the value of the "text" property.
     * @return the text property
     */
    @Override
    public String jsxGet_text() {
        return getDomNodeOrNull().getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlOption getDomNodeOrNull() {
        return (HtmlOption) super.getDomNodeOrNull();
    }

    /**
     * Sets the value of the "text" property.
     * @param newText the text property
     */
    public void jsxSet_text(final String newText) {
        getDomNodeOrNull().setText(newText);
    }

    /**
     * Returns the value of the "selected" property.
     * @return the text property
     */
    public boolean jsxGet_selected() {
        return getDomNodeOrNull().isSelected();
    }

    /**
     * Sets the value of the "selected" property.
     * @param selected the new selected property
     */
    public void jsxSet_selected(final boolean selected) {
        getDomNodeOrNull().setSelected(selected);
    }

    /**
     * Returns the value of the "defaultSelected" property.
     * @return the text property
     */
    public boolean jsxGet_defaultSelected() {
        return getDomNodeOrNull().isDefaultSelected();
    }

    /**
     * Returns the value of the "label" property.
     * @return the label property
     */
    public String jsxGet_label() {
        return getDomNodeOrNull().getLabelAttribute();
    }

    /**
     * Sets the value of the "label" property.
     * @param label the new label property
     */
    public void jsxSet_label(final String label) {
        getDomNodeOrNull().setLabelAttribute(label);
    }
}
