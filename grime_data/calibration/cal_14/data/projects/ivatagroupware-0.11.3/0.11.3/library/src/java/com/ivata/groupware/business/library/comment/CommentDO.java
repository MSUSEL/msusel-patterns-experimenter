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
package com.ivata.groupware.business.library.comment;

import java.io.Serializable;

import com.ivata.groupware.business.library.item.LibraryItemDO;
import com.ivata.groupware.container.persistence.TimestampDO;
import com.ivata.mask.web.format.FormatConstants;


/**
 * <p>Represents a single comment by a user, either in reply to a library item
 * or to another comment. The comments are usually displayed using a tree
 * generated from the {@link CommentTreeModelBean tree model}.</p>
 *
 * @since 2002-07-05
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table = "library_comment"
 */
public class CommentDO extends TimestampDO
        implements com.ivata.groupware.web.tree.TreeNode,
        Serializable {

    /**
     * <p>Store the format of this comment. This should correspond to one of the
     * <code>FORMAT_...</code> constants in {@link
     * com.ivata.mask.web.format.FormatConstants FormatConstants}.</p>
     */
    private int format = FormatConstants.FORMAT_TEXT;

    /**
     * <p>Store the item the comment relates to.</p>
     */
    private LibraryItemDO item;

    /**
     * <p>Parent comment which contains this one.</p>
     */
    private CommentDO parent;

    /**
     * <p>Store the subject for this comment. This is usually a clear-text
     * describing the comment, and can begin with "Re: " to show that it is
     * related to other comments in a thread.</p>
     */
    private String subject;

    /**
     * <p>Store the actual comment body, describing the user's thoughts.</p>
     */
    private String text;

    /**
     * <p>
     * If a comment should be ignored and needs no reply, set this to
     * <code>true</code>.
     * </p>
     */
    private boolean unacknowledged = false;

    /**
     * <p>Get the format of this comment. This should correspond to one of the
     * <code>FORMAT_...</code> constants in {@link
     * com.ivata.mask.web.format.FormatConstants FormatConstants}.</p>
     *
     * @return one of the <code>FORMAT_...</code> constants in {@link
     * com.ivata.mask.web.format.FormatConstants FormatConstants} which
     * identifies the formatting of this comment.
     *
     * @hibernate.property
     */
    public final int getFormat() {
        return format;
    }

    /**
     * <p>Get the library item this comment relates
     * to. Every comment in the system must relate to a library item.</p>
     *
     * @return unique identifier of the library item this comment
     * relates to.
     * @hibernate.many-to-one
     */
    public final LibraryItemDO getItem() {
        return item;
    }

    /**
     * <p>Get the 'name' for the subject as it appears in the tree. This returns
     * the same as <code>getSubject</code>.</p>
     *
     * @return see {@link #getSubject getSubject}.
     */
    public final String getName() {
        return getSubject();
    }
    /**
     * <p>Parent comment which contains this one.</p>
     *
     * @return comment which contains this one
     * @hibernate.many-to-one
     *      column="id_reply_to"
     */
    public final CommentDO getParent() {
        return parent;
    }

    /**
     * <p>Get the subject for this comment. This is usually a clear-text
     * describing the comment, and can begin with "Re: " to show that it is
     * related to other comments in a thread.</p>
     *
     * @return clear-text describing the comment content.
     * @hibernate.property
     */
    public final String getSubject() {
        return subject;
    }

    /**
     * <p>Get the actual comment body, describing the user's thoughts.</p>
     *
     * @return new value for the comment body describing the user's
     * thoughts.
     * @hibernate.property
     */
    public final String getText() {
        return text;
    }
    /**
     * <p>
     * If a comment should be ignored and needs no reply, set this to
     * <code>true</code>.
     * </p>
     *
     * @return current value of ignored.
     * @hibernate.property
     *      column="no_reply"
     */
    public boolean isUnacknowledged() {
        return unacknowledged;
    }

    /**
     * <p>Set the format of this comment. This should correspond to one of the
     * <code>FORMAT_...</code> constants in {@link
     * com.ivata.mask.web.format.FormatConstants FormatConstants}.</p>
     *
     * @param format one of the <code>FORMAT_...</code> constants in {@link
     * com.ivata.mask.web.format.FormatConstants FormatConstants} to
     * identify the formatting of this comment.
     */
    public final void setFormat(final int format) {
        this.format = format;
    }

    /**
     * <p>Set the unique identifier of the library item this comment relates
     * to. Every comment in the system must relate to a library item.</p>
     *
     * @param itemId new unique identifier of the library item this comment
     * relates to.
     */
    public final void setItem(final LibraryItemDO item) {
        this.item = item;
    }

    /**
     * <p>Parent comment which contains this one.</p>
     *
     * @param parent comment which contains this one
     */
    public final void setParent(final CommentDO parent) {
        this.parent = parent;
    }

    /**
     * <p>Set the subject for this comment. This is usually a clear-text
     * describing the comment, and can begin with "Re: " to show that it is
     * related to other comments in a thread.</p>
     *
     * @param subject clear-text describing the comment content.
     */
    public final void setSubject(final String subject) {
        this.subject = subject;
    }

    /**
     * <p>Set the actual comment body, describing the user's thoughts.</p>
     *
     * @param text new value for the comment body describing the user's
     * thoughts.
     */
    public final void setText(final String text) {
        this.text = text;
    }
    /**
     * <p>
     * If a comment should be ignored and needs no reply, set this to
     * <code>true</code>.
     * </p>
     *
     * @param ignored new value of ignored.
     */
    public final void setUnacknowledged(final boolean ignored) {
        this.unacknowledged = ignored;
    }
}
