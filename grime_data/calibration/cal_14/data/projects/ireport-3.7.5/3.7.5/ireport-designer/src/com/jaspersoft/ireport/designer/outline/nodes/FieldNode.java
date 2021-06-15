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
import com.jaspersoft.ireport.designer.dnd.ReportObjectPaletteTransferable;
import com.jaspersoft.ireport.designer.sheet.JRPropertiesMapProperty;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.ErrorManager;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.RenameAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.Children;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class FieldNode extends IRAbstractNode implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRDesignField field = null;

    public FieldNode(JasperDesign jd, JRDesignField field, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(jd, field)));
        this.jd = jd;
        this.field = field;
        setDisplayName ( field.getName());
        super.setName( field.getName() );
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/field-16.png");
        
        field.getEventSupport().addPropertyChangeListener(this);
    }

    @Override
    public String getDisplayName() {
        return field.getName();
    }
    
    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        Sheet.Set set = Sheet.createPropertiesSet();
        
        JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
        
        set.put(new NameProperty( getField(),dataset));
        set.put(new ValueClassNameProperty( getField()));
        set.put(new DescriptionProperty(getField()));
        set.put(new JRPropertiesMapProperty( getField()) );
        
        sheet.put(set);
        return sheet;
    }
    
    @Override
    public boolean canCut() {
        return true;
    }
    
    @Override
    public boolean canRename() {
        return true;
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
    public void destroy() throws IOException {
       
       JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
       dataset.removeField(getField());
       super.destroy();
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
                    "com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldFromFieldAction",
                    getField()));
        
        return tras;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setName(String s) {
        
        if (s.equals(""))
        {
            throw new IllegalArgumentException(I18n.getString("FieldNode.Property.FieldInvalid"));
        }
        
        List<JRDesignField> currentFields = null;
        JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
        currentFields = (List<JRDesignField>)dataset.getFieldsList();
        for (JRDesignField p : currentFields)
        {
            if (p != getField() && p.getName().equals(s))
            {
                throw new IllegalArgumentException(I18n.getString("FieldNode.Property.FieldInUse"));
            }
        }
        
        String oldName = getField().getName();
        getField().setName(s);
        dataset.getFieldsMap().remove(oldName);
        dataset.getFieldsMap().put(s,getField());
        
        
        ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getField(), "Name", String.class, oldName, s);

        IReportManager.getInstance().addUndoableEdit(opue);
    }

    public JRDesignField getField() {
        return field;
    }

    public void setField(JRDesignField field) {
        this.field = field;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignField.PROPERTY_NAME ))
        {
            super.setName(getField().getName());
            this.setDisplayName(getField().getName());
            // update sorting of childrens...
            if (this.getParentNode() != null && this.getParentNode() instanceof FieldsNode)
            {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        ((FieldsNode)(FieldNode.this.getParentNode())).updateSorting();
                    }
                });
            }
        }
        
        // Update the sheet
        this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
    }
    
    
    
    
    
    
    
    
    
    
    /***************  SHEET PROPERTIES DEFINITIONS **********************/
    
    
    /**
     *  Class to manage the JRDesignField.PROPERTY_NAME property
     */
    public static final class NameProperty extends PropertySupport.ReadWrite {

        JRDesignField field = null;
        JRDesignDataset dataset = null;

        @SuppressWarnings("unchecked")
        public NameProperty(JRDesignField field, JRDesignDataset dataset)
        {
            super(JRDesignField.PROPERTY_NAME, String.class,
                  I18n.getString("FieldNode.Property.Name"),
                  I18n.getString("FieldNode.Property.Namedetail"));
            this.field = field;
            this.dataset = dataset;
            this.setValue("oneline", Boolean.TRUE);
        }

        @Override
        public boolean canWrite()
        {
            return true;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return getField().getName();
        }

        @SuppressWarnings("unchecked")
        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {


            if (val == null || val.equals(""))
            {
                IllegalArgumentException iae = annotateException(I18n.getString("FieldNode.Property.FieldInvalid")); 
                throw iae; 
            }

            String s = val+"";

            List<JRDesignField> currentFields = null;
            currentFields = (List<JRDesignField>)getDataset().getFieldsList();
            for (JRDesignField p : currentFields)
            {
                if (p != getField() && p.getName().equals(s))
                {
                    IllegalArgumentException iae = annotateException(I18n.getString("FieldNode.Property.FieldInUse")); 
                    throw iae; 
                }
            }
            String oldName = getField().getName();
            getField().setName(s);
            dataset.getFieldsMap().remove(oldName);
            dataset.getFieldsMap().put(s,getField());

            ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getField(), "Name", String.class, oldName, getField().getName());

            IReportManager.getInstance().addUndoableEdit(opue);

        }

        public JRDesignDataset getDataset() {
            return dataset;
        }

        public void setDataset(JRDesignDataset dataset) {
            this.dataset = dataset;
        }

        public JRDesignField getField() {
            return field;
        }

        public void setField(JRDesignField field) {
            this.field = field;
        }

        public IllegalArgumentException annotateException(String msg)
        {
            IllegalArgumentException iae = new IllegalArgumentException(msg); 
            ErrorManager.getDefault().annotate(iae, 
                                    ErrorManager.EXCEPTION,
                                    msg,
                                    msg, null, null); 
            return iae;
        }
    }
    
    
    /**
     *  Class to manage the JRDesignField.PROPERTY_VALUE_CLASS_NAME property
     */
    public class ValueClassNameProperty extends PropertySupport.ReadWrite {

        JRDesignField field = null;
        PropertyEditor editor = null;

        @SuppressWarnings("unchecked")
        public ValueClassNameProperty(JRDesignField field)
        {
            super(JRDesignField.PROPERTY_VALUE_CLASS_NAME, String.class,
                  I18n.getString("FieldNode.Property.FieldClass"),
                  I18n.getString("FieldNode.Property.FieldClass"));
            this.field = field;
        }

        @Override
        public boolean canWrite()
        {
            return true;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return getField().getValueClassName();
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

            if (val == null) 
            {
                return;
            }
            if (val instanceof String)
            {
                String s = ((String)val).trim();
                if (s.length() == 0) s = "java.lang.String";

                String oldValue = getField().getValueClassName();
                String newValue = s;
                getField().setValueClassName( s);

                ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(getField(),"ValueClassName", String.class ,oldValue,newValue );
                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
        }

        @Override
        public boolean isDefaultValue() {
            return getField().getValueClassName().equals("java.lang.String");
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            super.restoreDefaultValue();
            setValue("java.lang.String");
            editor.setValue("java.lang.String");
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }

        public JRDesignField getField() {
            return field;
        }

        @Override
        @SuppressWarnings("unchecked")
        public PropertyEditor getPropertyEditor() {

            if (editor == null)
            {
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
                classes.add(new Tag("java.util.Collection"));
                classes.add(new Tag("java.util.List"));
                classes.add(new Tag("java.lang.Object"));
                classes.add(new Tag("java.io.InputStream"));
                editor = new ComboBoxPropertyEditor(true, classes);
            }
            return editor;
        }

        @Override
        public Object getValue(String attributeName) {
            if ("canEditAsText".equals(attributeName)) return true;
            if ("oneline".equals(attributeName)) return true;
            if ("suppressCustomEditor".equals(attributeName)) return false;
            return super.getValue(attributeName);
        }
    }
   
    /**
     *  Class to manage the JRDesignField.PROPERTY_DESCRIPTION property
     */
    public class DescriptionProperty extends PropertySupport.ReadWrite {

        JRDesignField field = null;


        @SuppressWarnings("unchecked")
        public DescriptionProperty(JRDesignField field)
        {
            super(JRDesignField.PROPERTY_DESCRIPTION, String.class,
                  I18n.getString("FieldNode.Property.Description"),
                  I18n.getString("FieldNode.Property.Description"));
            this.field = field;
        }

        @Override
        public boolean canWrite()
        {
            return true;
        }

        @Override
        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return field.getDescription() == null ? "" : field.getDescription();
        }

        @Override
        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

            String oldValue = getField().getDescription();
            String newValue = val == null ? null : ""+val.toString();
            
            getField().setDescription(newValue);
            
            ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            getField(),
                            "Description", 
                            String.class,
                            oldValue,newValue);
                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
        }

        public JRDesignField getField() {
            return field;
        }

        public void setField(JRDesignField field) {
            this.field = field;
        }

        @Override
        public boolean isDefaultValue() {
            return getField().getDescription() == null;
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            setValue(null);
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }
    }
 
}
