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

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.jrctx.JRCTXEditorSupport;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.AntiAliasProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ChartBackgroundImageAlignmentProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ChartBorderVisibleProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ChartBackgroundPaintProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ChartBorderProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ChartPaddingProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.TextAntiAliasProperty;
import com.jaspersoft.ireport.designer.outline.nodes.IRAbstractNode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.chartthemes.simple.ChartSettings;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;


/**
 *
 * @author gtoffoli
 */
public class ChartSettingsNode  extends IRAbstractNode implements PropertyChangeListener {

    private static final String IMAGE_ICON_BASE = "com/jaspersoft/ireport/designer/resources/chartsettings.png";

    private ChartSettings chartSettings = null;

    public ChartSettingsNode(ChartSettings chartSettings, Lookup doLkp)
    {
        super(Children.LEAF, doLkp); //
        this.chartSettings = chartSettings;
        setName("Chart Settings");
        setIconBaseWithExtension(IMAGE_ICON_BASE);
        chartSettings.getEventSupport().addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {

        // Model changed...
        JRCTXEditorSupport ed = getLookup().lookup(JRCTXEditorSupport.class);
        ed.notifyModelChangeToTheView();

        if (evt.getPropertyName() == null) return;

        if (ModelUtils.containsProperty(this.getPropertySets(),evt.getPropertyName()))
        {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
        }
    }


    /**
     *  This is the function to create the sheet...
     *
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        Sheet.Set set = Sheet.createPropertiesSet();
        
        set.put(new ChartBackgroundPaintProperty(getChartSettings()));
//	public static final String PROPERTY_backgroundImage = "backgroundImage";
        set.put(new ChartBackgroundImageAlignmentProperty(getChartSettings()));
//	public static final String PROPERTY_backgroundImageAlpha = "backgroundImageAlpha";
//	public static final String PROPERTY_font = "font";
        set.put(new ChartBorderVisibleProperty(getChartSettings()));
        set.put(new ChartBorderProperty(getChartSettings()));
        set.put(new AntiAliasProperty(getChartSettings()));
        set.put(new TextAntiAliasProperty(getChartSettings()));
        set.put(new ChartPaddingProperty(getChartSettings()));

        sheet.put(set);
        return sheet;
    }

    /**
     * @return the chartSettings
     */
    public ChartSettings getChartSettings() {
        return chartSettings;
    }
}