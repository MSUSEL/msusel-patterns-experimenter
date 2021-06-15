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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.widgets.*;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Point;
import java.util.List;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.fill.JRFillBand;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class BandMoveProvider implements MoveProvider {

    int startY = 0;
    boolean reversOrder = false;
    
    public BandMoveProvider()
    {
        this(false);
    }
    
    public BandMoveProvider( boolean reversOrder)
    {
        this.reversOrder =  reversOrder;
    }
    
    public void movementStarted(Widget w) {
        
        startY = w.getPreferredLocation().y;
        w.setForeground(ReportObjectScene.EDITING_DESIGN_LINE_COLOR);
    }

    public void movementFinished(Widget w) {
        // we have to update the whole visual things...
        w.setForeground(ReportObjectScene.DESIGN_LINE_COLOR);
        
        ReportObjectScene scene = (ReportObjectScene)w.getScene();
        JRBand b = ((BandSeparatorWidget)w).getBand();
        
        // If it is not reversOrder, find the first band that covers this band.
        // This makes sense only when this band is 0 hight
        if (!reversOrder && b.getHeight() == 0)
        {
            // Look for the right band...
             List<JRBand> bands = ModelUtils.getBands(scene.getJasperDesign());
             JRBand rightBand = bands.get(0);
             for (JRBand tmpBand : bands)
             {
                 if (tmpBand == b) break;
                 if (tmpBand.getHeight() == 0) continue;
                 rightBand = tmpBand;
             }
             b = rightBand;
        }
        
        int delta = w.getPreferredLocation().y - startY;

        // Update the main page...
        int originalHight = b.getHeight();
        int newValue = b.getHeight() + delta;
        ((JRDesignBand)b).setHeight( newValue );
                
        ObjectPropertyUndoableEdit edit = new ObjectPropertyUndoableEdit(
                b, "Height", Integer.TYPE, originalHight,  newValue); // NOI18N
        IReportManager.getInstance().addUndoableEdit(edit);
        
        ((PageWidget)scene.getPageLayer().getChildren().get(0)).updateBounds();
        
        // Update band separators widgets...
        List<Widget> list = scene.getBandSeparatorsLayer().getChildren();
        for (Widget separatorWidget : list)
        {
            ((BandSeparatorWidget)separatorWidget).updateBounds();
        }
        list = scene.getElementsLayer().getChildren();
        for (Widget elementWidget : list)
        {
            ((JRDesignElementWidget)elementWidget).updateBounds();
            ((JRDesignElementWidget)elementWidget).getSelectionWidget().updateBounds();
        }
        
        ((ReportObjectScene)w.getScene()).validate();
        
    }

    public Point getOriginalLocation(Widget widget) {
        return widget.getPreferredLocation();
    }

    public void setNewLocation(Widget widget, Point newLocation) {
        // JRBand b = ((BandSeparatorWidget)w).getBand();
        //((JRDesignBand)b).setHeight( b.getHeight() + (newLocation.y - originalLocation.y));
        // Updatre
        //((ReportObjectScene)w.getScene())
        widget.setPreferredLocation(newLocation);
    }

}
