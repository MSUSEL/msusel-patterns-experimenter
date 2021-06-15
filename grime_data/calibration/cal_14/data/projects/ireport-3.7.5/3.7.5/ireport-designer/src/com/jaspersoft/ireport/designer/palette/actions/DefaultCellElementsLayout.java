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
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.fill.JRMeasuredText;
import net.sf.jasperreports.engine.fill.JRTextMeasurer;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;
import net.sf.jasperreports.engine.util.JRTextMeasurerUtil;
import org.netbeans.api.visual.animator.AnimatorEvent;
import org.netbeans.api.visual.animator.AnimatorListener;
import org.netbeans.api.visual.animator.SceneAnimator;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class DefaultCellElementsLayout {

    private static DefaultCellElementsLayout instance;
    
    public static DefaultCellElementsLayout getInstance()
    {
        if (instance == null)
        {
            instance = new DefaultCellElementsLayout();
        }
        return instance;
    }
    
    public static void doLayout(final JRDesignCellContents cell, final CrosstabObjectScene scene)
    {
                SwingUtilities.invokeLater(new Runnable() {

                     public void run() {
                        getInstance().organizeCellElements(cell, scene);
                     }
                 });
    }
    
    private void organizeCellElements(JRDesignCellContents cell, CrosstabObjectScene sc) {
        
        int cW = cell.getWidth();
        int cH = cell.getHeight();
        List children = cell.getChildren();
        int y = 0;
        if (children.size() == 0) return;
        int h = cH/children.size();
        
        SceneAnimator animator = new SceneAnimator(sc);
        CellAnimatorListener listener = new CellAnimatorListener();
        animator.getPreferredBoundsAnimator().addAnimatorListener(listener);
        animator.getPreferredLocationAnimator().addAnimatorListener(listener);      
        listener.addAnimation(); // bounds
        listener.addAnimation(); // location
        
        // 1 Calculate new elements size and location..
        for (int i=0; i<children.size(); ++i)
        {
            if (children.get(i) instanceof JRDesignElement)
            {
                JRDesignElement element = (JRDesignElement)children.get(i);
                listener.getNewSizes().put(element, new Rectangle(0, i*h, cW, h));
            }
        }
        
        for (Iterator iter = listener.getNewSizes().keySet().iterator(); iter.hasNext(); )
        {
            JRDesignElement element = (JRDesignElement)iter.next();
            Rectangle rectangle = listener.getNewSizes().get(element);
            Widget w = sc.findWidget(element);
                
            if (w != null)
            {
               Rectangle r = w.getPreferredBounds();
               r.width = rectangle.width;
               r.height = rectangle.height;
               animator.animatePreferredBounds(w, r);
                    
                Point p = new Point( w.getLocation() );
                p.x -= element.getX() + rectangle.x;
                p.y += -(element.getY()) + rectangle.y;
                    
                animator.animatePreferredLocation(w, p)  ;
            }
            
        }
    }
    
    private class CellAnimatorListener implements AnimatorListener
    {

        private HashMap<JRDesignElement, Rectangle> newSizes = new HashMap<JRDesignElement, Rectangle>();
        int animations = 0;
        public void addAnimation()
        {
            animations++;
        }
        
        public void animatorStarted(AnimatorEvent arg0) {
        }

        public void animatorReset(AnimatorEvent arg0) {
        }

        public void animatorFinished(AnimatorEvent arg0) {
            animations--;
            if (animations == 0)
            {
                for (Iterator iter = getNewSizes().keySet().iterator(); iter.hasNext(); )
                {
                    JRDesignElement element = (JRDesignElement)iter.next();
                    Rectangle rectangle = getNewSizes().get(element);
                    element.setX( rectangle.x);
                    element.setY( rectangle.y);
                    element.setWidth( rectangle.width);
                    element.setHeight( rectangle.height);
                    
                    // Calculate the best font height...
                    if (element instanceof JRDesignTextElement)
                    {
                        JRStyledTextParser styledTextParser = new JRStyledTextParser();
                        JRDesignTextElement dte = (JRDesignTextElement)element;
                        dte.setFontSize(null);
                        for (int i=dte.getFontSize()-1; i>1 ; --i)
                        {
                                String text = "test";
                                
                                // Convert the element in a print element...
                                Map attributes = JRFontUtil.getAttributes(new HashMap(), dte, Locale.getDefault());
                                
                                JRStyledText styledText = 
                                    styledTextParser.getStyledText(
                                    attributes, 
                                    text, 
                                    JRCommonText.MARKUP_STYLED_TEXT.equals(dte.getMarkup())//FIXMEMARKUP only static styled text appears on preview. no other markup
				);

                                
                                JRTextMeasurer measurer = JRTextMeasurerUtil.createTextMeasurer(dte);
                                JRMeasuredText measuredText = measurer.measure(  styledText, 0, dte.getHeight(), true);
                                
                                if  (measuredText.getTextHeight() > dte.getHeight())
                                {
                                    if (i>1)
                                    {
                                        dte.setFontSize(i);
                                    }
                                    else
                                    {
                                        break;
                                    }
                                }
                                else
                                {
                                    break;
                                }
                        }
                    }
                }
            }
        }

        public void animatorPreTick(AnimatorEvent arg0) {
        }

        public void animatorPostTick(AnimatorEvent arg0) {
        }

        public HashMap<JRDesignElement, Rectangle> getNewSizes() {
            return newSizes;
        }

        
    }
}
