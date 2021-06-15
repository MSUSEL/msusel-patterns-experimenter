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
 * Created on 03.05.2005
 */
package net.sourceforge.ganttproject.export;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.sourceforge.ganttproject.export.ExportFileWizardImpl.State;
import net.sourceforge.ganttproject.gui.options.GPOptionChoicePanel;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.gui.projectwizard.WizardPage;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
class ExporterChooserPage implements WizardPage {
    private Exporter[] myExporters;

    private State myState;
    private GanttLanguage language = GanttLanguage.getInstance(); 

    /**
     * 
     */
    ExporterChooserPage(Exporter[] exporters, ExportFileWizardImpl.State state) {
        myExporters = exporters;
        myState = state;
        
    }

    public String getTitle() {
        return language.getText("option.exporter.title");
    }

    public Component getComponent() {
        int selectedGroupIndex = 0;
        Action[] choiceChangeActions = new Action[myExporters.length];
        GPOptionGroup[] choiceOptions = new GPOptionGroup[myExporters.length];
        for (int i = 0; i < myExporters.length; i++) {
            final Exporter nextExporter = myExporters[i];
            if (nextExporter==myState.getExporter()) {
                selectedGroupIndex = i;
            }
            Action nextAction = new AbstractAction(nextExporter
                    .getFileTypeDescription()) {
                public void actionPerformed(ActionEvent e) {
                    ExporterChooserPage.this.myState.setExporter(nextExporter);
                }
            };
            GPOptionGroup nextOptions = nextExporter.getOptions();
            if (nextOptions!=null) {
                nextOptions.lock();
            }
            choiceChangeActions[i] = nextAction;
            choiceOptions[i] = nextOptions;
        }
        GPOptionChoicePanel choicePanel = new GPOptionChoicePanel();
        return choicePanel.getComponent(choiceChangeActions, choiceOptions, selectedGroupIndex);
    }

    public void setActive(boolean b) {
        if (false==b) {
            for (int i=0; i<myExporters.length; i++) {
                if (myExporters[i].getOptions()!=null) {
                    myExporters[i].getOptions().commit();
                }
            }
        }
        else {
            for (int i=0; i<myExporters.length; i++) {
                if (myExporters[i].getOptions()!=null) {
                    myExporters[i].getOptions().lock();
                }
            }
            
        }
    }

}
