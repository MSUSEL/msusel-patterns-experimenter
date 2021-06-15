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
package com.jaspersoft.ireport.designer.charts.datasets;

import com.jaspersoft.ireport.designer.editor.ExpressionContext;

/**
 *
 * @author gtoffoli
 */
public interface ChartDatasetPanel {
    public void setExpressionContext(ExpressionContext ec);

    /**
     * This method is used to higlight and focus a component that is generating an error.
     * Since the expression to show can be nested in some remote window, the array hold
     * the complete "path" required to open the right expression.
     *
     * The simplest case is a single Integer value (like for the PieChart)
     * A complicated case can be an error in an hyperlink parameter in some category series
     * that would make the array to be something like:
     * [0] CATEGORY_LIST
     * [1] index of the category
     * [2] (used for the category window) COMPONENT_HYPERLINK
     * [3] (for the hyper link component) COMPONENT_PARAMETERS
     * [4] Index of the parameter
     * [5] Expression to hilight in the parameter link window
     *
     */
    public void setFocusedExpression(Object[] expressionInfo);
    
    /**
     * this method is called when the container window is opened...
     */
    public void containerWindowOpened();
    
}
