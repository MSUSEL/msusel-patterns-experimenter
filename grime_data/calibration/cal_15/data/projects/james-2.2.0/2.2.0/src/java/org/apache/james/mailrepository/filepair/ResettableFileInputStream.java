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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 */
public class ResettableFileInputStream
    extends InputStream
{
    protected static final int DEFAULT_BUFFER_SIZE = 1024;

    protected final String m_filename;
    protected int m_bufferSize;
    protected InputStream m_inputStream;
    protected long m_position;
    protected long m_mark;
    protected boolean m_isMarkSet;

    public ResettableFileInputStream( final File file )
        throws IOException
    {
        this( file.getCanonicalPath() );
    }

    public ResettableFileInputStream( final String filename )
        throws IOException
    {
        this( filename, DEFAULT_BUFFER_SIZE );
    }

    public ResettableFileInputStream( final String filename, final int bufferSize )
        throws IOException
    {
        m_bufferSize = bufferSize;
        m_filename = filename;
        m_position = 0;

        m_inputStream = newStream();
    }

    public void mark( final int readLimit )
    {
        m_isMarkSet = true;
        m_mark = m_position;
        m_inputStream.mark( readLimit );
    }

    public boolean markSupported()
    {
        return true;
    }

    public void reset()
        throws IOException
    {
        if( !m_isMarkSet )
        {
            throw new IOException( "Unmarked Stream" );
        }
        try
        {
            m_inputStream.reset();
        }
        catch( final IOException ioe )
        {
            try
            {
                m_inputStream.close();
                m_inputStream = newStream();
                m_inputStream.skip( m_mark );
                m_position = m_mark;
            }
            catch( final Exception e )
            {
                throw new IOException( "Cannot reset current Stream: " + e.getMessage() );
            }
        }
    }

    protected InputStream newStream()
        throws IOException
    {
        return new BufferedInputStream( new FileInputStream( m_filename ), m_bufferSize );
    }

    public int available()
        throws IOException
    {
        return m_inputStream.available();
    }

    public void close() throws IOException
    {
        m_inputStream.close();
    }

    public int read() throws IOException
    {
        m_position++;
        return m_inputStream.read();
    }

    public int read( final byte[] bytes, final int offset, final int length )
        throws IOException
    {
        final int count = m_inputStream.read( bytes, offset, length );
        m_position += count;
        return count;
    }

    public long skip( final long count )
        throws IOException
    {
        m_position += count;
        return m_inputStream.skip( count );
    }
}
