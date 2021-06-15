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
/***************************************************************************
 CSVOptions.java 
 ------------------------------------------
 begin                : 7 juil. 2004
 copyright            : (C) 2004 by Thomas Alexandre
 email                : alexthomas@ganttproject.org
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package net.sourceforge.ganttproject.io;

/**
 * @author athomas Settings for exporting in csv format
 */
public class CSVOptions {
    public boolean bExportTaskID = true;

    public boolean bExportTaskName = true;

    public boolean bExportTaskStartDate = true;

    public boolean bExportTaskEndDate = true;

    public boolean bExportTaskPercent = true;

    public boolean bExportTaskDuration = true;

    public boolean bExportTaskWebLink = true;

    public boolean bExportTaskResources = true;

    public boolean bExportTaskNotes = true;

    public boolean bExportResourceID = true;

    public boolean bExportResourceName = true;

    public boolean bExportResourceMail = true;

    public boolean bExportResourcePhone = true;

    public boolean bExportResourceRole = true;

    public boolean bFixedSize = false;

    public String sSeparatedChar = ",";

    public String sSeparatedTextChar = "\"";

    /** @return the csv settings as an xml schema. */
    public String getXml() {
        String res = "    <csv-export>\n";
        // general options
        res += "      <csv-general \n";
        res += "        fixed=\"" + bFixedSize + "\"\n";
        res += "        separatedChar=\"" + correct(sSeparatedChar) + "\"\n";
        res += "        separatedTextChar=\"" + correct(sSeparatedTextChar)
                + "\"/>\n";

        // tasks export options
        res += "      <csv-tasks\n";
        res += "        id=\"" + bExportTaskID + "\"\n";
        res += "        name=\"" + bExportTaskName + "\"\n";
        res += "        start-date=\"" + bExportTaskStartDate + "\"\n";
        res += "        end-date=\"" + bExportTaskEndDate + "\"\n";
        res += "        percent=\"" + bExportTaskPercent + "\"\n";
        res += "        duration=\"" + bExportTaskDuration + "\"\n";
        res += "        webLink=\"" + bExportTaskWebLink + "\"\n";
        res += "        resources=\"" + bExportTaskResources + "\"\n";
        res += "        notes=\"" + bExportTaskNotes + "\"/>\n";

        // resources export options
        res += "      <csv-resources\n";
        res += "        id=\"" + bExportResourceID + "\"\n";
        res += "        name=\"" + bExportResourceName + "\"\n";
        res += "        mail=\"" + bExportResourceMail + "\"\n";
        res += "        phone=\"" + bExportResourcePhone + "\"\n";
        res += "        role=\"" + bExportResourceRole + "\"/>\n";

        return res += "    </csv-export>\n";
    }

    public String correct(String s) {
        String res;
        res = s.replaceAll("&", "&#38;");
        res = res.replaceAll("<", "&#60;");
        res = res.replaceAll(">", "&#62;");
        res = res.replaceAll("/", "&#47;");
        res = res.replaceAll("\"", "&#34;");
        return res;
    }

    /** @return a list of the possible separated char. */
    public String[] getSeparatedTextChars() {
        String[] charText = { "   \'   ", "   \"   " };
        return charText;
    }
}
