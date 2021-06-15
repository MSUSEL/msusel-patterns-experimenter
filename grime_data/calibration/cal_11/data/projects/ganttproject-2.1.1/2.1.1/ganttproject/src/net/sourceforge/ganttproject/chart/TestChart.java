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
 * Created on 20.05.2005
 */
package net.sourceforge.ganttproject.chart;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Date;
import java.util.Map;

import javax.swing.Icon;

import org.eclipse.core.runtime.IStatus;

import net.sourceforge.ganttproject.GanttExportSettings;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.task.TaskManager;

public class TestChart implements Chart {
    public BufferedImage getChart(GanttExportSettings settings) {
        throw new UnsupportedOperationException();
    }

    public Date getStartDate() {
        throw new UnsupportedOperationException();
    }

    public Date getEndDate() {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        return "Test chart";
    }

    public void setTaskManager(TaskManager taskManager) {
        // TODO Auto-generated method stub

    }

    public Object getAdapter(Class arg0) {
        return null;
    }

    public void reset() {
        // TODO Auto-generated method stub

    }

    public Icon getIcon() {
        // TODO Auto-generated method stub
        return null;
    }

    public GPOptionGroup[] getOptionGroups() {
        return null;
    }

    public Chart createCopy() {
        // TODO Auto-generated method stub
        return null;
    }

	public RenderedImage getRenderedImage(GanttExportSettings settings) {
		// TODO Auto-generated method stub
		return null;
	}

	public ChartSelection getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	public IStatus canPaste(ChartSelection selection) {
		// TODO Auto-generated method stub
		return null;
	}

	public void paste(ChartSelection selection) {
		// TODO Auto-generated method stub
		
	}

	public void addSelectionListener(ChartSelectionListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void removeSelectionListener(ChartSelectionListener listener) {
		// TODO Auto-generated method stub
		
	}

}
