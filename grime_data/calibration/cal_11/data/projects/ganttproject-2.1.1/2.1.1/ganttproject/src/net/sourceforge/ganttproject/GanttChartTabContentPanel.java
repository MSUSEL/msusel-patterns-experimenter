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
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.eclipse.core.runtime.IAdaptable;

import net.sourceforge.ganttproject.action.task.LinkTasksAction;
import net.sourceforge.ganttproject.action.task.UnlinkTasksAction;
import net.sourceforge.ganttproject.chart.Chart;
import net.sourceforge.ganttproject.gui.TaskTreeUIFacade;
import net.sourceforge.ganttproject.gui.TestGanttRolloverButton;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.language.GanttLanguage;

class GanttChartTabContentPanel implements IAdaptable {
    private JSplitPane mySplitPane;
    private Component myTaskTree;
    private final Component myGanttChart;
    private final TaskTreeUIFacade myTreeFacade;
    private JPanel myTabContentPanel;
    private final IGanttProject myProject;
    private final UIFacade myWorkbenchFacade;

    GanttChartTabContentPanel(IGanttProject project, UIFacade workbenchFacade, TaskTreeUIFacade treeFacade, Component ganttChart) {
        myProject = project;
        myWorkbenchFacade = workbenchFacade;
        myTreeFacade = treeFacade;
        myTaskTree = treeFacade.getTreeComponent();
        myGanttChart = ganttChart;
    }
    Component getComponent() {
    	if (myTabContentPanel==null) {
	        JPanel left = new JPanel(new BorderLayout());
	        Box treeHeader = Box.createVerticalBox();
	        GanttImagePanel but = new GanttImagePanel("big.png", 300,
	                42);
	        treeHeader.add(but);
	        left.add(treeHeader, BorderLayout.NORTH);
	        left.add(myTaskTree, BorderLayout.CENTER);
	        left.setPreferredSize(new Dimension(315, 600));
	        left.setBackground(new Color(102, 153, 153));
	
	        
	        JPanel right = new JPanel(new BorderLayout());
	        right.add(myGanttChart, BorderLayout.CENTER);
	        // A splitpane is use
	        mySplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	        if (GanttLanguage.getInstance().getComponentOrientation() == ComponentOrientation.LEFT_TO_RIGHT) {
	            mySplitPane.setLeftComponent(left);
	            mySplitPane.setRightComponent(right);
	            mySplitPane
	                    .applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	        } else {
	            mySplitPane.setRightComponent(left);
	            mySplitPane.setLeftComponent(right);
	            mySplitPane.setDividerLocation((int) (Toolkit.getDefaultToolkit()
	                    .getScreenSize().getWidth() - left.getPreferredSize()
	                    .getWidth()));
	            mySplitPane
	                    .applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	        }
	        mySplitPane.setOneTouchExpandable(true);
	        mySplitPane.setPreferredSize(new Dimension(800, 500));
	        myTabContentPanel = new JPanel(new BorderLayout());
	        myTabContentPanel.add(createButtonPanel(), BorderLayout.NORTH);
	        myTabContentPanel.add(mySplitPane, BorderLayout.CENTER);
    	}
        return myTabContentPanel;
        
    }
    
    private Component createButtonPanel() {
        Box buttonBar = Box.createHorizontalBox();
        //JToolBar buttonBar = new JToolBar();
        //buttonBar.setFloatable(false);
        TestGanttRolloverButton unindentButton = new TestGanttRolloverButton(myTreeFacade.getUnindentAction()) {
			public String getText() {
				return null;
			}        
        };
        buttonBar.add(unindentButton);
        
        TestGanttRolloverButton indentButton = new TestGanttRolloverButton(myTreeFacade.getIndentAction()) {
			public String getText() {
				return null;
			}                	
        };
        buttonBar.add(indentButton);
        //
        buttonBar.add(Box.createHorizontalStrut(3));
        //
        TestGanttRolloverButton upButton = new TestGanttRolloverButton(myTreeFacade.getMoveUpAction()) {
			public String getText() {
				return null;
			}                	
        };
        buttonBar.add(upButton);
        //
        TestGanttRolloverButton downButton = new TestGanttRolloverButton(myTreeFacade.getMoveDownAction()) {
			public String getText() {
				return null;
			}                	
        };
        buttonBar.add(downButton);
        //
        buttonBar.add(Box.createHorizontalStrut(8));
        Action linkAction = new LinkTasksAction(myProject.getTaskManager(), Mediator.getTaskSelectionManager(), myWorkbenchFacade);
        myTreeFacade.setLinkTasksAction(linkAction);
        TestGanttRolloverButton linkButton = new TestGanttRolloverButton(linkAction) {
			public String getText() {
				return null;
			}                	
        };
        buttonBar.add(linkButton);
        //
        Action unlinkAction = new UnlinkTasksAction(myProject.getTaskManager(), Mediator.getTaskSelectionManager(), myWorkbenchFacade);
        myTreeFacade.setUnlinkTasksAction(unlinkAction);
        TestGanttRolloverButton unlinkButton = new TestGanttRolloverButton(unlinkAction) {
			public String getText() {
				return null;
			}                	
        };
        buttonBar.add(unlinkButton);
        //
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(buttonBar, BorderLayout.WEST);
        return buttonPanel;
    }
    int getDividerLocation() {
        return mySplitPane.getDividerLocation();
    }
    void setDividerLocation(int location) {
        mySplitPane.setDividerLocation(location);
    }
	public Object getAdapter(Class adapter) {
		if (Container.class.equals(adapter)) {
			return getComponent();
		}
		if (Chart.class.equals(adapter)) {
			return myGanttChart;
		}
		return null;
	}
}
