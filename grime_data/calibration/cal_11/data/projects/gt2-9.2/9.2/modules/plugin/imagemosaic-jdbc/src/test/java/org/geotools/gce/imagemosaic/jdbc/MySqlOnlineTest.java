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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.jdbc;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 *
 * @source $URL$
 */
public class MySqlOnlineTest extends AbstractTest {
	static DBDialect dialect = null;

	public MySqlOnlineTest(String test) {
		super(test);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		MySqlOnlineTest test = new MySqlOnlineTest("");

		if (test.checkPreConditions() == false) {
			return suite;
		}

		suite.addTest(new MySqlOnlineTest("testScripts"));
		suite.addTest(new MySqlOnlineTest("testGetConnection"));
		suite.addTest(new MySqlOnlineTest("testDrop"));
		suite.addTest(new MySqlOnlineTest("testCreate"));
		suite.addTest(new MySqlOnlineTest("testImage1"));
		suite.addTest(new MySqlOnlineTest("testFullExtent"));
		suite.addTest(new MySqlOnlineTest("testNoData"));
		suite.addTest(new MySqlOnlineTest("testPartial"));
		suite.addTest(new MySqlOnlineTest("testVienna"));
		suite.addTest(new MySqlOnlineTest("testViennaEnv"));
		suite.addTest(new MySqlOnlineTest("testDrop"));
		suite.addTest(new MySqlOnlineTest("testCreateJoined"));
		suite.addTest(new MySqlOnlineTest("testImage1Joined"));
		suite.addTest(new MySqlOnlineTest("testFullExtentJoined"));
		suite.addTest(new MySqlOnlineTest("testNoDataJoined"));
		suite.addTest(new MySqlOnlineTest("testPartialJoined"));
		suite.addTest(new MySqlOnlineTest("testViennaJoined"));
		suite.addTest(new MySqlOnlineTest("testViennaEnvJoined"));
		suite.addTest(new MySqlOnlineTest("testDrop"));
		suite.addTest(new MySqlOnlineTest("testCloseConnection"));

		return suite;
	}

	@Override
	public String getConfigUrl() {
		return "file:target/resources/oek.mysql.xml";
	}

	@Override
	protected String getSubDir() {
		return "mysql";
	}

	@Override
	protected DBDialect getDBDialect() {
		if (dialect != null) {
			return dialect;
		}

		Config config = null;

		try {
			config = Config.readFrom(new URL(getConfigUrl()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		dialect = DBDialect.getDBDialect(config);

		return dialect;
	}

	protected String getXMLConnectFragmentName() {
		return "connect.mysql.xml.inc";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.AbstractTest#getDriverClassName()
	 */
	protected String getDriverClassName() {
		return "com.mysql.jdbc.Driver";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.AbstractTest#getJDBCUrl(java.lang.String,
	 *      java.lang.Integer, java.lang.String)
	 */
	protected String getJDBCUrl(String host, Integer port, String dbName) {
		return "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	}

}
