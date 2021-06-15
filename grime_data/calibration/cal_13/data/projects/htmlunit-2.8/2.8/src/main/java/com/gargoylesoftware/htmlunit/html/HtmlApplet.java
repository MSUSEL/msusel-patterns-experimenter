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

import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.applets.AppletClassLoader;
import com.gargoylesoftware.htmlunit.html.applets.AppletStubImpl;

/**
 * Wrapper for the HTML element "applet".
 *
 * @version $Revision: 5654 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HtmlApplet extends HtmlElement {

    private static final long serialVersionUID = -2868018454095564791L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "applet";

    private boolean downloaded_;
    private WebResponse appletWebResponse_;
    private Applet applet_;
    private AppletClassLoader appletClassLoader_;

    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlApplet(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute "codebase". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "codebase" or an empty string if that attribute isn't defined
     */
    public final String getCodebaseAttribute() {
        return getAttribute("codebase");
    }

    /**
     * Returns the value of the attribute "archive". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "archive" or an empty string if that attribute isn't defined
     */
    public final String getArchiveAttribute() {
        return getAttribute("archive");
    }

    /**
     * Returns the value of the attribute "code". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "code" or an empty string if that attribute isn't defined
     */
    public final String getCodeAttribute() {
        return getAttribute("code");
    }

    /**
     * Returns the value of the attribute "object". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "object" or an empty string if that attribute isn't defined
     */
    public final String getObjectAttribute() {
        return getAttribute("object");
    }

    /**
     * Returns the value of the attribute "alt". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "alt" or an empty string if that attribute isn't defined
     */
    public final String getAltAttribute() {
        return getAttribute("alt");
    }

    /**
     * Returns the value of the attribute "name". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "name" or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Returns the value of the attribute "width". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "width" or an empty string if that attribute isn't defined
     */
    public final String getWidthAttribute() {
        return getAttribute("width");
    }

    /**
     * Returns the value of the attribute "height". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "height" or an empty string if that attribute isn't defined
     */
    public final String getHeightAttribute() {
        return getAttribute("height");
    }

    /**
     * Returns the value of the attribute "align". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "align" or an empty string if that attribute isn't defined
     */
    public final String getAlignAttribute() {
        return getAttribute("align");
    }

    /**
     * Returns the value of the attribute "hspace". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "hspace" or an empty string if that attribute isn't defined
     */
    public final String getHspaceAttribute() {
        return getAttribute("hspace");
    }

    /**
     * Returns the value of the attribute "vspace". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "vspace" or an empty string if that attribute isn't defined
     */
    public final String getVspaceAttribute() {
        return getAttribute("vspace");
    }

    /**
     * Gets the applet referenced by this tag. Instantiates it if necessary.
     * @return the applet
     * @throws IOException in case of problem
     */
    public Applet getApplet() throws IOException {
        downloadContentIfNeeded();
        return applet_;
    }

    /**
     * Downloads the associated content specified in the code attribute.
     *
     * @throws IOException if an error occurs while downloading the content
     */
    @SuppressWarnings("unchecked")
    private void downloadContentIfNeeded() throws IOException {
        if (!downloaded_) {
            final HtmlPage page = (HtmlPage) getPage();
            final WebClient webclient = page.getWebClient();

            final String src = getArchiveAttribute();
            final URL url = page.getFullyQualifiedUrl(src);
            appletWebResponse_ = webclient.loadWebResponse(new WebRequest(url));

            downloaded_ = true;
        }

        appletClassLoader_ = new AppletClassLoader();
        appletClassLoader_.addToClassPath(appletWebResponse_);

        // simple case in a first time: only one class, the applet
        final String appletClassName = getCodeAttribute();
        try {
            final Class<Applet> appletClass = (Class<Applet>) appletClassLoader_.loadClass(appletClassName);
            applet_ = appletClass.newInstance();
            applet_.setStub(new AppletStubImpl(this));
            applet_.init();
            applet_.start();
        }
        catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (final InstantiationException e) {
            throw new RuntimeException(e);
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
