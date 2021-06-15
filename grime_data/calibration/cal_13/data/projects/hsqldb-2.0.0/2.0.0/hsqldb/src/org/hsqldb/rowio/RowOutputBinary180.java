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

package org.hsqldb.rowio;

import org.hsqldb.HsqlDateTime;
import org.hsqldb.types.TimeData;
import org.hsqldb.types.TimestampData;
import org.hsqldb.types.Type;
import org.hsqldb.types.Types;

/**
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class RowOutputBinary180 extends RowOutputBinary {

    public RowOutputBinary180(int initialSize, int scale) {
        super(initialSize, scale);
    }

    protected void writeDate(TimestampData o, Type type) {

        long millis = o.getSeconds() * 1000L;

        millis =
            HsqlDateTime.convertMillisToCalendar(HsqlDateTime.tempCalDefault,
                millis);

        writeLong(millis);
        writeLong(o.getSeconds() * 1000L);
    }

    protected void writeTime(TimeData o, Type type) {

        if (type.typeCode == Types.SQL_TIME) {
            long millis = o.getSeconds() * 1000L;

            millis = HsqlDateTime.convertMillisToCalendar(
                HsqlDateTime.tempCalDefault, millis);

            writeLong(millis);
        } else {
            writeInt(o.getSeconds());
            writeInt(o.getNanos());
            writeInt(o.getZone());
        }
    }

    protected void writeTimestamp(TimestampData o, Type type) {

        if (type.typeCode == Types.SQL_TIMESTAMP) {
            long millis = o.getSeconds() * 1000L;

            millis = HsqlDateTime.convertMillisToCalendar(
                HsqlDateTime.tempCalDefault, millis);

            writeLong(millis);
            writeInt(o.getNanos());
        } else {
            writeLong(o.getSeconds());
            writeInt(o.getNanos());
            writeInt(o.getZone());
        }
    }
}
