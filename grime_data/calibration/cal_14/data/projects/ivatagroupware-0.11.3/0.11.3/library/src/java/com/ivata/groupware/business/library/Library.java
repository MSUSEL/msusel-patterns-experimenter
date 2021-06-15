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
package com.ivata.groupware.business.library;

import java.util.List;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.library.comment.CommentDO;
import com.ivata.groupware.business.library.item.LibraryItemDO;
import com.ivata.groupware.business.library.topic.TopicDO;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationErrors;

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

public interface Library {
    final static String BUNDLE_PATH = "library";

    /**
     * <p>Add a new comment to the system. The user supplied is checked, to make
     * sure she has the necessary permission to add the comment, then the comment
     * is added and the new primary key value (id) is returned.</p>
     *
     * @throws BusinessException if the user
     *     provided is not entitled to add this topic.
     * @throws BusinessException
     *     if either the <code>subject</code>, <code>subject</code>  or
     *     <code>item id</code> fields are <code>null</code>.
     * @param securitySession mail session used to post notifications about the new
     *     comment.
     * @param comment data object containing all values of the new
     *     object to add.
     * @param replyToId the unique identifier of the comment this is a reply to,
     *     or <code>null</code> if this is the first comment in a thread.
     * @return new value of the comment as it is now in the system.
     */
    public CommentDO addComment(final SecuritySession securitySession,
            final CommentDO comment) throws SystemException;

    /**
     * <p>Add a new library item to the system. The <code>created</code> and
     * <code>modified</code> timestamps are set automatically by this method
     * (any values in <code>item</code> are ignored). The <code>user</code>,
     * <code>createdBy<code> and <code>modifiedBy<code> fields are set to the
     * user name you provide.</p>
     *
     * <p>For the topic, the <code>topicId</code> field is taken from the
     * data object: settings for the caption and image are
     * ignored.</p>
     *
     * @param userName the name of the user who wants to add the item. This is
     *     used to check user rights.
     * @param item a data object containing all the details
     *     of the item to add.
     * @param securitySession mail session used to post notifications about the new
     *     library item.
     * @param comment the comment used in revision control
     * @exception com.ivata.groupware.ejb.entity.InvalidFieldValueException
     *     if a field has an incorrect value.
     * @exception com.ivata.groupware.ejb.entity.MandatoryFieldException
     *     if a mandatory field has no value.
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to add this item.
     * @return the new item data object, with the details as they
     *     now are in the adressbook.
     */
    public LibraryItemDO addItem(final SecuritySession securitySession,
            final LibraryItemDO item,
            final String comment) throws SystemException;

    /**
     * <p>Add a new topic to the system. The user supplied is checked, to make
     * sure she has the necessary permission to add the topic, then the topic
     * is added and the new primary key value (id) is returned.</p>
     *
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to add this topic.
     * @exception com.ivata.groupware.ejb.entity.InvalidFieldValueException
     *     if either the <code>caption</code> or <code>image</code> fields are
     *     <code>null</code>.
     * @param userName the user who is trying to add the topic (to check the
     *     user right permissions).
     * @param topic details of new TOPIC.
     *
     * @return TopicDO which was added.
     */
    public TopicDO addTopic(final SecuritySession securitySession,
            final TopicDO topic) throws SystemException;

    /**
     * <p>Amend an existing comment in the system. The user supplied is
     * checked, to make sure she has the necessary permission to amend the
     * comment, then the comment is changed and the current comment values
     * are returned, as they now are in the database.</p>
     *
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to add this topic.
     * @exception com.ivata.groupware.ejb.entity.MandatoryFieldException
     *     if the <code>id</code> field is <code>null</code>.
     * @param userName the user who is trying to add the comment (to check the
     *     user right permissions).
     * @param comment data object containing all values of the
     *     object to amend.
     * @param securitySession mail session used to post notifications about the amended
     *     comment.
     * @return new value of the comment as it is now in the system.
     */
    public CommentDO amendComment(final SecuritySession securitySession,
            final CommentDO comment) throws SystemException;

    /**
     * <p>Amend an existing library item in the system. The
     * <code>modified</code> timestamp is set automatically by this method
     * (any values in <code>item</code> are ignored). Values for the
     * <code>created</code> timestamp are not changed.</p>
     *
     * <p>The <code>modifiedBy<code> field is set to the user name you
     * provide.</p>
     *
     * <p>For the topic, the <code>topicId</code> field is taken from the
     * data object: settings for the caption and image are
     * ignored.</p>
     *
     * @param userName the name of the user who wants to amend the item. This is
     *     used to check user rights.
     * @param item a data object containing all the details
     *     of the item to amend.
     * @param securitySession mail server used to post notifications about the amended
     *     library item.
     * @param comment the comment used in revision control
     * @exception com.ivata.groupware.ejb.entity.MandatoryFieldException
     *     if a mandatory field has no value.
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to add this item.
     * @return the new item data object, with the details as they
     *     now are in the adressbook.
     */
    public LibraryItemDO amendItem(final SecuritySession securitySession,
            final LibraryItemDO item,
            final String comment) throws SystemException;

    /**
     * <p>Amend an existing topic in the system. The user supplied is checked,
     * to make sure she has the necessary permission to amend the topic, then
     * the topic is changed.</p>
     *
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to amend this topic.
     * @exception com.ivata.groupware.ejb.entity.InvalidFieldValueException
     *     if the id provided is <code>null</code>.
     * @param userName the user who is trying to add the topic (to check the
     *     user right permissions).
     * @param topic details of TOPIC.
     *
     * @return TopicDO which was amend
     */
    public TopicDO amendTopic(final SecuritySession securitySession,
            final TopicDO topic) throws SystemException;
    /**
     * <p>
     * Find out how many comments exists in total for a library item.
     * </p>
     *
     * @param integerParam unique identifier for the item to count comments for.
     * @return Total number of comments for the library item specified.
     */
    public int countCommentsForItem(final SecuritySession securitySession,
            Integer integerParam)
            throws SystemException;

    /**
     * <p>Locate a list of top-level comments in a specificy item. Note:
     * this does not include comments which are replies - only top-level
     * comments</p>
     *
     * @param integerParam the unique identifier of comment item to locate.
     * @return data object containing all values of the
     *     object as they are now in the system.
     */
    public List findCommentByItem(final
            SecuritySession securitySession,
            Integer integerParam)
            throws SystemException;

    /**
     * <p>Locate a list of comments in the system by their parents.</p>
     *
     * @param integerParam the unique identifier of parent comment to locate.
     * @return data object containing all values of the
     *     object as they are now in the system.
     */
    public List findCommentByParent(final
            SecuritySession securitySession,
            Integer integerParam)
            throws SystemException;

    /**
     * <p>Locate a comment in the system by its unique identifier, and return
     * all the comment values.</p>
     *
     * @exception com.ivata.groupware.ejb.entity.MandatoryFieldException
     *     if the <code>id</code> field is <code>null</code>.
     * @param id the unique identifier of the comment to locate.
     * @return data object containing all values of the
     *     object as they are now in the system.
     */
    public CommentDO findCommentByPrimaryKey(final
            SecuritySession securitySession,
            final Integer id)
            throws SystemException;

    /**
     * <p>Find an item in the library by its unique identifier.</p>
     *
     * @param id the unique identifier of the library item to find.
     * @return the library item data object which matches this id,
     *      with the details as they now are in the library.
     */
    public LibraryItemDO findItemByPrimaryKey(final
            SecuritySession securitySession,
            final Integer id)
            throws SystemException;

    /**
     * <p>This merhod searches for an item by revision.</p>
     *
     * @param rev revision number
     * @param userName user who is doing this
     * @return
     */
    public LibraryItemDO findItemByRevision(final
            SecuritySession securitySession,
            final String itemId,
            final String rev)
            throws SystemException;

    /**
     * <p>Find items sorted by modification date/time, to the count provided in
     * the parameter. If there are less items in the library than
     * <code>count</code>, then all library items are returned.</p>
     *
     * <p>The items are filtered using a view, so that only those items with the
     * <code>access</code> level indicated are shown for the user requested.</p>
     *
     * @param count the total number of items to return. Specify
     *   <code>null</code> to return all items.
     * @param userName the user to display results for. The results filtered so
     *   that only those items are return which the user is entitled to see.
     * @param access the access level which the user should see. This is defined
     *   as one of the constants in {@link
     *   com.ivata.groupware.business.addressbook.person.user.group.right.RightConstants
     *   RightConstants}.
     * @param topicId unique identifier of the topic to filter items for, or all
     *   topics, if <code>null</code>.
     * @return a <code>Collection</code> of all of the item as {@link
     *   LibraryItemDO item dependent value}
     *   instances.
     */
    public List findRecentItems(final SecuritySession securitySession,
            final Integer count,
            final Integer access,
            final Integer topicId)
            throws SystemException;

    /**
     * <p>when we need to find library item by meeting ID</p>
     *
     * @return libraryItemDO
     */
    public TopicDO findTopicByPrimaryKey(
            final
            SecuritySession securitySession,
            final Integer id)
            throws SystemException;

    /**
     * <p>Find all topics with the given access rights for the user name
     * provided.</p>
     *
     * @param userName the user to display results for. The results filtered so
     *   that only those topics are return which the user is entitled to access.
     * @param access the access level which the user should see. This is defined
     *   as one of the constants in {@link
     *   com.ivata.groupware.business.addressbook.person.user.group.right.RightConstants
     *   RightConstants}.
     * @param detail, at this moment we are trying to find Topics for :
     *  - add, amend, delete and view LIBRARY ITEMS
     *  - amend, delete and view TOPICS
     * @return a <code>List</code> containing two <code>Map</code>s. The map
     *   in element <code>0</code> contains a <code>Map</code> of all of the
     *   topic captions. The map in element <code>1</code> contains a
     *   <code>Map</code> of all of the topic images. In each <code>Map</code>,
     *   the key is topic id.
     */
    public List findTopics(final SecuritySession securitySession) throws SystemException;

    /**
     * <p>Find all comments to which there has been no reply.</p>
     * @param userName
     * @param count
     * @return
     */
    public List findUnacknowledgedComments(final
            SecuritySession securitySession,
            final Integer count)
            throws SystemException;

    /**
     * <p>Remove an existing comment from the system. The user supplied is
     * checked, to make sure she has the necessary permission to remove the
     * comment, then the comment is removed.</p>
     *
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to add this topic.
     * @exception com.ivata.groupware.ejb.entity.MandatoryFieldException
     *     if the <code>id</code> field is <code>null</code>.
     * @param userName the user who is trying to add the comment (to check the
     *     user right permissions).
     * @param comment data object containing all values of the
     *     object to amend. Only the <code>id</code> field is read.
     */
    public void removeComment(final SecuritySession securitySession,
            final CommentDO comment) throws SystemException;

    /**
     * <p>Remove a library item to the system. The item with the id specified in
     * <code>item</code> is removed.</p>
     *
     * @param userName the name of the user who wants to add the item. This is
     *     used to check user rights.
     * @param item a data object containing all the details
     *     of the item to remove. Only the <code>id</code> from this object is
     *     actually used.
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to add this item.
     */
    public void removeItem(final SecuritySession securitySession,
            final LibraryItemDO item) throws SystemException;

    /**
     * <p>Remove a topic from the system. The user supplied is checked,
     * to make sure she has the necessary permission to remove the topic, then
     * the topic is removed.</p>
     *
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to remove this topic.
     * @exception com.ivata.groupware.ejb.entity.InvalidFieldValueException
     *     if the id provided is <code>null</code> or doesn't exist.
     * @param userName the user who is trying to add the topic (to check the
     *     user right permissions).
     * @param topic topic which we are going to remove.
     */
    public void removeTopic(final SecuritySession securitySession,
            final TopicDO topic) throws SystemException;

    /**
     * <p>This methos is reverting item to older version, It's mean that it will check OUT from CVS and call setDO with it.</p>
     * @param itemId
     * @param revision
     * @param userName
     * @return
     */
    public LibraryItemDO revertItemToRevision(final
            SecuritySession securitySession,
            final String itemId,
            final String revision,
            final String comment) throws SystemException;

    /**
     * <p>
     * Sanitize all library contents.
     * </p>
     *
     * @param item
     * @param persistenceSession
     */
    public void sanitize(SecuritySession securitySession)
        throws SystemException;

    /**
     * <p>
     * Re-index all library contents (items and comments) in the search engine.
     * This is useful after an upgrade, if the search functionality has changed.
     * </p>
     */
    public void updateSearchIndex (SecuritySession securitySession)
            throws SystemException ;

    /**
     * <p>Confirm all of the elements of the comment are valid before it
     * is submitted.</p>
     *
     * @param comment data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final CommentDO comment);

    /**
     * <p>Confirm all of the elements of the item are present and valid,
     * before the item is submitted.</p>
     *
     * @param item data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final LibraryItemDO item);

    /**
     * <p>Confirm all of the elements of the topic are present and valid,
     * before the topic is submitted.</p>
     *
     * @param topic data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final TopicDO topic);
}