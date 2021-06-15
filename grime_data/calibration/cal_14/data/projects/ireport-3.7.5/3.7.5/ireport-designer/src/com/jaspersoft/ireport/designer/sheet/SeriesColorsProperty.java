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
package com.jaspersoft.ireport.designer.sheet;

import com.jaspersoft.ireport.designer.sheet.editors.SeriesColorsPropertyEditor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import net.sf.jasperreports.engine.base.JRBaseChartPlot.JRBaseSeriesColor;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author gtoffoli
 */
public class SeriesColorsProperty extends PropertySupport {

    PropertyEditor editor = null;
    
    @SuppressWarnings("unchecked")
    public SeriesColorsProperty(String name,
                       String displayName,
                       String shortDescription)
    {
       super( name, SortedSet.class, displayName,shortDescription, true,true);
       setValue( "canEditAsText", Boolean.FALSE );
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return "";
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new SeriesColorsPropertyEditor();
        }
        return editor;
    }

    public static List sortedSetAsList(SortedSet set)
    {
         List list = new ArrayList();
        list.addAll( set );
        return list;
    }

    public static SortedSet listAsSortedSet(List list)
    {
         SortedSet set = new TreeSet();
         set.addAll( list );
         return set;
    }

    public static boolean isListChanged(List values, SortedSet originalValues)
    {
            if (values == null && originalValues == null)
            {
                return false;
            }

            if (values != null && originalValues != null)
            {
                // Check if the colors are the same...
                List l1 = values;
                List l2 = sortedSetAsList(originalValues);

                boolean areEq = true;
                if (l1.size() != l2.size())
                {
                    areEq = false;
                }
                if (areEq)
                {
                    for (int i=0; areEq && i<l1.size(); ++i)
                    {
                        if (l1.get(i) instanceof JRBaseSeriesColor && l2.get(i) instanceof JRBaseSeriesColor)
                        {
                            JRBaseSeriesColor c1 = (JRBaseSeriesColor)l1.get(i);
                            JRBaseSeriesColor c2 = (JRBaseSeriesColor)l2.get(i);

                            if (c1 == null || c2 == null)
                            {
                                if (c1 != c2)
                                {
                                    areEq = false;
                                }
                            }
                            else
                            {
                                if (c1.getSeriesOrder() != c2.getSeriesOrder())
                                {
                                    areEq = false;
                                }
                                else if (c1.getColor() != c2.getColor() || (c1.getColor() != null && !c1.getColor().equals(c2.getColor()))  )
                                {
                                    areEq = false;
                                }
                            }


                        }
                        else if (!(l1.get(i).equals(l2.get(i))))
                        {
                            areEq = false;
                        }
                    }
                }

                return !areEq;
            }
            return true;
    }
    
}
