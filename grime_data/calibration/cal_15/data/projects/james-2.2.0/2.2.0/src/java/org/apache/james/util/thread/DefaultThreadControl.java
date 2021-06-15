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
package org.apache.james.util.thread;

import org.apache.avalon.excalibur.thread.ThreadControl;

/**
 * Default implementation of ThreadControl interface.
 *
 */
final class DefaultThreadControl
   implements ThreadControl
{
    ///Thread that this control is associated with
       private Thread m_thread;

    ///Throwable that caused thread to terminate
       private Throwable m_throwable;

    /**
     * Construct thread control for a specific thread.
     *
     * @param thread the thread to control
     */
       protected DefaultThreadControl( final Thread thread )
       {
           m_thread = thread;
       }

    /**
     * Wait for specified time for thread to complete it's work.
     *
     * @param milliSeconds the duration in milliseconds to wait until the thread has finished work
     * @throws IllegalStateException if isValid() == false
     * @throws InterruptedException if another thread has interrupted the current thread.
     *            The interrupted status of the current thread is cleared when this exception
     *            is thrown.
     */
       public synchronized void join( final long milliSeconds )
               throws IllegalStateException, InterruptedException
       {
        //final long start = System.currentTimeMillis();
           wait( milliSeconds );
        /*
          if( !isFinished() )
          {
          final long now = System.currentTimeMillis();
          if( start + milliSeconds > now )
          {
          final long remaining = milliSeconds - (now - start);
          join( remaining );
          }
          }
        */
       }

    /**
     * Call Thread.interrupt() on thread being controlled.
     *
     * @throws IllegalStateException if isValid() == false
     * @throws SecurityException if caller does not have permission to call interupt()
     */
       public synchronized void interupt()
               throws IllegalStateException, SecurityException
       {
           if( !isFinished() )
           {
               m_thread.interrupt();
           }
       }

    /**
     * Determine if thread has finished execution
     *
     * @return true if thread is finished, false otherwise
     */
       public synchronized boolean isFinished()
       {
           return ( null == m_thread );
       }

    /**
     * Retrieve throwable that caused thread to cease execution.
     * Only valid when true == isFinished()
     *
     * @return the throwable that caused thread to finish execution
     */
       public Throwable getThrowable()
       {
           return m_throwable;
       }

    /**
     * Method called by thread to release control.
     *
     * @param throwable Throwable that caused thread to complete (may be null)
     */
       protected synchronized void finish( final Throwable throwable )
       {
           m_thread = null;
           m_throwable = throwable;
           notifyAll();
       }
}
