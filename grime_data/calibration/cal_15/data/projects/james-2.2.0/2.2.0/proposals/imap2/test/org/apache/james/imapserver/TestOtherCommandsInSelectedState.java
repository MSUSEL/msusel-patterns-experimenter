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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests commands which are valid in AUTHENTICATED and NONAUTHENTICATED by running
 * them in the SELECTED state. Many commands function identically, while others
 * are invalid in this state.
 *
 *
 * @version $Revision: 1.1.2.3 $
 */
public class TestOtherCommandsInSelectedState
        extends TestCommandsInAuthenticatedState
{
    public TestOtherCommandsInSelectedState( String name )
    {
        super( name );
    }

    /**
     * Superclass sets up welcome message and login session in {@link #preElements}.
     * A "SELECT INBOX" session is then added to these elements.
     * @throws Exception
     */
    public void setUp() throws Exception
    {
        super.setUp();
        addTestFile( "SelectInbox.test", preElements );
    }

    /**
     * Provides all tests which should be run in the selected state. Each test name
     * corresponds to a protocol session file.
     */
    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();
        // Not valid in this state
        suite.addTest( new TestOtherCommandsInSelectedState( "ValidNonAuthenticated" ) );

        // Valid in all states
        suite.addTest( new TestOtherCommandsInSelectedState( "Capability" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "Noop" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "Logout" ) );

        // Valid in authenticated state
        suite.addTest( new TestOtherCommandsInSelectedState( "Create" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "ExamineEmpty" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "SelectEmpty" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "ListNamespace" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "ListMailboxes" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "Status" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "StringArgs" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "Subscribe" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "Append" ) );
        suite.addTest( new TestOtherCommandsInSelectedState( "Delete" ) );

        return suite;
    }
}
