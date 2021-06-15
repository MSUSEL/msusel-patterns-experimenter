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
/* LineReadingIterator
*
* $Id: RegexpLineIterator.java 5744 2008-02-05 23:30:50Z Gojomo $
*
* Created on Jul 27, 2004
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
package org.archive.util.iterator;

import java.util.Iterator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class providing an Iterator interface over line-oriented
 * text input. By providing regexps indicating lines to ignore
 * (such as pure whitespace or comments), lines to consider input, and
 * what to return from the input lines (such as a whitespace-trimmed
 * non-whitespace token with optional trailing comment), this can
 * be configured to handle a number of formats. 
 * 
 * The public static members provide pattern configurations that will
 * be helpful in a wide variety of contexts. 
 * 
 * @author gojomo
 */
public class RegexpLineIterator 
extends TransformingIteratorWrapper<String,String> {
    private static final Logger logger =
        Logger.getLogger(RegexpLineIterator.class.getName());

    public static final String COMMENT_LINE = "\\s*(#.*)?";
    public static final String NONWHITESPACE_ENTRY_TRAILING_COMMENT = 
        "^\\s*(\\S+)\\s*(#.*)?$";
    public static final String TRIMMED_ENTRY_TRAILING_COMMENT = 
        "^\\s*([^#]+?)\\s*(#.*)?$";

    public static final String ENTRY = "$1";

    protected Matcher ignoreLine = null;
    protected Matcher extractLine = null;
    protected String outputTemplate = null;


    public RegexpLineIterator(Iterator<String> inner, String ignore, 
            String extract, String replace) {
        this.inner = inner;
        ignoreLine = Pattern.compile(ignore).matcher("");
        extractLine = Pattern.compile(extract).matcher("");
        outputTemplate = replace;
    }

    /**
     * Loads next item into lookahead spot, if available. Skips
     * lines matching ignoreLine; extracts desired portion of
     * lines matching extractLine; informationally reports any
     * lines matching neither. 
     * 
     * @return whether any item was loaded into next field
     */
    protected String transform(String line) {
        ignoreLine.reset(line);
        if(ignoreLine.matches()) {
            return null; 
        }
        extractLine.reset(line);
        if(extractLine.matches()) {
            StringBuffer output = new StringBuffer();
            // TODO: consider if a loop that find()s all is more 
            // generally useful here
            extractLine.appendReplacement(output,outputTemplate);
            return output.toString();
        }
        // no match; possibly error
        logger.warning("line not extracted nor no-op: "+line);
        return null;
    }
}
