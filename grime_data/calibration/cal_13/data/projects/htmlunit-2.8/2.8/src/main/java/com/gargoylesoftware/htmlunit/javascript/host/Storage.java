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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * The JavaScript object that represents a Storage.
 *
 * @version $Revision: 5768 $
 * @author Ahmed Ashour
 */
public class Storage extends SimpleScriptable {

    private static final long serialVersionUID = 7181399874147128543L;

    static enum Type { GLOBAL_STORAGE, LOCAL_STORAGE, SESSION_STORAGE };

    private static List<String> RESERVED_NAMES_ = Arrays.asList("clear", "key", "getItem", "length", "removeItem",
        "setItem");

    private Type type_;

    void setType(final Type type) {
        type_ = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (type_ == null || RESERVED_NAMES_.contains(name)) {
            super.put(name, start, value);
            return;
        }
        jsxFunction_setItem(name, Context.toString(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (type_ == null || RESERVED_NAMES_.contains(name)) {
            return super.get(name, start);
        }
        return jsxFunction_getItem(name);
    }

    /**
     * Returns the length property.
     * @return the length property
     */
    public int jsxGet_length() {
        return getMap().size();
    }

    /**
     * Removes the specified key.
     * @param key the item key
     */
    public void jsxFunction_removeItem(final String key) {
        getMap().remove(key);
    }

    /**
     * Returns the key of the specified index.
     * @param index the index
     * @return the key
     */
    public String jsxFunction_key(final int index) {
        int counter = 0;
        for (final String key : getMap().keySet()) {
            if (counter++ == index) {
                return key;
            }
        }
        return null;
    }

    private Map<String, String> getMap() {
        return StorageImpl.getInstance().getMap(type_, (HtmlPage) getWindow().getWebWindow().getEnclosedPage());
    }

    /**
     * Returns the value of the specified key.
     * @param key the item key
     * @return the value
     */
    public Object jsxFunction_getItem(final String key) {
        return getMap().get(key);
    }

    /**
     * Sets the item value.
     * @param key the item key
     * @param data the value
     */
    public void jsxFunction_setItem(final String key, final String data) {
        getMap().put(key, data);
    }

    /**
     * Clears all items.
     */
    public void jsxFunction_clear() {
        StorageImpl.getInstance().clear(type_, (HtmlPage) getWindow().getWebWindow().getEnclosedPage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        if (getWindow().getWebWindow() != null) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.STORAGE_OBSOLETE)) {
                return "StorageObsolete";
            }
        }
        return super.getClassName();
    }
}
