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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.spatial;

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Name of the dimension.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_DimensionNameTypeCode", specification=ISO_19115)
public final class DimensionNameType extends CodeList<DimensionNameType> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -8534729639298737965L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<DimensionNameType> VALUES = new ArrayList<DimensionNameType>(8);

    /**
     * Ordinate (y) axis.
     */
    @UML(identifier="row", obligation=CONDITIONAL, specification=ISO_19115)
    public static final DimensionNameType ROW = new DimensionNameType("ROW");

    /**
     * Abscissa (x) axis.
     */
    @UML(identifier="column", obligation=CONDITIONAL, specification=ISO_19115)
    public static final DimensionNameType COLUMN = new DimensionNameType("COLUMN");

    /**
     * Vertical (z) axis.
     */
    @UML(identifier="vertical", obligation=CONDITIONAL, specification=ISO_19115)
    public static final DimensionNameType VERTICAL = new DimensionNameType("VERTICAL");

    /**
     * Along the direction of motion of the scan point
     */
    @UML(identifier="track", obligation=CONDITIONAL, specification=ISO_19115)
    public static final DimensionNameType TRACK = new DimensionNameType("TRACK");

    /**
     * Perpendicular to the direction of motion of the scan point.
     */
    @UML(identifier="crossTrack", obligation=CONDITIONAL, specification=ISO_19115)
    public static final DimensionNameType CROSS_TRACK = new DimensionNameType("CROSS_TRACK");

    /**
     * Scan line of a sensor.
     */
    @UML(identifier="line", obligation=CONDITIONAL, specification=ISO_19115)
    public static final DimensionNameType LINE = new DimensionNameType("LINE");

    /**
     * Element along a scan line.
     */
    @UML(identifier="sample", obligation=CONDITIONAL, specification=ISO_19115)
    public static final DimensionNameType SAMPLE = new DimensionNameType("SAMPLE");

    /**
     * Duration.
     */
    @UML(identifier="time", obligation=CONDITIONAL, specification=ISO_19115)
    public static final DimensionNameType TIME = new DimensionNameType("TIME");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private DimensionNameType(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code DimensionNameType}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static DimensionNameType[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new DimensionNameType[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public DimensionNameType[] family() {
        return values();
    }

    /**
     * Returns the dimension name type that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static DimensionNameType valueOf(String code) {
        return valueOf(DimensionNameType.class, code);
    }
}
