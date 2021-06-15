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

package org.hsqldb.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.TestCase;

public class TestNullInUnion extends TestCase {

    public void testUnionSubquery() throws Exception {

        Class.forName("org.hsqldb.jdbcDriver").newInstance();

        Connection con = DriverManager.getConnection("jdbc:hsqldb:mem:test",
            "sa", "");
        Statement st = con.createStatement();

        st.execute(
            "CREATE TABLE t1 (id int not null, v1 int, v2 int, primary key(id))");
        st.execute(
            "CREATE TABLE t2 (id int not null, v1 int, v3 int, primary key(id))");
        st.execute("INSERT INTO t1 values(1,1,1)");
        st.execute("INSERT INTO t1 values(2,2,2)");
        st.execute("INSERT INTO t2 values(1,3,3)");

        ResultSet rs = st.executeQuery(
            "select t as atable, a as idvalue, b as value1, c as value2, d as value3 from("
            + "(select 't1' as t, t1.id as a, t1.v1 as b, t1.v2 as c, null as d from t1) union"
            + "(select 't2' as t, t2.id as a, t2.v1 as b, null as c, t2.v3 as d from t2)) order by atable, idvalue");

        assertTrue(rs.next());
        assertEquals("t1", rs.getObject("atable"));
        assertEquals(1, rs.getInt("idvalue"));
        assertEquals(1, rs.getInt("value1"));
        assertEquals(1, rs.getInt("value2"));
        assertEquals(null, rs.getObject("value3"));
        assertTrue(rs.next());
        assertEquals("t1", rs.getObject("atable"));
        assertEquals(2, rs.getInt("idvalue"));
        assertEquals(2, rs.getInt("value1"));
        assertEquals(2, rs.getInt("value2"));
        assertEquals(null, rs.getObject("value3"));
        assertTrue(rs.next());
        assertEquals("t2", rs.getObject("atable"));
        assertEquals(1, rs.getInt("idvalue"));
        assertEquals(3, rs.getInt("value1"));
        assertEquals(null, rs.getObject("value2"));
        assertEquals(3, rs.getInt("value3"));    //this fails!
        assertFalse(rs.next());
    }
}
