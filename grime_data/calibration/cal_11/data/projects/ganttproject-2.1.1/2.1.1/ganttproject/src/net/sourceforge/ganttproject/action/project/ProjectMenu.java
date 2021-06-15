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
 * Created on 26.09.2005
 */
package net.sourceforge.ganttproject.action.project;

import javax.swing.Action;
import javax.swing.JMenuItem;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.action.GPAction;

public class ProjectMenu {
    private GanttProject myMainFrame;
    private NewProjectAction myNewProjectAction;
    private OpenProjectAction myOpenProjectAction;
    private SaveProjectAction mySaveProjectAction;
    private SaveProjectAsAction mySaveProjectAsAction;
    private OpenURLAction myOpenURLAction;
    private ExitAction myExitAction;
    private GPAction mySaveURLAction;
    private GPAction myPrintAction;
    
    public ProjectMenu(GanttProject mainFrame) {
        myMainFrame = mainFrame;
        myNewProjectAction = new NewProjectAction(mainFrame);
        myOpenProjectAction = new OpenProjectAction(mainFrame);
        mySaveProjectAction =new SaveProjectAction(mainFrame);
        mySaveProjectAsAction =new SaveProjectAsAction(mainFrame);
        myOpenURLAction = new OpenURLAction(mainFrame);
        mySaveURLAction = new SaveURLAction(mainFrame);
        myPrintAction = new PrintAction(mainFrame);
        myExitAction = new ExitAction(mainFrame);
    }
    public GPAction getNewProjectAction() {
        return myNewProjectAction;
    }
    public GPAction getOpenProjectAction() {
        return myOpenProjectAction;
    }
    public GPAction getSaveProjectAction() {
        return mySaveProjectAction;
    }
    public GPAction getSaveProjectAsAction() {
        return mySaveProjectAsAction;
    }
    public GPAction getOpenURLAction() {
        return myOpenURLAction;
    }
    public GPAction getExitAction() {
        return myExitAction;
    }
    public GPAction getSaveURLAction() {
        return mySaveURLAction;
    }
    public GPAction getPrintAction() {
        return myPrintAction;
    }
}
