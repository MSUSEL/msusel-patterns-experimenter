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
package org.archive.util;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author stack
 * @version $Date: 2006-09-20 22:40:21 +0000 (Wed, 20 Sep 2006) $, $Revision: 4644 $
 */
public interface Reporter {
    /**
     * Get an array of report names offered by this Reporter. 
     * A name in brackets indicates a free-form String, 
     * in accordance with the informal description inside
     * the brackets, may yield a useful report.
     * 
     * @return String array of report names, empty if there is only
     * one report type
     */
    public String[] getReports();
    
    /**
     * Make a report of the given name to the passed-in Writer,
     * If null, give the default report. 
     * 
     * @param writer to receive report
     */
    public void reportTo(String name, PrintWriter writer);
    
    /**
     * Make a default report to the passed-in Writer. Should
     * be equivalent to reportTo(null, writer)
     * 
     * @param writer to receive report
     */
    public void reportTo(PrintWriter writer) throws IOException;
    
    /**
     * Make a single-line summary report to the passed-in writer
     * 
     * @param writer to receive report
     */
    public void singleLineReportTo(PrintWriter writer) throws IOException;
    
    /**
     * Return a short single-line summary report as a String.
     * 
     * @return String single-line summary report
     */
    public String singleLineReport();
    
    /**
     * Return a  legend for the single-line summary report as a String.
     * 
     * @return String single-line summary legend
     */
    public String singleLineLegend();
}
