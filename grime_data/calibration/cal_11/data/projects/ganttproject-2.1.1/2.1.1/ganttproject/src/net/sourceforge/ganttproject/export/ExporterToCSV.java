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
 * Created on 17.12.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.ganttproject.export;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;

import net.sourceforge.ganttproject.GanttOptions;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.io.GanttCSVExport;
import net.sourceforge.ganttproject.language.GanttLanguage;

public class ExporterToCSV implements Exporter, ExportFileWizardImpl.LegacyOptionsClient {

	private static String[] FILE_EXTENSIONS = new String[] {"csv"};
	private IGanttProject myProject;
	private GanttOptions myOptions;
	
	public String getFileTypeDescription() {
        return GanttLanguage.getInstance().getText("impex.csv.description");
	}

	public GPOptionGroup getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	public GPOptionGroup[] getSecondaryOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFileNamePattern() {
		return FILE_EXTENSIONS[0];
	}

	public void run(File outputFile, ExportFinalizationJob finalizationJob) throws Exception {
		// TODO Auto-generated method stub
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		GanttCSVExport legacyExporter = new GanttCSVExport(myProject, myOptions.getCSVOptions());
		legacyExporter.save(new FileOutputStream(outputFile));
        finalizationJob.run(new File[] {outputFile});
	}

	public String proposeFileExtension() {
		return FILE_EXTENSIONS[0];
	}

	public String[] getFileExtensions() {
		return FILE_EXTENSIONS;
	}

	public void setContext(IGanttProject project, UIFacade uiFacade) {
		myProject = project;
	}

	public void setOptions(GanttOptions options) {
		myOptions = options;
	}

}
