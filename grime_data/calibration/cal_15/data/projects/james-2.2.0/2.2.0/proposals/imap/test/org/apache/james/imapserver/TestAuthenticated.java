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

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.net.Socket;
import java.io.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Date;

import org.apache.james.test.SimpleFileProtocolTest;
import org.apache.james.remotemanager.UserManagementTest;

public class TestAuthenticated
        extends SimpleFileProtocolTest implements IMAPTest
{
    public TestAuthenticated( String name )
    {
        super( name );
        _port = 143;
    }

    public void setUp() throws Exception
    {
        super.setUp();
        addTestFile( "Welcome.test", _preElements );
        addLogin( USER, PASSWORD );
    }

    protected void addLogin( String username, String password )
    {
        _testElements.add( new ClientRequest( "a001 LOGIN " + username + " " + password ) );
        _testElements.add( new ServerResponse( "a001 OK LOGIN completed" ));
    }

    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new TestAuthenticated( "Capability" ) );
        suite.addTest( new TestAuthenticated( "AuthenticateAuthenticated" ) );
        suite.addTest( new TestAuthenticated( "LoginAuthenticated" ) );
        suite.addTest( new TestAuthenticated( "Logout" ) );
        suite.addTest( new TestAuthenticated( "ExamineInbox" ) );
        suite.addTest( new TestAuthenticated( "SelectInbox" ) );
        suite.addTest( new TestAuthenticated( "Create" ) );
        suite.addTest( new TestAuthenticated( "ExamineEmpty" ) );
        suite.addTest( new TestAuthenticated( "SelectEmpty" ) );
        suite.addTest( new TestAuthenticated( "SelectInbox" ) );
        suite.addTest( new TestAuthenticated( "List" ) );
        suite.addTest( new TestAuthenticated( "List1" ) );
        suite.addTest( new TestAuthenticated( "List2" ) );

        suite.addTest( new TestAuthenticated( "Subscribe" ) );
        suite.addTest( new TestAuthenticated( "Subscribe2" ) );

        suite.addTest( new TestAuthenticated( "Delete" ) );

        return suite;
    }

}
