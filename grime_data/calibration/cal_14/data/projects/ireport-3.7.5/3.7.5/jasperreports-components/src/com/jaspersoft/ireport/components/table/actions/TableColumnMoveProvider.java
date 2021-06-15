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
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.widgets.IndicatorWidget;
import java.awt.Point;
import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class TableColumnMoveProvider implements MoveProvider {

    Point originalLocation = null;
    boolean reversOrder = false;

    int sepIndex = -1;
    
    public TableColumnMoveProvider()
    {
        this(false);
    }
    
    public TableColumnMoveProvider( boolean reversOrder)
    {
        this.reversOrder =  reversOrder;
    }
    
    public void movementStarted(Widget w) {
        
        originalLocation = w.getPreferredLocation();
    }

    public void movementFinished(Widget w) {

        // Valid conditions:
        // 1. The new location cannot be a child of the column
        if (sepIndex < 0 || !(w instanceof IndicatorWidget) || !( ((IndicatorWidget)w).getData() instanceof BaseColumn))
        {
            w.setPreferredLocation(originalLocation);
            return;
        }
        
        IndicatorWidget widget = (IndicatorWidget)w;
        BaseColumn column = (BaseColumn)widget.getData();

        TableObjectScene scene = (TableObjectScene)widget.getScene();
        TableMatrix matrix = scene.getTableMatrix();

        List<StandardColumn> columns = matrix.getStandardColumns();
        StandardColumn siblingColumn = null;
        if (sepIndex >= columns.size())
        {
            siblingColumn = columns.get(columns.size()-1);
        }
        else
        {
            siblingColumn = columns.get(sepIndex);
        }

        Object newParent = matrix.getColumnParent(siblingColumn);

        List<BaseColumn> newColumns = (newParent instanceof StandardTable) ? ((StandardTable)newParent).getColumns() : ((StandardColumnGroup)newParent).getColumns();
        int newPosition = newColumns.indexOf(siblingColumn);
        if (sepIndex >= columns.size()) newPosition++;

        matrix.moveColumn(column, newParent, newPosition);

        // Update the column position...
        originalLocation.x = matrix.getColumnBounds((BaseColumn)widget.getData()).x;
        w.setPreferredLocation(originalLocation);

        // if the widget has been removed from a event, re-add it to the scene...
        if (scene.getIndicatorsLayer().getChildren().size() == 0)
        {
            scene.getIndicatorsLayer().addChild(w);
            scene.validate();
        }
    }

    public Point getOriginalLocation(Widget widget) {
        return widget.getPreferredLocation();
    }

    public void setNewLocation(Widget widget, Point newLocation) {
        widget.setPreferredLocation(newLocation);

        if (widget instanceof IndicatorWidget)
        {
            TableObjectScene scene = (TableObjectScene)widget.getScene();
            TableMatrix matrix = scene.getTableMatrix();

            List<Integer> vertLines = matrix.getVerticalSeparators();
            List<Integer> horizLines = matrix.getHorizontalSeparators();
            int closest = widget.getBounds().width/2;
            int mid = newLocation.x + closest;

            int i = 0;
            sepIndex = -1;
            for (Integer v : vertLines)
            {
                if (Math.abs(mid - v)  < closest)
                {
                    closest = Math.abs(mid - v);
                    sepIndex = i;
                }
                i++;
            }

            ((IndicatorWidget)widget).setLastIndicatedIndex(sepIndex);
        }

    }

}
