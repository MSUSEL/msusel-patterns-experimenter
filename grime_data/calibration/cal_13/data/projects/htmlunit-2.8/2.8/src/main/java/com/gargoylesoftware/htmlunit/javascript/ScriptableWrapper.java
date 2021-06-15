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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * Simple wrapper to make "normal" object scriptable according to specific configuration
 * and allowing use of index properties.
 * TODO: Configuration of the
 * properties and functions should occur from the XML configuration according to
 * the browser to simulate.
 *
 * @version $Revision: 5864 $
 * @author Marc Guillemot
 */
public class ScriptableWrapper extends ScriptableObject {
    private static final long serialVersionUID = 1736378450382368760L;

    private final Map<String, Method> properties_ = new HashMap<String, Method>();

    private Method getByIndexMethod_;
    private final Object javaObject_;
    private final String jsClassName_;
    private Method getByNameFallback_;

    /**
     * Constructs a wrapper for the java object.
     * @param scope the scope of the executing script
     * @param javaObject the javaObject to wrap
     * @param staticType the static type of the object
     */
    public ScriptableWrapper(final Scriptable scope, final Object javaObject,
            final Class< ? > staticType) {
        javaObject_ = javaObject;
        setParentScope(scope);

        // all these information should come from the XML js configuration file
        // just for a first time...
        if (NodeList.class.equals(staticType)
                || NamedNodeMap.class.equals(staticType)) {
            try {
                jsClassName_ = ClassUtils.getShortClassName(staticType);

                // is there a better way that would avoid to keep local
                // information?
                // it seems that defineProperty with only accepts delegate if
                // its method takes a ScriptableObject
                // as parameter.
                final Method length = javaObject.getClass().getMethod(
                        "getLength", ArrayUtils.EMPTY_CLASS_ARRAY);
                properties_.put("length", length);

                final Method item = javaObject.getClass().getMethod("item",
                        new Class[] {Integer.TYPE});
                defineProperty("item", new MethodWrapper("item", staticType, new Class[] {Integer.TYPE}),
                        0);

                final Method toString = getClass().getMethod("jsToString",
                        ArrayUtils.EMPTY_CLASS_ARRAY);
                defineProperty("toString", new FunctionObject("toString",
                        toString, this), 0);

                getByIndexMethod_ = item;

                if (NamedNodeMap.class.equals(staticType)) {
                    final Method getNamedItem = javaObject.getClass()
                            .getMethod("getNamedItem",
                                    new Class[] {String.class});
                    defineProperty("getNamedItem",
                            new MethodWrapper("getNamedItem", staticType, new Class[] {String.class}), 0);

                    getByNameFallback_ = getNamedItem;
                }
            }
            catch (final Exception e) {
                throw new RuntimeException("Method not found", e);
            }
        }
        else {
            throw new RuntimeException("Unknown type: " + staticType.getName());
        }
    }

    /**
     * {@inheritDoc}
     * @see ScriptableObject#get(java.lang.String,net.sourceforge.htmlunit.corejs.javascript.Scriptable)
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        final Method propertyGetter = properties_.get(name);
        final Object response;
        if (propertyGetter != null) {
            response = invoke(propertyGetter);
        }
        else {
            final Object fromSuper = super.get(name, start);
            if (fromSuper != Scriptable.NOT_FOUND) {
                response = fromSuper;
            }
            else {
                final Object byName = invoke(getByNameFallback_,
                        new Object[] {name});
                if (byName != null) {
                    response = byName;
                }
                else {
                    response = Scriptable.NOT_FOUND;
                }
            }
        }

        return Context.javaToJS(response, ScriptableObject
                .getTopLevelScope(start));
    }

    /**
     * {@inheritDoc}
     * @see ScriptableObject#has(java.lang.String, net.sourceforge.htmlunit.corejs.javascript.Scriptable)
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        return properties_.containsKey(name) || super.has(name, start);
    }

    /**
     * Invokes the method on the wrapped object.
     * @param method the method to invoke
     * @return the invocation result
     */
    protected Object invoke(final Method method) {
        return invoke(method, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Invokes the method on the wrapped object.
     * @param method the method to invoke
     * @param args the argument to pass to the method
     * @return the invocation result
     */
    protected Object invoke(final Method method, final Object[] args) {
        try {
            return method.invoke(javaObject_, args);
        }
        catch (final Exception e) {
            throw new RuntimeException(
                    "Invocation of method on java object failed", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see ScriptableObject#get(int, net.sourceforge.htmlunit.corejs.javascript.Scriptable)
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (getByIndexMethod_ != null) {
            final Object byIndex = invoke(getByIndexMethod_,
                    new Object[] {new Integer(index)});
            return Context.javaToJS(byIndex, ScriptableObject
                    .getTopLevelScope(start));
        }
        return super.get(index, start);
    }

    /**
     * {@inheritDoc}
     * @see ScriptableObject#getDefaultValue(java.lang.Class)
     */
    @Override
    public Object getDefaultValue(final Class< ? > hint) {
        if (String.class.equals(hint) || hint == null) {
            return jsToString();
        }
        return super.getDefaultValue(hint);
    }

    /**
     * To use as "toString" function in JavaScript.
     * @return the string representation
     */
    public String jsToString() {
        return "[object " + getClassName() + "]";
    }

    /**
     * {@inheritDoc}
     * @see ScriptableObject#getClassName()
     */
    @Override
    public String getClassName() {
        return jsClassName_;
    }

    /**
     * Gets the java object made availabe to JavaScript through this wrapper.
     * @return the wrapped object
     */
    public Object getWrappedObject() {
        return javaObject_;
    }
}
