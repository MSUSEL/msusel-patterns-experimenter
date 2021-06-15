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
package com.jaspersoft.ireport.designer.jrctx.nodes;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
class ChartThemeChildren  extends Children.Keys {

    ChartThemeSettings template = null;
    private Lookup doLkp = null;

    @SuppressWarnings("unchecked")
    public ChartThemeChildren(ChartThemeSettings template, Lookup doLkp) {
        this.template = template;
        this.doLkp=doLkp;
        //this.template.getEventSupport().addPropertyChangeListener(this);
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        updateChildren();
    }


    protected Node[] createNodes(Object key) {

        Node node = null;
        
        if (key.equals("Chart"))
        {
            node = new ChartSettingsNode(template.getChartSettings(), doLkp);
        }
        else if (key.equals("Title"))
        {
            node = new TitleSettingsNode(template.getTitleSettings(), doLkp);
        }
        else if (key.equals("Subtitle"))
        {
            node = new TitleSettingsNode(template.getSubtitleSettings(), doLkp);
        }
        else if (key.equals("Legend"))
        {
            node = new LegendSettingsNode(template.getLegendSettings(), doLkp);
        }
        else if (key.equals("Plot"))
        {
            node = new PlotSettingsNode(template.getPlotSettings(), doLkp);
        }
        else if (key.equals("Domain Axis"))
        {
            node = new AxisSettingsNode(template.getDomainAxisSettings(), doLkp);
        }
        else if (key.equals("Range Axis"))
        {
            node = new AxisSettingsNode(template.getRangeAxisSettings(), doLkp);
        }

        node.setDisplayName(""+key);
        return new Node[]{node};
    }



    @SuppressWarnings("unchecked")
    public void updateChildren()
    {

        List l = new ArrayList();

        l.add( "Chart");
        l.add( "Title");
        l.add( "Subtitle");
        l.add( "Legend");
        l.add( "Plot");
        l.add( "Domain Axis");
        l.add( "Range Axis");

        setKeys(l);
    }

   
    

}
