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
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.DomainGridlineProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.DomainGridlineVisibleProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.PlotBackgroundImageAlignmentProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.PlotBackgroundPaintProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.PlotOutlineProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.PlotOutlineVisibleProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.PlotSeriesColorsProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.RangeGridlineProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.RangeGridlineVisibleProperty;
import com.jaspersoft.ireport.designer.outline.nodes.IRAbstractNode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.chartthemes.simple.PlotSettings;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;


/**
 *
 * @author gtoffoli
 */
public class PlotSettingsNode  extends IRAbstractNode implements PropertyChangeListener {

    private static final String IMAGE_ICON_BASE = "com/jaspersoft/ireport/designer/resources/chartsettings.png";

    private PlotSettings plotSettings = null;

    public PlotSettingsNode(PlotSettings plotSettings, Lookup doLkp)
    {
        super(Children.LEAF, doLkp); //
        this.plotSettings = plotSettings;
        setName("Plot Settings");
        setIconBaseWithExtension(IMAGE_ICON_BASE);
        plotSettings.getEventSupport().addPropertyChangeListener(this);
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
        
//	public static final String PROPERTY_orientation = "orientation";
//	public static final String PROPERTY_foregroundAlpha = "foregroundAlpha";
        set.put(new PlotBackgroundPaintProperty(getPlotSettings()));
//	public static final String PROPERTY_backgroundAlpha = "backgroundAlpha";
//	public static final String PROPERTY_backgroundImage = "backgroundImage";
//	public static final String PROPERTY_backgroundImageAlpha = "backgroundImageAlpha";
        set.put(new PlotBackgroundImageAlignmentProperty(getPlotSettings()));
//	public static final String PROPERTY_labelRotation = "labelRotation";
//	public static final String PROPERTY_padding = "padding";
        set.put(new PlotOutlineVisibleProperty(getPlotSettings()));
        set.put(new PlotOutlineProperty(getPlotSettings()));
        set.put(new PlotSeriesColorsProperty(getPlotSettings()));
//	public static final String PROPERTY_seriesGradientPaintSequence = "seriesGradientPaintSequence";
//	public static final String PROPERTY_seriesOutlinePaintSequence = "seriesOutlinePaintSequence";
//	public static final String PROPERTY_seriesStrokeSequence = "seriesStrokeSequence";
//	public static final String PROPERTY_seriesOutlineStrokeSequence = "seriesOutlineStrokeSequence";
        set.put(new DomainGridlineVisibleProperty(getPlotSettings()));
        set.put(new DomainGridlineProperty(getPlotSettings()));
        set.put(new RangeGridlineVisibleProperty(getPlotSettings()));
        set.put(new RangeGridlineProperty(getPlotSettings()));


        sheet.put(set);
        return sheet;
    }

    /**
     * @return the plotSettings
     */
    public PlotSettings getPlotSettings() {
        return plotSettings;
    }
}