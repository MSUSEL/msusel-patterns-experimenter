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
package net.sourceforge.ganttproject.action;

import java.awt.event.ActionEvent;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.Mediator;
import net.sourceforge.ganttproject.gui.GanttDialogInfo;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.UIFacade.Choice;
import net.sourceforge.ganttproject.resource.AssignmentContext;
import net.sourceforge.ganttproject.resource.ResourceManager;
import net.sourceforge.ganttproject.task.ResourceAssignment;

public class DeleteAssignmentAction extends GPAction {
    private final AssignmentContext myContext;

    private GanttProject myProjectFrame;


    public DeleteAssignmentAction(ResourceManager hrManager,
            AssignmentContext context, GanttProject projectFrame) {
        myProjectFrame = projectFrame;
        // this.putValue(AbstractAction.NAME, GanttProject
        // .correctLabel(getLanguage().getText("deleteAssignment")));
        myContext = context;
    }

    public void actionPerformed(ActionEvent e) {
        myProjectFrame.getTabs().setSelectedIndex(UIFacade.RESOURCES_INDEX);
        final ResourceAssignment[] context = myContext.getResourceAssignments();
        if (context != null && context.length > 0) {
        	Choice choice = myProjectFrame.getUIFacade().showConfirmationDialog(getI18n("msg23") + " "
                    + getDisplayName(context) + "?", getI18n("warning"));
            if (choice==Choice.YES) {
                myProjectFrame.getUIFacade().getUndoManager().undoableEdit("Resource removed",
                        new Runnable() {
                            public void run() {
                                deleteAssignments(context);
                                myProjectFrame.setAskForSave(true);
                                myProjectFrame.refreshProjectInfos();
                                myProjectFrame.repaint2();
                            }
                        });
            }
        }
        else {
        	myProjectFrame.deleteResources();
        }
    }

    private void deleteAssignments(ResourceAssignment[] context) {
        for (int i = 0; i < context.length; i++) {
            ResourceAssignment ra = context[i];
            ra.delete();
            ra.getTask().getAssignmentCollection().deleteAssignment(
                    ra.getResource());
        }
    }

    private static String getDisplayName(Object[] objs) {
        if (objs.length == 1) {
            return objs[0].toString();
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < objs.length; i++) {
            result.append(objs[i].toString());
            if (i < objs.length - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }

	protected String getIconFilePrefix() {
		return "delete_";
	}

	protected String getLocalizedName() {
		return getI18n("deleteAssignment");
	}
	
	

}
