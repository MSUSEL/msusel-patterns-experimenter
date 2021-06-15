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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class PostgisDateTestSetup extends JDBCDateTestSetup {


    public PostgisDateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createDateTable() throws Exception {
        run( "CREATE TABLE DATES (D DATE, DT TIMESTAMP, T TIME)");
        
        //_date('1998/05/31:12:00:00AM', 'yyyy/mm/dd:hh:mi:ssam'));
        
        run( "INSERT INTO DATES VALUES (" +
                "TO_DATE('2009-06-28', 'yyyy-MM-dd'), " +
                "TO_TIMESTAMP('2009-06-28 15:12:41', 'yyyy-MM-dd HH24:mi:ss')," +
                "TO_TIMESTAMP('15:12:41', 'HH24:mi:ss')  )");
        
        run( "INSERT INTO DATES VALUES (" +
                "TO_DATE('2009-01-15', 'yyyy-MM-dd'), " +
                "TO_TIMESTAMP('2009-01-15 13:10:12', 'yyyy-MM-dd HH24:mi:ss')," +
                "TO_TIMESTAMP('13:10:12', 'HH24:mi:ss')  )");
        
        run( "INSERT INTO DATES VALUES (" +
                "TO_DATE('2009-09-29', 'yyyy-MM-dd'), " +
                "TO_TIMESTAMP('2009-09-29 17:54:23', 'yyyy-MM-dd HH24:mi:ss')," +
                "TO_TIMESTAMP('17:54:23', 'HH24:mi:ss')  )");
    }

    @Override
    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE DATES");
    }

}
