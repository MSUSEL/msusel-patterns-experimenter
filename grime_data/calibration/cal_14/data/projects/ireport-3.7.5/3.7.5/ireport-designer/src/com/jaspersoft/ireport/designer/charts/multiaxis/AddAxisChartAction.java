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
package com.jaspersoft.ireport.designer.charts.multiaxis;

import com.jaspersoft.ireport.designer.charts.ChartSelectionJDialog;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.JOptionPane;
import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.charts.design.JRDesignMultiAxisPlot;
import net.sf.jasperreports.engine.design.JRDesignChart;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class AddAxisChartAction extends NodeAction {

    private static AddAxisChartAction instance = null;
    
    public static synchronized AddAxisChartAction getInstance()
    {
        if (instance == null)
        {
            instance = new AddAxisChartAction();
        }
        
        return instance;
    }
    
    private AddAxisChartAction()
    {
        super();
    }
    
    
    public String getName() {
        return I18n.getString("AddAxisChart.Name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {

        JRDesignChart theChart = (JRDesignChart)((ElementNode)activatedNodes[0]).getElement();
        // Show a window with all the available charts...
        ChartSelectionJDialog cd = new ChartSelectionJDialog(Misc.getMainFrame(), true);
        cd.setMultiAxisMode(true);
        cd.setJasperDesign( ((ElementNode)activatedNodes[0]).getJasperDesign());
        cd.setVisible(true);
        if (cd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            JRDesignChart designChart = cd.getChart();
            JRDesignChartAxis axis = new JRDesignChartAxis(theChart);
            axis.setChart(designChart);
            ((JRDesignMultiAxisPlot)theChart.getPlot()).setChart(theChart);
            ((JRDesignMultiAxisPlot)theChart.getPlot()).addAxis(axis);
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if (!(activatedNodes[0] instanceof ElementNode)) return false;
        ElementNode node = (ElementNode)activatedNodes[0];
        if (node.getElement() instanceof JRDesignChart)
        {
            return ((JRDesignChart)node.getElement()).getChartType() == JRDesignChart.CHART_TYPE_MULTI_AXIS;
        }
        return false;
    }
}