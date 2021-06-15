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
package com.jaspersoft.ireport.components.table.nodes;

import com.jaspersoft.ireport.components.table.TableElementNode;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.nodes.properties.CellStyleProperty;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnAfterAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnBeforeAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnEndAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnGroupAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnStartAction;
import com.jaspersoft.ireport.components.table.actions.DeleteTableCellAction;
import com.jaspersoft.ireport.components.table.actions.DeleteTableColumnAction;
import com.jaspersoft.ireport.components.table.actions.GroupColumnsAction;
import com.jaspersoft.ireport.components.table.actions.TableCellDoHorizontalLayoutAction;
import com.jaspersoft.ireport.components.table.actions.TableCellDoLayoutAction;
import com.jaspersoft.ireport.components.table.actions.TableCellDoVerticalLayoutAction;
import com.jaspersoft.ireport.components.table.nodes.properties.CellHeightProperty;
import com.jaspersoft.ireport.components.table.nodes.properties.CellPaddingAndBordersProperty;
import com.jaspersoft.ireport.components.table.nodes.properties.ColumnPrintWhenExpressionProperty;
import com.jaspersoft.ireport.components.table.nodes.properties.ColumnWidthProperty;
import com.jaspersoft.ireport.components.table.nodes.properties.RowSpanProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.outline.nodes.ElementContainerChildren;
import com.jaspersoft.ireport.designer.outline.nodes.ElementGroupNode;
import com.jaspersoft.ireport.designer.outline.nodes.ElementPasteType;
import com.jaspersoft.ireport.designer.outline.nodes.IRIndexedNode;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.PasteAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class TableCellNode extends IRIndexedNode implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRDesignComponentElement tableElement = null;
    private BaseColumn column = null;
    private DesignCell cell = null;
    private byte section = 0;
    private JRGroup group = null;

     public TableCellNode(JasperDesign jd,  JRDesignComponentElement tableElement, BaseColumn column, DesignCell cell, byte section, JRGroup group, Lookup doLkp) {
        this(new ElementContainerChildren(jd, cell, doLkp), jd, tableElement, column, cell,section,group,doLkp);
    }

    public TableCellNode(ElementContainerChildren pc, JasperDesign jd, JRDesignComponentElement tableElement, BaseColumn column, DesignCell cell, byte section, JRGroup group,Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup( Lookups.fixed(jd, tableElement, column, cell), doLkp) );
        this.jd = jd;
        this.column = column;
        this.tableElement = tableElement;
        this.section = section;
        this.group = group;
        this.cell = cell;

        super.setName("Table cell"); // NOI18N

        if (column instanceof StandardColumnGroup)
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/components/table/header-16.png");
        }
        else
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/cell-16.png");
        }
        cell.getEventSupport().addPropertyChangeListener(this);
        
        this.addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {}
            public void childrenRemoved(NodeMemberEvent ev) {}
            public void nodeDestroyed(NodeEvent ev) {}
            public void propertyChange(PropertyChangeEvent evt) {}

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {
                // Fire an event now...

                List elements = getCell().getChildren();
                int[] permutations = ev.getPermutation();

                Object[] elementsArray = new Object[elements.size()];
                for (int i=0; i<elementsArray.length; ++i)
                {
                    elementsArray[permutations[i]] = elements.get(i);
                }
                elements.clear();
                for (int i=0; i<elementsArray.length; ++i)
                {
                    elements.add(elementsArray[i]);
                }

                getCell().getEventSupport().firePropertyChange( JRDesignBand.PROPERTY_CHILDREN, null, getCell().getChildren());
            }
        });
    }

    public String getDisplayName()
    {
        if (column instanceof StandardColumnGroup)
        {
            return NbBundle.getMessage(TableElementNode.class, "table.groupCell.name", (TableModelUtils.getColumnIndex(getTable(),getColumn())+1));
        }
        else
        {
            return NbBundle.getMessage(TableElementNode.class, "table.cell.name", (TableModelUtils.getColumnIndex(getTable(),getColumn())+1));
        }
    }

    @Override
    public Action[] getActions(boolean popup) {

        java.util.List<Action> list = new ArrayList<Action>();
        list.add( SystemAction.get(DeleteTableCellAction.class));
        list.add(null);
        list.add( SystemAction.get(AddTableColumnAfterAction.class));
        list.add( SystemAction.get(AddTableColumnBeforeAction.class));
        list.add( SystemAction.get(AddTableColumnStartAction.class));
        list.add( SystemAction.get(AddTableColumnEndAction.class));
        list.add( SystemAction.get(AddTableColumnGroupAction.class));
        list.add( null);
        list.add( SystemAction.get(DeleteTableColumnAction.class));
        list.add( null);
        list.add( SystemAction.get(GroupColumnsAction.class));
        list.add( null );
        list.add( SystemAction.get(PasteAction.class));
        list.add( SystemAction.get(TableCellDoVerticalLayoutAction.class));
        list.add( SystemAction.get(TableCellDoHorizontalLayoutAction.class));

        return list.toArray(new Action[list.size()]);
    }

    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        Sheet.Set set = Sheet.createPropertiesSet();

        set.put(new CellStyleProperty(getCell(), jd));
        set.put(new CellHeightProperty(getCell(), getTable(), getJasperDesign()));
        set.put(new RowSpanProperty(getCell(), getTable(), getJasperDesign()));
        set.put(new ColumnWidthProperty((StandardBaseColumn) getColumn(), getTable(), getJasperDesign()));
        set.put(new CellPaddingAndBordersProperty(getCell()));

        JRDesignDataset dataset = getJasperDesign().getMainDesignDataset();
        if (getTable().getDatasetRun() != null && getTable().getDatasetRun().getDatasetName() != null)
        {
            dataset = (JRDesignDataset) getJasperDesign().getDatasetMap().get(getTable().getDatasetRun().getDatasetName());
        }

        set.put(new ColumnPrintWhenExpressionProperty((StandardBaseColumn) getColumn(), dataset));

        sheet.put(set);

        return sheet;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    public void propertyChange(PropertyChangeEvent evt) {

        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) {
            return;
        }


        // Update the sheet
        if (ModelUtils.containsProperty(this.getPropertySets(), evt.getPropertyName())) {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }

    }

    public BaseColumn getColumn() {
        return column;
    }

    public JRDesignComponentElement getComponentElement() {
        return tableElement;
    }

    public StandardTable getTable() {
        return (StandardTable) getComponentElement().getComponent();
    }

    public DesignCell getCell() {
        return cell;
    }

    public JasperDesign getJasperDesign() {
        return jd;
    }

    /**
     * @return the section
     */
    public byte getSection() {
        return section;
    }

    /**
     * @return the group
     */
    public JRGroup getGroup() {
        return group;
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void createPasteTypes(Transferable t, List s) {
        super.createPasteTypes(t, s);
        PasteType paste = getDropType(t, DnDConstants.ACTION_MOVE, -1);
        if (null != paste) {
            s.add(paste);
        }
    }

    @Override
    public boolean hasCustomizer() {
        return true;
    }


    
    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        Node[] dropNodes = NodeTransfer.nodes(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        int dropAction = DnDUtilities.getTransferAction(t);

        if (dropNode == null)
        {
            ElementPasteType.setLastPastedNodes(dropNodes);
        }

        if (null != dropNode) {
            JRDesignElement element = dropNode.getLookup().lookup(JRDesignElement.class);

            if (element instanceof JRDesignComponentElement && ((JRDesignComponentElement)element).getComponent() == getTable()) return null;

            if (null != element ) {

                return new ElementPasteType( element.getElementGroup(),
                                             (JRElementGroup)getCell(),
                                             element,dropAction,this);
            }

            if (dropNode instanceof ElementGroupNode)
            {
                JRDesignElementGroup g = ((ElementGroupNode)dropNode).getElementGroup();
                return new ElementPasteType( g.getElementGroup(),
                                             (JRElementGroup)getCell(),
                                             g,dropAction,this);
            }
            else
            {

            }
        }
        return null;
    }
     
}
