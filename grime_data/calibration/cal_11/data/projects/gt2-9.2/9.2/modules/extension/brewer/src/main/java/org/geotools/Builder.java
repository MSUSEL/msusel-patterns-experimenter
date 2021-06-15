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
package org.geotools;

/**
 * Builder interface used to impose consistency on Builder implementations.
 * 
 * @param <T>
 *            class of object under construction
 *
 *
 *
 * @source $URL$
 */
public interface Builder<T> {
    /**
     * Configure the Builder to produce <code>null</code>.
     * <p>
     * This method allows a Builder to be used as a placeholder; in its
     * unset state the build() method will produce <code>null</code>. If
     * any of the builder methods are used the builder will produce a
     * result.
     * 
     * @return Builder configured to build <code>null</code>
     */
    Builder<T> unset();
    /**
     * Configure the Builder to produce a default result.
     * @return Builder configured to produce a default result.
     */
    Builder<T> reset();
    /**
     * Configure the Builder to produce a copy of the provided original.
     * @param origional Original, if null this will behave the same as unset()
     * @return Builder configured to produce the provided original
     */
    Builder<T> reset( T original );    

    /**
     * Created object, may be null if unset()
     * @return Created object may be null if unset()
     */
    T build();
}
