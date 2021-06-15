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
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.AxisLabelFontProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.AxisLabelPaintProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.AxisLabelVisibleProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.AxisLineProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.AxisLineVisibleProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.TickLabelFontProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.AxisVisibleProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.TickLabelPaintProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.TickLabelsVisibleProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.TickMarksProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.TickMarksVisibleProperty;
import com.jaspersoft.ireport.designer.outline.nodes.IRAbstractNode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.chartthemes.simple.AxisSettings;
import net.sf.jasperreports.engine.base.JRBaseFont;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;


/**
 *
 * @author gtoffoli
 */
public class AxisSettingsNode  extends IRAbstractNode implements PropertyChangeListener {

    private static final String IMAGE_ICON_BASE = "com/jaspersoft/ireport/designer/resources/chartsettings.png";

    private AxisSettings axisSettings = null;

    public AxisSettingsNode(AxisSettings axisSettings, Lookup doLkp)
    {
        super(Children.LEAF, doLkp); //
        this.axisSettings = axisSettings;
        setName("Axis Settings");
        setIconBaseWithExtension(IMAGE_ICON_BASE);
        axisSettings.getEventSupport().addPropertyChangeListener(this);
        ((JRBaseFont)axisSettings.getLabelFont()).getEventSupport().addPropertyChangeListener(this);
        ((JRBaseFont)axisSettings.getTickLabelFont()).getEventSupport().addPropertyChangeListener(this);
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
        
        set.put(new AxisVisibleProperty(getAxisSettings()));
//	public static final String PROPERTY_location = "location";
        set.put(new AxisLineVisibleProperty(getAxisSettings()));
        set.put(new AxisLineProperty(getAxisSettings()));
        set.put(new AxisLabelVisibleProperty(getAxisSettings()));
//	public static final String PROPERTY_label = "label";
        set.put(new AxisLabelPaintProperty(getAxisSettings()));
        set.put(new AxisLabelFontProperty(getAxisSettings()));
//	public static final String PROPERTY_labelInsets = "labelInsets";
//	public static final String PROPERTY_labelAngle = "labelAngle";
        set.put(new TickLabelsVisibleProperty(getAxisSettings()));
        set.put(new TickLabelPaintProperty(getAxisSettings()));
        set.put(new TickLabelFontProperty(getAxisSettings()));
        set.put(new TickMarksVisibleProperty(getAxisSettings()));
        set.put(new TickMarksProperty(getAxisSettings()));
//	public static final String PROPERTY_tickLabelInsets = "tickLabelInsets";
//	public static final String PROPERTY_tickMarksInsideLength = "tickMarksInsideLength";
//	public static final String PROPERTY_tickMarksOutsideLength = "tickMarksOutsideLength";
//	public static final String PROPERTY_tickCount = "tickCount";
//	public static final String PROPERTY_tickInterval = "tickInterval";


        sheet.put(set);
        return sheet;
    }

    /**
     * @return the axisSettings
     */
    public AxisSettings getAxisSettings() {
        return axisSettings;
    }
}