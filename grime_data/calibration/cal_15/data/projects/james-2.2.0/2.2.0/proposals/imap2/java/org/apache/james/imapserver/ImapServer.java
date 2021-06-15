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
import org.apache.avalon.excalibur.pool.DefaultPool;
import org.apache.avalon.excalibur.pool.HardResourceLimitingPool;
import org.apache.avalon.excalibur.pool.ObjectFactory;
import org.apache.avalon.excalibur.pool.Pool;
import org.apache.avalon.excalibur.pool.Poolable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.james.core.AbstractJamesService;
import org.apache.james.services.MailServer;
import org.apache.james.services.UsersRepository;
import org.apache.james.services.UsersStore;
import org.apache.james.util.watchdog.Watchdog;
import org.apache.james.util.watchdog.WatchdogFactory;

/**
 * TODO: this is a quick cut-and-paste hack from POP3Server. Should probably be
 * rewritten from scratch, together with ImapHandler.
 *
 * <p>Accepts IMAP connections on a server socket and dispatches them to IMAPHandlers.</p>
 *
 * <p>Also responsible for loading and parsing IMAP specific configuration.</p>
 *
 * @version 1.0.0, 24/04/1999
 */
public class ImapServer extends AbstractJamesService
{

    /**
     * The internal mail server service
     */
    private MailServer mailServer;

    /**
     * The user repository for this server - used to authenticate users.
     */
    private UsersRepository users;

    /**
     * The ImapHost for this server - used for all mail storage.
     */
    private ImapHost imapHost;

    /**
     * The number of bytes to read before resetting
     * the connection timeout timer.  Defaults to
     * 20 KB.
     */
    private int lengthReset = 20 * 1024;

    /**
     * The pool used to provide IMAP Handler objects
     */
    private Pool theHandlerPool = null;

    /**
     * The factory used to provide IMAP Handler objects
     */
    private ObjectFactory theHandlerFactory = new IMAPHandlerFactory();

    /**
     * The factory used to generate Watchdog objects
     */
    private WatchdogFactory theWatchdogFactory;

    /**
     * The configuration data to be passed to the handler
     */
    private ImapHandlerConfigurationData theConfigData
            = new IMAPHandlerConfigurationDataImpl();

    /**
     * @see Composable#compose(ComponentManager)
     */
    public void compose( final ComponentManager componentManager )
            throws ComponentException
    {
        super.compose( componentManager );
        mailServer = ( MailServer ) componentManager.
                lookup( "org.apache.james.services.MailServer" );
        UsersStore usersStore = ( UsersStore ) componentManager.
                lookup( "org.apache.james.services.UsersStore" );
        users = usersStore.getRepository( "LocalUsers" );
        imapHost = ( ImapHost ) componentManager.
                lookup( "org.apache.james.imapserver.ImapHost" );
        if ( users == null ) {
            throw new ComponentException( "The user repository could not be found." );
        }
    }

    /**
     * @see org.apache.avalon.framework.configuration.Configurable#configure(Configuration)
     */
    public void configure( final Configuration configuration ) throws ConfigurationException
    {
        super.configure( configuration );
        if ( isEnabled() ) {
            Configuration handlerConfiguration = configuration.getChild( "handler" );
            lengthReset = handlerConfiguration.getChild( "lengthReset" ).getValueAsInteger( lengthReset );
            if ( getLogger().isInfoEnabled() ) {
                getLogger().info( "The idle timeout will be reset every " + lengthReset + " bytes." );
            }
        }
    }

    /**
     * @see Initializable#initialize()
     */
    public void initialize() throws Exception
    {
        super.initialize();
        if ( !isEnabled() ) {
            return;
        }

        if ( connectionLimit != null ) {
            theHandlerPool = new HardResourceLimitingPool( theHandlerFactory, 5, connectionLimit.intValue() );
            getLogger().debug( "Using a bounded pool for IMAP handlers with upper limit " + connectionLimit.intValue() );
        }
        else {
            // NOTE: The maximum here is not a real maximum.  The handler pool will continue to
            //       provide handlers beyond this value.
            theHandlerPool = new DefaultPool( theHandlerFactory, null, 5, 30 );
            getLogger().debug( "Using an unbounded pool for IMAP handlers." );
        }
        if ( theHandlerPool instanceof LogEnabled ) {
            ( ( LogEnabled ) theHandlerPool ).enableLogging( getLogger() );
        }
        if ( theHandlerPool instanceof Initializable ) {
            ( ( Initializable ) theHandlerPool ).initialize();
        }

        theWatchdogFactory = getWatchdogFactory();
    }

    /**
     * @see AbstractJamesService#getDefaultPort()
     */
    protected int getDefaultPort()
    {
        return 110;
    }

    /**
     * @see AbstractJamesService#getServiceType()
     */
    public String getServiceType()
    {
        return "IMAP Service";
    }

    /**
     * @see org.apache.avalon.cornerstone.services.connection.AbstractHandlerFactory#newHandler()
     */
    protected ConnectionHandler newHandler()
            throws Exception
    {
        ImapHandler theHandler = ( ImapHandler ) theHandlerPool.get();

        Watchdog theWatchdog = theWatchdogFactory.getWatchdog( theHandler.getWatchdogTarget() );

        theHandler.setConfigurationData( theConfigData );

        theHandler.setWatchdog( theWatchdog );

        return theHandler;
    }

    /**
     * @see org.apache.avalon.cornerstone.services.connection.ConnectionHandlerFactory#releaseConnectionHandler(ConnectionHandler)
     */
    public void releaseConnectionHandler( ConnectionHandler connectionHandler )
    {
        if ( !( connectionHandler instanceof ImapHandler ) ) {
            throw new IllegalArgumentException( "Attempted to return non-ImapHandler to pool." );
        }
        theHandlerPool.put( ( Poolable ) connectionHandler );
    }

    /**
     * The factory for producing handlers.
     */
    private static class IMAPHandlerFactory
            implements ObjectFactory
    {

        /**
         * @see ObjectFactory#newInstance()
         */
        public Object newInstance() throws Exception
        {
            return new ImapHandler();
        }

        /**
         * @see ObjectFactory#getCreatedClass()
         */
        public Class getCreatedClass()
        {
            return ImapHandler.class;
        }

        /**
         * @see ObjectFactory#decommission(Object)
         */
        public void decommission( Object object ) throws Exception
        {
            return;
        }
    }

    /**
     * A class to provide POP3 handler configuration to the handlers
     */
    private class IMAPHandlerConfigurationDataImpl
            implements ImapHandlerConfigurationData
    {

        /**
         * @see ImapHandlerConfigurationData#getHelloName()
         */
        public String getHelloName()
        {
            return ImapServer.this.helloName;
        }

        /**
         * @see ImapHandlerConfigurationData#getResetLength()
         */
        public int getResetLength()
        {
            return ImapServer.this.lengthReset;
        }

        /**
         * @see ImapHandlerConfigurationData#getMailServer()
         */
        public MailServer getMailServer()
        {
            return ImapServer.this.mailServer;
        }

        /**
         * @see ImapHandlerConfigurationData#getUsersRepository()
         */
        public UsersRepository getUsersRepository()
        {
            return ImapServer.this.users;
        }

        /** @see ImapHandlerConfigurationData#getImapHost */
        public ImapHost getImapHost()
        {
            return ImapServer.this.imapHost;
        }
    }
}
