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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import java.util.List;

import org.xml.sax.SAXException;


/**
 * Holds the transaction response document.
 *
 * @author dzwiers
 *
 *
 *
 * @source $URL$
 */
public class TransactionResult {
    /**
     * no status
     */
    public static final int NO_STATUS = 0;
    /**
     * success
     */
    public static final int SUCCESS = 1;
    /**
     * failed
     */
    public static final int FAILED = 2;
    /**
     * partial
     */
    public static final int PARTIAL = 4;
    
    /**
     * A list of the fids returned in the TransactionResult in the order they were received.
     * The first element is the FID of the first InsertResults response.
     */
    private List<String> insertResult;
    private int status;
    private SAXException error;

    /**
     * 
     * @param status
     * @param insertResult
     * @param error
     */
    public TransactionResult(int status, List<String> insertResult,
        SAXException error) {
        this.status = status;
        this.insertResult = insertResult;
        this.error = error;
    }

    /**
     * 
     * @param status
     * @param insertResult
     * @param locator nullable
     * @param message
     */
    public TransactionResult(int status, List<String> insertResult,
        String locator, String message) {
        this.status = status;
        this.insertResult = insertResult;
        error = new SAXException(message + ":"
                + ((locator == null) ? "" : locator));
    }

    /**
     * 
     * @param s
     * @return one of the constant status'
     */
    public static int parseStatus(String s) {
        if ("SUCCESS".equalsIgnoreCase(s)) {
            return SUCCESS;
        }

        if ("FAILED".equalsIgnoreCase(s)) {
            return FAILED;
        }

        if ("PARTIAL".equalsIgnoreCase(s)) {
            return PARTIAL;
        }

        return NO_STATUS;
    }

    /**
     * Used to convert getStatus() into a string for display.
     * @param i
     * @return String representation of the constant value in i
     */
    public static String printStatus(int i) {
        switch (i) {
        case SUCCESS:
            return "SUCCESS";

        case FAILED:
            return "FAILED";

        case PARTIAL:
            return "PARTIAL";

        default:
            return "";
        }
    }

    /**
     * SAXException in the event the response could not be processed.
     *
     * @return Returns the error.
     */
    public SAXException getError() {
        return error;
    }

    /**
     * A list of the fids returned in the TransactionResult in the order they were received.  
     * The first element is the FID of the first InsertResults response.
     *
     * @return list of the fids returned in the TransactionResult in the order they were received.  
     */
    public List<String> getInsertResult() {
        return insertResult;
    }

    /**
     * Status of result (either FAILED, NO_STATUS, PARTIAL or SUCCESS).
     *
     * @return Returns the status.
     */
    public int getStatus() {
        return status;
    }
}
