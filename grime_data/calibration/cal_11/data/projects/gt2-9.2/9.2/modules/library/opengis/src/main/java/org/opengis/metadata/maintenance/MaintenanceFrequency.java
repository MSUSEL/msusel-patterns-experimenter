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
package org.opengis.metadata.maintenance;

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Frequency with which modifications and deletions are made to the data after it is
 * first produced.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_MaintenanceFrequencyCode", specification=ISO_19115)
public final class MaintenanceFrequency extends CodeList<MaintenanceFrequency> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -6034786030982260550L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<MaintenanceFrequency> VALUES = new ArrayList<MaintenanceFrequency>(12);

    /**
     * Data is repeatedly and frequently updated.
     */
    @UML(identifier="continual", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency CONTINUAL = new MaintenanceFrequency("CONTINUAL");

    /**
     * Data is updated each day.
     */
    @UML(identifier="daily", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency DAILY = new MaintenanceFrequency("DAILY");

    /**
     * Data is updated on a weekly basis.
     */
    @UML(identifier="weekly", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency WEEKLY = new MaintenanceFrequency("WEEKLY");

    /**
     * Data is updated every two weeks.
     */
    @UML(identifier="fortnightly", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency FORTNIGHTLY = new MaintenanceFrequency("FORTNIGHTLY");

    /**
     * Data is updated each month.
     */
    @UML(identifier="monthly", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency MONTHLY = new MaintenanceFrequency("MONTHLY");

    /**
     * Data is updated every three months.
     */
    @UML(identifier="quarterly", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency QUARTERLY = new MaintenanceFrequency("QUARTERLY");

    /**
     * Data is updated twice each year.
     */
    @UML(identifier="biannually", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency BIANNUALLY = new MaintenanceFrequency("BIANNUALLY");

    /**
     * Data is updated every year.
     */
    @UML(identifier="annually", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency ANNUALLY = new MaintenanceFrequency("ANNUALLY");

    /**
     * Data is updated as deemed necessary.
     */
    @UML(identifier="asNeeded", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency AS_NEEDED = new MaintenanceFrequency("AS_NEEDED");

    /**
     * Data is updated in intervals that are uneven in duration.
     */
    @UML(identifier="irregular", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency IRREGULAR = new MaintenanceFrequency("IRREGULAR");

    /**
     * There are no plans to update the data.
     */
    @UML(identifier="notPlanned", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency NOT_PLANNED = new MaintenanceFrequency("NOT_PLANNED");

    /**
     * Frequency of maintenance for the data is not known
     */
    @UML(identifier="unknow", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MaintenanceFrequency UNKNOW = new MaintenanceFrequency("UNKNOW");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private MaintenanceFrequency(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code MaintenanceFrequency}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static MaintenanceFrequency[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new MaintenanceFrequency[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public MaintenanceFrequency[] family() {
        return values();
    }

    /**
     * Returns the maintenance frequency that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static MaintenanceFrequency valueOf(String code) {
        return valueOf(MaintenanceFrequency.class, code);
    }
}
