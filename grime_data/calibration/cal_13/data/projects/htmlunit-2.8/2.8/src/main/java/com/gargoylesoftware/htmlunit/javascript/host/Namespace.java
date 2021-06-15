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

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a Namespace.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535854.aspx">MSDN documentation</a>
 */
public class Namespace extends SimpleScriptable {

    private static final long serialVersionUID = -5554898606769625960L;
    private String name_;
    private String urn_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     * Don't call.
     */
    @Deprecated
    public Namespace() {
        // Empty.
    }

    /**
     * Creates an instance.
     *
     * @param parentScope parent scope
     * @param name the name
     * @param urn the Uniform Resource Name
     */
    public Namespace(final ScriptableObject parentScope, final String name, final String urn) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
        name_ = name;
        urn_ = urn;
    }

    /**
     * Retrieves the name of the namespace.
     * @return the name
     */
    public String jsxGet_name() {
        return name_;
    }

    /**
     * Gets a Uniform Resource Name (URN) for a target document.
     * @return the URN
     */
    public String jsxGet_urn() {
        return urn_;
    }

    /**
     * Gets a Uniform Resource Name (URN) for a target document.
     * @param urn the Uniform Resource Name
     */
    public void jsxSet_urn(final String urn) {
        urn_ = urn;
    }
}
