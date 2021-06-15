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

import java.util.Map;
import java.util.Set;

/**
 * Interface for objects representing the record of a folder on an IMAP host.
 * 
 * @version 0.1 on 14 Dec 2000
 */

public interface FolderRecord {
             
    /**
     * Returns the full name, including namespace, of this mailbox. 
     * Example 1: '#mail.projectBonzi'
     * Example 2: '#shared.projectBonzi'
     *
     * @return String mailbox hierarchical name including namespace
     */
    //String readAstring();

    /**
     * Returns the user in whose namespace the mailbox existed.
     * Example 1: 'fred.flintstone'
     * Example 2: ''
     *
     * @param user String a user.An empty string indicates that the 
     * mailbox name is absolute.
     */
    String getUser();

    /**
     * Returns the absolute name of this mailbox. The absolute name is
     * user-independent and unique for a given host.
     * Example 1: 'privatemail.fred.flintstone.projectBonzi'
     * Example 2: '#shared.projectBonzi'
     *
     * @return String mailbox absolute name
     */
    String getAbsoluteName();

    /**
     * Records if this mailbox name is currently in use. The mailbox name is
     * in use when a mailbox with this name has been created. Implementations
     * that allow shared mailboxes may encounter a sate where the mailbox has
     * been deleted but there are clients who were already connected to the
     * mailbox. In this case the name remains in use until all clients have
     * either de-selected the mailbox or been disconnected from the server. 
     *
     * @param state boolean true when mailbox created, false when name no
     * longer in use.
     */
    void setNameInUse(boolean state);

    /**
     * Returns unavailability of name for a new mailbox.
     *
     * @return true if this name is in use. Must return true if isDeleted
     * returns false.
     */
    boolean isNameInUse();

    /**
     *  Records if the corresponding mailbox has been deleted.
     *
     * @param state boolean true when mailbox deleted, false when created
     */
    void setDeleted(boolean state);

    /**
     * Returns whether mailbox has been deleted. A deleted mailbox is an
     * invalid argument to any IMAP command..
     *
     * @return boolean true if mailbox does not exist
     */
    boolean isDeleted();

    /**
     * Records the Unique Identifier Validity Value for this mailbox.
     *
     * @param uidValidity int the uid validity value must be incremented if
     * the current uid values overlap uid values of this or a previous
     * incarnation of the mailbox.
     */
    void setUidValidity(int uidValidity);

    /**
     * Returns current uid validity value
     *
     * @return int uid validity value
     */
    int getUidValidity();

    /**
     * Records the highest assigned Unique Identifier Value for this mailbox.
     *
     * @param uid int the highest uid assigned to a message in this mailbox.
     */
    void setHighestUid(int uid);

    /**
     * Returns current highest assigned uid value
     *
     * @return int uid  value
     */
    int getHighestUid();

    /**
     * Record which users have LookupRights.
     *
     * @param users Set of Strings, one per user with Lookup rights
     */
    void setLookupRights(Set users);

    /**
     * Indicates if given user has lookup rights for this mailbox.  Need
     * lookup rights to be included in a List response.
     *
     * @return boolean true if user has lookup rights
     */
    boolean hasLookupRights(String user);

    /**
     * Record which users have ReadRights.
     *
     * @param users Set of Strings, one per user with read rights
     */
    void setReadRights(Set users);

    /**
     * Indicates if given user has read rights for this mailbox. Need read
     * rights for user to select or examine mailbox.  
     *
     * @return boolean true if user has read rights
     */
    boolean hasReadRights(String user);

    /**
     * Record if mailbox is marked.
     */
    void setMarked(boolean mark);

    /**
     * Indicates if the mailbox is marked. Usually means unseen mail.
     *
     * @return boolean true if marked
     */
    boolean isMarked();

    /**
     * Mark this mailbox as not selectable by anyone. 
     * Example folders at the roots of hierarchies, e. #mail for each user.
     *
     * @param state true if folder is not selectable by anyone
     */
    void setNotSelectableByAnyone(boolean state);

    boolean isNotSelectableByAnyone();

    /**
     * A folder is selectable by a given user if both it is not
     * NotSelectableByAnyone and the named user has read rights.
     *
     * @param user the user to be tested
     * @return true if user can SELECT this mailbox.
     */
    boolean isSelectable(String user);

    /**
     * Set number of messages in this folder
     */
    void setExists(int num);

    /**
     * Indicates number of messages in folder
     *
     * @return int number of messages
     */
    int getExists();

    /**
     * Set number of messages in this folder with Recent flag set
     */
    void setRecent(int num);

    /**
     * Indicates no of messages with \Recent flag set
     *
     * @return int no of messages with \Recent flag set
     */
    int getRecent();

    /**
     * Set map of users versus number of messages in this folder without
     * \Seen flag set for them
     */
    void setUnseenbyUser(Map unseen);

    /** 
     * Indicates the number of  unseen messages for the specified user. 
     *
     * @return int number of messages without \Seen flag set for this User.
     */
    int getUnseen(String user);
}
