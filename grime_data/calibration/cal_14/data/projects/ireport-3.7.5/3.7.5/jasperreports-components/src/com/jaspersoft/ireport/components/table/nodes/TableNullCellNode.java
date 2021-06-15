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
import com.jaspersoft.ireport.components.table.actions.AddTableCellAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnAfterAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnBeforeAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnEndAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnGroupAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnStartAction;
import com.jaspersoft.ireport.components.table.actions.DeleteTableColumnAction;
import com.jaspersoft.ireport.components.table.actions.GroupColumnsAction;
import com.jaspersoft.ireport.designer.outline.nodes.IRAbstractNode;
import java.util.ArrayList;
import javax.swing.Action;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class TableNullCellNode extends IRAbstractNode {

    private JasperDesign jd = null;
    private byte section = 0;
    private JRGroup group = null;
    private BaseColumn column = null;
    private JRDesignComponentElement tableElement = null;


    public TableNullCellNode(JasperDesign jd, JRDesignComponentElement tableElement, BaseColumn column, byte section, JRGroup group, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup( doLkp, Lookups.fixed(jd, column, tableElement)));
        this.jd = jd;
        this.column = column;
        this.section = section;
        this.group = group;
        this.tableElement = tableElement;
        setDisplayName ("Empty cell");

        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/cell-16.png");
    }

    public String getDisplayName()
    {
        if (column instanceof StandardColumnGroup)
        {
            return NbBundle.getMessage(TableElementNode.class, "table.emptyGroupCell.name", (TableModelUtils.getColumnIndex(getTable(), getColumn())+1));
        }
        else
        {
            return NbBundle.getMessage(TableElementNode.class, "table.emptyCell.name", (TableModelUtils.getColumnIndex(getTable(),getColumn())+1));
        }
    }

    @Override
    public String getHtmlDisplayName()
    {
        return "<font color=#999999>" + getDisplayName();
    }

    @Override
    public Action[] getActions(boolean popup) {

        java.util.List<Action> list = new ArrayList<Action>();
        list.add( SystemAction.get(AddTableCellAction.class));
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


        return list.toArray(new Action[list.size()]);
    }

    /**
     * @return the section
     */
    public byte getSection() {
        return section;
    }

    /**
     * @param section the section to set
     */
    public void setSection(byte section) {
        this.section = section;
    }

    /**
     * @return the group
     */
    public JRGroup getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(JRGroup group) {
        this.group = group;
    }

    /**
     * @return the column
     */
    public BaseColumn getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(BaseColumn column) {
        this.column = column;
    }

    public JRDesignComponentElement getComponentElement() {
        return tableElement;
    }

    public StandardTable getTable() {
        return (StandardTable) getComponentElement().getComponent();
    }

}
