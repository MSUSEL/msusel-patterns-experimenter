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

import java.awt.Component;
import java.awt.Frame;
import java.io.File;

import javax.swing.Action;

import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.chart.Chart;
import net.sourceforge.ganttproject.chart.GanttChart;
import net.sourceforge.ganttproject.gui.ResourceTreeUIFacade;
import net.sourceforge.ganttproject.gui.TaskSelectionContext;
import net.sourceforge.ganttproject.gui.TaskTreeUIFacade;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.scrolling.ScrollingManager;
import net.sourceforge.ganttproject.gui.zoom.ZoomManager;
import net.sourceforge.ganttproject.undo.GPUndoManager;

public class ConsoleUIFacade implements UIFacade {
    private UIFacade myRealFacade;

    ConsoleUIFacade(UIFacade realFacade) {
        myRealFacade = realFacade;
    }
    public ScrollingManager getScrollingManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public ZoomManager getZoomManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public Choice showConfirmationDialog(String message, String title) {
        // TODO Auto-generated method stub
        return null;
    }

    public void showPopupMenu(Component invoker, Action[] actions, int x, int y) {
        // TODO Auto-generated method stub

    }

    public void showDialog(Component content, Action[] buttonActions) {
        // TODO Auto-generated method stub

    }
    public void showDialog(Component content, Action[] buttonActions, String title) {
        // TODO Auto-generated method stub

    }

    public void setStatusText(String text) {
        // TODO Auto-generated method stub

    }

    public void showErrorDialog(String errorMessage) {
        System.err.println("[ConsoleUIFacade] ERROR: "+errorMessage);
    }

    public void showErrorDialog(Throwable e) {
        System.err.println("[ConsoleUIFacade] ERROR: "+e.getMessage());
       	e.printStackTrace();
    }

	public void logErrorMessage(Throwable e) {
		System.err.println("[ConsoleUIFacade] ERROR:"+e.getMessage());
        e.printStackTrace();
	}
	public GanttChart getGanttChart() {
        return myRealFacade.getGanttChart();
    }

    public Chart getResourceChart() {
        return myRealFacade.getResourceChart();
    }

    public Chart getActiveChart() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getViewIndex() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setViewIndex(int viewIndex) {
        // TODO Auto-generated method stub

    }

    public int getGanttDividerLocation() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setGanttDividerLocation(int location) {
        // TODO Auto-generated method stub

    }

    public int getResourceDividerLocation() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setResourceDividerLocation(int location) {
        // TODO Auto-generated method stub

    }

    public void refresh() {
        // TODO Auto-generated method stub

    }

    public Frame getMainFrame() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setWorkbenchTitle(String title) {
        // TODO Auto-generated method stub

    }

    public void changeWorkingDirectory(File parentFile) {
        // TODO Auto-generated method stub

    }
    public GPUndoManager getUndoManager() {
        // TODO Auto-generated method stub
        return null;
    }
	public TaskTreeUIFacade getTaskTree() {
		return myRealFacade.getTaskTree();
	}
	public ResourceTreeUIFacade getResourceTree() {
		return myRealFacade.getResourceTree();
	}
	public TaskSelectionContext getTaskSelectionContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
