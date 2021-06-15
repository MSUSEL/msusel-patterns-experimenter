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
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Event;

/**
 * Wrapper for the HTML element "input".
 *
 * @version $Revision: 5926 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Mike Bresnahan
 * @author Daniel Gredler
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Benoit Heinrich
 * @author Ronald Brill
 */
public class HtmlRadioButtonInput extends HtmlInput {

    private static final long serialVersionUID = 425993174633373218L;

    private boolean defaultCheckedState_;
    private boolean valueAtFocus_;

    /**
     * Creates an instance.
     * If no value is specified, it is set to "on" as browsers do (eg IE6 and Mozilla 1.7)
     * even if spec says that it is not allowed
     * (<a href="http://www.w3.org/TR/REC-html40/interact/forms.html#adef-value-INPUT">W3C</a>).
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlRadioButtonInput(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);

        // default value for both IE6 and Mozilla 1.7 even if spec says it is unspecified
        if (getAttribute("value") == ATTRIBUTE_NOT_DEFINED) {
            setAttribute("value", "on");
        }

        defaultCheckedState_ = hasAttribute("checked");
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        if (defaultCheckedState_) {
            setAttribute("checked", "checked");
        }
        else {
            removeAttribute("checked");
        }
    }

    /**
     * Sets the "checked" attribute.
     *
     * @param isChecked true if this element is to be selected
     * @return the page that occupies this window after setting checked status
     * It may be the same window or it may be a freshly loaded one.
     */
    @Override
    public Page setChecked(final boolean isChecked) {
        final HtmlForm form = getEnclosingForm();
        final boolean changed = isChecked() != isChecked;

        Page page = getPage();
        if (isChecked) {
            if (form != null) {
                form.setCheckedRadioButton(this);
            }
            else if (page instanceof HtmlPage) {
                ((HtmlPage) page).setCheckedRadioButton(this);
            }
        }
        else {
            removeAttribute("checked");
        }

        if (changed && !getPage().getWebClient().getBrowserVersion()
                .hasFeature(BrowserVersionFeatures.EVENT_ONCHANGE_LOSING_FOCUS)) {
            final ScriptResult scriptResult = fireEvent(Event.TYPE_CHANGE);
            if (scriptResult != null) {
                page = scriptResult.getNewPage();
            }
        }
        return page;
    }

    /**
     * A radio button does not have a textual representation,
     * but we invent one for it because it is useful for testing.
     * @return "checked" or "unchecked" according to the radio state
     */
    // we need to preserve this method as it is there since many versions with the above documentation.
    @Override
    public String asText() {
        return super.asText();
    }

    /**
     * Override of default clickAction that makes this radio button the selected
     * one when it is clicked.
     *
     * @throws IOException if an IO error occurred
     */
    @Override
    protected void doClickAction() throws IOException {
        setChecked(true);
    }

    /**
     * {@inheritDoc}
     * Also sets the value to the new default value.
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        super.setDefaultValue(defaultValue);
        setValueAttribute(defaultValue);
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultChecked(boolean)
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        defaultCheckedState_ = defaultChecked;
        if (getPage().getWebClient().getBrowserVersion()
                .hasFeature(BrowserVersionFeatures.HTMLINPUT_DEFAULT_IS_CHECKED)) {
            setChecked(defaultChecked);
        }
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#isDefaultChecked()
     */
    @Override
    public boolean isDefaultChecked() {
        return defaultCheckedState_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isStateUpdateFirst() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onAddedToPage() {
        if (getPage().getWebClient().getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_4)) {
            setChecked(isDefaultChecked());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focus() {
        super.focus();
        valueAtFocus_ = isChecked();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void removeFocus() {
        super.removeFocus();

        final boolean fireOnChange = getPage().getWebClient().getBrowserVersion()
            .hasFeature(BrowserVersionFeatures.EVENT_ONCHANGE_LOSING_FOCUS);
        if (fireOnChange && valueAtFocus_ != isChecked()) {
            executeOnChangeHandlerIfAppropriate(this);
        }
        valueAtFocus_ = isChecked();
    }
}
