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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.regex.Pattern;


/**
 * A {@link FileFilter} implementation using Unix-style wildcards.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.0
 */
public class DefaultFileFilter extends javax.swing.filechooser.FileFilter
        implements FileFilter, FilenameFilter
{
    /**
     * The description of this filter, usually for graphical user interfaces.
     */
    private final String description;

    /**
     * The pattern to matchs to filenames.
     */
    private final Pattern pattern;

    /**
     * Constructs a file filter for the specified pattern.
     * The pattern can contains the {@code "*"} and {@code "?"} wildcards.
     *
     * @param pattern The pattern (e.g. {@code "*.png"}).
     */
    public DefaultFileFilter(final String pattern) {
        this(pattern, new File(pattern).getName());
    }

    /**
     * Constructs a file filter for the specified pattern and description.
     * The pattern can contains the {@code "*"} and {@code "?"} wildcards.
     *
     * @param pattern The pattern (e.g. {@code "*.png"}).
     * @param description The description of this filter, usually for graphical user interfaces.
     */
    public DefaultFileFilter(final String pattern, final String description) {
        this.description = description.trim();
        final int length = pattern.length();
        final StringBuilder buffer = new StringBuilder(length + 8);
        for (int i=0; i<length; i++) {
            final char c = pattern.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                switch (c) {
                    case '?': buffer.append('.' ); continue;
                    case '*': buffer.append(".*"); continue;
                    default : buffer.append('\\'); break;
                }
            }
            buffer.append(c);
        }
        this.pattern = Pattern.compile(buffer.toString());
    }

    /**
     * Returns the description of this filter. For example: {@code "PNG images"}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Tests if a specified file matches the pattern.
     *
     * @param  file The file to be tested.
     * @return {@code true} if and only if the name matches the pattern.
     */
    public boolean accept(final File file) {
        return (file != null) && pattern.matcher(file.getName()).matches();
    }

    /**
     * Tests if a specified file matches the pattern.
     *
     * @param  directory The directory in which the file was found.
     * @param  name The name of the file.
     * @return {@code true} if and only if the name matches the pattern.
     */
    public boolean accept(final File directory, final String name) {
        return (name != null) && pattern.matcher(name).matches();
    }
}
