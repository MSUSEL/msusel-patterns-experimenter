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

import java.lang.reflect.Method;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * An implementation of native ActiveX components using <a href="http://jacob-project.wiki.sourceforge.net/">Jacob</a>.
 *
 * @version $Revision: 5721 $
 * @author Ahmed Ashour
 */
public class ActiveXObjectImpl extends SimpleScriptable {

    private static final long serialVersionUID = 6954481782205807262L;

    private static final Class< ? > activeXComponentClass_;

    /** ActiveXComponent.getProperty(String) */
    private static final Method METHOD_getProperty_;
    private final Object object_;

    /** Dispatch.callN(Dispatch, String, Object[]) */
    private static final Method METHOD_callN_;

    /** Variant.getvt() */
    private static final Method METHOD_getvt_;

    /** Variant.getDispatch() */
    private static final Method METHOD_getDispatch_;

    static {
        try {
            activeXComponentClass_ = Class.forName("com.jacob.activeX.ActiveXComponent");
            METHOD_getProperty_ = activeXComponentClass_.getMethod("getProperty", String.class);
            final Class< ? > dispatchClass = Class.forName("com.jacob.com.Dispatch");
            METHOD_callN_ = dispatchClass.getMethod("callN", dispatchClass, String.class, Object[].class);
            final Class< ? > variantClass = Class.forName("com.jacob.com.Variant");
            METHOD_getvt_ = variantClass.getMethod("getvt");
            METHOD_getDispatch_ = variantClass.getMethod("getDispatch");
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs a new instance.
     *
     * @param activeXName ActiveX object name
     * @throws Exception if failed to initiate Jacob
     */
    public ActiveXObjectImpl(final String activeXName) throws Exception {
        this(activeXComponentClass_.getConstructor(String.class).newInstance(activeXName));
    }

    private ActiveXObjectImpl(final Object object) {
        object_ = object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        try {
            final Object variant = METHOD_getProperty_.invoke(object_, name);
            return wrapIfNecessary(variant);
        }
        catch (final Exception e) {
            return new Function() {
                public Object call(final Context arg0, final Scriptable arg1, final Scriptable arg2,
                    final Object[] arg3) {
                    try {
                        final Object rv = METHOD_callN_.invoke(null, object_, name, arg3);
                        return wrapIfNecessary(rv);
                    }
                    catch (final Exception e) {
                        throw Context.throwAsScriptRuntimeEx(e);
                    }
                }

                public Scriptable construct(final Context arg0, final Scriptable arg1, final Object[] arg2) {
                    throw new UnsupportedOperationException();
                }

                public void delete(final String arg0) {
                    throw new UnsupportedOperationException();
                }

                public void delete(final int arg0) {
                    throw new UnsupportedOperationException();
                }

                public Object get(final String arg0, final Scriptable arg1) {
                    throw new UnsupportedOperationException();
                }

                public Object get(final int arg0, final Scriptable arg1) {
                    throw new UnsupportedOperationException();
                }

                public String getClassName() {
                    throw new UnsupportedOperationException();
                }

                public Object getDefaultValue(final Class< ? > arg0) {
                    throw new UnsupportedOperationException();
                }

                public Object[] getIds() {
                    throw new UnsupportedOperationException();
                }

                public Scriptable getParentScope() {
                    throw new UnsupportedOperationException();
                }

                public Scriptable getPrototype() {
                    throw new UnsupportedOperationException();
                }

                public boolean has(final String arg0, final Scriptable arg1) {
                    throw new UnsupportedOperationException();
                }

                public boolean has(final int arg0, final Scriptable arg1) {
                    throw new UnsupportedOperationException();
                }

                public boolean hasInstance(final Scriptable arg0) {
                    throw new UnsupportedOperationException();
                }

                public void put(final String arg0, final Scriptable arg1, final Object arg2) {
                    throw new UnsupportedOperationException();
                }

                public void put(final int arg0, final Scriptable arg1, final Object arg2) {
                    throw new UnsupportedOperationException();
                }

                public void setParentScope(final Scriptable arg0) {
                    throw new UnsupportedOperationException();
                }

                public void setPrototype(final Scriptable arg0) {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    /**
     * Wrap the specified variable into {@link ActiveXObjectImpl} if its type is Variant.VariantDispatch.
     * @param variant the variant to potentially wrap
     * @return either the variant if it is basic type or wrapped {@link ActiveXObjectImpl}
     */
    private Object wrapIfNecessary(final Object variant) throws Exception {
        if (((Short) METHOD_getvt_.invoke(variant)) == 9) { //Variant.VariantDispatch
            return new ActiveXObjectImpl(METHOD_getDispatch_.invoke(variant));
        }
        return variant;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        try {
            final Method setMethod = activeXComponentClass_.getMethod("setProperty", String.class, value.getClass());
            setMethod.invoke(object_, name, Context.toString(value));
        }
        catch (final Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }
}
