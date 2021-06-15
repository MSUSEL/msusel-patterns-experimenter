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

import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Proxy for a {@link SimpleScriptable}.
 *
 * @param <T> the type of scriptable object being wrapped
 * @version $Revision: 5564 $
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
public abstract class SimpleScriptableProxy<T extends SimpleScriptable> extends Delegator implements
    ScriptableWithFallbackGetter, Serializable {
    private static final long serialVersionUID = -3836061858668746684L;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract T getDelegee();

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, Scriptable start) {
        if (start instanceof SimpleScriptableProxy<?>) {
            start = ((SimpleScriptableProxy<?>) start).getDelegee();
        }
        return getDelegee().get(index, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, Scriptable start) {
        if (start instanceof SimpleScriptableProxy<?>) {
            start = ((SimpleScriptableProxy<?>) start).getDelegee();
        }
        return getDelegee().get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        final SimpleScriptable delegee = getDelegee();
        if (delegee instanceof ScriptableWithFallbackGetter) {
            return ((ScriptableWithFallbackGetter) delegee).getWithFallback(name);
        }
        return NOT_FOUND;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final int index, Scriptable start) {
        if (start instanceof SimpleScriptableProxy<?>) {
            start = ((SimpleScriptableProxy<?>) start).getDelegee();
        }
        return getDelegee().has(index, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, Scriptable start) {
        if (start instanceof SimpleScriptableProxy<?>) {
            start = ((SimpleScriptableProxy<?>) start).getDelegee();
        }
        return getDelegee().has(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasInstance(Scriptable instance) {
        if (instance instanceof SimpleScriptableProxy<?>) {
            instance = ((SimpleScriptableProxy<?>) instance).getDelegee();
        }
        return getDelegee().hasInstance(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final int index, Scriptable start, final Object value) {
        if (start instanceof SimpleScriptableProxy<?>) {
            start = ((SimpleScriptableProxy<?>) start).getDelegee();
        }
        getDelegee().put(index, start, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, Scriptable start, final Object value) {
        if (start instanceof SimpleScriptableProxy<?>) {
            start = ((SimpleScriptableProxy<?>) start).getDelegee();
        }
        getDelegee().put(name, start, value);
    }

}
