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
package org.geotools.brewer.color;

import java.awt.Color;


/**
 * A ColorPalette with additional ColorBrewer information (suitability data and colour selection).
 *
 * @author James Macgill
 * @author Cory Horner, Refractions Research Inc.
 *
 *
 * @source $URL$
 */
public class BrewerPalette extends ColorPalette {
    private PaletteSuitability suitability;
    private SampleScheme sampler;
    private PaletteType type;

    /**
     * Creates a new instance of BrewerPalette
     */
    public BrewerPalette() {
    }

    /**
     * Getter for property type.
     *
     * @return Value of property type.
     */
    public PaletteType getType() {
        return this.type;
    }

    /**
     * Sets the type of palette.
     *
     * @param type new palette type
     */
    public void setType(PaletteType type) {
        this.type = type;
    }

    public Color getColor(int index, int length) {
        return getColors(length)[index];
    }

    /**
     * Getter for the colour count
     *
     * @return the most colours this palette currently supports
     */
    public int getMaxColors() {
        int countSampler = sampler.getMaxCount();
        int numColors = getCount();

        //return the lesser of countSampler and numColors
        if (countSampler < numColors) {
            return countSampler;
        } else {
            return numColors;
        }
    }

    /**
     * Getter for the colour count
     *
     * @return the minimum number of colours this palette currently supports
     */
    public int getMinColors() {
        return sampler.getMinCount();
    }

    /**
     * Obtains a set of colours from the palette.
     */
    public Color[] getColors(int length) {
        if (length < 2) {
            length = 2; //if they ask for 1 colour, give them 2 instead of crashing
        }

        int[] lookup = sampler.getSampleScheme(length);
        Color[] colors = getColors();
        Color[] result = new Color[length];

        for (int i = 0; i < length; i++) {
            result[i] = colors[lookup[i]];
        }

        return result;
    }

    public PaletteSuitability getPaletteSuitability() {
        return suitability;
    }

    public void setPaletteSuitability(PaletteSuitability suitability) {
        this.suitability = suitability;
    }

    public SampleScheme getColorScheme() {
        return sampler;
    }

    public void setColorScheme(SampleScheme scheme) {
        this.sampler = scheme;
    }
}
