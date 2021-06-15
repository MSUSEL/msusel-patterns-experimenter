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

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebugFrame;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;

/**
 * <p>
 * HtmlUnit's implementation of the {@link net.sourceforge.htmlunit.corejs.javascript.debug.Debugger} interface,
 * which registers {@link DebugFrameImpl} instances with Rhino for each new execution frame created.
 * See <a href="http://www.mozilla.org/rhino/rhino15R4-debugger.html">the Rhino documentation</a> or
 * <a href="http://lxr.mozilla.org/mozilla/source/js/rhino/src/org/mozilla/javascript/debug/Debugger.java">the
 * interface source code</a> for more info on the {@link net.sourceforge.htmlunit.corejs.javascript.debug.Debugger}
 * interface and its uses.
 * </p>
 *
 * <p>
 * Please note that this class is intended mainly to aid in the debugging and development of
 * HtmlUnit itself, rather than the debugging and development of web applications.
 * </p>
 *
 * <p>
 * In order to enable the debugging output, call
 * {@link HtmlUnitContextFactory#setDebugger(net.sourceforge.htmlunit.corejs.javascript.debug.Debugger)}, passing in
 * an instance of this class, and make sure your loggers are configured to output <tt>TRACE</tt> level log messages.
 * </p>
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 * @see DebugFrameImpl
 * @see HtmlUnitContextFactory#setDebugger(net.sourceforge.htmlunit.corejs.javascript.debug.Debugger)
 */
public class DebuggerImpl extends DebuggerAdapter {

    /**
     * {@inheritDoc}
     */
    @Override
    public DebugFrame getFrame(final Context cx, final DebuggableScript functionOrScript) {
        return new DebugFrameImpl(functionOrScript);
    }

}
