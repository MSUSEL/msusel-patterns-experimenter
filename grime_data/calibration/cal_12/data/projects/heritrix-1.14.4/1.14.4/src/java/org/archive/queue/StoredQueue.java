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
package org.archive.queue;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;

/**
 * Queue backed by a JE Collections StoredSortedMap. 
 * 
 * @author gojomo
 *
 * @param <E>
 */
public class StoredQueue<E extends Serializable> extends AbstractQueue<E>  implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger =
        Logger.getLogger(StoredQueue.class.getName());

    transient StoredSortedMap queueMap; // Long -> E
    transient Database queueDb; // Database
    AtomicLong tailIndex; // next spot for insert
    AtomicLong headIndex; // next spot for read
 
    /**
     * Create a StoredQueue backed by the given Database. 
     * 
     * The Class of values to be queued may be provided; there is only a 
     * benefit when a primitive type is specified. A StoredClassCatalog
     * must be provided if a primitive type is not supplied. 
     * 
     * @param db
     * @param clsOrNull 
     * @param classCatalog
     */
    public StoredQueue(Database db, Class clsOrNull, StoredClassCatalog classCatalog) {
        tailIndex = new AtomicLong(0);
        headIndex = new AtomicLong(0);
        hookupDatabase(db, clsOrNull, classCatalog);
    }

    /**
     * @param db
     * @param clsOrNull
     * @param classCatalog
     */
    public void hookupDatabase(Database db, Class clsOrNull, StoredClassCatalog classCatalog) {
        EntryBinding valueBinding = TupleBinding.getPrimitiveBinding(clsOrNull);
        if(valueBinding == null) {
            valueBinding = new SerialBinding(classCatalog, clsOrNull);
        }
        queueDb = db;
        queueMap = new StoredSortedMap(
                db,
                TupleBinding.getPrimitiveBinding(Long.class),
                valueBinding,
                true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<E> iterator() {
        return queueMap.values().iterator();
    }

    @Override
    public int size() {
        synchronized(tailIndex) {
            synchronized(headIndex) {
                return (int)(tailIndex.get()-headIndex.get());
            }
        }
        
    }

    public boolean offer(E o) {
        synchronized (tailIndex) {
            queueMap.put(tailIndex.getAndIncrement(), o);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public E peek() {
        synchronized (headIndex) {
            E head = null;
            while(head == null && headIndex.get() < tailIndex.get()) {
                head = (E) queueMap.get(headIndex.get());
                if(head != null) {
                    return head;
                }
                // ERROR; should never be null with headIndex < tailIndex
                logger.severe("unexpected empty index of StoredQueue: "
                        + headIndex.get() + " (tailIndex: " 
                        + tailIndex.get());
                headIndex.incrementAndGet();
            }
            return head;
        }
    }

    @SuppressWarnings("unchecked")
    public E poll() {
        synchronized (headIndex) {
            E head = peek();
            if(head!=null) {
                return (E) queueMap.remove(headIndex.getAndIncrement());
            } else {
                return null;
            }
        }
    }

    /**
     * A suitable DatabaseConfig for the Database backing a StoredQueue. 
     * (However, it is not necessary to use these config options.)
     * 
     * @return DatabaseConfig suitable for queue
     */
    public static DatabaseConfig databaseConfig() {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(false);
        dbConfig.setAllowCreate(true);
        dbConfig.setDeferredWrite(true);
        return dbConfig;
    }
    
    /**
     * Save the state to a stream (that is, serialize it).
     *
     * @serialData The capacity is emitted (int), followed by all of
     * its elements (each an <tt>Object</tt>) in the proper order,
     * followed by a null
     * @param s the stream
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        try {
            queueDb.sync();
        } catch (DatabaseException e) {
            throw new RuntimeException(e); 
        } 
        s.defaultWriteObject();
    }

    public void close() {
        try {
            queueDb.sync();
            queueDb.close();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}