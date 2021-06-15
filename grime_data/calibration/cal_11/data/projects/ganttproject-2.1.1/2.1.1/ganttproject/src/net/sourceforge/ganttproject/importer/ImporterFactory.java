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
package net.sourceforge.ganttproject.importer;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.ganttproject.GanttOptions;
import net.sourceforge.ganttproject.filter.ExtensionBasedFileFilter;

public abstract class ImporterFactory {
    public static Importer createImporter(FileFilter fileFilter) {
        if (fileFilter == txtFilter) {
            return new ImporterFromTxtFile();
        }
        if (fileFilter == ganFilter) {
            return new ImporterFromGanttFile();
        }
        // else if (fileFilter==plannerFilter) {
        // return new ImporterFromPlannerFile();
        // }
        return null;
    }

    public static JFileChooser createFileChooser(GanttOptions options) {
        JFileChooser fc = new JFileChooser(options.getWorkingDir());
        FileFilter[] filefilters = fc.getChoosableFileFilters();
        for (int i = 0; i < filefilters.length; i++) {
            fc.removeChoosableFileFilter(filefilters[i]);
        }
        fc.addChoosableFileFilter(ganFilter);
        fc.addChoosableFileFilter(mppFilter);
        fc.addChoosableFileFilter(txtFilter);
        // fc.addChoosableFileFilter(plannerFilter);

        return fc;

    }

    private static FileFilter txtFilter = new ExtensionBasedFileFilter("txt",
            "Text files (.txt)");

    // private static FileFilter mppFilter = new
    // ExtensionBasedFileFilter("mpp|mpx|xml", "MsProject files (.mpp, .mpx,
    // .xml)");
    private static FileFilter mppFilter = new ExtensionBasedFileFilter(
            "mpp|mpx|xml", "MsProject files (.mpp, .mpx, .xml)");

    private static FileFilter ganFilter = new ExtensionBasedFileFilter(
            "xml|gan", "GanttProject files (.gan, .xml)");
    // private static FileFilter plannerFilter = new
    // ExtensionBasedFileFilter("mrproject|planner", "Planner (MrProject) files
    // (.mrproject)");

}
