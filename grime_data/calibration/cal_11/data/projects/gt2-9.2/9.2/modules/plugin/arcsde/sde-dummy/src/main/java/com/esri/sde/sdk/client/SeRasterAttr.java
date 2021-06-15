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

import java.awt.Dimension;
import java.awt.image.DataBufferByte;

/**
 * 
 *
 * @source $URL$
 */
public class SeRasterAttr {
	
	public SeRasterAttr(boolean b) {}

    public int getPixelType() throws SeException{ return -1; }
    public int getTileHeight() { return -1; }
    public int getTileWidth() { return -1; }
    public SeRasterBand[] getBands() throws SeException { return null; }
    public int getMaxLevel() { return -1; }
    public boolean skipLevelOne() { return false; }
    public SeExtent getExtentByLevel(int i) throws SeException { return null; }
    public int getImageWidthByLevel(int i) { return -1; }
    public int getImageHeightByLevel(int i) { return -1; }
    public SDEPoint getImageOffsetByLevel(int i) { return null; }
    public int getTilesPerRowByLevel(int i) { return -1; }
    public int getTilesPerColByLevel(int i) { return -1; }
    public int getNumBands() { return -1; }
    public SeRasterBand getBandInfo(int i) throws SeException { return null; }
    public SeObjectId getRasterColumnId() { return null; }
    public SeExtent getExtent() throws SeException { return null; }
    public SeRaster getRasterInfo() throws SeException, CloneNotSupportedException { return null; }
    
    public void setImageSize(int h, int w, int d) {}
    public void setTileSize(int w, int h) {}
    public void setPixelType(int p) {}
    public void setCompressionType(int c) {}
    public void setMaskMode(boolean b) {}
    public void setImportMode(boolean b) {}
    public void setExtent(SeExtent ext) {}
    public void setRasterProducer(SeRasterProducer p) {}

    public void setPyramidInfo(int maxLevel, boolean skipLevelOne, int interpolation) throws SeException{}

    public void setColorMap(int colorMapType, DataBufferByte dataBuffer)throws SeException {
    }

    public int getImageWidth() throws SeException {
        return 0;
    }

    public int getImageHeight() throws SeException {
        return 0;
    }

    public int getCompressionType() {
        return 0;
    }

    public SDEPoint getTileOrigin() {
        return null;
    }

    public int getInterpolation() {
        return 0;
    }

    public int getBandWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getBandHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getInterleave() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setInterleave(boolean b, int se_raster_interleave_bsq) {
        // TODO Auto-generated method stub
        
    }

    public SeObjectId getRasterId() throws SeException{
        // TODO Auto-generated method stub
        return null;
    }

	public SDEPoint getExtentOffsetByLevel(int level) throws SeException{
		// TODO Auto-generated method stub
		return null;
	}
    
}
