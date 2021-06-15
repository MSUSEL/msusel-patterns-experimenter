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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.property;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * DOCUMENT ME!
 * 
 * @author jgarnett
 * 
 * 
 * @source $URL$
 * @version $Id
 */
public class PropertyFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.property");

    /** DOCUMENT ME! */
    PropertyAttributeReader reader;

    /**
     * Creates a new PropertyFeatureReader object.
     * 
     * @param directory DOCUMENT ME!
     * @param typeName DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
     */
    public PropertyFeatureReader(File directory, String typeName) throws IOException {
        File file = new File(directory, typeName + ".properties");
        reader = new PropertyAttributeReader(file);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public SimpleFeatureType getFeatureType() {
        return reader.type;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public SimpleFeature next() throws IOException, NoSuchElementException {
        reader.next();

        SimpleFeatureType type = reader.type;
        String fid = reader.getFeatureID();
        Object[] values = new Object[reader.getAttributeCount()];

        for (int i = 0; i < reader.getAttributeCount(); i++) {
            try {
                values[i] = reader.read(i);
            } catch (RuntimeException e) {
                values[i] = null;
            } catch (IOException e) {
                throw e;
            }
        }

        return SimpleFeatureBuilder.build(type, values, fid);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
     */
    public boolean hasNext() throws IOException {
        return reader.hasNext();
    }

    /**
     * DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
     */
    public void close() throws IOException {
        if (reader == null) {
            LOGGER.warning("Stream seems to be already closed.");
        } else {
            reader.close();
        }

        reader = null;
    }
}
