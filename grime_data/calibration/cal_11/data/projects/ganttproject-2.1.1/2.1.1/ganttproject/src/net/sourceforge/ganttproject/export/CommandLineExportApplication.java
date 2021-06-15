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
package net.sourceforge.ganttproject.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Platform;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.Mediator;
import net.sourceforge.ganttproject.gui.UIFacade;

public class CommandLineExportApplication {
    private Map myFlag2exporter = new HashMap();
    
    public CommandLineExportApplication() {
        Exporter[] exporters = Mediator.getPluginManager().getExporters();
        for (int i=0; i<exporters.length; i++) {
            Exporter next = exporters[i];
            List nextExtensions = Arrays.asList(next.getFileExtensions());
            for (int j=0; j<nextExtensions.size(); j++) {
                myFlag2exporter.put("-" + nextExtensions.get(j), next);
            }
        }
        
    }
    public Collection getCommandLineFlags() {
        return myFlag2exporter.keySet();
    }
    public boolean export(Map parsedArgs) {
        if (parsedArgs.isEmpty()) {
            return false;
        }
        
        List values = new ArrayList(); 
        Exporter exporter = findExporter(parsedArgs, values);
        if (exporter!=null && values.size()>0) {
            GanttProject project = new GanttProject(false);
            String inputFileName = String.valueOf(values.get(0));
            
            project.openStartupDocument(inputFileName);
            ConsoleUIFacade consoleUI = new ConsoleUIFacade(project.getUIFacade());
            File inputFile = new File(inputFileName);
            if (false==inputFile.exists()) {
                consoleUI.showErrorDialog("File "+inputFileName+" does not exist.");
                return true;
            }
            if (false==inputFile.canRead()) {
                consoleUI.showErrorDialog("File "+inputFileName+" is not readable.");
                return true;                    
            }

            Platform.getJobManager().setProgressProvider(null);
            File outputFile;
            if (values.size()>1) {
                outputFile = new File(String.valueOf(values.get(1)));
            }
            else {
                outputFile = FileChooserPage.proposeOutputFile(project, exporter);
            }
            System.err.println("[CommandLineExportApplication] export(): exporting with "+exporter);
            exporter.setContext(project, consoleUI);
            if (exporter instanceof ExportFileWizardImpl.LegacyOptionsClient) {
                ((ExportFileWizardImpl.LegacyOptionsClient)exporter).setOptions(project.getOptions());
            }
            try {
            	ExportFinalizationJob finalizationJob = new ExportFinalizationJob() {
					public void run(File[] exportedFiles) {
						System.exit(0);
					}
            	};
                exporter.run(outputFile, finalizationJob);
            } catch (Exception e) {
                consoleUI.showErrorDialog(e);
            }
            return true;
        }
        return false;
    }
    
    private Exporter findExporter(Map args, List outputParams) {
        for (Iterator exporters = myFlag2exporter.entrySet().iterator();
             exporters.hasNext();) {
            Map.Entry nextEntry = (Entry) exporters.next();
            String flag = (String) nextEntry.getKey();
            if (args.containsKey(flag)) {
                outputParams.addAll((Collection) args.get(flag));
                return (Exporter) nextEntry.getValue();
            }
        }
        return null;
    }
}
