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
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.raster.io;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;

import org.opengis.coverage.grid.GridEnvelope;

/**
 * An Iterator like interface to read ArcSDE rasters for a given ArcSDE raster dataset (whether it
 * is a single raster or a raster catalog).
 * <p>
 * Sample usage: <code> 
 * <pre>
 * RasterReaderFactory readerFactory = ....
 * RasterDatasetInfo raserInfo = ...
 * ArcSDERasterReader reader = readerFactory.create(rasterInfo);
 * try{
 *      Long nextRasterId;
 *      while((nextRasterId = reader.nextRaster()) != null){
 *          if(amIInterestedInThisRaster(nextRasterId)){
 *              int pyramidLevel = ...
 *              Rectangle tileRange = ...
 *              RenderedImage raster = reader.read(pyramidLevel, tileRange);
 *          }
 *      }
 * }finally{
 *      reader.dispose();
 * }
 * </pre>
 * </code>
 * </p>
 * <p>
 * So one has to call {@code nextRaster()} to get the id of the raster immediately available to be
 * read through {@link #read()}. This is so because there might be more than one raster on a raster
 * dataset and the order they are fetched from the ArcSDE server is non deterministic, and once you
 * opened a stream to a raster you can't open another one and then read the former.
 * </p>
 * 
 * @author Gabriel Roldan
 *
 *
 * @source $URL$
 * @version $Id$
 * @since 2.5.7
 */
public interface TiledRasterReader {

    /**
     * Disposes any resource being held by this reader, whether it's a connection to the ArcSDE
     * server, opened streams, etc.
     */
    // void dispose();

    /**
     * Advances to the next available raster in the raster dataset this reader works upon and
     * returns it's {@link SeRasterAttr#getRasterId() raster id}.
     * 
     * @return the ID for the raster ready to be read from the queried raster column in the raster
     *         dataset, or {@code null} if there are no more rasters to be read.
     * @throws IOException
     *             for any problem occurred retrieving the next {@link SeRasterAttr} in the request
     */
    // Long nextRaster() throws IOException;

    /**
     * Reads the image subset determined by the given pyramid level and tile range for the currently
     * available raster attribute in the requested raster column for the given raster dataset.
     * 
     * @param pyramidLevel
     *            the pyramid level to read
     * @param matchingTiles
     *            the range of tiles to read at the given pyramid level. The boundaries of the tile
     *            range are inclusive and starts at {@code 0,0} for the upper left most tile.
     * @return the rendered image determined by the requested pyramid level and tile range
     * @throws IOException
     *             for any exception occurred while reading the image
     */
    RenderedImage read(final long rasterId, final int pyramidLevel, final GridEnvelope matchingTiles)
            throws IOException;

}
