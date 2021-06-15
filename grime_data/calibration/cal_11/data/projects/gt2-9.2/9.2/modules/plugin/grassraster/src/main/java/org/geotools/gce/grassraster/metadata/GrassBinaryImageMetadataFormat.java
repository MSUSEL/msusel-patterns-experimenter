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
package org.geotools.gce.grassraster.metadata;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormat;
import javax.imageio.metadata.IIOMetadataFormatImpl;

import org.geotools.gce.grassraster.JGrassRegion;
import org.geotools.gce.grassraster.core.GrassBinaryRasterReadHandler;

/**
 * Defines the structure of metadata documents describing Grass raster image metadata.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see GrassBinaryImageMetadata
 * @see GrassBinaryRasterReadHandler
 * @see JGrassRegion
 *
 *
 * @source $URL$
 */
public final class GrassBinaryImageMetadataFormat extends IIOMetadataFormatImpl {

    /**
     * The instance of {@linkplain GrassBinaryImageMetadataFormat}.
     */
    private static IIOMetadataFormat instance = null;

    /**
     * Default constructor.
     */
    protected GrassBinaryImageMetadataFormat() {
        super(GrassBinaryImageMetadata.nativeMetadataFormatName,
                IIOMetadataFormatImpl.CHILD_POLICY_ALL);

        // root -> EnvelopeDescriptor
        addElement(GrassBinaryImageMetadata.REGION_DESCRIPTOR,
                GrassBinaryImageMetadata.nativeMetadataFormatName, CHILD_POLICY_EMPTY);
        addAttribute(GrassBinaryImageMetadata.REGION_DESCRIPTOR, GrassBinaryImageMetadata.NORTH,
                DATATYPE_DOUBLE, true, null);
        addAttribute(GrassBinaryImageMetadata.REGION_DESCRIPTOR, GrassBinaryImageMetadata.SOUTH,
                DATATYPE_DOUBLE, true, null);
        addAttribute(GrassBinaryImageMetadata.REGION_DESCRIPTOR, GrassBinaryImageMetadata.EAST,
                DATATYPE_DOUBLE, true, null);
        addAttribute(GrassBinaryImageMetadata.REGION_DESCRIPTOR, GrassBinaryImageMetadata.WEST,
                DATATYPE_DOUBLE, true, null);
        addAttribute(GrassBinaryImageMetadata.REGION_DESCRIPTOR, GrassBinaryImageMetadata.XRES,
                DATATYPE_DOUBLE, true, null);
        addAttribute(GrassBinaryImageMetadata.REGION_DESCRIPTOR, GrassBinaryImageMetadata.YRES,
                DATATYPE_DOUBLE, true, null);
        addAttribute(GrassBinaryImageMetadata.REGION_DESCRIPTOR, GrassBinaryImageMetadata.NO_DATA,
                DATATYPE_DOUBLE, false, null);
        addAttribute(GrassBinaryImageMetadata.REGION_DESCRIPTOR, GrassBinaryImageMetadata.NROWS,
                DATATYPE_INTEGER, true, null);
        addAttribute(GrassBinaryImageMetadata.REGION_DESCRIPTOR, GrassBinaryImageMetadata.NCOLS,
                DATATYPE_INTEGER, true, null);

        // root-> ColorrulesDescriptor
        addElement(GrassBinaryImageMetadata.COLOR_RULES_DESCRIPTOR,
                GrassBinaryImageMetadata.nativeMetadataFormatName, CHILD_POLICY_EMPTY);
        addAttribute(GrassBinaryImageMetadata.COLOR_RULES_DESCRIPTOR,
                GrassBinaryImageMetadata.COLOR_RULES_DESCRIPTOR, DATATYPE_STRING, false, null);

        // root-> CategoriesDescriptor
        addElement(GrassBinaryImageMetadata.CATEGORIES_DESCRIPTOR,
                GrassBinaryImageMetadata.nativeMetadataFormatName, CHILD_POLICY_EMPTY);
        addAttribute(GrassBinaryImageMetadata.CATEGORIES_DESCRIPTOR,
                GrassBinaryImageMetadata.CATEGORIES_DESCRIPTOR, DATATYPE_STRING, false, null);

    }

    /**
     * Returns an instance of the {@link GrassBinaryImageMetadataFormat} class.
     * <p>
     * One instance is enough, therefore it is built as a singleton.
     * </p>
     * 
     * @return an instance of the {@link GrassBinaryImageMetadataFormat} class.
     */
    public static synchronized IIOMetadataFormat getInstance() {
        if (instance == null)
            instance = new GrassBinaryImageMetadataFormat();
        return instance;
    }

    public boolean canNodeAppear( String elementName, ImageTypeSpecifier imageType ) {
        return true;
    }
}
