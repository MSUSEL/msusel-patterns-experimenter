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
 * Runs tests for commands valid only in the SELECTED state. A login session
 * and setup of a "seleted" mailbox precedes the execution of the test elements.
 */
public class TestSelectedCommandsInSelectedState
        extends TestCommandsInAuthenticatedState
{
    public TestSelectedCommandsInSelectedState( String name )
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
        addTestFile( "SelectedStateSetup.test", preElements );
        addTestFile( "SelectedStateCleanup.test", postElements );
    }

    /**
     * Provides all tests which should be run in the selected state. Each test name
     * corresponds to a protocol session file.
     */
    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();

        // Valid in selected state
        suite.addTest( new TestSelectedCommandsInSelectedState( "Check" ) );
        suite.addTest( new TestSelectedCommandsInSelectedState( "Expunge" ) );
        suite.addTest( new TestSelectedCommandsInSelectedState( "Search" ) );
        suite.addTest( new TestSelectedCommandsInSelectedState( "FetchSingleMessage" ) );
//        suite.addTest( new TestSelectedCommandsInSelectedState( "FetchMultipleMessages" ) );
        suite.addTest( new TestSelectedCommandsInSelectedState( "Store" ) );
        suite.addTest( new TestSelectedCommandsInSelectedState( "Copy" ) );
        suite.addTest( new TestSelectedCommandsInSelectedState( "Uid" ) );

        return suite;
    }
}
