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
package org.geotools.data.ows;


/**
 * Available WMS Operations are listed in a Request element.
 *
 * @author rgould
 *
 *
 * @source $URL$
 */
public class WMSRequest {
    private OperationType getCapabilities;
    private OperationType getMap;
    private OperationType getFeatureInfo;
    private OperationType describeLayer;
    private OperationType getLegendGraphic;
    private OperationType getStyles;
    private OperationType putStyles;

    /**
     * DOCUMENT ME!
     *
     * @return Returns the getCapabilities.
     */
    public OperationType getGetCapabilities() {
        return getCapabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @param getCapabilities The getCapabilities to set.
     */
    public void setGetCapabilities(OperationType getCapabilities) {
        this.getCapabilities = getCapabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the getFeatureInfo.
     */
    public OperationType getGetFeatureInfo() {
        return getFeatureInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param getFeatureInfo The getFeatureInfo to set.
     */
    public void setGetFeatureInfo(OperationType getFeatureInfo) {
        this.getFeatureInfo = getFeatureInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the getMap.
     */
    public OperationType getGetMap() {
        return getMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param getMap The getMap to set.
     */
    public void setGetMap(OperationType getMap) {
        this.getMap = getMap;
    }
    public OperationType getDescribeLayer() {
        return describeLayer;
    }
    public void setDescribeLayer( OperationType describeLayer ) {
        this.describeLayer = describeLayer;
    }
    public OperationType getGetLegendGraphic() {
        return getLegendGraphic;
    }
    public void setGetLegendGraphic( OperationType getLegendGraphic ) {
        this.getLegendGraphic = getLegendGraphic;
    }
    public OperationType getGetStyles() {
        return getStyles;
    }
    public void setGetStyles( OperationType getStyles ) {
        this.getStyles = getStyles;
    }
    public OperationType getPutStyles() {
        return putStyles;
    }
    public void setPutStyles( OperationType putStyles ) {
        this.putStyles = putStyles;
    }
}
