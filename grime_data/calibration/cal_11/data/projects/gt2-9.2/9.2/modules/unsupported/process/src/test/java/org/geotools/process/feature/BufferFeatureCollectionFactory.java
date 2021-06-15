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

import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.process.feature.FeatureToFeatureProcessFactory;
import org.geotools.text.Text;
import org.opengis.util.InternationalString;

/**
 * Factory for process which buffers an entire feature collection.
 * 
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 *
 *
 *
 *
 * @source $URL$
 */
public class BufferFeatureCollectionFactory extends FeatureToFeatureProcessFactory {

    /** Buffer amount */
    public static final Parameter<Double> BUFFER = new Parameter<Double>("buffer",
            Double.class, Text.text("Buffer Amount"), Text.text("Amount to buffer each feature by"));

    public InternationalString getTitle() {
        return Text.text("Buffer Features");
    }
    
    public InternationalString getDescription() {
        return Text.text("Buffer each Feature in a Feature Collection");
    }
    
    @Override
    protected void addParameters(Map<String, Parameter<?>> parameters) {
        parameters.put(BUFFER.key, BUFFER);
    }
    
    public BufferFeatureCollectionProcess create() throws IllegalArgumentException {
        return new BufferFeatureCollectionProcess(this);
    }
}
