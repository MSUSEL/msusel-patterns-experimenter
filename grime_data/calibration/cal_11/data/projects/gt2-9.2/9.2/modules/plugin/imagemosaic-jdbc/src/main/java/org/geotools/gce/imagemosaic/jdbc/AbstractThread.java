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
package org.geotools.gce.imagemosaic.jdbc;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.geotools.geometry.GeneralEnvelope;

/**
 * this class is the base class for concrete thread classes
 * 
 * @author mcr
 * 
 */
abstract class AbstractThread extends Thread {
	LinkedBlockingQueue<TileQueueElement> tileQueue;

	Config config;

	GeneralEnvelope requestEnvelope;

	Rectangle pixelDimension;

	ImageLevelInfo levelInfo;

	double rescaleX;

	double rescaleY;

	double resX;

	double resY;

	/**
	 * Constructor
	 * 
	 * @param pixelDimenison
	 *            the requested pixel dimension
	 * @param requestEnvelope
	 *            the requested world rectangle
	 * @param levelInfo
	 *            levelinfo of selected pyramid
	 * @param tileQueue
	 *            queue for thread synchronization
	 * @param config
	 *            the configuraton of the plugin
	 * 
	 */
	AbstractThread(Rectangle pixelDimenison, GeneralEnvelope requestEnvelope,
			ImageLevelInfo levelInfo,
			LinkedBlockingQueue<TileQueueElement> tileQueue, Config config) {
		super();
		this.config = config;
		this.tileQueue = tileQueue;
		this.requestEnvelope = requestEnvelope;
		this.levelInfo = levelInfo;
		this.pixelDimension = pixelDimenison;

		resX = requestEnvelope.getSpan(0) / pixelDimenison.getWidth();
		resY = requestEnvelope.getSpan(1) / pixelDimenison.getHeight();
		rescaleX = levelInfo.getResX() / resX;
		rescaleY = levelInfo.getResY() / resY;
	}

	/**
	 * @return a Map containing a rendereing hint for the interpolation as
	 *         specified in the config
	 */
	Map<RenderingHints.Key, Object> getRenderingHints() {
		final Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
		Object interpolation = null;

		if (config.getInterpolation().intValue() == 1) {
			interpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
		}

		if (config.getInterpolation().intValue() == 2) {
			interpolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
		}

		if (config.getInterpolation().intValue() == 3) {
			interpolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
		}

		hints.put(RenderingHints.KEY_INTERPOLATION, interpolation);

		return hints;
	}

	/**
	 * @param image
	 *            to scale
	 * @return rescaled image fitting into the requested pixel dimension
	 */
	protected BufferedImage rescaleImage(BufferedImage image) {

		BufferedImage scaledImage = null;
		int imageType = image.getType();

		if ((imageType == BufferedImage.TYPE_BYTE_BINARY || imageType == BufferedImage.TYPE_BYTE_INDEXED)
				&& (image.getColorModel() instanceof IndexColorModel))
			scaledImage = new BufferedImage((int) Math.floor(image.getWidth()
					* rescaleX),
					(int) Math.floor(image.getHeight() * rescaleY), image
							.getType(), (IndexColorModel) image.getColorModel());
		else
			scaledImage = new BufferedImage((int) Math.floor(image.getWidth()
					* rescaleX),
					(int) Math.floor(image.getHeight() * rescaleY), image
							.getType());

		final Graphics2D g2D = (Graphics2D) scaledImage.getGraphics();

		g2D.addRenderingHints(getRenderingHints());
		g2D.drawImage(image, AffineTransform.getScaleInstance(rescaleX,
				rescaleY), null);

		return scaledImage;
	}
}
