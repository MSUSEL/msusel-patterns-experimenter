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
 * Created on 22.01.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.ganttproject;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.ganttproject.calendar.GPCalendar;
import net.sourceforge.ganttproject.calendar.WeekendCalendarImpl;
import net.sourceforge.ganttproject.document.Document;
import net.sourceforge.ganttproject.document.DocumentManager;
import net.sourceforge.ganttproject.font.Fonts;
import net.sourceforge.ganttproject.gui.UIConfiguration;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.resource.ResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.task.CustomColumnsManager;
import net.sourceforge.ganttproject.task.CustomColumnsStorage;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskContainmentHierarchyFacade;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.TaskManagerConfig;
import net.sourceforge.ganttproject.time.TimeUnitStack;
import net.sourceforge.ganttproject.time.gregorian.GPTimeUnitStack;

public class GanttProjectImpl implements IGanttProject {
    private String myProjectName;
    private String myDescription;
    private String myOrganization;
    private String myWebLink;
    private final TaskManager myTaskManager;
    private final HumanResourceManager myResourceManager;
    private final TaskManagerConfigImpl myTaskManagerConfig;
    private Document myDocument;
    private final List myListeners = new ArrayList();
    private UIConfiguration myUIConfiguration;
	private final CustomColumnsManager myTaskCustomColumnManager;
	private final CustomColumnsStorage myTaskCustomColumnStorage;
	private final List myBaselines = new ArrayList();

    public GanttProjectImpl() {
        myResourceManager = new HumanResourceManager(RoleManager.Access.getInstance().getDefaultRole());
        myTaskManagerConfig = new TaskManagerConfigImpl(myResourceManager, GanttLanguage.getInstance());
        myTaskManager = TaskManager.Access.newInstance(null, myTaskManagerConfig);
        myUIConfiguration = new UIConfiguration(Fonts.DEFAULT_MENU_FONT, Fonts.DEFAULT_CHART_FONT, Color.BLUE, true);
        myTaskCustomColumnStorage = new CustomColumnsStorage();
        myTaskCustomColumnManager = new CustomColumnsManager(myTaskCustomColumnStorage);
    }
    public String getProjectName() {
        return myProjectName;
    }

    public void setProjectName(String projectName) {
        myProjectName = projectName;
    }

    public String getDescription() {
        return myDescription;
    }

    public void setDescription(String description) {
        myDescription = description;
    }

    public String getOrganization() {
        return myOrganization;
    }

    public void setOrganization(String organization) {
        myOrganization = organization;
    }

    public String getWebLink() {
        return myWebLink;
    }

    public void setWebLink(String webLink) {
        myWebLink = webLink;
    }

    public Task newTask() {
        Task result = getTaskManager().createTask();
        getTaskManager().getTaskHierarchy().move(result,getTaskManager().getRootTask());
        return result;
    }

    public GanttLanguage getI18n() {
        return GanttLanguage.getInstance();
    }

    public UIConfiguration getUIConfiguration() {
        return myUIConfiguration;
    }

    public ResourceManager getHumanResourceManager() {
        return myResourceManager;
    }

    public RoleManager getRoleManager() {
        return RoleManager.Access.getInstance();
    }

    public TaskManager getTaskManager() {
        return myTaskManager;
    }

    public TaskContainmentHierarchyFacade getTaskContainment() {
        return getTaskManager().getTaskHierarchy();
    }

    public GPCalendar getActiveCalendar() {
        return myTaskManagerConfig.getCalendar();
    }

    public TimeUnitStack getTimeUnitStack() {
        return myTaskManagerConfig.getTimeUnitStack();
    }

    public void setModified() {
        // TODO Auto-generated method stub

    }

    public void setModified(boolean modified) {
        // TODO Auto-generated method stub

    }

    public void close() {
        // TODO Auto-generated method stub

    }

    public Document getDocument() {
        return myDocument;
    }

    public void setDocument(Document document) {
        myDocument = document;
    }

    public void addProjectEventListener(ProjectEventListener listener) {
        myListeners.add(listener);
    }

    public void removeProjectEventListener(ProjectEventListener listener) {
        myListeners.remove(listener);
    }

    public boolean isModified() {
        // TODO Auto-generated method stub
        return false;
    }

    public void open(Document document) throws IOException {
        // TODO Auto-generated method stub

    }


	public DocumentManager getDocumentManager() {
		// TODO Auto-generated method stub
		return null;
	}
	public CustomPropertyManager getResourceCustomPropertyManager() {
		return myResourceManager;
	};

    private static class TaskManagerConfigImpl implements TaskManagerConfig {
        private final ResourceManager myResourceManager;
        private final GPTimeUnitStack myTimeUnitStack;
        private final WeekendCalendarImpl myCalendar;

        private TaskManagerConfigImpl(ResourceManager resourceManager, GanttLanguage i18n) {
            myResourceManager = resourceManager;
            myTimeUnitStack = new GPTimeUnitStack(i18n);
            myCalendar = new WeekendCalendarImpl();
        }
        public Color getDefaultColor() {
            return Color.BLUE;
        }

        public GPCalendar getCalendar() {
            return myCalendar;
        }

        public TimeUnitStack getTimeUnitStack() {
            return myTimeUnitStack;
        }

        public ResourceManager getResourceManager() {
            return myResourceManager;
        }
		public URL getProjectDocumentURL() {
			return null;
		}

    }

	public CustomColumnsManager getTaskCustomColumnManager() {
		return myTaskCustomColumnManager;
	}
	public CustomColumnsStorage getCustomColumnsStorage() {
		return myTaskCustomColumnStorage;
	}
	public List getBaselines() {
		return myBaselines;
	}

}
