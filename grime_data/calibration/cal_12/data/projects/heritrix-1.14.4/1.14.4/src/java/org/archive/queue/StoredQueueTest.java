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

import java.io.File;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

import org.archive.util.FileUtils;
import org.archive.util.TmpDirTestCase;
import org.archive.util.bdbje.EnhancedEnvironment;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.EnvironmentConfig;

public class StoredQueueTest extends TmpDirTestCase {
    StoredQueue<String> queue;
    EnhancedEnvironment env;
    Database db; 
    File envDir; 

    protected void setUp() throws Exception {
        super.setUp();
        this.envDir = new File(getTmpDir(),"StoredMapTest");
        this.envDir.mkdirs();
        try {
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setTransactional(false);
            envConfig.setAllowCreate(true);
            env = new EnhancedEnvironment(envDir,envConfig);
            DatabaseConfig dbConfig = StoredQueue.databaseConfig();
            db = env.openDatabase(null, "StoredMapTest", dbConfig);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        this.queue = new StoredQueue<String>(db, String.class, env.getClassCatalog());
    }
    
    protected void tearDown() throws Exception {
        db.close();
        env.close(); 
        FileUtils.deleteDir(this.envDir);
        super.tearDown();
    }
    
    public void testAdd() {
        assertEquals("not empty at start",0,queue.size());
        fill(queue, 10);
        assertEquals("unexpected size at full",10,queue.size());
    }

    /**
     * @deprecated Use {@link #fill(Queue,int)} instead
     */
    protected void fill(int size) {
        fill(queue, size);
    }

    protected void fill(java.util.Queue<String> q, int size) {
        for(int i = 1; i <= size; i++) {
            q.add("item-"+i);
        }
    }
    
    protected int drain(java.util.Queue<String> q) {
        int count = 0; 
        while(true) {
            try {
                q.remove();
                count++;
            } catch(NoSuchElementException nse) {
                return count;
            }
        }
    }

    public void testClear() {
        fill(queue, 10);
        queue.clear();
        assertEquals("unexpected size after clear",0,queue.size());
    }

    public void testRemove() {
        fill(queue, 10);
        assertEquals("unexpected remove value","item-1",queue.remove());
        assertEquals("improper count of removed items",9,drain(queue));
        try {
            queue.remove();
            fail("expected NoSuchElementException not received");
        } catch (NoSuchElementException nse) {
            // do nothing
        }
    }
    
    public void testOrdering() {
        fill(queue, 10);
        for(int i = 1; i <= 10; i++) {
            assertEquals("unexpected remove value","item-"+i,queue.remove());
        }
    }

    public void testElement() {
        fill(queue, 10);
        assertEquals("unexpected element value","item-1",queue.element());
        assertEquals("unexpected element value",queue.peek(),queue.element());
        queue.clear();
        try {
            queue.element();
            fail("expected NoSuchElementException not received");
        } catch (NoSuchElementException nse) {
            // do nothing
        }
    }

    public void xestTimingsAgainstLinkedBlockingQueue() {
        tryTimings(50000);
        tryTimings(500000);
    }

    private void tryTimings(int i) {
        LinkedBlockingQueue<String> lbq = new LinkedBlockingQueue<String>();
        long start = System.currentTimeMillis();
        fill(lbq,i);
        drain(lbq);
        long finish = System.currentTimeMillis();
        System.out.println("LBQ - "+i+":"+(finish-start));
        start = System.currentTimeMillis();
        fill(queue,i);
        drain(queue);
        finish = System.currentTimeMillis();
        System.out.println("SQ - "+i+":"+(finish-start));
    }
}
