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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

/**
 * A collection of constants that represent the various ways a form can be encoded when submitted.
 *
 * @version $Revision: 5724 $
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public final class FormEncodingType implements Serializable {

    private static final long serialVersionUID = -7341913381207910442L;

    /** URL-encoded form encoding. */
    public static final FormEncodingType URL_ENCODED = new FormEncodingType("application/x-www-form-urlencoded");

    /** Multipart form encoding (used to be a constant in HttpClient but it was deprecated with no alternative). */
    public static final FormEncodingType MULTIPART = new FormEncodingType("multipart/form-data");

    private final String name_;

    private FormEncodingType(final String name) {
        name_ = name;
    }

    /**
     * Returns the name of this encoding type.
     *
     * @return the name of this encoding type
     */
    public String getName() {
        return name_;
    }

    /**
     * Returns the constant that matches the specified name.
     *
     * @param name the name to search by
     * @return the constant corresponding to the specified name, {@link #URL_ENCODED} if none match.
     */
    public static FormEncodingType getInstance(final String name) {
        final String lowerCaseName = name.toLowerCase();

        if (MULTIPART.getName().equals(lowerCaseName)) {
            return MULTIPART;
        }

        return URL_ENCODED;
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "EncodingType[name=" + getName() + "]";
    }
}
