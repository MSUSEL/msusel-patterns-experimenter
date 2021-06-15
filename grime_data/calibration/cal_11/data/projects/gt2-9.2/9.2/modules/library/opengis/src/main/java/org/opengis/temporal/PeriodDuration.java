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

import org.opengis.annotation.UML;
import org.opengis.util.InternationalString;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Uses the format specified by ISO 8601 for exchanging information
 * about the duration of a period.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 *
 * @todo Uses Java 1.5 {@link javax.xml.datatype.Duration}.
 *
 *
 * @source $URL$
 */
@UML(identifier="TM_PeriodDuration", specification=ISO_19108)
public interface PeriodDuration extends Duration {
   /**
    * A mandatory element which designates that the returned string
    * represents the duration of a period.
    */
    @UML(identifier="designator", obligation=MANDATORY, specification=ISO_19108)
    InternationalString getDesignator();

   /**
    * A positive integer, followed by the character "Y",
    * which indicated the number of years in the period.
    */
    @UML(identifier="years", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getYears();

   /**
    * A positive integer, followed by the character "M",
    * which indicated the number of months in the period.
    */
    @UML(identifier="months", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getMonths();

   /**
    * A positive integer, followed by the character "D",
    * which indicated the number of days in the period.
    */
    @UML(identifier="days", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getDays();

   /**
    * Included whenever the sequence includes values for
    * units less than a day.
    */
    @UML(identifier="timeIndicator", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getTimeIndicator();

   /**
    * A positive integer, followed by the character "H",
    * which indicated the number of hours in the period.
    */
    @UML(identifier="hours", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getHours();

   /**
    * A positive integer, followed by the character "M",
    * which indicated the number of minutes in the period.
    */
    @UML(identifier="minutes", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getMinutes();

   /**
    * A positive integer, followed by the character "S",
    * which indicated the number of seconds in the period.
    */
    @UML(identifier="seconds", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getSeconds();
}
