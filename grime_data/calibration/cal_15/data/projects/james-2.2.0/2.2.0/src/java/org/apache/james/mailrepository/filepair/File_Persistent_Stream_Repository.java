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
package org.apache.james.mailrepository.filepair;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.avalon.cornerstone.services.store.StreamRepository;
import org.apache.avalon.excalibur.io.IOUtil;

/**
 * Implementation of a StreamRepository to a File.
 * TODO: -retieve(String key) should return a FilterInputStream to allow
 * mark and reset methods. (working not like BufferedInputStream!!!)
 *
 */
public class File_Persistent_Stream_Repository
    extends AbstractFileRepository
    implements StreamRepository
{
    protected final HashMap m_inputs = new HashMap();
    protected final HashMap m_outputs = new HashMap();

    protected String getExtensionDecorator()
    {
        return ".FileStreamStore";
    }

    /**
     * Get the object associated to the given unique key.
     */
    public synchronized InputStream get( final String key )
    {
        try
        {
            final ResettableFileInputStream stream =
                new ResettableFileInputStream( getFile( key ) );

            final Object o = m_inputs.get( key );
            if( null == o )
            {
                m_inputs.put( key, stream );
            }
            else if( o instanceof ArrayList )
            {
                ( (ArrayList)o ).add( stream );
            }
            else
            {
                final ArrayList list = new ArrayList();
                list.add( o );
                list.add( stream );
                m_inputs.put( key, list );
            }

            return stream;
        }
        catch( final IOException ioe )
        {
            final String message = "Exception caught while retrieving a stream ";
            getLogger().warn( message, ioe );
            throw new RuntimeException( message + ": " + ioe );
        }
    }

    /**
     * Store the given object and associates it to the given key
     */
    public synchronized OutputStream put( final String key )
    {
        try
        {
            final OutputStream outputStream = getOutputStream( key );
            final BufferedOutputStream stream = new BufferedOutputStream( outputStream );

            final Object o = m_outputs.get( key );
            if( null == o )
            {
                m_outputs.put( key, stream );
            }
            else if( o instanceof ArrayList )
            {
                ( (ArrayList)o ).add( stream );
            }
            else
            {
                final ArrayList list = new ArrayList();
                list.add( o );
                list.add( stream );
                m_outputs.put( key, list );
            }

            return stream;
        }
        catch( final IOException ioe )
        {
            final String message = "Exception caught while storing a stream ";
            getLogger().warn( message, ioe );
            throw new RuntimeException( message + ": " + ioe );
        }
    }

    public synchronized void remove( final String key )
    {
        Object o = m_inputs.remove( key );
        if( null != o )
        {
            if( o instanceof InputStream )
            {
                IOUtil.shutdownStream( (InputStream)o );
            }
            else
            {
                final ArrayList list = (ArrayList)o;
                final int size = list.size();

                for( int i = 0; i < size; i++ )
                {
                    IOUtil.shutdownStream( (InputStream)list.get( i ) );
                }
            }
        }

        o = m_outputs.remove( key );
        if( null != o )
        {
            if( o instanceof OutputStream )
            {
                IOUtil.shutdownStream( (OutputStream)o );
            }
            else
            {
                final ArrayList list = (ArrayList)o;
                final int size = list.size();

                for( int i = 0; i < size; i++ )
                {
                    IOUtil.shutdownStream( (OutputStream)list.get( 0 ) );
                }
            }
        }

        super.remove( key );
    }

    public long getSize(final String key) {
        try {
            return getFile(key).length();
        }
        catch(IOException e) {
            return 0;
        }
    }
}
