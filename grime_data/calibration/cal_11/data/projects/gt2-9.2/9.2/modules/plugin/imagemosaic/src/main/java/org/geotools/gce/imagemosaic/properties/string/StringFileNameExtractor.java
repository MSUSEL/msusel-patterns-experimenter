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
package org.geotools.gce.imagemosaic.properties.string;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.RegExPropertiesCollector;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
/**
 * {@link PropertiesCollector} that is able to collect properties from a file name.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
class StringFileNameExtractor extends RegExPropertiesCollector {
    private final static Logger LOGGER = Logging.getLogger(StringFileNameExtractor.class);

    public StringFileNameExtractor(PropertiesCollectorSPI spi, List<String> propertyNames,
            String regex) {
        super(spi, propertyNames, regex);

    }

    @Override
    public void setProperties(SimpleFeature feature) {

        // get all the matches and convert them in times
        List<String> matches = getMatches();

        // set the properties, only if we have matches!
        if (matches.size() <= 0) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("No matches found for this property extractor:");
        }
        int index = 0;
        for (String propertyName : getPropertyNames()) {
            // set the property
            feature.setAttribute(propertyName, matches.get(index++));

            // do we have more dates?
            if (index >= matches.size())
                return;
        }
    }

}
