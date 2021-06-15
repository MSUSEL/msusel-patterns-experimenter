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
package org.apache.james.test;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.ExceptionUtil;
import org.apache.avalon.framework.CascadingRuntimeException;
import org.apache.avalon.phoenix.components.embeddor.SingleAppEmbeddor;

/**
 * An attempt at a task which can launch James from Ant, and shut it down again.
 * Doesn't really work, yet.
 */
public final class JamesTask
       extends org.apache.tools.ant.Task
{
    private CommandlineJava cmdl = new CommandlineJava();
    private Parameters m_parameters;
    private static SingleAppEmbeddor m_embeddor;
    private String m_action;

    public void setAction( String action )
    {
        m_action = action;
    }

    /**
     * Set the classpath to be used for this compilation.
     */
    public void setClasspath(Path s) {
        createClasspath().append(s);
    }

    /**
     * Creates a nested classpath element
     */
    public Path createClasspath() {
        return cmdl.createClasspath(project).createPath();
    }

    /**
     * Adds a reference to a CLASSPATH defined elsewhere.
     */
    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }


    public void execute() throws BuildException
    {
        if ( m_action.equalsIgnoreCase( "start" ) ) {
            startup();
        }
        else if ( m_action.equalsIgnoreCase( "stop" ) ) {
            shutdown();
        }
        else if ( m_action.equalsIgnoreCase( "start-stop" ) ) {
            startup();
            shutdown();
        }
        else if ( m_action.equalsIgnoreCase( "restart" ) ) {
            optionalShutdown();
            startup();
        }
        else {
            throw new BuildException( "Invalid action: '" + m_action + "'" );
        }
    }

    private void startup() throws BuildException
    {
        if ( m_embeddor != null ) {
           throw new BuildException( "Already started" );
        }

        m_parameters = new Parameters();
//        m_parameters.setParameter( "log-destination", "." );
//        m_parameters.setParameter( "log-priority", "DEBUG" );
//        m_parameters.setParameter( "application-name", "james" );
        m_parameters.setParameter( "application-location", "dist/apps/james.sar");

        try
        {
            m_embeddor = new SingleAppEmbeddor();
            if( m_embeddor instanceof Parameterizable )
            {
                ( (Parameterizable)m_embeddor ).parameterize( m_parameters );
            }
            m_embeddor.initialize();

//            final Thread thread = new Thread( this, "Phoenix" );
//            thread.start();
        }
        catch( final Throwable throwable )
        {
            System.out.println( "Exception in initiliaze()" );
            throw new BuildException( throwable );
        }

        try
        {
            ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
            AntClassLoader loader = new AntClassLoader(ctxLoader, project, cmdl.getClasspath(), true);
            loader.setIsolated(false);
            loader.setThreadContextLoader();
            // nothing
            // m_embeddor.execute();
            // Launch the startup thread.
            Thread startThread = new StartupThread();
            startThread.start();

            // Hack to make sure that the embeddor has actually started.
            // Need to make a change to Phoenix, so that we can wait til it's running.
            // Yeild processor.
            Thread.sleep( 1000 );
            // m_embeddor will now be in use until applications are deployed.
            synchronized ( m_embeddor ) {
                System.out.println( "got synch at: " + System.currentTimeMillis() );
            }
        }
        catch( final Throwable throwable )
        {
            System.out.println( "Exception in execute()" );
            throw new BuildException( throwable );
        }

    }

    private class StartupThread extends Thread
    {
        StartupThread()
        {
            super( "JamesStartup" );
            this.setDaemon( true );
        }

        public void run()
        {
            try {
                m_embeddor.execute();
            }
            catch ( Exception exc ) {
                exc.printStackTrace();
                throw new CascadingRuntimeException( "Exception in execute()", exc );
            }
        }

    }

    private void optionalShutdown() throws BuildException
    {
        if ( m_embeddor != null ) {
            shutdown();
        }
    }

    private void shutdown() throws BuildException
    {
        System.out.println( "In shutdown()" );
        if ( m_embeddor == null ) {
            throw new BuildException( "Not running." );
        }

        try
        {
            m_embeddor.dispose();
            System.out.println( "Called dispose()" );
            m_embeddor = null;
            m_parameters = null;
        }
        catch( final Throwable throwable )
        {
            System.out.println( "Exception in dispose()" );
            throw new BuildException( throwable );
        }
    }
}
