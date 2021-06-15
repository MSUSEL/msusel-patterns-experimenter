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

package org.hsqldb.persist;

import org.hsqldb.Row;
import org.hsqldb.Session;
import org.hsqldb.TableBase;
import org.hsqldb.index.Index;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.rowio.RowInputInterface;

/**
 * Interface for a store for CachedObject objects.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.0
 * @since 1.9.0
 */
public interface PersistentStore {

    int               INT_STORE_SIZE  = 4;
    int               LONG_STORE_SIZE = 8;
    PersistentStore[] emptyArray      = new PersistentStore[]{};

    TableBase getTable();

    long getTimestamp();

    void setTimestamp(long timestamp);

    boolean isMemory();

    int getAccessCount();

    void set(CachedObject object);

    /** get object */
    CachedObject get(int key);

    /** get object, ensuring future gets will return the same instance of the object */
    CachedObject getKeep(int key);

    /** get object with keep, ensuring future gets will return the same instance of the object */
    CachedObject get(int key, boolean keep);

    CachedObject get(CachedObject object, boolean keep);

    int getStorageSize(int key);

    /** add new object */
    void add(CachedObject object);

    CachedObject get(RowInputInterface in);

    CachedObject getNewInstance(int size);

    CachedObject getNewCachedObject(Session session, Object object);

    /** remove the persisted image but not the cached copy */
    void removePersistence(int i);

    void removeAll();

    /** remove both persisted and cached copies */
    void remove(int i);

    /** remove the cached copies */
    void release(int i);

    /** commit persisted image */
    void commitPersistence(CachedObject object);

    //
    void delete(Session session, Row row);

    void indexRow(Session session, Row row);

    void commitRow(Session session, Row row, int changeAction, int txModel);

    void rollbackRow(Session session, Row row, int changeAction, int txModel);
    //
    void indexRows();

    RowIterator rowIterator();

    //
    DataFileCache getCache();

    void setCache(DataFileCache cache);

    void release();

    PersistentStore getAccessorStore(Index index);

    CachedObject getAccessor(Index key);

    void setAccessor(Index key, CachedObject accessor);

    void setAccessor(Index key, int accessor);

    int elementCount(Session session);

    int elementCountUnique(Index index);

    void setElementCount(Index key, int size, int uniqueSize);

    void updateElementCount(Index key, int size, int uniqueSize);

    void resetAccessorKeys(Index[] keys);

    void moveData(Session session, PersistentStore other, int colindex,
                  int adjust);

    void lock();

    void unlock();
}
