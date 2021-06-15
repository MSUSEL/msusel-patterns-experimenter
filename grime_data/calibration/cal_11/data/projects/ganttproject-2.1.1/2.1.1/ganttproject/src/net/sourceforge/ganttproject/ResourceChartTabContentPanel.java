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
 * Created on 22.10.2005
 */
package net.sourceforge.ganttproject;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.Box;
import javax.swing.JPanel;

import org.eclipse.core.runtime.IAdaptable;

import net.sourceforge.ganttproject.chart.Chart;
import net.sourceforge.ganttproject.gui.ResourceTreeUIFacade;
import net.sourceforge.ganttproject.gui.TestGanttRolloverButton;

class ResourceChartTabContentPanel implements IAdaptable {
    private ResourceTreeUIFacade myTreeFacade;
    private final Component myResourceChart;
    private JPanel myTabContentPanel;

    ResourceChartTabContentPanel(ResourceTreeUIFacade resourceTree, Component resourceChart) {
        myTreeFacade = resourceTree;
        myResourceChart = resourceChart;
    }
    
    Component getComponent() {
    	if (myTabContentPanel==null) {
	        myTabContentPanel = new JPanel(new BorderLayout());
	        Component buttonPanel = createButtonPanel();
	        myTabContentPanel.add(buttonPanel, BorderLayout.NORTH);
	        myTabContentPanel.add(myTreeFacade.getUIComponent(), BorderLayout.CENTER);
    	}
        return myTabContentPanel;
    }

    private Component createButtonPanel() {
        Box buttonBar = Box.createHorizontalBox();
        TestGanttRolloverButton upButton = new TestGanttRolloverButton(myTreeFacade.getMoveUpAction());
        upButton.setTextHidden(true);
        buttonBar.add(upButton);
        //
        TestGanttRolloverButton downButton = new TestGanttRolloverButton(myTreeFacade.getMoveDownAction());
        downButton.setTextHidden(true);
        buttonBar.add(downButton);
        //
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(buttonBar, BorderLayout.WEST);
        return buttonPanel;
    }

	public Object getAdapter(Class adapter) {
		if (Container.class.equals(adapter)) {
			return getComponent();
		}
		if (Chart.class.equals(adapter)) {
			return myResourceChart;
		}
		return null;
	}
}
