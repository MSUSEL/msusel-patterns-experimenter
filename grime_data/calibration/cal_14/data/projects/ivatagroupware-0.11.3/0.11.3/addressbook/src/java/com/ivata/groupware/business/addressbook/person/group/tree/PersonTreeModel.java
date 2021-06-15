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
package com.ivata.groupware.business.addressbook.person.group.tree;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.group.GroupConstants;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.business.addressbook.person.group.right.UserRightFilter;
import com.ivata.mask.util.SystemException;


/**
 * <p>Provides a tree model of the heirarchy of groups. This class is wrapped
 * by {@link com.ivata.groupware.business.addressbook.person.group.PersonTreeModel PersonTreeModel}
 * so that client classes can easily display the group tree heirarchy in a tree
 * control.</p>
 *
 * <p>In <em>ivata groupware</em>, this tree control is displayed using {@link
 * com.ivata.groupware.web.tag.webgui.TreeTag TreeTag}.</p>
 *
 * <p>This is a wrapper class which wraps the remote class
 * {@link GroupTreeModelRemote GroupTreeModelRemote}, handles any
 * <code>RemoteException</code> and implements interfaces required
 * by the client application.</p>
 *
 * <p><strong>Note:</strong> This class provides data from {@link GroupTreeModelBean GroupTreeModelBean}.
 * This is no local copy of the bean class, however, and changes here
 * will not be automatically reflected in {@link GroupTreeModelBean GroupTreeModelBean}.</p>
 *
 * @since  2002-05-16
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 * @see GroupTreeModelBean
 * @see GroupTreeModelRemote
 */
public class PersonTreeModel implements TreeModel, Serializable {

    /**
     * <p>
     * The address book implementation, used to find the root group.
     * </p>
     */
    private AddressBook addressBook;

    /**
     * <p>This parameter is used to set exclude a branch of the tree, beginning
     * with the id of the node provided. This is useful because you don't want a
     * node to appear in the tree to select its parent.</p>
     */
    private Integer excludeBranch = null;

    /**
     * <p>Stores the filter to apply, if one has been set.</p>
     */
    private UserRightFilter filter = null;

    /**
     * <p>Stores whether or not the tree includes people data in
     * <code>PersonTreeNode.personDO</code>.</p>
     */
    private boolean includePeople = false;

    /**
     * <p>Stores whether or not the tree only includes users.</p>
     */
    private boolean onlyUsers = false;

    /**
     * <p>Specifies the id of the root tree element.</p>
     */
    private Integer rootId = GroupConstants.ADDRESS_BOOK;

    /**
     * <p>
     * SecuritySession valid security session, used to build the whole tree.
     * </p>
     */
    private SecuritySession securitySession;

    /**
     * Construct a group tree model with the given address book.
     *
     * @param addressBook valid address book. Used to retrieve the root node.
     * @param securitySession valid security session, used to build the whole
     *     tree.
     */
    public PersonTreeModel(AddressBook addressBook, SecuritySession securitySession) {
        this.addressBook = addressBook;
        this.securitySession = securitySession;
    }

    /**
     * <p>This method is not implemented for this class</p>
     *
     * @param l the listener to add. Not used in this class.
     */
    public void addTreeModelListener(final TreeModelListener l) {
        throw new UnsupportedOperationException("Method addTreeModelListener() not implemented.");
    }

    /**
     * <p>
     * Helper. Get all the children of the given group node - this includes
     * people, if so desired.
     * </p>
     *
     * @param parent person tree node representing a parent group.
     * @return all child groups and people contained in the group.
     */
    private List getAllChildren(final Object parent) {
        GroupDO parentGroup = ((PersonTreeNode) parent).getGroup();
        // make a list to include people in the children if necessary
        List allChildren = new Vector();
        try {
            List groups = addressBook.findGroupsByParent(securitySession,
                    parentGroup.getId());
            Iterator groupIterator = groups.iterator();
            while(groupIterator.hasNext()) {
                GroupDO childGroup = (GroupDO) groupIterator.next();
                if (!childGroup.getId().equals(excludeBranch)) {
                    allChildren.add(new PersonTreeNode(childGroup));
                }
            }
        } catch(SystemException e) {
            throw new RuntimeException("ERROR in group tree model: could not locate children of '"
                + parentGroup.getId()
                + "'",
                e);
        }
        if (includePeople) {
            Iterator personIterator = parentGroup.getPeople().iterator();
            while(personIterator.hasNext()) {
                allChildren.add(new PersonTreeNode((PersonDO) personIterator.next()));
            }
        }
        return allChildren;
    }

    /**
     * <p>Get the child of the parent group provided, with the specified
     * index.</p>
     *
     * @param parent the parent for whom to return the child.
     * @param index the index of the child within the internal parent array.
     * @return the child of the parent group provided, with the specified index,
     *      or <code>null</code> if the specified group has no children.
     */
    public final Object getChild(final Object parent,
            final int index) {
        PersonTreeNode parentNode = (PersonTreeNode) parent;
        if (parentNode.getPerson() != null) {
            return null;
        }
        List allChildren = getAllChildren(parent);
        if (allChildren.size() == 0) {
            return null;
        }
        return allChildren.get(index);
    }


    /**
     * <p>Get the number of children for the given parent group.</p>
     *
     * @param parent the parent for whom to return the count of the number of
     *     children.
     * @return the number of children for the given parent group.
     */
    public final int getChildCount(final Object parent) {
        PersonTreeNode parentNode = (PersonTreeNode) parent;
        if (parentNode.getPerson() != null) {
            return 0;
        }
        List allChildren = getAllChildren(parent);
        return allChildren.size();
    }

    /**
     * <p>This parameter is used to set exclude a branch of the tree, beginning
     * with the id of the node provided. This is useful because you don't want a
     * node to appear in the tree to select its parent.</p>
     *
     * @return branch to be excluded, or null if all branches are included.
     */
    public final Integer getExcludeBranch() {
        return excludeBranch;
    }

    /**
     * <p>This parameter is used to set a filter. For a full description, please
     * refer to the documentation of {@link GroupTreeModelBean
     * GroupTreeModelBean}.</p>
     *
     * @return filter applied, or <code>null</code> if none has been set.
     */
    public UserRightFilter getFilter() {
        return filter;
    }

    /**
     * <p>Get whether or not this tree should include people, or just display
     * groups.</p>
     *
     * @return if <code>true</code>, then the tree will include
     *    people, otherwise only groups are displayed.
     */
    public final boolean getIncludePeople() {
        return includePeople;
    }

    /**
     * <p>Get the index of a particular child in the parent's array of
     * children.</p>
     *
     * @param parent the parent node for whom to find the child index.
     * @param child the child node to find the index for.
     * @return the index of a child node for the given parent group, or
     *     <code>-1</code> if this parent has no children.
     */
    public final int getIndexOfChild(final Object parent,
            final Object child) {
        PersonTreeNode parentNode = (PersonTreeNode) parent;
        if (parentNode.getPerson() != null) {
            return -1;
        }
        List allChildren = getAllChildren(parent);
        if (allChildren.size() == 0) {
            return -1;
        }
        return allChildren.indexOf(child);
    }

    /**
     * <p>Get whether or not this tree should include people, or just display
     * groups.</p>
     *
     * @return <code>true</code>if only groups containing users
     *     (or groups containing groups containing users) are included,
     *     otherwise <code>false</code>.
     */
    public final boolean getOnlyUsers() {
        return onlyUsers;
    }

    /**
     * <p>Get the root node of the tree.</p>
     *
     * @return the root node of the tree, as an instance of {@link
     *     GroupTreeModelRemote GroupTreeModelRemote}.
     */
    public final Object getRoot() {
        // the root element is always a group
        try {
            GroupDO root = addressBook.findGroupByPrimaryKey(securitySession, rootId);
            return new PersonTreeNode(root);
        } catch(SystemException e) {
            throw new RuntimeException("ERROR in group tree model: could not locate root with id '"
                + rootId
                + "'",
                e);
        }

    }

    /**
     * <p>Set the unique identifier of the root node.</p>
     *
     * @return unique identifier of the root node.
     */
    public final Integer getRootId() {
        return rootId;
    }

    /**
     * <p>Unless people are included in the tree, always returns
     * <code>false</code> since there are no leaf nodes in the group tree. If
     * people are included, returns <code>true</code> if this is a person node.</p>
     *
     * @param node this parameter is not used.
     * @return returns <code>true</code> if this is a person node, otherwise
     *     returns <code>false</code>.
     */
    public boolean isLeaf(final Object node) {
        PersonTreeNode parentNode = (PersonTreeNode) node;
        return parentNode.getPerson() != null;
    }

    /**
     * <p>Serialize the object from the input stream provided.</p>
     *
     * @param ois the input stream to serialize the object from
     * @throws IOException thrown by <code>ObjectInputStream.defaultReadObject()</code>.
     * @throws ClassNotFoundException thrown by <code>ObjectInputStream.defaultReadObject()</code>.
     */
    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    /**
     * <p>This interface method is not implemented for this class</p>
     *
     * @param l the listener to remove. Not usd in this class.
     */
    public void removeTreeModelListener(final TreeModelListener l) {
        throw new UnsupportedOperationException("Method removeTreeModelListener() not implemented.");
    }

    /**
     * <p>This parameter is used to set exclude a branch of the tree, beginning
     * with the id of the node provided. This is useful because you don't want a
     * node to appear in the tree to select its parent.</p>
     *
     * @param excludeBranchParam new value of branch to exclude, or null not to
     *     exclude any branch.
     */
    public final void setExcludeBranch(final Integer excludeBranchParam) {
        this.excludeBranch = excludeBranchParam;
    }

    /**
     * <p>This parameter is used to set a filter. For a full description, please
     * refer to the documentation of {@link GroupTreeModelBean
     * GroupTreeModelBean}.</p>
     *
     * @param filter new value of the filter to apply to the tree.
     */
    public final void setFilter(final UserRightFilter filter) {
        this.filter = filter;
    }

    /**
     * <p>Set whether or not this tree should include people, or just display
     * groups.</p>
     *
     * @param includePeople if <code>true</code>, then the tree will include
     *    people, otherwise (<code>false</code> or <code>null</code>) only
     *    groups are displayed.
     */
    public final void setIncludePeople(final boolean includePeople) {
        this.includePeople = includePeople;
    }

    /**
     * <p>Set whether or not this tree should only include users.</p>
     *
     * @param onlyUsers if <code>true</code>, then only groups containing users
     *     (or groups containing groups containing users) are included.
     */
    public final void setOnlyUsers(final boolean onlyUsers) {
        this.onlyUsers = onlyUsers;
    }

    /**
     * <p>Set the unique identifier of the root node.<p>
     * @param rootId unique identifier of the root node.
     */
    public final void setRootId(final Integer rootId) {
        this.rootId = rootId;
    }

    /**
     * <p>This interface method is not implemented for this class.</p>
     *
     * @param path path to the node that the user has altered.
     * @param newValue the new value from the TreeCellEditor.
     */
    public void valueForPathChanged(final TreePath path,
            final Object newValue) {
        throw new UnsupportedOperationException("Method valueForPathChanged() not implemented.");
    }

    /**
     * <p>Serialize the object to the output stream provided.</p>
     *
     * @param oos the output stream to serialize the object to
     * @throws IOException thrown by <code>ObjectOutputStream.defaultWriteObject()</code>
     */
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }
}
