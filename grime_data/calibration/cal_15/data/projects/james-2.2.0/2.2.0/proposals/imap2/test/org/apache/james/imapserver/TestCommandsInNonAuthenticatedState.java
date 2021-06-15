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
package org.apache.james.imapserver;

import org.apache.james.test.SimpleFileProtocolTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Runs tests for commands valid in the NON_AUTHENTICATED state.
 * A welcome message precedes the execution of the test elements.
 */
public class TestCommandsInNonAuthenticatedState
        extends SimpleFileProtocolTest
{
    public TestCommandsInNonAuthenticatedState( String name )
    {
        super( name );
    }

    /**
     * Adds a welcome message to the {@link #preElements}.
     * @throws Exception
     */
    public void setUp() throws Exception
    {
        super.setUp();
        addTestFile( "Welcome.test", preElements );
    }

    /**
     * Sets up tests valid in the non-authenticated state.
     */ 
    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();
        // Not valid in this state
        suite.addTest( new TestCommandsInNonAuthenticatedState( "ValidAuthenticated" ) );
        suite.addTest( new TestCommandsInNonAuthenticatedState( "ValidSelected" ) );

        // Valid in all states
        suite.addTest( new TestCommandsInNonAuthenticatedState( "Capability" ) );
        suite.addTest( new TestCommandsInNonAuthenticatedState( "Noop" ) );
        suite.addTest( new TestCommandsInNonAuthenticatedState( "Logout" ) );

        // Valid only in non-authenticated state.
        suite.addTest( new TestCommandsInNonAuthenticatedState( "Authenticate" ) );
        suite.addTest( new TestCommandsInNonAuthenticatedState( "Login" ) );

        return suite;
    }

}
