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
package org.hibernate.cache.ehcache.internal.nonstop;

import net.sf.ehcache.constructs.nonstop.NonStopCacheException;
import org.jboss.logging.Logger;

import org.hibernate.cache.ehcache.EhCacheMessageLogger;

/**
 * Class that takes care of {@link net.sf.ehcache.constructs.nonstop.NonStopCacheException} that happens in hibernate module
 *
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public final class HibernateNonstopCacheExceptionHandler {
    /**
     * Property name which set as "true" will throw exceptions on timeout with hibernate
     */
    public static final String HIBERNATE_THROW_EXCEPTION_ON_TIMEOUT_PROPERTY = "ehcache.hibernate.propagateNonStopCacheException";

    /**
     * Property name for logging the stack trace of the nonstop cache exception too. False by default
     */
    public static final String HIBERNATE_LOG_EXCEPTION_STACK_TRACE_PROPERTY = "ehcache.hibernate.logNonStopCacheExceptionStackTrace";

    private static final EhCacheMessageLogger LOG = Logger.getMessageLogger(
            EhCacheMessageLogger.class,
            HibernateNonstopCacheExceptionHandler.class.getName()
    );
    private static final HibernateNonstopCacheExceptionHandler INSTANCE = new HibernateNonstopCacheExceptionHandler();

    /**
     * private constructor
     */
    private HibernateNonstopCacheExceptionHandler() {
        // private
    }

    /**
     * Returns the singleton instance
     *
     * @return the singleton instance
     */
    public static HibernateNonstopCacheExceptionHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Handle {@link net.sf.ehcache.constructs.nonstop.NonStopCacheException}.
     * If {@link HibernateNonstopCacheExceptionHandler#HIBERNATE_THROW_EXCEPTION_ON_TIMEOUT_PROPERTY} system property is set to true,
     * rethrows the {@link net.sf.ehcache.constructs.nonstop.NonStopCacheException}, otherwise logs the exception. While logging, if
     * {@link HibernateNonstopCacheExceptionHandler#HIBERNATE_LOG_EXCEPTION_STACK_TRACE_PROPERTY} is set to true, logs the exception stack
     * trace too, otherwise logs the exception message only
     *
     * @param nonStopCacheException
     */
    public void handleNonstopCacheException(NonStopCacheException nonStopCacheException) {
        if ( Boolean.getBoolean( HIBERNATE_THROW_EXCEPTION_ON_TIMEOUT_PROPERTY ) ) {
            throw nonStopCacheException;
        }
        else {
            if ( Boolean.getBoolean( HIBERNATE_LOG_EXCEPTION_STACK_TRACE_PROPERTY ) ) {
                LOG.debug(
                        "Ignoring NonstopCacheException - " + nonStopCacheException.getMessage(),
                        nonStopCacheException
                );
            }
            else {
                LOG.debug( "Ignoring NonstopCacheException - " + nonStopCacheException.getMessage() );
            }
        }
    }
}
