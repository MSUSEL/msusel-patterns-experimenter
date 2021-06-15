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
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster.spi;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.gce.grassraster.GrassBinaryImageReader;
import org.geotools.gce.grassraster.GrassBinaryImageWriter;

/**
 * The Service Provider Interface for GRASS binary rasters.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see GrassBinaryImageReader
 * @see GrassBinaryImageWriter
 * @see GrassBinaryImageReaderSpi
 *
 *
 * @source $URL$
 */
@SuppressWarnings("nls")
public class GrassBinaryImageReaderSpi extends ImageReaderSpi {

    private static final String vendorName = "www.hydrologis.com";
    private static final String[] suffixes = {""};
    private static final String[] formatNames = {"grass", "GRASS", "grassbin",
            "GRASS binary raster"};
    private static final String[] MIMETypes = {"image/grass"};
    private static final String version = "1.0";

    /**
     * the class name of the image reader.
     */
    private static final String readerCN = "eu.hydrologis.jgrass.grassbinary.imageio.io.GrassBinaryImageReader";

    /**
     * the inputTypes that are accepted by the {@link GrassBinaryImageReader}.
     */
    private static final Class< ? >[] inputTypes = new Class[]{File.class, ImageInputStream.class};

    /**
     * the writerSpiName
     */
    private static final String[] wSN = {"eu.hydrologis.jgrass.grassbinary.imageio.io.GrassBinaryImageWriterSpi"};

    /**
     * the flag for stream metadata support.
     */
    private static final boolean supportsStandardStreamMetadataFormat = false;

    private static final String nativeStreamMetadataFormatName = null;
    private static final String nativeStreamMetadataFormatClassName = null;
    private static final String[] extraStreamMetadataFormatNames = null;
    private static final String[] extraStreamMetadataFormatClassNames = null;

    /**
     * the flag for image metadata support.
     */
    private static final boolean supportsStandardImageMetadataFormat = true;

    private static final String nativeImageMetadataFormatName = "eu.hydrologis.jgrass.grassbinary.imageio.metadata.GrassBinaryImageMetadata";
    private static final String nativeImageMetadataFormatClassName = "eu.hydrologis.jgrass.grassbinary.imageio.metadata.GrassBinaryImageMetadataFormat";
    private static final String[] extraImageMetadataFormatNames = {null};
    private static final String[] extraImageMetadataFormatClassNames = {null};

    /**
     * default constructor for the service provider interface.
     */
    public GrassBinaryImageReaderSpi() {
        super(vendorName, version, formatNames, suffixes, MIMETypes, readerCN, inputTypes, wSN,
                supportsStandardStreamMetadataFormat, nativeStreamMetadataFormatName,
                nativeStreamMetadataFormatClassName, extraStreamMetadataFormatNames,
                extraStreamMetadataFormatClassNames, supportsStandardImageMetadataFormat,
                nativeImageMetadataFormatName, nativeImageMetadataFormatClassName,
                extraImageMetadataFormatNames, extraImageMetadataFormatClassNames);
    }

    public String getDescription( Locale locale ) {
        return "GRASS binary raster image reader service provider interface, version " + version;
    }

    public boolean canDecodeInput( Object source ) throws IOException {
        if (source instanceof File) {
            return true;
        }
        return false;
    }

    public GrassBinaryImageReader createReaderInstance( Object extension ) throws IOException {
        return new GrassBinaryImageReader(this);
    }

}
