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
package net.sourceforge.ganttproject.plugins;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.chart.Chart;
import net.sourceforge.ganttproject.export.Exporter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * Very basic Plugin Manager
 * @author bbaranne
 *
 */
public class PluginManager {

    private static final String EXTENSION_POINT_ID_CHART = "net.sourceforge.ganttproject.chart";

    private static final String EXTENSION_POINT_ID_EXPORTER = "net.sourceforge.ganttproject.exporter";

    private Chart[] myCharts;

    private Exporter[] myExporters;

    public Object[] getExtensions(Class extensionPointInterface) {
        String extensionPointID = extensionPointInterface.getName();
        return getExtensions(extensionPointID, extensionPointInterface);
    }
    
    public Object[] getExtensions(String extensionPointID, Class extensionPointInterface) {
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IConfigurationElement[] configElements = extensionRegistry
                .getConfigurationElementsFor(extensionPointID);
        
        ArrayList extensions = new ArrayList();
        for (int i = 0; i < configElements.length; i++) {
            try {
                Object nextExtension = configElements[i]
                        .createExecutableExtension("class");
                assert nextExtension!=null && extensionPointInterface.isAssignableFrom(nextExtension.getClass());
                extensions.add(nextExtension);
            } catch (CoreException e) {
            	if (!GPLogger.log(e)) {
            		e.printStackTrace(System.err);
            	}
            }
        }
        return extensions.toArray((Object[])Array.newInstance(extensionPointInterface, 0));
        
    }
    
    public Chart[] getCharts() {
        if (myCharts == null) {
            myCharts = (Chart[]) getExtensions(EXTENSION_POINT_ID_CHART, Chart.class);
        }
        return myCharts;
    }

    public Exporter[] getExporters() {
        if (myExporters == null) {
            myExporters = (Exporter[]) getExtensions(EXTENSION_POINT_ID_EXPORTER, Exporter.class);
        }
        return myExporters;

    }
}
