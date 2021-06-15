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
package org.apache.james.context;

/**
 * This class is a placeholder for Avalon Context keys.
 *
 * In order to decouple James from Phoenix, and to allow James
 * to run in any Avalon Framework container it is necessary that
 * James not depend on the BlockContext class from Phoenix, but
 * rather only on the Context interface.  This requires that we
 * look up context values directly, using String keys.  This
 * class stores the String keys that are used by James to
 * look up context values.
 * 
 * The lifetime of this class is expected to be limited.  At some
 * point in the near future the Avalon folks will make a decision
 * about how exactly to define, describe, and publish context
 * values.  At that point we can replace this temporary mechanism
 * with the Avalon mechanism.  Unfortunately right now that decision
 * is still unmade, so we need to use this class as a temporary
 * solution.
 */
public class AvalonContextConstants {

    /**
     * Private constructor to prevent instantiation or subclassing
     */
    private AvalonContextConstants() {}

    /**
     * The context key associated with the home directory of the application
     * being run.  The object returned on a 
     * context.get(AvalonContextConstants.APPLICATION_HOME) should be of
     * type <code>java.io.File</code> and should be the home directory
     * for the application (in our case, James)
     */
    public static final String APPLICATION_HOME = "app.home";
}
