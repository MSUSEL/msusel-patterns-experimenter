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

import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Characteristics of each calendar era.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 *
 *
 * @source $URL$
 */
@UML(identifier="TM_CalendarEra", specification=ISO_19108)
public interface CalendarEra {
    /**
     * Uniquely identifies the calendar era within this calendar.
     */
    @UML(identifier="name", obligation=MANDATORY, specification=ISO_19108)
    InternationalString getName();

    /**
     * Provides the name or description of a mythical or historic event which fixes the position
     * of the base scale of the calendar era.
     */
    @UML(identifier="referenceEvent", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getReferenceEvent();

    /**
     * Provides the date of the reference event expressed as a date in the given calendar.
     */
    @UML(identifier="referenceDate", obligation=OPTIONAL, specification=ISO_19108)
    CalendarDate getReferenceDate();

    /**
     * Provides the {@linkplain JulianDate julian date} that corresponds to the reference date.
     */
    @UML(identifier="julianReference", specification=ISO_19108)
    JulianDate getJulianReference();

    /**
     * Identifies the {@linkplain Period period} for which the calendar era
     * was used as a reference fro dating.
     *
     *
     * @return The period, where the data type for {@linkplain Period#getBegin begin}
     *         and {@link Period#getEnd end} is {@link JulianDate}.
     */
    @UML(identifier="epochOfUse", specification=ISO_19108)
    Period getEpochOfUse();
}
