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
package com.gargoylesoftware.htmlunit;

import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * This object contains the result of executing a chunk of script code.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 */
public final class ScriptResult {

    /** The object that was returned from the script engine. */
    private final Object javaScriptResult_;

    /** The page that is currently loaded at the end of the script execution. */
    private final Page newPage_;

    /**
     * Creates a new instance.
     *
     * @param javaScriptResult the object that was returned from the script engine
     * @param newPage the page that is currently loaded at the end of the script execution
     */
    public ScriptResult(final Object javaScriptResult, final Page newPage) {
        javaScriptResult_ = javaScriptResult;
        newPage_ = newPage;
    }

    /**
     * Returns the object that was the output of the script engine.
     * @return the result from the script engine
     */
    public Object getJavaScriptResult() {
        return javaScriptResult_;
    }

    /**
     * Returns the page that is loaded at the end of the script execution.
     * @return the new page
     */
    public Page getNewPage() {
        return newPage_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ScriptResult[result=" + javaScriptResult_ + " page=" + newPage_ + "]";
    }

    /**
     * Utility method testing if a script result is <tt>false</tt>.
     * @param scriptResult a script result (may be <tt>null</tt>)
     * @return <tt>true</tt> if <tt>scriptResult</tt> is <tt>false</tt>
     */
    public static boolean isFalse(final ScriptResult scriptResult) {
        return scriptResult != null && Boolean.FALSE.equals(scriptResult.getJavaScriptResult());
    }

    /**
     * Utility method testing if a script result is undefined (there was no return value).
     * @param scriptResult a script result (may be <tt>null</tt>)
     * @return <tt>true</tt> if <tt>scriptResult</tt> is undefined (there was no return value)
     */
    public static boolean isUndefined(final ScriptResult scriptResult) {
        return scriptResult != null && scriptResult.getJavaScriptResult() instanceof Undefined;
    }

    /**
     * Creates and returns a composite {@link ScriptResult} based on the two input {@link ScriptResult}s. This
     * method defines how the return values for multiple event handlers are combined during event capturing and
     * bubbling. The behavior of this method varies based on whether or not we are emulating IE.
     *
     * @param newResult the new {@link ScriptResult} (may be <tt>null</tt>)
     * @param originalResult the original {@link ScriptResult} (may be <tt>null</tt>)
     * @param ie whether or not we are emulating IE
     * @return a composite {@link ScriptResult}, based on the two input {@link ScriptResult}s
     */
    public static ScriptResult combine(final ScriptResult newResult, final ScriptResult originalResult,
        final boolean ie) {

        final Object jsResult;
        final Page page;

        // If we're emulating IE, the overall JavaScript return value is the last return value.
        // If we're emulating FF, the overall JavaScript return value is false if the return value
        // was false at any level.
        if (ie) {
            if (newResult != null && !ScriptResult.isUndefined(newResult)) {
                jsResult = newResult.getJavaScriptResult();
            }
            else if (originalResult != null) {
                jsResult = originalResult.getJavaScriptResult();
            }
            else {
                jsResult = null;
            }
        }
        else {
            if (ScriptResult.isFalse(newResult)) {
                jsResult = newResult.getJavaScriptResult();
            }
            else if (originalResult != null) {
                jsResult = originalResult.getJavaScriptResult();
            }
            else {
                jsResult = null;
            }
        }

        // The new page is always the newest page.
        if (newResult != null) {
            page = newResult.getNewPage();
        }
        else if (originalResult != null) {
            page = originalResult.getNewPage();
        }
        else {
            page = null;
        }

        // Build and return the composite script result.
        if (jsResult == null && page == null) {
            return null;
        }
        return new ScriptResult(jsResult, page);
    }

}
