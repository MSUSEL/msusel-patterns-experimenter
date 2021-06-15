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
package org.archive.crawler.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Sorts {

    // Sorts by value not key
    public static StringIntPair[] sortStringIntHashMap (HashMap<String,Integer> hm){
        String[] keys = hm.keySet().toArray(new String[hm.size()]);
        Integer[] values = hm.values().toArray(new Integer[hm.size()]);

        ArrayList<StringIntPair> unsortedList = new ArrayList<StringIntPair>();

        for (int i = 0; i < keys.length; i++)
            unsortedList.add(i, new StringIntPair(keys[i], values[i]));

        StringIntPair[] sortedArray 
         = unsortedList.toArray(new StringIntPair[unsortedList.size()]);
        Arrays.sort(sortedArray, new StringIntPairComparator());

        return sortedArray;
    }

}
