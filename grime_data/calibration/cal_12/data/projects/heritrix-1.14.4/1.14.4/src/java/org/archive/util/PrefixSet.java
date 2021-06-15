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
package org.archive.util;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Utility class for maintaining sorted set of string prefixes.
 * Redundant prefixes are coalesced into the shorter prefix. 
 */
public class PrefixSet extends TreeSet<String> {
    private static final long serialVersionUID = -6054697706348411992L;

    public PrefixSet() {
        super();
    }

    /**
     * Test whether the given String is prefixed by one
     * of this set's entries. 
     * 
     * @param s
     * @return True if contains prefix.
     */
    public boolean containsPrefixOf(String s) {
        SortedSet sub = headSet(s);
        // because redundant prefixes have been eliminated,
        // only a test against last item in headSet is necessary
        if (!sub.isEmpty() && s.startsWith((String)sub.last())) {
            return true; // prefix substring exists
        } // else: might still exist exactly (headSet does not contain boundary)
        return contains(s); // exact string exists, or no prefix is there
    }
    
    /** 
     * Maintains additional invariant: if one entry is a 
     * prefix of another, keep only the prefix. 
     * 
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(String s) {
        SortedSet<String> sub = headSet(s);
        if (!sub.isEmpty() && s.startsWith((String)sub.last())) {
            // no need to add; prefix is already present
            return false;
        }
        boolean retVal = super.add(s);
        sub = tailSet(s+"\0");
        while(!sub.isEmpty() && ((String)sub.first()).startsWith(s)) {
            // remove redundant entries
            sub.remove(sub.first());
        }
        return retVal;
    }
    
}