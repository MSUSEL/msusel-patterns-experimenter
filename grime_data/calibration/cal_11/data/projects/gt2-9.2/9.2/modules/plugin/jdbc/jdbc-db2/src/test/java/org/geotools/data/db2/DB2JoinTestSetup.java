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
package org.geotools.data.db2;

import java.sql.Connection;

import org.geotools.jdbc.JDBCJoinTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class DB2JoinTestSetup extends JDBCJoinTestSetup {

    public DB2JoinTestSetup() {
        super(new DB2TestSetup());
    }
    
    @Override
    protected void createJoinTable() throws Exception {
                
        Connection con = getDataSource().getConnection();
        
        String stmt = "create table "+DB2TestUtil.SCHEMA_QUOTED+
                        ".\"ftjoin\" (\"id\" int ," +
                        "\"name\" varchar(255), " +
                        " \"geom\" DB2GSE.ST_GEOMETRY ) ";
                        
        con.prepareStatement(stmt    ).execute();    
        DB2Util.executeRegister(DB2TestUtil.SCHEMA, "ftjoin", "geom", DB2TestUtil.SRSNAME, con);
        
        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin\" (\"id\",\"name\", \"geom\")  " +                        
                "VALUES (0, 'zero',db2gse.st_GeomFromText('POLYGON ((-0.1 -0.1, -0.1 0.1, 0.1 0.1, 0.1 -0.1, -0.1 -0.1))', "+DB2TestUtil.SRID+"))" )
                .execute();
        
        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin\" (\"id\",\"name\", \"geom\")  " +                        
                "VALUES (1, 'one',db2gse.st_GeomFromText('POLYGON ((-1.1 -1.1, -1.1 1.1, 1.1 1.1, 1.1 -1.1, -1.1 -1.1))', "+DB2TestUtil.SRID+"))" )
                .execute();

        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin\" (\"id\",\"name\", \"geom\")  " +                        
                "VALUES (2, 'two',db2gse.st_GeomFromText('POLYGON ((-10 -10, -10 10, 10 10, 10 -10, -10 -10))', "+DB2TestUtil.SRID+"))" )
                .execute();

        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin\" (\"id\",\"name\", \"geom\")  " +                        
                "VALUES (3, 'three',NULL)" )
                .execute();

        
        con.close();
        
        
        
    }

    @Override
    protected void dropJoinTable() throws Exception {
        Connection con = getDataSource().getConnection();
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "ftjoin", "geom",  con);
        con.prepareStatement("DROP TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin\"").execute();
        con.close();
        
    }

}
