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
 * Created on 02.05.2005
 */
package org.ganttproject.impex.msproject;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.export.ExportFinalizationJob;
import net.sourceforge.ganttproject.export.Exporter;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.options.model.DefaultEnumerationOption;
import net.sourceforge.ganttproject.gui.options.model.EnumerationOption;
import net.sourceforge.ganttproject.gui.options.model.GPOption;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
public class ExporterToMsProjectFile implements Exporter {

    private static final String[] FILE_FORMAT_IDS = new String[] {
            "impex.msproject.fileformat.mpx", 
            "impex.msproject.fileformat.mspdi" };

    private static final String[] FILE_EXTENSIONS = new String[] { "mpx","xml" };

    private String myFileFormat = FILE_FORMAT_IDS[0];

    private EnumerationOption myFileFormatOption = new DefaultEnumerationOption("impex.msproject.fileformat", FILE_FORMAT_IDS) {
        public void commit() {
            super.commit();
            ExporterToMsProjectFile.this.myFileFormat = getValue();
        }
    };

    private LocaleOption myLanguageOption = new LocaleOption();
    
    private GPOptionGroup myOptions = new GPOptionGroup("exporter.msproject",
            new GPOption[] { myFileFormatOption });

    private GPOptionGroup myMPXOptions = new GPOptionGroup("exporter.msproject.mpx", new GPOption[] {myLanguageOption});
    
    private IGanttProject myProject;

    private UIFacade myUIFacade;

    //private Locale myLocale;


    public ExporterToMsProjectFile() {
        myOptions.setTitled(false);
        myMPXOptions.setTitled(false);
        myFileFormatOption.lock();
        myFileFormatOption.setValue(FILE_FORMAT_IDS[0]);
        myFileFormatOption.commit();
    }


    public String getFileTypeDescription() {
        return GanttLanguage.getInstance().getText("impex.msproject.description");
    }

    public GPOptionGroup getOptions() {
        return myOptions;
    }

    public GPOptionGroup[] getSecondaryOptions() {
        return FILE_FORMAT_IDS[0].equals(myFileFormat) ? new GPOptionGroup[] {myMPXOptions} : null;
    }    
    
    public String getFileNamePattern() {
        return myFileFormat;
    }

    public void setContext(IGanttProject project, UIFacade uiFacade) {
        myProject = project;
        myUIFacade = uiFacade;
        myLanguageOption = new LocaleOption();
        myMPXOptions = new GPOptionGroup("exporter.msproject.mpx", new GPOption[] {myLanguageOption});
        myLanguageOption.lock();
        myLanguageOption.setSelectedLocale(GanttLanguage.getInstance().getLocale());
        myLanguageOption.commit();
    }
    public void run(final File outputFile, ExportFinalizationJob finalizationJob) throws Exception {
        ClassLoader contextClassLoader = Thread.currentThread()
                .getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    getClass().getClassLoader());
            myUIFacade.setStatusText("msproject-export");
            if (FILE_FORMAT_IDS[0].equals(myFileFormat)) {
                if (myLanguageOption.getSelectedLocale() != null) {
                    GanttMPXSaver saver = new GanttMPXSaver(myProject,
                            myLanguageOption.getSelectedLocale());
                    saver.save(outputFile);
                }
            }
            else if (FILE_FORMAT_IDS[1].equals(myFileFormat)) {
                GanttMSPDISaver saver = new GanttMSPDISaver(myProject);
                saver.save(outputFile);
            }
            finalizationJob.run(new File[] { outputFile });
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    public String proposeFileExtension() {
        return getSelectedFormatExtension();
    }

    private String getSelectedFormatExtension() {
        for (int i = 0; i < FILE_FORMAT_IDS.length; i++) {
            if (myFileFormat.equals(FILE_FORMAT_IDS[i])) {
                return FILE_EXTENSIONS[i];
            }
        }
        throw new IllegalStateException("Selected format=" + myFileFormat
                + " has not been found in known formats:"
                + Arrays.asList(FILE_FORMAT_IDS));

    }
    
    public String[] getFileExtensions() {
        
        return FILE_EXTENSIONS;
    }

}
