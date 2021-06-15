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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for XSLTemplate.
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms767644.aspx">MSDN documentation</a>
 *
 * @version $Revision: 5893 $
 * @author Ahmed Ashour
 */
public class XSLTemplate extends SimpleScriptable {

    private static final long serialVersionUID = 2820794481694666278L;

    private Node stylesheet_;

    /**
     * Sets the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template.
     * @param node the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template
     */
    public void jsxSet_stylesheet(final Node node) {
        stylesheet_ = node;
    }

    /**
     * Returns the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template.
     * @return the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template
     */
    public Node jsxGet_stylesheet() {
        return stylesheet_;
    }

    /**
     * Creates a rental-model XSLProcessor object that will use this template.
     * @return the XSLTProcessor
     */
    public XSLTProcessor jsxFunction_createProcessor() {
        final XSLTProcessor processor = new XSLTProcessor();
        processor.setPrototype(getPrototype(processor.getClass()));
        processor.setParentScope(getParentScope());
        processor.jsxFunction_importStylesheet(stylesheet_);
        return processor;
    }
}
