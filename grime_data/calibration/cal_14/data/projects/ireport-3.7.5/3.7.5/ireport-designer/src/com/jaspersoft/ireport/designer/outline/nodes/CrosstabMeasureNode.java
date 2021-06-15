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
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.dnd.ReportObjectPaletteTransferable;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.properties.ExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.ByteProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StringListProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StringProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.RenameAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.Children;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class CrosstabMeasureNode extends IRAbstractNode implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRDesignCrosstab crosstab = null;
    
    private JRDesignCrosstabMeasure measure = null;
    
    public CrosstabMeasureNode(JasperDesign jd, JRDesignCrosstab crosstab, JRDesignCrosstabMeasure measure, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(jd, crosstab, measure)));
        this.jd = jd;
        this.measure = measure;
        this.crosstab = crosstab;
        setDisplayName ( measure.getName());
        super.setName( measure.getName() );
        
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/crosstabmeasures-16.png");
        measure.getEventSupport().addPropertyChangeListener(this);
    }

    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        Sheet.Set set = Sheet.createPropertiesSet();
        
        set.put(new NameProperty( getMeasure(), jd, getCrosstab()));
        set.put(new ValueClassNameProperty(getMeasure()));
        set.put(new ValueExpressionProperty( getMeasure(),getCrosstab(), jd));
        set.put(new CalculationProperty( getMeasure(),getCrosstab()));
        set.put(new IncrementerFactoryClassNameProperty(getMeasure()));
        set.put(new PercentageOfTypeProperty( getMeasure()));
        set.put(new PercentageCalculatorClassNameProperty( getMeasure()));
        
        sheet.put(set);
        
         return sheet;
    }
    
    @Override
    public boolean canCut() {
        return true;
    }
    
    
    /**
     * TODO: make the group name changable.
     * For now we prevent this possibility to avoid to mess around the model
     * since a lot of stuff must be aligned with this name.
     * 
     * @return false (always)
     */
    @Override
    public boolean canRename() {
        return false;
    }
    
    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        super.destroy();
        
        // Destroy the measure...
        this.getCrosstab().removeMeasure(measure);
            
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
            SystemAction.get( ReorderAction.class ),
            null,
            SystemAction.get( DeleteAction.class ) };
    }
    
    @Override
    public Transferable drag() throws IOException {
        
        ExTransferable tras = ExTransferable.create(clipboardCut());
        tras.put(new ReportObjectPaletteTransferable( 
                    "com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldFromCrosstabMeasureNodeAction",
                    this));
        
        return tras;
    }

    
    
    @Override
    @SuppressWarnings("unchecked")
    public void setName(String s) {
        
        if (s.equals(""))
        {
            throw new IllegalArgumentException("Measure name not valid.");
        }
        
        List<JRCrosstabMeasure> currentMeasures = Arrays.asList(getCrosstab().getMeasures());
        for (JRCrosstabMeasure p : currentMeasures)
        {
            if (p != getMeasure() && p.getName().equals(s))
            {
                throw new IllegalArgumentException("Measure name already in use.");
            }
        }
        
        String oldName = getMeasure().getName();
        getMeasure().setName(s);
        
        ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getMeasure(), "Name", String.class, oldName, s);

        IReportManager.getInstance().addUndoableEdit(opue);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignCrosstabMeasure.PROPERTY_NAME ))
        {
            super.setName(getMeasure().getName());
            this.setDisplayName(getMeasure().getName());
        }
        
        // Update the sheet
        this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
    }

    public JRDesignCrosstab getCrosstab() {
        return crosstab;
    }

    public JRDesignCrosstabMeasure getMeasure() {
        return measure;
    }
    
    
    /***************  SHEET PROPERTIES DEFINITIONS **********************/
    
    
    /**
     *  Class to manage the JRDesignField.PROPERTY_NAME property
     */
    public static final class NameProperty extends StringProperty {

        private JRDesignCrosstabMeasure measure = null;
        private JRDesignCrosstab crosstab = null;
        private JasperDesign jd = null;

        @SuppressWarnings("unchecked")
        public NameProperty(JRDesignCrosstabMeasure measure, JasperDesign jd, JRDesignCrosstab crosstab)
        {
            super(measure);
            setName(JRDesignCrosstabMeasure.PROPERTY_NAME);
            setDisplayName("Name");
            setShortDescription("Name of the measure");
            this.measure = measure;
            this.crosstab = crosstab;
            this.jd = jd;
            this.setValue("oneline", Boolean.TRUE);
        }

        public JRDesignCrosstab getCrosstab() {
            return crosstab;
        }

        public JRDesignCrosstabMeasure getMeasure() {
            return measure;
        }

        @Override
        public String getString() {
            return measure.getName();
        }

        @Override
        public String getOwnString() {
            return measure.getName();
        }

        @Override
        public String getDefaultString() {
            return measure.getName();
        }

        @Override
        public boolean supportsDefaultValue() {
            return false;
        }

        @Override
        public void validate(Object value) {

            super.validate(value);
            
            if (value == null || value.equals(""))
            {
                IllegalArgumentException iae = annotateException("Measure name not valid."); 
                throw iae; 
            }
                
            String s = value +"";
            if (s.equals(getMeasure().getName())) return;
            if (!ModelUtils.isValidNewCrosstabObjectName(crosstab, s))
            {
                IllegalArgumentException iae = annotateException("Measure name already in use."); 
                    throw iae; 
            }
        }

        
        @Override
        public void setString(String newName) {
            
            
            
            String oldName = getMeasure().getName();
            int index = crosstab.getMesuresList().indexOf(getMeasure());
            
            
            crosstab.removeMeasure(getMeasure().getName());
            
            getMeasure().setName(newName);
            try {
                crosstab.addMeasure(getMeasure());
                crosstab.getMesuresList().remove(getMeasure());
                crosstab.getMesuresList().add(index, getMeasure());
                crosstab.getEventSupport().firePropertyChange( JRDesignCrosstab.PROPERTY_MEASURES, 0, 1);
                IReportManager.getInstance().setSelectedObject(getMeasure());
            } catch (JRException ex) {
                Exceptions.printStackTrace(ex);
            }
            
            
            // replace oldName with newName in all the crosstab expressions...
            
            ModelUtils.fixElementsExpressions(crosstab, oldName, newName, JRExpressionChunk.TYPE_VARIABLE, getMeasure().getValueClassName());
            
            List expressions = JRExpressionCollector.collectExpressions(jd, crosstab);
            for (int i=0; i<expressions.size(); ++i)
            {
                JRDesignExpression exp = (JRDesignExpression)expressions.get(i);
                ModelUtils.replaceChunkText(exp, oldName, newName, JRExpressionChunk.TYPE_VARIABLE, getMeasure().getValueClassName());
            }
        }

    }
    
    
    
    /**
     *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
     */
    public static final class CalculationProperty extends ByteProperty
    {
        private final JRDesignCrosstabMeasure measure;
        private final JRDesignCrosstab crosstab;

        @SuppressWarnings("unchecked")
        public CalculationProperty(JRDesignCrosstabMeasure measure, JRDesignCrosstab crosstab)
        {
            super(measure);
            setName( JRDesignCrosstabMeasure.PROPERTY_CALCULATION);
            setDisplayName("Calculation");
            setShortDescription("The calculation type which will be performed on the measure values");
            this.crosstab = crosstab;
            this.measure = measure;
        }

        @Override
        public List getTagList() 
        {
            List tags = new java.util.ArrayList();
            tags.add(new Tag(new Byte(JRDesignVariable.CALCULATION_NOTHING), "Nothing"));
            tags.add(new Tag(new Byte(JRDesignVariable.CALCULATION_COUNT), "Count"));
            tags.add(new Tag(new Byte(JRDesignVariable.CALCULATION_DISTINCT_COUNT), "Distinct Count"));
            tags.add(new Tag(new Byte(JRDesignVariable.CALCULATION_SUM), "Sum"));
            tags.add(new Tag(new Byte(JRDesignVariable.CALCULATION_AVERAGE), "Average"));
            tags.add(new Tag(new Byte(JRDesignVariable.CALCULATION_LOWEST), "Lowest"));
            tags.add(new Tag(new Byte(JRDesignVariable.CALCULATION_HIGHEST), "Highest"));
            tags.add(new Tag(new Byte(JRDesignVariable.CALCULATION_STANDARD_DEVIATION), "Standard Deviation"));
            tags.add(new Tag(new Byte(JRDesignVariable.CALCULATION_VARIANCE), "Variance"));
            tags.add(new Tag(new Byte(JRDesignVariable.CALCULATION_FIRST), "First"));
            return tags;
        }

        @Override
        public Byte getByte()
        {
            return measure.getCalculation();
        }

        @Override
        public Byte getOwnByte()
        {
            return measure.getCalculation();
        }

        @Override
        public Byte getDefaultByte()
        {
            return JRDesignVariable.CALCULATION_COUNT;
        }

        @Override
        public void setByte(Byte b)
        {
            measure.setCalculation(b);

            if (b == JRDesignVariable.CALCULATION_COUNT ||
                b == JRDesignVariable.CALCULATION_DISTINCT_COUNT)
            {
                measure.setValueClassName( "java.lang.Integer");
            }
            else if (measure.getValueExpression() != null &&
                     measure.getValueExpression().getValueClassName() != null)
            {
                measure.setValueClassName( measure.getValueExpression().getValueClassName() );
            }
        }
        
        public JRDesignCrosstab getCrosstab() {
            return crosstab;
        }

        public JRDesignCrosstabMeasure getMeasure() {
            return measure;
        }

    }
    
    
    
    /**
     *  Class to manage the JRDesignParameter.PROPERTY_VALUE_CLASS_NAME property
     */
    public class ValueClassNameProperty extends StringListProperty {

        private final JRDesignCrosstabMeasure measure;
        
        public JRDesignCrosstabMeasure getMeasure() {
            return measure;
        }
        
        @SuppressWarnings("unchecked")
        public ValueClassNameProperty(JRDesignCrosstabMeasure measure)
        {
            super(measure);
            setName( JRDesignCrosstabMeasure.PROPERTY_VALUE_CLASS);
            setDisplayName("Measure Class");
            setShortDescription("Measure Class");
            this.measure = measure;
        }

        @Override
        public List getTagList() {
            java.util.List classes = new ArrayList();
            classes.add(new Tag("java.lang.Boolean"));
            classes.add(new Tag("java.lang.Byte"));
            classes.add(new Tag("java.util.Date"));
            classes.add(new Tag("java.sql.Timestamp"));
            classes.add(new Tag("java.sql.Time"));
            classes.add(new Tag("java.lang.Double"));
            classes.add(new Tag("java.lang.Float"));
            classes.add(new Tag("java.lang.Integer"));
            classes.add(new Tag("java.lang.Long"));
            classes.add(new Tag("java.lang.Short"));
            classes.add(new Tag("java.math.BigDecimal"));
            classes.add(new Tag("java.lang.Number"));
            classes.add(new Tag("java.lang.String"));
            classes.add(new Tag("java.lang.Object"));
            return classes;
        }

        @Override
        public String getString() {
            return getMeasure().getValueClassName();
        }

        @Override
        public String getOwnString() {
            return getMeasure().getValueClassName();
        }

        @Override
        public String getDefaultString() {
            return "java.lang.Integer";
        }

        @Override
        public void setString(String value) {
           getMeasure().setValueClassName(value);
           if (getMeasure().getValueExpression() != null)
           {
               ((JRDesignExpression)getMeasure().getValueExpression()).setValueClassName("java.lang.Object");
               //((JRDesignExpression)getMeasure().getValueExpression()).setValueClassName(value);
           }
        }

    }
    
    
    /**
     *  Class to manage the JRDesignDataset.PROPERTY_SCRIPTLET_CLASS property
     */
    private static final class IncrementerFactoryClassNameProperty extends PropertySupport
    {
            private final JRDesignCrosstabMeasure measure;
            
            public JRDesignCrosstabMeasure getMeasure() {
                return measure;
            }
        
            @SuppressWarnings("unchecked")
            public IncrementerFactoryClassNameProperty(JRDesignCrosstabMeasure measure)
            {
                super(JRDesignCrosstabMeasure.PROPERTY_INCREMENTER_FACTORY_CLASS_NAME,String.class, "Incrementer Factory", "The class to use as Factory for the Incrementer.",true, true);
                this.measure = measure;
                this.setValue("oneline", Boolean.TRUE);
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return (getMeasure().getIncrementerFactoryClassName() == null) ? "" : getMeasure().getIncrementerFactoryClassName();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof String)
                {
                    String oldValue = getMeasure().getIncrementerFactoryClassName();
                    String newValue = (val == null || ((String)val).trim().length() == 0) ? null : ((String)val).trim();
                    getMeasure().setIncrementerFactoryClassName( newValue );
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                getMeasure(),
                                "IncrementerFactoryClassName", 
                                String.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            
                }
            }
    }
    
    
    /**
     */
    public static final class ValueExpressionProperty extends ExpressionProperty
    {
        private final JRDesignCrosstabMeasure measure;
        private final JRDesignCrosstab crosstab;
        private final JasperDesign jd;

        public JRDesignCrosstabMeasure getMeasure() {
            return measure;
        }
        
        @SuppressWarnings("unchecked")
        public ValueExpressionProperty(JRDesignCrosstabMeasure measure, JRDesignCrosstab crosstab, JasperDesign jd)
        {
            super(measure, new ExpressionContext( ModelUtils.getCrosstabDataset(crosstab, jd)));
            setName( JRDesignCrosstabMeasure.PROPERTY_VALUE_EXPRESSION);
            setDisplayName("Value Expression");
            setShortDescription("The expression of the measure.");
            this.crosstab = crosstab;
            this.measure = measure;
            this.jd = jd;
            crosstab.getEventSupport().addPropertyChangeListener(JRDesignCrosstab.PROPERTY_DATASET,  new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, ModelUtils.getElementDataset(getCrosstab(), getJasperDesign()));
                }
            });
            
            

        }
        
        public JasperDesign getJasperDesign()
        {
            return jd;
        }
        
        @Override
        public String getDefaultExpressionClassName() {
            return "java.lang.Integer";
        }

        @Override
        public JRDesignExpression getExpression() {
            return (JRDesignExpression)measure.getValueExpression();
        }

        @Override
        public void setExpression(JRDesignExpression expression) {
            // TODO: check the best way to solve the class expression problem...
            if (measure.getCalculation() == JRDesignVariable.CALCULATION_COUNT ||
                measure.getCalculation() == JRDesignVariable.CALCULATION_DISTINCT_COUNT)
            {
                expression.setValueClassName("java.lang.Object"); //measure.getValueClassName()
            }
            else
            {
                expression.setValueClassName(measure.getValueClassName()); //
            }
            measure.setValueExpression(expression);
        }

        
        public JRDesignCrosstab getCrosstab() {
            return crosstab;
        }

        
    }
 
    
    /**
     *  Class to manage the JRDesignCrosstabMeasure.PROPERTY_PERCENTAGE_OF_TYPE property
     */
    public static final class PercentageOfTypeProperty extends ByteProperty
    {
        private final JRDesignCrosstabMeasure measure;
            
        public JRDesignCrosstabMeasure getMeasure() {
            return measure;
        }
        
        @SuppressWarnings("unchecked")
        public PercentageOfTypeProperty(JRDesignCrosstabMeasure measure)
        {
            super(measure);
            setName( JRDesignCrosstabMeasure.PROPERTY_PERCENTAGE_OF_TYPE );
            setDisplayName("Percentage of type");
            setShortDescription("The percentage calculation type performed on this measure");
            this.measure = measure;
        }

        @Override
        public List getTagList() 
        {
            List tags = new java.util.ArrayList();
            tags.add(new Tag(new Byte(JRDesignCrosstabMeasure.PERCENTAGE_TYPE_NONE), "None"));
            tags.add(new Tag(new Byte(JRDesignCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL), "Grand Total"));
            return tags;
        }

        @Override
        public Byte getByte()
        {
            return measure.getPercentageOfType();
        }

        @Override
        public Byte getOwnByte()
        {
            return measure.getPercentageOfType();
        }

        @Override
        public Byte getDefaultByte()
        {
            return JRDesignCrosstabMeasure.PERCENTAGE_TYPE_NONE;
        }

        @Override
        public void setByte(Byte b)
        {
            measure.setPercentageOfType(b);
        }

    }
    
    
    
    
    /**
     *  Class to manage the JRDesignDataset.PROPERTY_SCRIPTLET_CLASS property
     */
    private static final class PercentageCalculatorClassNameProperty extends PropertySupport
    {
            private final JRDesignCrosstabMeasure measure;
            
            public JRDesignCrosstabMeasure getMeasure() {
                return measure;
            }
        
            @SuppressWarnings("unchecked")
            public PercentageCalculatorClassNameProperty(JRDesignCrosstabMeasure measure)
            {
                super(JRDesignCrosstabMeasure.PROPERTY_PERCENTAGE_CALCULATION_CLASS_NAME,String.class, "Percentage calculator", "The class to use to calculate the percentage",true, true);
                this.measure = measure;
                this.setValue("oneline", Boolean.TRUE);
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return (getMeasure().getPercentageCalculatorClassName() == null) ? "" : getMeasure().getPercentageCalculatorClassName();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof String)
                {
                    String oldValue = getMeasure().getPercentageCalculatorClassName();
                    String newValue = (val == null || ((String)val).trim().length() == 0) ? null : ((String)val).trim();
                    getMeasure().setPercentageCalculatorClassName( newValue );
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                getMeasure(),
                                "PercentageCalculatorClassName", 
                                String.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            
                }
            }
    }
    
    
    
}
