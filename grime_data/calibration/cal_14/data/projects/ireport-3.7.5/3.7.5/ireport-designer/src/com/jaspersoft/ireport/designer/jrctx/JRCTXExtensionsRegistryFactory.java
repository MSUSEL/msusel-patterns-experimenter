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
package com.jaspersoft.ireport.designer.jrctx;

import java.util.Collections;
import java.util.List;
import net.sf.jasperreports.charts.ChartTheme;
import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;
import net.sf.jasperreports.chartthemes.simple.SimpleChartTheme;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRCTXExtensionsRegistryFactory implements ExtensionsRegistryFactory, ChartThemeBundle 
{
    private static final ThreadLocal threadLocal = new ThreadLocal();
    
    public static void setChartThemeSettings(ChartThemeSettings chartThemeSettings)
    {
        threadLocal.set(chartThemeSettings);
    }

    public String[] getChartThemeNames()
    {
        return new String[0];
    }

    public ChartTheme getChartTheme(String themeName)
    {
        ChartThemeSettings settings = (ChartThemeSettings)threadLocal.get();
        if (settings != null)
        {
            return new SimpleChartTheme(settings);
        }
        return null;
    }

    private final ExtensionsRegistry extensionsRegistry = 
        new ExtensionsRegistry()
        {
            public List getExtensions(Class extensionType) 
            {
                if (ChartThemeBundle.class.equals(extensionType))
                {
                    return Collections.singletonList(JRCTXExtensionsRegistryFactory.this);
                }
                return null;
            }
        };

    public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) 
    {
        return extensionsRegistry;
    }
}
