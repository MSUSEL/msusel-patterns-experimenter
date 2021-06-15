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


/**
 * This class doubles as a filter and an identifier for colour palettes.
 *
 *
 *
 * @source $URL$
 */
public class PaletteType {
    /** name of the type, if null this will match any name */
    String name;

    /** is the palette appropriate for numerical ranges? */
    boolean suitableRanged;

    /** is the palette appropriate for categorical data (unique values)? */
    boolean suitableUnique;

    /** interal flag for marking the type as UNKNOWN */
    boolean isEmpty = false;

    /**
     * Creates an identifier for palettes
     *
     * @param name simple identifier
     * @param suitableRanged
     * @param suitableUnique
     */
    public PaletteType(boolean suitableRanged, boolean suitableUnique, String name) {
        this.name = name;
        this.suitableRanged = suitableRanged;
        this.suitableUnique = suitableUnique;
    }

    /**
     *
     * @param suitableRanged true, false, or null (for don't care)
     * @param suitableUnique
     */
    public PaletteType(boolean suitableRanged, boolean suitableUnique) {
        this.name = null;
        this.suitableRanged = suitableRanged;
        this.suitableUnique = suitableUnique;
    }

    public PaletteType() {
        this.name = null;
        this.isEmpty = true;
    }

    public String getName() {
        return name;
    }

    /**
     * Deterines if this PaletteType instance is suitable ranged.
     *
     * @return a boolean, true if the paletteType is sutableRanged.
     */
    public boolean isSuitableRanged() {
        return suitableRanged;
    }

    public boolean isSuitableUnique() {
        return suitableUnique;
    }

    public boolean isMatch(PaletteType filter) {
        if (filter.equals(ColorBrewer.ALL)) {
            return true; //wildcard
        }

        if (filter.isEmpty) {
            return true; //wildcard (everything is null)
        }

        if (filter.getName() == null) { //generic filter

            if (isEmpty) {
                return false; //we know nothing about this item, so we assume it doesn't match 
            }
        } else { //specific filter (exact name match + conditions)

            if (!filter.getName().equals(name)) {
                return false;
            }
        }

        if (filter.isSuitableRanged() != suitableRanged) {
            return false;
        }

        if (filter.isSuitableUnique() != suitableUnique) {
            return false;
        }

        return true;
    }

    public boolean equals(Object arg0) {
        if (!(arg0 instanceof PaletteType)) {
            return false;
        }

        PaletteType arg = (PaletteType) arg0;

        if (name == null) {
            if (arg.getName() != null) {
                return false;
            }
        } else {
            if (arg.getName() == null) {
                return false;
            }

            if (!arg.getName().equals(name)) {
                return false;
            }
        }

        if (arg.isSuitableRanged() != suitableRanged) {
            return false;
        }

        if (arg.isSuitableUnique() != suitableUnique) {
            return false;
        }

        return true;
    }
}
