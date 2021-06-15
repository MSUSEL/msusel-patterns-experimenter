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

/**
 * Implementation of data item for TIMESTAMP.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class TimestampData {

    final long seconds;
    final int  nanos;
    final int  zone;

    public TimestampData(long seconds) {

        this.seconds = seconds;
        this.nanos   = 0;
        this.zone    = 0;
    }

    public TimestampData(long seconds, int nanos) {

        this.seconds = seconds;
        this.nanos   = nanos;
        this.zone    = 0;
    }

    public TimestampData(long seconds, int nanos, int zoneSeconds) {

        this.seconds = seconds;
        this.nanos   = nanos;
        this.zone    = zoneSeconds;
    }

    public long getSeconds() {
        return seconds;
    }

    public int getNanos() {
        return nanos;
    }

    public int getZone() {
        return zone;
    }

    public boolean equals(Object other) {

        if (other instanceof TimestampData) {
            return seconds == ((TimestampData) other).seconds
                   && nanos == ((TimestampData) other).nanos
                   && zone == ((TimestampData) other).zone;
        }

        return false;
    }

    public int hashCode() {
        return (int) seconds ^ nanos;
    }

    public int compareTo(TimestampData b) {

        long diff = seconds - b.seconds;

        if (diff == 0) {
            diff = nanos - b.nanos;

            if (diff == 0) {
                return 0;
            }
        }

        return diff > 0 ? 1
                        : -1;
    }
}
