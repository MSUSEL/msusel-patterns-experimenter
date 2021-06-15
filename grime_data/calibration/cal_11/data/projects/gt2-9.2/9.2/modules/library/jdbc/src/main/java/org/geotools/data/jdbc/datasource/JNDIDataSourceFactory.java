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
package org.geotools.data.jdbc.datasource;

import java.io.IOException;
import java.util.Map;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.factory.GeoTools;

/**
 * A datasource factory SPI doing JDNI lookups
 * @author Administrator
 *
 *
 *
 *
 * @source $URL$
 */
public class JNDIDataSourceFactory extends AbstractDataSourceFactorySpi {
    
    public static final Param DSTYPE = new Param("dstype", String.class,
            "Must be JNDI", false);

    public static final Param JNDI_REFNAME = new Param("jdniReferenceName", String.class,
            "The path where the connection pool must be located", true);

    private static final Param[] PARAMS = new Param[] { DSTYPE, JNDI_REFNAME };

    public DataSource createDataSource(Map params) throws IOException {
        return createNewDataSource(params);
    }
    
    public boolean canProcess(Map params) {
        return super.canProcess(params) && "JNDI".equals(params.get("dstype"));
    }

    public DataSource createNewDataSource(Map params) throws IOException {
        String refName = (String) JNDI_REFNAME.lookUp(params);
        try {
            return (DataSource) GeoTools.getInitialContext(GeoTools.getDefaultHints()).lookup(refName);
        } catch (Exception e) {
            throw new DataSourceException("Could not find the specified data source in JNDI", e);
        }
    }

    public String getDescription() {
        return "A JNDI based DataSource locator. Provide the JDNI location of a DataSource object in order to make it work";
    }

    public Param[] getParametersInfo() {
        return PARAMS;
    }

    /**
     * Make sure a JNDI context is available
     */
    public boolean isAvailable() {
        try {
            GeoTools.getInitialContext(GeoTools.getDefaultHints());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
