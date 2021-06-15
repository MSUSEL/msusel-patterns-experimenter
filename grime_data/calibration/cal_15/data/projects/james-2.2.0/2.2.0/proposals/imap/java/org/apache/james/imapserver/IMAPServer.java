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

import org.apache.avalon.cornerstone.services.connection.AbstractService;
import org.apache.avalon.cornerstone.services.connection.ConnectionHandlerFactory;
import org.apache.avalon.cornerstone.services.connection.DefaultHandlerFactory;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.component.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The Server listens on a specified port and passes connections to a
 * ConnectionHandler. In this implementation, each ConnectionHandler runs in
 * its own thread.
 *
 * @version 0.2 on 04 Aug 2002
 */
public class IMAPServer 
    extends AbstractService implements Component {

    private Context _context;

    protected ConnectionHandlerFactory createFactory()
    {
        return new DefaultHandlerFactory( SingleThreadedConnectionHandler.class );
    }

    public void configure( final Configuration configuration )
        throws ConfigurationException {

        m_port = configuration.getChild( "port" ).getValueAsInteger( 143 );

        try 
        { 
            final String bindAddress = configuration.getChild( "bind" ).getValue( null );
            if( null != bindAddress )
            {
                m_bindTo = InetAddress.getByName( bindAddress ); 
            }
        }
        catch( final UnknownHostException unhe ) 
        {
            throw new ConfigurationException( "Malformed bind parameter", unhe );
        }

        final String useTLS = configuration.getChild("useTLS").getValue( "" );
        if( useTLS.equals( "TRUE" ) ) m_serverSocketType = "ssl";

        super.configure( configuration.getChild( "handler" ) );
    }

    public void initialize() throws Exception {

        getLogger().info( "IMAPServer init..." );
        getLogger().info( "IMAPListener using " + m_serverSocketType + " on port " + m_port );
        super.initialize();
        getLogger().info("IMAPServer ...init end");
        System.out.println("Started IMAP Server "+m_connectionName);
    }
}
    
