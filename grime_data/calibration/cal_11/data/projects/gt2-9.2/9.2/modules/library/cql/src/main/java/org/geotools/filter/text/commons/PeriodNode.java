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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.commons;

import java.security.InvalidParameterException;
import java.util.Date;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;


/**
 * Period is constructed in the parsing process. this has convenient method to
 * deliver begin and end date of period. a period can be created from
 * date-time/date-time or date-time/duration or duration/date-time
 * <p>
 * Warning: This component is not published. It is part of module implementation. 
 * Client module should not use this feature.
 * </p>
 *
 * @since 2.4
 * @author Mauricio Pazos - Axios Engineering
 * @author Gabriel Roldan - Axios Engineering
 * @version $Id$
 *
 * @source $URL$
 *
 */
public class PeriodNode {
    private Literal begin = null;
    private Literal end = null;

    /**
     * @see create
     *
     * @param begin
     * @param end
     */
    private PeriodNode(final Literal begin, final Literal end) {
        if (!(begin.getValue() instanceof Date)) {
            throw new InvalidParameterException("begin parameter must be Literal with Date");
        }

        if (!(begin.getValue() instanceof Date)) {
            throw new InvalidParameterException("end paremeter must be Literal with Date");
        }

        this.begin = begin;
        this.end = end;
    }

    public static PeriodNode createPeriodDateAndDate(final Literal beginDate, final Literal endDate) {
        PeriodNode period = new PeriodNode(beginDate, endDate);

        return period;
    }

    public static PeriodNode createPeriodDateAndDuration(final Literal date,
        final Literal duration, FilterFactory filterFactory) {
        // compute last date from duration
        // Y M D and H M S
        Date firstDate = (Date) date.getValue();
        String strDuration = (String) duration.getValue();

        Date lastDate = DurationUtil.addDurationToDate(firstDate, strDuration);

        Literal literalLastDate = filterFactory.literal(lastDate);

        PeriodNode period = new PeriodNode(date, literalLastDate);

        return period;
    }

    public static PeriodNode createPeriodDurationAndDate(final Literal duration,
        final Literal date, FilterFactory filterFactory) {
        // compute first date from duration Y M D and H M S
        Date lastDate = (Date) date.getValue();
        String strDuration = (String) duration.getValue();

        Date firstDate = DurationUtil.subtractDurationToDate(lastDate, strDuration);

        Literal literalFirstDate = filterFactory.literal(firstDate);

        PeriodNode period = new PeriodNode(literalFirstDate, date);

        return period;
    }

    /**
     * @return Literal with begining date of period
     */
    public Literal getBeginning() {
        return this.begin;
    }

    /**
     * @return with ending date of period
     */
    public Literal getEnding() {
        return this.end;
    }
}
