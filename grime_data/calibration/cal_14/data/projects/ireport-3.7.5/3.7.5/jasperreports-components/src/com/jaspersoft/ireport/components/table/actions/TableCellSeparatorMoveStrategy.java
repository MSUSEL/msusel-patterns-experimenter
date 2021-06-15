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

import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.widgets.TableCellSeparatorWidget;
import java.awt.Point;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.SeparatorWidget.Orientation;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class TableCellSeparatorMoveStrategy implements MoveStrategy {

    boolean reversOrder = false;
    
    public TableCellSeparatorMoveStrategy()
    {
        this(false);
    }
    
    public TableCellSeparatorMoveStrategy( boolean reversOrder)
    {
        this.reversOrder =  reversOrder;
    }
    
    public Point locationSuggested(Widget w, Point originalLocation, Point suggestedLocation) {

        if (w instanceof TableCellSeparatorWidget)
        {
            TableCellSeparatorWidget separator = (TableCellSeparatorWidget)w;
            TableObjectScene scene = (TableObjectScene)w.getScene();
            Orientation orientation = ((TableCellSeparatorWidget)w).getOrientation();
            if (orientation == Orientation.HORIZONTAL)
            {
                suggestedLocation.x = 0;
                int previousY = separator.getIndex() > 0 ? scene.getHorizontalSeparators().get(  separator.getIndex() -1 ) : 0 ;
                suggestedLocation.y = Math.max(previousY, suggestedLocation.y);
            }
            else
            {
                suggestedLocation.y = 0;
                int previousX = separator.getIndex() > 0 ? scene.getVerticalSeparators().get(  separator.getIndex() -1  ) : 0;
                suggestedLocation.x = Math.max(previousX, suggestedLocation.x);
            }
        }
        
        return suggestedLocation;
    }
}
