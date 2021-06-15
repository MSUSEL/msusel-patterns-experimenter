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

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.lang.ArrayUtils;

/**
 * Wraps a Java method to make it available as a JavaScript function
 * (more flexible than Rhino's {@link FunctionObject}.
 *
 * @version $Revision: 5864 $
 * @author Marc Guillemot
 */
public class MethodWrapper extends ScriptableObject implements Function {

    private static final long serialVersionUID = 6106771000496895783L;
    private final Class< ? > clazz_;
    private final Method method_;
    private final int[] jsTypeTags_;

    /**
     * Facility constructor to wrap a method without arguments.
     * @param methodName the name of the method to wrap
     * @param clazz the class declaring the method
     * @throws NoSuchMethodException if the method is no found
     */
    MethodWrapper(final String methodName, final Class< ? > clazz) throws NoSuchMethodException {
        this(methodName, clazz, ArrayUtils.EMPTY_CLASS_ARRAY);
    }

    /**
     * Wraps a method as a JavaScript function.
     * @param methodName the name of the method to wrap
     * @param clazz the class declaring the method
     * @param parameterTypes the types of the method's parameter
     * @throws NoSuchMethodException if the method is no found
     */
    MethodWrapper(final String methodName, final Class< ? > clazz, final Class< ? >[] parameterTypes)
        throws NoSuchMethodException {

        clazz_ = clazz;
        method_ = clazz.getMethod(methodName, parameterTypes);
        jsTypeTags_ = new int[parameterTypes.length];
        int i = 0;
        for (final Class< ? > klass : parameterTypes) {
            jsTypeTags_[i++] = FunctionObject.getTypeTag(klass);
        }
    }

    /**
     * @see net.sourceforge.htmlunit.corejs.javascript.ScriptableObject#getClassName()
     * @return a name based on the method name
     */
    @Override
    public String getClassName() {
        return "function " + method_.getName();
    }

    /**
     * {@inheritDoc}
     */
    public Object call(final Context context, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        final Object javaResp;
        if (thisObj instanceof ScriptableWrapper) {
            final ScriptableWrapper wrapper = (ScriptableWrapper) thisObj;
            final Object wrappedObject = wrapper.getWrappedObject();
            if (clazz_.isInstance(wrappedObject)) {
                // convert arguments
                final Object[] javaArgs = convertJSArgsToJavaArgs(context, scope, args);
                try {
                    javaResp = method_.invoke(wrappedObject, javaArgs);
                }
                catch (final Exception e) {
                    throw Context.reportRuntimeError("Exception calling wrapped function "
                            + method_.getName() + ": " + e.getMessage());
                }
            }
            else {
                throw buildInvalidCallException(thisObj);
            }

        }
        else {
            throw buildInvalidCallException(thisObj);
        }

        final Object jsResp = Context.javaToJS(javaResp, ScriptableObject.getTopLevelScope(scope));
        return jsResp;
    }

    private RuntimeException buildInvalidCallException(final Scriptable thisObj) {
        return Context.reportRuntimeError("Function " + method_.getName()
                + " called on incompatible object: " + thisObj);
    }

    /**
     * Converts JavaScript arguments to Java arguments.
     * @param context the current context
     * @param scope the current scope
     * @param jsArgs the JavaScript arguments
     * @return the java arguments
     */
    Object[] convertJSArgsToJavaArgs(final Context context, final Scriptable scope, final Object[] jsArgs) {
        if (jsArgs.length != jsTypeTags_.length) {
            throw Context.reportRuntimeError("Bad number of parameters for function " + method_.getName()
                    + ": expected " + jsTypeTags_.length + " got " + jsArgs.length);
        }
        final Object[] javaArgs = new Object[jsArgs.length];
        int i = 0;
        for (final Object object : jsArgs) {
            javaArgs[i] = FunctionObject.convertArg(context, scope, object, jsTypeTags_[i++]);
        }
        return javaArgs;
    }

    /**
     * {@inheritDoc}
     */
    public Scriptable construct(final Context context, final Scriptable scope, final Object[] args) {
        throw Context.reportRuntimeError("Function " + method_.getName() + " can't be used as a constructor");
    }

}
