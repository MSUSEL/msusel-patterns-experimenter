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

import java.util.Collection;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A discrete temporal reference system that provides a
 * basis for defining temporal position to a resolution of one day.
 *
 * @author Alexander Petkov
 *
 *
 * @source $URL$
 */
@UML(identifier="TM_Calendar", specification=ISO_19108)
public interface Calendar extends TemporalReferenceSystem {
    /**
     * Converts a {@linkplain CalendarDate date} in this calendar to a
     * {@linkplain JulianDate julian date}.
     */
    @UML(identifier="dateTrans", obligation=MANDATORY, specification=ISO_19108)
    JulianDate dateTrans(CalendarDate date, ClockTime time);

    /**
     * Converts a {@linkplain JulianDate julian date} to a {@linkplain CalendarDate date}
     * in this calendar.
     */
    @UML(identifier="julTrans", obligation=MANDATORY, specification=ISO_19108)
    CalendarDate julTrans(JulianDate julian);

    /**
     * links this calendar to the {@linkplain CalendarEra calendar eras}
     * that it uses as a reference for dating.
     *
     * @todo The original version of this class returned {@code TemporalCalendarEra}, which
     *       doesn't exists in the provided sources. I assumed that it was a typo and that
     *       the actual class was {@link CalendarEra}.
     */
    @UML(identifier="Basis", specification=ISO_19108)
    Collection<CalendarEra> getBasis();

    /**
     * Links this calendar to the {@linkplain Clock clock} that is used for specifying
     * temporal positions within the smallest calendar interval.
     *
     * @todo Method name doesn't match the UML identifier.
     */
    @UML(identifier="Resolution", specification=ISO_19108)
    Clock getClock();
}
