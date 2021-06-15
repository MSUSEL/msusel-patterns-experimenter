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

import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.undo.TableCellResizeUndoableEdit;
import com.jaspersoft.ireport.components.table.widgets.TableCellSeparatorWidget;
import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.SeparatorWidget.Orientation;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class TableCellSeparatorMoveProvider implements MoveProvider {

    int startY = 0;
    int startX = 0;
    boolean reversOrder = false;
    
    public TableCellSeparatorMoveProvider()
    {
        this(false);
    }
    
    public TableCellSeparatorMoveProvider( boolean reversOrder)
    {
        this.reversOrder =  reversOrder;
    }
    
    public void movementStarted(Widget w) {
        
        startY = w.getPreferredLocation().y;
        startX = w.getPreferredLocation().x;
        w.setForeground(AbstractReportObjectScene.EDITING_DESIGN_LINE_COLOR);

    }

    public void movementFinished(Widget w) {
        


        // we have to update the whole visual things...
        w.setForeground(new Color(0,0,0,0));
        
        if (!(w instanceof TableCellSeparatorWidget)) return;

        TableCellSeparatorWidget separator = (TableCellSeparatorWidget)w;
        TableObjectScene scene = (TableObjectScene)w.getScene();
         
        // Update the width of all the cells having the widget.getIndex()
        // as right border...
        //List<JRDesignCellContents> cells = getAllCells(scene.getDesignCrosstab());

        int delta = 0;
        java.util.List<TableCellResizeUndoableEdit> undos = new java.util.ArrayList<TableCellResizeUndoableEdit>();
        
        int currentPosition = 0;
        
        if (separator.getOrientation() == Orientation.HORIZONTAL)
        {
            currentPosition = startY;
            delta = w.getPreferredLocation().y - startY;

            // set the new cell hight for all the cells in this row...
            List<TableCell> cells = ((TableObjectScene)w.getScene()).getTableMatrix().getCells();
            TableMatrix matrix = ((TableObjectScene)w.getScene()).getTableMatrix();

            DesignCell firstNotNullCell = null;
            for (TableCell cell : cells)
            {
                if (cell.getRow()+cell.getRowSpan() == separator.getIndex() &&  cell.getCell() != null)
                {
                    firstNotNullCell = cell.getCell();
                    int oldHeight = matrix.getHorizontalSeparators().get(separator.getIndex()) - matrix.getHorizontalSeparators().get(cell.getRow());
                    int newHeight = oldHeight+delta;
                    oldHeight = cell.getCell().getHeight();

                    cell.getCell().setHeight(newHeight);
                    undos.add( new TableCellResizeUndoableEdit(scene.getTable(), scene.getJasperDesign(),cell.getCell(),"Height",Integer.class, oldHeight, newHeight ) );
                }
            }
            if (firstNotNullCell != null)
                firstNotNullCell.getEventSupport().firePropertyChange("ROW_HEIGHT", null, 0);

        }
        else
        {
            currentPosition = startX;
            delta = w.getPreferredLocation().x - startX;

            // Find the columns looking at the headers cells which have the right side on this index...


            TableMatrix matrix = ((TableObjectScene)w.getScene()).getTableMatrix();

            List<TableCell> cells = matrix.getCells();
            StandardBaseColumn firstNotNullColumn = null;
            for (TableCell cell : cells)
            {
                if (cell.getType() == TableCell.TABLE_HEADER &&
                    cell.getCol()+cell.getColSpan() == separator.getIndex())
                {
                    int oldWidth = matrix.getVerticalSeparators().get(separator.getIndex()) - matrix.getVerticalSeparators().get(cell.getCol());
                    int newWidth = oldWidth + delta;
                    oldWidth = cell.getColumn().getWidth();

                    ((StandardBaseColumn)cell.getColumn()).setWidth(newWidth);
                    firstNotNullColumn = (StandardBaseColumn)cell.getColumn();
                    undos.add( new TableCellResizeUndoableEdit(scene.getTable(), scene.getJasperDesign(),((StandardBaseColumn)cell.getColumn()),"Width",Integer.class, oldWidth, newWidth ) );
                }
            }
            if (firstNotNullColumn != null)
                firstNotNullColumn.getEventSupport().firePropertyChange("COLUMN_WIDTH", null, 0);
        }
              
        if (delta != 0 && undos.size() > 0)
        {
            // Change the position of all the elements under the
            // modified line or on his right...
            TableCellResizeUndoableEdit mainUndo = undos.get(0);
            
            mainUndo.setMain(true);

            for (int i=1; i<undos.size(); ++i)
            {
                TableCellResizeUndoableEdit undo = undos.get(i);
                mainUndo.concatenate(undo);
            }
                
            IReportManager.getInstance().addUndoableEdit(mainUndo, false);
            TableModelUtils.fixTableLayout(scene.getTable(), scene.getJasperDesign());
        }
    
    }

    public Point getOriginalLocation(Widget widget) {
        return widget.getPreferredLocation();
    }

    public void setNewLocation(Widget widget, Point newLocation) {
        widget.setPreferredLocation(newLocation);
    }

}
