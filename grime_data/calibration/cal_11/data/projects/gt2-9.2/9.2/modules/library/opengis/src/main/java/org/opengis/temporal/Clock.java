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
 * Provides a basis for defining temporal position within a day.
 *
 * @author Alexander Petkov
 *
 * @todo Retrofit in the referencing framework.
 *
 *
 * @source $URL$
 */
@UML(identifier="TM_Clock", specification=ISO_19108)
public interface Clock extends TemporalReferenceSystem {
    /**
     * Event used as the datum for this clock.
     */
    @UML(identifier="referenceEvent", obligation=MANDATORY, specification=ISO_19108)
    InternationalString getReferenceEvent();

    /**
     * Time of the reference Event for this clock, usually the origin of the clock scale.
     */
    @UML(identifier="ReferenceTime", obligation=MANDATORY, specification=ISO_19108)
    ClockTime getReferenceTime();

    /**
     * Provides the 24-hour local or UTC time that corresponds to the reference time.
     */
    @UML(identifier="utcReference", obligation=MANDATORY, specification=ISO_19108)
    ClockTime getUTCReference();

    /**
     * Converts UTC time to a time on this clock.
     */
    @UML(identifier="clkTrans", obligation=MANDATORY, specification=ISO_19108)
    ClockTime clkTrans(ClockTime clkTime);

    /**
     * Converts UTC time to a time on this clock.
     */
    @UML(identifier="utcTrans", obligation=MANDATORY, specification=ISO_19108)
    ClockTime utcTrans(ClockTime uTime);
}
