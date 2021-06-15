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

package org.hsqldb.types;

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;

/**
 * Implementation of data item for INTERVAL MONTH.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class IntervalMonthData {

    public final long units;

    public static IntervalMonthData newIntervalYear(long years,
            IntervalType type) {
        return new IntervalMonthData(years * 12, type);
    }

    public static IntervalMonthData newIntervalMonth(long months,
            IntervalType type) {
        return new IntervalMonthData(months, type);
    }

    public IntervalMonthData(long months, IntervalType type) {

        if (months >= type.getIntervalValueLimit()) {
            throw Error.error(ErrorCode.X_22006);
        }

        if (type.typeCode == Types.SQL_INTERVAL_YEAR) {
            months -= (months % 12);
        }

        this.units = months;
    }

    public IntervalMonthData(long months) {
        this.units = months;
    }

    public boolean equals(Object other) {

        if (other instanceof IntervalMonthData) {
            return units == ((IntervalMonthData) other).units;
        }

        return false;
    }

    public int hashCode() {
        return (int) units;
    }

    public int compareTo(IntervalMonthData b) {

        long diff = units - b.units;

        if (diff == 0) {
            return 0;
        } else {
            return diff > 0 ? 1
                            : -1;
        }
    }

    public String toString() {
        return Type.SQL_INTERVAL_MONTH_MAX_PRECISION.convertToString(this);
    }
}
