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
 * Created on 02.04.2005
 */
package net.sourceforge.ganttproject.chart;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sourceforge.ganttproject.action.CancelAction;
import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.action.OkAction;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.options.OptionsPageBuilder;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
public class OptionsDialogAction extends GPAction {
    private UIFacade myUIFacade;

    private GPOptionGroup[] myGroups;

    public OptionsDialogAction(GPOptionGroup[] groups, UIFacade uifacade) {
        super(GanttLanguage.getInstance().getText("chartOptions"));
        myGroups = groups;
        myUIFacade = uifacade;
        this.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource(
                "/icons/chartOptions_16.gif")));
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < myGroups.length; i++) {
            myGroups[i].lock();
        }
        final OkAction okAction = new OkAction() {
            public void actionPerformed(ActionEvent e) {
                commit();
            }
        };
        final CancelAction cancelAction = new CancelAction() {
            public void actionPerformed(ActionEvent e) {
                rollback();
            }
        };
        myUIFacade.showDialog(createDialogComponent(), new Action[] { okAction,
                cancelAction });
    }

    private void commit() {
        for (int i = 0; i < myGroups.length; i++) {
            myGroups[i].commit();
        }
    }

    private void rollback() {
        for (int i = 0; i < myGroups.length; i++) {
            myGroups[i].rollback();
        }
    }

    protected Component createPreviewComponent() {
        return null;
    }
    private Component createDialogComponent() {
        OptionsPageBuilder builder = new OptionsPageBuilder();

        JPanel combinedPanel = new JPanel(new BorderLayout());
        JComponent comp = builder.buildPage(myGroups, "ganttChart");
        combinedPanel.add(comp, BorderLayout.CENTER);
        combinedPanel.setBorder(BorderFactory.createEmptyBorder(0,0,3,0));
        Component previewComponent = createPreviewComponent();
        if (previewComponent!=null) {
            JPanel previewPanel = new JPanel(new BorderLayout());
            previewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                    .createEtchedBorder(), GanttLanguage.getInstance().getText("previewBar")));

            previewPanel.add(previewComponent, BorderLayout.CENTER);
            combinedPanel.add(previewPanel, BorderLayout.SOUTH);
        }
        return combinedPanel;
    }

	protected String getIconFilePrefix() {
		return null;
	}

	protected String getLocalizedName() {
		return getI18n("chartOptions");
	}
	
	
}
