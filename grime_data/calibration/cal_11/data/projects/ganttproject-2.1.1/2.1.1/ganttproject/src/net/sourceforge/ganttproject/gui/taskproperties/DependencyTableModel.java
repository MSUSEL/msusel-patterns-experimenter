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
package net.sourceforge.ganttproject.gui.taskproperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.dependency.TaskDependency;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyCollectionMutator;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyConstraint;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyException;
import net.sourceforge.ganttproject.task.dependency.TaskDependency.Hardness;
import net.sourceforge.ganttproject.task.dependency.constraint.ConstraintImpl;
import net.sourceforge.ganttproject.task.dependency.constraint.FinishStartConstraintImpl;

/**
 * Created by IntelliJ IDEA. User: bard
 */
public class DependencyTableModel extends AbstractTableModel {

    final String[] columnNames = { GanttLanguage.getInstance().getText("id"),
            GanttLanguage.getInstance().getText("taskname"),
            GanttLanguage.getInstance().getText("type"),
            GanttLanguage.getInstance().getText("delay"),
            GanttLanguage.getInstance().getText("hardness"),};

    private final List myDependencies;

    private final TaskDependencyCollectionMutator myMutator;

    private final Task myTask;

    public DependencyTableModel(Task task) {
        myDependencies = new ArrayList(Arrays.asList(task
                .getDependenciesAsDependant().toArray()));
        myMutator = task.getManager().getDependencyCollection().createMutator();
        myTask = task;
    }

    public void commit() {
        myMutator.commit();
    }

    /**
     * Return the number of colums
     */
    public int getColumnCount() {

        return columnNames.length;

    }

    /**
     * Return the numbers of rows
     */
    public int getRowCount() {
        return myDependencies.size() + 1;
    }

    /**
     * Return the name of the colums col
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Return the object at the specify row & colums
     */
    public Object getValueAt(int row, int col) {
        Object result;

        if (row >= 0 && row < getRowCount()) {
            if (row == myDependencies.size()) {
                result = "";
            } else {
                TaskDependency dep = (TaskDependency) myDependencies.get(row);
                switch (col) {
                case 0: {
                    result = new Integer(dep.getDependee().getTaskID());
                    break;
                }
                case 1: {
                    result = dep.getDependee();
                    break;
                }
                case 2: {
                    result = dep.getConstraint().getName();
                    break;
                }
                case 3: {
                    result = new Integer(dep.getDifference());
                    break;
                }
                case 4: {
                	result = dep.getHardness();
                	break;
                }
                default: {
                    throw new IllegalArgumentException("Illegal column number="
                            + col);
                }
                }

            }
        } else {
            throw new IllegalArgumentException("Illegal row number=" + row);
        }
        return result;
    }

    public boolean isCellEditable(int row, int col) {
        boolean result = col > 0;
        if (result) {
            result = col == 2 ? row < myDependencies.size()
                    : row <= myDependencies.size();
        }
        return result;
    }

    public void setValueAt(Object value, int row, int col) {
        if (row >= 0) {

            try {
                if (row == myDependencies.size()) {
                    createDependency(value);
                } else {
                    updateDependency(value, row, col);
                }
            } catch (TaskDependencyException e) {
            	if (!GPLogger.log(e)) {
            		e.printStackTrace(System.err);
            	}
            }
        } else {
            throw new IllegalArgumentException("I can't set data in row=" + row);
        }

        fireTableCellUpdated(row, col);

    }

    private void updateDependency(Object value, int row, int col)
            throws TaskDependencyException {
        TaskDependency dep = (TaskDependency) myDependencies.get(row);
        switch (col) {
        case 4:
        	dep.setHardness((Hardness) value);
        	break;
        case 3: {
            int loadAsInt = Integer.parseInt(String.valueOf(value));
            dep.setDifference(loadAsInt);
            break;
        }
        case 2: {
            TaskDependencyConstraint clone;
            try {
                clone = (TaskDependencyConstraint) ((ConstraintImpl) value).clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            } 
            dep.setConstraint(clone);
            break;
        }
        case 1: {
            dep.delete();
            myDependencies.remove(row);
            if (value == null) {
                fireTableRowsDeleted(row, row);
            } else {
                Task selectedTask = ((TaskComboItem) value).myTask;
                TaskDependency newDependency = myMutator.createDependency(
                        myTask, selectedTask, new FinishStartConstraintImpl());
                myDependencies.add(newDependency);
            }
        }
        }
    }

    private void createDependency(Object value) throws TaskDependencyException {
        if (value instanceof TaskComboItem) {
            Task selectedTask = ((TaskComboItem) value).myTask;
            TaskDependency dep = myMutator.createDependency(myTask,
                    selectedTask, new FinishStartConstraintImpl());
            myDependencies.add(dep);
            fireTableRowsInserted(myDependencies.size(), myDependencies.size());
        }
    }

    static class TaskComboItem {
        final String myText;

        final Task myTask;

        TaskComboItem(Task task) {
            myTask = task;
            myText = "[#" + task.getTaskID() + "] " + task.getName();
        }

        public String toString() {
            return myText;
        }
    }

}
