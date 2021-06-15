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

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;

/**
 * <p>
 * Factory for the GeoRest DataStore. Required parameters are:
 * <ul>
 * <li><b>url</b>: The URL that points to the GeoRest service.</li>
 * <li><b>layers</b>: Comma separated list of known layers for the GeoRest service.</li>
 * </ul>
 * </p>
 * <p>
 * The created DataStore should be able to connect to a rest-like service that provides the GeoJson
 * format as defined in the following projects:
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
public class GeoRestDataStoreFactory implements DataStoreFactorySpi {

    /**
     * Parameter that points to the base URL of the rest service.
     */
    protected static final String PARAM_URL = "url";

    /**
     * Parameter that specifies the supported list of layers (type-names) as a comma separated
     * string.
     */
    protected static final String PARAM_LAYERS = "layers";

    private List<Param> parameters = new ArrayList<Param>();

    /**
     * Create a new factory for GeoJson based rest-like services.
     */
    public GeoRestDataStoreFactory() {
        parameters.add(new Param(PARAM_URL, String.class, "The base URL for the GeoJson service."));
        parameters
                .add(new Param(PARAM_LAYERS, String.class, "Comma separated list of layer names."));
    }

    /**
     * Create a new {@link GeoRestDataStore}, given the list of parameters.
     * 
     * @param params
     *            The actual list of parameters. Must contain the 'url' and 'layers' parameters.
     */
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        return new GeoRestDataStore(params);
    }

    /**
     * Returns null.
     */
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return null;
    }

    /**
     * Returns 'GeoJsonDataStoreFactory'.
     */
    public String getDisplayName() {
        return "GeoJsonDataStoreFactory";
    }

    /**
     * Checks to see if the 'url' and 'layers' parameters are available in the given parameter list.
     * 
     * @param params
     *            The list of parameters that need checking.
     * @return Returns true or false indicating if the given parameter list is suitable to create a
     *         {@link GeoRestDataStore}.
     */
    public boolean canProcess(Map<String, Serializable> params) {
        for (Param p : parameters) {
            if (!params.containsKey(p.key)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns an empty string.
     */
    public String getDescription() {
        return "";
    }

    public Param[] getParametersInfo() {
        return parameters.toArray(new Param[] {});
    }

    public boolean isAvailable() {
        return true;
    }

    public Map<Key, ?> getImplementationHints() {
        return null;
    }
}
