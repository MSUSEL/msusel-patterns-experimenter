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
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.temporal;

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Values for relative temporal position based on the 13 temporal relationships
 * identified by Allen (1993).
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 * @author Martin Desruisseaux (IRD)
 *
 * @todo Need javadoc for each enumeration.
 *
 *
 * @source $URL$
 */
@UML(identifier="TM_RelativePosition", specification=ISO_19108)
public final class RelativePosition extends CodeList<RelativePosition> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -2918422623747953495L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<RelativePosition> VALUES = new ArrayList<RelativePosition>(13);

    public static final RelativePosition BEFORE = new RelativePosition("BEFORE");
    public static final RelativePosition AFTER = new RelativePosition("AFTER");
    public static final RelativePosition BEGINS = new RelativePosition("BEGINS");
    public static final RelativePosition ENDS = new RelativePosition("ENDS");
    public static final RelativePosition DURING = new RelativePosition("DURING");
    public static final RelativePosition EQUALS = new RelativePosition("EQUALS");
    public static final RelativePosition CONTAINS = new RelativePosition("CONTAINS");
    public static final RelativePosition OVERLAPS = new RelativePosition("OVERLAPS");
    public static final RelativePosition MEETS = new RelativePosition("MEETS");
    public static final RelativePosition OVERLAPPED_BY = new RelativePosition("OVERLAPPED_BY");
    public static final RelativePosition MET_BY = new RelativePosition("MET_BY");
    public static final RelativePosition BEGUN_BY = new RelativePosition("BEGUN_BY");
    public static final RelativePosition ENDED_BY = new RelativePosition("ENDED_BY");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private RelativePosition(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code RelativePosition}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static RelativePosition[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new RelativePosition[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public RelativePosition[] family() {
        return values();
    }

    /**
     * Returns the relative position that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static RelativePosition valueOf(String code) {
        return valueOf(RelativePosition.class, code);
    }
}
