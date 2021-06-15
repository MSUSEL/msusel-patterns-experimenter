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

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class IngresTestSetup extends JDBCTestSetup {

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new IngresDataStoreFactory();
    }
    
    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema(null);
    }
    
    @Override
    protected String typeName(String raw) {
    	return raw.toLowerCase();
    }
    
    @Override
    protected String attributeName(String raw) {
    	return raw.toLowerCase();
    }
   
    @Override
    protected void setUpData() throws Exception {
        runSafe("DROP TABLE ft1");
        runSafe("DROP TABLE ft2");

        run("CREATE TABLE ft1(" //
                + "id int primary key, " //
                + "geometry geometry SRID 4326, " //
                + "intProperty int," //
                + "doubleProperty double precision, " // 
                + "stringProperty varchar(256))");
        
        run("INSERT INTO ft1 VALUES(0, GeometryFromText('POINT(0 0)', 4326), 0, 0.0, 'zero')");
        run("INSERT INTO ft1 VALUES(1, GeometryFromText('POINT(1 1)', 4326), 1, 1.1, 'one')");
        run("INSERT INTO ft1 VALUES(2, GeometryFromText('POINT(2 2)', 4326), 2, 2.2, 'two')");
    }

    
}
