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

import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.ScopeProvider;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.SourceProvider;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;

/**
 * Utility class containing miscellaneous {@link WebClient}-related methods.
 *
 * @version $Revision: 5658 $
 * @author Daniel Gredler
 */
public final class WebClientUtils {

    /**
     * Disallow instantiation of this class.
     */
    private WebClientUtils() {
        // Empty.
    }

    /**
     * Attaches a visual (GUI) debugger to the specified client.
     * @param client the client to which the visual debugger is to be attached
     * @see <a href="http://www.mozilla.org/rhino/debugger.html">Mozilla Rhino Debugger Documentation</a>
     */
    public static void attachVisualDebugger(final WebClient client) {
        final ScopeProvider sp = null;
        final HtmlUnitContextFactory cf = client.getJavaScriptEngine().getContextFactory();
        final Main main = Main.mainEmbedded(cf, sp, "HtmlUnit JavaScript Debugger");

        final SourceProvider sourceProvider = new SourceProvider() {
            public String getSource(final DebuggableScript script) {
                String sourceName = script.getSourceName();
                if (sourceName.endsWith("(eval)")) {
                    return null; // script is result of eval call. Rhino already knows the source and we don't
                }
                if (sourceName.startsWith("script in ")) {
                    sourceName = StringUtils.substringBetween(sourceName, "script in ", " from");
                    for (final WebWindow ww : client.getWebWindows()) {
                        final WebResponse wr = ww.getEnclosedPage().getWebResponse();
                        if (sourceName.equals(wr.getWebRequest().getUrl().toString())) {
                            return wr.getContentAsString();
                        }
                    }
                }
                return null;
            }
        };
        main.setSourceProvider(sourceProvider);
    }

}
