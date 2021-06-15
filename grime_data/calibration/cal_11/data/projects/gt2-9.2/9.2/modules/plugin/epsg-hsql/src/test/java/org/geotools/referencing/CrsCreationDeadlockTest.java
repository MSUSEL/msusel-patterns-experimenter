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
package org.geotools.referencing;

import static org.junit.Assert.*;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.opengis.referencing.crs.CRSAuthorityFactory;

/**
 * 
 *
 * @source $URL$
 */
public class CrsCreationDeadlockTest {

    private static final int NUMBER_OF_THREADS = 32;

    @Test
    public void testForDeadlock() throws InterruptedException {
        // prepare the loaders
        final AtomicInteger ai = new AtomicInteger(NUMBER_OF_THREADS);
        final Runnable runnable = new Runnable() {
            public void run() {
                try {
                    final CRSAuthorityFactory authorityFactory = ReferencingFactoryFinder
                            .getCRSAuthorityFactory("EPSG", null);
                    authorityFactory.createCoordinateReferenceSystem("4326");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    ai.decrementAndGet();
                }
            }
        };

        // start them
        final List<Thread> threads = new ArrayList<Thread>();
        for (int index = 0; index < NUMBER_OF_THREADS; index++) {
            final Thread thread = new Thread(runnable);
            thread.start();
            threads.add(thread);
        }

        // use jmx to do deadlock detection
        try {
            final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
            while (ai.get() > 0) {
                long[] deadlockedThreads = mbean.findMonitorDeadlockedThreads();
                if (deadlockedThreads != null && deadlockedThreads.length > 0) {
                    fail("Deadlock detected between the following threads: "
                            + Arrays.toString(deadlockedThreads));
                }
                // sleep for a bit
                Thread.currentThread().sleep(10);
            }
        } finally {
            // kill all the 
            for (final Thread thread : threads) {
                if(thread.isAlive()) {
                    thread.interrupt();
                }
            }
        }
    }
}
