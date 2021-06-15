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
package org.geotools.utils;

import javax.media.jai.BorderExtender;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;

import org.geotools.coverage.processing.operation.FilteredSubsample;
import org.geotools.coverage.processing.operation.Scale;
import org.geotools.coverage.processing.operation.SubsampleAverage;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * 
 *
 *
 *
 *
 * @source $URL$
 */
public class CoverageToolsConstants {

	/**
	 * Default interpolation.
	 */
	public final static Interpolation DEFAULT_INTERPOLATION = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
	/** Default filter for subsampling averaged. */
	public final static float[] DEFAULT_KERNEL_GAUSSIAN = new float[] { 0.5F,
			1.0F / 3.0F, 0.0F, -1.0F / 12.0F };
	/**
	 * Default border extender.
	 */
	public final static BorderExtender DEFAULT_BORDER_EXTENDER = BorderExtender
			.createInstance(BorderExtender.BORDER_COPY);
	public final static FilteredSubsample FILTERED_SUBSAMPLE_FACTORY = new FilteredSubsample();
	public static final SubsampleAverage SUBSAMPLE_AVERAGE_FACTORY = new SubsampleAverage();
	public static final Scale SCALE_FACTORY = new Scale();

	public static final int DEFAULT_INTERNAL_TILE_WIDTH = 512;
	public static final int DEFAULT_INTERNAL_TILE_HEIGHT = 512;
	
	public final static String DEFAULT_COMPRESSION_SCHEME="LZW";
	
	public final static float  DEFAULT_COMPRESSION_RATIO=0.75f;
	/**
	 * Default number of resolution steps.. 
	 */
	public static final int DEFAULT_RESOLUTION_STEPS = 5;
	
	       /** Tile width. */
	public static final int DEFAULT_TILE_WIDTH = (int) JAI.getDefaultTileSize().getWidth();

        /** Tile height. */
	public static final  int DEFAULT_TILE_HEIGHT = (int) JAI.getDefaultTileSize().getHeight();
        /**
         * Default tile cache size.
         */
        public static final long DEFAULT_TILE_CACHE_SIZE = 128 * 1024 * 1024;
        /**
         * Default imageio caching behaviour.
         */
        public final static boolean DEFAULT_IMAGEIO_CACHING_BEHAVIOUR = false;

}
