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
package com.jaspersoft.ireport.designer.standalone;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.standalone.actions.ImportSettingsAction;
import com.jaspersoft.ireport.designer.standalone.actions.ImportSettingsUtilities;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.modules.ModuleInstall;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent.Registry;
import org.openide.windows.WindowManager;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        // By default, do nothing.
        // Put your startup code here.
        WindowManager.getDefault().getRegistry().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals( Registry.PROP_TC_CLOSED))
                {
                    System.gc();
                }
            }
        });

        // Import settings...
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

            public void run() {
                String[] versions = ImportSettingsUtilities.getAvailableVersions();
                if (versions != null && versions.length > 0 && IReportManager.getPreferences().getBoolean("askForImportingSettings", true))
                {
                    try {
                        SystemAction.get(ImportSettingsAction.class).performAction();
                    } catch (Exception ex)
                    {}
                    IReportManager.getPreferences().putBoolean("askForImportingSettings", false);
                }
            }
        });
    }
}
