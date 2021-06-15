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

//package org.apache.avalon.excalibur.thread.impl;

import org.apache.avalon.excalibur.pool.ObjectFactory;
import org.apache.avalon.excalibur.pool.HardResourceLimitingPool;
import org.apache.avalon.excalibur.thread.ThreadControl;
import org.apache.avalon.excalibur.thread.ThreadPool;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Executable;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.LogKitLogger;
import org.apache.avalon.framework.logger.Loggable;
import org.apache.avalon.framework.logger.Logger;
import org.apache.excalibur.threadcontext.ThreadContext;

/**
 * This class is the public frontend for the thread pool code.
 *
 */
public class DefaultThreadPool
   extends ThreadGroup
   implements ObjectFactory, Loggable, LogEnabled, Disposable, ThreadPool
{
       private HardResourceLimitingPool m_pool;

       private int m_level;

       private Logger m_logger;

       private ThreadContext m_context;

       public DefaultThreadPool( final int capacity )
               throws Exception
       {
           this( "Worker Pool", capacity );
       }

       public DefaultThreadPool( final String name, final int capacity )
               throws Exception
       {
           this( name, capacity, null );
       }

       public DefaultThreadPool( final String name,
                                 final int min, final int max,
                                 final ThreadContext context )
               throws Exception
       {
           super( name );
           m_pool = new HardResourceLimitingPool( this, min, max );
           /* AbstractPool will initialize non-Initializable pools, so
            * we have to initialize otherwise ... sheer idiocy */
           if(m_pool instanceof org.apache.avalon.framework.activity.Initializable)
           {
               m_pool.initialize();
           }
           m_context = context;
       }

       public DefaultThreadPool( final String name,
                                 final int capacity,
                                 final ThreadContext context )
               throws Exception
       {
           super( name );
           m_pool = new HardResourceLimitingPool( this, capacity );
           /* AbstractPool will initialize non-Initializable pools, so
            * we have to initialize otherwise ... sheer idiocy */
           if(m_pool instanceof org.apache.avalon.framework.activity.Initializable)
           {
               m_pool.initialize();
           }
           m_context = context;
       }

       public void setLogger( final org.apache.log.Logger logger )
       {
           enableLogging( new LogKitLogger( logger ) );
       }

       public void enableLogging( final Logger logger )
       {
           m_logger = logger;
           m_pool.enableLogging( m_logger );
       }

       public void dispose()
       {
           m_pool.dispose();
           m_pool = null;
       }

       public Object newInstance()
       {
           final String name = getName() + " Worker #" + m_level++;

           ThreadContext context = null;
           if( null != m_context )
           {
               context = m_context.duplicate();
           }

           final WorkerThread worker =
                                      new WorkerThread( this, name, m_pool, context );
           worker.setDaemon( true );
           worker.enableLogging( m_logger );
           worker.start();
           return worker;
       }

       public void decommission( final Object object )
       {
           if( object instanceof WorkerThread )
           {
               ((WorkerThread)object).dispose();
           }
       }

       public Class getCreatedClass()
       {
           return WorkerThread.class;
       }

    /**
     * Run work in separate thread.
     * Return a valid ThreadControl to control work thread.
     *
     * @param work the work to be executed.
     * @return the ThreadControl
     */
       public ThreadControl execute( final Runnable work )
       {
           return execute( new ExecutableRunnable( work ) );
       }

    /**
     * Run work in separate thread.
     * Return a valid ThreadControl to control work thread.
     *
     * @param work the work to be executed.
     * @return the ThreadControl
     */
       public ThreadControl execute( final Executable work )
       {
           final WorkerThread worker = getWorker();
           return worker.execute( work );
       }

    /**
     * Retrieve a worker thread from pool.
     *
     * @return the worker thread retrieved from pool
     */
       protected WorkerThread getWorker()
       {
           try
           {
               return (WorkerThread)m_pool.get();
           }
           catch( final Exception e )
           {
               throw new IllegalStateException( "Unable to access thread pool due to " + e );
           }
       }
}
