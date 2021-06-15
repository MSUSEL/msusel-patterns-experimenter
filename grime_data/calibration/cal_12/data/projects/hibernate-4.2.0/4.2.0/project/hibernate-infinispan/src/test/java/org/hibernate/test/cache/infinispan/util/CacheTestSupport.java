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
package org.hibernate.test.cache.infinispan.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.infinispan.Cache;
import org.jboss.logging.Logger;

import org.hibernate.cache.spi.RegionFactory;

/**
 * Support class for tracking and cleaning up objects used in tests.
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 */
public class CacheTestSupport {
	private static final Logger log = Logger.getLogger( CacheTestSupport.class );

	private static final String PREFER_IPV4STACK = "java.net.preferIPv4Stack";

    private Set<Cache> caches = new HashSet();
    private Set<RegionFactory> factories = new HashSet();
    private Exception exception;
    private String preferIPv4Stack;

    public void registerCache(Cache cache) {
        caches.add(cache);
    }

    public void registerFactory(RegionFactory factory) {
        factories.add(factory);
    }

    public void unregisterCache(Cache cache) {
        caches.remove( cache );
    }

    public void unregisterFactory(RegionFactory factory) {
        factories.remove( factory );
    }

    public void setUp() throws Exception {

        // Try to ensure we use IPv4; otherwise cluster formation is very slow
        preferIPv4Stack = System.getProperty(PREFER_IPV4STACK);
        System.setProperty(PREFER_IPV4STACK, "true");

        cleanUp();
        throwStoredException();
    }

    public void tearDown() throws Exception {

        if (preferIPv4Stack == null)
            System.clearProperty(PREFER_IPV4STACK);
        else
            System.setProperty(PREFER_IPV4STACK, preferIPv4Stack);

        cleanUp();
        throwStoredException();
    }

    public void avoidConcurrentFlush() {
       // JG 2.6.1 has a problem where calling flush more than once too quickly
       // can result in several second delays
       sleep( 100 );
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            log.warn("Interrupted during sleep", e);
        }
    }

    private void cleanUp() {
        for (Iterator it = factories.iterator(); it.hasNext(); ) {
            try {
                ((RegionFactory) it.next()).stop();
            }
            catch (Exception e) {
                storeException(e);
            }
            finally {
                it.remove();
            }
        }
        factories.clear();

        for (Iterator it = caches.iterator(); it.hasNext(); ) {
            try {
                Cache cache = (Cache) it.next();
                cache.stop();
            }
            catch (Exception e) {
                storeException(e);
            }
            finally {
                it.remove();
            }
            avoidConcurrentFlush();
        }
        caches.clear();
    }

    private void storeException(Exception e) {
        if (this.exception == null) {
            this.exception = e;
        }
    }

    private void throwStoredException() throws Exception {
        if (exception != null) {
            Exception toThrow = exception;
            exception = null;
            throw toThrow;
        }
    }

}
