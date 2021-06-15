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
import com.jaspersoft.ireport.designer.actions.DeleteGroupAction;
import com.jaspersoft.ireport.designer.actions.MoveGroupDownAction;
import com.jaspersoft.ireport.designer.actions.MoveGroupUpAction;
import com.jaspersoft.ireport.designer.outline.nodes.BandNode.RefreshNodes;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.PasteAction;
import org.openide.actions.RenameAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.Children;
import org.openide.nodes.NodeTransfer;
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
public class GroupNodeImpl extends IRAbstractNode implements PropertyChangeListener, GroupNode {

    JasperDesign jd = null;
    private JRDesignGroup group = null;

    public GroupNodeImpl(JasperDesign jd, JRDesignGroup group, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(jd, group)));
        this.jd = jd;
        this.group = group;
        setDisplayName ( group.getName());
        super.setName( group.getName() );
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/groups-16.png");
        
        group.getEventSupport().addPropertyChangeListener(this);
    }

    @Override
    public String getDisplayName() {
        return group.getName();
    }
    
    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        if (group != null)
        {
            Sheet.Set groupPropertiesSet = Sheet.createPropertiesSet();
            groupPropertiesSet.setName("GROUP_PROPERTIES");
            groupPropertiesSet.setDisplayName(I18n.getString("BandNode.Property.Groupproperties"));

            if (getDataset() != null)
            {
                groupPropertiesSet = BandNode.fillGroupPropertySet(groupPropertiesSet, getDataset(), group);
            }
            
            sheet.put(groupPropertiesSet);
        }
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
       getGroup().getEventSupport().removePropertyChangeListener(this);
       dataset.removeGroup(getGroup());
       super.destroy();
    }


        
    @Override
    public Action[] getActions(boolean popup) {

        java.util.List<Action> list = new ArrayList<Action>();

        list.add( SystemAction.get( CopyAction.class ));
        list.add( SystemAction.get( CutAction.class ));
        list.add( SystemAction.get( RenameAction.class ));
        list.add( SystemAction.get( ReorderAction.class ));
        list.add( SystemAction.get( PasteAction.class));
        list.add( SystemAction.get( RefreshNodes.class));

        if (group != null)
        {
            list.add( null );
            list.add( SystemAction.get(MoveGroupUpAction.class));
            list.add( SystemAction.get(MoveGroupDownAction.class));
            list.add( DeleteGroupAction.getInstance() );
        }


        return list.toArray(new Action[list.size()]);
    }
    
    @Override
    public Transferable drag() throws IOException {
        
        ExTransferable tras = ExTransferable.create(clipboardCut());
        return tras;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setName(String s) {
        
        if (s.equals(""))
        {
            throw new IllegalArgumentException(I18n.getString("GroupNode.Property.GroupInvalid"));
        }
        
        List<JRDesignGroup> currentGroups = null;
        JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
        currentGroups = (List<JRDesignGroup>)dataset.getGroupsList();
        for (JRDesignGroup p : currentGroups)
        {
            if (p != getGroup() && p.getName().equals(s))
            {
                throw new IllegalArgumentException(I18n.getString("GroupNode.Property.GroupInUse"));
            }
        }
        
        String oldName = getGroup().getName();
        getGroup().setName(s);
        dataset.getGroupsMap().remove(oldName);
        dataset.getGroupsMap().put(s,getGroup());

        JRDesignVariable var = (JRDesignVariable) dataset.getVariablesMap().get(oldName + "_COUNT");
        var.setName(s + "_COUNT");
        dataset.getVariablesMap().remove(oldName + "_COUNT");
        dataset.getVariablesMap().put(s + "_COUNT", var);

        dataset.getEventSupport().firePropertyChange(JRDesignDataset.PROPERTY_VARIABLES, null, null);

        
        
        ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getGroup(), "Name", String.class, oldName, s);

        IReportManager.getInstance().addUndoableEdit(opue);
    }

    public JRDesignGroup getGroup() {
        return group;
    }

    public void setGroup(JRDesignGroup group) {
        this.group = group;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignGroup.PROPERTY_NAME ))
        {
            super.setName(getGroup().getName());
            this.setDisplayName(getGroup().getName());
        }
        
        // Update the sheet
        if (ModelUtils.containsProperty(this.getPropertySets(), evt.getPropertyName())) {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }

    public JRDesignDataset getDataset() {
        if (getParentNode() == null) return null;
        return getParentNode().getLookup().lookup(JRDesignDataset.class);
    }
    
    
    
 
}
