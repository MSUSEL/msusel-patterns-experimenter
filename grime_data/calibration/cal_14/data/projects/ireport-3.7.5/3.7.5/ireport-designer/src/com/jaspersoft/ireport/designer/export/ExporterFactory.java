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
package com.jaspersoft.ireport.designer.export;

import net.sf.jasperreports.engine.JRExporter;

/**
 *
 * @author gtoffoli
 */
public interface ExporterFactory {

    /**
     * Return a code that will be set as preview type
     * in order to use this exporter (i.e. myTxtFormat)
     * @return
     */
    public String getExportFormat();

    /**
     * Return the name that should be appear in the Preview
     * menu when specifying this format. I.e. (John Smith's custom text)
     * @return
     */
    public String getExportFormatDisplayName();

    /**
     * The extension of the file that will be used to replace the .jasper
     * extension in the original file (i.e. txt)
     * @return
     */
    public String getExporterFileExtension();
    
    /**
     * Return the name of an application to be executed to view this file
     * The command should be executed with the file name as first parameter.
     * If the return is null or an empty string, the internal preview will be used.
     * @return
     */
    public String getViewer();

    /**
     * This function creates and configures the exporter. Extra parameters
     * can be set or replaced by the IReportCompiler class. In particular this
     * class will set these parameters:
     * JRExporterParameter.OUTPUT_FILE_NAME
     * JRExporterParameter.JASPER_PRINT
     * JRExporterParameter.PROGRESS_MONITOR
     * JRExporterParameter.IGNORE_PAGE_MARGINS
     * JRExporterParameter.PAGE_INDEX
     * JRExporterParameter.START_PAGE_INDEX
     * JRExporterParameter.END_PAGE_INDEX
     * JRExporterParameter.PROPERTY_CHARACTER_ENCODING
     * JRExporterParameter.CHARACTER_ENCODING
     * JRExporterParameter.OFFSET_X
     * JRExporterParameter.OFFSET_Y
     *
     * if propertly requested by the user with the general export options panel.
     *
     * @return
     */
    public JRExporter createExporter();

}
