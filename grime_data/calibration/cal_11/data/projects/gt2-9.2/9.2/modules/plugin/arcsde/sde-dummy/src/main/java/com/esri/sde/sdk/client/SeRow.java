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

import java.io.ByteArrayInputStream;
import java.util.Calendar;

import com.esri.sde.sdk.geom.GeometryFactory;

/**
 * 
 *
 * @source $URL$
 */
public class SeRow {
	
	public static /* GEOT-947 final*/ int SE_IS_NULL_VALUE = 0;
	public static /* GEOT-947 final*/ int SE_IS_REPEATED_FEATURE = 1;
	public static /* GEOT-947 final*/ int SE_IS_ALREADY_FETCHED = 2;
	public static /* GEOT-947 final*/ int SE_IS_NOT_NULL_VALUE = 3;
	
	public void reset() throws SeException{};
	public SeColumnDefinition[] getColumns() { return null; }
	public Object getObject(int i) throws SeException { return null; }
	public SeColumnDefinition getColumnDef(int i) throws SeException{ return null; }
	public void setInteger(int i, Integer b) {}
	public void setShort(int i, Short s) {}
	public void setFloat(int i, Float f) {}
	public void setDouble(int i, Double d) {}
	public void setString(int i, String s) {}
	public void setTime(int i, Calendar c) {}
	public void setShape(int i, SeShape s) {}
	public void setRaster(int i, SeRasterAttr a) {}
	public SeRasterAttr getRaster(int i) { return null; }
	public SeRasterTile getRasterTile() throws SeException { return null; }
	public short getNumColumns() { return -1; }
	public int getIndicator(int i) { return -1; }
    public Integer getInteger(int i) throws SeException {return null;}
    public void setLong(int index, Long value) {}
    public SeShape getShape(int i) {return null;}
    public void setNString(int index, String convertedValue) {}
    public Object getGeometry(GeometryFactory seGeomFac, int i)throws SeException {
        return null;
    }
    public ByteArrayInputStream getBlob(int i) throws SeException{
        // TODO Auto-generated method stub
        return null;
    }
    public String getString(int i) throws SeException{
        // TODO Auto-generated method stub
        return null;
    }
    public ByteArrayInputStream getClob(int i) {return null;}
    public void setClob(int i, ByteArrayInputStream clobVal) {}
    public ByteArrayInputStream getNClob(int i) {return null;}
    public void setNClob(int i, ByteArrayInputStream clobVal) {}
}
