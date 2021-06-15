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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.ganttproject.delay.DelayManager;
import net.sourceforge.ganttproject.gui.options.model.ChangeValueDispatcher;
import net.sourceforge.ganttproject.plugins.PluginManager;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.task.CustomColumnsManager;
import net.sourceforge.ganttproject.task.CustomColumnsStorage;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.TaskSelectionManager;
import net.sourceforge.ganttproject.undo.GPUndoManager;

/**
 * This class allow the developer to get some useful references. - GanttProject
 * reference; - CustomColumnManager reference; - CustomColumnStorage reference.
 * 
 * @author bbaranne Mar 2, 2005
 */
public class Mediator {
    /**
     * The unique GanttProject instance.
     */
    private static GanttProject ganttprojectSingleton = null;

    /**
     * The unique CustomColumnsManager instance.
     */
    private static CustomColumnsManager customColumnsManager = null;

    private static CustomColumnsStorage customColumnsStorage = null;

    private static TaskSelectionManager taskSelectionManager = null;

    private static RoleManager roleManager = null;

    private static TaskManager taskManager = null;

    private static GPUndoManager undoManager = null;

    private static DelayManager delayManager = null;

    private static PluginManager pluginManager = new PluginManager();
    
    private static List changeValueDispatchers = new ArrayList();

    /**
     * Regsiters the unique GanttProject instance.
     * 
     * @param gp
     *            The unique GanttProject instance.
     */
    public static void registerGanttProject(GanttProject gp) {
        ganttprojectSingleton = gp;
    }

    /**
     * Regsiters the unique CustomColumnsManager instance.
     * 
     * @param managerThe
     *            unique CustomColumnsManager instance.
     */
//    public static void registerCustomColumnsManager(CustomColumnsManager manager) {
//        customColumnsManager = manager;
//    }
//
//    public static void registerCustomColumnsStorage(CustomColumnsStorage storage) {
//        customColumnsStorage = storage;
//    }
//
    public static void registerTaskSelectionManager(
            TaskSelectionManager taskSelection) {
        taskSelectionManager = taskSelection;
    }

//    public static void registerRoleManager(RoleManager roleMgr) {
//        roleManager = roleMgr;
//    }
//
//    public static void registerTaskManager(TaskManager taskMgr) {
//        taskManager = taskMgr;
//    }

    public static void registerUndoManager(GPUndoManager undoMgr) {
        undoManager = undoMgr;
    }

    public static void registerDelayManager(DelayManager delayMgr) {
        delayManager = delayMgr;
    }
    
    public static void addChangeValueDispatcher(ChangeValueDispatcher dispatcher){
        changeValueDispatchers.add(dispatcher);
    }

    /**
     * Returns the unique GanttProject instance.
     * 
     * @return The unique GanttProject instance.
     */
    public static GanttProject getGanttProjectSingleton() {
        return ganttprojectSingleton;
    }

    /**
     * Returns the unique CustomColumnsStorage instance.
     * 
     * @return The unique CustomColumnsStorage instance.
     */
//    public static CustomColumnsStorage getCustomColumnsStorage() {
//        return customColumnsStorage;
//    }

    /**
     * Returns the unique CustomColumnsManager instance.
     * 
     * @return The unique CustomColumnsManager instance.
     */
//    public static CustomColumnsManager getCustomColumnsManager() {
//        return customColumnsManager;
//    }

    public static TaskSelectionManager getTaskSelectionManager() {
        return taskSelectionManager;
    }

//    public static RoleManager getRoleManager() {
//        return roleManager;
//    }

//    public static TaskManager getTaskManager() {
//        return taskManager;
//    }

    public static GPUndoManager getUndoManager() {
        return undoManager;
    }

    public static DelayManager getDelayManager() {
        return delayManager;
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }
    
    public static List getChangeValueDispatchers(){
        return changeValueDispatchers;
    }
}
