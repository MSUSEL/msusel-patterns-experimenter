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
package com.jaspersoft.ireport.designer.outline.nodes;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.IReportManager;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitable;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ElementNameVisitor implements JRVisitor {

    private JasperDesign jasperDesign = null;
    private String name = null;
    
    
    /**
     *
     */
    public ElementNameVisitor(JasperDesign jasperDesign)
    {
        this.jasperDesign = jasperDesign;
    }
    
    
    /**
     *
     */
    public String getName(JRVisitable visitable)
    {
        visitable.visit(this);
        
        // At this point name has got his value.
        // We can choose to show the key...
        if (visitable instanceof JRElement)
        {

            JRElement ele = (JRElement)visitable;
            
            if ( ele.getKey() != null && IReportManager.getPreferences().getBoolean("showKeyInReportInspector", false))
            {
                    name = ""+ele.getKey();
            }
            else if (name != null && ele.getKey() != null)
            {
                name += " (" + ele.getKey() + ")";
            }


        }
        
        return name;
    }
    
    
    /**
     *
     */
    public void visitBreak(JRBreak breakElement)
    {
        if (breakElement.getType() == JRBreak.TYPE_PAGE)
            name = "Page Break";
        else
           name = "Column Break";
    }

    /**
     *
     */
    public void visitChart(JRChart chart)
    {
        switch (chart.getChartType())
        {
            case JRChart.CHART_TYPE_AREA: name = "Area"; break;
            case JRChart.CHART_TYPE_BAR: name = "Bar"; break;
            case JRChart.CHART_TYPE_BAR3D: name = "Bar 3D"; break;
            case JRChart.CHART_TYPE_BUBBLE: name = "Bubble"; break;
            case JRChart.CHART_TYPE_CANDLESTICK: name = "Candlestick"; break;
            case JRChart.CHART_TYPE_HIGHLOW: name = "High Low"; break;
            case JRChart.CHART_TYPE_LINE: name = "Line"; break;
            case JRChart.CHART_TYPE_METER: name = "Meter"; break;
            case JRChart.CHART_TYPE_MULTI_AXIS: name = "Multi Axis"; break;
            case JRChart.CHART_TYPE_PIE: name = "Pie"; break;
            case JRChart.CHART_TYPE_PIE3D: name = "Pie 3D"; break;
            case JRChart.CHART_TYPE_SCATTER: name = "Scatter"; break;
            case JRChart.CHART_TYPE_STACKEDBAR: name = "Stackedbar"; break;
            case JRChart.CHART_TYPE_STACKEDBAR3D: name = "Stackedbar 3D"; break;
            case JRChart.CHART_TYPE_THERMOMETER: name = "Thermometer"; break;
            case JRChart.CHART_TYPE_TIMESERIES: name = "Timeseries"; break;
            case JRChart.CHART_TYPE_XYAREA: name = "XY Area"; break;
            case JRChart.CHART_TYPE_XYBAR: name = "XY Bar"; break;
            case JRChart.CHART_TYPE_XYLINE: name = "XY Line"; break;
            case JRChart.CHART_TYPE_STACKEDAREA: name = "Stackedarea"; break; 
            default: name = "Unknown";
        }

        name += " Chart";
    }

    /**
     *
     */
    public void visitCrosstab(JRCrosstab crosstab)
    {
        name = "[" + crosstab.getX() + ", " + crosstab.getY() + ", " + crosstab.getWidth() + ", " + crosstab.getHeight() + "]";
    }

    /**
     *
     */
    public void visitElementGroup(JRElementGroup elementGroup)
    {
        name = null;
    }

    /**
     *
     */
    public void visitEllipse(JREllipse ellipse)
    {
        name = "[" + ellipse.getX() + ", " + ellipse.getY() + ", " + ellipse.getWidth() + ", " + ellipse.getHeight() + "]";
    }

    /**
     *
     */
    public void visitFrame(JRFrame frame)
    {
        name = "[" + frame.getX() + ", " + frame.getY() + ", " + frame.getWidth() + ", " + frame.getHeight() + "]";
    }

    /**
     *
     */
    public void visitImage(JRImage image)
    {
        name = Misc.getExpressionText( image.getExpression() );
        if (name.length() > 15)
        {
            name = name.substring(0,15) + "...";
        }
    }

    /**
     *
     */
    public void visitLine(JRLine line)
    {
        name = "[" + line.getX() + ", " + line.getY() + ", " + line.getWidth() + ", " + line.getHeight() + "]";
    }

    /**
     *
     */
    public void visitRectangle(JRRectangle rectangle)
    {
        name = "[" + rectangle.getX() + ", " + rectangle.getY() + ", " + rectangle.getWidth() + ", " + rectangle.getHeight() + "]";
    }

    /**
     *
     */
    public void visitStaticText(JRStaticText staticText)
    {
        // Take just few characters...
        String s = staticText.getText();
        if (s.length() > 15)
        {
            s = s.substring(0,15) + "...";
        }
        name = staticText.getText();
    }

    /**
     *
     */
    public void visitSubreport(JRSubreport subreport)
    {
        name = Misc.getExpressionText( subreport.getExpression() );
        if (name.length() > 15)
        {
            name = name.substring(0,15) + "...";
        }
    }

    /**
     *
     */
    public void visitTextField(JRTextField textField)
    {
        name = Misc.getExpressionText( textField.getExpression() );
        
        if (name.length() > 15)
        {
            name = name.substring(0,15) + "...";
        }
    }

    public void visitComponentElement(JRComponentElement component) {
        name = "Component";
    }

    public void visitGenericElement(JRGenericElement arg0) {
        name = "Generic element";
    }
}
