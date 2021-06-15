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

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;

/**
 * Wrapper for the HTML element "area".
 *
 * @version $Revision: 5742 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlArea extends HtmlElement {

    private static final long serialVersionUID = 8933911141016200386L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "area";

    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlArea(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doClickAction() throws IOException {
        final HtmlPage enclosingPage = (HtmlPage) getPage();
        final WebClient webClient = enclosingPage.getWebClient();

        final String href = getHrefAttribute();
        if (href != null && href.length() > 0) {
            final HtmlPage page = (HtmlPage) getPage();
            if (TextUtil.startsWithIgnoreCase(href, JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
                page.executeJavaScriptIfPossible(
                    href, "javascript url", getStartLineNumber());
                return;
            }
            final URL url;
            try {
                url = enclosingPage.getFullyQualifiedUrl(getHrefAttribute());
            }
            catch (final MalformedURLException e) {
                throw new IllegalStateException(
                        "Not a valid url: " + getHrefAttribute());
            }
            final WebRequest request = new WebRequest(url);
            request.setAdditionalHeader("Referer", page.getWebResponse().getWebRequest().getUrl().toExternalForm());
            final WebWindow webWindow = enclosingPage.getEnclosingWindow();
            webClient.getPage(
                    webWindow,
                    enclosingPage.getResolvedTarget(getTargetAttribute()),
                    request);
        }
    }

    /**
     * Returns the value of the attribute "shape". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "shape" or an empty string if that attribute isn't defined
     */
    public final String getShapeAttribute() {
        return getAttribute("shape");
    }

    /**
     * Returns the value of the attribute "coords". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "coords" or an empty string if that attribute isn't defined
     */
    public final String getCoordsAttribute() {
        return getAttribute("coords");
    }

    /**
     * Returns the value of the attribute "href". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "href" or an empty string if that attribute isn't defined
     */
    public final String getHrefAttribute() {
        return getAttribute("href");
    }

    /**
     * Returns the value of the attribute "nohref". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "nohref" or an empty string if that attribute isn't defined
     */
    public final String getNoHrefAttribute() {
        return getAttribute("nohref");
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
     * Returns the value of the attribute "tabindex". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "tabindex" or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttribute("tabindex");
    }

    /**
     * Returns the value of the attribute "accesskey". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accesskey" or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttribute("accesskey");
    }

    /**
     * Returns the value of the attribute "onfocus". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onfocus" or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttribute("onfocus");
    }

    /**
     * Returns the value of the attribute "onblur". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onblur" or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttribute("onblur");
    }

    /**
     * Returns the value of the attribute "target". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "target" or an empty string if that attribute isn't defined
     */
    public final String getTargetAttribute() {
        return getAttribute("target");
    }

    /**
     * Indicates if this area contains the specified point.
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return <code>true</code> if the point is contained in this area
     */
    boolean containsPoint(final int x, final int y) {
        final String shape = StringUtils.defaultIfEmpty(getShapeAttribute(), "rect").toLowerCase();

        if ("default".equals(shape) && getCoordsAttribute() != null) {
            return true;
        }
        else if ("rect".equals(shape) && getCoordsAttribute() != null) {
            final String[] coords = getCoordsAttribute().split(",");
            final double leftX = Double.parseDouble(coords[0].trim());
            final double topY = Double.parseDouble(coords[1].trim());
            final double rightX = Double.parseDouble(coords[2].trim());
            final double bottomY = Double.parseDouble(coords[3].trim());
            final Rectangle2D rectangle = new Rectangle2D.Double(leftX, topY,
                    rightX - leftX + 1, bottomY - topY + 1);
            if (rectangle.contains(x, y)) {
                return true;
            }
        }
        else if ("circle".equals(shape) && getCoordsAttribute() != null) {
            final String[] coords = getCoordsAttribute().split(",");
            final double centerX = Double.parseDouble(coords[0].trim());
            final double centerY = Double.parseDouble(coords[1].trim());
            final String radiusString = coords[2].trim();

            final int radius;
            try {
                radius = Integer.parseInt(radiusString);
            }
            catch (final NumberFormatException nfe) {
                throw new NumberFormatException("Circle radius of " + radiusString + " is not yet implemented.");
            }
            final Ellipse2D ellipse = new Ellipse2D.Double(centerX - radius / 2, centerY - radius / 2,
                    radius, radius);
            if (ellipse.contains(x, y)) {
                return true;
            }
        }
        else if ("poly".equals(shape) && getCoordsAttribute() != null) {
            final String[] coords = getCoordsAttribute().split(",");
            final GeneralPath path = new GeneralPath();
            for (int i = 0; i + 1 < coords.length; i += 2) {
                if (i == 0) {
                    path.moveTo(Float.parseFloat(coords[i]), Float.parseFloat(coords[i + 1]));
                }
                else {
                    path.lineTo(Float.parseFloat(coords[i]), Float.parseFloat(coords[i + 1]));
                }
            }
            path.closePath();
            if (path.contains(x, y)) {
                return true;
            }
        }

        return false;
    }
}
