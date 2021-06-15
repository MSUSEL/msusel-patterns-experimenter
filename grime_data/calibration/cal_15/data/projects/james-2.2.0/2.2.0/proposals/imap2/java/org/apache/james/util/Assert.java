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
package org.apache.james.util;


/**
 * A set of debugging utilities.
 */
public final class Assert
{
    public static final boolean ON = true;

    // Can't instantiate.
    private Assert()
    {
    };

    /**
     * Checks the supplied boolean expression, throwing an AssertionException if false;
     */
    public static void isTrue( boolean expression )
    {
        if ( !expression ) {
            throw new RuntimeException( "Assertion Failed." );
        }
    }

    /**
     * Fails with an assertion exception.
     */
    public static void fail()
    {
        throw new RuntimeException( "Assertion error - should not reach here." );
    }

    /**
     * Fails, indicating not-yet-implemented features.
     */
    public static void notImplemented()
    {
        throw new RuntimeException( "Not implemented" );
    }

}
