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
package org.archive.io;

import it.unimi.dsi.fastutil.io.RepositionableStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper around an {@link InputStream} to make a primitive Repositionable
 * stream. Uses a {@link BufferedInputStream}.  Calls mark on every read so
 * we'll remember at least the last thing read (You can only backup on the
 * last thing read -- not last 2 or 3 things read).  Used by
 * {@link GzippedInputStream} when reading streams over a network.  Wraps a
 * HTTP, etc., stream so we can back it up if needs be after the
 * GZIP inflater has done a fill of its full buffer though it only needed
 * the first few bytes to finish decompressing the current GZIP member.
 * 
 * <p>TODO: More robust implementation.  Tried to use the it.unimi.dsi.io
 * FastBufferdInputStream but relies on FileChannel ByteBuffers and if not
 * present -- as would be the case reading from a network stream, the main
 * application for this instance -- then it expects the underlying stream 
 * implements RepositionableStream interface so chicken or egg problem.
 * @author stack
 */
public class RepositionableInputStream extends BufferedInputStream implements
        RepositionableStream {
    private long position = 0;
    private long markPosition = -1;
    
    public RepositionableInputStream(InputStream in) {
        super(in);
    }
    
    public RepositionableInputStream(InputStream in, int size) {
        super(in, size);
    }

    public int read(byte[] b) throws IOException {
        int read = super.read(b);
        if (read != -1) {
            position += read;
        }
        return read;
    }
    
    public synchronized int read(byte[] b, int offset, int ct)
    throws IOException {
        // Mark the underlying stream so that we'll remember what we are about
    	// to read unless a mark has been set in this RepositionableStream
    	// (We have two levels of mark).  In this latter case we want the
    	// underlying stream to preserve its mark position so aligns with
    	// this RS when eset is called.
    	if (!isMarked()) {
    		super.mark((ct > offset)? ct - offset: ct);
    	}
        int read = super.read(b, offset, ct);
        if (read != -1) {
            position += read;
        }
        return read;
    }
    
    public int read() throws IOException {
        // Mark the underlying stream so that we'll remember what we are about
    	// to read unless a mark has been set in this RepositionableStream
    	// (We have two levels of mark).  In this latter case we want the
    	// underlying stream to preserve its mark position so aligns with
    	// this RS when eset is called.
    	if (!isMarked()) {
    		super.mark(1);
    	}
        int c = super.read();
        if (c != -1) {
            position++;
        }
        return c;
    }

    public void position(final long offset) {
        if (this.position == offset) {
            return;
        }
        int diff =  (int)(offset - this.position);
        long lowerBound = this.position - this.pos;
        long upperBound = lowerBound + this.count;
        if (offset < lowerBound || offset >= upperBound) {
            throw new IllegalAccessError("Offset goes outside " +
                "current this.buf (TODO: Do buffer fills if positive)");
        }
        this.position = offset;
        this.pos += diff;
        // Clear any mark.
        this.markPosition = -1;
    }

    public void mark(int readlimit) {
        this.markPosition = this.position;
        super.mark(readlimit);
    }

    public void reset() throws IOException {
        super.reset();
        this.position = this.markPosition;
        this.markPosition = -1;
    }
    
    protected boolean isMarked() {
    	return this.markPosition != -1;
    }

    public long position() {
        return this.position;
    }
}