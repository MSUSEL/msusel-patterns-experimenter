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
package com.jaspersoft.ireport.designer.connection;

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;

/**
 *
 * @author gtoffoli
 */
public class DefaultIReportConnectionFactory implements IReportConnectionFactory {

    private String connectionClassName = null;

    public DefaultIReportConnectionFactory(String connectionClassName)
    {
        this.connectionClassName = connectionClassName;
    }


    public IReportConnection createConnection() {

        try {
            IReportConnection c = (IReportConnection)Class.forName(connectionClassName,true, IReportManager.getReportClassLoader()).newInstance();
            return c;
        } catch (Throwable tw)
        {
            tw.printStackTrace();
        }
        return null;
    }

    /**
     * @return the connectionClassName
     */
    public String getConnectionClassName() {
        return connectionClassName;
    }

    /**
     * @param connectionClassName the connectionClassName to set
     */
    public void setConnectionClassName(String connectionClassName) {
        this.connectionClassName = connectionClassName;
    }



}
