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

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;

/**
 * 
 *
 * @source $URL$
 */
public class SeRasterBand {
    
    public SeObjectId getId() { return null; }
    public boolean hasColorMap() { return false; }
    
    public SeRasterBandColorMap getColorMap() { return null; }
    
    public class SeRasterBandColorMap {
        
    }

    public void setColorMap(int colorMapType, DataBufferByte dataBuffer) {

    }
    public void alter() throws SeException{
    }
    public DataBuffer getColorMapData() throws SeException{
        return null;
    }
    public int getColorMapType() throws SeException{
        return 0;
    }
    public int getColorMapNumBanks() throws SeException{
        return 0;
    }
    public int getColorMapDataType() throws SeException{
        return 0;
    }
    public int getColorMapNumEntries() throws SeException{
        return 0;
    }
    public int getBandNumber() {
        return 0;
    }
    public boolean hasStats() {
        return false;
    }
    public double getStatsMin() throws SeException{
        return 0;
    }
    public double getStatsMax() throws SeException{
        return 0;
    }
    public String getBandName() {
        // TODO Auto-generated method stub
        return null;
    }
    public SeObjectId getRasterId() {
        // TODO Auto-generated method stub
        return null;
    }
    public SeObjectId getRasterColumnId() {
        // TODO Auto-generated method stub
        return null;
    }
    public int getBandHeight() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getBandWidth() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getCompressionType() {
        // TODO Auto-generated method stub
        return 0;
    }
    public SeExtent getExtent() {
        // TODO Auto-generated method stub
        return null;
    }
    public int getPixelType() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getInterleave() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getInterpolation() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getMaxLevel() {
        // TODO Auto-generated method stub
        return 0;
    }
    public boolean skipLevelOne() {
        // TODO Auto-generated method stub
        return false;
    }
    public int getTileWidth() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getTileHeight() {
        // TODO Auto-generated method stub
        return 0;
    }
    public SDEPoint getTileOrigin() throws SeException{
        // TODO Auto-generated method stub
        return null;
    }
    public double getStatsMean() {
        // TODO Auto-generated method stub
        return 0;
    }
    public double getStatsStdDev() {
        // TODO Auto-generated method stub
        return 0;
    }
    public void setColorMap(int colorMapType, DataBuffer dataBuffer) {
        // TODO Auto-generated method stub
        
    }

}
