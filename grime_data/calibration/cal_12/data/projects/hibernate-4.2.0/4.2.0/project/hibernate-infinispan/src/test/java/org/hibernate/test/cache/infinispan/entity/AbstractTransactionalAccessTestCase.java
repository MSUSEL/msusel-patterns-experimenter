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
package org.hibernate.test.cache.infinispan.entity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.AssertionFailedError;
import org.infinispan.transaction.tm.BatchModeTransactionManager;
import org.jboss.logging.Logger;

import org.hibernate.cache.spi.access.AccessType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Base class for tests of TRANSACTIONAL access.
 *
 * @author Galder Zamarreño
 * @since 3.5
 */
public abstract class AbstractTransactionalAccessTestCase extends AbstractEntityRegionAccessStrategyTestCase {
	private static final Logger log = Logger.getLogger( AbstractTransactionalAccessTestCase.class );

	@Override
   protected AccessType getAccessType() {
      return AccessType.TRANSACTIONAL;
   }

    public void testContestedPutFromLoad() throws Exception {

        final String KEY = KEY_BASE + testCount++;

        localAccessStrategy.putFromLoad(KEY, VALUE1, System.currentTimeMillis(), new Integer(1));

        final CountDownLatch pferLatch = new CountDownLatch(1);
        final CountDownLatch pferCompletionLatch = new CountDownLatch(1);
        final CountDownLatch commitLatch = new CountDownLatch(1);
        final CountDownLatch completionLatch = new CountDownLatch(1);

        Thread blocker = new Thread("Blocker") {

            @Override
            public void run() {

                try {
                    long txTimestamp = System.currentTimeMillis();
                    BatchModeTransactionManager.getInstance().begin();

                    assertEquals("Correct initial value", VALUE1, localAccessStrategy.get(KEY, txTimestamp));

                    localAccessStrategy.update(KEY, VALUE2, new Integer(2), new Integer(1));

                    pferLatch.countDown();
                    commitLatch.await();

                    BatchModeTransactionManager.getInstance().commit();
                } catch (Exception e) {
                    log.error("node1 caught exception", e);
                    node1Exception = e;
                    rollback();
                } catch (AssertionFailedError e) {
                    node1Failure = e;
                    rollback();
                } finally {
                    completionLatch.countDown();
                }
            }
        };

        Thread putter = new Thread("Putter") {

            @Override
            public void run() {

                try {
                    long txTimestamp = System.currentTimeMillis();
                    BatchModeTransactionManager.getInstance().begin();

                    localAccessStrategy.putFromLoad(KEY, VALUE1, txTimestamp, new Integer(1));

                    BatchModeTransactionManager.getInstance().commit();
                } catch (Exception e) {
                    log.error("node1 caught exception", e);
                    node1Exception = e;
                    rollback();
                } catch (AssertionFailedError e) {
                    node1Failure = e;
                    rollback();
                } finally {
                    pferCompletionLatch.countDown();
                }
            }
        };

        blocker.start();
        assertTrue("Active tx has done an update", pferLatch.await(1, TimeUnit.SECONDS));
        putter.start();
        assertTrue("putFromLoadreturns promtly", pferCompletionLatch.await(10, TimeUnit.MILLISECONDS));

        commitLatch.countDown();

        assertTrue("Threads completed", completionLatch.await(1, TimeUnit.SECONDS));

        assertThreadsRanCleanly();

        long txTimestamp = System.currentTimeMillis();
        assertEquals("Correct node1 value", VALUE2, localAccessStrategy.get(KEY, txTimestamp));
    }

}
