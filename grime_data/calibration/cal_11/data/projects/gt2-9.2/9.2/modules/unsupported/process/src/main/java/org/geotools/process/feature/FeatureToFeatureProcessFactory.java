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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.feature;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.feature.FeatureCollection;
import org.geotools.process.ProcessFactory;
import org.geotools.text.Text;

/**
 * Base class for process factories which perform an operation on each feature in a feature 
 * collection with the result being a feature collection (the original collection modified
 * or a new collection).
 * <p>
 * <b>Note</b>: This base class is intended to be used for processes which operate on each feature in a feature 
 * collection, resulting in a new feature collection which has the same schema as the original.
 * </p>
 * <p>
 * Subclasses must implement:
 * <ul>
 *   <li>{@link ProcessFactory#getTitle()}
 *   <li>{@link ProcessFactory#getDescription()}
 *   <li>{@link #addParameters(Map)}
 *   <li>
 * </ul>
 * </p>
 * 
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class FeatureToFeatureProcessFactory extends AbstractFeatureCollectionProcessFactory {

    private static final String VERSION = "1.0.0";

    /** 
     * Result of the operation is a FeatureCollection.
     * This can be the input FeatureCollection, modified by the process
     * or a new FeatureCollection.
     */
    public static final Parameter<FeatureCollection> RESULT = new Parameter<FeatureCollection>(
            "result", FeatureCollection.class, Text.text("Result"), Text
                    .text("Buffered features"));
    
    static final Map<String,Parameter<?>> resultInfo = new HashMap<String, Parameter<?>>();

    static {
        resultInfo.put( RESULT.key, RESULT );
    }
    
    public final Map<String, Parameter<?>> getResultInfo(
            Map<String, Object> parameters) throws IllegalArgumentException {
        return Collections.unmodifiableMap( resultInfo );
    }
    
    public final boolean supportsProgress() {
        return true;
    }

    public String getVersion() {
        return VERSION;
    }
}
