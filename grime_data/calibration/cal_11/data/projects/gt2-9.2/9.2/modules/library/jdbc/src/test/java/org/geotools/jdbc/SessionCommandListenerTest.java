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
package org.geotools.jdbc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.geotools.filter.function.EnvFunction;
import org.junit.Test;

import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockStatement;

public class SessionCommandListenerTest {

    static class RecordingConnection extends MockConnection {

        List<String> commands = new ArrayList<String>();

        public java.sql.Statement createStatement() throws java.sql.SQLException {
            return new MockStatement(this) {
                public boolean execute(String sql) throws java.sql.SQLException {
                    commands.add(sql);
                    return false;
                }
            };
        }
    };

    RecordingConnection conn = new RecordingConnection();

    JDBCDataStore store = new JDBCDataStore();

    @Test
    public void testPlain() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener("A", "B");

        // check borrow
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("A", conn.commands.get(0));
        conn.commands.clear();

        // check release
        listener.onRelease(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("B", conn.commands.get(0));
    }
    
    @Test
    public void testOnlyBorrow() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener("A", null);

        // check borrow
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("A", conn.commands.get(0));
        conn.commands.clear();

        // check release
        listener.onRelease(store, conn);
        assertEquals(0, conn.commands.size());
    }
    
    @Test
    public void testOnlyRelease() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener(null, "B");

        // check borrow
        listener.onBorrow(store, conn);
        assertEquals(0, conn.commands.size());

        // check release
        listener.onRelease(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("B", conn.commands.get(0));
    }

    @Test
    public void testExpandVariables() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener("call startSession('${user}')",
                "call endSession('${user,joe}')");
        
        // check borrow
        EnvFunction.setLocalValue("user", "abcde");
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("call startSession('abcde')", conn.commands.get(0));
        conn.commands.clear();
        
        // check release
        EnvFunction.clearLocalValues();
        listener.onRelease(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("call endSession('joe')", conn.commands.get(0));
        conn.commands.clear();
    }
    
    @Test
    public void testInvalid() throws Exception {
        try {
            new SessionCommandsListener("startSession('${user')", null);
            fail("This should have failed, the syntax is not valid");
        } catch(IllegalArgumentException e) {
            // fine
        }
    }

}
