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
package com.jaspersoft.ireport.designer.jrctx.nodes.properties;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.SeriesColorsProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import net.sf.jasperreports.chartthemes.simple.ColorProvider;
import net.sf.jasperreports.chartthemes.simple.PlotSettings;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
    
    
/**
 *
 */
public final class PlotSeriesColorsProperty extends SeriesColorsProperty 
{
    private final PlotSettings settings;

    @SuppressWarnings("unchecked")
    public PlotSeriesColorsProperty(PlotSettings settings)
    {
        super(JRBaseChartPlot.PROPERTY_SERIES_COLORS, 
              "Series Colors",
              "Series Colors");
        this.settings = settings;
    }

    @Override
    public Object getValue()
    {
        List list = new ArrayList();
        if (settings.getSeriesColorSequence() != null)
        {
            for(int i = 0; i < settings.getSeriesColorSequence().size(); i++)
            {
                list.add(
                    new JRBaseChartPlot.JRBaseSeriesColor(i, ((ColorProvider)settings.getSeriesColorSequence().get(i)).getColor())
                    
                    );
            }
        }
        return list;
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        setPropertyValue(val);
    }

    @SuppressWarnings("unchecked")
    private void setPropertyValue(Object val)
    {
        if (val instanceof List)
        {
            List oldValue = (List)settings.getSeriesColorSequence();
            
            List colors = new ArrayList();
            if (oldValue == val) return;
            if (oldValue == null && val == null) return;

            // Check for changes...



            if (val != null)
            {
                boolean update = false;
                if (oldValue ==null || oldValue.size() != ((List)val).size())
                {
                    update = true;
                }
                else
                {
                    ;
                    for(int idx = 0; !update && idx < ((List)val).size(); ++idx)
                    {

                        Color c1 = ((JRBaseChartPlot.JRBaseSeriesColor)((List)val).get(idx)).getColor();
                        Color c2 = ((ColorProvider)oldValue.get(idx)).getColor();

                        if (c1 == null && c1 != c2)
                        {
                            update = true;
                        }
                        else if (c1 != null && !c1.equals(c2))
                        {
                            update = true;
                        }
                    }
                }

                if (!update) return;
                
                for(Iterator it = ((List)val).iterator(); it.hasNext();)
                {
                    colors.add(new ColorProvider(((JRBaseChartPlot.JRBaseSeriesColor)it.next()).getColor()));
                }
            }
            settings.setSeriesColorSequence(colors);
            
            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        settings,
                        "SeriesColorSequence",
                        List.class,
                        oldValue,colors);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }
}
