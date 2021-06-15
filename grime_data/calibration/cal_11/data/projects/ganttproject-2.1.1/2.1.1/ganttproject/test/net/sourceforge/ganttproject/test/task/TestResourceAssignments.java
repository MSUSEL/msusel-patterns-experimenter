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

import junit.framework.TestCase;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.ResourceAssignment;
import net.sourceforge.ganttproject.task.TaskManagerConfig;
import net.sourceforge.ganttproject.time.TimeUnitStack;
import net.sourceforge.ganttproject.time.gregorian.GregorianTimeUnitStack;
import net.sourceforge.ganttproject.calendar.AlwaysWorkingTimeCalendarImpl;
import net.sourceforge.ganttproject.calendar.GPCalendar;
import net.sourceforge.ganttproject.resource.ResourceManager;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.resource.ProjectResource;
import net.sourceforge.ganttproject.roles.RoleManager;

import java.awt.Color;
import java.net.URL;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class TestResourceAssignments extends TestCase {
    private TaskManager myTaskManager;

    public TestResourceAssignments(String s) {
        super(s);
    }

    public void testResourceAppearsInListAfterCreation() {
        TaskManager taskManager = getTaskManager();
        Task task = taskManager.createTask();
        ProjectResource res1 = getResourceManager().getById(1);
        ProjectResource res2 = getResourceManager().getById(2);
        task.getAssignmentCollection().addAssignment(res1);
        task.getAssignmentCollection().addAssignment(res2);
        Set actualResources = extractResources(task);
        Set expectedResources = new HashSet(Arrays.asList(new Object[] { res1,
                res2 }));
        assertEquals("Unexpected set of resources assigned to task=" + task,
                expectedResources, actualResources);
    }

    public void testResourceDisappearsFromListAfterAssignmentDeletion() {
        TaskManager taskManager = getTaskManager();
        Task task = taskManager.createTask();
        ProjectResource res1 = getResourceManager().getById(1);
        ProjectResource res2 = getResourceManager().getById(2);
        ResourceAssignment asgn1 = task.getAssignmentCollection()
                .addAssignment(res1);
        ResourceAssignment asgn2 = task.getAssignmentCollection()
                .addAssignment(res2);
        //
        asgn2.delete();
        //
        Set actualResources = extractResources(task);
        Set expectedResources = new HashSet(Arrays
                .asList(new Object[] { res1 }));
        assertEquals("Unexpected set of resources assigned to task=" + task,
                expectedResources, actualResources);
    }

    public void testResourceIsNotAssignedTwice() {
        TaskManager taskManager = getTaskManager();
        Task task = taskManager.createTask();
        ProjectResource res1 = getResourceManager().getById(1);
        task.getAssignmentCollection().addAssignment(res1);
        task.getAssignmentCollection().addAssignment(res1);
        Set actualResources = extractResources(task);
        Set expectedResources = new HashSet(Arrays
                .asList(new Object[] { res1 }));
        assertEquals("Unexpected set of resources assigned to task=" + task,
                expectedResources, actualResources);
    }


    public void testAssignmentsDisappearOnTaskDeletion() {
        TaskManager taskManager = getTaskManager();
        Task task = taskManager.createTask();
        ProjectResource res1 = getResourceManager().getById(1);
        task.getAssignmentCollection().addAssignment(res1);
        task.delete();
        ResourceAssignment[] assignments = res1.getAssignments();
        assertTrue(
                "Resource is expected to have no assignments after task deletion",
                assignments.length == 0);
    }

    public void testAssignmentDisappearOnResourceDeletion() {
        TaskManager taskManager = getTaskManager();
        Task task = taskManager.createTask();
        taskManager.registerTask(task);
        ProjectResource res1 = getResourceManager().getById(1);
        task.getAssignmentCollection().addAssignment(res1);
        res1.delete();
        Set resources = extractResources(task);
        assertTrue("It is expecte that after resource deletion assignments disappear", resources.isEmpty());
    }

    private Set extractResources(Task task) {
        Set result = new HashSet();
        ResourceAssignment[] assignments = task.getAssignments();
        for (int i = 0; i < assignments.length; i++) {
            ResourceAssignment next = assignments[i];
            result.add(next.getResource());
            assertEquals("Unexpected task is owning resource assignment="
                    + next, task, next.getTask());
        }
        return result;
    }

    protected void setUp() throws Exception {
        super.setUp();
        myHumanResourceManager = new HumanResourceManager(RoleManager.Access
                .getInstance().getDefaultRole());
        getResourceManager().create("test resource#1", 1);
        getResourceManager().create("test resource#2", 2);
        myTaskManager = newTaskManager();
    }

    private TaskManager newTaskManager() {
        return TaskManager.Access.newInstance(null, new TaskManagerConfig() {

            public Color getDefaultColor() {
                return null;
            }

            public GPCalendar getCalendar() {
                return new AlwaysWorkingTimeCalendarImpl();
            }

            public TimeUnitStack getTimeUnitStack() {
                return new GregorianTimeUnitStack();
            }

            public ResourceManager getResourceManager() {
                return null;
            }

			public URL getProjectDocumentURL() {
				// TODO Auto-generated method stub
				return null;
			}
        });
    }

    private TaskManager getTaskManager() {
        return myTaskManager;
    }

    private ResourceManager getResourceManager() {
        return myHumanResourceManager;
    }

    private HumanResourceManager myHumanResourceManager;
}
