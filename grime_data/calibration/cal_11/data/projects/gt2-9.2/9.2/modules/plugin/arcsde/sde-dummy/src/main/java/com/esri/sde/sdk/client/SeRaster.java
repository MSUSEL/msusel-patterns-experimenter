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
package com.esri.sde.sdk.client; 

/**
 * 
 *
 * @source $URL$
 */
public class SeRaster {
    
    public static int SE_RASTER_INTERLEAVE_NONE = 0;
    public static int SE_RASTER_INTERLEAVE_BSQ_91 = 0;
    public static int SE_RASTER_INTERLEAVE_BIP_91 = 0;
    public static int SE_RASTER_INTERLEAVE_BIP = 0;
    public static int SE_RASTER_INTERLEAVE_BIL_91 = 0;
    public static int SE_RASTER_INTERLEAVE_BIL = 0;
    public static int SE_COLORMAP_DATA_BYTE = 0;
    public static int SE_COLORMAP_DATA_SHORT = 0;
    public static int SE_COLORMAP_RGBA = 0;
    public static int SE_COLORMAP_RGB = 0;
    public static int SE_PIXEL_TYPE_64BIT_REAL = 0;
    public static int SE_PIXEL_TYPE_32BIT_S = 0;
    public static int SE_PIXEL_TYPE_32BIT_U = 0;
    public static int SE_PIXEL_TYPE_16BIT_S = 0;
    public static int SE_PIXEL_TYPE_16BIT_U = 0;
    public static int SE_PIXEL_TYPE_4BIT = 0;
    public static int SE_INTERPOLATION_BILINEAR = 3;
    public static int SE_INTERPOLATION_NONE = 2;
    public static int SE_INTERPOLATION_NEAREST = 1;
    public static int SE_INTERPOLATION_BICUBIC = 0;
    public static int SE_RASTER_INTERLEAVE_BSQ = 0;
    public static int SE_PIXEL_TYPE_8BIT_U = 1;
    public static int SE_PIXEL_TYPE_8BIT_S = 2;
    public static int SE_PIXEL_TYPE_1BIT = 3;
    public static int SE_PIXEL_TYPE_32BIT_REAL = 5;
    
    public static int SE_COMPRESSION_NONE = 100;
    public static int SE_COMPRESSION_LZ77 = 101;
    public static int SE_COMPRESSION_JPEG = 102;
    public static int SE_COMPRESSION_JP2 = 103;

    public SeRasterBand[] getBands() {
        return null;
    }

    public SeObjectId getRasterId() {
        return null;
    }

    public void getInfoById(SeObjectId rasterId) throws SeException{    
    }
    
    
}
