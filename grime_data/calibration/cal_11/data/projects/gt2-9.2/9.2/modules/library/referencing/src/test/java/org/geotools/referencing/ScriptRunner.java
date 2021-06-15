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
package org.geotools.referencing;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.NumberFormat;

import org.geotools.io.TableWriter;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.MismatchedDimensionException;


/**
 * A console for running test scripts. Most of the work is already done by the subclass.
 * {@code ScriptRunner} mostly add statistics about the test executed. This class is
 * used by {@link ScriptTest}. It can also be run from the command line for executing all
 * files specified in argument.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class ScriptRunner extends Console {
    /**
     * Number of tests run and passed. Used for displaying statistics.
     */
    private int testRun, testPassed;

    /**
     * Creates a new console instance using the specified input stream.
     *
     * @param in The input stream.
     */
    public ScriptRunner(final LineNumberReader in) {
        super(in);
        setPrompt(null);
    }

    /**
     * Invoked automatically when the <code>target pt</code> instruction were executed.
     */
    @Override
    protected void test() throws TransformException, MismatchedDimensionException {
        testRun++;
        super.test();
        testPassed++;
    }

    /**
     * Prints the number of tests executed, the number of errors and the success rate.
     */
    private void printStatistics() throws IOException {
        NumberFormat f = NumberFormat.getNumberInstance();
        final TableWriter table = new TableWriter(out, 1);
        table.setMultiLinesCells(true);
        table.writeHorizontalSeparator();
        table.write("Tests:");
        table.nextColumn();
        table.setAlignment(TableWriter.ALIGN_RIGHT);
        table.write(f.format(testRun));
        table.nextLine();
        table.setAlignment(TableWriter.ALIGN_LEFT);
        table.write("Errors:");
        table.nextColumn();
        table.setAlignment(TableWriter.ALIGN_RIGHT);
        table.write(f.format(testRun - testPassed));
        table.nextLine();
        if (testRun != 0) {
            f = NumberFormat.getPercentInstance();
            table.setAlignment(TableWriter.ALIGN_LEFT);
            table.write("Success rate:");
            table.nextColumn();
            table.setAlignment(TableWriter.ALIGN_RIGHT);
            table.write(f.format((double)testPassed / (double)testRun));
            table.nextLine();
        }
        table.writeHorizontalSeparator();
        table.flush();
    }

    /**
     * Run all tests scripts specified on the command line.
     */
    public static void main(final String[] args) {
        final String lineSeparator = System.getProperty("line.separator", "\r");
        try {
            for (int i=0; i<args.length; i++) {
                final String filename = args[i];
                final LineNumberReader in = new LineNumberReader(new FileReader(filename));
                final ScriptRunner test = new ScriptRunner(in);
                test.out.write("Running \"");
                test.out.write(filename);
                test.out.write('"');
                test.out.write(lineSeparator);
                test.out.flush();
                test.run();
                test.printStatistics();
                test.out.write(lineSeparator);
                test.out.flush();
                in.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
