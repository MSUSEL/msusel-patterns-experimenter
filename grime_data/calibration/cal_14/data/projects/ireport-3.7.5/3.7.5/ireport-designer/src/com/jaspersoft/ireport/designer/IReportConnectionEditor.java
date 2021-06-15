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
package com.jaspersoft.ireport.designer;

/**
 * A IReportConnectionEditor class provides a complete custom GUI for customizing a target IReportConnection.<br>
 * Each IReportConnectionEditor should inherit from the java.awt.Component class so it can be instantiated inside an AWT dialog or panel.<br>
 * Each IReportConnectionEditor should have a null constructor.<br>
 * 
 * @author gtoffoli
 */
public interface IReportConnectionEditor {
    
    /**
     * Set the IReportConnection to edit. Actually it is a copy of the original IReportConnection.
     * It can be modifed by the user interface.<br><br>
     * 
     * The copy of an IReportConnection is done instancing a new class of the same type and loading
     * the properties stored by the first object
     * @param c IReportConnection to edit
     */
    public void setIReportConnection(IReportConnection c);
    
    /**
     * This method is called when the user completes to edit the datasource or when a datasource test is required.
     * @return IReportConnection modified. IT can be the same instance get in input with setIReportConnection or a new one.
     */
    public IReportConnection getIReportConnection();
    
}
