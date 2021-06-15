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

public class FilteredRenderListener implements RenderListener {

    /** The delegate that will receive the text render operation if the filters all pass */
    private final RenderListener delegate;
    /** The filters to be applied */
    private final RenderFilter[] filters;

    /**
     * Construction
     * @param delegate the delegate {@link RenderListener} that will receive filtered text operations
     * @param filters the filter(s) to apply
     */
    public FilteredRenderListener(RenderListener delegate, RenderFilter... filters) {
        this.delegate = delegate;
        this.filters = filters;
    }

    /**
     * Applies filters, then delegates to the delegate if all filters pass
     * @param renderInfo contains info to render text
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(com.itextpdf.text.pdf.parser.TextRenderInfo)
     */
    public void renderText(TextRenderInfo renderInfo) {
        for (RenderFilter filter : filters) {
            if (!filter.allowText(renderInfo))
                return;
        }
        delegate.renderText(renderInfo);
    }

    /**
     * This class delegates this call
     * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
     */
    public void beginTextBlock() {
        delegate.beginTextBlock();
    }

    /**
     * This class delegates this call
     * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
     */
    public void endTextBlock() {
        delegate.endTextBlock();
    }

    /**
     * Applies filters, then delegates to the delegate if all filters pass
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo)
     * @since 5.0.1
     */
    public void renderImage(ImageRenderInfo renderInfo) {
        for (RenderFilter filter : filters) {
            if (!filter.allowImage(renderInfo))
                return;
        }
        delegate.renderImage(renderInfo);
    }

}