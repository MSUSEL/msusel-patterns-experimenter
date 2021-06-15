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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import org.opengis.util.GenericName;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the various {@link InternationalString} implementations.
 *
 * @author Martin Desruisseaux (IRD)
 *
 *
 * @source $URL$
 * @version $Id$
 */
public final class InternationalStringTest {
    /**
     * Tests the {@link SimpleInternationalString} implementation.
     */
    @Test
    public void testSimple() throws IOException, ClassNotFoundException {
        final String message = "This is an unlocalized message";
        final SimpleInternationalString toTest = new SimpleInternationalString(message);
        assertSame("Construction:", message, toTest.toString());
        basicTests(toTest);
    }

    /**
     * Tests the {@link SimpleInternationalString} implementation.
     */
    @Test
    public void testGrowable() throws IOException, ClassNotFoundException {
        final String message     = "This is an unlocalized message";
        final String messageEn   = "This is a localized message";
        final String messageFr   = "Voici un message";
        final String messageFrCa = "Caribou!";
        GrowableInternationalString toTest = new GrowableInternationalString();
        basicTests(toTest);
        toTest.add(Locale.ENGLISH, message);
        assertSame("Addition:", message, toTest.toString());
        basicTests(toTest);

        toTest = new GrowableInternationalString(message);
        assertSame("Construction:", message, toTest.toString());
        basicTests(toTest);
        toTest.add(Locale.ENGLISH, messageEn);
        basicTests(toTest);
        toTest.add(Locale.FRENCH,  messageFr);
        basicTests(toTest);
        assertEquals("Unlocalized message:", message,   toTest.toString(null));
        assertEquals("English message:",     messageEn, toTest.toString(Locale.ENGLISH));
        assertEquals("French message:",      messageFr, toTest.toString(Locale.FRENCH));
        assertEquals("French message:",      messageFr, toTest.toString(Locale.CANADA_FRENCH));
        assertEquals("Other language:",      message,   toTest.toString(Locale.CHINESE));
        toTest.add(Locale.CANADA_FRENCH, messageFrCa);
        basicTests(toTest);
        assertEquals("Unlocalized message:", message,     toTest.toString(null));
        assertEquals("English message:",     messageEn,   toTest.toString(Locale.ENGLISH));
        assertEquals("French message:",      messageFr,   toTest.toString(Locale.FRENCH));
        assertEquals("French message:",      messageFrCa, toTest.toString(Locale.CANADA_FRENCH));
        assertEquals("Other language:",      message,     toTest.toString(Locale.CHINESE));
    }

    /**
     * Tests the {@link GenericName} implementation.
     */
    @Test
    public void testName() throws IOException, ClassNotFoundException {
        final GenericName name = NameFactory.create("codespace:subspace:name");
        basicTests(name);
        assertEquals("toString:", "codespace:subspace:name", name.toString());
        assertEquals("toString:", "codespace:subspace",      name.scope().name().toString());
        assertEquals("toString:", "codespace",               name.scope().name().scope().name().toString());
        assertSame("asScopedName", name, name.toFullyQualifiedName());
        assertSame("asLocalName",  name, name.tip().toFullyQualifiedName());
    }

    /**
     * Performs basic test on the given object.
     */
    @SuppressWarnings("unchecked")
    private <T extends Comparable> void basicTests(final T toTest)
            throws IOException, ClassNotFoundException
    {
        assertEquals("CompareTo: ", 0, toTest.compareTo(toTest));
        assertEquals("Equals:", toTest, toTest);
        if (toTest instanceof CharSequence) {
            assertEquals("CharSequence:", toTest.toString(),
                    new StringBuilder((CharSequence) toTest).toString());
        }
        /*
         * Tests serialization
         */
        final ByteArrayOutputStream    out = new ByteArrayOutputStream();
        final ObjectOutputStream objectOut = new ObjectOutputStream(out);
        objectOut.writeObject(toTest);
        objectOut.close();

        final ByteArrayInputStream    in = new ByteArrayInputStream(out.toByteArray());
        final ObjectInputStream objectIn = new ObjectInputStream(in);
        final Object              object = objectIn.readObject();
        objectIn.close();

        assertEquals("Serialization:", toTest.getClass(), object.getClass());
        assertEquals("Serialization:", toTest,            object           );
        assertEquals("Hash code:",     toTest.hashCode(), object.hashCode());
    }
}
