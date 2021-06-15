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
package com.jaspersoft.ireport.components.table.actions;

import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableColumnGroupNode;
import com.jaspersoft.ireport.components.table.nodes.TableNullCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableSectionNode;
import com.jaspersoft.ireport.components.table.undo.DeleteTableColumnUndoableEdit;
import com.jaspersoft.ireport.designer.IReportManager;
import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class DeleteTableColumnAction extends NodeAction {

    private static DeleteTableColumnAction instance = null;
    
    public static synchronized DeleteTableColumnAction getInstance()
    {
        if (instance == null)
        {
            instance = new DeleteTableColumnAction();
        }
        
        return instance;
    }
    
    private DeleteTableColumnAction()
    {
        super();
    }
    
    
    public String getName() {
        return NbBundle.getMessage(DeleteTableColumnAction.class, "DeleteTableColumnAction.Name.CTL_DeleteTableColumnAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {

        StandardTable table = null;
        JasperDesign jd = null;
        BaseColumn column = null;
        Node node = activatedNodes[0];
        if (node instanceof TableNullCellNode)
        {
            column = ((TableNullCellNode)node).getColumn();
            table = ((TableNullCellNode)node).getTable();
            jd = ((TableNullCellNode)node).getLookup().lookup(JasperDesign.class);
        }
        else if (node instanceof TableCellNode)
        {
            column = ((TableCellNode)activatedNodes[0]).getColumn();
            table = ((TableCellNode)node).getTable();
            jd = ((TableCellNode)node).getJasperDesign();
        }
        else if (node instanceof TableColumnGroupNode)
        {
            column = ((TableColumnGroupNode)activatedNodes[0]).getColumnGroup();
            table = ((TableColumnGroupNode)node).getTable();
            jd = ((TableColumnGroupNode)node).getJasperDesign();
        }
        else
        {

            Node parent = activatedNodes[0].getParentNode();
            while (parent != null)
            {
                if (parent instanceof TableCellNode)
                {
                    node = (TableCellNode)parent;
                    column = ((TableCellNode)node).getColumn();
                    table = ((TableCellNode)node).getTable();
                    jd = ((TableCellNode)node).getJasperDesign();
                    break;
                }
                parent = parent.getParentNode();
            }
        }

        if (table == null || column == null || jd == null) return;

        TableMatrix matrix = TableModelUtils.createTableMatrix(table, jd);

        Object columnParent = matrix.getColumnParent(column);
        List<BaseColumn> oldColumns = TableModelUtils.getColumns(columnParent);

        /*  Not useful. It is obvious I want to remove the whole column even if there are groups involved...
        if (oldColumns.size() == 1)
        {
            // Removing this cell we have to remove the parent column recursively...
            if (JOptionPane.showConfirmDialog(
                Misc.getMainFrame(),
                "You are deleting the only colum available in this column group so the group will be removed.\n" +
                "All the headers of the groups having only this detail column as child will be removed as well.\n\n"+
                "\nContinue anyway?",
                "Deleting Column Group",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                ImageUtilities.image2Icon(ImageUtilities.loadImage("com/jaspersoft/ireport/components/table/deleting_group.png"))) != JOptionPane.OK_OPTION) return;
        }
        */

        int position = oldColumns.indexOf(column);
        TableModelUtils.removeColumn(columnParent, column, position);

        DeleteTableColumnUndoableEdit edit = new DeleteTableColumnUndoableEdit(table, jd, column, columnParent,position);
        
        // Remove all the column groups with no longer children recursively....
        while (oldColumns.size() == 0 && columnParent instanceof StandardColumnGroup)
        {
            Object oldParentParent = matrix.getColumnParent((StandardColumnGroup)columnParent);
            if (oldParentParent != null) // This shoule be ALWAYS true....
            {
                if (oldParentParent instanceof StandardTable)
                {
                    position = ((StandardTable)oldParentParent).getColumns().indexOf((StandardColumnGroup)columnParent);
                    ((StandardTable)oldParentParent).removeColumn((StandardColumnGroup)columnParent);
                    edit.concatenate(new DeleteTableColumnUndoableEdit(table, jd, (StandardColumnGroup)columnParent, oldParentParent, position));
                    oldColumns = ((StandardTable)oldParentParent).getColumns();
                    columnParent = oldParentParent;
                }
                else
                {
                    position = ((StandardColumnGroup)oldParentParent).getColumns().indexOf((StandardColumnGroup)columnParent);
                    ((StandardColumnGroup)oldParentParent).removeColumn((StandardColumnGroup)columnParent);
                    edit.concatenate(new DeleteTableColumnUndoableEdit(table, jd, (StandardColumnGroup)columnParent, oldParentParent, position));
                    oldColumns = ((StandardColumnGroup)oldParentParent).getColumns();
                    columnParent = oldParentParent;
                }
            }
        }

        IReportManager.getInstance().addUndoableEdit(edit);
        TableModelUtils.fixTableLayout(table, jd);
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        
        boolean typeOk = false;
        
        Node node = activatedNodes[0];
        if (node instanceof TableNullCellNode ||
              node instanceof TableCellNode ||
              node instanceof TableColumnGroupNode) typeOk = true;



        if (!typeOk)
        {
            Node parent = activatedNodes[0].getParentNode();
            while (parent != null)
            {
                if (parent instanceof TableCellNode)
                {
                    node = (TableCellNode)parent;
                    typeOk = true;
                    break;
                }
                parent = parent.getParentNode();
            }
        }

        if (!typeOk) return false;

        Node parent = node.getParentNode();
        if (parent == null) return false;

        // Check if there are at leat 2 standard columns...


        if (parent instanceof TableSectionNode && TableModelUtils.getStandardColumnsCount( ((TableSectionNode)parent).getTable().getColumns()) > 1) return true;
        if (parent instanceof TableColumnGroupNode && TableModelUtils.getStandardColumnsCount( ((TableColumnGroupNode)parent).getTable().getColumns()) > 1) return true;

        return false;

    }

    
}