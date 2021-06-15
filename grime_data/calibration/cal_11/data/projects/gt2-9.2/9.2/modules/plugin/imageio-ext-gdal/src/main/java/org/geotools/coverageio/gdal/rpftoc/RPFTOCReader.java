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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.gdal.rpftoc;

import java.io.File;

import it.geosolutions.imageio.plugins.rpftoc.RPFTOCImageReaderSpi;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;


/**
 * This class can read a RPFTOC data source and create a {@link GridCoverage2D}
 * from the data.
 *
 * @author Daniele Romagnoli, GeoSolutions.
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 *
 *
 * @source $URL$
 */
public final class RPFTOCReader extends BaseGDALGridCoverage2DReader implements GridCoverageReader {
    private final static String worldFileExt = "";

    /**
     * Creates a new instance of a {@link RPFTOCReader}. I assume nothing about
     * file extension.
     *
     * @param input
     *            Source object for which we want to build an {@link RPFTOCReader}.
     * @throws DataSourceException
     */
    public RPFTOCReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link RPFTOCReader}. I assume nothing about
     * file extension.
     *
     * @param input
     *            Source object for which we want to build an {@link RPFTOCReader}.
     * @param hints
     *            Hints to be used by this reader throughout his life.
     * @throws DataSourceException
     */
    public RPFTOCReader(Object input, Hints hints) throws DataSourceException {
        super(input, hints, worldFileExt, new RPFTOCImageReaderSpi());
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
     */
    public Format getFormat() {
        return new RPFTOCFormat();
    }
    
    /**
     * Override coverage name because of the default structure of the folders. Very often XXX/RPF/A.TOC
     * The parent directory is a better alternative
     * @return the coverage name
     */
    public String getCoverageName() {
    	/* 
    	* Override coverage name because of the default structure of the folders. Very often XXX/RPF/A.TOC
        * The coverage name would be always A. The parent directory is a better alternative.
        */
    	String retVal = "RPFTOC Coverage";
    	if (!super.getCoverageName().equals("A")){
    		retVal = super.getCoverageName();
    	}
    	else
    	{
    		retVal = "RPFTOC Coverage";
    	    File oFile = super.getInputFile().getParentFile();
    		while (oFile != null)
    		{
    			String sName = oFile.getName();
    			if (!sName.toUpperCase().equals("RPF"))
    			{
    				retVal = sName;
    				break;
    			}
    			else
    			{
    				oFile = oFile.getParentFile();
    			}
    		} 
    	}
        return retVal;
    }
    
    
}
