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
package org.geotools.coverage.grid.imageio.geotiff.metadata.codes;

/**
 * 
 * @author Simone Giannecchini
 * @since 2.3
 * 
 *
 *
 *
 * @source $URL$
 */
public final class GeoTiffVCSCodes {

	/**
	 * VerticalCitationGeoKey Key ID = 4097 Type = ASCII Values = text This key
	 * may be used to document the vertical coordinate system used, and its
	 * parameters.
	 */
	public static final int VerticalCitationGeoKey = 4097;

	/**
	 * VerticalCSTypeGeoKey Key ID = 4096 Type = SHORT (code) Values = Section
	 * 6.3.4.1 Codes
	 * http://www.remotesensing.org/geotiff/spec/geotiff6.html#6.3.4.1 This key
	 * may be used to specify the vertical coordinate system.
	 */
	public static final int VerticalCSTypeGeoKey = 4096;

	/**
	 * VerticalDatumGeoKey Key ID = 4098 Type = SHORT (code) Values = Section
	 * 6.3.4.2 codes
	 * http://www.remotesensing.org/geotiff/spec/geotiff6.html#6.3.4.2 This key
	 * may be used to specify the vertical datum for the vertical coordinate
	 * system.
	 */
	public static final int VerticalDatumGeoKey = 4098;

	/**
	 * VerticalUnitsGeoKey Key ID = 4099 Type = SHORT (code) Values = Section
	 * 6.3.1.3 Codes
	 * http://www.remotesensing.org/geotiff/spec/geotiff6.html#6.3.1.3 This key
	 * may be used to specify the vertical units of measurement used in the
	 * geographic coordinate system, in cases where geographic CS's need to
	 * reference the vertical coordinate. This, together with the Citation key,
	 * comprise the only fully implemented keys in this section, at present.
	 */
	public static final int VerticalUnitsGeoKey = 4099;

	private GeoTiffVCSCodes() {
	}

}
