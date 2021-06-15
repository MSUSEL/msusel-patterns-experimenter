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

/**
 * An enumeration of the property keys and default property values used by
 * HSQLDB servers
 *
 * @author  boucherb@users
 * @version 1.9.0
 * @since 1.7.2
 */
public interface ServerConstants {

    // server states
    int SERVER_STATE_ONLINE   = 1;
    int SERVER_STATE_OPENING  = 4;
    int SERVER_STATE_CLOSING  = 8;
    int SERVER_STATE_SHUTDOWN = 16;
    int SC_DATABASE_SHUTDOWN  = 0;

    // use default address for server socket
    String SC_DEFAULT_ADDRESS = "0.0.0.0";

    // default database name if non specified
    String SC_DEFAULT_DATABASE = "test";

    // default port for each protocol
    int SC_DEFAULT_HSQL_SERVER_PORT  = 9001;
    int SC_DEFAULT_HSQLS_SERVER_PORT = 554;
    int SC_DEFAULT_HTTP_SERVER_PORT  = 80;
    int SC_DEFAULT_HTTPS_SERVER_PORT = 443;
    int SC_DEFAULT_BER_SERVER_PORT   = 9101;

    // operation modes
    boolean SC_DEFAULT_SERVER_AUTORESTART = false;
    boolean SC_DEFAULT_NO_SYSTEM_EXIT     = true;
    boolean SC_DEFAULT_SILENT             = true;
    boolean SC_DEFAULT_TLS                = false;
    boolean SC_DEFAULT_TRACE              = false;
    boolean SC_DEFAULT_REMOTE_OPEN_DB     = false;
    int     SC_DEFAULT_MAX_DATABASES      = 10;

    // type of server
    int SC_PROTOCOL_HTTP = 0;
    int SC_PROTOCOL_HSQL = 1;
    int SC_PROTOCOL_BER  = 2;

    // keys to properties
    String SC_KEY_PREFIX             = "server";
    String SC_KEY_ADDRESS            = SC_KEY_PREFIX + ".address";
    String SC_KEY_AUTORESTART_SERVER = SC_KEY_PREFIX + ".restart_on_shutdown";
    String SC_KEY_DATABASE           = SC_KEY_PREFIX + ".database";
    String SC_KEY_DBNAME             = SC_KEY_PREFIX + ".dbname";
    String SC_KEY_NO_SYSTEM_EXIT     = SC_KEY_PREFIX + ".no_system_exit";
    String SC_KEY_PORT               = SC_KEY_PREFIX + ".port";
    String SC_KEY_SILENT             = SC_KEY_PREFIX + ".silent";
    String SC_KEY_TLS                = SC_KEY_PREFIX + ".tls";
    String SC_KEY_TRACE              = SC_KEY_PREFIX + ".trace";
    String SC_KEY_DAEMON             = SC_KEY_PREFIX + ".daemon";
    String SC_KEY_WEB_DEFAULT_PAGE   = SC_KEY_PREFIX + ".default_page";
    String SC_KEY_WEB_ROOT           = SC_KEY_PREFIX + ".root";
    String SC_KEY_MAX_CONNECTIONS    = SC_KEY_PREFIX + ".maxconnections";
    String SC_KEY_REMOTE_OPEN_DB     = SC_KEY_PREFIX + ".remote_open";
    String SC_KEY_MAX_DATABASES      = SC_KEY_PREFIX + ".maxdatabases";
    String SC_KEY_ACL_FILEPATH       = SC_KEY_PREFIX + ".acl_filepath";

    // web server page defaults
    String SC_DEFAULT_WEB_MIME = "text/html";
    String SC_DEFAULT_WEB_PAGE = "index.html";
    String SC_DEFAULT_WEB_ROOT = ".";
}
