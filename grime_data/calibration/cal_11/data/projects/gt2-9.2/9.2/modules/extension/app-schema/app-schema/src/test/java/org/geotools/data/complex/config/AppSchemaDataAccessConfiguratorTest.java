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
package org.geotools.data.complex.config;

import org.geotools.data.complex.AppSchemaDataAccessRegistry;
import org.geotools.test.AppSchemaTestSupport;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * This is to test app-schema joining configuration. Joining should be on by
 * default.
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * 
 */
public class AppSchemaDataAccessConfiguratorTest extends AppSchemaTestSupport {

	@Test
	public void testJoiningDefault() {
		// not set in the app-schema properties
		// joining should be on by default
		AppSchemaDataAccessRegistry.clearAppSchemaProperties();
		boolean joining = AppSchemaDataAccessConfigurator.isJoining();
		assertTrue(joining);
	}

	@Test
	public void testJoiningFalse() {
		// test joining set to false
		AppSchemaDataAccessRegistry.getAppSchemaProperties().setProperty(
				AppSchemaDataAccessConfigurator.PROPERTY_JOINING, "false");
		boolean joining = AppSchemaDataAccessConfigurator.isJoining();
		assertFalse(joining);
		AppSchemaDataAccessRegistry.clearAppSchemaProperties();
	}

	@Test
	public void testJoiningTrue() {
		// test joining set to true
		AppSchemaDataAccessRegistry.getAppSchemaProperties().setProperty(
				AppSchemaDataAccessConfigurator.PROPERTY_JOINING, "true");
		boolean joining = AppSchemaDataAccessConfigurator.isJoining();
		assertTrue(joining);
		AppSchemaDataAccessRegistry.clearAppSchemaProperties();
	}

}