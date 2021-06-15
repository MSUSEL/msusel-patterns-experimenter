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
package com.itextpdf.text.pdf.parser;

/**
 * A text render listener that filters text operations before passing them on to a delegate
 * @since 5.0.1
 */

public class FilteredTextRenderListener extends FilteredRenderListener implements TextExtractionStrategy {

    /** The delegate that will receive the text render operation if the filters all pass */
    private final TextExtractionStrategy delegate;

    /**
     * Construction
     * @param delegate the delegate {@link RenderListener} that will receive filtered text operations
     * @param filters the filter(s) to apply
     */
    public FilteredTextRenderListener(TextExtractionStrategy delegate, RenderFilter... filters) {
        super(delegate, filters);
        this.delegate = delegate;
    }

    /**
     * This class delegates this call
     * @see com.itextpdf.text.pdf.parser.TextExtractionStrategy#getResultantText()
     */
    public String getResultantText() {
        return delegate.getResultantText();
    }

}