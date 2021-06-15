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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.h2;

import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class H2PrimaryKeyFinderTestSetup extends JDBCPrimaryKeyFinderTestSetup {

    protected H2PrimaryKeyFinderTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void createMetadataTable() throws Exception {
        run("CREATE TABLE gt_pk_metadata ( " + "table_schema VARCHAR, "
                + "table_name VARCHAR NOT NULL, " + "pk_column VARCHAR NOT NULL, "
                + "pk_column_idx INTEGER, " + "pk_policy VARCHAR, " + "pk_sequence VARCHAR)");
    }

    @Override
    protected void dropMetadataTable() throws Exception {
        runSafe("DROP TABLE gt_pk_metadata");
    }

    @Override
    protected void createSequencedPrimaryKeyTable() throws Exception {
        run("CREATE TABLE \"seqtable\" ( \"key\" int PRIMARY KEY, "
                + "\"name\" VARCHAR, \"geom\" GEOMETRY)");
        run("CALL AddGeometryColumn(NULL, 'seqtable', 'geom', 4326, 'GEOMETRY', 2)");
        run("CREATE SEQUENCE pksequence START WITH 1");

        run("INSERT INTO \"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES ("
                + "(SELECT NEXTVAL('pksequence')),'one',NULL)");
        run("INSERT INTO \"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES ("
                + "(SELECT NEXTVAL('pksequence')),'two',NULL)");
        run("INSERT INTO \"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES ("
                + "(SELECT NEXTVAL('pksequence')),'three',NULL)");

        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'seqtable', 'key', 0, 'sequence', 'pksequence')");
    }

    @Override
    protected void dropSequencedPrimaryKeyTable() throws Exception {
        run("DROP TABLE \"seqtable\"");
        run("DROP SEQUENCE pksequence");
    }

    @Override
    protected void createPlainTable() throws Exception {
        run("CREATE TABLE \"plaintable\" ( \"key1\" int, \"key2\" int, "
                + "\"name\" VARCHAR, \"geom\" GEOMETRY)");
        run("CALL AddGeometryColumn(NULL, 'plaintable', 'geom', 4326, 'GEOMETRY', 2)");
        run("INSERT INTO \"plaintable\" VALUES (1, 2, 'one', NULL)");
        run("INSERT INTO \"plaintable\" VALUES (2, 3, 'two', NULL)");
        run("INSERT INTO \"plaintable\" VALUES (3, 4, 'three', NULL)");
    }

    @Override
    protected void dropPlainTable() throws Exception {
        runSafe("DROP TABLE \"plaintable\"");
    }

    @Override
    protected void createAssignedSinglePkView() throws Exception {
        run("CREATE VIEW \"assignedsinglepk\" AS SELECT * FROM \"plaintable\"");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedsinglepk', 'key1', 0, 'assigned', NULL)");
    }

    @Override
    protected void dropAssignedSinglePkView() throws Exception {
        runSafe("DROP VIEW \"assignedsinglepk\"");
    }

    @Override
    protected void createAssignedMultiPkView() throws Exception {
        run("CREATE VIEW \"assignedmultipk\" AS SELECT * FROM \"plaintable\"");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedmultipk', 'key1', 0, 'assigned', NULL)");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedmultipk', 'key2', 1, 'assigned', NULL)");

    }

    @Override
    protected void dropAssignedMultiPkView() throws Exception {
        runSafe("DROP VIEW \"assignedmultipk\"");
    }

}
