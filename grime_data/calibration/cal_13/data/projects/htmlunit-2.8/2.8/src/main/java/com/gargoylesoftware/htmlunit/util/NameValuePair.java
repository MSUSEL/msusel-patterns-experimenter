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
package com.gargoylesoftware.htmlunit.util;

import java.io.Serializable;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.LangUtils;

/**
 * A name/value pair.
 *
 * @version $Revision: 5724 $
 * @author Daniel Gredler
 * @author Nicolas Belisle
 */
public class NameValuePair implements Serializable {

    private static final long serialVersionUID = 7787648500094609403L;

    /** The name. */
    private final String name_;

    /** The value. */
    private final String value_;

    /**
     * Creates a new instance.
     * @param name the name
     * @param value the value
     */
    public NameValuePair(final String name, final String value) {
        name_ = name;
        value_ = value;
    }

    /**
     * Returns the name.
     * @return the name
     */
    public String getName() {
        return name_;
    }

    /**
     * Returns the value.
     * @return the value
     */
    public String getValue() {
        return value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof NameValuePair)) {
            return false;
        }
        final NameValuePair other = (NameValuePair) object;
        return LangUtils.equals(name_, other.name_) && LangUtils.equals(value_, other.value_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = LangUtils.HASH_SEED;
        hash = LangUtils.hashCode(hash, name_);
        hash = LangUtils.hashCode(hash, value_);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name_ + "=" + value_;
    }

    /**
     * Converts the specified name/value pairs into HttpClient name/value pairs.
     * @param pairs the name/value pairs to convert
     * @return the converted name/value pairs
     */
    public static org.apache.http.NameValuePair[] toHttpClient(final NameValuePair[] pairs) {
        final org.apache.http.NameValuePair[] pairs2 =
            new org.apache.http.NameValuePair[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            final NameValuePair pair = pairs[i];
            pairs2[i] = new BasicNameValuePair(pair.getName(), pair.getValue());
        }
        return pairs2;
    }

    /**
     * Converts the specified name/value pairs into HttpClient name/value pairs.
     * @param pairs the name/value pairs to convert
     * @return the converted name/value pairs
     */
    public static org.apache.http.NameValuePair[] toHttpClient(final List<NameValuePair> pairs) {
        final org.apache.http.NameValuePair[] pairs2 = new org.apache.http.NameValuePair[pairs.size()];
        for (int i = 0; i < pairs.size(); i++) {
            final NameValuePair pair = pairs.get(i);
            pairs2[i] = new BasicNameValuePair(pair.getName(), pair.getValue());
        }
        return pairs2;
    }

}
