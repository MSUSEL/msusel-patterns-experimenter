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
package org.geotools.data.wfs.internal.v1_x;

import java.util.Set;

import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.xml.Configuration;

/**
 * WFS strategy for GeoServer WFS 1.1 where the geoserver version is lower than 2.0.
 * 
 * <p>
 * <ul>
 * <li>State {@code text/gml; subtype=gml/3.1.1} instead of {@code text/xml; subtype=gml/3.1.1} as
 * default output format.
 * <li>Can only reliably parse Filter 1.0
 * </p>
 */
public class GeoServerPre200Strategy extends StrictWFS_1_x_Strategy {

    private static final String GEOSERVER_WRONG_FORMAT_NAME = "text/gml; subtype=gml/3.1.1";

    /**
     * GeoServer versions prior to 2.0 state {@code text/gml; subtype=gml/3.1.1} instead of
     * {@code text/xml; subtype=gml/3.1.1}
     */
    @Override
    public String getDefaultOutputFormat(WFSOperationType op) {
        try {
            return super.getDefaultOutputFormat(op);
        } catch (IllegalArgumentException e) {
            Set<String> supportedOutputFormats = getServerSupportedOutputFormats(op);
            if (supportedOutputFormats.contains(GEOSERVER_WRONG_FORMAT_NAME)) {
                return "text/xml; subtype=gml/3.1.1";
            }
            throw new IllegalArgumentException(
                    "Server does not support 'text/gml; subtype=gml/3.1.1' output format: "
                            + supportedOutputFormats);
        }
    }

    /**
     * GeoServer versions lower than 2.0 can only reliably parse Filter 1.0.
     * <p>
     * TODO: find a way to figure out whether the geoserver instance is actually that old
     * 
     * @see org.geotools.data.wfs.internal.v1_x.StrictWFS_1_x_Strategy#getFilterConfiguration()
     */
    @Override
    public Configuration getFilterConfiguration() {
        return FILTER_1_0_CONFIGURATION;
    }

}
