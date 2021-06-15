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
package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCEmptyTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class OracleEmptyTestSetup extends JDBCEmptyTestSetup {

    protected OracleEmptyTestSetup() {
        super(new OracleTestSetup());
    }

    @Override
    protected void createEmptyTable() throws Exception {
        String sql = "CREATE TABLE empty (" 
            + "id INT, geom MDSYS.SDO_GEOMETRY, PRIMARY KEY(id))";
        run(sql);
        sql = "CREATE SEQUENCE empty_id_seq";
        run(sql);
        
        sql = "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) " + 
         "VALUES ('EMPTY','GEOM',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " + 
         "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)";
        run(sql);
        
        sql = "CREATE INDEX EMPTY_GEOMETRY_IDX ON EMPTY(GEOM) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                        + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POINT\"')";
        run(sql);
    }

    @Override
    protected void dropEmptyTable() throws Exception {
        try {
            run("DROP TRIGGER empty_pkey_trigger");
        } catch (Exception e) {
        }
        try {
            run("DROP TABLE empty purge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            run("DROP SEQUENCE empty_id_seq");
        } catch (Exception e) {
        }
     
        run("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'EMPTY'");
    }

}
