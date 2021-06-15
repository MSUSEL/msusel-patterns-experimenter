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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.mail.MethodNotSupportedException;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.business.addressbook.person.group.right.RightConstants;
import com.ivata.groupware.business.addressbook.person.group.right.RightDO;
import com.ivata.groupware.business.library.comment.CommentDO;
import com.ivata.groupware.business.library.topic.TopicDO;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.SystemException;


/**
 * <p>
 * Facade to the intranet library access rights. This POJO can be
 * used both locally and remotely to establish what users are entitled to
 * within the library subsystem.
 * </p>
 *
 * @since 2002-07-10
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class LibraryRightsImpl implements LibraryRights, Serializable {
    /**
     * Persistence manger used to store/retrieve data objects.
     */
    private QueryPersistenceManager persistenceManager;

    /**
     * Construct a new library rights instance.
     *
     * @param persistenceManager used to store objects in db.
     */
    public LibraryRightsImpl(QueryPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

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
    public void amendAddRightsForItem(final SecuritySession securitySession,
            final Integer id,
            final Collection rights)
            throws SystemException {
        throw new SystemException(new MethodNotSupportedException("ERROR: LibraryRightsImpl.amendAddRightsForItem is not implemented yet!"));
    }

    /**
     * <p>This method changing AMEND rights. Users in those groups will be able to amend ITEMS with this TOPIC.
     * It's working only with those groups which can be see by user.
     * if I am adding AMEND right for group and there is not VIEW right -> so create VIEW right for that group.</p>
     *
     * @param id of TOPIC
     * @param userName user vhich is goin to change rights
     * @param rights collection of groups for which we will set up AMEND right
     */
    public void amendAmendRightsForItem(final SecuritySession securitySession,
            final Integer id,
            final Collection rights)
            throws SystemException {
        throw new SystemException(new MethodNotSupportedException("ERROR: LibraryRightsImpl.amendAmendRightsForItem is not implemented yet!"));
    }

    /**
     * <p>This method changing AMEND rights of TOPIC. It's working only with those groups which can be see by user.
     * if I am adding AMEND right for group and there is not VIEW right -> so create VIEW right for that group.</p>
     *
     * @param id of TOPIC
     * @param userName user which is chaning
     * @param rights collection of groups for which we will set up AMEND right
     */
    public void amendAmendRightsForTopic(final SecuritySession securitySession,
            final Integer id,
            final Collection rights)
            throws SystemException {
        throw new SystemException(new MethodNotSupportedException("ERROR: LibraryRightsImpl.amendAmendRightsForTopic is not implemented yet!"));
    }

    /**
     * <p>This method changing REMOVE rights. Users in those groups will be able to remove ITEMS with this TOPIC.
     * It's working only with those groups which can be see by user.
     * if I am adding REMOVE right for group and there is not VIEW right -> so create VIEW right for that group.</p>
     *
     * @param id of TOPIC
     * @param userName user vhich is goin to change rights
     * @param rights collection of groups for which we will set up REMOVE right
     */
    public void amendRemoveRightsForItem(final SecuritySession securitySession,
            final Integer id,
            final Collection rights)
            throws SystemException {
        throw new SystemException(new MethodNotSupportedException("ERROR: LibraryRightsImpl.amendRemoveRightsForItem is not implemented yet!"));
    }

    /**
     * <p>This method changing REMOVE rights of TOPIC. It's working only with those groups which can be see by user.
     * if I am adding REMOVE right for group and there is not VIEW right -> so create VIEW right for that group.</p>
     *
     * @param id of TOPIC
     * @param userName user vhich is going to change
     * @param rights collection of groups for which we will set up REMOVE right
     */
    public void amendRemoveRightsForTopic(final SecuritySession securitySession,
            final Integer id,
            final Collection rights)
            throws SystemException {
        throw new SystemException(new MethodNotSupportedException("ERROR: LibraryRightsImpl.amendRemoveRightsForTopic is not implemented yet!"));
    }

    /**
     * <p>This method changing VIEW rights of ITEMS. Users in those groups will be albe to see ITEMS with this TOPIC.
     *  It's working only with those groups which can be see by user.</p>
     *
     * @param id of TOPIC
     * @param userName user vhich is goin to change rights
     * @param rights collection of groups for which we will set up VIEW right
     */
    public void amendViewRightsForItem(final SecuritySession securitySession,
            final Integer id,
            final Collection rights)
            throws SystemException {
        throw new SystemException(new MethodNotSupportedException("ERROR: LibraryRightsImpl.amendViewRightsForItem is not implemented yet!"));
    }

    /**
     * <p>This method changing VIEW rights of TOPIC. It's working only with those groups which can be see by user.</p>
     *
     * @param id of TOPIC
     * @param userName user which is changing
     * @param rights collection of groupIds for which we will set up VIEW right
     */
    public void amendViewRightsForTopic(final SecuritySession securitySession,
            final Integer id,
            final Collection rights)
            throws SystemException {
        throw new SystemException(new MethodNotSupportedException("ERROR: LibraryRightsImpl.amendViewRightsForTopic is not implemented yet!"));
    }

    /**
     * <p>Find out if a user is allowed to add a new comment.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param comment the comment check.
     * @return <code>true</code> if the user is entitled to add this comment,
     *   otherwise <code>false</code>.
     */
    public boolean canAddComment(final SecuritySession securitySession,
            final CommentDO comment)
            throws SystemException {
        // user rights for adding comments are simple: if the user can view the
        // item this comment refers to, she can also add comments to it
        return canViewInTopic(securitySession, comment.getItem().getTopic().getId());
    }

    /**
     * <p>Find out if a user is allowed to add a new topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to add new topics,
     *     otherwise <code>false</code>.
     */
    public boolean canAddTopic(final SecuritySession securitySession)
            throws SystemException {
        return canUser(securitySession, (Integer)null,
                RightConstants.DETAIL_LIBRARY_TOPIC,
                RightConstants.ACCESS_ADD);
    }

    /**
     * <p>Find out if a user is allowed to add items to a given topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to add items to the
     *      topic, otherwise <code>false</code>.
     */
    public boolean canAddToTopic(final SecuritySession securitySession,
            final Integer topicId)
            throws SystemException {
        return canUser(securitySession, topicId, RightConstants.DETAIL_LIBRARY_ITEM_TOPIC,
                RightConstants.ACCESS_ADD);
    }

    /**
     * <p>Find out if a user is allowed to change an existing comment.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param commentParam the comment check.
     * @return <code>true</code> if the user is entitled to change this comment,
     *   otherwise <code>false</code>.
     */
    public boolean canAmendComment(final SecuritySession securitySession,
            final CommentDO commentParam)
            throws SystemException {
        // prerequisite: nobody can amend a comment with a null id
        if (commentParam.getId() == null) {
            return false;
        }
        // in order to amend a comment, the user must have sufficient rights
        // at topic level, or be the original author of the topic
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        CommentDO comment;
        try {
            // re-get the comment from the data store
            comment = (CommentDO)
                persistenceManager.findByPrimaryKey(persistenceSession,
                    CommentDO.class, commentParam.getId());
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
        // so is this the original author?
        if (comment.getCreatedBy().equals(securitySession.getUser())) {
            return true;
        }
        // if that didn't work, check the rights at topic level
        return canUser(securitySession, comment.getItem().getTopic().getId(),
                RightConstants.DETAIL_LIBRARY_COMMENT_TOPIC,
                RightConstants.ACCESS_AMEND);
    }

    /**
     * <p>Find out if a user is allowed to amend items in a given topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to amend items in the
     *      topic, otherwise <code>false</code>.
     */
    public boolean canAmendInTopic(final SecuritySession securitySession,
            final Integer topicId)
            throws SystemException {
        return canUser(securitySession, topicId, RightConstants.DETAIL_LIBRARY_ITEM_TOPIC,
                RightConstants.ACCESS_AMEND);
    }

    /**
     * <p>Find out if a user is allowed to amend an existing new topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to amend the
     *      topic, otherwise <code>false</code>.
     */
    public boolean canAmendTopic(final SecuritySession securitySession,
            final Integer id)
            throws SystemException {
        return canUser(securitySession, id, RightConstants.DETAIL_LIBRARY_TOPIC,
                RightConstants.ACCESS_AMEND);
    }

    /**
     * <p>Find out if a user is allowed to remove an existing comment.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param commentParam the comment check.
     * @return <code>true</code> if the user is entitled to remove this comment,
     *   otherwise <code>false</code>.
     */
    public boolean canRemoveComment(final SecuritySession securitySession,
            final CommentDO commentParam)
            throws SystemException {
        // prerequisite: nobody can remove a comment with a null id
        if (commentParam.getId() == null) {
            return false;
        }
        // in order to remove a comment, the user must have sufficient rights
        // at topic level, or be the original author of the topic
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        CommentDO comment;
        try {
            // re-get the comment from the data store
            comment = (CommentDO)
                persistenceManager.findByPrimaryKey(persistenceSession,
                    CommentDO.class, commentParam.getId());
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
        // so is this the original author?
        if (comment.getCreatedBy().equals(securitySession.getUser())) {
            return true;
        }
        // if that didn't work, check the rights at topic level
        return canUser(securitySession, comment.getItem().getTopic().getId(),
                RightConstants.DETAIL_LIBRARY_COMMENT_TOPIC,
                RightConstants.ACCESS_REMOVE);
    }

    /**
     * <p>Find out if a user is allowed to remove items from a given topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to remove items from
     *      the topic, otherwise <code>false</code>.
     */
    public boolean canRemoveFromTopic(final SecuritySession securitySession,
            final Integer topicId)
            throws SystemException {
        return canUser(securitySession, topicId, RightConstants.DETAIL_LIBRARY_ITEM_TOPIC,
                RightConstants.ACCESS_REMOVE);
    }


    /**
     * <p>Find out if a user is allowed to remove a topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param topicId the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to remove the
     *      topic, otherwise <code>false</code>.
     */
    public boolean canRemoveTopic(final SecuritySession securitySession,
            final Integer id)
            throws SystemException {
        return canUser(securitySession, id, RightConstants.DETAIL_LIBRARY_TOPIC,
                RightConstants.ACCESS_REMOVE);
    }
    /**
     * <p>Internal helper method. Find out if a user is allowed to access
     * entries in a given group.</p>
     *
     * @param securitySession Security session to check the rights for.
     * @param integerParam Unique identifier of the topic to check.
     * @param access The access level as defined in {@link
     *      com.ivata.groupware.business.addressbook.person.group.right.RightConstants
     *      RightConstants}.
     * @return <code>true</code> if the user is entitled to access entries in the
     *      topic, otherwise <code>false</code>.
     */
    public boolean canUser(final SecuritySession securitySession,
            Integer integerParam,
            final Integer detail,
            final Integer access)
            throws SystemException {
/* TODO        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);

        // see if we're allowed to
        try {
            Collection tmp = persistenceManager.find(persistenceSession,
                "rightByUserNameAccessDetailTargetId",
                new Object [] {
                    securitySession.getUser().getName(),
                    access,
                    detail,
                    topicId
                });
            if (tmp.size() == 0) {
                return false;
            }
        } catch (FinderException e) {
            // oops
            return false;
        }
        // only return true if we get this far :- )
*/

        return true;
    }

    /**
     * <p>Find out if a user is allowed to view items to a given topic.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param integerParam the unique identifier of the topic to check.
     * @return <code>true</code> if the user is entitled to view items in the
     *      topic, otherwise <code>false</code>.
     */
    public boolean canViewInTopic(final SecuritySession securitySession,
            Integer topicId)
            throws SystemException {
        return canUser(securitySession, topicId,
                RightConstants.DETAIL_LIBRARY_ITEM_TOPIC,
                RightConstants.ACCESS_VIEW);
    }
    /**
     * <p>Find groups which have <code>access</code> to items with topic.
     * Return only those groups which can be see by that user.</p>
     *
     * @param id of TOPIC
     * @param userName user which is trying to find rights
     * @param access find rights with this access
     * @return Collection of IDS of groups which have <code>access</code> to that items wuth that topic
     */
    public Collection findRightsForItemsInTopic(final SecuritySession securitySession,
            final Integer id,
            final Integer access)
            throws SystemException {
        Vector rights = new Vector();
        String userName = securitySession.getUser().getName();

        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // I will send only groupIds which user can view, no more.
            List groupIdsCanViewByUser =
                persistenceManager.find(persistenceSession,
                        "rightTargetIdByUserNameAccessDetail",
                        new Object [] {
                            userName, RightConstants.ACCESS_VIEW,
                            RightConstants.DETAIL_PERSON_GROUP_MEMBER
                        });
            // find rights for TOPIC
            List tmp =
                persistenceManager.find(persistenceSession,
                        "rightByAccessDetailTargetId",
                        new Object [] {
                            access, RightConstants.DETAIL_LIBRARY_ITEM_TOPIC, id
                        });

            for (int i = 0; i < tmp.size(); i++) {
                RightDO right = (RightDO) tmp.get(i);
                Integer groupId = right.getGroup().getId();

                if (groupIdsCanViewByUser.contains(groupId)) {
                    rights.add(groupId);
                }
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
        return rights;
    }

    /**
     * <p>Find groups which have <code>access</code> to topic.
     * Return only those groups which can be see by that user.</p>
     *
     * @param id of TOPIC
     * @param userName user which is trying to find rights
     * @param access find rights with this access
     * @return Collection of IDS of groups which have <code>access</code> to that topic
     */
    public Collection findRightsForTopic(final SecuritySession securitySession,
            final Integer id,
            final Integer access)
            throws SystemException {
        Vector rights = new Vector();
        String userName = securitySession.getUser().getName();

        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // I will send only groupIds which user can view, no more.
            List groupIdsCanViewByUser =
                persistenceManager.find(persistenceSession,
                        "rightTargetIdByUserNameAccessDetail",
                        new Object [] {
                            userName, RightConstants.ACCESS_VIEW,
                            RightConstants.DETAIL_LIBRARY_TOPIC
                        });
            // find rights for TOPIC
            List tmp =
                persistenceManager.find(persistenceSession,
                        "rightByAccessDetailTargetId",
                        new Object [] {
                            access, RightConstants.DETAIL_LIBRARY_ITEM_TOPIC, id
                        });

            for (int i = 0; i < tmp.size(); i++) {
                RightDO right = (RightDO) tmp.get(i);
                Integer groupId = right.getGroup().getId();

                if (groupIdsCanViewByUser.contains(groupId)) {
                    rights.add(groupId);
                }
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
        return rights;
    }

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
    public Collection findTopicsByGroupAccess(final SecuritySession
            securitySession,
            final Integer groupId,
            final Integer access)
            throws SystemException {
        Vector topics = new Vector();

        PersistenceSession persistenceSession =
                persistenceManager.openSession(securitySession);
        try {
            // first find the group
            GroupDO group = (GroupDO)
                persistenceManager.findByPrimaryKey(persistenceSession,
                        GroupDO.class, groupId);

            // find all the ids
            List topicIds =
                persistenceManager.find(persistenceSession,
                        "rightTargetIdByGroupIdAccessDetail",
                        new Object [] {
                            group, access, RightConstants.DETAIL_LIBRARY_ITEM_TOPIC
                        });
            for (Iterator i = topicIds.iterator(); i.hasNext(); ) {
                String topicId = (String) i.next();
                TopicDO topic = (TopicDO)
                    persistenceManager.findByPrimaryKey(persistenceSession,
                            TopicDO.class, topicId);
                topics.add(topic);
            }

        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }

        return topics;
    }
}
