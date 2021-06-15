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

import java.util.Date;
import java.sql.Time;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A union class that consists of one of the data types listed as its attributes.
 * Date, Time, and DateTime are basic data types defined in ISO/TS 19103,
 * and may be used for describing temporal positions referenced to the
 * Gregorian calendar and UTC.
 *
 * @author Alexander Petkov
 * @author Martin Desruisseaux (IRD)
 *
 *
 *
 * @source $URL$
 */
@UML(identifier="TM_Position", specification=ISO_19108)
public interface Position {
    /**
     * {@linkplain TemporalPosition} and its subtypes shall be used
     * for describing temporal positions referenced to other reference systems, and may be used for
     * temporal positions referenced to any calendar or clock, including the Gregorian calendar and UTC.
     * @return TemporalPosition
     */
    @UML(identifier="anyOther", obligation=OPTIONAL, specification=ISO_19108)
    TemporalPosition anyOther();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    @UML(identifier="date8601", obligation=OPTIONAL, specification=ISO_19108)
    Date getDate();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    @UML(identifier="time8601", obligation=OPTIONAL, specification=ISO_19108)
    Time getTime();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    @UML(identifier="dateTime8601", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getDateTime();

}
