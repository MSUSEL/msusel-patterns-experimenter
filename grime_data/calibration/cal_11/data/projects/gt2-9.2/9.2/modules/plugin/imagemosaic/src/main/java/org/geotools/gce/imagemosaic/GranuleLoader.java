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
package org.geotools.gce.imagemosaic;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.util.concurrent.Callable;

import javax.imageio.ImageReadParam;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;

import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.GranuleDescriptor.GranuleLoadingResult;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.resources.image.ImageUtilities;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.operation.MathTransform2D;

/**
 * Specific {@link Callable} implementation that can be used to load the result of a request on a
 * single {@link GranuleDescriptor} via {@link GranuleLoadingResult}.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * 
 */
class GranuleLoader implements Callable<GranuleLoadingResult>{

	final ReferencedEnvelope cropBBox;
	
	final MathTransform2D mosaicWorldToGrid;
	
	final GranuleDescriptor granuleDescriptor;
	
	final ImageReadParam readParameters;
	
	final int imageIndex;

	final Hints hints;

	RasterLayerRequest request;
	    
	GranuleLoader(
			final ImageReadParam readParameters, 
			final int imageIndex,
			final ReferencedEnvelope cropBBox, 
			final MathTransform2D mosaicWorldToGrid,
			final GranuleDescriptor granuleDescriptor,
			final RasterLayerRequest request,
			final Hints hints) {
		this.readParameters = ImageUtilities.cloneImageReadParam(readParameters);
		this.imageIndex = imageIndex;
		this.cropBBox = cropBBox;
		this.mosaicWorldToGrid = mosaicWorldToGrid;
		this.granuleDescriptor = granuleDescriptor;
		this.request=request;
		this.hints = new Hints(hints);
		if (request.getTileDimensions()!= null) {
		    final Dimension tileDimension = request.getTileDimensions();
		    if (hints != null && hints.containsKey(JAI.KEY_IMAGE_LAYOUT)){
		        final Object layout = this.hints.get(JAI.KEY_IMAGE_LAYOUT);
		        if (layout != null && layout instanceof ImageLayout){
		            final ImageLayout imageLayout = (ImageLayout) layout;
		            imageLayout.setTileHeight(tileDimension.height);
		            imageLayout.setTileWidth(tileDimension.width);
		        }
		    } else {
		        final ImageLayout layout = new ImageLayout();
		        layout.setTileWidth(tileDimension.width).setTileHeight(tileDimension.height);
		        this.hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout));
		    }
		}
	}
	
	public BoundingBox getCropBBox() {
		return cropBBox;
	}

	public MathTransform2D getMosaicWorldToGrid() {
		return mosaicWorldToGrid;
	}

	public GranuleDescriptor getGranule() {
		return granuleDescriptor;
	}

	public ImageReadParam getReadParameters() {
		return readParameters;
	}

	public int getImageIndex() {
		return imageIndex;
	}
	
	public GranuleLoadingResult call() throws Exception {
		return granuleDescriptor.loadRaster(readParameters, imageIndex, cropBBox, mosaicWorldToGrid, request, hints);
	}

}