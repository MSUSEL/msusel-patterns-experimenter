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
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebugFrame;

/**
 * An adapter class for debug frame implementations. The methods in this class are empty. This class
 * exists as a convenience for creating debug frame objects.
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 */
public class DebugFrameAdapter implements DebugFrame {

    /** {@inheritDoc} */
    public void onDebuggerStatement(final Context cx) {
        // Empty.
    }

    /** {@inheritDoc} */
    public void onEnter(final Context cx, final Scriptable activation, final Scriptable thisObj, final Object[] args) {
        // Empty.
    }

    /** {@inheritDoc} */
    public void onExceptionThrown(final Context cx, final Throwable ex) {
        // Empty.
    }

    /** {@inheritDoc} */
    public void onExit(final Context cx, final boolean byThrow, final Object resultOrException) {
        // Empty.
    }

    /** {@inheritDoc} */
    public void onLineChange(final Context cx, final int lineNumber) {
        // Empty.
    }

}
