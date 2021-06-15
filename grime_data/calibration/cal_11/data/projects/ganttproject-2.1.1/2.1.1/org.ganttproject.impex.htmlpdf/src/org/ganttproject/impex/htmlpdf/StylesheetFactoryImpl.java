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
 * Created on 22.09.2005
 */
package org.ganttproject.impex.htmlpdf;

import java.net.URL;

import net.sourceforge.ganttproject.GPLogger;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

abstract class StylesheetFactoryImpl {
    Stylesheet[] createStylesheets(Class stylesheetInterface) {
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IConfigurationElement[] configElements = extensionRegistry
                .getConfigurationElementsFor(stylesheetInterface.getName());
        Stylesheet[] result = (Stylesheet[])java.lang.reflect.Array.newInstance(
                stylesheetInterface, configElements.length);
        for (int i = 0; i < configElements.length; i++) {
            try {
                //Object nextExtension = configElements[i].createExecutableExtension("class");
                //assert nextExtension!=null && nextExtension instanceof HTMLStylesheet : "Extension="+nextExtension+" is expected to be instance of HTMLStylesheet";
                String localizedName = configElements[i].getAttribute("name");
                String pluginRelativeUrl = configElements[i].getAttribute("url");
                String namespace = configElements[i].getDeclaringExtension().getNamespace();
                URL stylesheetUrl = Platform.getBundle(namespace).getResource(pluginRelativeUrl);
                assert stylesheetUrl!=null : "Failed to resolve url="+pluginRelativeUrl;
                URL resolvedUrl = Platform.resolve(stylesheetUrl);
                assert resolvedUrl!=null : "Failed to resolve URL="+stylesheetUrl;
                result[i] = newStylesheet(resolvedUrl, localizedName);
            }
            catch(Exception e) {
            	if (!GPLogger.log(e)) {
            		e.printStackTrace(System.err);
            	}
            }
        }
        return result;
    }

    protected abstract Stylesheet newStylesheet(URL url, String localizedName);
}
