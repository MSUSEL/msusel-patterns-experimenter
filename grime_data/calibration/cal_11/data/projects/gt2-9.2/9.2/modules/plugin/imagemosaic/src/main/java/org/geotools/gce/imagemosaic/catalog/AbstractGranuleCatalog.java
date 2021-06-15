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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;
import java.util.Collection;

import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;

/**
 * 
 *
 * @source $URL$
 */
public class AbstractGranuleCatalog implements GranuleCatalog {

    public void addGranule(SimpleFeature granule, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("This Catalog does not support adding granules ");
    }

    public void addGranules(Collection<SimpleFeature> granules, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("This Catalog does not support adding granules ");
    }

    public void computeAggregateFunction(Query q, FeatureCalc function) throws IOException {
        throw new UnsupportedOperationException("This Catalog does not support Aggregate function");
    }

    public void createType(String namespace, String typeName, String typeSpec) throws IOException, SchemaException {
        throw new UnsupportedOperationException("This Catalog does not support FeatureTypes creation");
    }

    public void createType(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("This Catalog does not support FeatureTypes creation");
    }

    public void createType(String identification, String typeSpec) throws SchemaException, IOException {
        throw new UnsupportedOperationException("This Catalog does not support FeatureTypes creation");
    }

    public void dispose() {
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public BoundingBox getBounds(){
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public Collection<GranuleDescriptor> getGranules(BoundingBox envelope) throws IOException{
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public Collection<GranuleDescriptor> getGranules(Query q) throws IOException{
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public  Collection<GranuleDescriptor> getGranules() throws IOException{
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public  void getGranules(BoundingBox envelope, GranuleCatalogVisitor visitor) throws IOException{
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public  void getGranules(Query q, GranuleCatalogVisitor visitor) throws IOException{
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public QueryCapabilities getQueryCapabilities(){
        return null;
    }

    public SimpleFeatureType getType() throws IOException {
        return null;
    }

    public int removeGranules(Query query) {
        throw new UnsupportedOperationException("This Catalog does not support removing granules");        
    }

}
