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
package net.sourceforge.ganttproject.action.task;

import java.util.List;

import javax.swing.SwingUtilities;

import net.sourceforge.ganttproject.GanttTask;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.gui.GanttDialogProperties;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.task.TaskSelectionManager;

public class TaskPropertiesAction extends TaskActionBase {

	private RoleManager myRoleManager;
	private HumanResourceManager myHumanManager;
	private IGanttProject myProject;
    private final TaskSelectionManager mySelectionManager;

	public TaskPropertiesAction(IGanttProject project, TaskSelectionManager selectionManager, UIFacade uiFacade) {
		super(project.getTaskManager(), selectionManager, uiFacade);
		myProject = project;
        mySelectionManager = selectionManager;
		myHumanManager = (HumanResourceManager) project.getHumanResourceManager();
		myRoleManager = project.getRoleManager();
	}

	protected boolean isEnabled(List selection) {
		return selection.size()==1;
	}

	protected void run(List/*<Task>*/ selection) throws Exception {
        if (selection.size()!=1) {
            return;
        }
		final GanttTask[] tasks = new GanttTask[] {(GanttTask)selection.get(0)};
        GanttDialogProperties pd = new GanttDialogProperties(tasks);
        mySelectionManager.setUserInputConsumer(pd);
        pd.show(myProject, getUIFacade());
        if (pd.change) {
            myProject.setModified(true);
//            setRowHeight(rowHeight);
//            getResourcePanel().getResourceTreeTableModel()
//                    .updateResources();
        }
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mySelectionManager.clear();
				mySelectionManager.addTask(tasks[0]);
			}
        });

	}

	protected String getLocalizedName() {
		return getI18n("propertiesTask");
	}

	protected String getIconFilePrefix() {
		return "properties_";
	}

}
