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
package org.apache.james.test;

import org.apache.james.imapserver.ImapHandler;
import org.apache.james.imapserver.ImapHost;
import org.apache.james.imapserver.ImapRequestHandler;
import org.apache.james.imapserver.ImapSession;
import org.apache.james.imapserver.ImapSessionImpl;
import org.apache.james.imapserver.ImapTest;
import org.apache.james.imapserver.JamesImapHost;
import org.apache.james.imapserver.store.MailboxException;
import org.apache.james.services.User;
import org.apache.james.services.UsersRepository;
import org.apache.james.userrepository.AbstractUsersRepository;
import org.apache.james.userrepository.DefaultUser;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Abstract Protocol Test is the root of all of the James Imap Server test
 * cases.  It provides basic functionality for running a protocol session
 * as a JUnit test, and failing if exceptions are thrown.
 * To create a test which reads the entire protocol session from a single
 * protocol definition file, use the {@link SimpleFileProtocolTest}.
 *
 */
public abstract class AbstractProtocolTest
        extends TestCase implements ImapTest
{
    /** The Protocol session which is run before the testElements */
    protected ProtocolSession preElements = new ProtocolSession();
    /** The Protocol session which contains the tests elements */
    protected ProtocolSession testElements = new ProtocolSession();
    /** The Protocol session which is run after the testElements. */
    protected ProtocolSession postElements = new ProtocolSession();

    /** The host name to connect to for socket-based testing. */
    protected String host = HOST;
    /** The host port to connect to for socket-based testing. */
    protected int port = PORT;
    /** The timeout to set on the socket for socket-based testing. */
    protected int timeout = TIMEOUT;

    /** A UsersRepository which all tests share. */
    private UsersRepository users;
    /** An ImapHost instance which all tests share. */
    private ImapHost imapHost;

    public AbstractProtocolTest( String s )
    {
        super( s );
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        setUpEnvironment();
    }

    /**
     * Uses a system property to determine whether to run tests locally, or against
     * a remote server.
     */
    protected void runSessions() throws Exception
    {
        String runLocal = System.getProperty( "runTestsLocal", "true" );
        boolean local = Boolean.valueOf( runLocal ).booleanValue();
        if ( local ) {
            runLocalProtocolSessions();
        }
        else {
            runSocketProtocolSessions();
        }
    }

    /**
     * Runs the pre,test and post protocol sessions against a running instance of James,
     * by communicating via a socket connection. After a request is sent, the server response
     * is parsed to determine if the actual response matches that expected.
     */
    private void runSocketProtocolSessions()
            throws Exception
    {
        Socket socket = new Socket( host, port );
        socket.setSoTimeout( timeout );

        PrintWriter out = new PrintWriter( socket.getOutputStream(), true );
        BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
        try {
             preElements.runLiveSession( out, in );
             testElements.runLiveSession( out, in );
             postElements.runLiveSession( out, in );
         }
         catch ( ProtocolSession.InvalidServerResponseException e ) {
             fail( e.getMessage() );
         }

        out.close();
        in.close();
        socket.close();
     }

    /**
     * Runs the pre,test and post protocol sessions against a local copy of the ImapServer.
     * This does not require that James be running, and is useful for rapid development and
     * debugging.
     *
     * Instead of sending requests to a socket, requests are written to a CharArrayWriter,
     * which then constructs a Reader which can be given to the ImapRequestHandler.
     * Likewise, server responses are written to a CHarArrayWriter, and the associate reader
     * is parsed to ensure that the responses match those expected.
     */
    private void runLocalProtocolSessions() throws Exception
    {
        ByteArrayOutputStream clientRequestCollector = new ByteArrayOutputStream();
        PrintWriter clientOut = new PrintWriter( clientRequestCollector );
        preElements.writeClient( clientOut );
        testElements.writeClient( clientOut );
        postElements.writeClient( clientOut );

        InputStream clientIn = new ByteArrayInputStream( clientRequestCollector.toByteArray() );
        clientRequestCollector.close();

        ByteArrayOutputStream serverResponseCollector = new ByteArrayOutputStream();
        serverResponseCollector.write( "* OK IMAP4rev1 Server XXX ready".getBytes() );
        serverResponseCollector.write( '\r' );
        serverResponseCollector.write( '\n' );

        ImapSession session = getImapSession();
        ImapRequestHandler requestHandler = new ImapRequestHandler();
        while( requestHandler.handleRequest( clientIn, serverResponseCollector, session ) ) {};

        InputStream serverInstream = new ByteArrayInputStream( serverResponseCollector.toByteArray() );
        BufferedReader serverIn = new BufferedReader( new InputStreamReader( serverInstream ) );

        try {
            preElements.testResponse(  serverIn );
            testElements.testResponse(  serverIn );
            postElements.testResponse(  serverIn );
        }
        catch ( ProtocolSession.InvalidServerResponseException e ) {
            fail( e.getMessage() );
        }
    }

    /**
     * Provides an ImapSession to use for this test. An ImapSession is accosiated
     * with a single client connection.
     */
    private ImapSession getImapSession() throws MailboxException
    {
        ImapSession session = new ImapSessionImpl( imapHost, users, new ImapHandler(), null, null );
        return session;
    }

    /**
     * Initialises the UsersRepository and ImapHost on first call.
     * TODO enable logging, set up components properly.
     */
    private void setUpEnvironment() throws MailboxException
    {
        if ( users == null || imapHost == null ) {
            users = new InMemoryUsersRepository();
            DefaultUser user = new DefaultUser( USER, "SHA" );
            user.setPassword( PASSWORD );
            users.addUser( user );

            imapHost = new JamesImapHost();
            imapHost.createPrivateMailAccount( user );
        }
    }

    /**
     * A simple, dummy implementation of UsersRepository to use for testing,
     * which stored all users in memory.
     */
    private class InMemoryUsersRepository extends AbstractUsersRepository
    {
        private Map users = new HashMap();

        protected Iterator listAllUsers()
        {
            return users.values().iterator();
        }

        protected void doAddUser( User user )
        {
            users.put( user.getUserName(), user );
        }

        protected void doRemoveUser( User user )
        {
            users.remove( user.getUserName());
        }

        protected void doUpdateUser( User user )
        {
            users.put( user.getUserName(), user );
        }
    }
}
