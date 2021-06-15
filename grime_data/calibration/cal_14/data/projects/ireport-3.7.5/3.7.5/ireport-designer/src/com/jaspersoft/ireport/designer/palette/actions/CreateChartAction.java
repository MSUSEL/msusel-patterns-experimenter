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
package com.jaspersoft.ireport.designer.palette.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.charts.ChartSelectionJDialog;
import com.jaspersoft.ireport.designer.charts.datasets.wizards.CategoryDatasetWizardAction;
import com.jaspersoft.ireport.designer.charts.datasets.wizards.PieWizardAction;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.JOptionPane;
import net.sf.jasperreports.charts.design.JRDesignCategoryDataset;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Scene;
import org.openide.util.Mutex;
import org.openide.util.actions.SystemAction;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CreateChartAction extends CreateReportElementAction 
{

    public JRDesignElement createReportElement(JasperDesign jd)
    {
        JRDesignElement element = null;

        if (getScene() instanceof CrosstabObjectScene)
        {
            Runnable r = new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(Misc.getMainFrame(), "You can not use a chart inside a crosstab","Error", JOptionPane.WARNING_MESSAGE);
                }
            };
            
            Mutex.EVENT.readAccess(r); 
            return null;
        }
        
        Object pWin = Misc.getMainWindow();
        ChartSelectionJDialog dialog = null;
        if (pWin instanceof Dialog) dialog = new ChartSelectionJDialog((Dialog)pWin, true);
        else dialog = new ChartSelectionJDialog((Frame)pWin, true);

        dialog.setJasperDesign(jd);
        dialog.setVisible(true);
        
        if (dialog.getDialogResult() == JOptionPane.OK_OPTION)
        {
            element = dialog.getChart();
            element.setWidth(200);
            element.setHeight(100);

            if ( ((JRDesignChart)element).getDataset() instanceof JRDesignPieDataset)
            {
                SystemAction.get(PieWizardAction.class).configureChart(  (JRDesignPieDataset)((JRDesignChart)element).getDataset()  );
            }
            else if ( ((JRDesignChart)element).getDataset() instanceof JRDesignCategoryDataset)
            {
                SystemAction.get(CategoryDatasetWizardAction.class).configureChart(  (JRDesignCategoryDataset)((JRDesignChart)element).getDataset(), ((JRDesignChart)element).getChartType()  );
            }

            String s = IReportManager.getPreferences().get("DefaultTheme","");
            if (s.length() > 0)
            {
                if (getJasperDesign().getPropertiesMap().getProperty("net.sf.jasperreports.chart.theme") == null)
                {
                    getJasperDesign().getPropertiesMap().setProperty("net.sf.jasperreports.chart.theme", s);
                }
            }
        }



        return element;
    }


    @Override
    public void drop(DropTargetDropEvent dtde) {

        JRDesignChart element = (JRDesignChart)createReportElement(getJasperDesign());

        if (element == null) return;
        // Find location...
        dropFieldElementAt(getScene(), getJasperDesign(), element, dtde.getLocation());
    }

    public void dropFieldElementAt(Scene theScene, JasperDesign jasperDesign, JRDesignChart element, Point location)
    {
        if (theScene instanceof ReportObjectScene)
        {
            Point p = theScene.convertViewToScene( location );

            // find the band...
            JRDesignBand b = ModelUtils.getBandAt(jasperDesign, p);
            int yLocation = ModelUtils.getBandLocation(b, jasperDesign);
            Point pLocationInBand = new Point(p.x - jasperDesign.getLeftMargin(),
                                              p.y - yLocation);
            if (b != null)
            {

                // if the band is not a detail, propose to aggregate the value...
                if (b.getOrigin().getBandType() == JROrigin.TITLE)
                {
                    element.setEvaluationTime(JRExpression.EVALUATION_TIME_REPORT);
                }
            }

        }

        super.dropElementAt(theScene, jasperDesign, element, location);
    }
    
}
