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
package net.sourceforge.ganttproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.ganttproject.calendar.GPCalendar;
import net.sourceforge.ganttproject.document.Document;
import net.sourceforge.ganttproject.document.DocumentManager;
import net.sourceforge.ganttproject.gui.UIConfiguration;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.resource.ResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.task.CustomColumnsManager;
import net.sourceforge.ganttproject.task.CustomColumnsStorage;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskContainmentHierarchyFacade;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.time.TimeUnitStack;

/**
 * This interface represents a project as a logical business entity, without any
 * UI (except some configuration options :)
 * 
 * @author bard
 */
public interface IGanttProject {

    String getProjectName();

    void setProjectName(String projectName);

    //
    String getDescription();

    void setDescription(String description);

    //
    String getOrganization();

    void setOrganization(String organization);

    //
    String getWebLink();

    void setWebLink(String webLink);

    //
    /**
     * Creates a new task and performs all necessary initialization procedures
     * such as changing properties of parent task, adjusting schedule, etc.
     */
    Task newTask();

    //
    GanttLanguage getI18n();

    UIConfiguration getUIConfiguration();

    ResourceManager getHumanResourceManager();

    RoleManager getRoleManager();

    TaskManager getTaskManager();

    TaskContainmentHierarchyFacade getTaskContainment();

    GPCalendar getActiveCalendar();

    TimeUnitStack getTimeUnitStack();

    void setModified();
    void setModified(boolean modified);

    void close();

    Document getDocument();
    void setDocument(Document document);
    DocumentManager getDocumentManager();
    
    void addProjectEventListener(ProjectEventListener listener);
    void removeProjectEventListener(ProjectEventListener listener);

    boolean isModified();

    void open(Document document) throws IOException;

	CustomPropertyManager getResourceCustomPropertyManager();

	CustomColumnsManager getTaskCustomColumnManager();

	CustomColumnsStorage getCustomColumnsStorage();

	List/*<GanttPreviousState*/ getBaselines();

    
}