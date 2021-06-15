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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.jasperserver;

import java.io.File;

/**
 * This listener is used when a JRXML is replaced with the current JRXML.
 * The event is reised ONLY when the Replace with current file menu item is used.
 * This allows plugins like domains to change the JRXML or perform other
 * operations before actually save the file on the server.
 *
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 * @version $Id: FileResourceUpdatingListener.java $
 */
public interface FileResourceUpdatingListener {

    /**
     * This method is called before send the file and update the repository.
     * If the file belongs to a ReportUnit, the report unit is provided.
     *
     * @param repositoryFile - The resource that will be updated.
     * @param reportUnit - The report unit to which the resource belongs too. If null the resource does not belong to any report unit.
     * @param file - the file that is going to be updated
     * 
     */
    public void resourceWillBeUpdated(RepositoryFile repositoryFile, RepositoryReportUnit reportUnit, File file) throws Exception;

    /**
     * This method is called when the file has been sent and updated on the server.
     * 
     * @param repositoryFile - The updated resource
     * @param reportUnit - The report unit to which the resource belongs too. If null the resource does not belong to any report unit.
     * @param file - the file that is going to be updated
     */
    public void resourceUpdated(RepositoryFile rf, RepositoryReportUnit reportUnit, File file) throws Exception;

}
