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

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.resource.ResourceContext;
import net.sourceforge.ganttproject.resource.ResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;

public class ResourceActionSet {
    private final UIFacade myUIFacade;

	private final DeleteHumanAction myDeleteHumanAction;

	public ResourceActionSet(IGanttProject project, ResourceContext context,
            GanttProject projectFrame, UIFacade uiFacade) {
        myManager = project.getHumanResourceManager();
        myRoleManager = project.getRoleManager();
        myContext = context;
        myProjectFrame = projectFrame;
        myUIFacade = uiFacade;
        myDeleteHumanAction = new DeleteHumanAction(myManager, myContext, myProjectFrame, myUIFacade);
    }

    public AbstractAction[] getActions() {
        if (myActions == null) {
            myActions = new AbstractAction[] {
                    new NewHumanAction(myManager, myRoleManager,
                            myProjectFrame, myProjectFrame),
                    myDeleteHumanAction};
        }
        return myActions;
    }

    public Action getDeleteHumanAction() {
    	return myDeleteHumanAction;
    }
    private final RoleManager myRoleManager;

    private final ResourceManager myManager;

    private final ResourceContext myContext;

    private final GanttProject myProjectFrame;

    private AbstractAction[] myActions;
}
