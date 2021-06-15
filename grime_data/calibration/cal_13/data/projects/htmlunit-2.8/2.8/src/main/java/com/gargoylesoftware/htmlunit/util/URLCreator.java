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
package com.gargoylesoftware.htmlunit.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;

/**
 * Responsible for URL creation.
 * @author Marc Guillemot
 * @version $Revision: 5842 $
 */
abstract class URLCreator {
    abstract URL toUrlUnsafeClassic(final String url) throws MalformedURLException;

    /**
     * Gets the instance responsible for URL creating, detecting if we are running on GoogleAppEngine
     * where custom URLStreamHandler is prohibited.
     */
    static URLCreator getCreator() {
        if (!GAEUtils.isGaeMode()) {
            return new URLCreatorStandard();
        }
        return new URLCreatorGAE();
    }

    /**
     * For the normal case.
     */
    static class URLCreatorStandard extends URLCreator {
        private static final URLStreamHandler JS_HANDLER
            = new com.gargoylesoftware.htmlunit.protocol.javascript.Handler();
        private static final URLStreamHandler ABOUT_HANDLER
            = new com.gargoylesoftware.htmlunit.protocol.about.Handler();
        private static final URLStreamHandler DATA_HANDLER = new com.gargoylesoftware.htmlunit.protocol.data.Handler();

        @Override
        URL toUrlUnsafeClassic(final String url) throws MalformedURLException {
            if (TextUtil.startsWithIgnoreCase(url, "javascript:")) {
                return new URL(null, url, JS_HANDLER);
            }
            else if (TextUtil.startsWithIgnoreCase(url, "about:")) {
                if (WebClient.URL_ABOUT_BLANK != null
                        && org.apache.commons.lang.StringUtils.equalsIgnoreCase(
                                WebClient.URL_ABOUT_BLANK.toExternalForm(), url)) {
                    return WebClient.URL_ABOUT_BLANK;
                }
                return new URL(null, url, ABOUT_HANDLER);
            }
            else if (TextUtil.startsWithIgnoreCase(url, "data:")) {
                return new URL(null, url, DATA_HANDLER);
            }
            else {
                return new URL(url);
            }
        }
    }

    /**
     * For working on GoogleAppEngine. The URL hack will require special handling from a dedicated WebConnection.
     */
    static class URLCreatorGAE extends URLCreator {

        @Override
        URL toUrlUnsafeClassic(final String url) throws MalformedURLException {
            if (TextUtil.startsWithIgnoreCase(url, "javascript:")) {
                return new URL("http://gaeHack_" + url.replaceFirst(":", "/"));
            }
            else if (TextUtil.startsWithIgnoreCase(url, "about:")) {
                if (WebClient.URL_ABOUT_BLANK != null
                        && org.apache.commons.lang.StringUtils.equalsIgnoreCase(
                                WebClient.URL_ABOUT_BLANK.toExternalForm(), url)) {
                    return WebClient.URL_ABOUT_BLANK;
                }
                return new URL("http://gaeHack_" + url.replaceFirst(":", "/"));
            }
            else if (TextUtil.startsWithIgnoreCase(url, "data:")) {
                return new URL("http://gaeHack_" + url.replaceFirst(":", "/"));
            }
            else {
                return new URL(url);
            }
        }
    }
}

