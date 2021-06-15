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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.opengis.annotation.Specification;

import static org.junit.Assert.*;


/**
 * Scans every classes in the OpenGIS classpath.
 *
 * @author Martin Desruisseaux (IRD)
 *
 * @source $URL$
 */
final class ClassScanner implements Iterator<Class<?>> {
    /**
     * Extension for class files.
     */
    private static final String EXTENSION = ".class";

    /**
     * The classes to load.
     */
    private final List<String> classNames = new ArrayList<String>();

    /**
     * Index of the next element to return.
     */
    private int index = 0;

    /**
     * Creates a new instance of {@code ClassScanner}.
     */
    public ClassScanner() {
        final URL url = Specification.class.getResource("Specification.class");
        assertNotNull(url);
        final File classFile;
        try {
            classFile = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
        add(classFile.getParentFile().getParentFile(), "org.opengis");
    }

    /**
     * Adds every class names found in the specified directory and sub-directories.
     */
    private void add(final File directory, final String packageName) {
        assertTrue(directory.isDirectory());
        final StringBuilder b = new StringBuilder(packageName).append('.');
        final int base = b.length();
        for (final File file : directory.listFiles()) {
            String name = file.getName();
            if (file.isDirectory()) {
                add(file, packageName + '.' + name);
            } else if (name.endsWith(EXTENSION)) {
                name = name.substring(0, name.length() - EXTENSION.length());
                b.setLength(base);
                classNames.add(b.append(name).toString());
            }
        }
    }

    /**
     * Returns {@code true} if there is more OpenGIS classes or interfaces in the classpath.
     */
    public boolean hasNext() {
        return index < classNames.size();
    }

    /**
     * Returns the next OpenGIS class or interface in the classpath.
     */
    public Class<?> next() {
        if (hasNext()) try {
            return Class.forName(classNames.get(index++));
        } catch (ClassNotFoundException e) {
            fail(e.getLocalizedMessage());
        }
        throw new NoSuchElementException();
    }

    /**
     * Unsupported operation.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
