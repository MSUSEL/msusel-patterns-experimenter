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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCDateTestSetup;

public class SQLServerDateTestSetup extends JDBCDateTestSetup {

    protected SQLServerDateTestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createDateTable() throws Exception {
        run( "CREATE TABLE dates (d DATE, dt DATETIME, t TIME)");
        
        run( "INSERT INTO dates VALUES (" +
            "CAST('2009-06-28' as DATE), " +
            "CAST('2009-06-28 15:12:41' as DATETIME)," +
            "CAST('15:12:41' as TIME)  )");
        
        run( "INSERT INTO dates VALUES (" +
                "CAST('2009-01-15' as DATE), " +
                "CAST('2009-01-15 13:10:12' as DATETIME)," +
                "CAST('13:10:12' as TIME)  )");
        
        run( "INSERT INTO dates VALUES (" +
                "CAST('2009-09-29' as DATE), " +
                "CAST('2009-09-29 17:54:23' as DATETIME)," +
                "CAST('17:54:23' as TIME)  )");
    }

    @Override
    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE dates");
    }

}
