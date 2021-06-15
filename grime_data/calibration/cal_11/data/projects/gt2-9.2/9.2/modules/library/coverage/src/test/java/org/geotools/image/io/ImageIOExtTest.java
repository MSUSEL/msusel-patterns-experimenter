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
 *    (C) 2001-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

import static org.junit.Assert.*;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileCacheImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.operator.ConstantDescriptor;

import org.geotools.image.io.ImageIOExt;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public class ImageIOExtTest {

    private boolean useCache;

    @Before
    public void before() {
        useCache = ImageIO.getUseCache();
    }

    @After
    public void after() {
        ImageIO.setUseCache(useCache);
    }

    @Test
    public void testDefaultMemoryOutputStreams() throws Exception {
        ImageIO.setUseCache(false);
        testSameStreamClass();
    }
    
    @Test
    public void testDefaultFileOutputStreams() throws Exception {
        ImageIO.setUseCache(true);
        testSameStreamClass();
    }
    
    @Test 
    public void testThreshold() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        ImageIOExt.setFilesystemThreshold(100 * 100 * 3l);
        
        RenderedImage imageSmall = getTestRenderedImage(50, 50, 3);
        final ImageOutputStream iosSmall = ImageIOExt.createImageOutputStream(imageSmall, os);
        try {
            assertEquals(MemoryCacheImageOutputStream.class, iosSmall.getClass());
        } finally {
            iosSmall.close();
        }
        
        RenderedImage imageLarge = getTestRenderedImage(101, 101, 3);
        final ImageOutputStream iosLarge = ImageIOExt.createImageOutputStream(imageLarge, os);
        try {
            assertEquals(FileCacheImageOutputStream.class, iosLarge.getClass());
        } finally {
            iosLarge.close();
        }
    }

    void testSameStreamClass() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        RenderedImage image = getTestRenderedImage(50, 50, 1);

        ImageOutputStream iosExt = ImageIOExt.createImageOutputStream(image, os);
        ImageOutputStream iosStd = ImageIO.createImageOutputStream(os);

        assertEquals(iosExt.getClass(), iosStd.getClass());
    }

    RenderedImage getTestRenderedImage(int width, int height, int bands) {
        Byte[] values = new Byte[bands];
        for (int i = 0; i < values.length; i++) {
            values[i] = new Byte((byte) 0);
        }
        return ConstantDescriptor.create((float) width, (float) height, values, null);
    }
}
