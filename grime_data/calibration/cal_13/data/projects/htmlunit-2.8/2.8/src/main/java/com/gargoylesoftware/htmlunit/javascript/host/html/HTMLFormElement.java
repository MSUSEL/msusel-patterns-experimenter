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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FormFieldWithNameHistory;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A JavaScript object for a Form.
 *
 * @version $Revision: 5893 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Kent Tong
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535249.aspx">MSDN documentation</a>
 */
public class HTMLFormElement extends HTMLElement implements Function {

    private static final long serialVersionUID = -1860993922147246513L;

    private HTMLCollection elements_; // has to be a member to have equality (==) working

    /**
     * Creates an instance. A default constructor is required for all JavaScript objects.
     */
    public HTMLFormElement() {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHtmlElement(final HtmlElement htmlElement) {
        super.setHtmlElement(htmlElement);
        final HtmlForm htmlForm = getHtmlForm();
        htmlForm.setScriptObject(this);
    }

    /**
     * Returns the value of the JavaScript attribute "name".
     * @return the value of this attribute
     */
    public String jsxGet_name() {
        return getHtmlForm().getNameAttribute();
    }

    /**
     * Sets the value of the JavaScript attribute "name".
     * @param name the new value
     */
    public void jsxSet_name(final String name) {
        WebAssert.notNull("name", name);
        getHtmlForm().setNameAttribute(name);
    }

    /**
     * Returns the value of the JavaScript attribute "elements".
     * @return the value of this attribute
     */
    public HTMLCollection jsxGet_elements() {
        if (elements_ == null) {
            final HtmlForm htmlForm = getHtmlForm();

            elements_ = new HTMLCollection(this) {
                private static final long serialVersionUID = -2554743215194459203L;

                @Override
                protected List<Object> computeElements() {
                    final List<Object> response = super.computeElements();
                    response.addAll(htmlForm.getLostChildren());
                    return response;
                }

                @Override
                protected Object getWithPreemption(final String name) {
                    final List<HtmlElement> matchingElements = new ArrayList<HtmlElement>();
                    for (final Object o : getElements()) {
                        final HtmlElement elt = (HtmlElement) o;
                        if (isAccessibleByIdOrName(elt, name)) {
                            matchingElements.add(elt);
                        }
                    }
                    if (matchingElements.isEmpty()) {
                        return NOT_FOUND;
                    }
                    else if (matchingElements.size() == 1) {
                        return matchingElements.get(0).getScriptObject();
                    }
                    return new HTMLCollection(htmlForm, matchingElements);
                }
            };
            final String xpath = ".//*[(name() = 'input' or name() = 'button'"
                    + " or name() = 'select' or name() = 'textarea')]";
            elements_.init(htmlForm, xpath);

        }
        return elements_;
    }

    /**
     * Returns the value of the JavaScript attribute "length".
     * Does not count input type=image elements as browsers (IE6, Mozilla 1.7) do
     * (cf <a href="http://msdn.microsoft.com/en-us/library/ms534101.aspx">MSDN doc</a>)
     * @return the value of this attribute
     */
    public int jsxGet_length() {
        final int all = jsxGet_elements().jsxGet_length();
        final int images = getHtmlForm().getElementsByAttribute("input", "type", "image").size();
        return all - images;
    }

    /**
     * Returns the value of the JavaScript attribute "action".
     * @return the value of this attribute
     */
    public String jsxGet_action() {
        String action = getHtmlForm().getActionAttribute();
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_169)) {
            try {
                action = ((HtmlPage) getHtmlForm().getPage()).getFullyQualifiedUrl(action).toExternalForm();
            }
            catch (final MalformedURLException e) {
                // nothing, return action attribute
            }
        }
        return action;
    }

    /**
     * Sets the value of the JavaScript attribute "action".
     * @param action the new value
     */
    public void jsxSet_action(final String action) {
        WebAssert.notNull("action", action);
        getHtmlForm().setActionAttribute(action);
    }

    /**
     * Returns the value of the JavaScript attribute "method".
     * @return the value of this attribute
     */
    public String jsxGet_method() {
        return getHtmlForm().getMethodAttribute();
    }

    /**
     * Sets the value of the JavaScript attribute "method".
     * @param method the new value
     */
    public void jsxSet_method(final String method) {
        WebAssert.notNull("method", method);
        getHtmlForm().setMethodAttribute(method);
    }

    /**
     * Returns the value of the JavaScript attribute "target".
     * @return the value of this attribute
     */
    public String jsxGet_target() {
        return getHtmlForm().getTargetAttribute();
    }

    /**
     * Returns the <tt>onsubmit</tt> event handler for this element.
     * @return the <tt>onsubmit</tt> event handler for this element
     */
    public Object jsxGet_onsubmit() {
        return getEventHandlerProp("onsubmit");
    }

    /**
     * Sets the <tt>onsubmit</tt> event handler for this element.
     * @param onsubmit the <tt>onsubmit</tt> event handler for this element
     */
    public void jsxSet_onsubmit(final Object onsubmit) {
        setEventHandlerProp("onsubmit", onsubmit);
    }

    /**
     * Sets the value of the JavaScript attribute "target".
     * @param target the new value
     */
    public void jsxSet_target(final String target) {
        WebAssert.notNull("target", target);
        getHtmlForm().setTargetAttribute(target);
    }

    /**
     * Returns the value of the JavaScript attribute "encoding".
     * @return the value of this attribute
     */
    public String jsxGet_encoding() {
        return getHtmlForm().getEnctypeAttribute();
    }

    /**
     * Sets the value of the JavaScript attribute "encoding".
     * @param encoding the new value
     */
    public void jsxSet_encoding(final String encoding) {
        WebAssert.notNull("encoding", encoding);
        getHtmlForm().setEnctypeAttribute(encoding);
    }

    private HtmlForm getHtmlForm() {
        return (HtmlForm) getDomNodeOrDie();
    }

    /**
     * Submits the form (at the end of the current script execution).
     *
     * @throws IOException if an IO error occurs
     */
    public void jsxFunction_submit()
        throws IOException {

        final HtmlPage page = (HtmlPage) getDomNodeOrDie().getPage();
        final WebClient webClient = page.getWebClient();

        final String action = getHtmlForm().getActionAttribute();
        if (action.startsWith("javascript:")) {
            final String js = StringUtils.substringAfter(action, "javascript:");
            webClient.getJavaScriptEngine().execute(page, js, "Form action", 0);
        }
        else {
            // download should be done ASAP, response will be loaded into a window later
            final WebRequest request = getHtmlForm().getWebRequest(null);
            final String target = page.getResolvedTarget(jsxGet_target());
            webClient.download(page.getEnclosingWindow(), target, request, "JS form.submit()");
        }
    }

    /**
     * Retrieves a form object or an object from an elements collection.
     * @param index Integer or String that specifies the object or collection to retrieve.
     *              If this parameter is an integer, it is the zero-based index of the object.
     *              If this parameter is a string, all objects with matching name or id properties are retrieved,
     *              and a collection is returned if more than one match is made
     * @param subIndex Optional. Integer that specifies the zero-based index of the object to retrieve
     *              when a collection is returned
     * @return an object or a collection of objects if successful, or null otherwise
     */
    public Object jsxFunction_item(final Object index, final Object subIndex) {
        if (index instanceof Number) {
            return jsxGet_elements().jsxFunction_item(index);
        }
        final HtmlForm htmlForm = getHtmlForm();

        final HTMLCollection elements = new HTMLCollection(this) {
            private static final long serialVersionUID = -2554743215194459203L;

            @Override
            protected List<Object> computeElements() {
                final List<Object> response = super.computeElements();
                response.addAll(htmlForm.getLostChildren());
                return response;
            }
        };
        final String xpath = ".//*[((name() = 'input' or name() = 'button'"
                + " or name() = 'select' or name() = 'textarea')) and @name='" + index + "']";
        elements.init(htmlForm, xpath);
        if (elements.getLength() == 0) {
            return null;
        }
        else if (elements.getLength() == 1) {
            return elements.jsxFunction_item(0);
        }
        if (subIndex instanceof Number) {
            return elements.jsxFunction_item(subIndex);
        }
        return elements;
    }

    /**
     * Resets this form.
     */
    public void jsxFunction_reset() {
        getHtmlForm().reset();
    }

    /**
     * Overridden to allow the retrieval of certain form elements by ID or name.
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected Object getWithPreemption(final String name) {
        final List<HtmlElement> elements = findElements(name);

        if (elements.isEmpty()) {
            return NOT_FOUND;
        }
        if (elements.size() == 1) {
            return getScriptableFor(elements.get(0));
        }

        final HTMLCollection collection = new HTMLCollection(getHtmlForm(), elements) {
            protected List<Object> computeElements() {
                return new ArrayList<Object>(findElements(name));
            };
        };
        return collection;
    }

    private List<HtmlElement> findElements(final String name) {
        final List<HtmlElement> elements = new ArrayList<HtmlElement>();
        addElements(name, getHtmlForm().getHtmlElementDescendants(), elements);
        addElements(name, getHtmlForm().getLostChildren(), elements);

        // If no form fields are found, IE and Firefox are able to find img elements by ID or name.
        if (elements.isEmpty()) {
            for (final DomNode node : getHtmlForm().getChildren()) {
                if (node instanceof HtmlImage) {
                    final HtmlImage img = (HtmlImage) node;
                    if (name.equals(img.getId()) || name.equals(img.getNameAttribute())) {
                        elements.add(img);
                    }
                }
            }
        }

        return elements;
    }

    private void addElements(final String name, final Iterable<HtmlElement> nodes,
        final List<HtmlElement> addTo) {
        for (final HtmlElement node : nodes) {
            if (isAccessibleByIdOrName(node, name)) {
                addTo.add(node);
            }
        }
    }

    /**
     * Indicates if the element can be reached by id or name in expressions like "myForm.myField".
     * @param element the element to test
     * @param name the name used to address the element
     * @return <code>true</code> if this element matches the conditions
     */
    private boolean isAccessibleByIdOrName(final HtmlElement element, final String name) {
        if ((element instanceof FormFieldWithNameHistory && !(element instanceof HtmlImageInput))) {
            final FormFieldWithNameHistory elementWithNames = (FormFieldWithNameHistory) element;
            if (name.equals(elementWithNames.getOriginalName())
                    || name.equals(element.getId())) {
                return true;
            }

            if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.FORMFIELD_REACHABLE_BY_NEW_NAMES)) {
                return false;
            }
            else if (name.equals(element.getAttribute("name"))
                || elementWithNames.getPreviousNames().contains(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the specified indexed property.
     * @param index the index of the property
     * @param start the scriptable object that was originally queried for this property
     * @return the property
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (getDomNodeOrNull() == null) {
            return NOT_FOUND; // typically for the prototype
        }
        return jsxGet_elements().get(index, ((HTMLFormElement) start).jsxGet_elements());
    }

    /**
     * {@inheritDoc}
     */
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_80)) {
            throw Context.reportRuntimeError("Not a function.");
        }
        if (args.length > 0) {
            final Object arg = args[0];
            if (arg instanceof String) {
                return ScriptableObject.getProperty(this, (String) arg);
            }
            else if (arg instanceof Number) {
                return ScriptableObject.getProperty(this, ((Number) arg).intValue());
            }
        }
        return Context.getUndefinedValue();
    }

    /**
     * {@inheritDoc}
     */
    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_81)) {
            throw Context.reportRuntimeError("Not a function.");
        }
        return null;
    }

}
