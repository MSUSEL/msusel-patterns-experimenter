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
package org.opengis.metadata.identification;

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Status of the dataset or progress of a review.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_ProgressCode", specification=ISO_19115)
public final class Progress extends CodeList<Progress> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 7521085150853319219L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<Progress> VALUES = new ArrayList<Progress>(7);

    /**
     * Production of the data has been completed.
     */
    @UML(identifier="completed", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Progress COMPLETED = new Progress("COMPLETED");

    /**
     * Data has been stored in an offline storage facility
     */
    @UML(identifier="historicalArchive", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Progress HISTORICAL_ARCHIVE = new Progress("HISTORICAL_ARCHIVE");

    /**
     * Data is no longer relevant.
     */
    @UML(identifier="obsolete", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Progress OBSOLETE = new Progress("OBSOLETE");

    /**
     * Data is continually being updated.
     */
    @UML(identifier="onGoing", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Progress ON_GOING = new Progress("ON_GOING");

    /**
     * Fixed date has been established upon or by which the data will be created or updated.
     */
    @UML(identifier="planned", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Progress PLANNED = new Progress("PLANNED");

    /**
     * Data needs to be generated or updated.
     */
    @UML(identifier="required", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Progress REQUIRED = new Progress("REQUIRED");

    /**
     * Data is currently in the process of being created.
     */
    @UML(identifier="underDevelopment", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Progress UNDER_DEVELOPMENT = new Progress("UNDER_DEVELOPMENT");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private Progress(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code Progress}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static Progress[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new Progress[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public Progress[] family() {
        return values();
    }

    /**
     * Returns the progress that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static Progress valueOf(String code) {
        return valueOf(Progress.class, code);
    }
}
