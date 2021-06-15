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
package org.geotools.data.gen;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.Repository;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.gen.info.GeneralizationInfo;
import org.geotools.data.gen.info.GeneralizationInfos;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.view.DefaultView;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * @author Christian Mueller
 * 
 * Datastore for multiple feature types with pregeneralized geometries
 * 
 * The data store is read only, all modifying methods throw an {@link UnsupportedOperationException}
 * 
 * This data store does business as usual with the following exception:
 * 
 * If a method has a {@link Query} parameter and {@link Query#getHints()} includes
 * {@link Hints#GEOMETRY_DISTANCE} with a given distance, the datastore looks for the best fit
 * pregeneralized geometries and returns these geometries instead of the original ones.
 * 
 * This process results in a lower memory usage, lower cpu usage for further processing and will
 * decrease response time for the user.
 * 
 * 
 *
 *
 *
 * @source $URL$
 */
public class PreGeneralizedDataStore implements DataStore {

    public URI getNamespace() {
        return namespace;
    }

    Map<String, PreGeneralizedFeatureSource> featureSources;

    private URI namespace;

    /**
     * @param infos
     * @param repository
     * 
     */
    public PreGeneralizedDataStore(GeneralizationInfos infos, Repository repository) {
        this(infos, repository, null);

    }

    /**
     * @param infos
     * @param repository
     * @param namespace
     */
    public PreGeneralizedDataStore(GeneralizationInfos infos, Repository repository, URI namespace) {
        this.namespace = namespace;
        featureSources = new HashMap<String, PreGeneralizedFeatureSource>();
        for (GeneralizationInfo gi : infos.getGeneralizationInfoCollection()) {
            featureSources.put(gi.getFeatureName(), new PreGeneralizedFeatureSource(gi, repository,
                    this));
        }

    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,
            Transaction transaction) throws IOException {
        PreGeneralizedFeatureSource fs = featureSources.get(query.getTypeName());
        if (fs == null)
            throw new IOException(query.getTypeName() + " not found");
        return fs.getFeatureReader(query, transaction);

    }

    public SimpleFeatureSource getFeatureSource(String typeName)
            throws IOException {
        SimpleFeatureSource fs = featureSources.get(typeName);
        if (fs == null)
            throw new IOException(typeName + " not found");
        return fs;
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Transaction transaction) throws IOException {

        throw new UnsupportedOperationException("getFeatureWriter");
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Filter filter, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("getFeatureWriter");

    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
            Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("getFeatureWriterAppend");
    }

    public LockingManager getLockingManager() {
        throw new UnsupportedOperationException("getLockingManager");

    }

    public SimpleFeatureType getSchema(String typeName) throws IOException {
        return getFeatureSource(typeName).getSchema();
    }

    public String[] getTypeNames() throws IOException {
        Set<String> keys = featureSources.keySet();
        return keys.toArray(new String[keys.size()]);

    }

    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("updateSchema");

    }

    public void createSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("createSchema");

    }

    public void dispose() {
        for (PreGeneralizedFeatureSource fs : featureSources.values()) {
            fs.reset();
        }
    }

    public SimpleFeatureSource getFeatureSource(Name typeName)
            throws IOException {
        return getFeatureSource(typeName.getLocalPart());

    }

    public ServiceInfo getInfo() {
        // TODO
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Generalized Feature Store ");
        info.setSchema(FeatureTypes.DEFAULT_NAMESPACE);
        try {
            info.setPublisher(new URI(System.getProperty("user.name")));
            // TODO IBM SDK problem
            // info.setPublisher( new URI(System.getenv("user.name")) );
        } catch (URISyntaxException e) {
        }
        return info;
    }

    public List<Name> getNames() throws IOException {
        List<Name> nameList = new ArrayList<Name>();
        for (PreGeneralizedFeatureSource fs : featureSources.values()) {
            nameList.add(fs.getName());
        }
        return nameList;
    }

    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getFeatureSource(name).getSchema();

    }

    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("updateSchema");

    }

}
