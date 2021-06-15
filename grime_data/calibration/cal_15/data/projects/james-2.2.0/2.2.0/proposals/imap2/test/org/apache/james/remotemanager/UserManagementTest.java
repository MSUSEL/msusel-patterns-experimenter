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
package org.apache.james.remotemanager;

import org.apache.james.test.AbstractProtocolTest;
import org.apache.james.test.FileProtocolSessionBuilder;

public class UserManagementTest
        extends AbstractProtocolTest
{
    private String _userName;
    private String _password;
    private FileProtocolSessionBuilder builder = new FileProtocolSessionBuilder();

    public UserManagementTest( String action, String userName )
    {
        this( action, userName, "password" );
    }

    public UserManagementTest( String action, String userName, String password )
    {
        super( action );
        _userName = userName;
        _password = password;
    }

    public void setUp() throws Exception
    {
        super.setUp();
        builder.addTestFile( "RemoteManagerLogin.test", preElements );
        builder.addTestFile( "RemoteManagerLogout.test", postElements );
    }

    public void addUser() throws Exception
    {
          addUser( _userName, _password );
    }

    protected void addUser( String userName, String password )
            throws Exception
    {
        testElements.CL( "adduser " + userName + " " + password );
        testElements.SL( "User " + userName + " added", "Generated test." );
        runSessions();
    }

    /*protected void addExistingUser( String userName, String password )
        throws Exception{
        CL( "adduser " + userName + " " + password );
        SL( "user " + userName + " already exist" );
        executeTests();
    }*/

    public void deleteUser() throws Exception
    {
        deleteUser( _userName );
    }

    protected void deleteUser( String userName ) throws Exception
    {
        testElements.CL( "deluser " + userName );
        testElements.SL( "User " + userName + " deleted", "UserManagementTest.java:60" );
        runSessions();
    }
}
