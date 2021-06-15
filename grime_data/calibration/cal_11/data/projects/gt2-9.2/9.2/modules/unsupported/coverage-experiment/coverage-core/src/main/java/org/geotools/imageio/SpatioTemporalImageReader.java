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
package org.geotools.imageio;

import java.io.IOException;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.imageio.metadata.SpatioTemporalMetadata;

/**
 * Class leveraging on an underlying imageio flat reader to extract
 * spatio-temporal information and setup proper spatio-temporal metadata. A flat
 * reader simply allows to get data access as well as exposing basic metadata
 * available for the underlying source. Basic metadata are usually, dataset
 * width/height, name, tiling and similar things. Moreover a flat reader may
 * returns attributes and properties of a whole data source by means of a
 * StreamMetadata instance, as well as a set of "single dataset" specific
 * attributes on which the spatioTemporal reader may leverage to expose spatio-
 * temporal metadata.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 *
 *
 *
 * @source $URL$
 */
public abstract class SpatioTemporalImageReader extends ImageReader {

    protected SpatioTemporalImageReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
    }

    /**
     * Returns the {@link SliceDescriptor} describing a specified 2D raster
     * 
     * @param imageIndex
     *                the index of the required 2D raster
     * @return a {@link SliceDescriptor}object.
     * @throws IOException
     */
    public abstract SliceDescriptor getSliceDescriptor(int imageIndex) throws IOException;

    /**
     * Returns a {@link SpatioTemporalMetadata} instance for a specified 2D
     * raster
     * 
     * @param imageIndex
     *                the index of the required 2D raster
     * @return a {@link SpatioTemporalMetadata} object.
     */
    public abstract SpatioTemporalMetadata getSpatioTemporalMetadata(int imageIndex);
}
