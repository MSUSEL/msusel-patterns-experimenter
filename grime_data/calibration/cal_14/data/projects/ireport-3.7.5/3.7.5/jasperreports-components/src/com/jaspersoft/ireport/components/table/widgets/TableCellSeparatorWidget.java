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
package com.jaspersoft.ireport.components.table.widgets;

import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.SeparatorWidget;

/**
 * A BandBorderWidget is just a line that is sensible to mouse dragging.
 * @author gtoffoli
 */
public class TableCellSeparatorWidget extends SeparatorWidget {

    final private Stroke stroke = new BasicStroke(1.0f);
    private int index = 0;
    
    public TableCellSeparatorWidget(TableObjectScene scene, int index, Orientation orientation) {
        super(scene, orientation);
        this.index=index;
        
        // We set a border to improve the sensible area....
        if (Orientation.HORIZONTAL.equals(orientation))
        {
            setBorder(BorderFactory.createEmptyBorder(0, 3));
            setCursor( Cursor.getPredefinedCursor( Cursor.S_RESIZE_CURSOR) );
       }
        else
        {
            setBorder(BorderFactory.createEmptyBorder(3, 0));
            setCursor( Cursor.getPredefinedCursor( Cursor.W_RESIZE_CURSOR) );
        }
        setForeground(new Color(0,0,0,0));
        updateBounds();
    }
    
    public void updateBounds()
    {
        if (getOrientation() == Orientation.HORIZONTAL)
        {
            setPreferredLocation(new Point( 0, ((TableObjectScene)getScene()).getHorizontalSeparators().get(getIndex())  ));
            setPreferredBounds(new Rectangle( -10,-3, ((TableObjectScene)getScene()).getTableMatrix().getTableDesignWidth()+10,7));
        }
        else
        {
            setPreferredLocation(new Point(((TableObjectScene)getScene()).getVerticalSeparators().get(getIndex()), 0));
            setPreferredBounds(new Rectangle( -3,-10,7, ((TableObjectScene)getScene()).getTableMatrix().getTableDesignHeight()+10));
        }
    }
    
    
    /**
     * Paints the separator widget.
     */
    @Override
    protected void paintWidget() {
        Graphics2D gr = getGraphics();
        gr.setColor (getForeground());
        Rectangle bounds = getBounds ();
        Insets insets = getBorder ().getInsets ();
        
        gr.setStroke( Java2DUtils.getInvertedZoomedStroke(stroke, 
                this.getScene().getZoomFactor()));
        if (getOrientation() == Orientation.HORIZONTAL)
        {
            Rectangle2D r = new Rectangle2D.Double(0.0,0.0, bounds.width - insets.left - insets.right, 0.0 );
            gr.draw(r);
        }
        else
        {
            Rectangle2D r = new Rectangle2D.Double(0.0,0.0,0.0,bounds.height - insets.top - insets.bottom );
            gr.draw(r);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
