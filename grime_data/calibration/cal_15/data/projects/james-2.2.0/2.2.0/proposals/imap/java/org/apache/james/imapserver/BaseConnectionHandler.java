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

package org.apache.james.imapserver;

import org.apache.avalon.cornerstone.services.connection.ConnectionHandler;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * THIS FILE RETRIEVED FROM THE MAIN TRUNK ATTIC, TO ENABLE IMAP PROPOSAL
 * TO COMPILE - EXTENDED BY o.a.james.imapserver.BaseCommand.
 * TODO: Use the AbstractJamesService for ImapServer, and get rid of this file.
 *
 * Different connection handlers extend this class
 * Common Connection Handler code could be factored into this class.
 * At present(April 28' 2001) there is not much in this class
 *
 */
public class BaseConnectionHandler extends AbstractLogEnabled implements Configurable {

    /**
     * The default timeout for the connection
     */
    private static int DEFAULT_TIMEOUT = 1800000;

    /**
     * The timeout for the connection
     */
    protected int timeout = DEFAULT_TIMEOUT;

    /**
     * The hello name for the connection
     */
    protected String helloName;

    /**
     * Get the hello name for this server
     *
     * @param configuration a configuration object containing server name configuration info
     * @return the hello name for this server
     */
    public static String configHelloName(final Configuration configuration)
        throws ConfigurationException {
        String hostName = null;

        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch  (UnknownHostException ue) {
            // Default to localhost if we can't get the local host name.
            hostName = "localhost";
        }

        Configuration helloConf = configuration.getChild("helloName");
        boolean autodetect = helloConf.getAttributeAsBoolean("autodetect", true);

        return autodetect ? hostName : helloConf.getValue("localhost");
    }


    /**
     * @see org.apache.avalon.framework.configuration.Configurable#configure(Configuration)
     */
    public void configure( final Configuration configuration )
        throws ConfigurationException {

        timeout = configuration.getChild( "connectiontimeout" ).getValueAsInteger( DEFAULT_TIMEOUT );
        helloName = configHelloName(configuration);
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Hello Name is: " + helloName);
        }
    }

    /**
     * Release a previously created ConnectionHandler e.g. for spooling.
     *
     * @param connectionHandler the ConnectionHandler to be released
     */
    public void releaseConnectionHandler(ConnectionHandler connectionHandler) {
    }
}
