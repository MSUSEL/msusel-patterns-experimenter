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
package org.hibernate.bytecode.internal.javassist;

/**
 * An exception thrown while generating a bulk accessor.
 * 
 * @author Muga Nishizawa
 * @author modified by Shigeru Chiba
 */
public class BulkAccessorException extends RuntimeException {
    private Throwable myCause;

    /**
     * Gets the cause of this throwable.
     * It is for JDK 1.3 compatibility.
     */
    public Throwable getCause() {
        return (myCause == this ? null : myCause);
    }

    /**
     * Initializes the cause of this throwable.
     * It is for JDK 1.3 compatibility.
     */
    public synchronized Throwable initCause(Throwable cause) {
        myCause = cause;
        return this;
    }

    private int index;

    /**
     * Constructs an exception.
     */
    public BulkAccessorException(String message) {
        super(message);
        index = -1;
        initCause(null);
    }

    /**
     * Constructs an exception.
     *
     * @param index     the index of the property that causes an exception.
     */
    public BulkAccessorException(String message, int index) {
        this(message + ": " + index);
        this.index = index;
    }

    /**
     * Constructs an exception.
     */
    public BulkAccessorException(String message, Throwable cause) {
        super(message);
        index = -1;
        initCause(cause);
    }

    /**
     * Constructs an exception.
     *
     * @param index     the index of the property that causes an exception.
     */
    public BulkAccessorException(Throwable cause, int index) {
        this("Property " + index);
        this.index = index;
        initCause(cause);
    }

    /**
     * Returns the index of the property that causes this exception.
     *
     * @return -1 if the index is not specified.
     */
    public int getIndex() {
        return this.index;
    }
}
