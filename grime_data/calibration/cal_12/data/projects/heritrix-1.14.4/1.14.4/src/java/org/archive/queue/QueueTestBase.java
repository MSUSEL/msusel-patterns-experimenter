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

import java.util.NoSuchElementException;

import org.archive.util.TmpDirTestCase;

/**
 * JUnit test suite for Queue.  It's an abstract class which is implemented by
 * each queue implementation
 *
 * @author <a href="mailto:me@jamesc.net">James Casey</a>
 * @version $Id: QueueTestBase.java 4645 2006-09-22 16:08:03Z paul_jack $
 */
public abstract class QueueTestBase extends TmpDirTestCase {
    /**
     * Create a new PaddingStringBufferTest object
     *
     * @param testName the name of the test
     */
    public QueueTestBase(final String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        queue = makeQueue();
    }

    public void tearDown() {
        if(queue != null) {
            queue.release();
        }
    }

    /**
     * The abstract subclass constructor.  The subclass should create an
     * instance of the object it wishes to have tested
     *
     * @return the Queue object to be tested
     */
    protected abstract Queue<Object> makeQueue();

    /*
     * test methods
     */

    /** test that queue puts things on, and they stay there :) */
    public void testQueue() {
        assertEquals("no items in new queue", 0, queue.length());
        assertTrue("queue is empty", queue.isEmpty());
        queue.enqueue("foo");
        assertEquals("now one item in queue", 1, queue.length());
        assertFalse("queue not empty", queue.isEmpty());
    }

    /** test that dequeue works */
    public void testDequeue() {
        assertEquals("no items in new queue", 0, queue.length());
        assertTrue("queue is empty", queue.isEmpty());
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");
        assertEquals("now three items in queue", 3, queue.length());
        assertEquals("foo dequeued", "foo", queue.dequeue());
        assertEquals("bar dequeued", "bar", queue.dequeue());
        assertEquals("baz dequeued", "baz", queue.dequeue());

        assertEquals("no items in new queue", 0, queue.length());
        assertTrue("queue is empty", queue.isEmpty());

    }

    /** check what happens we dequeue on empty */
    public void testDequeueEmptyQueue() {
        assertTrue("queue is empty", queue.isEmpty());

        try {
            queue.dequeue();
        } catch (NoSuchElementException e) {
            return;
        }
        fail("Expected a NoSuchElementException on dequeue of empty queue");
    }
    /*
     * member variables
     */

    /** the queue object to be tested */
    protected Queue<Object> queue;
}