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

import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.web.tree.TreeNode;


/**
 * <p>Stores one node of a tree which can either contain just groups or groups
 * and people.</p>
 *
 * <p>If this is a node representing a group, the person element will be
 * <code>null</code>, otherwise the group element will contain the main group
 * details of the person represented by the node.</p>
 *
 * @since  2002-08-26
 * @author Jan Boros <janboros@sourceforge.net>
 * @version $Revision: 1.4 $
 */
public class PersonTreeNode implements Serializable, TreeNode {

    /**
     * <p>Stores the group data for this node as a data object.</p>
     */
    private GroupDO group;

    /**
     * <p>Stores the person data for this node as a data object.</p>
     */
    private PersonDO person;

    /**
     * <p>Default contructor.</p>
     */
    public PersonTreeNode() {}
    /**
     * Create a person tree node which represents a group of people.
     *
     * @param group group of people this tree node represents.
     */
    public PersonTreeNode(GroupDO group) {
        this.group = group;
    }

    /**
     * Create a person tree node which represents a person.
     *
     * @param person person this tree node represents.
     */
    public PersonTreeNode(PersonDO person) {
        this.person = person;
    }

    /**
     * <p>Get the group data for this node as a data object. If this
     * node represents a group, this gets the values of that group. Otherwise,
     * this gets the values of the main group associated with the person this
     * node represents.</p>
     *
     * @return the data of the group this node represents.
     *     In the case of the node representing a person, this method gets the
     *     details of that person's main group.
     */
    public final GroupDO getGroup() {
        return group;
    }

    /**
     * <p>Get the unique identifier for this tree node.</p>
     *
     * @return if the node represents a group, return the unique identifier for
     *     that group. Otherwise the <em>negative</em> value of the person's unique
     *     identifier is returned.
     */
    public final Integer getId() {
        if (person == null) {
            return group.getId();
        }
        return new Integer(0 - person.getId().intValue());
    }
    /**
     * <p>The name of this tree node is either the value of the
     * <code>fileAs<code> attribute of the person, or the <code>name</code>
     * of the group depending which this tree node represents.</p>
     *
     * @return if this is a person, the <code>fileAs</code> value for that
     *     person, otherwise the <code>name</code> of the group.
     */
    public final String getName() {
        return (person != null) ? person.getFileAs() : group.getName();
    }

    /**
     * <p>Get the person data for this node as a data object.</p>
     *
     * @return values of the person this node represents, or
     *     <code>null</code> if this node represents a group.
     */
    public PersonDO getPerson() {
        return person;
    }

    /**
     * <p>Serialize the object from the input stream provided.</p>
     *
     * @param ois the input stream to serialize the object from
     * @throws IOException thrown by <code>ObjectInputStream.defaultReadObject(  )</code>.
     * @throws ClassNotFoundException thrown by <code>ObjectInputStream.defaultReadObject(  )</code>.
     */
    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    /**
     * <p>Serialize the object to the output stream provided.</p>
     *
     * @param oos the output stream to serialize the object to
     * @throws IOException thrown by <code>ObjectOutputStream.defaultWriteObject(  )</code>
     */
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }
}
