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
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
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
package org.geotools.data.edigeo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.data.FeatureReader;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.IllegalAttributeException;

/**
 * <p>
 * Private FeatureReader inner class for reading Features from the MIF file
 * </p>
 *
 *
 *
 *
 * @source $URL$
 */
public class EdigeoFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    EdigeoVEC vecParser = null;
    private SimpleFeatureType ft;
    private ArrayList<SimpleFeature> featureList;
    private Iterator<SimpleFeature> featureListIterator = null;

    protected class Visitor {
        public void visit(Object[] values, String fid)
                throws IllegalAttributeException {
            // create feature and add it to list
            SimpleFeature f = SimpleFeatureBuilder.build(ft, values, fid);
            featureList.add(f);
        }
    }

    public EdigeoFeatureReader (File dir, String filename, String obj, SimpleFeatureType ft)
            throws IOException, IllegalAttributeException {
        this.ft = ft;
        featureList = new ArrayList<SimpleFeature>();
        vecParser = new EdigeoVEC(dir.getParentFile().getPath() + "/" + filename);
        vecParser.readVECFile(obj, new Visitor());
    }

    public SimpleFeatureType getFeatureType() {
        return ft;
    }

    public SimpleFeature next()
            throws IOException, IllegalAttributeException, NoSuchElementException {
        if (featureListIterator == null) {
            featureListIterator = featureList.iterator();
        }
        return featureListIterator.next();

    }

    public boolean hasNext() throws IOException {
        if (featureListIterator == null) {
            featureListIterator = featureList.iterator();
        }
        return featureListIterator.hasNext();
    }

    public void close() throws IOException {
    }
}
