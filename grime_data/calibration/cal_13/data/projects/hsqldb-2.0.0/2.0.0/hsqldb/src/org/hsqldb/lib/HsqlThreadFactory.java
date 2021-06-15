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

package org.hsqldb.lib;

/**
 * The default HSQLDB thread factory implementation.  This factory can be
 * used to wrap other thread factories using the setImpl method, but, by
 * default simply produces new, vanilla thread objects constructed with
 * the supplied runnable object.
 *
 * @author boucherb@users
 * @version 1.7.2
 * @since 1.7.2
 */
class HsqlThreadFactory implements ThreadFactory {

    /**
     * The factory implementation.  Typically, this will be the
     * HsqlThreadFactory object itself.
     */
    protected ThreadFactory factory;

    /**
     * Constructs a new HsqlThreadFactory that uses itself as the factory
     * implementation.
     */
    public HsqlThreadFactory() {
        this(null);
    }

    /**
     * Constructs a new HsqlThreadFactory whose retrieved threads come from the
     * specified ThreadFactory object or from this factory implementation, if'
     * the specified implementation is null.
     *
     * @param f the factory implementation this factory uses
     */
    public HsqlThreadFactory(ThreadFactory f) {
        setImpl(f);
    }

    /**
     * Retreives a thread instance for running the specified Runnable
     * @param r The runnable that the retrieved thread handles
     * @return the requested thread inatance
     */
    public Thread newThread(Runnable r) {
        return factory == this ? new Thread(r)
                               : factory.newThread(r);
    }

    /**
     * Sets the factory implementation that this factory will use to
     * produce threads.  If the specified argument, f, is null, then
     * this factory uses itself as the implementation.
     *
     * @param f the factory implementation that this factory will use
     *      to produce threads
     * @return the previously installed factory implementation
     */
    public synchronized ThreadFactory setImpl(ThreadFactory f) {

        ThreadFactory old;

        old     = factory;
        factory = (f == null) ? this
                              : f;

        return old;
    }

    /**
     * Retrieves the factory implementation that this factory is using
     * to produce threads.
     *
     * @return the factory implementation that this factory is using to produce
     * threads.
     */
    public synchronized ThreadFactory getImpl() {
        return factory;
    }
}
