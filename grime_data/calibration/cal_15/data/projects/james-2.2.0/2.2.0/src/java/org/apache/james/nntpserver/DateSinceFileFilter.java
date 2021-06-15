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
package org.apache.james.nntpserver;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filters files according to their last modified date
 *
 */
public class DateSinceFileFilter implements FilenameFilter {

    /**
     * The date that serves as the lower bound of the region of 
     * interest
     */
    private final long m_date;

    /**
     * Creates a new FileFilter that returns all Files that
     * have been modified since the date specified.
     *
     * @param date the date that serves as the lower bound of the region of 
     * interest
     */
    public DateSinceFileFilter( long date ) {
        m_date = date;
    }

    /**
     * Tests if a specified file has been modified since a
     * specified date.
     *
     * @param dir the directory in which the file was found
     * @param name the name of the file
     *
     * @return true if the file meets the criteria, false otherwise
     */
    public boolean accept( final File dir, final String name ) {
        return (new File(dir,name).lastModified() >= m_date);
    }
}
