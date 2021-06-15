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

import com.jaspersoft.ireport.designer.sheet.properties.style.StyledTextProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.RadiusProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.StrikeThroughProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ScaleImageProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PdfEmbeddedProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PaddingAndBordersProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.HorizontalAlignmentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FillProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.BoldProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.BlankWhenNullProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.BackcolorProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.UnderlineProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.VerticalAlignmentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ModeProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.sheet.JRPenProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.MarkupProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StylePatternProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FontSizeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ForecolorProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ItalicProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.LineSpacingProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PdfEncodingProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PdfFontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.RotationProperty;
import com.jaspersoft.ireport.designer.styles.ResetStyleAction;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.RenameAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * ParameterNode detects the events fired by the subtended parameter.
 * Implements the support for the property sheet of a parameter.
 * If a parameter is system defined, it can not be cut.
 * Actions of a parameter node include copy, paste, reorder, rename and delete.
 * 
 * @author gtoffoli
 */
public class AbstractStyleNode extends IRIndexedNode implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRBaseStyle style = null;

    public JasperDesign getJasperDesign()
    {
        return jd;
    }

    public AbstractStyleNode(StyleChildren pc, JasperDesign jd, JRDesignStyle style, Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(jd,style)));

        this.jd = jd;
        this.style = style;

        init();
    }

    public AbstractStyleNode(JasperDesign jd, JRDesignStyle style, Lookup doLkp) {
        this(new StyleChildren(jd, style, doLkp), jd, style, doLkp);
    }

    public AbstractStyleNode(JasperDesign jd, JRDesignConditionalStyle style, Lookup doLkp) {
        super(new ConditionalStyleChildren(jd, style, doLkp), null, new ProxyLookup(doLkp, Lookups.fixed(jd,style)));
        this.jd = jd;
        this.style = style;

        init();
    }

    /*
    public AbstractStyleNode(JasperDesign jd, JRBaseStyle style, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(jd, style)));
        this.jd = jd;
        this.style = style;
        
        init();
    }
    */
    
    private void init()
    {
        setDisplayName ( style.getName());
        super.setName( style.getName() );
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/style-16.png");
        
        style.getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)style.getLinePen()).getEventSupport().addPropertyChangeListener(this);
        
        JRBaseLineBox baseBox = (JRBaseLineBox)style.getLineBox();
        baseBox.getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)baseBox.getPen()).getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)baseBox.getTopPen()).getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)baseBox.getBottomPen()).getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)baseBox.getLeftPen()).getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)baseBox.getRightPen()).getEventSupport().addPropertyChangeListener(this);
    }

    @Override
    public String getDisplayName() {
        return getStyle().getName();
    }
    
    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        Sheet.Set set = Sheet.createPropertiesSet();
        
        //set.put(new NameProperty( getStyle(), jd));
        //set.put(new DefaultStyleProperty( getStyle(), jd));
        //set.put(new ParentStyleProperty( getStyle(), jd));
        set.put(new ModeProperty( getStyle()));
        set.put(new ForecolorProperty( getStyle()));
        set.put(new BackcolorProperty( getStyle()));
        // Pen....
        
        set.put(new PaddingAndBordersProperty( getStyle()));
        JRPenProperty penProp = new JRPenProperty(getStyle().getLinePen(), getStyle());
        
        set.put(penProp);
        
        // Width...
        set.put(new FillProperty( getStyle()));
        set.put(new RadiusProperty( getStyle()));
        set.put(new ScaleImageProperty( getStyle()));
        set.put(new HorizontalAlignmentProperty( getStyle()));
        set.put(new VerticalAlignmentProperty( getStyle()));
        
        set.put(new RotationProperty( getStyle() ));
        set.put(new LineSpacingProperty( getStyle() ));
        set.put(new StyledTextProperty( getStyle() ));
        
        set.put(new FontNameProperty( getStyle() ));
        set.put(new FontSizeProperty( getStyle() ));
        
        set.put(new BoldProperty( getStyle() ));
        set.put(new ItalicProperty( getStyle() ));
        set.put(new UnderlineProperty( getStyle() ));
        set.put(new StrikeThroughProperty( getStyle() ));
        
        set.put(new PdfFontNameProperty( getStyle() ));
        set.put(new PdfEmbeddedProperty( getStyle() ));
        set.put(new PdfEncodingProperty( getStyle() ));
        set.put(new MarkupProperty( getStyle() ));
        
        set.put(new StylePatternProperty( getStyle() ));
        set.put(new BlankWhenNullProperty( getStyle() ));
        
                
        sheet.put(set);
        return sheet;
    }
    
    @Override
    public boolean canCut() {
        return true;
    }
    
    @Override
    public boolean canRename() {
        return false;
    }
    
    @Override
    public boolean canDestroy() {
        return true;
    }
    
    @Override
    public Transferable clipboardCut() throws IOException {
        return NodeTransfer.transferable(this, NodeTransfer.CLIPBOARD_CUT);
    }
    
    @Override
    public Transferable clipboardCopy() throws IOException {
        return NodeTransfer.transferable(this, NodeTransfer.CLIPBOARD_COPY);
    }
    
    @Override
    public Action[] getActions(boolean popup) {
        return new Action[] {
            SystemAction.get( CopyAction.class ),
            SystemAction.get( CutAction.class ),
            SystemAction.get( RenameAction.class ),
            SystemAction.get( ResetStyleAction.class ),
            SystemAction.get( ReorderAction.class ),
            null,
            SystemAction.get( DeleteAction.class ) };
    }
    
    @Override
    public Transferable drag() throws IOException {
        ExTransferable tras = ExTransferable.create(clipboardCut());
        //tras.put(new ReportObjectPaletteTransferable( 
        //            "com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldFromParameterAction",
        //            getParameter()));
        
        return tras;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignStyle.PROPERTY_NAME ))
        {
            super.setName(getStyle().getName());
            this.setDisplayName(getStyle().getName());
        }
        else if (evt.getPropertyName().equals(JRBasePen.PROPERTY_LINE_COLOR) ||
                 evt.getPropertyName().equals(JRBasePen.PROPERTY_LINE_STYLE) ||
                 evt.getPropertyName().equals(JRBasePen.PROPERTY_LINE_WIDTH))
        {
            
            if (ModelUtils.containsProperty(this.getPropertySets(),"pen"))
            {
                this.firePropertyChange("pen", evt.getOldValue(), evt.getNewValue() );
            }
            
            if (ModelUtils.containsProperty(this.getPropertySets(),"linebox"))
            {
                this.firePropertyChange("linebox", evt.getOldValue(), evt.getNewValue() );
            }
        }
        else if (evt.getPropertyName().equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
                 evt.getPropertyName().equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
                 evt.getPropertyName().equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
                 evt.getPropertyName().equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING))
        {
            if (ModelUtils.containsProperty(this.getPropertySets(),"linebox"))
            {
                this.firePropertyChange("linebox", evt.getOldValue(), evt.getNewValue() );
            }
        }
        
        // Update the sheet
        if (ModelUtils.containsProperty(this.getPropertySets(),evt.getPropertyName()))
        {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
        }
        
    }

    public JRBaseStyle getStyle() {
        return style;
    }

    public void setStyle(JRBaseStyle style) {
        this.style = style;
    }


}
