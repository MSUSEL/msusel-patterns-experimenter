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
package org.hibernate.service.jdbc.connections.internal;

import org.jboss.logging.LogMessage;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;

import org.hibernate.internal.CoreMessageLogger;

import static org.jboss.logging.Logger.Level.INFO;

/**
 * The jboss-logging {@link MessageLogger} for the hibernate-proxool module.  It reserves message ids ranging from
 * 30001 to 35000 inclusively.
 * <p/>
 * New messages must be added after the last message defined to ensure message codes are unique.
 */
@MessageLogger( projectCode = "HHH" )
public interface ProxoolMessageLogger extends CoreMessageLogger {

    @LogMessage( level = INFO )
    @Message( value = "Autocommit mode: %s", id = 30001 )
    void autoCommmitMode( boolean autocommit );

    @LogMessage( level = INFO )
    @Message( value = "Configuring Proxool Provider to use pool alias: %s", id = 30002 )
    void configuringProxoolProviderToUsePoolAlias( String proxoolAlias );

    @LogMessage( level = INFO )
    @Message( value = "Configuring Proxool Provider using existing pool in memory: %s", id = 30003 )
    void configuringProxoolProviderUsingExistingPool( String proxoolAlias );

    @LogMessage( level = INFO )
    @Message( value = "Configuring Proxool Provider using JAXPConfigurator: %s", id = 30004 )
    void configuringProxoolProviderUsingJaxpConfigurator( String jaxpFile );

    @LogMessage( level = INFO )
    @Message( value = "Configuring Proxool Provider using Properties File: %s", id = 30005 )
    void configuringProxoolProviderUsingPropertiesFile( String proxoolAlias );

    @Message( value = "Exception occured when closing the Proxool pool", id = 30006 )
    String exceptionClosingProxoolPool();

    @Message( value = "Cannot configure Proxool Provider to use an existing in memory pool without the %s property set.", id = 30007 )
    String unableToConfigureProxoolProviderToUseExistingInMemoryPool( String proxoolPoolAlias );

    @Message( value = "Cannot configure Proxool Provider to use JAXP without the %s property set.", id = 30008 )
    String unableToConfigureProxoolProviderToUseJaxp( String proxoolPoolAlias );

    @Message( value = "Cannot configure Proxool Provider to use Properties File without the %s property set.", id = 30009 )
    String unableToConfigureProxoolProviderToUsePropertiesFile( String proxoolPoolAlias );

    @Message( value = "Proxool Provider unable to load JAXP configurator file: %s", id = 30010 )
    String unableToLoadJaxpConfiguratorFile( String jaxpFile );

    @Message( value = "Proxool Provider unable to load Property configurator file: %s", id = 30011 )
    String unableToLoadPropertyConfiguratorFile( String propFile );
}
