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
 *    GeoTools - +The Open Source Java GIS Toolkit
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
import java.sql.SQLException;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 *
 * @source $URL$
 */
public class OracleOnlineTest extends AbstractTest {
	static DBDialect dialect = null;

	public OracleOnlineTest(String test) {
		super(test);
	}

	@Override
	protected String getSrsId() {
		return "4326";
	}

	@Override
	public String getConfigUrl() {
		return "file:target/resources/oek.oracle.xml";
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		OracleOnlineTest test = new OracleOnlineTest("");

		if (test.checkPreConditions() == false) {
			return suite;
		}

		suite.addTest(new OracleOnlineTest("testScripts"));
		suite.addTest(new OracleOnlineTest("testGetConnection"));
		suite.addTest(new OracleOnlineTest("testDrop"));
		suite.addTest(new OracleOnlineTest("testCreate"));
		suite.addTest(new OracleOnlineTest("testImage1"));
		suite.addTest(new OracleOnlineTest("testFullExtent"));
		suite.addTest(new OracleOnlineTest("testNoData"));
		suite.addTest(new OracleOnlineTest("testPartial"));
		suite.addTest(new OracleOnlineTest("testVienna"));
		suite.addTest(new OracleOnlineTest("testViennaEnv"));
		suite.addTest(new OracleOnlineTest("testDrop"));
		suite.addTest(new OracleOnlineTest("testCreateJoined"));
		suite.addTest(new OracleOnlineTest("testImage1Joined"));
		suite.addTest(new OracleOnlineTest("testFullExtentJoined"));
		suite.addTest(new OracleOnlineTest("testNoDataJoined"));
		suite.addTest(new OracleOnlineTest("testPartialJoined"));
		suite.addTest(new OracleOnlineTest("testViennaJoined"));
		suite.addTest(new OracleOnlineTest("testViennaEnvJoined"));
		suite.addTest(new OracleOnlineTest("testDrop"));
		suite.addTest(new OracleOnlineTest("testCloseConnection"));

		return suite;
	}

	@Override
	protected String getSubDir() {
		return "oracle";
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

	void executeRegister(String stmt) throws SQLException {
		Connection.prepareStatement(stmt).execute();
	}

	void executeUnRegister(String stmt) throws SQLException {
		Connection.prepareStatement(stmt).execute();
	}

	protected String getXMLConnectFragmentName() {
		return "connect.oracle.xml.inc";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.AbstractTest#getDriverClassName()
	 */
	protected String getDriverClassName() {
		return "oracle.jdbc.OracleDriver";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.AbstractTest#getJDBCUrl(java.lang.String,
	 *      java.lang.Integer, java.lang.String)
	 */
	protected String getJDBCUrl(String host, Integer port, String dbName) {
		return "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
	}

}
