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

import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.Tokens;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.StringConverter;

/**
 * A User Object extends Grantee with password for a
 * particular database user.<p>
 *
 * @author Campbell Boucher-Burnett (boucherb@users dot sourceforge.net)
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 *
 * @version 1.9.0
 * @since 1.8.0
 */
public class User extends Grantee {

    /** password. */
    private String password;

    /** default schema when new Sessions started (defaults to PUBLIC schema) */
    private HsqlName initialSchema = null;

    /**
     * Constructor
     */
    User(HsqlName name, GranteeManager manager) {

        super(name, manager);

        if (manager != null) {
            updateAllRights();
        }
    }

    public String getSQL() {

        StringBuffer sb = new StringBuffer();

        sb.append(Tokens.T_CREATE).append(' ').append(Tokens.T_USER);
        sb.append(' ').append(granteeName.statementName).append(' ');
        sb.append(Tokens.T_PASSWORD).append(' ');
        sb.append('\'').append(password).append('\'');

        return sb.toString();
    }

    public long getChangeTimestamp() {
        return 0;
    }

    public void setPassword(String password) {

        /** @todo - introduce complexity interface */

        // checkComplexity(password);
        // requires: UserManager.createSAUser(), UserManager.createPublicUser()
        this.password = password;
    }

    /**
     * Checks if this object's password attibute equals
     * specified argument, else throws.
     */
    public void checkPassword(String value) {

        if (!value.equals(password)) {
            throw Error.error(ErrorCode.X_28000);
        }
    }

    /**
     * Returns the initial schema for the user
     */
    public HsqlName getInitialSchema() {
        return initialSchema;
    }

    public HsqlName getInitialOrDefaultSchema() {

        if (initialSchema != null) {
            return initialSchema;
        }

        HsqlName schema =
            granteeManager.database.schemaManager.findSchemaHsqlName(
                getNameString());

        if (schema == null) {
            return granteeManager.database.schemaManager
                .getDefaultSchemaHsqlName();
        } else {
            return schema;
        }
    }

    /**
     * This class does not have access to the SchemaManager, therefore
     * caller should verify that the given schemaName exists.
     *
     * @param schema An existing schema.  Null value allowed,
     *                   which means use the DB default session schema.
     */
    public void setInitialSchema(HsqlName schema) {
        initialSchema = schema;
    }

    /**
     * Returns the ALTER USER DDL character sequence that preserves the
     * this user's current password value and mode. <p>
     *
     * @return  the DDL
     */
    public String getAlterUserSQL() {

        StringBuffer sb = new StringBuffer();

        sb.append(Tokens.T_ALTER).append(' ');
        sb.append(Tokens.T_USER).append(' ');
        sb.append(getStatementName()).append(' ');
        sb.append(Tokens.T_SET).append(' ');
        sb.append(Tokens.T_PASSWORD).append(' ');
        sb.append('"').append(password).append('"');

        return sb.toString();
    }

    public String getInitialSchemaSQL() {

        StringBuffer sb = new StringBuffer();

        sb.append(Tokens.T_ALTER).append(' ');
        sb.append(Tokens.T_USER).append(' ');
        sb.append(getStatementName()).append(' ');
        sb.append(Tokens.T_SET).append(' ');
        sb.append(Tokens.T_INITIAL).append(' ');
        sb.append(Tokens.T_SCHEMA).append(' ');
        sb.append(initialSchema.statementName);

        return sb.toString();
    }

    /**
     * Returns the DDL string
     * sequence that creates this user.
     *
     */
    public String getCreateUserSQL() {

        StringBuffer sb = new StringBuffer(64);

        sb.append(Tokens.T_CREATE).append(' ');
        sb.append(Tokens.T_USER).append(' ');
        sb.append(getStatementName()).append(' ');
        sb.append(Tokens.T_PASSWORD).append(' ');
        sb.append(StringConverter.toQuotedString(password, '"', true));

        return sb.toString();
    }

    /**
     * Retrieves the redo log character sequence for connecting
     * this user
     *
     * @return the redo log character sequence for connecting
     *      this user
     */
    public String getConnectUserSQL() {

        StringBuffer sb = new StringBuffer();

        sb.append(Tokens.T_SET).append(' ');
        sb.append(Tokens.T_SESSION).append(' ');
        sb.append(Tokens.T_AUTHORIZATION).append(' ');
        sb.append(StringConverter.toQuotedString(getNameString(), '\'', true));

        return sb.toString();
    }
}
