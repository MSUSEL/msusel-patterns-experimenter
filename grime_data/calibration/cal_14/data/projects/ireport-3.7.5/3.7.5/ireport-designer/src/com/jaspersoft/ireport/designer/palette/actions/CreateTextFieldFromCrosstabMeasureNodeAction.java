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
package com.jaspersoft.ireport.designer.palette.actions;

import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.outline.nodes.CrosstabMeasureNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
@SuppressWarnings("unchecked")
public class CreateTextFieldFromCrosstabMeasureNodeAction extends CreateTextFieldAction {

    @Override
    public void drop(DropTargetDropEvent dtde) {
    
        if ( !(getPaletteItem().getData() instanceof CrosstabMeasureNode))
        {
            return;
        }
        
        CrosstabMeasureNode measureNode = (CrosstabMeasureNode)getPaletteItem().getData();
        
        if (!(getScene() instanceof CrosstabObjectScene) ||
            measureNode.getCrosstab() != ((CrosstabObjectScene)getScene()).getDesignCrosstab() )
        {
            Runnable r = new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(Misc.getMainFrame(), "You can only use a measure inside the crosstab\nin which the measure is defined.","Error", JOptionPane.WARNING_MESSAGE);
                }
            };
            
            Mutex.EVENT.readAccess(r); 
            return;
        }
        
        super.drop(dtde);
    }

    @Override
    public JRDesignElement createReportElement(JasperDesign jd)
    {
        JRDesignTextField element = (JRDesignTextField)super.createReportElement( jd );
        
        CrosstabMeasureNode measureNode = (CrosstabMeasureNode)getPaletteItem().getData();
        
        ((JRDesignExpression)element.getExpression()).setText("$V{"+ measureNode.getMeasure().getName() + "}");
        setMatchingClassExpression(
                ((JRDesignExpression)element.getExpression()),
                measureNode.getMeasure().getValueClassName(), 
                true);
        
        return element;
    }

}
