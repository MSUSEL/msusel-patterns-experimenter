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
/* ArrayLongFPCache
*
* $Id: ArrayLongFPCache.java 3870 2005-10-06 05:01:49Z gojomo $
*
* Created on Oct 5, 2005
*
* Copyright (C) 2005 Internet Archive.
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
package org.archive.util.fingerprint;

/**
 * Simple long fingerprint cache using a backing array; any long maps to 
 * one of 'smear' slots. Longs inserted should be randomly distributed, 
 * 
 * @author gojomo
 */
public class ArrayLongFPCache implements LongFPSet {
    public static final int DEFAULT_CAPACITY = 1 << 20; // 1 million, 8MB
    public static final int DEFAULT_SMEAR = 5; 

    long cache[] = new long[DEFAULT_CAPACITY];
    int smear = DEFAULT_SMEAR;
    int count = 0;
    
    public void setCapacity(int newCapacity) {
        long[] oldCache = cache;
        cache = new long[newCapacity];
        for(int i=0;i<oldCache.length;i++) {
            add(oldCache[i]);
        }
    }
    
    /* (non-Javadoc)
     * @see org.archive.util.fingerprint.LongFPSet#add(long)
     */
    public boolean add(long l) {
        if(contains(l)) {
            return false; 
        }
        int index = (Math.abs((int) (l % cache.length)) + (count % smear)) % cache.length;
        count++;
        cache[index]=l;
        return true;
    }

    /* (non-Javadoc)
     * @see org.archive.util.fingerprint.LongFPSet#contains(long)
     */
    public boolean contains(long l) {
        int index = Math.abs((int) (l % cache.length));
        for(int i = index; i < index + smear; i++) {
            if(cache[i%cache.length]==l) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.archive.util.fingerprint.LongFPSet#remove(long)
     */
    public boolean remove(long l) {
        int index = Math.abs((int) (l % cache.length));
        for(int i = index; i < index + smear; i++) {
            if(cache[i%cache.length]==l) {
                cache[i%cache.length]=0;
                count = Math.min(count,cache.length);
                count--;
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.archive.util.fingerprint.LongFPSet#count()
     */
    public long count() {
        return Math.min(count,cache.length);
    }

    /* (non-Javadoc)
     * @see org.archive.util.fingerprint.LongFPSet#quickContains(long)
     */
    public boolean quickContains(long fp) {
        return contains(fp);
    }
    
    public int cacheLength() {
        return cache.length;
    }

}
