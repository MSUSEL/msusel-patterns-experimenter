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
public class SeState {

    public static int SE_STATE_DIFF_NOCHECK = 0;
    public static int SE_NULL_STATE_ID = 0;

    public SeState(SeConnection conn, SeObjectId stateId) throws SeException {
    }

    public SeState(SeConnection conn) throws SeException {
    }

    public boolean isOpen() {
        return false;
    }

    public void close() throws SeException {
    }

    public SeObjectId getId() {
        return null;
    }

    public void create(SeObjectId parentId) throws SeException {
    }

    public void trimTree(SeObjectId from, SeObjectId to) throws SeException {
    }

    public void delete() throws SeException{
    }

    public SeObjectId getParentId() {
        return null;
    }

    public void open()throws SeException {
    }

    public void freeLock()throws SeException {
    }

    public void lock()throws SeException {
    }

    public void merge(SeObjectId id, SeObjectId id2)throws SeException {
    }

    public String getOwner() {
        return null;
    }

}
