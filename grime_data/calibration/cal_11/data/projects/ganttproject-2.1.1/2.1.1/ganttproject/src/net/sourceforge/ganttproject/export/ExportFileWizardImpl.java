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
 * Created on 01.05.2005
 */
package net.sourceforge.ganttproject.export;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.SwingUtilities;

import net.sourceforge.ganttproject.GanttOptions;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.Mediator;
import net.sourceforge.ganttproject.chart.Chart;
import net.sourceforge.ganttproject.document.Document;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.options.model.BooleanOption;
import net.sourceforge.ganttproject.gui.options.model.DefaultBooleanOption;
import net.sourceforge.ganttproject.gui.options.model.GPOption;
import net.sourceforge.ganttproject.gui.projectwizard.WizardImpl;
import net.sourceforge.ganttproject.io.GanttCSVExport;
import net.sourceforge.ganttproject.io.GanttXFIGSaver;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
public class ExportFileWizardImpl extends WizardImpl {

    private UIFacade myUIFacade;

    private IGanttProject myProject;

    private Chart myGanttChart;

    private Chart myResourceChart;

    private Chart myVisibleChart;

    private GanttOptions myOptions;

    private State myState;

    private static Exporter ourLastSelectedExporter;
    private static Exporter[] ourExporters;

    public ExportFileWizardImpl(UIFacade uiFacade, IGanttProject project,
            GanttOptions options) {
        this(uiFacade, project, uiFacade.getGanttChart(),
                uiFacade.getResourceChart(), uiFacade.getActiveChart(), options);
    }

    public ExportFileWizardImpl(UIFacade uiFacade,
            IGanttProject project, Chart ganttChart, Chart resourceChart,
            Chart visibleChart, GanttOptions options) {
        super(uiFacade, GanttLanguage.getInstance().getText("exportWizard.dialog.title"));
        myUIFacade = uiFacade;
        myProject = project;
        myGanttChart = ganttChart;
        myResourceChart = resourceChart;
        myVisibleChart = visibleChart;
        myOptions = options;
        myState = new State(project.getDocument());
        if (ourExporters==null) {
            ourExporters = Mediator.getPluginManager().getExporters();
        }
        Exporter[] exporters = ourExporters;
        myState.setExporter(ourLastSelectedExporter==null ? exporters[0] : ourLastSelectedExporter);
        for (int i = 0; i < exporters.length; i++) {
            exporters[i].setContext(project, uiFacade);
            if (exporters[i] instanceof LegacyOptionsClient) {
            	((LegacyOptionsClient)exporters[i]).setOptions(myOptions);
            }
        }
        addPage(new ExporterChooserPage(exporters, myState));
        addPage(new FileChooserPage(myState, myProject));
    }

    protected boolean canFinish() {
        return myState.getExporter() != null && myState.myFile != null;
    }

    protected void onOkPressed() {
        super.onOkPressed();
        SwingUtilities.invokeLater(new Runnable(){
        	public void run() {
	            try {
	            	ExportFinalizationJob finalizationJob = new ExportFinalizationJobImpl();
	                myState.getExporter().run(myState.getFile(), finalizationJob);
	            } catch (Exception e) {
	                getUIFacade().showErrorDialog(e);
	            }
        	}
        });
    }

    private class ExportFinalizationJobImpl implements ExportFinalizationJob {
		public void run(File[] exportedFiles) {
            if (myState.getPublishInWebOption().isChecked() && exportedFiles.length>0) {
                WebPublisher publisher = new WebPublisher();
                publisher.run(exportedFiles, myProject.getDocumentManager().getFTPOptions(), getUIFacade());
            }
		}
    }
/*
    public void doExport() {
        try {
            doExport(myExportFileInfo);
        } catch (Exception e) {
            e.printStackTrace();
            getUIFacade().showErrorDialog(e);
        }
    }

    private void doExport(ExportFileInfo info) throws Exception {
        Exporter[] exporters = Mediator.getPluginManager().getExporters();
        for (int i=0; i<exporters.length; i++) {
            Exporter next = exporters[i];
            Collection extensions = Arrays.asList(next.getFileExtensions());
            if (extensions.contains(info.getFileExtension())) {
                next.setContext(myProject, myUIFacade);
                next.run(info.myFile);
                continue;
            }
        }
        switch (info.myFormat) {
        case ExportFileInfo.FORMAT_XFIG: {
            // show a message on the status bar
            setStatusText(getLanguage().getText("xfigexport"));
            String filename = info.myFile.toString();
            if (!filename.toUpperCase().endsWith(".FIG"))
                filename += ".fig";

            GanttXFIGSaver saver = new GanttXFIGSaver(myProject);
            saver.save(new FileOutputStream(new File(filename)));
            break;
        }

        case ExportFileInfo.FORMAT_CSV: {
            // show a message on the status bar
            setStatusText(getLanguage().getText("csvexport"));
            String filename = info.myFile.toString();
            if (!filename.toUpperCase().endsWith(".CSV"))
                filename += ".csv";

            GanttCSVExport saver = new GanttCSVExport(myProject, myOptions
                    .getCSVOptions());
            saver.save(new FileOutputStream(new File(filename)));
            break;
        }
        }
    }
*/
    private GanttLanguage getLanguage() {
        return GanttLanguage.getInstance();
    }

    private void setStatusText(String text) {
        getUIFacade().setStatusText(text);
    }

    class State {
        //final Document myProjectDocument;

        private Exporter myExporter;

        File myFile;

        private final BooleanOption myPublishInWebOption = new DefaultBooleanOption("exporter.publishInWeb");

        State(Document projectDocument) {
            //myProjectDocument = projectDocument;
        }

        public void setFile(File file) {
            myFile = file;
            ExportFileWizardImpl.this.adjustButtonState();
        }

        public File getFile() {
            return myFile;
        }

        void setExporter(Exporter exporter) {
            myExporter = exporter;
            ExportFileWizardImpl.ourLastSelectedExporter = exporter;
        }

        Exporter getExporter() {
            return myExporter;
        }

        BooleanOption getPublishInWebOption() {
            return myPublishInWebOption;
        }
    }

    interface LegacyOptionsClient {
    	void setOptions(GanttOptions options);
    }
}