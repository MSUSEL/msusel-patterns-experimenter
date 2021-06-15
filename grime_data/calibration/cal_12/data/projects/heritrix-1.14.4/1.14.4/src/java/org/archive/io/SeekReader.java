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
/* SeekReader
*
* Created on September 18, 2006
*
* Copyright (C) 2006 Internet Archive.
*
* This file is part of the Heritrix web crawler (crawler.archive.org).
*
* Heritrix is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser Public License as published by
* the Free Software Foundation; either version 2.1 of the License, or
* any later version.
*
* Heritrix is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser Public License for more details.
*
* You should have received a copy of the GNU Lesser Public License
* along with Heritrix; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.archive.io;


import java.io.IOException;
import java.io.Reader;

import it.unimi.dsi.fastutil.io.RepositionableStream;


/**
 * Base class for repositionable readers.
 * 
 * @author pjack
 */
public abstract class SeekReader extends Reader 
implements RepositionableStream {


    /**
     * The marked file position.  A value less than zero
     * indicates that no mark has been set.
     */
    private long mark = -1;


    /**
     * Marks the current position of the stream.  The limit parameter is
     * ignored; the mark will remain valid until reset is called or the
     * stream is closed.
     * 
     * @param limit  ignored
     */
    @Override
    public void mark(int limit) {
        try {
            this.mark = position();
        } catch (IOException e) {
            mark = -1;
        }
    }


    /**
     * Resets this stream to its marked position.
     * 
     * @throws IOException  if there is no mark, or if an IO error occurs
     */
    @Override
    public void reset() throws IOException {
        if (mark < 0) {
            throw new IOException("No mark.");
        }
        position(mark);
    }


    /**
     * Returns true, since SeekInputStreams support mark/reset by default.
     * 
     * @return true
     */
    @Override
    public boolean markSupported() {
        return true;
    }
}
