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


import java.io.File;
import net.sf.jasperreports.engine.design.JRDesignSubreport;

/**
 *
 * @version $Id: SubreportOpenerProvider.java 0 2009-10-16 17:30:56 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public interface SubreportOpenerProvider {

    /**
     * This method is called by the OpenSubreportAction when the subreport is opened.
     * If subreportFile is null, this means that iReport or previous providers were not
     * able to find the file to open.
     * If a provider finds a file, it should return it. A provider can even return a different
     * file or just null (in this case nothing changes for iReport, meaning that the file
     * previously found is not replaced with null).
     * 
     * When all the providers have been queried, iReport will take care to open the file
     * if not null.
     *
     * SubreportOpenerProviders must be installed inside ireport/SubreportOpenerProviders in the jrxml
     * as class instance.
     *
     * 
     * @param ed
     * @param subreportElement
     * @param subreportFile
     * @return File the file to open, or null.
     */
    public File openingSubreport(JrxmlEditorSupport ed, JRDesignSubreport subreportElement, File subreportFile);

}
