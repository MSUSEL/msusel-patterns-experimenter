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
package org.geotools.gce.imagecollection;

import javax.imageio.spi.ImageReaderSpi;

import org.geotools.geometry.GeneralEnvelope;

/**
 * A simple property container, which store basic image properties such as
 * width, height, absolutePath of the image, ...
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 * 
 *
 *
 *
 * @source $URL$
 */
public class ImageProperty {
    private int width;

    private int height;

    private int numOverviews;

    private String path;
    
    private boolean isGeoSpatial;
    
    public boolean isGeoSpatial() {
        return isGeoSpatial;
    }

    public void setGeoSpatial(boolean isGeoSpatial) {
        this.isGeoSpatial = isGeoSpatial;
    }

    public GeneralEnvelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(GeneralEnvelope envelope) {
        this.envelope = envelope;
    }

    private GeneralEnvelope envelope;

    /** 
     * In case the file has been modified, we need to update the main parameters. This flag
     * takes note of the last Modified time value of the underlying file.
     */
    public long lastModifiedTime;

    /** 
     * the last time at which the file has been checked.
     */
    public long lastCheckTime;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getNumOverviews() {
        return numOverviews;
    }

    public void setNumOverviews(int numOverviews) {
        this.numOverviews = numOverviews;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLastModified() {
        return lastModifiedTime;
    }

    public void setLastModified(long lastModified) {
        this.lastModifiedTime = lastModified;
    }

    public long getLastCheck() {
        return lastCheckTime;
    }

    public void setLastCheck(long lastCheck) {
        this.lastCheckTime = lastCheck;
    }

    public ImageReaderSpi getSpi() {
        return spi;
    }

    public void setSpi(ImageReaderSpi spi) {
        this.spi = spi;
    }

    public ImageReaderSpi spi;

    public ImageProperty() {
        path = Utils.FAKE_IMAGE_PATH;
        width = 65536;//Integer.MAX_VALUE;
        height = 65536;//Integer.MAX_VALUE;
    }

    /**
     * 
     * @param path
     * @param width
     * @param height
     * @param numOverviews
     * @param spi
     * @param lastModifiedTime
     */
    public ImageProperty(final String path, final int width, final int height, 
            final int numOverviews, final ImageReaderSpi spi, final long lastModifiedTime) {
        this.width = width;
        this.height = height;
        this.numOverviews = numOverviews;
        this.path = path;
        this.spi = spi;
        this.lastModifiedTime = lastModifiedTime;
        this.lastCheckTime = System.currentTimeMillis();
    }
}
