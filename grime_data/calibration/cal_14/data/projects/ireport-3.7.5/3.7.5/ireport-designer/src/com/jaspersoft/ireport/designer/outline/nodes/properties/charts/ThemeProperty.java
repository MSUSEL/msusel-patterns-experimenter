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
package com.jaspersoft.ireport.designer.outline.nodes.properties.charts;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportClassLoader;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.StringListProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;

    
/**
 *  Class to manage the JRDesignChart.PROPERTY_EVALUATION_TIME property
 */
public final class ThemeProperty extends StringListProperty implements PreferenceChangeListener
{
        
    private List<Tag> tags = null;
    private final JRDesignChart chart;

    @SuppressWarnings("unchecked")
    public ThemeProperty(JRDesignChart chart)
    {
        super(chart);
        this.chart = chart;
        IReportManager.getPreferences().addPreferenceChangeListener(this);
    }

    @Override
    public String getName()
    {
        return JRChart.PROPERTY_CHART_THEME;
    }

    @Override
    public String getDisplayName()
    {
        return "Theme";
    }

    @Override
    public String getShortDescription()
    {
        return "The theme to use to render the chart.";
    }

    @Override
    public List getTagList()
    {
        if (tags == null)
        {
            updateTags();
        }

        return tags;
    }

    private void updateTags()
    {
        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(IReportManager.getJRExtensionsClassLoader());

        Set<String> themeNamesSet = new HashSet<String>();
        List themeBundles = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(ChartThemeBundle.class);
        for (Iterator it = themeBundles.iterator(); it.hasNext();)
        {
           ChartThemeBundle bundle = (ChartThemeBundle) it.next();
           String[] themeNames = bundle.getChartThemeNames();
           themeNamesSet.addAll(Arrays.asList(themeNames));
        }

        String[] allThemeNames  = themeNamesSet.toArray(new String[themeNamesSet.size()]);
        Arrays.sort(allThemeNames);

        Thread.currentThread().setContextClassLoader(oldCL);

        tags = new ArrayList<Tag>(allThemeNames.length);
        //tags.add(new Tag(null, "Default"));
        for (int i=0; i<allThemeNames.length; ++i)
        {
            tags.add(new Tag(allThemeNames[i]));
        }
    }

    @Override
    public String getString()
    {
        return chart.getTheme();
    }

    @Override
    public String getOwnString()
    {
        return chart.getTheme();
    }

    @Override
    public String getDefaultString()
    {
        return null;
    }

    @Override
    public void setString(String renderType)
    {
        chart.setTheme(renderType);
    }


    public void preferenceChange(PreferenceChangeEvent evt) {

        if (evt == null || evt.getKey() == null || evt.getKey().equals( IReportManager.IREPORT_CLASSPATH))
        {
            // Refresh the array...
            updateTags();
            ((ComboBoxPropertyEditor)getPropertyEditor()).setTagValues(getTagList());
        }
    }
}
