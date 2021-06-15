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
 * This code is provided under the terms of GPL version 2.
 * Please see LICENSE file for details
 * (C) Dmitry Barashev, GanttProject team, 2004-2008
 */
package net.sourceforge.ganttproject.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;

import net.sourceforge.ganttproject.calendar.GanttDaysOff;
import net.sourceforge.ganttproject.task.ResourceAssignment;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskActivity;

/**
 * Represents load of of one particular resource in the given time range
 */
public class LoadDistribution {
    public static class Load {
        public float load;

        public final Task refTask;

        Load(Date startDate, Date endDate, float load, Task ref) {
            this.load = load;
            this.refTask = ref;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String toString() {
            return "start="+this.startDate + " load=" + this.load
                    + " refTask = " + this.refTask;
        }

        public boolean isResourceUnavailable() {
            return load==-1;
        }
        public final Date startDate;
        public final Date endDate;
    }

    private final List/*<Load>*/ myDaysOff = new LinkedList/*<Load>*/();

    private final List/*<Load>*/ myLoads = new ArrayList/*<Load>*/();

    private final List/*<Load>*/ myTasksLoads = new ArrayList/*<Load>*/();

    private final ProjectResource myResource;

    public LoadDistribution(ProjectResource resource) {
        myLoads.add(new Load(null, null, 0, null));
        myDaysOff.add(new Load(null, null, 0, null));
        myResource = resource;
        ResourceAssignment[] assignments = myResource.getAssignments();
        for (int j = 0; j < assignments.length; j++) {
            processAssignment(assignments[j]);
        }
        processDaysOff(myResource);
    }

    private void processDaysOff(ProjectResource resource) {
        HumanResource hr = (HumanResource) resource;
        DefaultListModel daysOff = hr.getDaysOff();
        if (daysOff != null) {
            for (int l = 0; l < daysOff.size(); l++) {
                processDayOff((GanttDaysOff) daysOff.get(l));
            }
        }

    }

    private void processDayOff(GanttDaysOff dayOff) {
        Date dayOffEnd = dayOff.getFinish().getTime();
        addLoad(dayOff.getStart().getTime(), dayOffEnd, -1, myDaysOff, null);
    }

    private void processAssignment(ResourceAssignment assignment) {
        Task task = assignment.getTask();
        TaskActivity[] activities = task.getActivities();
        for (int i = 0; i < activities.length; i++) {
            processActivity(activities[i], assignment.getLoad());
        }
    }

    private void processActivity(TaskActivity activity, float load) {
        if (activity.getIntensity() == 0) {
            return;
        }
        addLoad(activity.getStart(), activity.getEnd(), load, myLoads, activity.getTask());
    }

    private void addLoad(Date startDate, Date endDate,
            float load, List/*<Load>*/ loads, Task t) {
        Load taskLoad = new Load(startDate, endDate, load, t);

        myTasksLoads.add(taskLoad);

        int idxStart = -1;
        float currentLoad = 0;
        if (startDate == null) {
            idxStart = 0;
        } else {
            for (int i = 1; i < loads.size(); i++) {
                Load nextLoad = (Load) loads.get(i);
                if (startDate.compareTo(nextLoad.startDate)>=0) {
                    currentLoad = ((Load)loads.get(i)).load;
                }
                if (startDate.compareTo(nextLoad.startDate)>0) {
                    continue;
                }
                idxStart = i;
                if (startDate.compareTo(nextLoad.startDate)<0) {
                    loads.add(i, new Load(startDate, null, currentLoad, null));
                }
                break;
            }
        }
        if (idxStart == -1) {
            idxStart = loads.size();
            loads.add(new Load(startDate, null, 0, t));
        }
        int idxEnd = -1;
        if (endDate == null) {
            idxEnd = loads.size() - 1;
        } else {
            for (int i = idxStart; i < loads.size(); i++) {
                Load nextLoad = (Load) loads.get(i);
                if (endDate.compareTo(nextLoad.startDate)>0) {
                    nextLoad.load += load;
                    continue;
                }
                idxEnd = i;
                if (endDate.compareTo(nextLoad.startDate)<0) {
                    Load prevLoad = (Load) loads.get(i - 1);
                    loads.add(i, new Load(endDate, null, prevLoad.load - load, null));
                }
                break;
            }
        }
        if (idxEnd == -1) {
            idxEnd = loads.size();
            loads.add(new Load(endDate, null, 0, t));
        }

    }

    public ProjectResource getResource() {
        return myResource;
    }

    public List/*<Load>*/ getLoads() {
        return myLoads;
    }

    public List/*<Load>*/ getDaysOff() {
        return myDaysOff;
    }

    /**
     * Returns a list of lists of <code>Load</code> instances. Each list
     * contains a set of <code>Load</code>
     *
     * @return a list of lists of <code>Load</code> instances.
     */
    public List/*<Load>*/ getTasksLoads() {
        return myTasksLoads;
    }

    public Map/*<Task, List<Load>>*/ getSeparatedTaskLoads() {
        HashMap/*<Task,List<Load>>*/ result = new HashMap/*<Task, List<Load>>*/();
        List taskLoads = getTasksLoads();
        for (int i=0; i<taskLoads.size(); i++) {
        	Load nextLoad = (Load) taskLoads.get(i);
            Task nextTask = nextLoad.refTask;
            List/*<Load>*/ partition = (List) result.get(nextTask);
            if (partition==null) {
                partition = new ArrayList/*<Load>*/();
                result.put(nextTask, partition);
            }
            partition.add(nextLoad);
        }
        return result;
    }
}