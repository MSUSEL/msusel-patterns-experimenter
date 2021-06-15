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
/* PatternMatcherRecycler
*
* $Id: PatternMatcherRecycler.java 4644 2006-09-20 22:40:21Z paul_jack $
*
* Created on Dec 21, 2004
*
* Copyright (C) 2004 Internet Archive.
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
package org.archive.util;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to retain a compiled Pattern and multiple corresponding 
 * Matcher instances for reuse.
 * 
 * @author gojomo
 */
public class PatternMatcherRecycler {
    /**
     * Upper-bound on Matcher Stacks.
     * Profiling has the size of these Stacks tending upward over
     * the life of a crawl.  TODO: do something better than an
     * a coarse upperbound; do something that can get GC'd in
     * low-memory conditions.
     */
    private final static int MAXIMUM_STACK_SIZE = 10;
    
    private Pattern pattern;
    private Stack<Matcher> matchers;

    public PatternMatcherRecycler(Pattern p) {
        this.pattern = p;
        this.matchers = new Stack<Matcher>();
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    /**
     * Get a Matcher for the internal Pattern, against the given
     * input sequence. Reuse an old Matcher if possible, otherwise
     * create a new one. 
     * 
     * @param input CharSequence to match
     * @return Matcher set against the the input sequence
     */
    public Matcher getMatcher(CharSequence input) {
        if (input == null) {
            throw new IllegalArgumentException("CharSequence 'input' must not be null");
        }
        try {
            return ((Matcher)matchers.pop()).reset(input);
        } catch (EmptyStackException e) {
            return this.pattern.matcher(input);
        }
    }
    
    /**
     * Return the given Matcher to the reuse stack, if stack is
     * not already at its maximum size.
     * 
     * @param m the Matcher to save for reuse
     */
    public void freeMatcher(Matcher m) {
        if(this.matchers.size() < MAXIMUM_STACK_SIZE) {
            matchers.push(m);
        }
    }
}

