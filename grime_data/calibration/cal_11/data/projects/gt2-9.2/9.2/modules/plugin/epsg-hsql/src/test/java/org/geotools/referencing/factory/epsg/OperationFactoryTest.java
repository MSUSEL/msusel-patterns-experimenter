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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import java.util.Iterator;
import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.Transformation;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.ConcatenatedOperation;

import org.geotools.referencing.operation.BufferedCoordinateOperationFactory;
import org.geotools.referencing.operation.AbstractCoordinateOperation;
import org.geotools.referencing.operation.AuthorityBackedFactory;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.resources.Arguments;
import org.geotools.resources.Classes;


/**
 * Tests the usage of {@link CoordinateOperationFactory} with the help of the
 * EPSG database. Any EPSG plugin should fit. However, this test live in the
 * {@code plugin/epsg-hsql} module since the HSQL plugin is the only one which
 * is garantee to work on any machine running Maven.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class OperationFactoryTest extends TestCase {
    /**
     * Run the suite from the command line. If {@code "-log"} flag is specified on the
     * command-line, then the logger will be set to {@link Level#CONFIG}. This is usefull
     * for tracking down which data source is actually used.
     */
    public static void main(final String[] args) {
        final Arguments arguments = new Arguments(args);
        final boolean log = arguments.getFlag("-log");
        arguments.getRemainingArguments(0);
        org.geotools.util.logging.Logging.GEOTOOLS.forceMonolineConsoleOutput(log ? Level.CONFIG : null);
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Returns the test suite.
     */
    public static Test suite() {
        return new TestSuite(OperationFactoryTest.class);
    }

    /**
     * Constructs a test case with the given name.
     */
    public OperationFactoryTest(final String name) {
        super(name);
    }

    /**
     * Returns the first identifier for the specified object.
     */
    private static String getIdentifier(final IdentifiedObject object) {
        return object.getIdentifiers().iterator().next().getCode();
    }

    /**
     * Tests the creation of an operation from EPSG:4230 to EPSG:4326. They are the same
     * CRS than the one tested in {@link DefaultDataSourceTest#testTransformations}.
     */
    public void testCreate() throws FactoryException {
        final CRSAuthorityFactory       crsFactory;
        final CoordinateOperationFactory opFactory;
              CoordinateReferenceSystem  sourceCRS;
              CoordinateReferenceSystem  targetCRS;
              CoordinateOperation        operation;

        crsFactory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        opFactory  = ReferencingFactoryFinder.getCoordinateOperationFactory(null);
        sourceCRS  = crsFactory.createCoordinateReferenceSystem("4230");
        targetCRS  = crsFactory.createCoordinateReferenceSystem("4326");
        operation  = opFactory.createOperation(sourceCRS, targetCRS);

        assertSame(sourceCRS, operation.getSourceCRS());
        assertSame(targetCRS, operation.getTargetCRS());
        assertSame(operation, opFactory.createOperation(sourceCRS, targetCRS));
        assertTrue("Expected a buffered factory but got " + opFactory.getClass().getName(),
                opFactory instanceof BufferedCoordinateOperationFactory);
        assertTrue("EPSG authority factory not found.",
                ((BufferedCoordinateOperationFactory) opFactory).getImplementationHints().
                get(Hints.COORDINATE_OPERATION_FACTORY) instanceof AuthorityBackedFactory);
        assertEquals("1612", getIdentifier(operation)); // See comment in DefaultDataSourceTest.
        assertEquals(1.0, AbstractCoordinateOperation.getAccuracy(operation), 1E-6);
        assertTrue(operation instanceof Transformation);
        /*
         * Tests a transformation not backed directly by an authority factory.
         * However, the inverse transform may exist in the authority factory.
         */
        sourceCRS  = crsFactory.createCoordinateReferenceSystem("4326");
        targetCRS  = crsFactory.createCoordinateReferenceSystem("2995");
        operation  = opFactory.createOperation(sourceCRS, targetCRS);
        assertTrue("This test needs an operation not backed by the EPSG factory.",
                   operation.getIdentifiers().isEmpty());
        // Should contains exactly one transformations and an arbitrary number of conversions.
        assertTrue(operation instanceof ConcatenatedOperation);
        int count = 0;
        for (final Iterator it=((ConcatenatedOperation) operation).getOperations().iterator(); it.hasNext();) {
            final CoordinateOperation op = (CoordinateOperation) it.next();
            if (op instanceof Transformation) {
                count++;
            } else {
                assertTrue("Expected Conversion but got " +
                           Classes.getShortName(AbstractCoordinateOperation.getType(op)) + ". ",
                           (op instanceof Conversion));
            }
        }
        assertEquals("The coordinate operation should contains exactly 1 transformation", 1, count);
        assertTrue(AbstractCoordinateOperation.getAccuracy(operation) <= 25);
    }
}
