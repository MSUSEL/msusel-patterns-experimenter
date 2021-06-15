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
package org.geotools.data.h2;

import org.geotools.jdbc.JDBCDateTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class H2DateTestSetup extends JDBCDateTestSetup {

    public H2DateTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void createDateTable() throws Exception {
        String sql = "CREATE SCHEMA \"geotools\";";
        runSafe(sql);
        
        run( "CREATE TABLE \"geotools\".\"dates\" (\"d\" DATE, \"dt\" TIMESTAMP, \"t\" TIME)");
        
        run( "INSERT INTO \"geotools\".\"dates\" VALUES (" +
                "PARSEDATETIME('2009-06-28', 'yyyy-MM-dd'), " +
                "PARSEDATETIME('2009-06-28 15:12:41', 'yyyy-MM-dd HH:mm:ss')," +
                "PARSEDATETIME('15:12:41', 'HH:mm:ss')  )");
        
        run( "INSERT INTO \"geotools\".\"dates\" VALUES (" +
                "PARSEDATETIME('2009-01-15', 'yyyy-MM-dd'), " +
                "PARSEDATETIME('2009-01-15 13:10:12', 'yyyy-MM-dd HH:mm:ss')," +
                "PARSEDATETIME('13:10:12', 'HH:mm:ss')  )");
        
        run( "INSERT INTO \"geotools\".\"dates\" VALUES (" +
                "PARSEDATETIME('2009-09-29', 'yyyy-MM-dd'), " +
                "PARSEDATETIME('2009-09-29 17:54:23', 'yyyy-MM-dd HH:mm:ss')," +
                "PARSEDATETIME('17:54:23', 'HH:mm:ss')  )");
    }

    @Override
    protected void dropDateTable() throws Exception {
        run( "DROP TABLE \"geotools\".\"dates\"" );
    }

}
