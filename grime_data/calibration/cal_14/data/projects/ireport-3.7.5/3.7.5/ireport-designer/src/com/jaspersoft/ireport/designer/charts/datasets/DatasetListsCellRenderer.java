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
package com.jaspersoft.ireport.designer.charts.datasets;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignGanttSeries;
import net.sf.jasperreports.charts.design.JRDesignPieSeries;
import net.sf.jasperreports.charts.design.JRDesignTimePeriodSeries;
import net.sf.jasperreports.charts.design.JRDesignTimeSeries;
import net.sf.jasperreports.charts.design.JRDesignXySeries;
import net.sf.jasperreports.charts.design.JRDesignXyzSeries;

/**
 *
 * @author gtoffoli
 */
public class DatasetListsCellRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
     {
         JLabel label = (JLabel)super.getListCellRendererComponent(list,value,index,isSelected, cellHasFocus);
         label.setIcon(null);
         
         if (value instanceof JRDesignTimePeriodSeries)
         {
                  label.setText( "Time period series [" + Misc.getExpressionText( ((JRDesignTimePeriodSeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignCategorySeries)
         {
                  label.setText( "Category series [" + Misc.getExpressionText( ((JRDesignCategorySeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignXySeries)
         {
                  label.setText( "XY series [" + Misc.getExpressionText( ((JRDesignXySeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignTimeSeries)
         {
                  label.setText( "Time series [" + Misc.getExpressionText( ((JRDesignTimeSeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignXyzSeries)
         {
                  label.setText( "XYZ series [" + Misc.getExpressionText( ((JRDesignXyzSeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignGanttSeries)
         {
                  label.setText( "Gantt series [" + Misc.getExpressionText( ((JRDesignGanttSeries)value).getSeriesExpression() ) +"]");
         }
         else if (value instanceof JRDesignPieSeries)
         {
                  if (list.getModel().getSize() <= 1)
                  {
                      label.setText( "Default pie series");
                  }
                  else
                  {
                      label.setText( "Pie series [" + (index+1) + "]");
                  }
         }
         
         return this;
     }
    
    
}
