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
package org.archive.crawler.settings;

import junit.framework.TestCase;


/**
 * Testing of the SimpleType
 *
 * @author John Erik Halse
 */
public class SimpleTypeTest extends TestCase {
    public void testGetName() {
        SimpleType t1 = new SimpleType("a", "b", "c");
        assertEquals("a", t1.getName());
    }

    public void testGetDescription() {
        SimpleType t1 = new SimpleType("a", "b", "c");
        assertEquals("b", t1.getDescription());
    }

    public void testGetDefaultValue() {
        SimpleType t1 = new SimpleType("a", "b", "c");
        assertEquals("c", t1.getDefaultValue());
    }

    public void testGetLegalValues() {
        SimpleType t1 = new SimpleType("a", "b", "c", new String[] {"d", "e"});
        checkArray(new String[] {"d", "e"}, t1.getLegalValues());
    }

    public void testSetLegalValues() {
        SimpleType t1 = new SimpleType("a", "b", "c", new String[] {"d", "e"});
        t1.setLegalValues(new String[] {"f", "g"});
        checkArray(new String[] {"f", "g"}, t1.getLegalValues());
    }

    public void testGetConstraints() {
        SimpleType t1 = new SimpleType("a1", "b1", "c1");
        SimpleType t2 = new SimpleType("a2", "b2", "c2", new String[] {"d", "e"});
        assertNotNull(t1.getConstraints());
        assertSame(LegalValueTypeConstraint.class, t2.getConstraints().get(0)
                .getClass());
        assertSame(LegalValueListConstraint.class, t2.getConstraints().get(1)
                .getClass());
    }

    public void testGetLegalValueType() {
        SimpleType t1 = new SimpleType("a1", "b1", "c1");
        SimpleType t2 = new SimpleType("a2", "b2", new Integer(1));
        SimpleType t3 = new SimpleType("a3", "b3", new TextField("c3"));
        assertSame(String.class, t1.getLegalValueType());
        assertSame(Integer.class, t2.getLegalValueType());
        assertSame(TextField.class, t3.getLegalValueType());
    }

    public void testEquals() {
        SimpleType t1 = new SimpleType("a1", "b1", "c1");
        SimpleType t2 = new SimpleType("a1", "b1", "c1");
        SimpleType t3 = new SimpleType("a2", "b2", "c2");
        assertTrue(t1.equals(t2));
        assertFalse(t1.equals(t3));
        assertTrue(t1.equals(t1));
        assertFalse(t1.equals(null));
    }

    private void checkArray(Object a1[], Object a2[]) {
        assertEquals("Arrays not of same length.", a1.length, a2.length);
        for (int i = 0; i < a1.length; i++) {
            assertEquals(a1[i], a2[i]);
        }
    }
}
