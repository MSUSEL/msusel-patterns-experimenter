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
/* FairGenericObjectPoolTest
*
* $Id: FairGenericObjectPoolTest.java 4672 2006-09-27 00:03:16Z paul_jack $
*
* Created on Apr 7, 2006
*
* Copyright (C) 2006 Internet Archive.
*
*/ 
package org.apache.commons.pool.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * Test for FairGenericObjectPool.
 * 
 * @author gojomo
 */
@SuppressWarnings("unchecked")
public class FairGenericObjectPoolTest extends TestCase {
//    public void testUnfair() throws InterruptedException {
////        System.out.println("unfair");
//        GenericObjectPool pool = new GenericObjectPool();
//        
//        Object[] borrowOrder = tryPool(pool);
//        
//        Object[] sortedOrder = (Object[]) borrowOrder.clone();
//        Arrays.sort(sortedOrder);
//        assertFalse("unexpectedly fair", Arrays.equals(borrowOrder,sortedOrder));
//    }
    
    public void testFair() throws InterruptedException {
//        System.out.println("fair");
        GenericObjectPool pool = new FairGenericObjectPool();
        
        Object[] borrowOrder = tryPool(pool);
        
        Object[] sortedOrder = (Object[]) borrowOrder.clone();
        Arrays.sort(sortedOrder);
        assertTrue("unexpectedly unfair", Arrays.equals(borrowOrder,sortedOrder));
    }
    
    /**
     * Test the given pool for fairness. 
     * 
     * @param pool GenericObjectPool to test
     * @throws InterruptedException
     */
    private Object[] tryPool(GenericObjectPool pool) throws InterruptedException {
        BlockerObjectFactory factory = new BlockerObjectFactory();
        pool.setFactory(factory);
        pool.setMaxActive(1);
        List borrowOrder = Collections.synchronizedList(new LinkedList());
        for(int i = 0; i < 10; i++) {
            Contender c = new Contender(borrowOrder);
            c.pool = pool;
            c.ordinal = i;
            (new Thread(c)).start();
            Thread.sleep(500);
        }
        factory.single.release();
        Thread.sleep(5000);
        return borrowOrder.toArray();
    }

    class Contender implements Runnable {
        public GenericObjectPool pool;
        public int ordinal;
        public List reportList; 
        
        public Contender(List borrowOrder) {
            reportList = borrowOrder;
        }

        public void run() {
            try {
                Blocker block = (Blocker) pool.borrowObject();
                System.out.println("borrowed #"+ordinal);
                reportList.add(new Integer(ordinal));
                block.acquire();
                System.out.println("returning #"+ordinal);
                pool.returnObject(block);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } 
        }
        
    }
    
    class BlockerObjectFactory extends BasePoolableObjectFactory {
        public Blocker single = new Blocker();
        public Object makeObject() throws Exception {
            System.out.println("makeObject");
            return single;
        }
    }
    
    class Blocker {
        boolean block = true;
        public synchronized void acquire() {
            // only block first time through
            if(block) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            block = false; 
        }
        public synchronized void release() {
            notifyAll();
        }
    }
}
