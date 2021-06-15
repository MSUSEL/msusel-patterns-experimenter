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
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.gridshift;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.factory.AbstractFactory;
import org.geotools.factory.BufferedFactory;
import org.geotools.referencing.factory.ReferencingFactory;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;

import au.com.objectix.jgridshift.GridShiftFile;

/**
 * Loads and caches NTv2 grid files. Thisthat incorporates a soft cache mechanism to keep grids in
 * memory when first loaded. It also checks NTv2 grid file format in {@link #isNTv2Grid(String)}
 * method.
 * 
 * @author Oscar Fonts
 */
public class NTv2GridShiftFactory extends ReferencingFactory implements BufferedFactory {

    /**
     * The number of hard references to hold internally.
     */
    private static final int GRID_CACHE_HARD_REFERENCES = 10;

    /**
     * Logger.
     */
    protected static final Logger LOGGER = Logging.getLogger("org.geotools.referencing");

    /**
     * The soft cache that holds loaded grids.
     */
    private SoftValueHashMap<String, GridShiftFile> ntv2GridCache;

    /**
     * Constructs a factory with the default priority.
     */
    public NTv2GridShiftFactory() {
        super();
        ntv2GridCache = new SoftValueHashMap<String, GridShiftFile>(GRID_CACHE_HARD_REFERENCES);
    }

    /**
     * Constructs an instance using the specified priority level.
     * 
     * @param priority The priority for this factory, as a number between
     *        {@link AbstractFactory#MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *        {@link AbstractFactory#MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     */
    public NTv2GridShiftFactory(final int priority) {
        super(priority);
        ntv2GridCache = new SoftValueHashMap<String, GridShiftFile>(GRID_CACHE_HARD_REFERENCES);
    }

    /**
     * Performs a NTv2 grid file lookup given its name, and checks for file format correctness.
     * 
     * @param name The NTv2 grid file name
     * @return {@code true} if file exists and is valid, {@code false} otherwise
     */
    public boolean isNTv2Grid(URL location) {
        if (location != null) {
            return isNTv2GridFileValid(location); // Check
        } else {
            return false;
        }
    }

    /**
     * Creates a NTv2 Grid.
     * 
     * @param name The NTv2 grid name
     * @return the grid
     * @throws FactoryException if grid cannot be created
     */
    public GridShiftFile createNTv2Grid(URL gridLocation) throws FactoryException {
        if(gridLocation == null) {
            throw new FactoryException("The grid location must be not null");
        }
        
        synchronized (ntv2GridCache) { // Prevent simultaneous threads trying to load same grid
            GridShiftFile grid = ntv2GridCache.get(gridLocation.toExternalForm());
            if (grid != null) { // Cached:
                return grid; // - Return
            } else { // Not cached:
                if (gridLocation != null) {
                    grid = loadNTv2Grid(gridLocation); // - Load
                    if (grid != null) {
                        ntv2GridCache.put(gridLocation.toExternalForm(), grid); // - Cache
                        return grid; // - Return
                    }
                }
                throw new FactoryException("NTv2 Grid " + gridLocation + " could not be created.");
            }
        }
    }

    

    /**
     * Checks if a given resource is a valid NTv2 file without fully loading it.
     * 
     * If file is not valid, the cause is logged at {@link Level#WARNING warning level}.
     * 
     * @param location the NTv2 file absolute path
     * @return true if file has NTv2 format, false otherwise
     */
    protected boolean isNTv2GridFileValid(URL url) {
        RandomAccessFile raf = null;
        InputStream is = null;
        try {

            // Loading as RandomAccessFile doesn't load the full grid
            // in memory, but is a quick method to see if file format
            // is NTv2.
            if (url.getProtocol().equals("file")) {
                File file = DataUtilities.urlToFile(url);

                if (!file.exists() || !file.canRead()) {
                    throw new IOException(Errors.format(ErrorKeys.FILE_DOES_NOT_EXIST_$1, file));
                }

                raf = new RandomAccessFile(file, "r");

                // will throw an exception if not a valid file
                new GridShiftFile().loadGridShiftFile(raf);
            } else {
                InputStream in = new BufferedInputStream(url.openConnection().getInputStream()); 

                // will throw an exception if not a valid file
                new GridShiftFile().loadGridShiftFile(in, false);
            }

            return true; // No exception thrown => valid file.
        } catch (IllegalArgumentException e) {
            // This usually means resource is not a valid NTv2 file.
            // Let exception message describe the cause.
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return false;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return false;
        } finally {
            try {
                if (raf != null)
                    raf.close();
            } catch (IOException e) {
            }

            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Loads the grid in memory.
     * 
     * If file cannot be loaded, the cause is logged at {@link Level#SEVERE severe level}.
     * 
     * @param location the NTv2 file absolute path
     * @return the grid, or {@code null} on error
     * @throws FactoryException
     */
    private GridShiftFile loadNTv2Grid(URL location) throws FactoryException {
        InputStream in = null;
        try {
            GridShiftFile grid = new GridShiftFile();
            in = new BufferedInputStream(location.openStream());
            grid.loadGridShiftFile(in, false); // Load full grid in memory
            in.close();
            return grid;
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw new FactoryException(e.getLocalizedMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // never mind
            }
        }
    }

}
