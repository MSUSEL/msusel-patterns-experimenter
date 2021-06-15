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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
/*
 * NOTICE OF RELEASE TO THE PUBLIC DOMAIN
 *
 * This work was created by employees of the USDA Forest Service's
 * Fire Science Lab for internal use.  It is therefore ineligible for
 * copyright under title 17, section 105 of the United States Code.  You
 * may treat it as you would treat any public domain work: it may be used,
 * changed, copied, or redistributed, with or without permission of the
 * authors, for free or for compensation.  You may not claim exclusive
 * ownership of this code because it is already owned by everyone.  Use this
 * software entirely at your own risk.  No warranty of any kind is given. 
 *
 * A copy of 17-USC-105 should have accompanied this distribution in the file
 * 17USC105.html.  If not, you may access the law via the US Government's
 * public websites:
 *   - http://www.copyright.gov/title17/92chap1.html#105
 *   - http://www.gpoaccess.gov/uscode/  (enter "17USC105" in the search box.)
 */
package org.geotools.gce.geotiff;

// Geotools dependencies
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;

/**
 * The <CODE>GeoTiffFormatFactorySpi</CODE> should never be instantiated
 * directly by user code. It is discovered by the <CODE>GridFormatFinder</CODE>
 * for automatic discovery. Use the standard Geotools method of discovering a
 * factory in order to create a format.
 * 
 * <p>
 * This format will only report itself to be &quot;available&quot; if the JAI
 * and JAI ImageI/O libraries are available. Otherwise it will be unavailable.
 * If a user attempts to create a new instance of the format when the required
 * libraries are unavailable, an <CODE>UnsupportedOperationException</CODE>
 * will be thrown.
 * </p>
 * 
 * @author Bryce Nordgren / USDA Forest Service
 * @author Simone Giannecchini
 *
 *
 * @source $URL$
 */
public class GeoTiffFormatFactorySpi implements GridFormatFactorySpi {
	/**
	 * Creates a new instance of GeoTiffFormatFactorySpi
	 */
	public GeoTiffFormatFactorySpi() {
	}

	/**
	 * Creates and returns a new instance of the <CODE>GeoTiffFormat</CODE>
	 * class if the required libraries are present. If JAI and JAI Image I/O are
	 * not present, will throw an <CODE>UnsupportedOperationException</CODE>.
	 * 
	 * @return <CODE>GeoTiffFormat</CODE> object.
	 * 
	 * @throws UnsupportedOperationException
	 *             if this format is unavailable.
	 */
	public AbstractGridFormat createFormat() {
		if (!isAvailable()) {
			throw new UnsupportedOperationException(
					"The GeoTiff plugin requires the JAI and JAI ImageI/O libraries!");
		}

		return new GeoTiffFormat();
	}

	/**
	 * Informs the caller whether the libraries required by the GeoTiff reader
	 * are installed or not.
	 * 
	 * @return availability of the GeoTiff format.
	 */
	public boolean isAvailable() {
		boolean available = true;

		// if these classes are here, then the runtine environment has
		// access to JAI and the JAI ImageI/O toolbox.
		try {
			Class.forName("javax.media.jai.JAI");
			Class.forName("com.sun.media.jai.operator.ImageReadDescriptor");
		} catch (ClassNotFoundException cnf) {
			available = false;
		}

		return available;
	}

	/**
	 * Returns the implementation hints. The default implementation returns an
	 * empty map.
	 * 
	 * @return Empty Map.
	 */
	public Map<RenderingHints.Key, ?>  getImplementationHints() {
		return Collections.emptyMap();
	}
}
