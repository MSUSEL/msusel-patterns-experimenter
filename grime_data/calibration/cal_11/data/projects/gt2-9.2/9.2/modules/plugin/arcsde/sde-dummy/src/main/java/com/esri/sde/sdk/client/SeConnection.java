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

import java.util.List;
import java.util.Vector;

/**
 * 
 *
 * @source $URL$
 */
public class SeConnection {

    public static int SE_TRYLOCK_POLICY = 0;
    public static /* GEOT-947 final*/ int SE_UNPROTECTED_POLICY = 0;
    public static int SE_ONE_THREAD_POLICY = 1;
	
	public SeConnection(String a, int i, String b, String c, String d) throws SeException{
	    throw new UnsupportedOperationException("this is the dummy api");
	}
	
	public SeConnection(String a, String i, String b, String c, String d) throws SeException{
	    throw new UnsupportedOperationException("this is the dummy api");
	}
	public String getDatabaseName() throws SeException { return null; }
	public String getUser() throws SeException { return null; }
	public java.util.Vector getLayers() throws SeException { return null; }
	public java.util.Vector getRasterColumns() throws SeException { return null; }
	public SeRelease getRelease() { return null; }
	public boolean isClosed() { return false; }
	
	public void close() throws SeException {}
	public void commitTransaction() throws SeException {}
	public void rollbackTransaction() throws SeException {}
	
	public void setConcurrency(int i)throws SeException {}
	public int setTransactionAutoCommit(int i) throws SeException { return -1;}
	public void startTransaction() throws SeException{}

    public SeDBMSInfo getDBMSInfo() throws SeException{
        return null;
    }

    public String getSdeDbaName() throws SeException{
        return null;
    }

    public long getTimeSinceLastRT() {
        return 0;
    }

    public void testServer(long testServerRoundtripIntervalSeconds) throws SeException{
    }

    public SeVersion[] getVersionList(String where) throws SeException{
        return null;
    }

    public Vector getTables(int se_select_privilege) throws SeException{
        return null;
    }

}
