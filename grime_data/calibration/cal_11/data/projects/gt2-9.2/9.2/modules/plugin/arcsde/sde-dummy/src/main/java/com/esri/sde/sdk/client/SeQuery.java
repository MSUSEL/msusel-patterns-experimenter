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

import com.esri.sde.sdk.client.SeTable.SeTableStats;

/**
 * 
 *
 * @source $URL$
 */
public class SeQuery extends SeStreamOp {
	
	public static short SE_SPATIAL_FIRST = 0;

    public SeQuery(SeConnection c) throws SeException {}
	public SeQuery(SeConnection c, String[] s, SeSqlConstruct y) throws SeException {}
	
	public static /* GEOT-947 final*/ short SE_OPTIMIZE = 0;
	
	public void prepareQuery()throws SeException {}
	public void prepareQueryInfo(SeQueryInfo i)throws SeException {}
	public SeExtent calculateLayerExtent(SeQueryInfo i) { return null; }
	public void cancel(boolean b) {}
	public void setRowLocking(int i) {}
	public SeRow fetch() throws SeException{ return null; }
	public void setSpatialConstraints(short i, boolean b, SeFilter[] f)throws SeException {}
	public SeTableStats calculateTableStatistics(String s, int i, SeQueryInfo q, int j) { return null; }
	public void queryRasterTile(SeRasterConstraint c) throws SeException{}
	public void prepareSql(String s) {}

}
