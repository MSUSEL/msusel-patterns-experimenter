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
public class SeTable {
	
	public SeTable(SeConnection s, String y) throws SeException {}
	
	public String getQualifiedName() { return null; }
	public String getName() { return null; }
	public void addColumn(SeColumnDefinition s) {}
	public void dropColumn(String s) {}
	public void delete() throws SeException {}
	public void create(SeColumnDefinition[] c, String s) {}
	public SeColumnDefinition[] describe() throws SeException { return null; }
	public void truncate()throws SeException{};
	public static class SeTableStats {
		public static int SE_ALL_STATS = 0;
        public static /* GEOT-947 final*/ int SE_COUNT_STATS = 0;
		public int getCount() { return 0; }
        public double getMin() {
            return 0;
        }
        public double getMax() {
            return 0;
        }
	}
    public int getPermissions() throws SeException{return 0;}
	
    public static class SeTableIdRange{
        public SeObjectId getStartId() {return null;}
    }
    public SeTableIdRange getIds(int i)throws SeException {return null;}
}
