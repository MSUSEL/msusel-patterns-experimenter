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
package org.ganttproject.impex.msproject;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.swing.Action;

import com.tapsterrock.mpx.MPXException;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.action.CancelAction;
import net.sourceforge.ganttproject.action.OkAction;
import net.sourceforge.ganttproject.document.Document;
import net.sourceforge.ganttproject.document.FileDocument;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.options.model.GPOption;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.importer.Importer;
import net.sourceforge.ganttproject.importer.ImporterBase;
import net.sourceforge.ganttproject.language.GanttLanguage;

public class ImporterFromMsProjectFile extends ImporterBase implements Importer {
    private LocaleOption myLanguageOption = new LocaleOption();
    
    private GPOptionGroup myMPXOptions = new GPOptionGroup("importer.msproject.mpx", new GPOption[] {myLanguageOption});
    
    public String getFileNamePattern() {
        return "mpp|mpx|xml";
    }

    public GPOptionGroup[] getSecondaryOptions() {
    	return new GPOptionGroup[] {myMPXOptions};
    }
    
    public String getFileTypeDescription() {
        return GanttLanguage.getInstance().getText("impex.msproject.description");
    }

    public void run(GanttProject project, UIFacade uiFacade, File selectedFile, boolean merge) {
        Document document = getDocument(selectedFile);
        openDocument(project, uiFacade, document);
    }

    protected Document getDocument(File selectedFile) {
        return new FileDocument(selectedFile);
    }

    GanttMPXJOpen open;

    protected void openDocument(final GanttProject project, final UIFacade uiFacade, Document document) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    getClass().getClassLoader());
            if (document.getPath().toLowerCase().endsWith(".mpp")) {
                open = new GanttMPPOpen(project.getTree(), project);
            }
            else if (document.getPath().toLowerCase().endsWith(".mpx")) {
                open = null;
                Locale importlocale = myLanguageOption.getSelectedLocale();
                open = new GanttMPXOpen(project.getTree(), project,
                        importlocale);
            } else if (document.getPath().toLowerCase().endsWith(".xml"))
                open = new GanttMSPDIOpen(project.getTree(), project);
            else
                open = null;

            open.load(document.getInputStream());

        } catch (IOException e) {
            uiFacade.showErrorDialog(e);
        } catch (MPXException e) {
        	uiFacade.showErrorDialog(e);
		}
        finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);        	
        }
    }

}
