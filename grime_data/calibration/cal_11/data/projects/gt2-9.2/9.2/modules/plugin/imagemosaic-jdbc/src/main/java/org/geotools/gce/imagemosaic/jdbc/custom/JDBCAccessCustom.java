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
 *    GeoTools+ - The Open Source Java GIS Toolkit
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

package org.geotools.gce.imagemosaic.jdbc.custom;


import java.awt.Rectangle;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.gce.imagemosaic.jdbc.Config;
import org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo;
import org.geotools.gce.imagemosaic.jdbc.JDBCAccess;
import org.geotools.gce.imagemosaic.jdbc.TileQueueElement;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This class is a base class for customzied JDBCAccess
 * Special implentations should subclass
 * 
 * @author mcr
 * 
 *
 *
 * @source $URL$
 */

public abstract class JDBCAccessCustom implements JDBCAccess {

    private final static Logger LOGGER = Logging.getLogger(JDBCAccessCustom.class
            .getPackage().getName());

    private Config config;
    private DataSource dataSource;

    private List<ImageLevelInfo> levelInfos = new ArrayList<ImageLevelInfo>();

    

    public JDBCAccessCustom(Config config) throws IOException{
        super();
        this.config = config;
        this.dataSource = DataSourceFinder.getDataSource(config.getDataSourceParams());
    }

    /* (non-Javadoc)
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#getLevelInfo(int)
     */
    public ImageLevelInfo getLevelInfo(int level) {
        LOGGER.fine("getLevelInfo Method");
        return levelInfos.get(level);
    }

    /* (non-Javadoc)
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#getNumOverviews()
     */
    public int getNumOverviews() {
        LOGGER.fine("getNumOverviews Method");
        return levelInfos.size() - 1;
    }

    /* (non-Javadoc)
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#initialize()
     */
    abstract public void initialize() throws SQLException, IOException;
    

    /* (non-Javadoc)
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#startTileDecoders(java.awt.Rectangle, org.geotools.geometry.GeneralEnvelope, org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo, java.util.concurrent.LinkedBlockingQueue, org.geotools.coverage.grid.GridCoverageFactory)
     */
    abstract public void startTileDecoders(Rectangle pixelDimension, GeneralEnvelope requestEnvelope,
            ImageLevelInfo info, LinkedBlockingQueue<TileQueueElement> tileQueue,
            GridCoverageFactory coverageFactory) throws IOException;

    /**
     * getConnection
     * 
     * @return Connection
     **/

    protected Connection getConnection() {

        Connection con = null;
        try {

            con = dataSource.getConnection();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return con;
    }

    /**
     * closeConnection
     * 
     * @param conn
     *            Connection Object passed to be closed
     **/

    protected void closeConnection(Connection con) {
        try {

            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * closePreparedStatement
     * 
     * @param stmt
     *            PreparedStatement Object passed to be closed
     **/

    protected void closePreparedStmt(PreparedStatement stmt) {
        try {

            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * closeCallableStatement
     * 
     * @param stmt
     *            CallableStatement Object passed to be closed
     **/

    protected void closeCallableStmt(CallableStatement stmt) {
        try {

            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * closeStmt
     * 
     * @param stmt
     *            Statement Object passed to be closed
     **/

    protected void closeStmt(Statement stmt) {

        try {

            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * closeResultSet
     * 
     * @param rs
     *            ResultSet Object passed to be closed
     **/

    protected void closeResultSet(ResultSet rs) {
        try {

            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *getCRS
     * 
     * @return CoordinateReferenceSystem
     **/

    protected CoordinateReferenceSystem getCRS() {

        LOGGER.fine("getCRS Method");

        CoordinateReferenceSystem crs = null;

        try {

            crs = CRS.decode(config.getCoordsys());
            LOGGER.fine("CRS get Identifier" + crs.getIdentifiers());

        } catch (Exception e) {
            LOGGER.severe("Cannot parse Decode CRS from Config File " + e.getMessage());
            throw new RuntimeException(e);
        } finally {

        }

        LOGGER.fine("Returning CRS Result");

        return crs;

    }

    /**
     * @return the Config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * @return LevelInofs
     */
    public List<ImageLevelInfo> getLevelInfos() {
        return levelInfos;
    }
    
}
