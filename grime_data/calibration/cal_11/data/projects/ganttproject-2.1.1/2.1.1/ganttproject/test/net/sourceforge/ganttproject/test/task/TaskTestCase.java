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
package net.sourceforge.ganttproject.test.task;

import java.awt.Color;
import java.net.URL;

import junit.framework.TestCase;
import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.calendar.AlwaysWorkingTimeCalendarImpl;
import net.sourceforge.ganttproject.calendar.GPCalendar;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.resource.ResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.roles.RoleManagerImpl;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.TaskManagerConfig;
import net.sourceforge.ganttproject.time.TimeUnitStack;
import net.sourceforge.ganttproject.time.gregorian.GregorianTimeUnitStack;

/**
 * Created by IntelliJ IDEA. User: bard
 */
public abstract class TaskTestCase extends TestCase implements
        TaskManagerConfig {
    private TaskManager myTaskManager;

    private GPCalendar myFakeCalendar = new AlwaysWorkingTimeCalendarImpl();

    private TimeUnitStack myTimeUnitStack;

    private ResourceManager myResourceManager;

    private RoleManager myRoleManager;

    protected TaskManager getTaskManager() {
        return myTaskManager;
    }

    protected void setUp() throws Exception {
        super.setUp();
        myTimeUnitStack = new GregorianTimeUnitStack();
        myTaskManager = newTaskManager();
        myRoleManager = new RoleManagerImpl();
        myResourceManager = new HumanResourceManager(myRoleManager
                .getDefaultRole());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        myTaskManager = null;
    }
    protected GanttCalendar newFriday() {
        return new GanttCalendar(2004, 9, 15);
    }

    protected GanttCalendar newSaturday() {
        return new GanttCalendar(2004, 9, 16);
    }

    protected GanttCalendar newSunday() {
        return new GanttCalendar(2004, 9, 17);
    }

    protected GanttCalendar newTuesday() {
        return new GanttCalendar(2004, 9, 19);
    }

    protected GanttCalendar newMonday() {
        return new GanttCalendar(2004, 9, 18);
    }

    protected GanttCalendar newWendesday() {
        return new GanttCalendar(2004, 9, 20);
    }

    public Color getDefaultColor() {
        return null;
    }

    public GPCalendar getCalendar() {
        return myFakeCalendar;
    }

    public TimeUnitStack getTimeUnitStack() {
        return myTimeUnitStack;
    }

    public ResourceManager getResourceManager() {
        return myResourceManager;
    }

    protected TaskManager newTaskManager() {
        return TaskManager.Access.newInstance(null, this);
    }

    public URL getProjectDocumentURL() {
    	return null;
    }
}