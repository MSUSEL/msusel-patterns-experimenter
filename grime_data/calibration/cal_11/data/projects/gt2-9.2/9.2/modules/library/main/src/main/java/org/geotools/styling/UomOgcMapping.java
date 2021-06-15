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
package org.geotools.styling;

import javax.measure.quantity.Length;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 * Defines the Units of Measure (UOMs) specified by the OGC SE standard and their mappings to Java
 * Units defined in <code>javax.measure.unit</code>. Each entry in this enum provides both the Java
 * Unit for the given UOM and the corresponding String that is defined by the SE standard.
 *
 *
 * @source $URL$
 */
public enum UomOgcMapping {

    METRE(SI.METER, "http://www.opengeospatial.org/se/units/metre"), FOOT(NonSI.FOOT,
            "http://www.opengeospatial.org/se/units/foot"), PIXEL(NonSI.PIXEL,
            "http://www.opengeospatial.org/se/units/pixel");

    private String seString;

    private Unit<Length> unit;

    /**
     * Internal constructor: specifies the UOM mapping passing a specific Java Unit and the
     * corresponding OGC SE string.
     * 
     * @param unit
     *            a Java Unit (e.g., <code>SI.METER</code>).
     * @param seString
     *            a String that follows the OGC SE specification.
     */
    private UomOgcMapping(Unit<Length> unit, String seString) {
        this.unit = unit;
        this.seString = seString;
    }

    @Override
    public String toString() {
        return seString;
    }

    /**
     * Returns the String defined by the OGC SE specification for the unit of measure.
     * 
     * @return a String that follows the OGC SE specification
     */
    public String getSEString() {
        return seString;
    }

    /**
     * Returns the Java Unit that corresponds to the unit of measure.
     * 
     * @return a Java Unit (e.g., <code>SI.METER</code>).
     */
    public Unit<Length> getUnit() {
        return unit;
    }

    /**
     * Returns the appropriate UOM mapping for a given OGC SE standard string.
     * 
     * @param seString
     *            a String that follows the OGC SE specification.
     * @return the corresponding UnitOfMeasure.
     * @throws IllegalArgumentException
     *             if the provided String is not a valid OGC SE value.
     */
    public static UomOgcMapping get(String seString) throws IllegalArgumentException {
        for (UomOgcMapping uom : UomOgcMapping.values()) {
            if (uom.getSEString().equals(seString))
                return uom;
        }
        throw new IllegalArgumentException("'" + seString
                + "' is not a valid OGC SE standard Unit of Measure");
    }

    /**
     * Returns the appropriate UOM mapping for a given Java Unit.
     * 
     * @param unit
     *            a Java Unit (e.g., <code>SI.METER</code>).
     * @return the corresponding UnitOfMeasure.
     * @throws IllegalArgumentException
     *             if the provided Unit is not part of the OGC SE specification.
     */
    public static UomOgcMapping get(Unit<Length> unit) throws IllegalArgumentException {
        for (UomOgcMapping uom : UomOgcMapping.values()) {
            if (uom.getUnit().equals(unit))
                return uom;
        }
        throw new IllegalArgumentException("'" + unit
                + "' is not a valid OGC SE standard Unit of Measure");
    }
}
