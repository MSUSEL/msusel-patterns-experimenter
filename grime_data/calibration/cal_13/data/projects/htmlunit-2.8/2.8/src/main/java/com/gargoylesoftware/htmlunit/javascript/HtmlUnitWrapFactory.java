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

import java.io.Serializable;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.WrapFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * Called by Rhino to Wrap Object as {@link Scriptable}.
 *
 * @version $Revision: 5864 $
 * @author Marc Guillemot
 */
public class HtmlUnitWrapFactory extends WrapFactory implements Serializable {
    private static final long serialVersionUID = 1779982280511278636L;

    /**
     * Constructor.
     */
    public HtmlUnitWrapFactory() {
        setJavaPrimitiveWrap(false); // We don't want to wrap String & Co.
    }

    /**
     * Wraps some objects used by HtmlUnit (like {@link NodeList}), or delegates directly to the parent class.
     * {@inheritDoc}
     * @see WrapFactory#wrapAsJavaObject(Context, Scriptable, Object, Class)
     */
    @Override
    public Scriptable wrapAsJavaObject(final Context context,
            final Scriptable scope, final Object javaObject, final Class< ? > staticType) {

        // TODO: should depend on the js configuration file
        final Scriptable resp;
        if (NodeList.class.equals(staticType)
                || NamedNodeMap.class.equals(staticType)) {
            resp = new ScriptableWrapper(scope, javaObject, staticType);
        }
        else {
            resp = super.wrapAsJavaObject(context, scope, javaObject,
                    staticType);
        }
        return resp;
    }
}
