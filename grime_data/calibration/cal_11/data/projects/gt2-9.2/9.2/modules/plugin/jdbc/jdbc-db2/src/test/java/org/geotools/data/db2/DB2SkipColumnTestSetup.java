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
package org.geotools.data.db2;

import java.sql.Connection;

import org.geotools.jdbc.JDBCSkipColumnTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class DB2SkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected DB2SkipColumnTestSetup() {
        super(new DB2TestSetup());
    }

    @Override
    protected void createSkipColumnTable() throws Exception {
        Connection con = getDataSource().getConnection();
        
        String stmt = "create table "+DB2TestUtil.SCHEMA_QUOTED+
                        ".\"skipcolumn\" (\"fid\" int generated always as identity (start with 0, increment by 1)," +
                        "\"id\" int ,"+
                        " \"geom\" DB2GSE.ST_GEOMETRY, " +
                        "\"weirdproperty\" XML, " +
                        "\"name\" varchar(255), " +
                        "primary key (\"fid\"))";
        con.prepareStatement(stmt    ).execute();    
        DB2Util.executeRegister(DB2TestUtil.SCHEMA, "auto", "geom", DB2TestUtil.SRSNAME, con);
        
        con.prepareStatement( "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"skipcolumn\" " +
        		"(\"id\",\"geom\",\"weirdproperty\",\"name\")  " +
        		"VALUES (0, db2gse.st_GeomFromText('POINT(0 0)', "+DB2TestUtil.SRID+"), null, 'GeoTools')" ).execute();        
        con.close();
        

    }

    @Override
    protected void dropSkipColumnTable() throws Exception {
        Connection con = getDataSource().getConnection();
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "skipcolumn", "geom",  con);
        con.prepareStatement("DROP TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"skipcolumn\"").execute();
        con.close();
    }

}
