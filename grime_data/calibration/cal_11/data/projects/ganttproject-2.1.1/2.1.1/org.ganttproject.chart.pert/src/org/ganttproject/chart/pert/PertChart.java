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
/***************************************************************************
PertChart.java - description
Copyright [2005 - ADAE]
This file is part of GanttProject].
***************************************************************************/

/***************************************************************************
 * GanttProject is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License as published by    *
 * the Free Software Foundation; either version 2 of the License, or       *
 * (at your option) any later version.                                     *
 *                                                                         *
 * GanttProject is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *

***************************************************************************/

package org.ganttproject.chart.pert;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.eclipse.core.runtime.IStatus;

import net.sourceforge.ganttproject.GanttExportSettings;
import net.sourceforge.ganttproject.chart.Chart;
import net.sourceforge.ganttproject.chart.ChartSelection;
import net.sourceforge.ganttproject.chart.ChartSelectionListener;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.task.TaskManager;

/**
 * Abstract class that should implement all PERT chart implementation.
 * 
 * @author bbaranne
 * 
 */
public abstract class PertChart extends JPanel implements Chart {

    private final List/*<ChartSelectionListener>*/ myListeners = new ArrayList/*<ChartSelectionListener>*/();
    /**
     * Task manager used to build PERT chart. It provides data.
     */
    protected TaskManager myTaskManager;

    public PertChart(TaskManager taskManager) {
        myTaskManager = taskManager;
    }

    /**
     * @inheritDoc
     */
    public abstract BufferedImage getChart(GanttExportSettings settings);

    /**
     * @inheritDoc
     */
    public abstract String getName();

    /**
     * Builds PERT chart.
     * 
     */
    protected abstract void buildPertChart();

    /**
     * This method in not supported by this Chart.
     */
    public Date getStartDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * This method in not supported by this Chart.
     */
    public Date getEndDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the task manager and build chart afterwards.
     */
    public void setTaskManager(TaskManager taskManager) {
        myTaskManager = taskManager;
    }

    public GPOptionGroup[] getOptionGroups() {
        return null;
    }

    public Chart createCopy() {
        return null;
    }
    
    public ChartSelection getSelection() {
        throw new UnsupportedOperationException();
    }
    
    public IStatus canPaste(ChartSelection selection) {
        throw new UnsupportedOperationException();        
    }
    
    public void paste(ChartSelection selection) {
        throw new UnsupportedOperationException();
        
    }
    
    public void addSelectionListener(ChartSelectionListener listener) {
        myListeners.add(listener);
        
    }
    public void removeSelectionListener(ChartSelectionListener listener) {
        myListeners.remove(listener);
        
    }
    

}
