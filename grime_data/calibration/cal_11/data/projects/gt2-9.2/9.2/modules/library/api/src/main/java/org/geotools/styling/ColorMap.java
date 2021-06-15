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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;



/**
 * The ColorMap element defines either the colors of a palette-type raster
 * source or the mapping of  fixed-numeric pixel values to colors.
 * <pre>
 * &lt;xs:element name="ColorMap"&gt;
 *   &lt;xs:complexType&gt;
 *     &lt;xs:choice minOccurs="0" maxOccurs="unbounded"&gt;
 *       &lt;xs:element ref="sld:ColorMapEntry"/&gt;
 *     &lt;/xs:choice&gt;
 *   &lt;/xs:complexType&gt;
 * &lt;/xs:element&gt;
 * </pre>
 * For example, a DEM raster giving elevations in meters above sea level can be
 * translated to a colored  image with a ColorMap.  The quantity attributes of
 * a color-map are used for translating between numeric  matrixes and color
 * rasters and the ColorMap entries should be in order of increasing numeric
 * quantity so  that intermediate numeric values can be matched to a color (or
 * be interpolated between two colors).   Labels may be used for legends or
 * may be used in the future to match character values.   Not all systems can
 * support opacity in colormaps.  The default opacity is 1.0 (fully opaque).
 * Defaults for quantity and label are system-dependent.
 *
 *
 * @source $URL$
 */
public interface ColorMap extends org.opengis.style.ColorMap{
    
    public static final int TYPE_RAMP = 1;
    
    public static final int TYPE_INTERVALS = 2;
    
    public static final int TYPE_VALUES = 3;

    public void addColorMapEntry(ColorMapEntry entry);

    public ColorMapEntry[] getColorMapEntries();

    public ColorMapEntry getColorMapEntry(int i);

    /**
     * Type of color map; matchinges the function returned by getFunction().getName()
     * 
     * @return One of TYPE_RAMP, TYPE_INTERVALS, or TYPE_VALUE
     */
    public int getType();

    /**
     * @param type One of TYPE_RAMP, TYPE_INTERVALS, or TYPE_VALUE
     */
    public void setType(int type);

    void accept(org.geotools.styling.StyleVisitor visitor);

    /**
     * Tells me to use 65536 colors even if 256 could suffice.
     *
     * @param extended
     *            <code>true</code> for using 65536 colors, <code>false</code>
     *            for using 256.
     */
    public void setExtendedColors(boolean extended);

    public boolean getExtendedColors();
}
