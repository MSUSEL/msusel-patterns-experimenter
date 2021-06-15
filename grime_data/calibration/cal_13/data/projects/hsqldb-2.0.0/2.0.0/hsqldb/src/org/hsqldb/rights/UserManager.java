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

package org.hsqldb.rights;

import org.hsqldb.Database;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.Session;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.SchemaObject;
import org.hsqldb.SqlInvariants;

/**
 * Manages the User objects for a Database instance.
 * The special users PUBLIC_USER_NAME and SYSTEM_AUTHORIZATION_NAME
 * are created and managed here.  SYSTEM_AUTHORIZATION_NAME is also
 * special in that the name is not kept in the user "list"
 * (PUBLIC_USER_NAME is kept in the list because it's needed by MetaData
 * routines via "listVisibleUsers(x, true)").
 *
 * @author Campbell Boucher-Burnett (boucherb@users dot sourceforge.net)
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 *
 * @version 1.8.0
 * @since 1.7.2
 * @see  User
 */
public final class UserManager {

    /**
     * This object's set of User objects. <p>
     *
     * Note: The special _SYSTEM  role
     * is not included in this list but the special PUBLIC
     * User object is kept in the list because it's needed by MetaData
     * routines via "listVisibleUsers(x, true)".
     */
    private HashMappedList userList;
    private GranteeManager granteeManager;

    /**
     * Construction happens once for each Database instance.
     *
     * Creates special users PUBLIC_USER_NAME and SYSTEM_AUTHORIZATION_NAME.
     * Sets up association with the GranteeManager for this database.
     */
    public UserManager(Database database) {
        granteeManager = database.getGranteeManager();
        userList       = new HashMappedList();
    }

    /**
     * Creates a new User object under management of this object. <p>
     *
     *  A set of constraints regarding user creation is imposed: <p>
     *
     *  <OL>
     *    <LI>If the specified name is null, then an
     *        ASSERTION_FAILED exception is thrown stating that
     *        the name is null.
     *
     *    <LI>If this object's collection already contains an element whose
     *        name attribute equals the name argument, then
     *        a GRANTEE_ALREADY_EXISTS exception is thrown.
     *        (This will catch attempts to create Reserved grantee names).
     *  </OL>
     */
    public User createUser(HsqlName name, String password) {

        // This will throw an appropriate exception if grantee already exists,
        // regardless of whether the name is in any User, Role, etc. list.
        User user = granteeManager.addUser(name);

        user.setPassword(password);

        boolean success = userList.add(name.name, user);

        if (!success) {
            throw Error.error(ErrorCode.X_28503, name.statementName);
        }

        return user;
    }

    /**
     * Attempts to drop a User object with the specified name
     *  from this object's set. <p>
     *
     *  A successful drop action consists of: <p>
     *
     *  <UL>
     *
     *    <LI>removing the User object with the specified name
     *        from the set.
     *
     *    <LI>revoking all rights from the removed User<br>
     *        (this ensures that in case there are still references to the
     *        just dropped User object, those references
     *        cannot be used to erronously access database objects).
     *
     *  </UL> <p>
     *
     */
    public void dropUser(String name) {

        boolean reservedUser = GranteeManager.isReserved(name);

        if (reservedUser) {
            throw Error.error(ErrorCode.X_28502, name);
        }

        boolean result = granteeManager.removeGrantee(name);

        if (!result) {
            throw Error.error(ErrorCode.X_28501, name);
        }

        User user = (User) userList.remove(name);

        if (user == null) {
            throw Error.error(ErrorCode.X_28501, name);
        }
    }

    public void createFirstUser(String username, String password) {

        boolean isQuoted = true;
        if (username.equalsIgnoreCase("SA")) {
            username = "SA";
            isQuoted = false;
        }

        HsqlName name =
            granteeManager.database.nameManager.newHsqlName(username, isQuoted,
                SchemaObject.GRANTEE);

        createUser(name, password);
        granteeManager.grant(name.name, SqlInvariants.DBA_ADMIN_ROLE_NAME,
                             granteeManager.getDBARole());
    }

    /**
     * Returns the User object with the specified name and
     * password from this object's set.
     */
    public User getUser(String name, String password) {

        if (name == null) {
            name = "";
        }

        if (password == null) {
            password = "";
        }

        User user = get(name);

        user.checkPassword(password);

        return user;
    }

    /**
     * Retrieves this object's set of User objects as
     *  an associative list.
     */
    public HashMappedList getUsers() {
        return userList;
    }

    public boolean exists(String name) {
        return userList.get(name) == null ? false
                                          : true;
    }

    /**
     * Returns the User object identified by the
     * name argument.
     */
    public User get(String name) {

        User user = (User) userList.get(name);

        if (user == null) {
            throw Error.error(ErrorCode.X_28501, name);
        }

        return user;
    }

    /**
     * Retrieves the <code>User</code> objects representing the database
     * users that are visible to the <code>User</code> object
     * represented by the <code>session</code> argument. <p>
     *
     * If the <code>session</code> argument's <code>User</code> object
     * attribute has isAdmin() true (directly or by virtue of a Role),
     * then all of the
     * <code>User</code> objects in this collection are considered visible.
     * Otherwise, only this object's special <code>PUBLIC</code>
     * <code>User</code> object attribute and the session <code>User</code>
     * object, if it exists in this collection, are considered visible. <p>
     *
     * @param session The <code>Session</code> object used to determine
     *          visibility
     * @return a list of <code>User</code> objects visible to
     *          the <code>User</code> object contained by the
     *         <code>session</code> argument.
     *
     */
    public HsqlArrayList listVisibleUsers(Session session) {

        HsqlArrayList list;
        User          user;
        boolean       isAdmin;
        String        sessionName;
        String        userName;

        list        = new HsqlArrayList();
        isAdmin     = session.isAdmin();
        sessionName = session.getUsername();

        if (userList == null || userList.size() == 0) {
            return list;
        }

        for (int i = 0; i < userList.size(); i++) {
            user = (User) userList.get(i);

            if (user == null) {
                continue;
            }

            userName = user.getNameString();

            if (isAdmin) {
                list.add(user);
            } else if (sessionName.equals(userName)) {
                list.add(user);
            }
        }

        return list;
    }

    /**
     * Returns the specially constructed
     * <code>SYSTEM_AUTHORIZATION_NAME</code>
     * <code>User</code> object for the current <code>Database</code> object.
     *
     * @return the <code>SYS_AUTHORIZATION_NAME</code>
     *          <code>User</code> object
     *
     */
    public User getSysUser() {
        return GranteeManager.systemAuthorisation;
    }

    public synchronized void removeSchemaReference(String schemaName) {

        for (int i = 0; i < userList.size(); i++) {
            User     user   = (User) userList.get(i);
            HsqlName schema = user.getInitialSchema();

            if (schema == null) {
                continue;
            }

            if (schemaName.equals(schema.name)) {
                user.setInitialSchema(null);
            }
        }
    }

    public String[] getInitialSchemaSQL() {

        HsqlArrayList list = new HsqlArrayList(userList.size());

        for (int i = 0; i < userList.size(); i++) {
            User user = (User) userList.get(i);

            if (user.isSystem) {
                continue;
            }

            HsqlName name = user.getInitialSchema();

            if (name == null) {
                continue;
            }

            list.add(user.getInitialSchemaSQL());
        }

        String[] array = new String[list.size()];

        list.toArray(array);

        return array;
    }
}
