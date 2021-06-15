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
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.undo.DeleteTableCellUndoableEdit;
import com.jaspersoft.ireport.components.table.undo.DeleteTableColumnUndoableEdit;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.util.List;
import javax.swing.JOptionPane;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class DeleteTableCellAction extends NodeAction {

    private static DeleteTableCellAction instance = null;
    
    public static synchronized DeleteTableCellAction getInstance()
    {
        if (instance == null)
        {
            instance = new DeleteTableCellAction();
        }
        
        return instance;
    }
    
    private DeleteTableCellAction()
    {
        super();
    }
    
    
    public String getName() {
        return NbBundle.getMessage(DeleteTableCellAction.class, "DeleteTableCellAction.Name.CTL_DeleteTableCellAction");
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
        
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (activatedNodes[i] instanceof TableCellNode)
            {
                TableCellNode cellNode = (TableCellNode)activatedNodes[i];

                StandardTable table = cellNode.getTable();
                JasperDesign jd = cellNode.getJasperDesign();
                
                BaseColumn column = cellNode.getColumn();

                TableMatrix matrix = TableModelUtils.createTableMatrix(cellNode.getTable(), cellNode.getJasperDesign() );
                Object parentGroup = matrix.getColumnParent(column);

                DesignCell cell = cellNode.getCell();

                if (column instanceof StandardColumn)
                {
                    // If this is the only column, check if this is the only cell...
                    int cells = 0;
                    if ( ((StandardColumn)column).getTableHeader() != null) cells++;
                    if ( ((StandardColumn)column).getTableFooter() != null) cells++;
                    if ( ((StandardColumn)column).getColumnHeader() != null) cells++;
                    if ( ((StandardColumn)column).getColumnFooter() != null) cells++;
                    if ( ((StandardColumn)column).getDetailCell() != null) cells++;
                    
                    for (GroupCell groupCell : ((StandardColumn)column).getGroupHeaders())
                    {
                        if (groupCell.getCell() != null) cells++;
                    }
                    
                    for (GroupCell groupCell : ((StandardColumn)column).getGroupFooters())
                    {
                        if (groupCell.getCell() != null) cells++;
                    }
                
                    if (cells == 1)
                    {
                        
                        if (matrix.getStandardColumns().size() == 1)
                        {
                            JOptionPane.showMessageDialog(Misc.getMainFrame(), "This is the only cell of a detail column in the table, and it cannot be removed.", "Cannot delete cell", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        
                        if (TableModelUtils.getColumns(parentGroup).size() == 1)
                        {
                            // Removing this cell we have to remove the parent column recursively...
                            if (JOptionPane.showConfirmDialog(
                                Misc.getMainFrame(),
                                "You are deleting the only cell available in this column and the column will be removed.\n" +
                                "All the headers of the groups having only this detail column as child will be removed as well.\n\n"+
                                "\nContinue anyway?",
                                "Deleting Column Group",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                                ImageUtilities.image2Icon(ImageUtilities.loadImage("com/jaspersoft/ireport/components/table/deleting_group.png"))) != JOptionPane.OK_OPTION) return;
                        }

                        // Let's proceed... remove the cell from the column (this is actually not necessary...
                        // TableModelUtils.removeCell(cellNode.getColumn(), cellNode.getSection(), (cellNode.getGroup() != null) ? cellNode.getGroup().getName() : null);

                        // Remove the column from the parent...
                        int position = TableModelUtils.getColumns(parentGroup).indexOf(column);
                        TableModelUtils.removeColumn(parentGroup, column, position);

                        DeleteTableColumnUndoableEdit edit = new DeleteTableColumnUndoableEdit(table,jd,column, parentGroup, position);
                        List<BaseColumn> oldColumns = TableModelUtils.getColumns(parentGroup);

                        // Remove all the column groups with no longer children recursively....
                        while (oldColumns.size() == 0 && parentGroup instanceof StandardColumnGroup)
                        {
                            Object oldParentParent = matrix.getColumnParent((StandardColumnGroup)parentGroup);
                            if (oldParentParent != null) // This shoule be ALWAYS true....
                            {
                                if (oldParentParent instanceof StandardTable)
                                {
                                    position = ((StandardTable)oldParentParent).getColumns().indexOf((StandardColumnGroup)parentGroup);
                                    ((StandardTable)oldParentParent).removeColumn((StandardColumnGroup)parentGroup);
                                    edit.concatenate(new DeleteTableColumnUndoableEdit(table,jd,(StandardColumnGroup)parentGroup, oldParentParent, position));
                                    oldColumns = ((StandardTable)oldParentParent).getColumns();
                                    parentGroup = oldParentParent;
                                }
                                else
                                {
                                    position = ((StandardColumnGroup)oldParentParent).getColumns().indexOf((StandardColumnGroup)parentGroup);
                                    ((StandardColumnGroup)oldParentParent).removeColumn((StandardColumnGroup)parentGroup);
                                    edit.concatenate(new DeleteTableColumnUndoableEdit(table,jd,(StandardColumnGroup)parentGroup, oldParentParent, position));
                                    oldColumns = ((StandardColumnGroup)oldParentParent).getColumns();
                                    parentGroup = oldParentParent;
                                }
                            }
                        }

                        edit.setPresentationName("Remove Cell");
                        IReportManager.getInstance().addUndoableEdit(edit);
                        TableModelUtils.fixTableLayout(table, jd);
                        return;

                    }
                }

                // Green light to proceed! This is the easy case!

                TableModelUtils.removeCell(cellNode.getColumn(), cellNode.getSection(), (cellNode.getGroup() != null) ? cellNode.getGroup().getName() : null);

                // Now we should check if this column has at least a cell. If not, the column must be recursively removed. In that case prompt for
                

                DeleteTableCellUndoableEdit edit = new DeleteTableCellUndoableEdit(table,jd,cell, cellNode.getColumn(), cellNode.getSection(),cellNode.getGroup());
                IReportManager.getInstance().addUndoableEdit(edit);
                TableModelUtils.fixTableLayout(table, jd);
            }
            
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof TableCellNode)) return false;
        }
        return true;
    }
}