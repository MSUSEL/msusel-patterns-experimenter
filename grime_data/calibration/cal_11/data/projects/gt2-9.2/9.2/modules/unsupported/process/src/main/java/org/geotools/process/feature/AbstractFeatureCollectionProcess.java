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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geotools.process.feature;

import java.util.Map;

import org.geotools.process.impl.AbstractProcess;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A Process for feature collections.
 *
 * @author Justin Deoliveira, OpenGEO
 * @author Michael Bedward
 * @since 2.6
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class AbstractFeatureCollectionProcess extends AbstractProcess {

    /**
     * Constructor
     *
     * @param factory
     */
    public AbstractFeatureCollectionProcess(AbstractFeatureCollectionProcessFactory factory) {
        super(factory);
    }

    /**
     * Performs an operation on a single feature in the collection.
     * <p>
     * This method should do some work based on the feature and then set any attributes on the feature
     * as necessary. Example of a simple buffering operation:
     * <pre>
     * protected void processFeature(SimpleFeature feature, Map<String, Object> input) throws Exception {
     *    Double buffer = (Double) input.get( BufferFeatureCollectionFactory.BUFFER.key );
     *
     *    Geometry g = (Geometry) feature.getDefaultGeometry();
     *    g = g.buffer( buffer );
     *
     *    feature.setDefaultGeometry( g );
     * }
     * </pre>
     * </p>
     *
     * @param feature the feature being processed
     * @param input a Map of input parameters
     *
     * @throws Exception
     */
    protected abstract void processFeature( SimpleFeature feature, Map<String,Object> input )
        throws Exception;
}
