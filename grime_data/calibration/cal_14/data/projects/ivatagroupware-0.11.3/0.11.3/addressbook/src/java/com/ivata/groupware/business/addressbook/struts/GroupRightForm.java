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
package com.ivata.groupware.business.addressbook.struts;

import java.io.Serializable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.mask.Mask;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>Contains details of AddressBook or UserGroup and  rights being changed for that kindof  group. Field type is saying what we are maintaining : addressBook = 1, userGroup = 2.</p>
 *
 * @since 2003-05-10
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class GroupRightForm extends DialogForm implements Serializable {
    /**
     * <p>Button for add group to the right list.</p>
     */
    private String add = null;
    /**
     *<p> if user did click "add" this will contain id of group which is *going to be add.</p>
     */
    private Integer addGroup = null;
    private AddressBook addressBook;
    /**
     * <p> if user has right to remove the userGroup/addressBook is this true.</p>
     */
    private boolean canRemove = false;
    /**
     * <p>If list of rights is empty write this message out.</p>
     */
    private String emptyListMessage = null;
    /**
     * <p>Instance of group data object, containing all values
     * of group being submitted.</p>
     */
    private GroupDO group = new GroupDO();
    /**
     * <p><code>Vector</code> containing <code>Integer</code> instances,
     * representing ids of all the groups users the current group.</p>
     */
    private Vector groupIds = new Vector ();
    /**
     * <p><code>Vector</code> containing <code>String</code> instances,
     * representing names of all the groups users the current group.</p>
     */
    private Vector groupNames = new Vector ();
    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can add to.</p>
     */
    private String[] groupRightsAdd;
    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can amend in.</p>
     */
    private String[] groupRightsAmend;
    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can delete from.</p>
     */
    private String[] groupRightsRemove;
    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can view.</p>
     */
    private String[] groupRightsView;

    /**
     * <p>The number of the active tab being displayed, starting with 0.</p>
     */
    private Integer groupRightTab_activeTab = null;
    /**
     * <p>The URI of the page which will be included to represent the currently
     * chosen tab.</p>
     */
    private String includePage = "/addressBook/groupRightGeneral.jsp";
    /**
     * <p> if user has not right to chage addressBook / useGroup it's true.</p>
     */
    private boolean readOnly = false;
    private String remove;
    /**
     * <p>Indicates the names of 'rows' of the list. This will either be
     * <code>groupNames</code> or <code>topicCaptions</code>, depending on the
     * list currently being displayed.</p>
     */
    private Vector rowNames;
    /**
     * <p>Indicates the 'rows' of the list. This will either be <code>groupIds</code>
     * or <code>topicIds</code>, depending on the list currently being displayed.</p>
     */
    private Vector rows;
    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * topics or groups which have been selected for deletion.</p>
     */
    private String[] selected;
    /**
     * <p> We can maintain AddressBook or UserGroup.</p>
     */
    private String type = null;
    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     */
    private Class baseClass;
    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     */
    private Mask mask;

    /**
     * <p>
     * Construct a group right form.
     * </p>
     *
     * @param addressBook address book implementation to use.
     * @param maskParam Refer to {@link DialogForm#DialogForm}.
     * @param baseClassParam Refer to {@link DialogForm#DialogForm}.
     */
    GroupRightForm(final AddressBook addressBook,
            final Mask maskParam,
            final Class baseClassParam) {
        this.addressBook = addressBook;
    }

    /**
     * <p>
     * Return all form state to initial values.
     * </p>
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
        add = null;
        addGroup = null;
        canRemove = false;
        emptyListMessage = null;
        group = new GroupDO();
        groupIds = new Vector ();
        groupNames = new Vector ();
        groupRightTab_activeTab = null;
        setHelpKey("addressbook.groupRightUser");
        includePage = "/addressBook/groupRightGeneral.jsp";
        readOnly = false;
        remove = null;
        rowNames = null;
        rows = null;
        selected = null;
        type = null;
    }

    /**
     * <p>Get whether or not the add button was pressed.</p>
     *
     * <p>If the add button was pressed, this attribute is set to a non-null
     * string.</p>
     *
     * @return non-<code>null</code> if the add key was pressed, otherwise
     * <code>null</code>.
     */
    public final String getAdd() {
        return add;
    }

    /**
     *<p> if user did click "add" this will contain id of group which is
     * *going to be added.</p>
     *
     * @return the current value of addGroup.
     */
    public final Integer getAddGroup() {
        return this.addGroup;
    }

    /**
     * <p> if user has right to remove the userGroup/addressBook is this true.</p>
     *
     * @return the current value of canRemove.
     */
    public final boolean getCanRemove() {
        return this.canRemove;
    }

    /**
     * <p>Description of group.</p>
     *
     * @return the current value of group.description
     */
    public final String getDescription() {
        return this.group.getDescription();
    }

    /**
     * <p>If list of rights is empty write this message out.</p>
     *
     * @return the current value of emptyListMessage.
     */
    public final String getEmptyListMessage() {
        return this.emptyListMessage;
    }

    /**
     * <p>Instance of group data object, containing all values
     * of group being submitted.</p>
     *
     * @return the current value of group.
     */
    public final GroupDO getGroup() {
        return this.group;
    }

    /**
     * <p>Get a <code>Vector</code> containing <code>Integer</code> instances,
     * representing ids of all the groups users the current group.</p>
     *
     * @return current value of groupIds.
     */
    public final Vector getGroupIds() {
        return groupIds;
    }

    /**
     * <p>Get a <code>Vector</code> containing <code>Integer</code> instances,
     * representing ids of all the groups users the current group.</p>
     *
     * @return current value of groupNames.
     */
    public final Vector getGroupNames() {
        return groupNames;
    }

    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can add to.</p>
     *
     * @return the current value of groupRightsAdd[].
     */
    public final String[] getGroupRightsAdd() {
        return this.groupRightsAdd;
    }

    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can amend in.</p>
     *
     * @return the current value of groupRightsAmend[].
     */
    public final String[] getGroupRightsAmend() {
        return this.groupRightsAmend;
    }

    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can delete from.</p>
     *
     * @return the current value of groupRightsRemove[].
     */
    public final String[] getGroupRightsRemove() {
        return this.groupRightsRemove;
    }

    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can view.</p>
     *
     * @return the current value of groupRightsView[].
     */
    public final String[] getGroupRightsView() {
        return this.groupRightsView;
    }

    /**
     * <p>Get the number of the active tab being displayed, starting with 0.</p>
     *
     * @return current value of groupTab_activeTab.
     */
    public final Integer getGroupRightTab_activeTab() {
        return groupRightTab_activeTab;
    }

    /**
     * <p>Get the URI of the page which will be included to represent the currently
     * chosen tab.</p>
     *
     * @return the current value of includePage.
     */
    public final String getIncludePage() {
        return includePage;
    }

    /**
     * <p>Name of group.</p>
     *
     * @return the current value of group.name
     */
    public String getName() {
        return this.group.getName();
    }

    /**
     * <p> if user has not right to chage addressBook / useGroup it's true.</p>
     *
     * @return the current value of readOnly.
     */
    public final boolean getReadOnly() {
        return this.readOnly;
    }

    /**
     * <p>Get whether or not the remove button was pressed.</p>
     *
     * <p>If the remove button was pressed, this attribute is set to a non-null
     * string.</p>
     *
     * @return non-<code>null</code> if the remove key was pressed, otherwise
     * <code>null</code>.
     */
    public final String getRemove() {
        return remove;
    }

    /**
     * <p>Get the names of 'rows' of the list.</p>
     *
     * @return <code>groupNames</code> or <code>topicCaptions</code>, depending
     * on the list currently being displayed.</p>
     */
    public final Vector getRowNames() {
        return this.rowNames;
    }

    /**
     * <p>Get the 'rows' of the list.</p>
     *
     * @return <code>groupIds</code> or <code>topicIds</code>, depending on the
     * list currently being displayed.</p>
     */
    public final Vector getRows() {
        return this.rows;
    }

    /**
     * <p>Get a <code>String</code> array of numbers representing the id's of the
     * topics or groups which have been selected for deletion.</p>
     *
     * @return current value of selected
     */
    public final String[] getSelected() {
        return selected;
    }

    /**
     * <p> We can maintain AddressBook or UserGroup.</p>
     *
     * @return the current value of type.
     */
    public final String getType() {
        return this.type;
    }

    /**
     * <p>Reset all bean properties to their default state.  This method
     * is called before the properties are repopulated by the controller
     * servlet.</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        super.reset(mapping, request);
        setGroupRightTab_activeTab(new Integer(0));
        selected = new String[0];

        if (includePage.equals("/addressBook/groupRightDetail.jsp")) {
            groupRightsView = new String [0];
            groupRightsAdd = new String [0];
            groupRightsAmend = new String [0];
            groupRightsRemove = new String [0];
        }
    }

    /**
     * <p>Set whether or not the add button was pressed.</p>
     *
     * <p>If the add button was pressed, this attribute is set to a non-null
     * string.</p>
     *
     * @param add set to non-<code>null</code> if the add key was pressed, otherwise
     * <code>null</code>.
     */
    public final void setAdd(final String add) {
        this.add = add;
    }

    /**
     *<p> if user did click "add" this will contain id of group which is *going to be add.</p>
     *
     * @param addGroup the new value of addGroup.
     */
    public final void setAddGroup(final Integer addGroup) {
        this.addGroup = addGroup;
    }

    /**
     * <p> if user has right to remove the userGroup/addressBook is this true.</p>
     *
     * @param canRemove the new value of canRemove.
     */
    public final void setCanRemove(final boolean canRemove) {
        this.canRemove = canRemove;
    }

    /**
     * <p>Description of group.</p>
     *
     * @param description the new value of group.description
     */
    public final void setDescription(final String description) {
        this.group.setDescription(description);
    }

    /**
     * <p>If list of rights is empty write this message out.</p>
     *
     * @param emptyListMessage the new value of emptyListMessage.
     */
    public final void setEmptyListMessage(final String emptyListMessage) {
        this.emptyListMessage = emptyListMessage;
    }

    /**
     * <p>Instance of group data object, containing all values
     * of group being submitted.</p>
     *
     * @param group the new value of group.
     */
    public final void setGroup(final GroupDO group) {
        this.group = group;
    }

    /**
     * <p>Set a <code>Vector</code> containing <code>Integer</code> instances,
     * representing ids of all the groups users the current group.</p>
     *
     * @param groupIds new value of groupIds.
     */
    public final void setGroupIds(final Vector groupIds) {
        this.groupIds = groupIds;
    }

    /**
     * <p>Set a <code>Vector</code> containing <code>Integer</code> instances,
     * representing ids of all the groups users the current group.</p>
     *
     * @param groupNames new value of groupNames.
     */
    public final void setGroupNames(final Vector groupNames) {
        this.groupNames = groupNames;
    }

    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can add to.</p>
     *
     * @param groupRightsAdd[] the new value of groupRightsAdd[].
     */
    public final void setGroupRightsAdd(final String groupRightsAdd[]) {
        this.groupRightsAdd = groupRightsAdd;
    }

    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can amend in.</p>
     *
     * @param groupRightsAmend[] the new value of groupRightsAmend[].
     */
    public final void setGroupRightsAmend(final String groupRightsAmend[]) {
        this.groupRightsAmend = groupRightsAmend;
    }

    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can delete from.</p>
     *
     * @param groupRightsRemove[] the new value of groupRightsRemove[].
     */
    public final void setGroupRightsRemove(final String groupRightsRemove[]) {
        this.groupRightsRemove = groupRightsRemove;
    }

    /**
     * <p><code>String</code> array of numbers representing the id's of the
     * contact groups which users in the current group can view.</p>
     *
     * @param groupRightsView[] the new value of groupRightsView[].
     */
    public final void setGroupRightsView(final String groupRightsView[]) {
        this.groupRightsView = groupRightsView;
    }

    /**
     * <p>Set the number of the active tab being displayed, starting with 0.</p>
     *
     * @param groupRightTab_activeTab the new value of groupTab_activeTab.
     */
    public final void setGroupRightTab_activeTab(final Integer groupRightTab_activeTab) {
        this.groupRightTab_activeTab = groupRightTab_activeTab;
    }

    /**
     * <p>Set the URI of the page which will be included to represent the currently
     * chosen tab.</p>
     *
     * @param includePage new value of includePage.
     */
    public final void setIncludePage(final String includePage) {
        this.includePage = includePage;
    }

    /**
     * <p>Name of group.</p>
     *
     * @param name the new value of group.name
     */
    public final void setName(final String name) {
        this.group.setName(name);
    }

    /**
     * <p> if user has not right to chage addressBook / useGroup it's true.</p>
     *
     * @param readOnly the new value of readOnly.
     */
    public final void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * <p>Set whether or not the remove button was pressed.</p>
     *
     * <p>If the remove button was pressed, this attribute is set to a non-null
     * string.</p>
     *
     * @param remove set to non-<code>null</code> if the remove key was pressed,
     * otherwise <code>null</code>.
     */
    public final void setRemove(final String remove) {
        this.remove = remove;
    }

    /**
     * <p>Set the names of 'rows' of the list.</p>
     *
     * @param rowNames <code>groupNames</code> or <code>topicCaptions</code>, depending
     * on the list currently being displayed.</p>
     */
    public final void setRowNames(final Vector rowNames) {
        this.rowNames = rowNames;
    }

    /**
     * <p>Set the 'rows' of the list.</p>
     *
     * @param rows <code>groupIds</code> or <code>topicIds</code>, depending on the
     * list currently being displayed.</p>
     */
    public final void setRows(final Vector rows) {
        this.rows = rows;
    }

    /**
     * <p>Set a <code>String</code> array of numbers representing the id's of the
     * topics which have been selected for deletion.</p>
     *
     * @param new value of libraryRightsAdd.
     */
    public final void setSelected(final String[] selected) {
        this.selected = selected;
    }

    /**
     * <p> We can maintain AddressBook or UserGroup.</p>
     *
     * @param type the new value of type.
     */
    public final void setType(final String type) {
        this.type = type;
    }

    /**
     * <p>Validates the form contents.</p>
     *
     * @param addressBook valid <code>AddressBookRemote</code> instance.
     * @param locale locale used to show error messages.
     * @param session current session to set/get attributes from.
     * @return <code>null</code> or an <code>ActionMessages</code> object
     * with recorded error messages.
     */
    public ValidationErrors validate(final HttpServletRequest request,
            final HttpSession session) {
        ValidationErrors validationErrors = new ValidationErrors();

        return validationErrors;
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
}
