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
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.georest;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

/**
 * <p>
 * DataStore that connects to a GeoJson service URL. Requires the actual URL of the service and a
 * comma separated list of type-names (layers).<br/>
 * This DataStore should be able to connect to a rest-like service as defined in the following
 * projects:
 * <ul>
 * <li>Geomajas GeoJson plug-in</li>
 * <li>FeatureServer</li>
 * <li>MapFish server</li>
 * </ul>
 * For a more detailed description, look up the projects documentation.
 * </p>
 * 
 * @author Pieter De Graef, Geosparc
 *
 *
 *
 * @source $URL$
 */
public class GeoRestDataStore extends ContentDataStore {

    private URL url;

    private List<Name> typeNames;

    GeoRestDataStore(Map<String, Serializable> params) throws MalformedURLException {
        url = new URL((String) params.get(GeoRestDataStoreFactory.PARAM_URL));
        String layers = (String) params.get(GeoRestDataStoreFactory.PARAM_LAYERS);
        String[] layerNames = layers.split(",");
        typeNames = new ArrayList<Name>();
        for (int i = 0; i < layerNames.length; i++) {
            typeNames.add(new NameImpl(layerNames[i].trim()));
        }
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        return typeNames;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new GeoRestFeatureSource(entry, null);
    }

    /**
     * @return Returns the base URL for the online GeoJson rest service.
     */
    protected URL getUrl() {
        return url;
    }
}
