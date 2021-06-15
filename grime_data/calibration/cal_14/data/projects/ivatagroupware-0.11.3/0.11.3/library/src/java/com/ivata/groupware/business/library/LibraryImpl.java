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

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.mail.MethodNotSupportedException;

import org.apache.log4j.Logger;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.business.BusinessLogic;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.library.comment.CommentDO;
import com.ivata.groupware.business.library.faq.FAQDO;
import com.ivata.groupware.business.library.faq.category.FAQCategoryDO;
import com.ivata.groupware.business.library.item.LibraryItemConstants;
import com.ivata.groupware.business.library.item.LibraryItemDO;
import com.ivata.groupware.business.library.page.PageDO;
import com.ivata.groupware.business.library.topic.TopicDO;
import com.ivata.groupware.business.mail.Mail;
import com.ivata.groupware.business.mail.session.MailSession;
import com.ivata.groupware.business.search.SearchEngine;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.groupware.container.persistence.TimestampDOHandling;
import com.ivata.groupware.web.format.SanitizerFormat;
import com.ivata.mask.Mask;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.persistence.PersistenceException;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.validation.ValidationException;
import com.ivata.mask.web.format.CharacterEntityFormat;
import com.ivata.mask.web.format.FormatConstants;
import com.ivata.mask.web.format.HTMLFormatter;
import com.ivata.mask.web.format.LineBreakFormat;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since Mar 26, 2004
 * @version $Revision: 1.5 $
 */
public class LibraryImpl extends BusinessLogic implements Library {


    /**
     * <p>This class is used to sort the comments.</p>
     */
    private class CommentComparator implements Comparator {

        /**
         * <p>Compare two objects (in this case, both are instances of Comment.</p>
         */
        public int compare(final Object o1,
            final Object o2) {
            CommentDO comment1 = (CommentDO) o1;
            CommentDO comment2 = (CommentDO) o2;
            long test1 = comment1.getModified().getTime();
            long test2 = comment2.getModified().getTime();

            // note: purposely don't allow an equals case here!! If two are
            //       equal, then only one will appear in the final set
            return (test1 > test2) ? -1 : 1;
        }
    }


    /**
     * <p>This class is used to sort the library item rights.</p>
     */
    private class LibraryItemComparator implements Comparator {

        /**
         * <p>Compare two objects (in this case, both are instances of
         * {@link com.ivata.groupware.business.library.item.right.LibraryItemRightLocal
         * LibraryItemRightLocal}) and return which is the higher priority.</p>
         *
         * @param o1 first instance of {@link
         *     com.ivata.groupware.business.library.item.right.LibraryItemRightLocal
         *     LibraryItemRightLocal} to be compared.
         * @param o2 second instance of {@link
         *     com.ivata.groupware.business.library.item.right.LibraryItemRightLocal
         *     LibraryItemRightLocal} to be compared.
         * @return a negative integer, zero, or a positive integer as the first argument
         *      comes after, same as, or before the second.
         */
        public int compare(final Object o1,
            final Object o2) {
            LibraryItemDO itemRight1 = (LibraryItemDO) o1;
            LibraryItemDO itemRight2 = (LibraryItemDO) o2;
            long test1 = itemRight1.getModified().getTime();
            long test2 = itemRight2.getModified().getTime();

            // note: purposely don't allow an equals case here!! If two are
            //       equal, then only one will appear in the final set
            return (test1 > test2) ? -1 : 1;
        }
    }
    /**
     * <p>
     * <strong>Log4J</strong> logger.
     * </p>
     */
    private static Logger log = Logger.getLogger(Library.class);
    private AddressBook addressBook;
    private HTMLFormatter formatter;
    private Mail mail;
    /**
     * Persistence manger used to store/retrieve data objects.
     */
    private QueryPersistenceManager persistenceManager;
    private SearchEngine searchEngine;
    private Settings settings;
    private MaskFactory maskFactory;
    /**
     * Construct a new address book.
     *
     * @param persistenceManager used to store objects in db.
     */
    public LibraryImpl(QueryPersistenceManager persistenceManager,
            AddressBook addressBook,
            Mail mail,
            Settings settings,
            SearchEngine searchEngine,
            HTMLFormatter formatter,
            MaskFactory maskFactory) {
        this.persistenceManager = persistenceManager;
        this.addressBook = addressBook;
        this.mail = mail;
        this.settings = settings;
        this.searchEngine = searchEngine;
        this.formatter = formatter;
        this.maskFactory = maskFactory;
    }

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
     * @param commentParam data object containing all values of the new
     *     object to add.
     * @param replyToId the unique identifier of the comment this is a reply to,
     *     or <code>null</code> if this is the first comment in a thread.
     * @return new value of the comment as it is now in the system.
     */
    public CommentDO addComment(final SecuritySession securitySession,
            final CommentDO commentParam)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before creating the event, check we have reasonable data
            ValidationErrors errors = validate(securitySession, commentParam);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            TimestampDOHandling.add(securitySession, commentParam);
            CommentDO comment = (CommentDO) persistenceManager.add(
                    persistenceSession,
                    commentParam);

            LibraryItemDO item = comment.getItem();
            searchEngine.updateIndex(securitySession,
                    item.getId(),
                    LibraryItemDO.class.getName(),
                    item.getTopic().getId().toString(),
                    comment.getId(),
                    CommentDO.class.getName(),
                    comment.getText(),
                    comment.getFormat());
            return comment;
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
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
     * @param itemParam a data object containing all the details
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
            final LibraryItemDO itemParam,
            final String comment)
            throws SystemException {
        LibraryItemDO item = itemParam;
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before creating the event, check we have reasonable data
            ValidationErrors errors = validate(securitySession, item);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }

            // set the page order, and set all ids null (to create new pages)
            if (item.getPages() != null) {
                Iterator pagesIterator = item.getPages().iterator();
                int pageNumber = 0;
                while (pagesIterator.hasNext()) {
                    PageDO page = (PageDO) pagesIterator.next();
                    // only new pages allowed!
                    page.setId(null);
                    page.setNumber(new Integer(pageNumber++));
                }
            }

            TimestampDOHandling.add(securitySession, item);
            item = (LibraryItemDO) persistenceManager.add(persistenceSession, item);
            updateSearchIndexForItem(securitySession, item);

/*
            DirectoryDO parentDirectory = (DirectoryDO)
                persistenceManager.findByPrimaryKey(persistenceSession,
                DirectoryDO.class, DirectoryConstants.LIBRARY_DIRECTORY);
            // create the directory for attachments and doc versions
            DirectoryDO attachmentsDirectory = new DirectoryDO();
            attachmentsDirectory.setName(item.getId().toString());
            attachmentsDirectory.setParent(parentDirectory);
// TODO            attachmentsDirectory = drive.addDirectory(securitySession, attachmentsDirectory);

            DirectoryDO versionsDirectory = new DirectoryDO();
            versionsDirectory.setName("versions");
            versionsDirectory.setParent(attachmentsDirectory);
// TODO            versionsDirectory = drive.addDirectory(securitySession, versionsDirectory);


            // commit this ITEM do CVS as XML file
            DriveFileDO driveFile = new DriveFileDO();

            String tmpItemFilePath;
            try {
                tmpItemFilePath = item.saveToFile();
            } catch(IOException e) {
                log.error("Saving to file.", e);
                throw new SystemException(e);
            }
            File tmpFile = new File(tmpItemFilePath);

            driveFile.setDirectory(versionsDirectory);
            driveFile.setName("document.xml");
            driveFile.setMimeType("text/xml");
            driveFile.setSize(new Integer((int)(tmpFile.length())));
            FileRevisionDO fileRevision = new FileRevisionDO();
            fileRevision.setComment(StringHandling.getNotNull(comment, "new revision"));
            driveFile.setHeadRevision(fileRevision);

            // drive.checkAndCreate("library" + File.separator + this.getId() + File.separator + "versions" + File.separator, item.getCreatedBy());
//          TODO            drive.commitFile(securitySession, driveFile, tmpItemFilePath);
            // delete tmp file
            tmpFile.delete();
            tmpFile = null;
*/
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
        notifyForSubmission(securitySession, item, true);
        return item;
    }
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
            final TopicDO topic)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before creating the event, check we have reasonable data
            ValidationErrors errors = validate(securitySession, topic);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            return (TopicDO) persistenceManager.add(persistenceSession, topic);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

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
            final CommentDO comment)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before changing the event, check we have reasonable data
            ValidationErrors errors = validate(securitySession, comment);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            TimestampDOHandling.amend(securitySession, comment);
            persistenceManager.amend(persistenceSession, comment);

            LibraryItemDO item = comment.getItem();
            searchEngine.updateIndex(securitySession, item.getId(),
                    LibraryItemDO.class.getName(),
                    item.getTopic().getId().toString(),
                    comment.getId(),
                    CommentDO.class.getName(),
                    comment.getText(),
                    comment.getFormat());
            return comment;
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
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
            final String comment)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before changing the event, check we have reasonable data
            ValidationErrors errors = validate(securitySession, item);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            // set the page order
            if (item.getPages() != null) {
                Iterator pagesIterator = item.getPages().iterator();
                int pageNumber = 0;
                while (pagesIterator.hasNext()) {
                    PageDO page = (PageDO) pagesIterator.next();
                    page.setNumber(new Integer(pageNumber++));
                }
            }
            TimestampDOHandling.amend(securitySession, item);
            persistenceManager.amend(persistenceSession, item);
            updateSearchIndexForItem(securitySession, item);

            /*TODO
            DirectoryDO attachmentsDirectory = (DirectoryDO)
                    persistenceManager.find(persistenceSession,
                    "driveDirectoryByParentIdName",
                    new Object[] {DirectoryConstants.LIBRARY_DIRECTORY, item.getId().toString()});
            DirectoryDO versionsDirectory = (DirectoryDO)
                    persistenceManager.find(persistenceSession,
                    "driveDirectoryByParentIdName",
                    new Object[] {attachmentsDirectory.getId(), "versions"});

            String tmpItemFilePath;
            try {
                tmpItemFilePath = item.saveToFile();
            } catch (IOException e) {
                throw new SystemException(e);
            }
            File tmpFile = new File(tmpItemFilePath);

            // commit the new version
            DriveFileDO driveFile = new DriveFileDO();

            driveFile.setDirectory(versionsDirectory);
            driveFile.setName("document.xml");
            driveFile.setMimeType("text/xml");
            driveFile.setSize(new Integer((int)tmpFile.length()));
            FileRevisionDO fileRevision = new FileRevisionDO();
            fileRevision.setComment(StringHandling.getNotNull(comment, "new revision"));
            driveFile.setHeadRevision(fileRevision);

            drive.commitFile(securitySession, driveFile, tmpItemFilePath);
            // delete tmp file
            tmpFile.delete();
            tmpFile = null;
            */

            notifyForSubmission(securitySession, item, false);
            return item;
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
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
            final TopicDO topic)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before changing the event, check we have reasonable data
            ValidationErrors errors = validate(securitySession, topic);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            persistenceManager.amend(persistenceSession, topic);
            return topic;
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>
     * Find out how many comments exists in total for a library item.
     * </p>
     *
     * @param itemId unique identifier for the item to count comments for.
     * @return Total number of comments for the library item specified.
     */
    public int countCommentsForItem(final SecuritySession securitySession,
            final Integer itemId)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            Integer count = persistenceManager.findInteger(persistenceSession,
                    "libraryCountCommentByItemId",
                    new Object [] {itemId});
            return (count == null) ? 0 : count.intValue();
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
    /**
     * <p>Locate a list of top-level comments in a specificy item. Note:
     * this does not include comments which are replies - only top-level
     * comments</p>
     *
     * @param itemId the unique identifier of comment item to locate.
     * @return data object containing all values of the
     *     object as they are now in the system.
     */
    public List findCommentByItem(final
            SecuritySession securitySession,
            final Integer itemId)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return persistenceManager.find(persistenceSession,
                    "libraryCommentByItemId",
                    new Object [] {itemId});
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
    /**
     * <p>Locate a list of comments in the system by their parents.</p>
     *
     * @param parentId the unique identifier of parent comment to locate.
     * @return data object containing all values of the
     *     object as they are now in the system.
     */
    public List findCommentByParent(final
            SecuritySession securitySession,
            final Integer parentId)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return persistenceManager.find(persistenceSession,
                    "libraryCommentByParentId",
                    new Object [] {parentId});
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

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
    public CommentDO findCommentByPrimaryKey(final SecuritySession securitySession,
            final Integer id)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return (CommentDO) persistenceManager.findByPrimaryKey(persistenceSession,
                    CommentDO.class, id);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>Find an item in the library by its unique identifier.</p>
     *
     * @param id the unique identifier of the library item to find.
     * @return the library item data object which matches this id,
     *      with the details as they now are in the library.
     */
    public LibraryItemDO findItemByPrimaryKey(final SecuritySession securitySession,
            final Integer id)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return (LibraryItemDO) persistenceManager.findByPrimaryKey(persistenceSession,
                    LibraryItemDO.class, id);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>This merhod searches for an item by revision.</p>
     *
     * @param rev revision number
     * @param userName user who is doing this
     * @return
     */
    public LibraryItemDO findItemByRevision(final SecuritySession securitySession,
            final String itemId,
            final String rev)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // let's find actual version
            LibraryItemDO item = (LibraryItemDO)
                persistenceManager.findByPrimaryKey(persistenceSession,
                    LibraryItemDO.class, itemId);

            /*TODO
            // let's find revision
            DriveLocalHome driveHome = (DriveLocalHome)
                ApplicationServer.findLocalHome("DriveLocal",
                    DriveLocalHome.class);
            DriveLocal drive = driveHome.create();

            DriveFileDO versionsFileDO =
                drive.findFileByPathFileName("/library/" + item.getId().toString() + "/versions", "document.xml", userName);
            FileContentDO fileContent = drive.getFileRevision(versionsFileDO.getId(), rev, userName);

            // convert xml file to ItemDO
            item = itemHome.convertFileToItem(fileContent);
            */
            return item;
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

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
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            if (topicId == null) {
                return Collections.synchronizedList(persistenceManager.find(persistenceSession,
                        "libraryItemRecent", new Object[]{}, count, new Integer(0)));
            } else {
                return Collections.synchronizedList(persistenceManager.find(persistenceSession,
                        "libraryItemRecentByTopic", new Object[]{topicId}, count,
                        new Integer(0)));
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
    /**
     * <p>when we need to find library item by meeting ID</p>
     *
     * @return libraryItemDO
     */
    /*    public LibraryItemDO findItemByMeetingId(String id) {
            LibraryItemDO item = null;

            // check we have an id
            if (id == null) {
                throw new EJBException("Please specify an id to find. Cannot find an item with a null id.");
            }
            try {
                LibraryItemDOHome itemHome = (LibraryItemDOHome)
                    ApplicationServer.findLocalHome("LibraryItemDO",
                        LibraryItemDOHome.class);
                MeetingLocalHome meetingHome = (MeetingLocalHome)
                    ApplicationServer.findLocalHome("MeetingLocal",
                        MeetingLocalHome.class);
                MeetingLocal meeting = meetingHome.findByPrimaryKey(id);
                LibraryItemDO item = itemHome.findByPrimaryKey(meeting.getLibraryItem().getId());

                item = item.getDO();
            } catch (NamingException e) {
                throw new EJBException(e);
            } catch (FinderException e) {
                throw new EJBException(e);
            }
            return item;
        }*/

    /**
     * <p>Find Topic by primary keyand return <code>TopicDO.</code></p>
     *
     * @param id of Topic which we are trying yo find
     * @return TopicDO which is containing all details of finded Topic
     */
    public TopicDO findTopicByPrimaryKey(final SecuritySession securitySession,
            final Integer id)
           throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return (TopicDO) persistenceManager.findByPrimaryKey(persistenceSession,
                    TopicDO.class, id);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

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
    public List findTopics(final SecuritySession securitySession)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return Collections.synchronizedList(persistenceManager.findAll(persistenceSession,
                    TopicDO.class));
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>Find all comments to which there has been no reply.</p>
     * @param userName
     * @param count
     * @return
     */
    public List findUnacknowledgedComments(final SecuritySession securitySession,
            final Integer count)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return Collections.synchronizedList(
                    persistenceManager.find(persistenceSession,
                    "libraryCommentByUserNameUnacknowledged",
                    new Object[] {securitySession.getUser().getName(),
                            Boolean.FALSE}, count, new Integer(0)));
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>Helper method. Notify everyone who can view a certain topic of a new
     * submission. This could be a new library item or a new comment.</p>
     *
     * @param submission instance of either <code>LibraryItemDO</code> or
     *     <code>CommentDO</code>.
     * @param isNew set to <code>true</code> if this is a new submission,
     *     otherwise false.
     * @param userName the user who has entered the new item or comment.
     * @param securitySession mail session used to post notifications.
     * @throws NotificationException if the mail cannot be sent for any reason.
     */
    private void notifyForSubmission(final SecuritySession securitySession,
            final Object submission,
            final boolean isNew)
            throws NotificationException, PersistenceException {
        // prerequisite: ignore any submission which didn't supply a valid mail object
        if (securitySession == null) {
            // just return: in codetutorial.com, for example, we don't want
            // notifications
            return;
        }
        PersistenceSession persistenceSession =
            persistenceManager.openSession(securitySession);
        String senderAddress = null;
        List recipientAddresses = null;
        List to = null;
        List cc = null;
        try {
            UserDO user = securitySession.getUser();

            String URL = settings.getStringSetting(securitySession,
                "pathContext", user);

            Integer topicId;                  // topic of the item
            UserDO originalAuthor = null;  // person who wrote original item/comment
            UserDO author;                 // author of this submission
            senderAddress = "\"ivata groupware library\" <" // email address for replies
                + settings.getStringSetting(securitySession,
                    "emailLibrary",
                    user) + ">";
            String content;                   // content of the message
            String subject;                   // subject of the comment or title of the item

            // let's look at the submission item
            // itself, to find out spcifically who this is addressed to
            if (LibraryItemDO.class.isInstance(submission)) {
                LibraryItemDO item = (LibraryItemDO) submission;
                String settingName = "libraryNotificationContentItem";

                topicId = item.getTopic().getId();
                author = item.getCreatedBy();
                subject = item.getTitle();
                URL += "/display.jsp?id=" + item.getId();
                // if this is a change, rather than a new submission, append "Amend"
                // to the setting name
                if (!isNew) {
                    settingName += "Amend";
                    author = item.getModifiedBy();
                }
                String[] arguments = {
                        author.getName(),
                        item.getTitle(),
                        item.getSummary(),
                        URL};
                String setting = settings.getStringSetting(securitySession,
                    settingName, user);

                if (setting == null) {
                    throw new SystemException("ERROR in Library.notifyForSubmission: "
                            + "the administrator must set the system setting '"
                            + settingName
                            + "'");
                }
                content = MessageFormat.format(setting, arguments);
            } else {
                // check this really _is_ a comment
                if (!CommentDO.class.isInstance(submission)) {
                    throw new SystemException("ERROR in Library: cannot notify on "
                            + "submission of class '"
                            + submission.getClass()
                            + "'");
                }
                CommentDO comment = (CommentDO) submission;

                topicId = comment.getItem().getTopic().getId();
                author = comment.getCreatedBy();
                subject = comment.getSubject();

                // if this is a reply to another comment, the original author is
                // the author of that comment
                String settingName, replyTitle, replySummary, commentText;

                int formatOfOrigComment = -1;
                int formatOfComment = comment.getFormat();

                if (comment.getParent() != null) {
                    originalAuthor = comment.getParent().getCreatedBy();
                    settingName = "libraryNotificationContentComment";
                    replyTitle = comment.getParent().getSubject();
                    replySummary = comment.getParent().getText();
                    formatOfOrigComment = comment.getParent().getFormat();
                    // for case that comment was changed
                    // carefull user could change not his comment
                    if (!isNew) {
                        settingName += "Amend";
                        if (!author.getId().equals(user.getId())) {
                            settingName += "Strange";
                            author = user;
                            originalAuthor = comment.getCreatedBy();
                            replyTitle = comment.getSubject();
                        }
                    }
                } else {
                    // otherwise the original author is the author of the
                    // item
                    originalAuthor = comment.getItem().getCreatedBy();
                    settingName = "libraryNotificationContentItemReply";
                    replyTitle = comment.getItem().getTitle();
                    replySummary = comment.getItem().getSummary();
                    formatOfOrigComment = FormatConstants.FORMAT_HTML;
                    // for case that comment was changed
                    if (!isNew) {
                        settingName += "Amend";
                        if (!author.getId().equals(user.getId())) {
                            author = user;
                            settingName = "libraryNotificationContentCommentAmendStrange";
                            originalAuthor = comment.getCreatedBy();
                            replyTitle = comment.getSubject();
                        }
                    }
                }
                commentText = comment.getText();
                LineBreakFormat lineBreakFormat = new LineBreakFormat();

                lineBreakFormat.setConvertLineBreaks(true);
                formatter.add(new CharacterEntityFormat());
                formatter.add(lineBreakFormat);
                if (formatOfComment == FormatConstants.FORMAT_TEXT) {
                    // if this is a plain text message, do HTML paragraph
                    commentText = formatter.format(commentText);
                }
                if (formatOfOrigComment == FormatConstants.FORMAT_TEXT) {
                    // if this is a plain text message, do HTML paragraph
                    replySummary = formatter.format(replySummary);
                }
                URL += "/display.jsp?id=" + comment.getItem().getId() + "#comments";
                String[] arguments = {
                        author.getName(),
                        originalAuthor.getName(),
                        replyTitle,
                        replySummary,
                        comment.getSubject(),
                        commentText, URL};
                String setting = settings.getStringSetting(securitySession,
                    settingName,
                    user);

                if (setting == null) {
                    throw new SystemException("ERROR in Library.notifyForSubmission: "
                        + "the administrator must set the system setting '"
                        + settingName
                        + "'");
                }
                content = MessageFormat.format(setting, arguments);
            }
            // next thing to do is construct a set of all of the recipients of
            // the message
            // TODO: users should subscribe
            List recipients = persistenceManager.findAll(persistenceSession,
                UserDO.class);


            // now remove the author from the recipients
            recipients.remove(author);
            // if there is an original author, remove her from the recipients too
            PersonDO originalAuthorPerson = null;
            if (originalAuthor != null) {
                recipients.remove(originalAuthor);
                originalAuthorPerson = addressBook.findPersonByUserName(securitySession,
                        originalAuthor.getName());
            }
            // since we are using the remote mail interface, we need to convert
            // the emailAddressses here
            recipientAddresses = new Vector(recipients.size());

            for (Iterator i = recipients.iterator(); i.hasNext();) {
                UserDO thisUser = (UserDO) i.next();
                // only add enabled users
                PersonDO thisPerson = addressBook.findPersonByUserName(securitySession,
                        thisUser.getName());
                String address = thisPerson.getEmailAddress();

                if (user.isEnabled()
                        && !user.isDeleted()
                        && (address != null)) {
                    recipientAddresses.add(address);
                }
            }
            String address;

            // if there is an original author, we'll use that person as the 'to'
            // recipient and make everyone else a cc
            if ((originalAuthor != null) &&
                (!originalAuthor.isDeleted()) &&
                ((address = originalAuthorPerson.getEmailAddress()) != null)) {
                to = new Vector();
                to.add(address);
                cc = recipientAddresses;
            } else {
                to = recipientAddresses;
                cc = null;
            }
            String contentType = settings.getStringSetting(securitySession,
                "libraryNotificationContentType", user);
            if (!to.isEmpty()
                && (securitySession instanceof MailSession)) {
                mail.send((MailSession) securitySession, senderAddress, to, cc,
                    null, subject, content, contentType, false);
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            List allRecipients = new Vector();
            if (to != null) {
                allRecipients.addAll(to);
            }
            if (cc != null) {
                allRecipients.addAll(cc);
            }
            throw new NotificationException(e, senderAddress, allRecipients);
        } finally {
            persistenceSession.close();
        }
    }
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
            final CommentDO comment)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            persistenceManager.remove(persistenceSession,
                    comment.getClass(), comment.getId());
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
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
            final LibraryItemDO item)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            /* TODO
            searchEngine.removeFromIndex(securitySession,
                    LibraryItemDO.class.getName(),
                    item.getId(), item.getTopic().getId().toString());
*/
            persistenceManager.remove(persistenceSession,
                    item.getClass(), item.getId());

/*TODO                // remove the directory with attachments (if any)
                // remove attachments and versions recursively
                DirectoryDO directory = (DirectoryDO)
                    persistenceManager.findInstance(persistenceSession,
                        "libraryDirectoryByParentIdName",
                        new Object[] {
                                DirectoryConstants.LIBRARY_DIRECTORY,
                                item.getId().toString()});

                drive.removeDirectory(securitySession, directory);
*/
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

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
            final TopicDO topic)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            persistenceManager.remove(persistenceSession,
                    topic.getClass(), topic.getId());
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
    /**
     * <p>
     * TODO
     * </p>
     *
     * @param itemId
     * @param revision
     * @param userName
     * @return
     */
    public LibraryItemDO revertItemToRevision(final SecuritySession securitySession,
            final String itemId,
            final String revision,
            final String comment)
            throws SystemException {
        throw new SystemException(new MethodNotSupportedException("ERROR: revertItemToRevision not implemented."));
/*TODO        LibraryItemDO revisedItemDO = null;

        try {
            if (itemId == null) {
                throw new InvalidFieldValueException("You must specify an identifier to revert a library item");
            }
            // find the current version
            LibraryItemDO item = itemHome.findByPrimaryKey(itemId);

            DriveLocalHome driveHome = (DriveLocalHome)
                ApplicationServer.findLocalHome("DriveLocal",
                    DriveLocalHome.class);
            DriveLocal drive = driveHome.create();

            // fidn Id of file
            DriveFileDO versionsFileDO =
                drive.findFileByPathFileName("/library/" + itemId.toString() + "/versions", "document.xml", userName);

            // find that head revision in CVS
            FileContentDO fileContent = drive.getFileRevision(versionsFileDO.getId(), revision, userName);

            // convert xml file to ItemDO
            revisedItemDO = itemHome.convertFileToItem(fileContent);
            // try to find that TOPIC, if not we will stay on actual
            try {
                TopicDO topic = topicHome.findByPrimaryKey(revisedItemDO.getTopicId());
            } catch (FinderException e) {
                revisedItemDO.setTopicId(item.getTopic().getId());
            }
            // if user changed topic we have to ask if user has right to add to that topic
            // if he didn't changed topic so we have to ask if he has right amend in that topic
            boolean canAmend = false;

            if (item.getTopic().getId().equals(revisedItemDO.getTopicId())) {
                canAmend = rights.canAmendInTopic(userName, item.getTopic().getId());
            } else {
                canAmend = rights.canAddToTopic(userName, revisedItemDO.getTopicId());
            }
            if (!canAmend && !userName.equals(item.getCreatedBy().getIntranetUserName())) {
                TopicDO topic = topicHome.findByPrimaryKey(item.getTopic().getId());

                throw new UserRightException("You do not have the necessary rights to amend an item in '" +
                        topic.getCaption() +
                        "'");
            }

            // let's do the business
            revisedItemDO.setModifiedBy(userName);
            revisedItemDO.setCreated(item.getCreated());
            revisedItemDO = item.setDO(revisedItemDO, comment);

            // submit the new version:
            Integer attachmentsDirectoryId =
                directoryHome.findByParentIdName(DirectoryConstants.LIBRARY_DIRECTORY, item.getId().toString()).getId();
            Integer versionsDirectoryId =
                directoryHome.findByParentIdName(attachmentsDirectoryId, "versions").getId();


            String tmpItemFilePath = item.convertItemToFile(revisedItemDO);

            // commit the new version
            DriveFileDO driveFile = new DriveFileDO();
            File tmpFile = new File(tmpItemFilePath);

            driveFile.setDirectoryId(versionsDirectoryId);
            driveFile.setFileName("document.xml");
            driveFile.setMimeType("text/xml");
            driveFile.setSize(new Integer((int)tmpFile.length()));
            FileRevisionDO fileRevision = new FileRevisionDO();
            fileRevision.setComment(StringHandling.getNotNull(comment, "new revision"));
            driveFile.setHeadRevision(fileRevision);



            drive.commitFile(driveFile, revisedItemDO.getModifiedBy(), tmpItemFilePath);
            // delete tmp file
            tmpFile.delete();tmpFile = null;

            notifyForSubmission(securitySession, userName, item, false);
        } catch (NamingException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        } catch (FinderException e) {
            throw new EJBException(e);
        }
        return revisedItemDO;
*/
    }

    /**
     * <p>
     * Sanitize all library contents.
     * </p>
     *
     * @param item
     * @param persistenceSession
     */
    public void sanitize(SecuritySession securitySession)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager
            .openSession(securitySession);
        SanitizerFormat sanitizer = new SanitizerFormat();
        sanitizer.setOnlyBodyContents(true);

        try {
            List allItems = Collections.synchronizedList(persistenceManager.findAll(persistenceSession,
                    LibraryItemDO.class));
            Iterator itemIterator = allItems.iterator();
            while (itemIterator.hasNext()) {
                sanitizeItem((LibraryItemDO) itemIterator.next(), persistenceSession,
                        sanitizer);
            }
            List allComments = Collections.synchronizedList(persistenceManager.findAll(persistenceSession,
                    CommentDO.class));
            Iterator commentIterator = allComments.iterator();
            List commentsToRemove = new Vector();
            while (commentIterator.hasNext()) {
                CommentDO comment = (CommentDO) commentIterator.next();
                if (comment.getFormat() == FormatConstants.FORMAT_HTML) {
                    if (log.isDebugEnabled()) {
                        log.debug("Sanitize text for comment id "
                                + comment.getId()
                                + " - subject '"
                                + comment.getSubject()
                                + "'");
                    }
                    String newText = sanitizer.format(comment.getText());
                    if (StringHandling.isNullOrEmpty(newText)) {
                        commentsToRemove.add(comment);
                    } else {
                        comment.setText(newText);
                        persistenceManager.amend(persistenceSession, comment);
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Ignoring non-HTML text for comment id "
                                + comment.getId()
                                + " - subject '"
                                + comment.getSubject()
                                + "'");
                    }

                }
            }
            commentIterator = commentsToRemove.iterator();
            while (commentIterator.hasNext()) {
                CommentDO comment = (CommentDO) commentIterator.next();
                persistenceManager.remove(persistenceSession, comment);
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }

    }

    /**
     * <p>
     * Private helper to clean up the HTML in an item.
     * </p>
     *
     * @param item The item to sanitize the HTML for.
     * @param persistenceSession Used to update the changed items.
     * @param sanitizer This sanitizer does all the hard work.
     */
    private void sanitizeItem(final LibraryItemDO item,
            final PersistenceSession persistenceSession,
            final SanitizerFormat sanitizer)
            throws PersistenceException {
        if (log.isDebugEnabled()) {
            log.debug("Sanitize text for item id "
                    + item.getId()
                    + " - title '"
                    + item.getTitle()
                    + "'");
        }
        item.setSummary(sanitizer.format(item.getSummary()));

        // update the index for the pages
        if (item.getPages() != null) {
            Iterator pagesIterator = item.getPages().iterator();
            // page number for debugging
            int pageNumber = 0;
            List pagesToRemove = new Vector();
            while (pagesIterator.hasNext()) {
                if (log.isDebugEnabled()) {
                    log.debug("Sanitize text for item id "
                            + item.getId()
                            + " - page "
                            + ++pageNumber);
                }
                PageDO page = (PageDO) pagesIterator.next();
                String newText = sanitizer.format(page.getText());
                // remove empty pages
                if (StringHandling.isNullOrEmpty(newText)) {
                    pagesToRemove.add(page);
                } else {
                    page.setText(newText);
                    persistenceManager.amend(persistenceSession, page);
                }
            }
            pagesIterator = pagesToRemove.iterator();
            while(pagesIterator.hasNext()) {
                PageDO page = (PageDO) pagesIterator.next();
                persistenceManager.remove(persistenceSession, page);
                item.getPages().remove(page);
            }

        }
        // update the index for the faqs
        if (item.getFAQCategories() != null) {
            Iterator fAQCategoriesIterator = item.getFAQCategories().iterator();
            while (fAQCategoriesIterator.hasNext()) {
                FAQCategoryDO faqCategory = (FAQCategoryDO) fAQCategoriesIterator.next();

                if (log.isDebugEnabled()) {
                    log.debug("Sanitize text for item id "
                            + item.getId()
                            + " - faq category "
                            + faqCategory.getName());
                }
                faqCategory.setDescription(sanitizer.format(faqCategory.getDescription()));


                if (faqCategory.getFAQs() != null) {
                    Iterator faqIterator = faqCategory.getFAQs().iterator();
                    while (faqIterator.hasNext()) {
                        FAQDO faq = (FAQDO) faqIterator.next();
                        faq.setAnswer(sanitizer.format(faq.getAnswer()));
                        persistenceManager.amend(persistenceSession, faq);
                    }
                }
                persistenceManager.amend(persistenceSession, faqCategory);
            }
        }
        persistenceManager.amend(persistenceSession, item);

    }

    /**
     * <p>
     * Re-index all library contents (items and comments) in the search engine.
     * This is useful after an upgrade, if the search functionality has changed.
     * </p>
     */
    public void updateSearchIndex (SecuritySession securitySession)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager
                .openSession(securitySession);
        try {
            List allItems = Collections.synchronizedList(persistenceManager.findAll(persistenceSession,
                    LibraryItemDO.class));
            Iterator itemIterator = allItems.iterator();
            while (itemIterator.hasNext()) {
                updateSearchIndexForItem(securitySession,
                        (LibraryItemDO) itemIterator.next());
            }
            List allComments = Collections.synchronizedList(persistenceManager.findAll(persistenceSession,
                    CommentDO.class));
            Iterator commentIterator = allComments.iterator();
            while (commentIterator.hasNext()) {
                CommentDO comment = (CommentDO) commentIterator.next();
                LibraryItemDO item = comment.getItem();
                searchEngine.updateIndex(securitySession, item.getId(),
                        LibraryItemDO.class.getName(),
                        item.getTopic().getId().toString(),
                        comment.getId(),
                        CommentDO.class.getName(),
                        comment.getText(),
                        comment.getFormat());
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>
     * Private helper to update the search index for an item which was added
     * or amended. This method goes thro' all the faq categories and pages,
     * and updates the index for each.
     * </p>
     *
     * @param item The item to update the search index for.
     * @throws SystemException See {@link
     * com.ivata.groupware.business.search.Search#updateIndex Search.updateIndex}
     */
    private void updateSearchIndexForItem(final SecuritySession securitySession,
            final LibraryItemDO item)
            throws SystemException {
        if (log.isDebugEnabled()) {
            log.debug("Update search index for item id "
                    + item.getId()
                    + " - title '"
                    + item.getTitle()
                    + "'");
        }

        // update the title and summary
        searchEngine.updateIndex(securitySession, item.getId(),
                LibraryItemDO.class.getName(),
                item.getTopic().getId().toString(),
                item.getId(),
                LibraryItemDO.class.getName(),
                item.getTitle()
                    + " "
                    + item.getSummary(),
                FormatConstants.FORMAT_HTML);

        // update the index for the pages
        if (item.getPages() != null) {
            Iterator pagesIterator = item.getPages().iterator();
            // page number for debugging
            int pageNumber = 0;
            while (pagesIterator.hasNext()) {
                if (log.isDebugEnabled()) {
                    log.debug("Update search index for item id "
                            + item.getId()
                            + " - page "
                            + ++pageNumber);
                }
                PageDO page = (PageDO) pagesIterator.next();
                searchEngine.updateIndex(securitySession, item.getId(),
                        LibraryItemDO.class.getName(),
                        item.getTopic().getId().toString(),
                        page.getId(),
                        PageDO.class.getName(),
                        page.getText(),
                        FormatConstants.FORMAT_HTML);
            }
        }
        // update the index for the faqs
        if (item.getFAQCategories() != null) {
            Iterator fAQCategoriesIterator = item.getFAQCategories().iterator();
            while (fAQCategoriesIterator.hasNext()) {
                FAQCategoryDO faqCategory = (FAQCategoryDO) fAQCategoriesIterator.next();

                if (log.isDebugEnabled()) {
                    log.debug("Update search index for item id "
                            + item.getId()
                            + " - faq category "
                            + faqCategory.getName());
                }

                searchEngine.updateIndex(securitySession, item.getId(),
                        LibraryItemDO.class.getName(),
                        item.getTopic().getId().toString(),
                        faqCategory.getId(),
                        FAQCategoryDO.class.getName(),
                        faqCategory.getName()
                            + " "
                            + faqCategory.getDescription(),
                        FormatConstants.FORMAT_HTML);

                if (faqCategory.getFAQs() != null) {
                    Iterator faqIterator = faqCategory.getFAQs().iterator();
                    while (faqIterator.hasNext()) {
                        FAQDO faq = (FAQDO) faqIterator.next();

                        searchEngine.updateIndex(securitySession, item.getId(),
                                LibraryItemDO.class.getName(),
                                item.getTopic().getId().toString(),
                                faq.getId(),
                                FAQDO.class.getName(),
                                faq.getQuestion()
                                    + " "
                                    + faq.getAnswer(),
                                FormatConstants.FORMAT_HTML);
                    }
                }
            }
        }
    }
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
            final CommentDO comment) {
        if (log.isDebugEnabled()) {
            log.debug("START validating "
                    + comment);
        }

        ValidationErrors errors = new ValidationErrors();
        Mask mask = maskFactory.getMask(CommentDO.class);

        if (comment.getItem() == null) {
            errors.add(new ValidationError(
                    "submitComment",
                    Library.BUNDLE_PATH,
                    mask.getField("item"),
                    "errors.required"));
        }
        if (StringHandling.isNullOrEmpty(comment.getSubject())) {
            errors.add(new ValidationError(
                    "submitComment",
                    Library.BUNDLE_PATH,
                    mask.getField("subject"),
                    "errors.required"));
        }
        if (StringHandling.isNullOrEmpty(comment.getText())) {
            errors.add(new ValidationError(
                    "submitComment",
                    Library.BUNDLE_PATH,
                    mask.getField("text"),
                    "errors.required"));
        }
        if (comment.getCreatedBy() == null) {
            errors.add(new ValidationError(
                    "submitComment",
                    Library.BUNDLE_PATH,
                    mask.getField("createdBy"),
                    "errors.required"));
        }

        if (log.isDebugEnabled()) {
            log.debug("END validating "
                    + comment
                    + "- errors: "
                    + errors);
        }
        return errors;
    }
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
            final LibraryItemDO item) {
        if (log.isDebugEnabled()) {
            log.debug("START validating "
                    + item);
        }

        ValidationErrors errors = new ValidationErrors();
        Mask mask = maskFactory.getMask(LibraryItemDO.class);

        if (StringHandling.isNullOrEmpty(item.getSummary())) {
            errors.add(new ValidationError(
                    "submit",
                    Library.BUNDLE_PATH,
                    mask.getField("summary"),
                    "errors.required"));
        }
        if (StringHandling.isNullOrEmpty(item.getTitle())) {
            errors.add(new ValidationError(
                    "submit",
                    Library.BUNDLE_PATH,
                    mask.getField("title"),
                    "errors.required"));
        }
        if (item.getTopic() == null) {
            errors.add(new ValidationError(
                    "submit",
                    Library.BUNDLE_PATH,
                    mask.getField("topic"),
                    "errors.required"));
        }
        if (item.getType() == null) {
            errors.add(new ValidationError(
                    "submit",
                    Library.BUNDLE_PATH,
                    mask.getField("type"),
                    "errors.required"));
        }
        // if this is a faq, it must have faq categories
        if (LibraryItemConstants.ITEM_FAQ.equals(item.getType())) {
            boolean noCategoryName = false;

            for (Iterator i = item.getFAQCategories().iterator(); i.hasNext();) {
                FAQCategoryDO faqCategory = (FAQCategoryDO) i.next();
                String categoryName = faqCategory.getName();

                if (!noCategoryName && StringHandling.isNullOrEmpty(categoryName)) {
                    errors.add(new ValidationError(
                            "submitFaq",
                            Library.BUNDLE_PATH,
                            mask.getField("category"),
                            "errors.required"));
                    // we only want to show the error once
                    noCategoryName = true;
                }
                for (Iterator j = faqCategory.getFAQs().iterator(); j.hasNext();) {
                    FAQDO faq = (FAQDO) j.next();

                    if (StringHandling.isNullOrEmpty(faq.getQuestion())) {
                        errors.add(new ValidationError(
                                "submitFaq",
                                Library.BUNDLE_PATH,
                                mask.getField("question"),
                                "errors.required"));
                        break;
                    }
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("END validating "
                    + item
                    + "- errors: "
                    + errors);
        }
        return errors;
    }

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
            final TopicDO topic) {
        ValidationErrors errors = new ValidationErrors();
        Mask mask = maskFactory.getMask(TopicDO.class);

        if (StringHandling.isNullOrEmpty(topic.getCaption())) {
            errors.add(new ValidationError(
                    "topicModify",
                    Library.BUNDLE_PATH,
                    mask.getField("caption"),
                    "errors.required"));
        }
        return errors;
    }

}
