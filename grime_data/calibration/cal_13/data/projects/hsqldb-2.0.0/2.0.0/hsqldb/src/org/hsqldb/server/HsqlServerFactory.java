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

package org.hsqldb.server;

import java.sql.SQLException;

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.HsqlException;
import org.hsqldb.jdbc.Util;
import org.hsqldb.persist.HsqlProperties;

// fredt@users 20020215 - patch 461556 by paul-h@users - modified
// minor changes to support the new HsqlServerProperties class
// boucherb@users 20030501 - Server now implements HsqlSocketRequestHandler

/**
 * HsqlServerFactory
 *
 * @author paul-h@users
 * @version 1.7.2
 * @since 1.7.0
 */
public class HsqlServerFactory {

    private HsqlServerFactory() {}

    public static HsqlSocketRequestHandler createHsqlServer(String dbFilePath,
            boolean debugMessages, boolean silentMode) throws SQLException {

        HsqlProperties props = new HsqlProperties();

        props.setProperty("server.database.0", dbFilePath);
        props.setProperty("server.trace", debugMessages);
        props.setProperty("server.silent", silentMode);

        Server server = new Server();

        try {
            server.setProperties(props);
        } catch (Exception e) {
            throw new SQLException("Failed to set server properties: " + e);
        }

        if (!server.openDatabases()) {
            Throwable t = server.getServerError();

            if (t instanceof HsqlException) {
                throw Util.sqlException((HsqlException) t);
            } else {

                throw Util.sqlException(Error.error(ErrorCode.GENERAL_ERROR));
            }
        }

        server.setState(ServerConstants.SERVER_STATE_ONLINE);

        // Server now implements HsqlSocketRequestHandler,
        // so there's really no need for HsqlSocketRequestHandlerImpl
        return server;
    }
}
