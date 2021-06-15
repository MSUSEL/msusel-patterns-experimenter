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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlAcronym;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride;
import com.gargoylesoftware.htmlunit.html.HtmlBig;
import com.gargoylesoftware.htmlunit.html.HtmlBlink;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlCenter;
import com.gargoylesoftware.htmlunit.html.HtmlCitation;
import com.gargoylesoftware.htmlunit.html.HtmlCode;
import com.gargoylesoftware.htmlunit.html.HtmlDefinition;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlEmphasis;
import com.gargoylesoftware.htmlunit.html.HtmlExample;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlKeyboard;
import com.gargoylesoftware.htmlunit.html.HtmlListing;
import com.gargoylesoftware.htmlunit.html.HtmlNoBreak;
import com.gargoylesoftware.htmlunit.html.HtmlPlainText;
import com.gargoylesoftware.htmlunit.html.HtmlS;
import com.gargoylesoftware.htmlunit.html.HtmlSample;
import com.gargoylesoftware.htmlunit.html.HtmlSmall;
import com.gargoylesoftware.htmlunit.html.HtmlStrike;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;
import com.gargoylesoftware.htmlunit.html.HtmlSubscript;
import com.gargoylesoftware.htmlunit.html.HtmlSuperscript;
import com.gargoylesoftware.htmlunit.html.HtmlTeletype;
import com.gargoylesoftware.htmlunit.html.HtmlUnderlined;
import com.gargoylesoftware.htmlunit.html.HtmlVariable;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;

/**
 * The JavaScript object "HTMLSpanElement".
 *
 * @version $Revision: 5876 $
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class HTMLSpanElement extends HTMLElement {

    private static final long serialVersionUID = -1837052392526933150L;

    /**
     * Creates an instance.
     */
    public HTMLSpanElement() {
        // Empty.
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);
        final HtmlElement element = (HtmlElement) domNode;
        final BrowserVersion browser = getBrowserVersion();
        if (browser.hasFeature(BrowserVersionFeatures.GENERATED_90)) {
            if ((element instanceof HtmlAbbreviated && browser.hasFeature(BrowserVersionFeatures.HTMLABBREVIATED))
                || element instanceof HtmlAcronym
                || element instanceof HtmlAddress
                || element instanceof HtmlBidirectionalOverride
                || element instanceof HtmlBig
                || element instanceof HtmlBold
                || element instanceof HtmlBlink
                || element instanceof HtmlCenter
                || element instanceof HtmlCitation
                || element instanceof HtmlCode
                || element instanceof HtmlDefinition
                || element instanceof HtmlExample
                || element instanceof HtmlEmphasis
                || element instanceof HtmlItalic
                || element instanceof HtmlKeyboard
                || element instanceof HtmlListing
                || element instanceof HtmlNoBreak
                || element instanceof HtmlPlainText
                || element instanceof HtmlS
                || element instanceof HtmlSample
                || element instanceof HtmlSmall
                || element instanceof HtmlStrike
                || element instanceof HtmlStrong
                || element instanceof HtmlSubscript
                || element instanceof HtmlSuperscript
                || element instanceof HtmlTeletype
                || element instanceof HtmlUnderlined
                || element instanceof HtmlVariable) {
                ActiveXObject.addProperty(this, "cite", true, true);
            }
            if ((element instanceof HtmlAbbreviated && browser.hasFeature(BrowserVersionFeatures.HTMLABBREVIATED))
                    || element instanceof HtmlAcronym
                    || element instanceof HtmlBold
                    || element instanceof HtmlBidirectionalOverride
                    || element instanceof HtmlBig
                    || element instanceof HtmlBlink
                    || element instanceof HtmlCitation
                    || element instanceof HtmlCode
                    || element instanceof HtmlDefinition
                    || element instanceof HtmlEmphasis
                    || element instanceof HtmlItalic
                    || element instanceof HtmlKeyboard
                    || element instanceof HtmlNoBreak
                    || element instanceof HtmlS
                    || element instanceof HtmlSample
                    || element instanceof HtmlSmall
                    || element instanceof HtmlStrike
                    || element instanceof HtmlStrong
                    || element instanceof HtmlSubscript
                    || element instanceof HtmlSuperscript
                    || element instanceof HtmlTeletype
                    || element instanceof HtmlUnderlined
                    || element instanceof HtmlVariable) {
                ActiveXObject.addProperty(this, "dateTime", true, true);
            }
        }
    }

    /**
     * Returns the value of the "cite" property.
     * @return the value of the "cite" property
     */
    public String jsxGet_cite() {
        String cite = getDomNodeOrDie().getAttribute("cite");
        if (cite == NOT_FOUND) {
            cite = "";
        }
        return cite;
    }

    /**
     * Returns the value of the "cite" property.
     * @param cite the value
     */
    public void jsxSet_cite(final String cite) {
        getDomNodeOrDie().setAttribute("cite", cite);
    }

    /**
     * Returns the value of the "dateTime" property.
     * @return the value of the "dateTime" property
     */
    public String jsxGet_dateTime() {
        String dateTime = getDomNodeOrDie().getAttribute("datetime");
        if (dateTime == NOT_FOUND) {
            dateTime = "";
        }
        return dateTime;
    }

    /**
     * Returns the value of the "dateTime" property.
     * @param dateTime the value
     */
    public void jsxSet_dateTime(final String dateTime) {
        getDomNodeOrDie().setAttribute("datetime", dateTime);
    }
}
