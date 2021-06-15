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
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.LegendBackgroundPaintProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.LegendForegroundPaintProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ShowLegendProperty;
import com.jaspersoft.ireport.designer.outline.nodes.IRAbstractNode;
import com.jaspersoft.ireport.designer.sheet.properties.BoldProperty;
import com.jaspersoft.ireport.designer.sheet.properties.FontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.FontSizeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ItalicProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PdfEmbeddedProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PdfEncodingProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PdfFontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StrikeThroughProperty;
import com.jaspersoft.ireport.designer.sheet.properties.UnderlineProperty;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.chartthemes.simple.LegendSettings;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;


/**
 *
 * @author gtoffoli
 */
public class LegendSettingsNode  extends IRAbstractNode implements PropertyChangeListener {

    private static final String IMAGE_ICON_BASE = "com/jaspersoft/ireport/designer/resources/chartsettings.png";

    private LegendSettings legendSettings = null;

    public LegendSettingsNode(LegendSettings legendSettings, Lookup doLkp)
    {
        super(Children.LEAF, doLkp); //
        this.legendSettings = legendSettings;
        setName("Legend Settings");
        setIconBaseWithExtension(IMAGE_ICON_BASE);
        legendSettings.getEventSupport().addPropertyChangeListener(this);
        ((JRBaseFont)legendSettings.getFont()).getEventSupport().addPropertyChangeListener(this);
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
        
        set.put(new ShowLegendProperty(getLegendSettings()));
//	public static final String PROPERTY_position = "position";
        set.put(new LegendForegroundPaintProperty(getLegendSettings()));
        set.put(new LegendBackgroundPaintProperty(getLegendSettings()));
        JRFont font = getLegendSettings().getFont();
        set.put(new FontNameProperty(font));
        set.put(new FontSizeProperty(font));
        set.put(new BoldProperty(font));
        set.put(new ItalicProperty(font));
        set.put(new UnderlineProperty(font));
        set.put(new StrikeThroughProperty(font));
        set.put(new PdfFontNameProperty(font));
        set.put(new PdfEmbeddedProperty(font));
        set.put(new PdfEncodingProperty(font));
//	public static final String PROPERTY_horizontalAlignment = "horizontalAlignment";
//	public static final String PROPERTY_verticalAlignment = "verticalAlignment";
//	public static final String PROPERTY_blockFrame = "blockFrame";
//	public static final String PROPERTY_padding = "padding";


        sheet.put(set);
        return sheet;
    }

    /**
     * @return the legendSettings
     */
    public LegendSettings getLegendSettings() {
        return legendSettings;
    }
}