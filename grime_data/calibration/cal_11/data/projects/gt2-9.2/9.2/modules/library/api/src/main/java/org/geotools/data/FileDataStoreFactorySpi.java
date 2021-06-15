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
package org.geotools.data;

import java.io.IOException;
import java.net.URL;


/**
 * DataAccessFactory for working with formats based on a single URL.
 * <p>
 * This interface provides a mechanism of discovery for DataAccessFactories
 * which support singular files.
 * </p>
 *
 * @author dzwiers
 *
 *
 * @source $URL$
 */
public interface FileDataStoreFactorySpi extends DataStoreFactorySpi {
    /**
     * The list of filename extentions handled by this factory.
     *
     * @return List of file extensions which can be read by this
     *         dataStore.
     */
    public String[] getFileExtensions();

    /**
     * Tests if the provided url can be handled by this factory.
     *
     * @param url URL to a real file (may not be local)
     *
     * @return <code>true</code> if this url can when this dataStore can resolve and read the data specified
     */
    public boolean canProcess(URL url);

    /**
     * A DataStore attached to the provided url, may be created if needed.
     * <p>
     * Please note that additional configuration options may be available
     * via the traditional createDataStore( Map ) method provided by the
     * superclass.
     * <p>
     * @param url The data location for the
     *
     * @return Returns an AbstractFileDataStore created from the data source
     *         provided.
     *
     * @throws IOException
     *
     * @see AbstractFileDataStore
     */
    public FileDataStore createDataStore(URL url) throws IOException;

    /**
     * The typeName represented by the provided url.
     *
     * @param url The location of the datum to parse into features
     *
     * @return Returns the typename of the datum specified (on occasion this
     *         may involve starting the parse as well to get the FeatureType
     *         -- may not be instantanious).
     *
     * @throws IOException
     */
    public String getTypeName(URL url) throws IOException;
}
