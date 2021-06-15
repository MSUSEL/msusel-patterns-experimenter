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
package org.hibernate.test.cache.infinispan.functional.classloader;

import java.util.HashSet;
import java.util.Set;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryVisited;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryVisitedEvent;
import org.jboss.logging.Logger;

@Listener
public class CacheAccessListener {
	private static final Logger log = Logger.getLogger( CacheAccessListener.class );

	HashSet modified = new HashSet();
    HashSet accessed = new HashSet();

    public void clear() {
        modified.clear();
        accessed.clear();
    }

    @CacheEntryModified
    public void nodeModified( CacheEntryModifiedEvent event ) {
        if (!event.isPre()) {
            Object key = event.getKey();
            log.info("Modified node " + key);
            modified.add(key.toString());
        }
    }

    @CacheEntryCreated
    public void nodeCreated( CacheEntryCreatedEvent event ) {
        if (!event.isPre()) {
            Object key = event.getKey();
            log.info("Created node " + key);
            modified.add(key.toString());
        }
    }

    @CacheEntryVisited
    public void nodeVisited( CacheEntryVisitedEvent event ) {
        if (!event.isPre()) {
            Object key = event.getKey();
            log.info("Visited node " + key);
            accessed.add(key.toString());
        }
    }

    public boolean getSawRegionModification( Object key ) {
        return getSawRegion(key, modified);
    }

    public int getSawRegionModificationCount() {
        return modified.size();
    }

    public void clearSawRegionModification() {
        modified.clear();
    }

    public boolean getSawRegionAccess( Object key ) {
        return getSawRegion(key, accessed);
    }

    public int getSawRegionAccessCount() {
        return accessed.size();
    }

    public void clearSawRegionAccess() {
        accessed.clear();
    }

    private boolean getSawRegion( Object key,
                                  Set sawEvents ) {
        if (sawEvents.contains(key)) {
            sawEvents.remove(key);
            return true;
        }
        return false;
    }

}
