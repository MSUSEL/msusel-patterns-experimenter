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

import com.jaspersoft.ireport.components.spiderchart.properties.AxisLineColorProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.AxisLineWidthProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.BackgroundAlphaProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.BackgroundColorProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.ChartBackgroundColorProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.CustomizerClassProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.EvaluationGroupProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.EvaluationTimeProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.ForegroundAlphaProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.HeadPercentProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.InteriorGapProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.LabelColorProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.LabelFontProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.LabelGapProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.LegendBackgroundColorProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.LegendColorProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.LegendFontProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.LegendPositionProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.MaxValueExpressionProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.RenderTypeProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.RotationProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.ShowLegendProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.StartAngleProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.SubtitleColorProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.SubtitleExpressionProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.SubtitleFontProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.TableOrderProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.TitleColorProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.TitleExpressionProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.TitleFontProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.TitlePositionProperty;
import com.jaspersoft.ireport.components.spiderchart.properties.WebFilledProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.components.spiderchart.StandardSpiderPlot;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author gtoffoli
 */
public class SpiderChartElementNode extends ElementNode {

    public SpiderChartElementNode(JasperDesign jd, JRDesignElement element, Lookup doLkp)
    {
        super(jd, element,doLkp);
        setIconBaseWithExtension("com/jaspersoft/ireport/components/spiderchart/spiderchart-16.png");
        
    }

    @Override
    public String getDisplayName() {
        return I18n.getString("SpiderChartElementNode.name");
    }




    
    @Override
    public Action[] getActions(boolean popup) {

        List<Action> actions = new ArrayList<Action>();
        Action[] originalActions = super.getActions(popup);

        actions.add(SystemAction.get(EditChartDataAction.class));
        actions.add(SystemAction.get(SpiderChartHyperlinkAction.class));
        
        for (int i=0; i<originalActions.length; ++i)
        {
            actions.add(originalActions[i]);
        }
        return actions.toArray(new Action[actions.size()]);
    }
    

    @Override
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        
        // adding common properties...
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("SpiderChart");
        SpiderChartComponent component = (SpiderChartComponent)( (JRDesignComponentElement)getElement()).getComponent();
        propertySet.setDisplayName(I18n.getString("SpiderChart"));

        JRDesignDataset dataset = ModelUtils.getElementDataset(getElement(), getJasperDesign());

        //propertySet.put(new ImageEvaluationTimeProperty( element,dataset));
        //propertySet.put(new EvaluationGroupProperty( element,dataset));

        StandardChartSettings chartSettings = (StandardChartSettings) component.getChartSettings();

        propertySet.put(new EvaluationTimeProperty(component,dataset));
        propertySet.put(new EvaluationGroupProperty(component,dataset));

        propertySet.put(new TitleExpressionProperty(chartSettings,dataset));
        propertySet.put(new TitleFontProperty(chartSettings, getJasperDesign()));
        propertySet.put(new TitleColorProperty(chartSettings));
        propertySet.put(new TitlePositionProperty(chartSettings));
        propertySet.put(new SubtitleExpressionProperty(chartSettings,dataset));
        propertySet.put(new SubtitleFontProperty(chartSettings, getJasperDesign()));
        propertySet.put(new SubtitleColorProperty(chartSettings));
        propertySet.put(new ShowLegendProperty(chartSettings));
        propertySet.put(new LegendFontProperty(chartSettings, getJasperDesign()));
        propertySet.put(new LegendColorProperty(chartSettings));
        propertySet.put(new LegendBackgroundColorProperty(chartSettings));
        propertySet.put(new LegendPositionProperty(chartSettings));
        propertySet.put(new ChartBackgroundColorProperty(chartSettings));
        propertySet.put(new RenderTypeProperty(chartSettings));
        propertySet.put(new CustomizerClassProperty(chartSettings));


        sheet.put( propertySet );

        StandardSpiderPlot spiderPlot = (StandardSpiderPlot) component.getPlot();

        Sheet.Set plotPropertySet = Sheet.createPropertiesSet();

        plotPropertySet.setName("SpiderChartPlot");
        plotPropertySet.setDisplayName(I18n.getString("SpiderChartPlot"));

        plotPropertySet.put(new MaxValueExpressionProperty(spiderPlot, dataset));

        
        plotPropertySet.put(new RotationProperty(spiderPlot));
        plotPropertySet.put(new TableOrderProperty(spiderPlot));
        plotPropertySet.put(new WebFilledProperty(spiderPlot));
        plotPropertySet.put(new StartAngleProperty(spiderPlot));
        plotPropertySet.put(new HeadPercentProperty(spiderPlot));
        plotPropertySet.put(new InteriorGapProperty(spiderPlot));

        plotPropertySet.put(new AxisLineColorProperty(spiderPlot));
        plotPropertySet.put(new AxisLineWidthProperty(spiderPlot));

        plotPropertySet.put(new LabelFontProperty(spiderPlot));
        plotPropertySet.put(new LabelColorProperty(spiderPlot));
        plotPropertySet.put(new LabelGapProperty(spiderPlot));

        plotPropertySet.put(new BackgroundColorProperty(spiderPlot));
        plotPropertySet.put(new BackgroundAlphaProperty(spiderPlot));
        plotPropertySet.put(new ForegroundAlphaProperty(spiderPlot));

        sheet.put( plotPropertySet );
        /*
        set.put(new BarbecueCodeExpressionProperty(component, dataset));
        set.put(new BarbecueEvaluationTimeProperty(component, dataset));//, dataset));
        set.put(new BarbecueEvaluationGroupProperty(component, dataset));
        set.put(new BarbecueBarWidthProperty(component) );
        set.put(new BarbecueBarHeightProperty(component) );
        set.put(new BarbecueDrawTextProperty(component) );
        set.put(new BarbecueChecksumRequiredProperty(component) );
        set.put(new BarbecueApplicationIdentifierExpressionProperty(component, ModelUtils.getElementDataset(getElement(), getJasperDesign())));
        */
        
        return sheet;
    }
 

}
