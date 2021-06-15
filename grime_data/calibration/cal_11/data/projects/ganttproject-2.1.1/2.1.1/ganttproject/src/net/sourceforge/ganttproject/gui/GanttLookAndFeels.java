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
 * Created on 17.01.2004
 *
 */
package net.sourceforge.ganttproject.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * @author Michael Haeusler (michael at akatose.de) This singleton class stores
 *         info about the installed LookAndFeels.
 */
public class GanttLookAndFeels {

    protected Map infoByClass;

    protected Map infoByName;

    protected static GanttLookAndFeels singleton;

    static {
        // static initializer:
        // installing the bundled LookAndFeels to the javax.UIManager

        // Plastic must be installed manually
        UIManager.installLookAndFeel("Plastic",
                "com.jgoodies.plaf.plastic.PlasticLookAndFeel");
    }

    protected GanttLookAndFeels() {
        infoByClass = new HashMap();
        infoByName = new HashMap();
        LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < lookAndFeels.length; i++) {
            GanttLookAndFeelInfo info = new GanttLookAndFeelInfo(
                    lookAndFeels[i]);
            addLookAndFeel(info);
        }
    }

    protected void addLookAndFeel(GanttLookAndFeelInfo info) {
        if (info.getName().startsWith("Kunststoff")
                && System.getProperty("os.name").startsWith("Mac")) {
            System.err
                    .println("LookAndFeel not added (Kunststoff is ignored on MacOS).");
        } else {
            if (!infoByClass.containsKey(info.getClassName())) {
                infoByClass.put(info.getClassName(), info);
                infoByName.put(info.getName(), info);
            } else {
                System.err.println("LookAndFeel " + info + "("
                        + info.getClassName() + ") already installed.");
            }
        }
    }

    public GanttLookAndFeelInfo getInfoByClass(String className) {
        return ((GanttLookAndFeelInfo) infoByClass.get(className));
    }

    public GanttLookAndFeelInfo getInfoByName(String name) {
        return ((GanttLookAndFeelInfo) infoByName.get(name));
    }

    public GanttLookAndFeelInfo getDefaultInfo() {
        GanttLookAndFeelInfo info = getInfoByClass(UIManager
                .getSystemLookAndFeelClassName());
        if (null == info)
            info = getInfoByClass(UIManager
                    .getCrossPlatformLookAndFeelClassName());
        return info;
    }

    public GanttLookAndFeelInfo[] getInstalledLookAndFeels() {
        GanttLookAndFeelInfo[] lookAndFeels = new GanttLookAndFeelInfo[0];
        return ((GanttLookAndFeelInfo[]) infoByClass.values().toArray(
                lookAndFeels));
    }

    public static GanttLookAndFeels getGanttLookAndFeels() {
        if (singleton == null) {
            singleton = new GanttLookAndFeels();
        }
        return singleton;
    }
}
