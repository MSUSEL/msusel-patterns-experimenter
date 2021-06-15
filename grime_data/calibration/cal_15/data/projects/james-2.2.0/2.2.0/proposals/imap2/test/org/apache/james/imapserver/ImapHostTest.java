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

import org.apache.james.imapserver.store.ImapMailbox;
import org.apache.james.imapserver.store.InMemoryStore;
import org.apache.james.imapserver.store.MailboxException;
import org.apache.james.services.User;
import org.apache.james.userrepository.DefaultUser;

import junit.framework.TestCase;

/**
 * A test for implementations of the {@link ImapHost} interface.
 *
 * TODO Tests to write:
 *   - Creating and accessing mailboxes with qualified names
 *   - Create existing mailbox
 *   - Delete Inbox
 *   - Rename
 *   - Rename Inbox
 *   - ListMailboxes
 *   - Copying messages - need to make sure that the copied message
 *                        is independent of the original
 *  
 *
 * @version $Revision: 1.4.2.3 $
 */
public class ImapHostTest extends TestCase
        implements ImapConstants
{
    private ImapHost imapHost;
    private User user;

    public ImapHostTest( String s )
    {
        super( s );
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        user = new DefaultUser( "user", null );

        imapHost = getHostImplementation();
        imapHost.createPrivateMailAccount( user );
    }

    protected ImapHost getHostImplementation()
    {
        return new JamesImapHost( new InMemoryStore() );
    }

    /**
     * Tests creation of mailboxes in user's personal space. No namespaces are used.
     */
    public void testCreatePersonal() throws Exception
    {
        // Create a single mailbox.
        create( "test" );
        assertMailbox( "test", true );

        // Create a child of an existing mailbox.
        create("test.another" );
        assertMailbox( "test", true );
        assertMailbox( "test.another", true );

        // A multi-level create, which creates intervening mailboxes,
        // with the \NoSelect attribute set.
        create( "this.is.another.mailbox");
        assertMailbox( "this", false );
        assertMailbox( "this.is", false );
        assertMailbox( "this.is.another", false );
        assertMailbox( "this.is.another.mailbox", true );

        // Create a child of an existing, no-select mailbox.
        create( "this.is.yet.another.mailbox");
        assertMailbox( "this", false );
        assertMailbox( "this.is", false );
        assertMailbox( "this.is.yet", false );
        assertMailbox( "this.is.yet.another", false );
        assertMailbox( "this.is.yet.another.mailbox", true );
    }

    /**
     * Tests deletion of mailboxes in user's personal space. No namespaces are used.
     * @throws Exception
     */
    public void testDelete() throws Exception
    {
        // Simple create/delete
        create( "test" );
        assertMailbox( "test", true );
        delete( "test" );
        assertNoMailbox( "test");

        // Create a chain and delete the parent.
        // Child should remain, and parent be switched to NoSelect.
        create( "one" );
        create( "one.two" );
        assertMailbox( "one", true );
        assertMailbox( "one.two", true );
        delete( "one");
        assertMailbox( "one", false);
        assertMailbox( "one.two", true );

        // Can't delete mailbox with NoSelect attribute and children.
        try
        {
            delete( "one" );
            fail( "Should not be able to delete a non-selectabl mailbox which has children." );
        }
        catch( MailboxException e )
        {
            // TODO check for correct exception.
        }

        // Delete the child, then the non-selectable parent
        delete( "one.two");
        delete( "one");
        assertNoMailbox( "one.two" );
        assertNoMailbox( "one" );
    }

    /**
     * Checks that a mailbox with the supplied name exists, and that its
     * NoSelect flag matches that expected.
     */
    private void assertMailbox( String name, boolean selectable ) throws MailboxException
    {
        ImapMailbox mailbox = imapHost.getMailbox( user, name );
        assertNotNull( "Mailbox <" + name + "> expected to exist in store.",
                       mailbox );
        if ( selectable )
        {
            assertTrue( "Mailbox <" + name + "> not selectable.",
                        mailbox.isSelectable() );
        }
        else
        {
            assertTrue( "Mailbox <" + name + "> should not be selectable.",
                        ! mailbox.isSelectable() );
        }
    }

    /**
     * Asserts that a mailbox with the supplied name doesn't exist.
     */
    private void assertNoMailbox( String name ) throws Exception
    {
        ImapMailbox mailbox = imapHost.getMailbox( user, name );
        assertNull( "Mailbox <" + name + "> should not exist.",
                    mailbox );
    }

    /**
     * Calls {@link ImapHost#createMailbox} with the specified name and the test user.
     */
    private ImapMailbox create( String name ) throws Exception
    {
        return imapHost.createMailbox( user, name );
    }

    /**
     * Calls {@link ImapHost#deleteMailbox} with the specified name and the test user.
     */
    private void delete( String name ) throws Exception
    {
        imapHost.deleteMailbox( user, name );
    }
}
