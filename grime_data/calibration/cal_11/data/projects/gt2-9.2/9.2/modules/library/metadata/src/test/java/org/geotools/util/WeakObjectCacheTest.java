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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.util;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the WeakObjectCache with simple tests.
 *
 * @author Cory Horner (Refractions Research)
 *
 *
 *
 * @source $URL$
 */
public final class WeakObjectCacheTest {
    private Integer  key1 = 1;
    private Integer  key2 = 2;
    private String value1 = new String("value 1");
    private String value2 = new String("value 2");
    private String value3 = new String("value 3");

    @Test
    public void testSimple() {
        ObjectCache cache = new WeakObjectCache();
        assertNotNull(cache);

        assertEquals(null, cache.get(key1));

        cache.writeLock(key1);
        cache.put(key1, value1);
        cache.writeUnLock(key1);
        assertEquals(value1, cache.get(key1));

        assertEquals(null, cache.get(key2));
        
        //test getKeys()
        assertEquals(1, cache.getKeys().size());
        assertEquals(key1, cache.getKeys().iterator().next());
    }
    
    @Test
    public void testRemoveSimple(){
        ObjectCache cache = new WeakObjectCache();
        assertNotNull(cache);

        assertEquals(null, cache.get(key1));

        cache.writeLock(key1);
        cache.put(key1, value1);
        cache.writeUnLock(key1);
        assertEquals(value1, cache.get(key1));

        assertEquals(null, cache.get(key2));
        
        //test getKeys()
        assertEquals(1, cache.getKeys().size());
        assertEquals(key1, cache.getKeys().iterator().next());
        
        //remove the key
        cache.remove(key1);
        assertEquals(0, cache.getKeys().size());
    }

    @Test
    public void testConcurrent() throws InterruptedException {
        ObjectCache cache = new WeakObjectCache();

        //lock the cache as if we were writing
        cache.writeLock(key1);

        //create another thread which starts writing and blocks
        Runnable thread1 = new WriterThread(cache);
        Thread t1 = new Thread(thread1);
        t1.start();
        Thread.yield();

        //write
        cache.put(key1, value2);

        //check that the write thread was blocked
        Object[] values = ((WriterThread) thread1).getValue();
        assertEquals(null, values);
        assertEquals(value2, cache.peek(key1));
        assertEquals(1,cache.getKeys().size());
        
        //check that a separate write thread can get through
        cache.writeLock(key2);
        cache.put(key2, value3);
        cache.writeUnLock(key2);
        assertEquals(2,cache.getKeys().size());
        
        //unlock
        try {
            cache.writeUnLock(key1);
        } catch (Exception e) {
            fail("couldn't unlock");
        }

        //check that the write thread is unblocked
        t1.join();
        values = ((WriterThread) thread1).getValue();
        assertNotNull(values);
        assertEquals(value1, values[0]);
        
        assertEquals(2, cache.getKeys().size());
        assertTrue(cache.getKeys().contains(key1));
        assertTrue(cache.getKeys().contains(key2));
    }

    private class WriterThread implements Runnable {

        ObjectCache cache = null;
        Object[] values = null;

        public WriterThread(ObjectCache cache) {
            this.cache = cache;
        }

        public void run() {
            try {
                cache.writeLock(key1);
                cache.put(key1, value1);
                values = new Object[] {cache.get(key1)};
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    cache.writeUnLock(key1);
                } catch (Exception e) {
                    fail("couldn't unlock");
                }
            }
        }

        public Object[] getValue() {
            return values;
        }
    }
}
