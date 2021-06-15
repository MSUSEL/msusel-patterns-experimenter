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

import java.sql.SQLException;

import org.jboss.logging.Cause;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;

import org.hibernate.internal.CoreMessageLogger;

import static org.jboss.logging.Logger.Level.INFO;
import static org.jboss.logging.Logger.Level.WARN;

/**
 * The jboss-logging {@link MessageLogger} for the hibernate-c3p0 module.  It reserves message ids ranging from
 * 10001 to 15000 inclusively.
 * <p/>
 * New messages must be added after the last message defined to ensure message codes are unique.
 */
@MessageLogger( projectCode = "HHH" )
public interface C3P0MessageLogger extends CoreMessageLogger {

    @LogMessage( level = WARN )
    @Message( value = "Both hibernate-style property '%s' and c3p0-style property '%s' have been set in hibernate.properties. "
                      + "Hibernate-style property '%s' will be used and c3p0-style property '%s' will be ignored!", id = 10001 )
    void bothHibernateAndC3p0StylesSet( String hibernateStyle,
                                        String c3p0Style,
                                        String hibernateStyle2,
                                        String c3p0Style2 );

    @LogMessage( level = INFO )
    @Message( value = "C3P0 using driver: %s at URL: %s", id = 10002 )
    void c3p0UsingDriver( String jdbcDriverClass,
                          String jdbcUrl );

    @Message( value = "JDBC Driver class not found: %s", id = 10003 )
    String jdbcDriverNotFound( String jdbcDriverClass );

    @LogMessage( level = WARN )
    @Message( value = "Could not destroy C3P0 connection pool", id = 10004 )
    void unableToDestroyC3p0ConnectionPool( @Cause SQLException e );

    @Message( value = "Could not instantiate C3P0 connection pool", id = 10005 )
    String unableToInstantiateC3p0ConnectionPool();
}
