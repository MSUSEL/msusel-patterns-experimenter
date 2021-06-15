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

import com.jaspersoft.ireport.designer.actions.EditConditionalStyleExpressionBandAction;
import com.jaspersoft.ireport.designer.editor.FullExpressionContext;
import com.jaspersoft.ireport.designer.sheet.properties.ExpressionProperty;
import com.jaspersoft.ireport.designer.styles.ResetStyleAction;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.NewAction;
import org.openide.actions.PasteAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
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
public class ConditionalStyleNode extends AbstractStyleNode implements PropertyChangeListener {

    private JRDesignStyle parentStyle;
    
    public ConditionalStyleNode(JasperDesign jd, JRDesignConditionalStyle style, Lookup doLkp, JRDesignStyle parentStyle)
    {
        super(jd, style, new ProxyLookup( Lookups.singleton(style), doLkp));
        this.parentStyle = parentStyle;
        style.getEventSupport().addPropertyChangeListener(this);
        this.setName("conditionalStyle");
    }
    
    public JRDesignConditionalStyle getConditionalStyle()
    {
        return (JRDesignConditionalStyle)getStyle();
    }

    @Override
    public String getDisplayName() {

        if (getConditionalStyle().getConditionExpression() != null)
        {
            return Misc.getExpressionText(getConditionalStyle().getConditionExpression());
        }
        else
        {
            return "<No condition set>";
        }
    }

    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        Set set = sheet.get(Sheet.PROPERTIES);
        Property[] props = set.getProperties();
        
        // Remove all the properties...
        for (int i=0; i<props.length; ++i)
        {
            set.remove(props[i].getName());
        }
        // Add the missing properties...
        set.put(new ConditionExpressionProperty( getConditionalStyle(), jd));
        
        for (int i=0; i<props.length; ++i)
        {
            set.put(props[i]);
        }
        
        return sheet;
    }
    

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public boolean canCut() {
        return true;
    }



    
    @Override
    public void destroy() throws IOException {
       
          int index = parentStyle.getConditionalStyleList().indexOf(getStyle());
          
          // add destroy....
          parentStyle.removeConditionalStyle(getConditionalStyle());
          // TODO: Add the undo operation
          //DeleteStyleUndoableEdit undo = new DeleteStyleUndoableEdit(getStyle(), jd,index); //newIndex
          //IReportManager.getInstance().addUndoableEdit(undo);
          
          super.destroy();
    }

    public JRDesignStyle getParentStyle() {
        return parentStyle;
    }

    public void setParentStyle(JRDesignStyle parentStyle) {
        this.parentStyle = parentStyle;
    }
        
    public void propertyChange(PropertyChangeEvent evt) {
        
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignConditionalStyle.PROPERTY_CONDITION_EXPRESSION ))
        {
            fireDisplayNameChange(null, null);
        }
        
        super.propertyChange(evt);
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(EditConditionalStyleExpressionBandAction.class); 
    }


    @Override
    public Action[] getActions(boolean popup) {
        return new Action[] {
            SystemAction.get(EditConditionalStyleExpressionBandAction.class),null,
            SystemAction.get( NewAction.class),
            SystemAction.get( CopyAction.class ),
            SystemAction.get( PasteAction.class),
            SystemAction.get( CutAction.class ),
            SystemAction.get( ResetStyleAction.class ),
            SystemAction.get( ReorderAction.class ),
            null,
            SystemAction.get( DeleteAction.class ) };
    }
    
    
    
    /***************  SHEET PROPERTIES DEFINITIONS **********************/
    
    /**
     */
    public static final class ConditionExpressionProperty extends ExpressionProperty
    {
        private final JRDesignConditionalStyle conditionalStyle;
        private final JasperDesign jd;

        public JRDesignConditionalStyle getConditionalStyle() {
            return conditionalStyle;
        }
        
        @SuppressWarnings("unchecked")
        public ConditionExpressionProperty(JRDesignConditionalStyle conditionalStyle, JasperDesign jd)
        {
            super(conditionalStyle, new FullExpressionContext(jd));
            setName( JRDesignConditionalStyle.PROPERTY_CONDITION_EXPRESSION);
            setDisplayName("Condition Expression");
            setShortDescription("The expression used as condition. It should return a boolean object.");
            this.conditionalStyle = conditionalStyle;
            this.jd = jd;
        }
        
        public JasperDesign getJasperDesign()
        {
            return jd;
        }
        
        @Override
        public String getDefaultExpressionClassName() {
            return "java.lang.Boolean";
        }

        @Override
        public JRDesignExpression getExpression() {
            return (JRDesignExpression)conditionalStyle.getConditionExpression();
        }

        @Override
        public void setExpression(JRDesignExpression expression) {
            conditionalStyle.setConditionExpression(expression);
        }
        
    }

}
