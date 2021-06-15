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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * An OGR feature reader, reads data from the provided layer.<br>
 * It assumes eventual filters have already been set on it, and will extract only the
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/ogr/src/main/java/org/geotools
 *         /data/ogr/OGRFeatureReader.java $
 */
class OGRFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    Object dataSource;

    Object layer;

    SimpleFeatureType schema;

    Object curr;

    private FeatureMapper mapper;

    boolean layerCompleted;

    OGR ogr;

    public OGRFeatureReader(Object dataSource, Object layer,
            SimpleFeatureType targetSchema, SimpleFeatureType originalSchema, GeometryFactory gf, OGR ogr) {
        this.dataSource = dataSource;
        this.layer = layer;
        this.schema = targetSchema;

        ogr.LayerResetReading(layer);

        this.layerCompleted = false;
        this.mapper = new FeatureMapper(targetSchema, layer, gf, ogr);
        this.ogr = ogr;
        // TODO: mark as ignored all the fields we don't want to handle, as well as ignoring
        // the per feature style, assuming the caps say we can
    }

    public void close() throws IOException {
        if (curr != null) {
            ogr.FeatureDestroy(curr);
            curr = null;
        }
        if (layer != null) {
            ogr.LayerRelease(layer);
            layer = null;
        }
        if (dataSource != null) {
            ogr.DataSourceRelease(dataSource);
            dataSource = null;
        }
        schema = null;
    }

    protected void finalize() throws Throwable {
        close();
    }

    public SimpleFeatureType getFeatureType() {
        return schema;
    }

    public boolean hasNext() throws IOException {
        // ugly, but necessary to close the reader when getting to the end, because
        // it would break feature appending otherwise (the reader is used in feature
        // writing too)
        if (layerCompleted) {
            return false;
        }

        if (curr == null) {
            curr = ogr.LayerGetNextFeature(layer);
        }
        if (curr != null) {
            return true;
        } else {
            layerCompleted = true;
            return false;
        }
    }

    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (!hasNext())
            throw new NoSuchElementException("There are no more Features to be read");

        SimpleFeature f = mapper.convertOgrFeature(curr);

        // .. nullify curr, so that we can move to the next one
        ogr.FeatureDestroy(curr);
        curr = null;

        return f;
    }

}
