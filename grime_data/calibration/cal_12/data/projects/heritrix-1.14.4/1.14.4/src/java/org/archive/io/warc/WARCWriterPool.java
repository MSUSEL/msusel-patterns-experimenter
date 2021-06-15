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
package org.archive.io.warc;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.archive.io.WriterPool;
import org.archive.io.WriterPoolMember;
import org.archive.io.WriterPoolSettings;


/**
 * A pool of WARCWriters.
 * @author stack
 * @version $Revision: 4566 $ $Date: 2006-08-31 09:51:41 -0700 (Thu, 31 Aug 2006) $
 */
public class WARCWriterPool extends WriterPool {
    /**
     * Constructor
     * @param settings Settings for this pool.
     * @param poolMaximumActive
     * @param poolMaximumWait
     */
    public WARCWriterPool(final WriterPoolSettings settings,
            final int poolMaximumActive, final int poolMaximumWait) {
    	this(new AtomicInteger(), settings, poolMaximumActive, poolMaximumWait);
    }
    
    /**
     * Constructor
     * @param serial  Used to generate unique filename sequences
     * @param settings Settings for this pool.
     * @param poolMaximumActive
     * @param poolMaximumWait
     */
    public WARCWriterPool(final AtomicInteger serial,
    		final WriterPoolSettings settings,
            final int poolMaximumActive, final int poolMaximumWait) {
    	super(serial, new BasePoolableObjectFactory() {
            public Object makeObject() throws Exception {
                return new WARCWriter(serial,
                		settings.getOutputDirs(),
                        settings.getPrefix(), settings.getSuffix(),
                        settings.isCompressed(), settings.getMaxSize(),
                        settings.getMetadata());
            }

            public void destroyObject(Object writer)
            throws Exception {
                ((WriterPoolMember)writer).close();
                super.destroyObject(writer);
            }
    	}, settings, poolMaximumActive, poolMaximumWait);
    }
}
