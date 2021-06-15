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

import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A script preprocessor removing weird syntax supported by IE like semicolons before <code>catch</code> or
 * before <code>finally</code> in a try-catch-finally block.
 *
 * @version $Revision: 5468 $
 * @author Marc Guillemot
 */
public class IEWeirdSyntaxScriptPreProcessor implements ScriptPreProcessor {

    private static final IEWeirdSyntaxScriptPreProcessor instance_ = new IEWeirdSyntaxScriptPreProcessor();
    private static final Pattern patternFinally_
        = Pattern.compile("(\\}(?:\\s*(?://.*\\n)?)*);((?:\\s*(?://.*\\n)?)*finally)");
    private static final Pattern patternCatch_
        = Pattern.compile("(\\}(?:\\s*(?://.*\\n)?)*);((?:\\s*(?://.*\\n)?)*catch)");

    /**
     * Gets an instance of the pre processor.
     * @return an instance
     */
    public static IEWeirdSyntaxScriptPreProcessor getInstance() {
        return instance_;
    }

    /**
     * {@inheritDoc}
     */
    public String preProcess(final HtmlPage htmlPage, String sourceCode,
            final String sourceName, final int lineNumber, final HtmlElement htmlElement) {

        if (sourceCode.contains("catch")) {
            sourceCode = patternCatch_.matcher(sourceCode).replaceAll("$1 $2");
        }
        if (sourceCode.contains("finally")) {
            sourceCode = patternFinally_.matcher(sourceCode).replaceAll("$1 $2");
        }
        return sourceCode;
    }
}
