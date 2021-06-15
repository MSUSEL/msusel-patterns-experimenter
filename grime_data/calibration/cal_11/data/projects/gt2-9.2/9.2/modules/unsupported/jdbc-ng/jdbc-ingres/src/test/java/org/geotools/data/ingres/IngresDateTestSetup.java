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
package org.geotools.data.ingres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class IngresDateTestSetup extends JDBCDateTestSetup {


    public IngresDateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createDateTable() throws Exception {
        Connection con = getDataSource().getConnection();
        con.prepareStatement("CREATE TABLE DATES (D ANSIDATE, DT TIMESTAMP, T TIME)").execute();
        
        PreparedStatement ps = con.prepareStatement("INSERT INTO DATES VALUES (?,?,?)");
        ps.setDate(1, java.sql.Date.valueOf("2009-06-28"));
        //ps.setTimestamp(2, java.sql.Timestamp.valueOf("2009-06-28 15:12:41.0"));
        ps.setTimestamp(2,  new java.sql.Timestamp(new java.text.SimpleDateFormat
        		("HH:mm:ss,dd-yyyy-MM").parse("15:12:41,28-2009-06").getTime()));
        ps.setTime(3, java.sql.Time.valueOf("15:12:41"));
        ps.execute();
        ps.setDate(1, java.sql.Date.valueOf("2009-01-15"));
 //       ps.setTimestamp(2, java.sql.Timestamp.valueOf("2009-01-15 13:10:12.0"));
        ps.setTimestamp(2,  new java.sql.Timestamp(new java.text.SimpleDateFormat
        		("HH:mm:ss,dd-yyyy-MM").parse("13:10:12,15-2009-01").getTime()));
        ps.setTime(3, java.sql.Time.valueOf("13:10:12"));
        ps.execute();
        ps.setDate(1, java.sql.Date.valueOf("2009-09-29"));
 //       ps.setTimestamp(2, java.sql.Timestamp.valueOf("2009-09-29 17:54:23.0"));
        ps.setTimestamp(2,  new java.sql.Timestamp(new java.text.SimpleDateFormat
        		("HH:mm:ss,dd-yyyy-MM").parse("17:54:23,29-2009-09").getTime()));
        ps.setTime(3, java.sql.Time.valueOf("17:54:23"));
        ps.execute();
        ps.close();
        con.close();               
    }

    @Override
    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE DATES");
    }

}
