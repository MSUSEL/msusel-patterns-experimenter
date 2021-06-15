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
package net.sourceforge.ganttproject.io;


import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.roles.Role;
import net.sourceforge.ganttproject.task.ResourceAssignment;
import net.sourceforge.ganttproject.task.Task;

class AssignmentSaver extends SaverBase {
    void save(IGanttProject project, TransformerHandler handler) throws SAXException {
        AttributesImpl attrs = new AttributesImpl();
        startElement("allocations", handler);
        Task[] tasks = project.getTaskManager().getTasks();
        for (int i = 0; i < tasks.length; i++) {
            Task task = tasks[i];
            ResourceAssignment[] assignments = task.getAssignments();
            for (int j = 0; j < assignments.length; j++) {
                int task_id = task.getTaskID();
                ResourceAssignment next = assignments[j];

                Role roleForAssignment = next.getRoleForAssignment();
                if (roleForAssignment == null) {
                    if (next.getResource() instanceof HumanResource) {
                        roleForAssignment = ((HumanResource) next
                                .getResource()).getRole();
                    }
                }
                addAttribute("task-id", String.valueOf(task_id), attrs);
                addAttribute("resource-id", String.valueOf(next.getResource().getId()), attrs);
                addAttribute("function", roleForAssignment.getPersistentID(), attrs);
                addAttribute("responsible", String.valueOf(next.isCoordinator()), attrs);
                addAttribute("load", String.valueOf(next.getLoad()), attrs);
                emptyElement("allocation", attrs, handler);
            }
        }
        endElement("allocations", handler);
    }

}
