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

package org.hsqldb.util;

import java.sql.Types;

// fredt@users 20020215 - patch 516309 by Nicolas Bazin - transfer Informix
// sqlbob@users 20020325 - patch 1.7.0 - reengineering

/**
 * Conversions from Informix databases
 *
 * @author Nichola Bazin
 * @version 1.7.0
 */
class InformixTransferHelper extends TransferHelper {

    public InformixTransferHelper() {
        super();
    }

    public InformixTransferHelper(TransferDb database, Traceable t,
                                  String q) {
        super(database, t, q);
    }

    void setSchema(String _Schema) {
        sSchema = "\"" + _Schema + "\"";
    }

    int convertFromType(int type) {

        //Correct a bug in Informix JDBC driver that maps:
        // DATETIME YEAR TO FRACTION to TIME and
        // DATETIME HOUR TO SECOND to TIMESTAMP
        if (type == Types.TIMESTAMP) {
            type = Types.TIME;

            tracer.trace("Converted INFORMIX TIMESTAMP to TIME");
        } else if (type == Types.TIME) {
            type = Types.TIMESTAMP;

            tracer.trace("Converted INFORMIX TIME to TIMESTAMP");
        }

        return (type);
    }

    int convertToType(int type) {

        //Correct a bug in Informix JDBC driver that maps:
        // DATETIME YEAR TO FRACTION to TIME and
        // DATETIME HOUR TO SECOND to TIMESTAMP
        if (type == Types.TIMESTAMP) {
            type = Types.TIME;

            tracer.trace("Converted TIMESTAMP to INFORMIX TIME");
        } else if (type == Types.TIME) {
            type = Types.TIMESTAMP;

            tracer.trace("Converted TIME to INFORMIX TIMESTAMP");
        }

        return (type);
    }
}
