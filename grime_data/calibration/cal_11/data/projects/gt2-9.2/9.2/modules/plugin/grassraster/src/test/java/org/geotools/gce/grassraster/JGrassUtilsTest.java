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
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster;

import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import junit.framework.TestCase;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Test the {@link JGrassMapEnvironment} class and the created paths.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 * @source $URL$
 */
@SuppressWarnings("nls")
public class JGrassUtilsTest extends TestCase {

    public void testScaling() throws IOException, NoSuchAuthorityCodeException, FactoryException {
        double[][] mapData = new double[][]{//
        {1000.0, 1000.0, 1200.0, 1250.0, 1300.0, 1350.0, 1450.0}, //
                {750.0, 850.0, 860.0, 900.0, 1000.0, 1200.0, 1250.0}, //
                {700.0, 750.0, 800.0, 850.0, 900.0, 1000.0, 1100.0}, //
                {650.0, 700.0, 750.0, 800.0, 850.0, 490.0, 450.0}, //
                {430.0, 500.0, 600.0, 700.0, 800.0, 500.0, 450.0}, //
                {700.0, 750.0, 760.0, 770.0, 850.0, 1000.0, 1150.0} //
        };

        double[][] mapDataAfter = new double[][]{//
        {1000.0, 1200.0, 1250.0, 1300.0, 1450.0}, //
                {700.0, 800.0, 850.0, 900.0, 1100.0}, //
                {650.0, 750.0, 800.0, 850.0, 450.0}, //
                {700.0, 760.0, 770.0, 850.0, 1150.0} //
        };

        double n = 5140020.0;
        double s = 5139840.0;
        double w = 1640710.0;
        double e = 1640920.0;
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32632");
        GridCoverage2D elevationCoverage = JGrassUtilities.buildCoverage("elevation", mapData, n, s, w, e, crs, true);

        RenderedImage scaledJAIImage = JGrassUtilities.scaleJAIImage(5, 4, elevationCoverage.getRenderedImage(), null);
        checkMatrixEqual(scaledJAIImage, mapDataAfter, 0.0);
    }

    // public void testScaling2() throws IOException, NoSuchAuthorityCodeException, FactoryException
    // {
    // double[][] mapData = new double[][]{//
    // {1000.0, 1000.0, 1200.0, 1250.0, 1300.0, 1350.0, 1450.0}, //
    // {750.0, 850.0, 860.0, 900.0, 1000.0, 1200.0, 1250.0}, //
    // {700.0, 750.0, 800.0, 850.0, 900.0, 1000.0, 1100.0}, //
    // {650.0, 700.0, 750.0, 800.0, 850.0, 490.0, 450.0}, //
    // {430.0, 500.0, 600.0, 700.0, 800.0, 500.0, 450.0}, //
    // {700.0, 750.0, 760.0, 770.0, 850.0, 1000.0, 1150.0} //
    // };
    //
    // double[][] mapDataAfter = new double[][]{//
    // {1000.0, 1200.0, 1250.0, 1300.0, 1450.0}, //
    // {750.0, 860.0, 900.0, 1000.0, 1250.0}, //
    // {700.0, 800.0, 850.0, 900.0, 1100.0}, //
    // // {650.0, 750.0, 800.0, 850.0, 450.0}, //
    // {430.0, 600.0, 700.0, 800.0, 450.0}, //
    // {700.0, 760.0, 770.0, 850.0, 1150.0} //
    // };
    //
    // double n = 5140020.0;
    // double s = 5139840.0;
    // double w = 1640710.0;
    // double e = 1640920.0;
    // CoordinateReferenceSystem crs = CRS.decode("EPSG:32632");
    // GridCoverage2D elevationCoverage = JGrassUtilities.buildCoverage("elevation", mapData, n, s,
    // w, e, crs, true);
    //
    // // JGrassUtilities.printImage(elevationCoverage);
    // RenderedImage scaledJAIImage = JGrassUtilities.scaleJAIImage(5, 5,
    // elevationCoverage.getRenderedImage(), null);
    // // System.out.println("***************************");
    //
    // // JGrassUtilities.printImage(scaledJAIImage);
    // // TODO: got 700 (rather than 650) on mac osx
    // checkMatrixEqual(scaledJAIImage, mapDataAfter, 0.0);
    // }

    protected void checkMatrixEqual( RenderedImage image, double[][] matrix, double delta ) {
        RectIter rectIter = RectIterFactory.create(image, null);
        int y = 0;
        do {
            int x = 0;
            do {
                double value = rectIter.getSampleDouble();
                double expectedResult = matrix[y][x];
                if (Double.isNaN(value)) {
                    assertTrue(x + " " + y, Double.isNaN(expectedResult));
                } else {
                    assertEquals(x + " " + y, expectedResult, value, delta);
                }
                x++;
            } while( !rectIter.nextPixelDone() );
            rectIter.startPixels();
            y++;
        } while( !rectIter.nextLineDone() );
    }
}
