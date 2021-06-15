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
public class SeRegistration {
	
    public static  int SE_REGISTRATION_ROW_ID_ALLOCATION_SINGLE = 0;
    public static /* GEOT-947 final*/ int SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE = 0;
	public static /* GEOT-947 final*/ int SE_REGISTRATION_ROW_ID_COLUMN_TYPE_USER = 1;
	public static /* GEOT-947 final*/ int SE_REGISTRATION_ROW_ID_COLUMN_TYPE_NONE = 2;
    
	public SeRegistration(SeConnection c, String s) throws SeException{}
	
	public SeRegistration(SeConnection conn) {
    }

    public String getRowIdColumnName() { return null; }
	public void setRowIdColumnName(String s) {}
	public int getRowIdColumnType() throws SeException { return -1;}
	public void setRowIdColumnType(int i) {}
	public void alter() {}
	public String getTableName() { return null; }

    public boolean isMultiVersion() {return false;}

    public boolean isView() {return false;}

    public void setMultiVersion(boolean b) {}

    public void getInfo() throws SeException{}

    public void setTableName(String tableName) {}

    public int getRowIdAllocation() {
        return 0;
    }

    public boolean isHidden() {
        return false;
    }

    public boolean hasLayer() {
        return false;
    }

}
