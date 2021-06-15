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
package com.ivata.groupware.business.addressbook.person.group.right;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * <p>Some classes access the system on the basis of user rights, and use this
 * class to filter the results according to the parameters given</p>
 *
 * <p>The parameters here are the same as for
 * {@link RightLocalHome#findTargetIdByUserNameAccessDetail
 * RightLocalHome.findTargetIdByUserNameAccessDetail}.</p>
 *
 * @since 2002-05-19
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @see RightLocalHome#findTargetIdByUserNameAccessDetail
 * @version $Revision: 1.2 $
 */
public class UserRightFilter implements Serializable {

    /**
     * <p>Default constructor.</p>
     */
    public UserRightFilter() {
        super();
    }

    /**
     * <p>Construct a user right filter from all its fields.</p>
     *
     * @param userName the user to filter results for. See {@link #setUserName
     *    setUserName}.
     * @param access the access type. See {@link #setAccess setAccess}.
     * @param detail the specific user right you want to filter against. See
     *    {@link #setDetail setDetail}.
     */
    public UserRightFilter(String userName, Integer access,
        Integer detail) {
        setUserName(userName);
        setAccess(access);
        setDetail(detail);
    }

    /**
     * <p>Stores the name of the user for whom the results will be filtered.</p>
     */
    private String userName = null;

    /**
     * <p>Stores the access type. This parameter should be set to one of the 'ACCESS_...'
     * constants in {@link RightConstants RightConstants}.</p>
     *
     * @see RightConstants
     */
    private Integer access = null;

    /**
     * <p>Get the access type. This parameter should be set to one of the 'ACCESS_...'
     * constants in {@link RightConstants RightConstants}.</p>
     *
     * @return the access type.
     * @see RightConstants
     */
    private Integer detail = null;

    /**
     * <p>Get the access type. This parameter should be set to one of the 'ACCESS_...'
     * constants in {@link RightConstants RightConstants}.</p>
     *
     * @return the value of the access type.
     * @see RightConstants
     */
    public final Integer getAccess() {
        return access;
    }

    /**
     * <p>Get the detail of the user right being checked. This parameter should
     * be set to one of the 'DETAIL_...' constants in
     * {@link RightConstants RightConstants}.</p>
     *
     * @return the user right detail.
     * @see RightConstants
     */
    public final Integer getDetail() {
        return detail;
    }

    /**
     * <p>Get the name of the user for whom the results will be filtered.</p>
     *
     * @return the name of the user for whom the results will be filtered.
     */
    public final String getUserName() {
        return userName;
    }

    /**
     * <p>Set the access type. This parameter should be set to one of the 'ACCESS_...'
     * constants in {@link RightConstants RightConstants}.</p>
     *
     * @param access the new value of the access type.
     * @see RightConstants
     */
    public final void setAccess(final Integer access) {
        this.access = access;
    }

    /**
     * <p>Set the detail of the user right being checked. This parameter should
     * be set to one of the 'DETAIL_...' constants in
     * {@link RightConstants RightConstants}.</p>
     *
     * @param detail the new value of the user right detail.
     * @see RightConstants
     */
    public final void setDetail(final Integer detail) {
        this.detail = detail;
    }

    /**
     * <p>Set the name of the user for whom the results will be filtered.</p>
     *
     * @param userName new name for the user for whom the results will
     *     be filtered.
     */
    public final void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * <p>Serialize the object to the output stream provided.</p>
     *
     * @exception IOException thrown by
     *     <code>ObjectOutputStream.defaultWriteObject(  )</code>
     * @param oos the output stream to serialize the object to
     */
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * <p>Serialize the object from the input stream provided.</p>
     *
     * @exception ClassNotFoundException thrown by
     *     <code>ObjectInputStream.defaultReadObject</code>.
     * @exception IOException thrown by
     *     <code>ObjectInputStream.defaultReadObject</code>.
     * @param ois the input stream to serialize the object from
     */
    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    /**
     * <p>Comparison method. See if the object supplied is a user right filter and,
     * if so, whether or not its contents are the same as this one.</p>
     *
     * @param compare the object to compare with this one.
     * @return <code>true</code> if the filter supplied in <code>compare</code>
     *     is effectively the same as this one, otherwise false.
     */
    public boolean equals(final Object compare) {
        // first check it is non-null and the class is right
        if ((compare == null) ||
            !(this.getClass().isInstance(compare))) {
            return false;
        }
        UserRightFilter filter = (UserRightFilter) compare;

        // check that the user names, access and detail are the same for both
        return (((userName == null) ?
                    (filter.userName == null) :
                    userName.equals(filter.userName)) &&
                ((access == null) ?
                    (filter.access == null) :
                    access.equals(filter.access)) &&
                ((detail == null) ?
                    (filter.detail == null) :
                    detail.equals(filter.detail)));
    }
}
