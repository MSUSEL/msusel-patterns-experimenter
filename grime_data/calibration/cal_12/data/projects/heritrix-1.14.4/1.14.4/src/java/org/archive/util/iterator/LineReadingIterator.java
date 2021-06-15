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
* $Id: LineReadingIterator.java 4650 2006-09-25 18:09:42Z paul_jack $
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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Utility class providing an Iterator interface over line-oriented
 * text input, as a thin wrapper over a BufferedReader.
 * 
 * @author gojomo
 */
public class LineReadingIterator extends LookaheadIterator<String> {
    private static final Logger logger =
        Logger.getLogger(LineReadingIterator.class.getName());

    protected BufferedReader reader = null;

    public LineReadingIterator(BufferedReader r) {
        reader = r;
    }

    /**
     * Loads next line into lookahead spot
     * 
     * @return whether any item was loaded into next field
     */
    protected boolean lookahead() {
        try {
            next = this.reader.readLine();
            if(next == null) {
                // TODO: make this close-on-exhaust optional?
                reader.close();
            }
            return (next!=null);
        } catch (IOException e) {
            logger.warning(e.toString());
            return false;
        }
    }
}
