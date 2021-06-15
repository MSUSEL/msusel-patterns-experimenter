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
package net.sourceforge.ganttproject.importer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.ganttproject.filter.ExtensionBasedFileFilter;
import net.sourceforge.ganttproject.gui.FileChooserPageBase;
import net.sourceforge.ganttproject.gui.TextFieldAndFileChooserComponent;
import net.sourceforge.ganttproject.gui.options.OptionsPageBuilder;
import net.sourceforge.ganttproject.gui.options.SpringUtilities;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.gui.projectwizard.WizardPage;
import net.sourceforge.ganttproject.importer.ImportFileWizardImpl.State;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
class FileChooserPage extends FileChooserPageBase {

    private State myState;

    public FileChooserPage(State state) {
        myState = state;
    }

    
    protected String getFileChooserTitle() {
        return GanttLanguage.getInstance().getText("importerFileChooserPageTitle");
    }


    public String getTitle() {
        return GanttLanguage.getInstance().getText("importerFileChooserPageTitle");
    }


    public void setActive(boolean b) {
        if (b == false) {
            super.setActive(b);
            myState.setFile(getSelectedFile());
        } else {
            if (myState.getFile() != null) {
                setSelectedFile(myState.getFile());
            }
            super.setActive(b);
        }
    }


	protected FileFilter createFileFilter() {
		return new ExtensionBasedFileFilter(
                myState.myImporter.getFileNamePattern(), myState.myImporter
                        .getFileTypeDescription());
	}
	
	protected GPOptionGroup[] getOptionGroups() {
		return myState.myImporter==null ? new GPOptionGroup[0] : myState.myImporter.getSecondaryOptions();
	}

	protected void onFileChosen(File file) {
		myState.setFile(file);
	}

}
