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
 * Created on 25.04.2005
 */
package net.sourceforge.ganttproject.application;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.GanttProject;

import org.eclipse.core.runtime.IPlatformRunnable;

/**
 * @author bard
 */
public class MainApplication implements IPlatformRunnable {
    private Object myLock = new Object();

    // The hack with waiting is necessary because when you
    // launch Runtime Workbench in Eclipse, it exists as soon as
    // GanttProject.main() method exits
    // without Eclipse, Swing thread continues execution. So we wait until main
    // window closes
    public Object run(Object args) throws Exception {
        Thread.currentThread().setContextClassLoader(
                getClass().getClassLoader());
        GPLogger.setup();
        String[] cmdLine = (String[]) args;
        WindowAdapter closingListener = new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                myLock.notify();
            }
        };
        GanttProject.setWindowListener(closingListener);
        GanttProject.main(cmdLine);
        synchronized (myLock) {
            myLock.wait();
        }
        return null;
    }

}
