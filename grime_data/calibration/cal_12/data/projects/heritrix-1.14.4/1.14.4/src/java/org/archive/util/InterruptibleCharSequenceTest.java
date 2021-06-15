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
package org.archive.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import junit.framework.TestCase;

/**
 * Tests (and 
 * @author gojomo
 */
public class InterruptibleCharSequenceTest extends TestCase {
    // this regex takes many seconds to fail on the input
    // (~20 seconds on 2Ghz Athlon64 JDK 1.6)
    public static String BACKTRACKER = "^(((((a+)*)*)*)*)*$";
    public static String INPUT = "aaaaab";
    
    /**
     * Development-time benchmarking of InterruptibleCharSequence in
     * regex use. (Rename 'xest' to 'test' if wanted as unit test, 
     * but never actually fails anything -- just measures.)
     * 
     * For reference the regex "^(((((a+)*)*)*)*)*$" requires 
     * 239,286,636 charAt(s) to fail on "aaaaab", which takes
     * around 20 seconds on a 2Ghz Athlon64(x2) with JDK 1.6. 
     * The runtime overhead of checking interrupt status in this
     * extreme case is around 5% in my tests.
     */
    public void xestOverhead() {
        String regex = BACKTRACKER;
        String inputNormal = INPUT;
        InterruptibleCharSequence inputWrapped = new InterruptibleCharSequence(inputNormal); 
        // warm up 
        tryMatch(inputNormal,regex);
        tryMatch(inputWrapped,regex);
        // inputWrapped.counter=0;
        int trials = 5; 
        long stringTally = 0;
        long icsTally = 0; 
        for(int i = 1; i <= trials; i++) {
            System.out.println("trial "+i+" of "+trials);
            long start = System.currentTimeMillis();
            System.out.print("String ");
            tryMatch(inputNormal,regex);
            long end = System.currentTimeMillis();
            System.out.println(end-start); 
            stringTally += (end-start); 
            start = System.currentTimeMillis();
            System.out.print("InterruptibleCharSequence ");
            tryMatch(inputWrapped,regex);
            end = System.currentTimeMillis();
            System.out.println(end-start); 
            //System.out.println(inputWrapped.counter+" steps");
            //inputWrapped.counter=0;
            icsTally += (end-start); 
        }
        System.out.println("InterruptibleCharSequence took "+((float)icsTally)/stringTally+" longer."); 
    }
    
    public boolean tryMatch(CharSequence input, String regex) {
        return Pattern.matches(regex,input);
    }
    
    public Thread tryMatchInThread(final CharSequence input, final String regex, final BlockingQueue<Object> atFinish) {
        Thread t = new Thread() { 
            public void run() { 
                boolean result; 
                try {
                    result = tryMatch(input,regex); 
                } catch (Exception e) {
                    atFinish.offer(e);
                    return;
                }
                atFinish.offer(result);
            } 
        };
        t.start();
        return t; 
    }
    
    public void testNoninterruptible() throws InterruptedException {
        BlockingQueue<Object> q = new LinkedBlockingQueue<Object>();
        Thread t = tryMatchInThread(INPUT, BACKTRACKER, q);
        Thread.sleep(1000);
        t.interrupt();
        Object result = q.take(); 
        assertTrue("mismatch uncompleted",Boolean.FALSE.equals(result));
    }
    
    public void testInterruptibility() throws InterruptedException {
        BlockingQueue<Object> q = new LinkedBlockingQueue<Object>();
        Thread t = tryMatchInThread(new InterruptibleCharSequence(INPUT), BACKTRACKER, q);
        Thread.sleep(1000);
        t.interrupt();
        Object result = q.take(); 
        assertTrue("exception not thrown",result instanceof RuntimeException);
    }
}
