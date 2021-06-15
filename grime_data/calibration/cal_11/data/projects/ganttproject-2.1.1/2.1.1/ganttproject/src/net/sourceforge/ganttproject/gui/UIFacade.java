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
 * Created on 26.02.2005
 */
package net.sourceforge.ganttproject.gui;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;

import javax.swing.Action;

import net.sourceforge.ganttproject.chart.Chart;
import net.sourceforge.ganttproject.chart.GanttChart;
import net.sourceforge.ganttproject.gui.scrolling.ScrollingManager;
import net.sourceforge.ganttproject.gui.zoom.ZoomManager;
import net.sourceforge.ganttproject.undo.GPUndoManager;

/**
 * @author bard
 */
public interface UIFacade {
    public static class Choice {
        public static final Choice YES = new Choice();
        public static final Choice NO = new Choice();
        public static final Choice CANCEL = new Choice();
        public static final Choice OK = new Choice();
    }
    
    public static final int GANTT_INDEX = 0;

    public static final int RESOURCES_INDEX = 1;

    ScrollingManager getScrollingManager();

    //ChartViewState getGanttChartViewState();

    ZoomManager getZoomManager();
    GPUndoManager getUndoManager();
    Choice showConfirmationDialog(String message, String title);

    void showPopupMenu(Component invoker, Action[] actions, int x, int y);

    void showDialog(Component content, Action[] buttonActions);
    void showDialog(Component content, Action[] buttonActions, String title);

    void setStatusText(String text);

    void showErrorDialog(String errorMessage);

    void showErrorDialog(Throwable e);
	void logErrorMessage(Throwable e);

    GanttChart getGanttChart();

    Chart getResourceChart();

    Chart getActiveChart();

    /**
     * Returns the index of the displayed tab.
     * 
     * @return the index of the displayed tab.
     */
    int getViewIndex();

    void setViewIndex(int viewIndex);

    int getGanttDividerLocation();

    void setGanttDividerLocation(int location);

    int getResourceDividerLocation();

    void setResourceDividerLocation(int location);

    void refresh();
    
    Frame getMainFrame();

    void setWorkbenchTitle(String title);

    TaskTreeUIFacade getTaskTree();
    
    ResourceTreeUIFacade getResourceTree();
    //void changeWorkingDirectory(File parentFile);

	TaskSelectionContext getTaskSelectionContext();

}
