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
package org.geotools.renderer.lite.gridcoverage2d;

import java.awt.Color;

import javax.media.jai.PlanarImage;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.util.SimpleInternationalString;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link TestCase}    subclass for    {@link BaseCoverageProcessingNode}    .
 * @author    Simone Giannecchini, GeoSlutions.
 *
 *
 *
 * @source $URL$
 */
public class BaseCoverageProcessingNodeTest  {

	private BaseCoverageProcessingNode testedObject;

	private BaseCoverageProcessingNode testedObject2;

	@Before
	public void setUp() throws Exception {
		this.testedObject = new BaseCoverageProcessingNode(1,
				SimpleInternationalString.wrap("fake node"),
				SimpleInternationalString.wrap("fake node")) {
			protected GridCoverage2D execute() {
				return CoverageFactoryFinder.getGridCoverageFactory(null).create(
						"name",
						PlanarImage.wrapRenderedImage(RasterSymbolizerTest
								.getSynthetic(Double.NaN)),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),
						new GridSampleDimension[] { new GridSampleDimension(
								"sd", new Category[] { new Category("",
										Color.BLACK, 0) }, null) }, null, null);
			}
		};
		this.testedObject2 = new BaseCoverageProcessingNode(1,
				SimpleInternationalString.wrap("fake node"),
				SimpleInternationalString.wrap("fake node")) {

			protected GridCoverage2D execute() {
				return CoverageFactoryFinder.getGridCoverageFactory(null).create(
						"name",
						PlanarImage.wrapRenderedImage(RasterSymbolizerTest
								.getSynthetic(Double.NaN)),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),
						new GridSampleDimension[] { new GridSampleDimension(
								"sd", new Category[] { new Category("",
										Color.BLACK, 0) }, null) }, null, null);
			}
		};
	}

	@Test
	public final void execute() {
		// execute
		Assert.assertNotNull(testedObject2.getOutput());
		// do nothing
		Assert.assertNotNull(testedObject2.getOutput());

		// add source clean output
		testedObject2.addSource(testedObject);
		testedObject2.addSink(testedObject);
		// recompute
		Assert.assertNotNull(testedObject2.getOutput());

		// dispose
		testedObject2.dispose(true);
	}

	@Test
	public final void dispose() {

		Assert.assertNotNull(testedObject.getOutput());
		// dispose
		testedObject.dispose(true);
		// do nothing
		testedObject.dispose(true);
		try {
			// trying to get the output from a disposed coverage should throw an
			// error
			testedObject.getOutput();
			Assert.assertTrue(false);
		} catch (Exception e) {

		}
	}

	@Test
	public final void addSource() {
		// execute
		Assert.assertNotNull(testedObject2.getOutput());
		// do nothing since we have already executed
		Assert.assertNotNull(testedObject2.getOutput());

		// add source clean output but we also create a cycle which kills our
		// small framework
		testedObject2.addSource((testedObject));
		testedObject.addSink((testedObject2));
		try {
			testedObject2.addSink((testedObject));
			Assert.assertTrue(false);
		} catch (IllegalStateException e) {
			// TODO: handle exception
		}
		// recompute
		Assert.assertNotNull(testedObject2.getOutput());

		// dispose
		testedObject2.dispose(true);
	}

}
