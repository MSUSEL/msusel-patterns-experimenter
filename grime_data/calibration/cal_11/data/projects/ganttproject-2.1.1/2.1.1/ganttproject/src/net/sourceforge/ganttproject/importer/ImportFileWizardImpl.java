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
 * Created on 30.04.2005
 */
package net.sourceforge.ganttproject.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.Mediator;
import net.sourceforge.ganttproject.gui.GanttDialogInfo;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.UIFacade.Choice;
import net.sourceforge.ganttproject.gui.projectwizard.WizardImpl;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.plugins.PluginManager;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * @author bard
 */
public class ImportFileWizardImpl extends WizardImpl {
    private State myState;

    private GanttProject myProject;

    public ImportFileWizardImpl(UIFacade uiFacade, GanttProject project) {
        super(uiFacade, GanttLanguage.getInstance().getText("importWizard.dialog.title"));
        myProject = project;
        myState = new State();
        Importer[] importers = getImporters();
        addPage(new ImporterChooserPage(importers, myState));
        addPage(new FileChooserPage(myState));
    }

    private Importer[] getImporters() {
        List result = new ArrayList();
        Importer[] importers = (Importer[]) Mediator.getPluginManager().getExtensions(Importer.EXTENSION_POINT_ID, Importer.class);
//        String extensionPointID = 
//        IConfigurationElement[] configElements = Platform
//                .getExtensionRegistry().getConfigurationElementsFor(
//                        extensionPointID);
        return importers;
    }

    protected void onOkPressed() {
        super.onOkPressed();
        //myProject.getTree().addBlankLine(null, myProject.getTree().getRoot().getChildCount());
        boolean merge = mergeProjects();
        myState.myImporter.run(myProject, myProject.getUIFacade(), myState.getFile(), merge);
    }

    protected boolean canFinish() {
        return myState.myImporter != null && myState.getFile() != null;
    }

    private boolean mergeProjects() {
        if(myProject.getDocument() == null && !myProject.isModified()) {
            return false;
        }
    	String message = i18n("msg17");
    	String title = i18n("question");
    	Choice mergeChoice = getUIFacade().showConfirmationDialog(message, title);
    	if (mergeChoice == Choice.YES) {
    		return true;
    	}
        if (myProject.checkCurrentProject()) {
            myProject.close();
        }
        return false;
    }
    
    private String i18n(String key) {
		return GanttLanguage.getInstance().getText(key);
	}

	class State {
        Importer myImporter;

        private File myFile;

        void setFile(File myFile) {
            this.myFile = myFile;
            ImportFileWizardImpl.this.adjustButtonState();
        }

        File getFile() {
            return myFile;
        }
    }
}
