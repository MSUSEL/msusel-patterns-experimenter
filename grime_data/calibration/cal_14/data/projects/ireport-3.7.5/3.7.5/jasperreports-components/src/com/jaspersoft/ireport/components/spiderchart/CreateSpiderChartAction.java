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
package com.jaspersoft.ireport.components.spiderchart;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.palette.actions.*;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.components.spiderchart.StandardSpiderDataset;
import net.sf.jasperreports.components.spiderchart.StandardSpiderPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import org.netbeans.api.visual.widget.Scene;


/**
 *
 * @author gtoffoli
 */
public class CreateSpiderChartAction extends CreateReportElementAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jd) {




        JRDesignComponentElement component = new JRDesignComponentElement();
        SpiderChartComponent componentImpl = new SpiderChartComponent();

        StandardSpiderDataset spiderDataset = new StandardSpiderDataset();
        StandardChartSettings chartSettings = new StandardChartSettings();
        StandardSpiderPlot spiderPlot = new StandardSpiderPlot();

        
        //chartSettings.setC

        /*
        JRDesignDataset newDataset = new JRDesignDataset(false);
        String name = "spiderChartDataset";
        for (int i = 1;; i++) {
            if (!jd.getDatasetMap().containsKey(name + i)) {
                newDataset.setName(name + i);
                break;
            }
        }
        try {
            jd.addDataset(newDataset);
        } catch (JRException ex) {
            //Exceptions.printStackTrace(ex);
        }

        JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();
        datasetRun.setDatasetName(newDataset.getName());

        JRDesignExpression exp = new JRDesignExpression();
        exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");//NOI18N
        exp.setText("new net.sf.jasperreports.engine.JREmptyDataSource(0)");//NOI18N

        datasetRun.setDataSourceExpression(exp);

        spiderDataset.setDatasetRun(datasetRun);
        */
        componentImpl.setDataset(spiderDataset);
        componentImpl.setChartSettings(chartSettings);
        componentImpl.setPlot(spiderPlot);

        spiderPlot.setWebFilled(Boolean.TRUE);

        component.setComponent(componentImpl);
        component.setComponentKey(new ComponentKey(
                                    "http://jasperreports.sourceforge.net/jasperreports/components",
                                    "sc", "spiderChart"));

        component.setWidth(200);
        component.setHeight(200);

        return component;
    }


    @Override
    public void drop(DropTargetDropEvent dtde) {

        JRDesignComponentElement element = (JRDesignComponentElement)createReportElement(getJasperDesign());

        if (element == null) return;
        // Find location...
        dropFieldElementAt(getScene(), getJasperDesign(), element, dtde.getLocation());
    }


    public void dropFieldElementAt(Scene theScene, JasperDesign jasperDesign, JRDesignComponentElement element, Point location)
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
                if (b.getOrigin().getBandTypeValue() == BandTypeEnum.TITLE)
                {
                    ((SpiderChartComponent)(element.getComponent())).setEvaluationTime(EvaluationTimeEnum.REPORT);
                }
            }
       }

        super.dropElementAt(theScene, jasperDesign, element, location);
    }

    
}
