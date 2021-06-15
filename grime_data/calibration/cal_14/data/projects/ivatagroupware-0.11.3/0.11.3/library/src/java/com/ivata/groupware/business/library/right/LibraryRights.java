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
package com.ivata.groupware.business.library.right;

import java.util.Collection;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.library.comment.CommentDO;
import com.ivata.mask.util.SystemException;

/**
 * <p>
 * TODO: add a comment for this type.
 * </p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Jun 19, 2004
 * @version $Revision: 1.4 $
 */

public interface LibraryRights {

    /**
     * <p>
     * This method changes ADD rights. Users in the specified groups will be
     * able to add ITEMS with this TOPIC.
     * </p>
     *
     * <p>
     * It's works only with those groups which can be seen by the user.
     * if I am adding ADD right for a group and the user has not VIEW right
     * -> also create a VIEW right for that group.
     * </p>
     *
     * @param id of TOPIC
     * @param userName user vhich is goin to change rights
     * @param rights collection of groups for which we will set up ADD right
     */
    public abstract void amendAddRightsForItem(SecuritySession securitySession,
            Integer id, Collection rights)
            throws SystemException;

    /**
     * <p>This method changing AMEND rights. Users in those groups will be able to amend ITEMS with this TOPIC.
     * It's working only with those groups which can be see by user.
     * if I am adding AMEND right for group and there is not VIEW right -> so create VIEW right for that group.</p>
     *
     * @param id of TOPIC
     * @param userName user vhich is goin to change rights
     * @param rights collection of groups for which we will set up AMEND right
     */
    public abstract void amendAmendRightsForItem(
            SecuritySession securitySession, Integer id,
            Collection rights) throws SystemException;

    /**
     * <p>This method changing AMEND rights of TOPIC. It's working only with those groups which can be see by user.
     * if I am adding AMEND right for group and there is not VIEW right -> so create VIEW right for that group.</p>
     *
     * @param id of TOPIC
     * @param userName user which is chaning
     * @param rights collection of groups for which we will set up AMEND right
     */
    public abstract void amendAmendRightsForTopic(
            SecuritySession securitySession, Integer id,
            Collection rights) throws SystemException;

    /**
     * <p>This method changing REMOVE rights. Users in those groups will be able to remove ITEMS with this TOPIC.
     * It's working only with those groups which can be see by user.
     * if I am adding REMOVE right for group and there is not VIEW right -> so create VIEW right for that group.</p>
     *
     * @param id of TOPIC
     * @param userName user vhich is goin to change rights
     * @param rights collection of groups for which we will set up REMOVE right
     */
    public abstract void amendRemoveRightsForItem(
            SecuritySession securitySession, Integer id,
            Collection rights) throws SystemException;

    /**
     * <p>This method changing REMOVE rights of TOPIC. It's working only with those groups which can be see by user.
     * if I am adding REMOVE right for group and there is not VIEW right -> so create VIEW right for that group.</p>
     *
     * @param id of TOPIC
     * @param userName user vhich is going to change
     * @param rights collection of groups for which we will set up REMOVE right
     */
    public abstract void amendRemoveRightsForTopic(
            SecuritySession securitySession, Integer id,
            Collection rights) throws SystemException;

    /**
     * <p>This method changing VIEW rights of ITEMS. Users in those groups will be albe to see ITEMS with this TOPIC.
     *  It's working only with those groups which can be see by user.</p>
     *
     * @param id of TOPIC
     * @param userName user vhich is goin to change rights
     * @param rights collection of groups for which we will set up VIEW right
     */
    public abstract void amendViewRightsForItem(
            SecuritySession securitySession, Integer id,
            Collection rights) throws SystemException;

    /**
     * <p>This method changing VIEW rights of TOPIC. It's working only with those groups which can be see by user.</p>
     *
     * @param id of TOPIC
     * @param userName user which is changing
     * @param rights collection of groupIds for which we will set up VIEW right
     */
    public abstract void amendViewRightsForTopic(
            SecuritySession securitySession, Integer id,
            Collection rights) throws SystemException;

    /**
     * <p>Find out if a user is allowed to add a new comment.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param comment the comment check.
     * @return <code>true</code> if the user is entitled to add this comment,
     *   otherwise <code>false</code>.
     */
    public abstract boolean canAddComment(SecuritySession securitySession,
            CommentDO comment) throws SystemException;

    /**
     * <p>Find out if a user is allowed to add a new topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to add new topics,
     *     otherwise <code>false</code>.
     */
    public abstract boolean canAddTopic(SecuritySession securitySession)
            throws SystemException;

    /**
     * <p>Find out if a user is allowed to add items to a given topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to add items to the
     *      topic, otherwise <code>false</code>.
     */
    public abstract boolean canAddToTopic(SecuritySession securitySession,
            Integer topicId) throws SystemException;

    /**
     * <p>Find out if a user is allowed to change an existing comment.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param comment the comment check.
     * @return <code>true</code> if the user is entitled to change this comment,
     *   otherwise <code>false</code>.
     */
    public abstract boolean canAmendComment(SecuritySession securitySession,
            CommentDO comment) throws SystemException;

    /**
     * <p>Find out if a user is allowed to amend items in a given topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param integerParam the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to amend items in the
     *      topic, otherwise <code>false</code>.
     */
    public abstract boolean canAmendInTopic(SecuritySession securitySession,
            Integer integerParam) throws SystemException;

    /**
     * <p>Find out if a user is allowed to amend an existing new topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to amend the
     *      topic, otherwise <code>false</code>.
     */
    public abstract boolean canAmendTopic(SecuritySession securitySession,
            Integer id) throws SystemException;

    /**
     * <p>Find out if a user is allowed to remove an existing comment.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param comment the comment check.
     * @return <code>true</code> if the user is entitled to remove this comment,
     *   otherwise <code>false</code>.
     */
    public abstract boolean canRemoveComment(SecuritySession securitySession,
            CommentDO comment) throws SystemException;

    /**
     * <p>Find out if a user is allowed to remove items from a given topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to remove items from
     *      the topic, otherwise <code>false</code>.
     */
    public abstract boolean canRemoveFromTopic(SecuritySession securitySession,
            Integer topicId) throws SystemException;

    /**
     * <p>Find out if a user is allowed to remove a topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to remove the
     *      topic, otherwise <code>false</code>.
     */
    public abstract boolean canRemoveTopic(SecuritySession securitySession,
            Integer id) throws SystemException;

    /**
     * <p>Internal helper method. Find out if a user is allowed to access
     * entries in a given group.</p>
     *
     * @param securitySession Security session to check the rights for.
     * @param topicId Unique identifier of the topic to check.
     * @param access The access level as defined in {@link
     *      com.ivata.groupware.business.addressbook.person.group.right.RightConstants
     *      RightConstants}.
     * @return <code>true</code> if the user is entitled to access entries in the
     *      topic, otherwise <code>false</code>.
     */
    public abstract boolean canUser(SecuritySession securitySession,
            Integer topicId, Integer detail, Integer access)
            throws SystemException;

    /**
     * <p>Find out if a user is allowed to view items to a given topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to view items in the
     *      topic, otherwise <code>false</code>.
     */
    public abstract boolean canViewInTopic(SecuritySession securitySession,
            Integer topicId) throws SystemException;

    /**
     * <p>Find groups which have <code>access</code> to items with topic.
     * Return only those groups which can be see by that user.</p>
     *
     * @param id of TOPIC
     * @param userName user which is trying to find rights
     * @param access find rights with this access
     * @return Collection of IDS of groups which have <code>access</code> to that items wuth that topic
     */
    public abstract Collection findRightsForItemsInTopic(
            SecuritySession securitySession, Integer id, Integer access)
            throws SystemException;

    /**
     * <p>Find groups which have <code>access</code> to topic.
     * Return only those groups which can be see by that user.</p>
     *
     * @param id of TOPIC
     * @param userName user which is trying to find rights
     * @param access find rights with this access
     * @return Collection of IDS of groups which have <code>access</code> to that topic
     */
    public abstract Collection findRightsForTopic(
            SecuritySession securitySession, Integer id, Integer access)
            throws SystemException;

    /**
     * <p>Find the unique identifiers of all library topics for which the items
     * can be accessed by the group specified, with the access level given.</p>
     *
     * @param groupId unique identifier of the group for which to search for
     *    library topics.
     * @param access the access level as defined in {@link
     *      com.ivata.groupware.business.addressbook.person.group.right.RightConstants
     *      RightConstants}.
     * @return a <code>Collection</code> of <code>Integer</code> instances,
     *      matching all topics which can be access with this level of access
     *      by the group specified.
     */
    public abstract Collection findTopicsByGroupAccess(
            SecuritySession securitySession,
            Integer groupId,
            Integer access) throws SystemException;
}