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

import org.apache.james.imapserver.AccessControlException;
import org.apache.james.imapserver.AuthorizationException;

import java.io.Serializable;
import java.util.Set;

/**
 * Interface for objects representing for an IMAP4rev1 Access Control List.
 * There should be one instance of this class per open mailbox. An Access
 * control list, for IMAP purposes, is a list of <identifier, rights> pairs.
 *
 * <p>The standard rights in RFC2086 are:
 * <br>l - lookup (mailbox is visible to LIST/LSUB commands)
 * <br>r - read (SELECT the mailbox, perform CHECK, FETCH, PARTIAL, SEARCH,
 * COPY from mailbox)
 * <br>s - keep seen/unseen information across sessions (STORE SEEN flag)
 * <br>w - write (STORE flags other than SEEN and DELETED)
 * <br>i - insert (perform APPEND, COPY into mailbox)
 * <br>p - post (send mail to submission address for mailbox, not enforced by
 * IMAP4 itself)
 * <br>c - create (CREATE new sub-mailboxes in any implementation-defined
 * hierarchy)
 * <br>d - delete (STORE DELETED flag, perform EXPUNGE)
 * <br>a - administer (perform SETACL)
 *
 *
 * <p>References: rfc 2060, rfc 2086
 * @version 0.1  on 14 Dec 2000
 */
public interface ACL 
    extends Serializable {

    char LOOKUP_RIGHTS = 'l';
    char READ_RIGHTS = 'r';
    char KEEP_SEEN_RIGHTS = 's';
    char WRITE_RIGHTS = 'w';
    char INSERT_RIGHTS = 'i';
    char POST_RIGHTS = 'p';
    char CREATE_RIGHTS = 'c';
    char DELETE_RIGHTS = 'd';
    char ADMIN_RIGHTS = 'a';
    char ADD_RIGHTS = '+';
    char REMOVE_RIGHTS = '-';
    char[] RIGHTS = 
    {
        LOOKUP_RIGHTS, READ_RIGHTS, KEEP_SEEN_RIGHTS, WRITE_RIGHTS,
        INSERT_RIGHTS, POST_RIGHTS, CREATE_RIGHTS, DELETE_RIGHTS,
        ADMIN_RIGHTS
    };

    /**
     * Store access rights for a given identity.
     * The setter is the user setting the rights, the identifier is the user
     * whose rights are affected.
     * The setter and identifier arguments must be non-null and non-empty.
     * The modification argument must be non-null and follow the syntax of the
     * third argument to a SETACL command.
     * If the modification argument is an empty string, that identifier is
     * removed from the ACL, if currently present.
     *
     * @param setter String representing user attempting to set rights, must
     * be non-null and non-empty
     * @param identity String representing user whose rights are being set,
     * must be non-null and non-empty
     * @param modification String representing the change in rights, following
     * the syntax specified in rfc 2086
     * @return true if requested modification succeeded. A return value of
     * false means an error other than an AccessControlException or
     * AuthorizationException.
     * @throws AccessControlException if setter does not have lookup rights for
     * this mailbox (ie they should not know this mailbox exists).
     * @throws AuthorizationException if specified setter does not have the
     * administer right (ie the right to write ACL rights), or if the result
     * of this method would leave no identities with admin rights.
     */
    boolean setRights( String setter, 
                       String identifier,
                       String modification)
        throws AccessControlException, AuthorizationException;

    /**
     * Retrieve access rights for a specific identity.
     *
     * @param getter String representing user attempting to get the rights,
     * must be non-null and non-empty
     * @param identity String representing user whose rights are being got,
     * must be non-null and non-empty
     * @return String of rights usingrfc2086 syntax, empty if identity has no
     * rights in this mailbox.
     * @throws AccessControlException if getter does not have lookup rights for
     * this mailbox (ie they should not know this mailbox exists).
     * @throws AuthorizationException if implementation does not wish to expose
     * ACL for this identity to this getter.
     */
    String getRights( String getter, String identity )
        throws AccessControlException, AuthorizationException;

    /**
     * Retrieves a String of one or more <identity space rights> who have
     * rights in this ACL
     *
     * @param getter String representing user attempting to get the rights,
     * must be non-null and non-empty
     * @return String of rights sets usingrfc2086 syntax
     * @throws AccessControlException if getter does not have lookup rights for
     * this mailbox (ie they should not know this mailbox exists).
     * @throws AuthorizationException if implementation does not wish to expose
     * ACL to this getter.
     */
    String getAllRights( String getter )
        throws AccessControlException, AuthorizationException;

    /**
     * Retrieve rights which will always be granted to the specified identity.
     *
     * @param getter String representing user attempting to get the rights,
     * must be non-null and non-empty
     * @param identity String representing user whose rights are being got,
     * must be non-null and non-empty
     * @return String of rights usingrfc2086 syntax, empty if identity has no
     * guaranteed rights in this mailbox.
     * @throws AccessControlException if getter does not have lookup rights for
     * this mailbox (ie they should not know this mailbox exists).
     * @throws AuthorizationException if implementation does not wish to expose
     * ACL for this identity to this getter.
     */
    String getRequiredRights( String getter, String identity )
        throws AccessControlException, AuthorizationException;

    /**
     * Retrieve rights which may be granted to the specified identity.
     * @param getter String representing user attempting to get the rights,
     * must be non-null and non-empty
     * @param identity String representing user whose rights are being got,
     * must be non-null and non-empty
     * @return String of rights usingrfc2086 syntax, empty if identity has no
     * guaranteed rights in this mailbox.
     * @throws AccessControlException if getter does not have lookup rights for
     * this mailbox (ie they should not know this mailbox exists).
     * @throws AuthorizationException if implementation does not wish to expose
     * ACL for this identity to this getter.
     */
    String getOptionalRights( String getter, String identity )
        throws AccessControlException, AuthorizationException;

    /**
     * Helper boolean methods.
     * Provided for cases where you need to check the ACL before selecting the
     * mailbox.
     *
     * @param username String representing user
     * @return true if user has the requested right.
     * &throws AccessControlException if username does not have lookup rights.
     * (Except for hasLookupRights which just returns false.
     */
    boolean hasReadRights( String username )
        throws AccessControlException;
    boolean hasKeepSeenRights( String username )
        throws AccessControlException;
    boolean hasWriteRights( String username )
        throws AccessControlException;
    boolean hasInsertRights( String username )
        throws AccessControlException;
    boolean hasDeleteRights( String username )
        throws AccessControlException;
    boolean hasAdminRights( String username )
        throws AccessControlException;

    Set getUsersWithLookupRights();

    Set getUsersWithReadRights();
}

