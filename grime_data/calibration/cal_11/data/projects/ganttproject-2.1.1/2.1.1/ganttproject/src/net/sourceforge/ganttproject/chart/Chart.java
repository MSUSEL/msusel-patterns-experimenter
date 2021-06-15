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
package net.sourceforge.ganttproject.chart;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Date;
import java.util.Map;

import javax.swing.Icon;

import net.sourceforge.ganttproject.GanttExportSettings;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.task.TaskManager;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public interface Chart extends IAdaptable {
	public RenderedImage getRenderedImage(GanttExportSettings settings);
	
	/** @deprecated Use getRenderedImage */
    public BufferedImage getChart(GanttExportSettings settings);

    public Date getStartDate();

    public Date getEndDate();

    public String getName();

    public void setTaskManager(TaskManager taskManager);

    public void reset();

    public Icon getIcon();
        
    public GPOptionGroup[] getOptionGroups();
    
    public Chart createCopy();

    public ChartSelection getSelection();
    
    public IStatus canPaste(ChartSelection selection);
    
    public void paste(ChartSelection selection);
    
    public void addSelectionListener(ChartSelectionListener listener);
    public void removeSelectionListener(ChartSelectionListener listener);
    
}
