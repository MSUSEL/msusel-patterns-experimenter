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
package org.hibernate.ejb.internal;

import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.logging.Cause;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;

import org.hibernate.internal.CoreMessageLogger;

import static org.jboss.logging.Logger.Level.DEBUG;
import static org.jboss.logging.Logger.Level.ERROR;
import static org.jboss.logging.Logger.Level.INFO;
import static org.jboss.logging.Logger.Level.WARN;

/**
 * The jboss-logging {@link MessageLogger} for the hibernate-entitymanager module.  It reserves message ids ranging from
 * 15001 to 20000 inclusively.
 * <p/>
 * New messages must be added after the last message defined to ensure message codes are unique.
 */
@MessageLogger( projectCode = "HHH" )
public interface EntityManagerMessageLogger extends CoreMessageLogger {

    @LogMessage( level = INFO )
    @Message( value = "Bound Ejb3Configuration to JNDI name: %s", id = 15001 )
    void boundEjb3ConfigurationToJndiName( String name );

    @LogMessage( level = INFO )
    @Message( value = "Ejb3Configuration name: %s", id = 15002 )
    void ejb3ConfigurationName( String name );

    @LogMessage( level = INFO )
    @Message( value = "An Ejb3Configuration was renamed from name: %s", id = 15003 )
    void ejb3ConfigurationRenamedFromName( String name );

    @LogMessage( level = INFO )
    @Message( value = "An Ejb3Configuration was unbound from name: %s", id = 15004 )
    void ejb3ConfigurationUnboundFromName( String name );

    @LogMessage( level = WARN )
    @Message( value = "Exploded jar file does not exist (ignored): %s", id = 15005 )
    void explodedJarDoesNotExist( URL jarUrl );

    @LogMessage( level = WARN )
    @Message( value = "Exploded jar file not a directory (ignored): %s", id = 15006 )
    void explodedJarNotDirectory( URL jarUrl );

    @LogMessage( level = ERROR )
    @Message( value = "Illegal argument on static metamodel field injection : %s#%s; expected type :  %s; encountered type : %s", id = 15007 )
    void illegalArgumentOnStaticMetamodelFieldInjection( String name,
                                                         String name2,
                                                         String name3,
                                                         String name4 );

    @LogMessage( level = ERROR )
    @Message( value = "Malformed URL: %s", id = 15008 )
    void malformedUrl( URL jarUrl,
                       @Cause URISyntaxException e );

    @LogMessage( level = WARN )
    @Message( value = "Malformed URL: %s", id = 15009 )
    void malformedUrlWarning( URL jarUrl,
                              @Cause URISyntaxException e );

    @LogMessage( level = WARN )
    @Message( value = "Unable to find file (ignored): %s", id = 15010 )
    void unableToFindFile( URL jarUrl,
                           @Cause Exception e );

    @LogMessage( level = ERROR )
    @Message( value = "Unable to locate static metamodel field : %s#%s", id = 15011 )
    void unableToLocateStaticMetamodelField( String name,
                                             String name2 );

    @LogMessage( level = INFO )
    @Message( value = "Using provided datasource", id = 15012 )
    void usingProvidedDataSource();


	@LogMessage( level = DEBUG )
	@Message( value = "Returning null (as required by JPA spec) rather than throwing EntityNotFoundException, " +
			"as the entity (type=%s, id=%s) does not exist", id = 15013 )
	void ignoringEntityNotFound( String entityName, String identifier);

}
