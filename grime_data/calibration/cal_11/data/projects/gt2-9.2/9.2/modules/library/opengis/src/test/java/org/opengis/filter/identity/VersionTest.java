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
package org.opengis.filter.identity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.opengis.filter.identity.Version.Action;

public class VersionTest {

    @Test
    public void bitwise() {
        for (Action action : Action.values()) {
            long encoded = Version.UNION_ACTION | ((long) action.ordinal());
            
            assertTrue( (encoded & Version.UNION_ACTION) > 0 );
            long decoded = Version.UNION_MASK & ((long)encoded);
            
            Action found = Action.lookup((int)decoded);
            assertEquals( action, found );
        }

    }

    @Test
    public void versionInteger() {
        try {
            new Version(-1);
            fail("Expected IAE on negative version");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            new Version(0);
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        Integer testInt = new Integer(1234567890);
        Version version = new Version(testInt);

        assertNotNull(version.getIndex());
        assertTrue( version.isIndex() );
        assertEquals(1234567890, (int) version.getIndex());

        assertFalse( version.isVersionAction() );
        assertNull(version.getVersionAction());
        
        assertNull(version.getDateTime());
    }

    @Test
    public void versionDate() {
        Date now = new Date();

        Version version = new Version(now);

        assertTrue( version.isDateTime() );
        assertEquals(now, version.getDateTime());
        assertNull(version.getIndex());
        assertNull(version.getVersionAction());
    }

    @Test
    public void versionAction() {
        Version version = new Version(Version.Action.ALL);

        assertEquals(Version.Action.ALL, version.getVersionAction());
        
        assertTrue(version.isVersionAction());

        assertNull(version.getIndex());
        assertNull(version.getDateTime());
    }
    @Test
    public void versionEmpty() {
        Version version = new Version();

        assertTrue(version.isEmpty());
    }

}
