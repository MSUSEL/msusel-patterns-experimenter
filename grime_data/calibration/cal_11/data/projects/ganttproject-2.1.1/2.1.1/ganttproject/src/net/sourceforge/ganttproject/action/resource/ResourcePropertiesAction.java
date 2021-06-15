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
package net.sourceforge.ganttproject.action.resource;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.gui.GanttDialogPerson;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.resource.ProjectResource;
import net.sourceforge.ganttproject.resource.ResourceContext;

public class ResourcePropertiesAction extends GPAction {
	private final IGanttProject myProject;
	private final UIFacade myUIFacade;
	private HumanResource mySelectedResource;

	public ResourcePropertiesAction(IGanttProject project, UIFacade uiFacade) {
		myProject = project;
		myUIFacade = uiFacade;
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.ALT_DOWN_MASK));
	}
	protected String getLocalizedName() {
		return getI18n("propertiesHuman");
	}

	protected String getTooltipText() {
		return getI18n("propertiesHuman");
	}

	protected String getIconFilePrefix() {
		return "properties_";
	}

	public void actionPerformed(ActionEvent arg0) {
        if (getSelectedResource()!=null) {
            GanttDialogPerson dp = new GanttDialogPerson(getUIFacade(), GanttLanguage.getInstance(), getSelectedResource());
            dp.setVisible(true);
            if (dp.result()) {
                getProject().setModified(true);
            }            
        }
	}

	private IGanttProject getProject() {
		return myProject;
	}
	private UIFacade getUIFacade() {
		return myUIFacade;
	}

	private HumanResource getSelectedResource() {
		return mySelectedResource;
	}

	public void setContext(ResourceContext context) {
		ProjectResource[] resources = context.getResources();
		if (resources.length==1) {
			mySelectedResource = (HumanResource) resources[0];
			setEnabled(true);
		}
		else {
			setEnabled(false);
		}
	}

}
