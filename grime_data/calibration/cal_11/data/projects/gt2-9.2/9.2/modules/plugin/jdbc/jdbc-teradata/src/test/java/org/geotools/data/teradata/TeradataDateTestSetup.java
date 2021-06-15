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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class TeradataDateTestSetup extends JDBCDateTestSetup {


    public TeradataDateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }


    protected void createDateTable() throws Exception {
        run("CREATE TABLE dates (d DATE, dt TIMESTAMP, t TIME)");

        //_date('1998/05/31:12:00:00AM', 'yyyy/mm/dd:hh:mi:ssam'));
        
        run("INSERT INTO DATES VALUES ('1969-12-23' (DATE, format 'yyyy-MM-dd'), '2009-06-28 15:12:41' (TIMESTAMP, format 'YYYY-MM-ddBHH:mi:ss'), '15:12:41' (TIME, format 'HH:mi:ss') );");
        run("INSERT INTO DATES VALUES ('2009-01-15' (DATE, format 'yyyy-MM-dd'), '2009-01-15 13:10:12' (TIMESTAMP, format 'YYYY-MM-ddBHH:mi:ss'), '13:10:12' (TIME, format 'HH:mi:ss') );");
        run("INSERT INTO DATES VALUES ('2009-09-29' (DATE, format 'yyyy-MM-dd'), '2009-09-29 17:54:23' (TIMESTAMP, format 'YYYY-MM-ddBHH:mi:ss'), '17:54:23' (TIME, format 'HH:mi:ss') );");

    }


    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE DATES");
    }

}
