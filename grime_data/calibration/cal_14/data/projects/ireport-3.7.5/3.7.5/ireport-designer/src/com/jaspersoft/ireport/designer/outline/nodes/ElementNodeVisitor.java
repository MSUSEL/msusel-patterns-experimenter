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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.charts.multiaxis.MultiAxisChartElementNode;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
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
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.design.JRDesignBreak;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignEllipse;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Lookup;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ElementNodeVisitor implements JRVisitor {

    public static final String ICON_BASE = "com/jaspersoft/ireport/designer/resources/";
    public static final String ICON_BREAK = ICON_BASE + "break-16.png"; 
    public static final String ICON_CROSSTAB = ICON_BASE + "crosstab-16.png"; 
    public static final String ICON_FRAME = ICON_BASE + "frame-16.png"; 
    public static final String ICON_ELLIPSE = ICON_BASE + "ellipse-16.png"; 
    public static final String ICON_LINE = ICON_BASE + "line-16.png"; 
    public static final String ICON_RECTANGLE = ICON_BASE + "rectangle-16.png";
    public static final String ICON_ROUND_RECTANGLE = ICON_BASE + "roundrectangle-16.png";
    public static final String ICON_IMAGE = ICON_BASE + "image-16.png";
    public static final String ICON_SUBREPORT = ICON_BASE + "subreport-16.png";
    public static final String ICON_STATIC_TEXT = ICON_BASE + "statictext-16.png";
    public static final String ICON_TEXT_FIELD = ICON_BASE + "textfield-16.png";
    public static final String ICON_CHART = ICON_BASE + "chart-16.png";
    public static final String ICON_GENERIC_ELEMENT = ICON_BASE + "genericelement-16.png";

    private JasperDesign jasperDesign = null;
    private ElementNameVisitor nameVisitor = null;
    private IRIndexedNode node = null;
    private Lookup doLkp = null;
    
    
    /**
     *
     */
    public ElementNodeVisitor(JasperDesign jasperDesign, Lookup doLkp)
    {
        this.doLkp = doLkp;
        this.jasperDesign = jasperDesign;
        this.nameVisitor = new ElementNameVisitor(jasperDesign);
    }
    
    
    /**
     *
     */
    public IRIndexedNode getNode(JRVisitable visitable)
    {
        visitable.visit(this);
        node.setDisplayName(nameVisitor.getName(visitable));
        return node;
    }
    
    
    /**
     *
     */
    public void visitBreak(JRBreak breakElement)
    {
        node = new ElementNode(jasperDesign, (JRDesignBreak)breakElement,doLkp);
        node.setIconBaseWithExtension(ICON_BREAK);
    }

    /**
     *
     */
    public void visitChart(JRChart chart)
    {
        if (chart.getChartType() == JRChart.CHART_TYPE_MULTI_AXIS)
        {
            node = new MultiAxisChartElementNode(jasperDesign, (JRDesignChart)chart, doLkp);
        }
        else
        {
            node = new ElementNode(jasperDesign, (JRDesignChart)chart,doLkp);
        }
        node.setIconBaseWithExtension(ICON_CHART);

        ((JRBaseChartPlot)((JRDesignChart)chart).getPlot()).getEventSupport()
                .addPropertyChangeListener((PropertyChangeListener)node);
    }

    /**
     *
     */
    public void visitCrosstab(JRCrosstab crosstab)
    {
        node = new CrosstabNode(jasperDesign, (JRDesignCrosstab)crosstab,doLkp);
        node.setIconBaseWithExtension(ICON_CROSSTAB);
    }

    /**
     *
     */
    public void visitElementGroup(JRElementGroup elementGroup)
    {
        node = new ElementGroupNode(jasperDesign, (JRDesignElementGroup)elementGroup,doLkp);
    }

    /**
     *
     */
    public void visitEllipse(JREllipse ellipse)
    {
        node = new ElementNode(jasperDesign, (JRDesignEllipse)ellipse,doLkp);
        node.setIconBaseWithExtension(ICON_ELLIPSE);
    }

    /**
     *
     */
    public void visitFrame(JRFrame frame)
    {
        node = new FrameNode(jasperDesign,(JRDesignFrame)frame,doLkp);
        node.setIconBaseWithExtension(ICON_FRAME);
    }

    /**
     *
     */
    public void visitImage(JRImage image)
    {
        node = new ElementNode(jasperDesign, (JRDesignImage)image,doLkp);
        node.setIconBaseWithExtension(ICON_IMAGE);
    }

    /**
     *
     */
    public void visitLine(JRLine line)
    {
        node = new ElementNode(jasperDesign, (JRDesignLine)line,doLkp);
        node.setIconBaseWithExtension(ICON_LINE);
    }

    /**
     *
     */
    public void visitRectangle(JRRectangle rectangle)
    {
        node = new ElementNode(jasperDesign, (JRDesignRectangle)rectangle,doLkp);
        
        if (rectangle.getRadius() > 0)
            node.setIconBaseWithExtension(ICON_ROUND_RECTANGLE);
        else
            node.setIconBaseWithExtension(ICON_RECTANGLE);
    }

    /**
     *
     */
    public void visitStaticText(JRStaticText staticText)
    {
        node = new ElementNode(jasperDesign, (JRDesignStaticText)staticText,doLkp);
        node.setIconBaseWithExtension(ICON_STATIC_TEXT);
    }

    /**
     *
     */
    public void visitSubreport(JRSubreport subreport)
    {
        node = new ElementNode(jasperDesign, (JRDesignSubreport)subreport,doLkp);
        node.setIconBaseWithExtension(ICON_SUBREPORT);
    }

    /**
     *
     */
    public void visitTextField(JRTextField textField)
    {
        node = new ElementNode(jasperDesign, (JRDesignTextField)textField,doLkp);
        node.setIconBaseWithExtension(ICON_TEXT_FIELD);
    }

    public void visitComponentElement(JRComponentElement componentElement) {

        node = IReportManager.getComponentNode(jasperDesign, (JRDesignComponentElement)componentElement, doLkp );

        if (node == null)
        {
            node = new ElementNode(jasperDesign, (JRDesignComponentElement)componentElement,doLkp);
            node.setIconBaseWithExtension(ICON_RECTANGLE);
        }
    }

    public void visitGenericElement(JRGenericElement genericElement) {
        node = new ElementNode(jasperDesign, (JRDesignGenericElement)genericElement,doLkp);
        node.setIconBaseWithExtension(ICON_GENERIC_ELEMENT);
    }
    
}
