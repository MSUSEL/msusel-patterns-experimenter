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

import java.net.InetAddress;

import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.StringUtil;
import org.hsqldb.persist.HsqlProperties;

/**
 * @todo:  move to here from Server and WebServer the remaining extraneous code
 *          dealing primarily with reading/setting properties from files, etc.
 */

/**
 * Assists with Server and WebServer configuration tasks.
 *
 * @author  boucherb@users
 * @version 1.9.0
 * @since 1.7.2
 */
public final class ServerConfiguration implements ServerConstants {

    private ServerConfiguration() {}

    /**
     * Retrieves the default port that a Server will try to use in the
     * abscence of an explicitly specified one, given the specified
     * value for whether or not to use secure sockets.
     *
     * @param protocol the protcol specifier code of the Server
     * @param isTls if true, retrieve the default port when using secure
     *      sockets, else the default port when using plain sockets
     * @return the default port used in the abscence of an explicit
     *      specification.
     *
     */
    public static int getDefaultPort(int protocol, boolean isTls) {

        switch (protocol) {

            case SC_PROTOCOL_HSQL : {
                return isTls ? SC_DEFAULT_HSQLS_SERVER_PORT
                             : SC_DEFAULT_HSQL_SERVER_PORT;
            }
            case SC_PROTOCOL_HTTP : {
                return isTls ? SC_DEFAULT_HTTPS_SERVER_PORT
                             : SC_DEFAULT_HTTP_SERVER_PORT;
            }
            case SC_PROTOCOL_BER : {
                return isTls ? -1
                             : SC_DEFAULT_BER_SERVER_PORT;
            }
            default : {
                return -1;
            }
        }
    }

    /**
     * Retrieves a new HsqlProperties object, if possible, loaded from the
     * specified file.
     *
     * @param path the file's path, without the .properties extention
     *      (which is added automatically)
     * @return a new properties object loaded from the specified file
     */
    public static ServerProperties getPropertiesFromFile(int protocol,
            String path) {

        if (StringUtil.isEmpty(path)) {
            return null;
        }

        ServerProperties p = new ServerProperties(protocol, path);

        try {
            p.load();
        } catch (Exception e) {}

        return p;
    }

    /**
     * Retrieves an array of Strings naming the distinct, known to be valid local
     * InetAddress names for this machine.  The process is to collect and
     * return the union of the following sets:
     *
     * <ol>
     * <li> InetAddress.getAllByName(InetAddress.getLocalHost().getHostAddress())
     * <li> InetAddress.getAllByName(InetAddress.getLocalHost().getHostName())
     * <li> InetAddress.getAllByName(InetAddress.getByName(null).getHostAddress())
     * <li> InetAddress.getAllByName(InetAddress.getByName(null).getHostName())
     * <li> InetAddress.getByName("loopback").getHostAddress()
     * <li> InetAddress.getByName("loopback").getHostname()
     * </ol>
     *
     * @return the distinct, known to be valid local
     *        InetAddress names for this machine
     */
    public static String[] listLocalInetAddressNames() {

        InetAddress   addr;
        InetAddress[] addrs;
        HashSet       set;

        set = new HashSet();

        try {
            addr  = InetAddress.getLocalHost();
            addrs = InetAddress.getAllByName(addr.getHostAddress());

            for (int i = 0; i < addrs.length; i++) {
                set.add(addrs[i].getHostAddress());
                set.add(addrs[i].getHostName());
            }

            addrs = InetAddress.getAllByName(addr.getHostName());

            for (int i = 0; i < addrs.length; i++) {
                set.add(addrs[i].getHostAddress());
                set.add(addrs[i].getHostName());
            }
        } catch (Exception e) {}

        try {
            addr  = InetAddress.getByName(null);
            addrs = InetAddress.getAllByName(addr.getHostAddress());

            for (int i = 0; i < addrs.length; i++) {
                set.add(addrs[i].getHostAddress());
                set.add(addrs[i].getHostName());
            }

            addrs = InetAddress.getAllByName(addr.getHostName());

            for (int i = 0; i < addrs.length; i++) {
                set.add(addrs[i].getHostAddress());
                set.add(addrs[i].getHostName());
            }
        } catch (Exception e) {}

        try {
            set.add(InetAddress.getByName("loopback").getHostAddress());
            set.add(InetAddress.getByName("loopback").getHostName());
        } catch (Exception e) {}

        return (String[]) set.toArray(new String[set.size()]);
    }

    /**
     * Retrieves a new default properties object for a server of the
     * specified protocol
     *
     * @return a new default properties object
     */
    public static ServerProperties newDefaultProperties(int protocol) {

        ServerProperties p = new ServerProperties(protocol);

        p.setProperty(SC_KEY_AUTORESTART_SERVER,
                      SC_DEFAULT_SERVER_AUTORESTART);
        p.setProperty(SC_KEY_ADDRESS, SC_DEFAULT_ADDRESS);
        p.setProperty(SC_KEY_NO_SYSTEM_EXIT, SC_DEFAULT_NO_SYSTEM_EXIT);
        p.setProperty(SC_KEY_MAX_DATABASES, SC_DEFAULT_MAX_DATABASES);
        p.setProperty(SC_KEY_SILENT, SC_DEFAULT_SILENT);
        p.setProperty(SC_KEY_TLS, SC_DEFAULT_TLS);
        p.setProperty(SC_KEY_TRACE, SC_DEFAULT_TRACE);
        p.setProperty(SC_KEY_WEB_DEFAULT_PAGE, SC_DEFAULT_WEB_PAGE);
        p.setProperty(SC_KEY_WEB_ROOT, SC_DEFAULT_WEB_ROOT);

        // Purposefully do not set a default Port because the default is
        // derived from TLS, which is runtime-configurable.
        // Things work very well if we leave it unset here and use the
        // getDefaultPort() method above to get the correct value.
        return p;
    }

    /**
     * Translates null or zero length value for address key to the
     * special value ServerConstants.SC_DEFAULT_ADDRESS which causes
     * ServerSockets to be constructed without specifying an InetAddress.
     *
     * @param p The properties object upon which to perform the translation
     */
    public static void translateAddressProperty(HsqlProperties p) {

        if (p == null) {
            return;
        }

        String address = p.getProperty(SC_KEY_ADDRESS);

        if (StringUtil.isEmpty(address)) {
            p.setProperty(SC_KEY_ADDRESS, SC_DEFAULT_ADDRESS);
        }
    }

    /**
     * Translates the legacy default database form: database=...
     * to the 1.7.2 form: database.0=...
     *
     * @param p The properties object upon which to perform the translation
     */
    public static void translateDefaultDatabaseProperty(HsqlProperties p) {

        if (p == null) {
            return;
        }

        if (!p.isPropertyTrue(SC_KEY_REMOTE_OPEN_DB)) {
            if (p.getProperty(SC_KEY_DATABASE + "." + 0) == null) {
                String defaultdb = p.getProperty(SC_KEY_DATABASE);

                if (defaultdb == null) {
                    defaultdb = SC_DEFAULT_DATABASE;
                }

                p.setProperty(SC_KEY_DATABASE + ".0", defaultdb);
                p.setProperty(SC_KEY_DBNAME + ".0", "");
            }

            if (p.getProperty(SC_KEY_DBNAME + "." + 0) == null) {
                p.setProperty(SC_KEY_DBNAME + ".0", "");
            }
        }
    }

    /**
     * Tranlates unspecified no_system_exit property to false, the default
     * typically required when a Server is started from the command line.
     *
     * @param p The properties object upon which to perform the translation
     */
    public static void translateDefaultNoSystemExitProperty(HsqlProperties p) {

        if (p == null) {
            return;
        }

        p.setPropertyIfNotExists(SC_KEY_NO_SYSTEM_EXIT, "false");
    }
}
