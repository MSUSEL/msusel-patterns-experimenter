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
 * Implementation of data item for INTERVAL SECOND.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class IntervalSecondData {

    public final long units;
    public final int  nanos;

    public static IntervalSecondData newIntervalDay(long days,
            IntervalType type) {
        return new IntervalSecondData(days * 24 * 60 * 60, 0, type);
    }

    public static IntervalSecondData newIntervalHour(long hours,
            IntervalType type) {
        return new IntervalSecondData(hours * 60 * 60, 0, type);
    }

    public static IntervalSecondData newIntervalMinute(long minutes,
            IntervalType type) {
        return new IntervalSecondData(minutes * 60, 0, type);
    }

    public static IntervalSecondData newIntervalSeconds(long seconds,
            IntervalType type) {
        return new IntervalSecondData(seconds, 0, type);
    }

    public IntervalSecondData(long seconds, int nanos, IntervalType type) {

        if (seconds >= type.getIntervalValueLimit()) {
            throw Error.error(ErrorCode.X_22015);
        }

        this.units = seconds;
        this.nanos = nanos;
    }

    public IntervalSecondData(long seconds, int nanos) {
        this.units = seconds;
        this.nanos = nanos;
    }

    /**
     * normalise is a marker, values are always normalised
     */
    public IntervalSecondData(long seconds, long nanos, IntervalType type,
                              boolean normalise) {

        if (nanos >= DTIType.limitNanoseconds) {
            long carry = nanos / DTIType.limitNanoseconds;

            nanos   = nanos % DTIType.limitNanoseconds;
            seconds += carry;
        } else if (nanos <= -DTIType.limitNanoseconds) {
            long carry = -nanos / DTIType.limitNanoseconds;

            nanos   = -(-nanos % DTIType.limitNanoseconds);
            seconds -= carry;
        }

        int scaleFactor = DTIType.nanoScaleFactors[type.scale];

        nanos /= scaleFactor;
        nanos *= scaleFactor;

        if (seconds > 0 && nanos < 0) {
            nanos += DTIType.limitNanoseconds;

            seconds--;
        } else if (seconds < 0 && nanos > 0) {
            nanos -= DTIType.limitNanoseconds;

            seconds++;
        }

        scaleFactor = DTIType.yearToSecondFactors[type.endPartIndex];
        seconds     /= scaleFactor;
        seconds     *= scaleFactor;

        if (seconds >= type.getIntervalValueLimit()) {
            throw Error.error(ErrorCode.X_22015);
        }

        this.units = seconds;
        this.nanos = (int) nanos;
    }

    public boolean equals(Object other) {

        if (other instanceof IntervalSecondData) {
            return units == ((IntervalSecondData) other).units
                   && nanos == ((IntervalSecondData) other).nanos;
        }

        return false;
    }

    public int hashCode() {
        return (int) units ^ nanos;
    }

    public int compareTo(IntervalSecondData b) {

        long diff = units - b.units;

        if (diff == 0) {
            diff = nanos - b.nanos;

            if (diff == 0) {
                return 0;
            }
        }

        return diff > 0 ? 1
                        : -1;
    }

    public long getSeconds() {
        return units;
    }

    public int getNanos() {
        return nanos;
    }

    public String toString() {
        return Type.SQL_INTERVAL_SECOND_MAX_FRACTION_MAX_PRECISION
            .convertToString(this);
    }
}
