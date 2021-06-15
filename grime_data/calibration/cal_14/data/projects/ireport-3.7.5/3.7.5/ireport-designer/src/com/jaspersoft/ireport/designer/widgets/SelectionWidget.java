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
package com.jaspersoft.ireport.designer.widgets;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.borders.ElementSelectedBorder;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class SelectionWidget extends Widget {

    

    
    protected ElementSelectedBorder SELECTED_BORDER = new ElementSelectedBorder();



    private int status = 0;
    /**
     * This is the widget virtually selected using this widget.
     */
    protected JRDesignElementWidget realWidget = null;

    public JRDesignElementWidget getRealWidget() {
        return realWidget;
    }
    /**
     *  Widget w is the widget to proxy.
     */
    public SelectionWidget(AbstractReportObjectScene scene, JRDesignElementWidget w) {
        super(scene);
        this.realWidget = w;
        setBorder(SELECTED_BORDER);
        updateBounds();

        this.realWidget.getElement().getEventSupport().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {

                if (SelectionWidget.this.isVisible() &&
                    (evt.getPropertyName().equals( JRDesignElement.PROPERTY_X) ||
                     evt.getPropertyName().equals( JRDesignElement.PROPERTY_Y) ||
                     evt.getPropertyName().equals( JRDesignElement.PROPERTY_WIDTH) ||
                     evt.getPropertyName().equals( JRDesignElement.PROPERTY_HEIGHT)))
                {
                    updateStatus();
                }
            }
        });
    }

    public void updateBounds()
    {
        Insets insets = getBorder().getInsets();
        Rectangle r = realWidget.getPreferredBounds();
        r.x -= insets.left;
        r.y -= insets.top;
        r.width += insets.left + insets.right;
        r.height += insets.top + insets.bottom;
        
        setPreferredBounds(r);
        Point p = realWidget.getPreferredLocation();
        
        setPreferredLocation(p);

        try {
            updateStatus();
            
        } catch (Exception ex) {}
    }


    public void updateStatus()
    {
        int newStatus = findWidgetStatus();
        if (newStatus != getStatus())
        {
            setStatus(newStatus);
        }
    }

    private int findWidgetStatus() {


        AbstractReportObjectScene theScene = (AbstractReportObjectScene)getRealWidget().getScene();
        // 1. find if this widget is out of bounds...
        JRDesignElement element = getRealWidget().getElement();

        
        // 2. find if this widget shares space with another...
        List<Widget> widgets = theScene.getElementsLayer().getChildren();

        Rectangle r0 = getElementWidgetBouns(getRealWidget());

        boolean intersect = false;
        synchronized(theScene)
        {
            for (Widget w2 : widgets)
            {
                if (w2 != getRealWidget() &&
                    w2 instanceof JRDesignElementWidget)
                {
                    JRDesignElementWidget dew = (JRDesignElementWidget)w2;

                    Rectangle r1 = getElementWidgetBouns(dew);

                    if (r0.contains(r1))
                    {
                        return ElementSelectedBorder.STATUS_TOTAL_OVERLAP;
                    }
                    else if (r0.intersects(r1))
                    {
                        intersect = true;
                    }
                }
            }
        }

        if (intersect) return ElementSelectedBorder.STATUS_PARTIAL_OVERLAP;
       
        List<JRDesignElement> selection = theScene.getSelectionManager().getSelectedElements();

        if (selection == null || selection.isEmpty()) return ElementSelectedBorder.STATUS_NONE;


        if (selection.get(0) == element)
        {
            return ElementSelectedBorder.STATUS_PRIMARY_SELECTION;
        }
        if (selection.contains(element))
        {
            return ElementSelectedBorder.STATUS_SECONDARY_SELECTION;
        }



        /*
        // The scene has not an ordered selection, we need to check in
        // the component for an Explorer manager looking for ElementNode(s) selected...
        if (getRealWidget().getScene().getView() != null)
        {
            ExplorerManager manager = ExplorerManager.find( getRealWidget().getScene().getView());
            Node[] nodes = manager.getSelectedNodes();

            List<JRDesignElement> elements = new ArrayList<JRDesignElement>();
            for (int i=0; i<nodes.length; ++i)
            {
                if (nodes[i] instanceof ElementNode)
                {
                    elements.add(((ElementNode)nodes[i]).getElement()) ;
                }
            }

            if (elements.size() > 0 && elements.get(0) == element)
            {
                return ElementSelectedBorder.STATUS_PRIMARY_SELECTION;
            }
            else if (elements.contains(element))
            {
                return ElementSelectedBorder.STATUS_SECONDARY_SELECTION;
            }
            
        }
        */
        /*
        Set selection = ((AbstractReportObjectScene)getRealWidget().getScene()).getSelectedObjects();
        

        
        if (selection.contains(element) )
        {
            if (selection.iterator().next() == element)
            {
                // Primary selection...
                
            }

            return SELECTED_BORDER.STATUS_SECONDARY_SELECTION;
        }
        */
        return ElementSelectedBorder.STATUS_NONE;
    }

    protected Rectangle getElementWidgetBouns(JRDesignElementWidget w)
    {
        JRDesignElement element = w.getElement();

        Point p0 = null; //w.getLocation();
        if (p0 == null)
        {
            p0 = ModelUtils.getParentLocation(((AbstractReportObjectScene)w.getScene()).getJasperDesign(), element, w);
//            if (p0.x == 0 && p0.y == 0)
//            {
//                // this is a very strange case... check if this element belongs to
//                // a custom component...
//                if (getScene() instanceof ReportObjectScene)
//                {
//                    JRDesignElementWidget owner = ((ReportObjectScene)getScene()).findCustomComponentOwner(element);
//                    if (owner != null)
//                    {
//                        p0 = ModelUtils.getParentLocation(((AbstractReportObjectScene)w.getScene()).getJasperDesign(), owner.getElement());
//                        p0.x += owner.getElement().getX();
//                        p0.y += owner.getElement().getY();
//                    }
//                }
//            }


            p0.x += element.getX();
            p0.y += element.getY();
        }
        Rectangle r0 = new Rectangle( p0.x, p0.y, element.getWidth(), element.getHeight());

        return r0;
    }
    
    @Override
    protected Cursor getCursorAt(Point localLocation) {
        Rectangle bounds = getBounds ();
        Insets insets = getBorder().getInsets();
        if (new Rectangle (bounds.x, bounds.y, insets.left, insets.top).contains (localLocation))
            return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
        else if (new Rectangle (bounds.x + (bounds.width/2) - insets.top/2,  bounds.y , insets.top, insets.top).contains (localLocation))
            return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
        else if (new Rectangle (bounds.x+bounds.width-insets.right, bounds.y, insets.right, insets.top).contains (localLocation))
            return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
        else if (new Rectangle (bounds.x, bounds.y+bounds.height-insets.bottom, insets.left, insets.bottom).contains (localLocation))
            return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
        else if (new Rectangle (bounds.x + (bounds.width/2) - insets.bottom/2, bounds.y+bounds.height-insets.bottom, insets.bottom, insets.bottom).contains (localLocation))
            return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
        else if (new Rectangle (bounds.x + bounds.width-insets.right, bounds.y+bounds.height-insets.bottom, insets.left, insets.top).contains (localLocation))
            return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
        else if (new Rectangle (bounds.x, bounds.y + (bounds.height/2) - insets.left/2, insets.left, insets.left).contains (localLocation))
            return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
        else if (new Rectangle (bounds.x + bounds.width - insets.right, bounds.y + (bounds.height/2) - insets.right/2, insets.right, insets.right).contains (localLocation))
            return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
        return super.getCursorAt(localLocation);
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
        SELECTED_BORDER.setStatus(status);
        this.revalidate(true);
        this.getScene().revalidate();
    }


}
