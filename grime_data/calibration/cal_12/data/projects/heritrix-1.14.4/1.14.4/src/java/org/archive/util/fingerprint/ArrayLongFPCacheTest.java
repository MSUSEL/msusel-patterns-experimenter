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
/* ArrayLongFPCacheTest
*
* $Id: ArrayLongFPCacheTest.java 3870 2005-10-06 05:01:49Z gojomo $
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

import junit.framework.TestCase;

/**
 * Unit tests for ArrayLongFPCache. 
 * 
 * @author gojomo
 */
public class ArrayLongFPCacheTest extends TestCase {

    public void testAdd() {
        long testVal = 123456L;
        ArrayLongFPCache cache = new ArrayLongFPCache();
        assertFalse("contains test value pre-add",cache.contains(testVal));
        assertFalse("contains test value pre-add",cache.contains(-testVal));
        cache.add(testVal);
        cache.add(-testVal);
        assertTrue("should contain after add",cache.contains(testVal));
        assertTrue("should contain after add",cache.contains(-testVal));
    }

    public void testContains() {
        long testVal1 = 123456L;
        long testVal2 = 9090909090L;
        long testVal3 = 76543210234567L;
        long testVal4 = 1L;
        ArrayLongFPCache cache = new ArrayLongFPCache();
        cache.add(testVal1);
        cache.add(testVal2);
        cache.add(testVal3);
        cache.add(testVal4);
        assertTrue("should contain after add",cache.contains(testVal1));
        assertTrue("should contain after add",cache.contains(testVal2));
        assertTrue("should contain after add",cache.contains(testVal3));
        assertTrue("should contain after add",cache.contains(testVal4));
    }

    public void testReplacement() {
        ArrayLongFPCache cache = new ArrayLongFPCache();
        for(long i=0; i<=ArrayLongFPCache.DEFAULT_SMEAR; i++) {
            cache.add(i*cache.cacheLength()+1);
        }
        assertFalse("contains value after overwrite",cache.contains(1L));
        assertTrue("value not retained",cache.contains(cache.cacheLength()+1));

    }
    
    public void testRemove() {
        long testVal = 4516500024601L;
        ArrayLongFPCache cache = new ArrayLongFPCache();
        cache.add(testVal);
        cache.add(-testVal);
        assertTrue("should contain after add",cache.contains(testVal));
        assertTrue("should contain after add",cache.contains(-testVal));
        cache.remove(testVal);
        cache.remove(-testVal);
        assertFalse("contains test value after remove",cache.contains(testVal));
        assertFalse("contains test value after remove",cache.contains(-testVal));
    }

}
