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
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.FeatureSource;
import org.geotools.data.ResourceInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A default implementaiton of a {@link ResourceInfo} based solely on the information that can be
 * obtained from {@link FeatureSource} (schema and bounds)
 * 
 * @author Andrea Aime - GeoSolutions
 */
class DefaultResourceInfo implements ResourceInfo {

    FeatureSource fs;

    Set<String> words;

    public DefaultResourceInfo(FeatureSource fs) {
        this.fs = fs;
        words = new HashSet<String>();
        {
            words.add("features");
            words.add(fs.getSchema().getName().toString());
        }
    }

    @Override
    public ReferencedEnvelope getBounds() {
        try {
            return fs.getBounds();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public CoordinateReferenceSystem getCRS() {
        return fs.getSchema().getCoordinateReferenceSystem();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Set<String> getKeywords() {
        return words;
    }

    @Override
    public String getName() {
        return fs.getSchema().getName().getLocalPart();
    }

    @Override
    public URI getSchema() {
        Name name = fs.getSchema().getName();
        URI namespace;
        try {
            namespace = new URI(name.getNamespaceURI());
            return namespace;
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override
    public String getTitle() {
        Name name = fs.getSchema().getName();
        return name.getLocalPart();
    }

}
