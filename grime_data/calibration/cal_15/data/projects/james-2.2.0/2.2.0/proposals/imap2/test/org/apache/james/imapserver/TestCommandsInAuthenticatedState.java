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
 * Runs tests for commands valid in the AUTHENTICATED state. A login session precedes
 * the execution of the test elements.
 */
public class TestCommandsInAuthenticatedState
        extends SimpleFileProtocolTest implements ImapTest
{
    public TestCommandsInAuthenticatedState( String name )
    {
        super( name );
    }

    /**
     * Sets up {@link #preElements} with a welcome message and login request/response.
     * @throws Exception
     */
    public void setUp() throws Exception
    {
        super.setUp();
        addTestFile( "Welcome.test", preElements );
        addLogin( USER, PASSWORD );
    }

    protected void addLogin( String username, String password )
    {
        preElements.CL( "a001 LOGIN " + username + " " + password );
        preElements.SL( "a001 OK LOGIN completed", "TestCommandsInAuthenticatedState.java:33" );
    }

    /**
     * Provides all tests which should be run in the authenicated state. Each test name
     * corresponds to a protocol session file.
     */
    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();
        // Not valid in this state
        suite.addTest( new TestCommandsInAuthenticatedState( "ValidSelected" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "ValidNonAuthenticated" ) );

        // Valid in all states
        suite.addTest( new TestCommandsInAuthenticatedState( "Capability" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "Noop" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "Logout" ) );

        // Valid in authenticated state
        suite.addTest( new TestCommandsInAuthenticatedState( "ExamineInbox" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "SelectInbox" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "Create" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "ExamineEmpty" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "SelectEmpty" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "ListNamespace" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "ListMailboxes" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "Status" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "Subscribe" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "Delete" ) );
        suite.addTest( new TestCommandsInAuthenticatedState( "Append" ) );

        return suite;
    }

}
