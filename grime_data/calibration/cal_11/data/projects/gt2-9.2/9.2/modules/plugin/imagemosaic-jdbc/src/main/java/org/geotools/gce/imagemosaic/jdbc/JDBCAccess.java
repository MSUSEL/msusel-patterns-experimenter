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
 *    GeoTools+ - The Open Source Java GIS Toolkit
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
package org.geotools.gce.imagemosaic.jdbc;

import java.awt.Rectangle;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.GeneralEnvelope;

/**
 * This interface lists the methods wich are used by the ImageMosaicJDBCReader
 * class to interact with the database system.
 * 
 * @author mcr
 * 
 *
 *
 * @source $URL$
 */
public interface JDBCAccess {
	/**
	 * Method for starting the main action, getting the neccessairy tiles and
	 * decoding them in a multithreaded manner
	 * 
	 * @param pixelDimension
	 *            the needed pixel dimension
	 * @param requestEnvelope
	 *            the needed envelope in world coordinates
	 * @param info
	 *            the corresponding ImageLevelInfo object
	 * @param tileQueue
	 *            a queue where to put the thread results
	 * @param coverageFactory
	 *            GridCoverageFactory
	 * @throws IOException
	 */
	public abstract void startTileDecoders(Rectangle pixelDimension,
			GeneralEnvelope requestEnvelope, ImageLevelInfo info,
			LinkedBlockingQueue<TileQueueElement> tileQueue,
			GridCoverageFactory coverageFactory) throws IOException;

	/**
	 * @param level
	 *            the level (0 is original, 1 is first pyramid,...)
	 * @return the corresponding ImageLevelInfo object
	 */
	public ImageLevelInfo getLevelInfo(int level);

	/**
	 * @return the number of existing pyramids
	 */
	public int getNumOverviews();

	/**
	 * initialze the the JDBCAccess object, has to be called exactly once
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public void initialize() throws SQLException, IOException;
}
