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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.resources;

import java.util.Arrays;
//import java.io.Console;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Method;


/**
 * Temporary methods to be removed when we will be allowed to compile for Java 6.
 * The Java 6 methods that we would like to use are commented-out at the begining
 * of each method.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class Java6 {
    private Java6() {
    }

    /**
     * Placeholder for {@link Arrays#binarySearch(int[], int, int, int)}.
     *
     * Note that I'm too lazy for implementing a real binary search.
     * Lets switch to Java 6 and throw away this code soon!
     */
    public static int binarySearch(final int[] a, final int fromIndex, final int toIndex, final int key) {
//      return Arrays.binarySearch(a, fromIndex, toIndex, key);

        for (int i=fromIndex; i<toIndex; i++) {
            final int v = a[i];
            if (v == key) {
                return i;
            }
            if (v > key) {
                return ~i;
            }
        }
        return ~toIndex;
    }

    /**
     * Returns the console reader, or {@code null} if none.
     */
    public static Reader consoleReader() {
//      Console console = System.console();
//      return (console != null) ? console.reader() : null;

        Method method;
        try {
            method = System.class.getMethod("console", (Class[]) null);
        } catch (NoSuchMethodException exception) {
            return null; // We are not running Java 6.
        }
        try {
            final Object console = method.invoke(null, (Object[]) null);
            if (console != null) {
                method = console.getClass().getMethod("reader", (Class[]) null);
                return (Reader) method.invoke(console, (Object[]) null);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            // Any kind of checked exception at this point would be a programming error.
            throw new AssertionError(e);
        }
        return null;
    }

    /**
     * Returns the console writer, or {@code null} if none.
     */
    public static PrintWriter consoleWriter() {
//      Console console = System.console();
//      return (console != null) ? console.writer() : null;

        Method method;
        try {
            method = System.class.getMethod("console", (Class[]) null);
        } catch (NoSuchMethodException exception) {
            return null; // We are not running Java 6.
        }
        try {
            final Object console = method.invoke(null, (Object[]) null);
            if (console != null) {
                method = console.getClass().getMethod("writer", (Class[]) null);
                return (PrintWriter) method.invoke(console, (Object[]) null);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            // Any kind of checked exception at this point would be a programming error.
            throw new AssertionError(e);
        }
        return null;
    }
}
