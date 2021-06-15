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
package com.gargoylesoftware.htmlunit.javascript.host;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Contains some missing features of Rhino NativeString.
 *
 * @version $Revision: 5699 $
 * @author Ahmed Ashour
 */
public final class StringCustom {

    private StringCustom() { }

    /**
     * Removes whitespace from the left end of the string.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the left trimmed string
     */
    public static String trimLeft(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final String string = Context.toString(thisObj);
        int start = 0;
        final int length = string.length();
        while (start < length && ScriptRuntime.isJSWhitespaceOrLineTerminator(string.charAt(start))) {
            start++;
        }
        if (start == 0) {
            return string;
        }
        return string.substring(start, length);
    }

    /**
     * Removes whitespace from the right end of the string.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the right trimmed string
     */
    public static String trimRight(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final String string = Context.toString(thisObj);
        final int length = string.length();
        int end = length;
        while (end > 0 && ScriptRuntime.isJSWhitespaceOrLineTerminator(string.charAt(end - 1))) {
            end--;
        }
        if (end == length) {
            return string;
        }
        return string.substring(0, end);
    }
}
