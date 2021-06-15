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
package com.ivata.groupware.business.library.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.library.Library;
import com.ivata.groupware.business.library.topic.TopicDO;
import com.ivata.mask.Mask;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>Contains details of a topic which is being changed, or where the
 * user (group) rights are being altered..</p>
 *
 * @since 2002-11-22
 * @author Jan Boros <janboros@sourceforge.net>
 * @version $Revision: 1.3 $
 */
public class TopicForm extends DialogForm {
    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     */
    private Class baseClass;

    /**
     * Library - used in validation.
     */
    private Library library;

    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     */
    private Mask mask;
    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>add</strong> library items to this topic.</p>
     */
    private Integer[] rightsAddItem;

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>amend</strong> this topic.</p>
     */
    private Integer[] rightsAmend;

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>amend</strong> library items which have this topic.</p>
     */
    private Integer[] rightsAmendItem;

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>remove</strong>  this topic.</p>
     */
    private Integer[] rightsRemove;

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>remove</strong> library items with this topic.</p>
     */
    private Integer[] rightsRemoveItem;
    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>view</strong> this topic.</p>
     */
    private Integer[] rightsView;

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>view</strong> library items with this topic.</p>
     */
    private Integer[] rightsViewItem;

    /**
     * <p>Contains details of the topic which is currently being modified.</p>
     */
    private TopicDO topic;

    /**
     * <p>which TAB is active.</p>
     */
    private Integer topicTab_activeTab;

    public TopicForm(final Library libraryParam) {
        this.library = libraryParam;
    }
    /**
     * TODO
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
        rightsAddItem = new Integer[] {  };
        rightsAmend = new Integer[] {  };
        rightsAmendItem = new Integer[] {  };
        rightsRemove = new Integer[] {  };
        rightsRemoveItem = new Integer[] {  };
        rightsView = new Integer[] {  };
        rightsViewItem = new Integer[] {  };
        topic = new TopicDO();
        topicTab_activeTab = null;
    }

    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     *
     * @return base class of all objects in the value object list.
     */
    public final Class getBaseClass() {
        return baseClass;
    }

    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     *
     * @return mask containing all the field definitions for this list.
     */
    public final Mask getMask() {
        return mask;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>add</strong> library items to this topic.</p>
     *
     * @return the current value of rightsAddItem.
     */
    public final Integer[] getRightsAddItem() {
        return this.rightsAddItem;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>amend</strong> library items which have this topic.</p>
     *
     * @return the current value of rightsAmend.
     */
    public final Integer[] getRightsAmend() {
        return this.rightsAmend;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>amend</strong> library items which have this topic.</p>
     *
     * @return the current value of rightsAmendItem.
     */
    public final Integer[] getRightsAmendItem() {
        return this.rightsAmendItem;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>remove</strong> library items with this topic.</p>
     *
     * @return the current value of groups who can remove from this topic.
     */
    public final Integer[] getRightsRemove() {
        return this.rightsRemove;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>remove</strong> library items with this topic.</p>
     *
     * @return the current value of rightsRemoveItem.
     */
    public final Integer[] getRightsRemoveItem() {
        return this.rightsRemoveItem;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>view</strong> library items with this topic.</p>
     *
     * @return the current value of groups who can view this topic.
     */
    public final Integer[] getRightsView() {
        return rightsView;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>view</strong> library items with this topic.</p>
     *
     * @return the current value of rightsViewItem.
     */
    public final Integer[] getRightsViewItem() {
        return this.rightsViewItem;
    }

    /**
     * <p>Contains details of the topic which is currently being modified.</p>
     *
     * @return the current value of topic.
     */
    public final TopicDO getTopic() {
        return topic;
    }

    /**
     * <p>which TAB is active.</p>
     *
     * @return the current value of topicTab_activeTab.
     */
    public final Integer getTopicTab_activeTab() {
        return this.topicTab_activeTab;
    }

    /**
     * <p>Reset all bean properties to their default state.  This method
     * is called before the properties are repopulated by the controller
     * servlet.<p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        // I have to clear arrays because STRUTS is not doing that
        // meybe we neew to CHAGE tree select tag
        Integer[] tmpRights = {  };
        int tab = ((this.topicTab_activeTab == null) ? 0
                                                     : this.topicTab_activeTab.intValue());

        if ((tab == 1) && (request.getParameterValues("rightsView") == null)) {
            this.setRightsView(tmpRights);
        } else if ((tab == 2) &&
                (request.getParameterValues("rightsAmend") == null)) {
            this.setRightsAmend(tmpRights);
        } else if ((tab == 3) &&
                (request.getParameterValues("rightsRemove") == null)) {
            this.setRightsRemove(tmpRights);
        }

        // if you didn't click delete butoon so don't show delete message if user want to delete topic
        if (request.getParameterValues("deleteWarn") == null) {
            this.setDeleteWarn(null);
        }
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>add</strong> library items to this topic.</p>
     *
     * @param rightsAddItem the new value of rightsAddItem.
     */
    public final void setRightsAddItem(final Integer[] rightsAddItem) {
        this.rightsAddItem = rightsAddItem;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>amend</strong> library items which have this topic.</p>
     *
     * @param rightsAmend the new value of rightsAmend.
     */
    public final void setRightsAmend(final Integer[] rightsAmend) {
        this.rightsAmend = rightsAmend;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>amend</strong> library items which have this topic.</p>
     *
     * @param rightsAmendItem the new value of rightsAmendItem.
     */
    public final void setRightsAmendItem(final Integer[] rightsAmendItem) {
        this.rightsAmendItem = rightsAmendItem;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>remove</strong> library items with this topic.</p>
     *
     * @param rightsRemove the new value of groups who can remove from
     * this topic.
     */
    public final void setRightsRemove(final Integer[] rightsRemove) {
        this.rightsRemove = rightsRemove;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>remove</strong> library items with this topic.</p>
     *
     * @param rightsRemoveItem the new value of rightsRemoveItem.
     */
    public final void setRightsRemoveItem(final Integer[] rightsRemoveItem) {
        this.rightsRemoveItem = rightsRemoveItem;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>view</strong> library items with this topic.</p>
     *
     * @param rightsView the new value of groups who can view this topic.
     */
    public final void setRightsView(final Integer[] rightsView) {
        this.rightsView = rightsView;
    }

    /**
     * <p>Contains an array of all of the group ids for groups who have
     * the right to <strong>view</strong> library items with this topic.</p>
     *
     * @param rightsViewItem the new value of rightsViewItem.
     */
    public final void setRightsViewItem(final Integer[] rightsViewItem) {
        this.rightsViewItem = rightsViewItem;
    }

    /**
     * <p>Contains details of the topic which is currently being modified.</p>
     *
     * @param topic the new value of topic.
     */
    public final void setTopic(final TopicDO topic) {
        this.topic = topic;
    }

    /**
     * <p>which TAB is active.</p>
     *
     * @param topicTab_activeTab the new value of topicTab_activeTab.
     */
    public final void setTopicTab_activeTab(final Integer topicTab_activeTab) {
        this.topicTab_activeTab = topicTab_activeTab;
    }

    /**
     * <p>Call the corresponding server-side validation, handle possible
     * exceptions and return any errors generated.</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @return <code>ActionMessages</code> collection containing all
     * validation errors, or <code>null</code> if there were no errors.
     * @see com.ivata.mask.web.struts.MaskForm#validate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpSession)
     */
    public ValidationErrors validate(final HttpServletRequest request,
            final HttpSession session) {
        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");
        return library.validate(securitySession, topic);
    }
}
