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
 * Simple deadlock tests for {@link ObjectCacheEntry}.
 *
 * @author Cory Horner (Refractions Research)
 *
 *
 *
 * @source $URL$
 */
public final class ObjectCacheEntryTest {

    DefaultObjectCache.ObjectCacheEntry entry;

    private class EntryReaderThread implements Runnable {

        Object[] values = null;

        public EntryReaderThread() {
        }

        public void run() {
            try {
                values = new Object[] {entry.getValue()};
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object[] getValue() {
            return values;
        }
    }

    private class EntryWriterThread implements Runnable {

        Object[] values = null;

        public EntryWriterThread() {
        }

        public void run() {
            try {
                entry.writeLock();
                entry.setValue(1);
                entry.writeUnLock();
                values = new Object[] {entry.getValue()};
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object[] getValue() {
            return values;
        }
    }

    @Test
    public void testWriteReadDeadlock() throws InterruptedException {
        //lock the entry as if we were writing
        entry = new DefaultObjectCache.ObjectCacheEntry();
        entry.writeLock();

        //create another thread which starts reading
        Runnable thread1 = new EntryReaderThread();
        Thread t1 = new Thread(thread1);
        t1.start();
        Thread.yield();

        //write
        entry.setValue(1);

        //check that the read thread was blocked
        Object[] values = ((EntryReaderThread) thread1).getValue();
        assertArrayEquals(null, values);

        //unlock
        entry.writeUnLock();

        //check that the read thread is unblocked
        t1.join();
        values = ((EntryReaderThread) thread1).getValue();
        assertNotNull(values);
        assertEquals(1, values[0]);
    }

    @Test
    public void testWriteWriteDeadlock() throws InterruptedException {
        //lock the entry as if we were writing
        entry = new DefaultObjectCache.ObjectCacheEntry();
        entry.writeLock();

        //write the value 2
        entry.setValue(2);

        //create another thread which starts writing
        Runnable thread1 = new EntryWriterThread();
        Thread t1 = new Thread(thread1);
        t1.start();
        Thread.yield();

        //check that the write thread was blocked
        Object[] values = ((EntryWriterThread) thread1).getValue();
        assertNull(values);
        assertEquals(2, entry.getValue());

        //unlock
        entry.writeUnLock();

        //check that the write thread is unblocked
        t1.join();
        values = ((EntryWriterThread) thread1).getValue();
        assertNotNull(values);
        assertEquals(1, values[0]);
    }
}
